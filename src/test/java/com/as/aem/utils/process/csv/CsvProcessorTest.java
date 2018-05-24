package com.as.aem.utils.process.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.as.aem.utils.FileUtils;
import com.as.aem.utils.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CsvProcessorTest {

    private static final String FAKE_PATH = "/fake/path";

    @Mock
    private FileUtils fileUtils;

    @Test
    @DisplayName("Case when csv path is null")
    public void test1() {
        assertThrows(NullPointerException.class, () -> new CsvProcessor(null, null));
    }


    @Test
    @DisplayName("Case when csv files is not a file")
    public void test2() {
        CsvProcessor spy = spy(new CsvProcessor(FAKE_PATH, fileUtils));
        File fileMock = mock(File.class);

        when(fileMock.isFile()).thenReturn(false);
        when(fileUtils.getFile(FAKE_PATH)).thenReturn(fileMock);

        assertThrows(IllegalArgumentException.class, spy::init);
    }

    @Test
    @DisplayName("Case with FileNotFoundException during file reading")
    public void test3() throws FileNotFoundException {
        CsvProcessor spy = spy(new CsvProcessor(FAKE_PATH, fileUtils));
        File fileMock = mock(File.class);

        when(fileMock.isFile()).thenReturn(true);
        when(fileUtils.getFile(FAKE_PATH)).thenReturn(fileMock);
        when(fileUtils.getFileReader(any(File.class))).thenThrow(new FileNotFoundException());

        spy.init();

        Map<String, Map<String, String>> i18nData = spy.getI18nData();

        assertTrue(i18nData.isEmpty());
    }

    @Test
    @DisplayName("Csv Parsed with not imported 'en' language")
    public void test4() {
        CsvProcessor spy = spy(new CsvProcessor(FAKE_PATH, getFilesUtilsMock("/process/csv/example.csv")));

        spy.init();

        Map<String, Map<String, String>> i18nData = spy.getI18nData();

        assertFalse(i18nData.isEmpty());
        assertEquals(3, i18nData.size(), "Should be only 3 languages");
        assertEquals(6, i18nData.get("fr").size(), "Should be exactly 6 keys for fr");
        assertEquals(6, i18nData.get("de").size(), "Should be exactly 6 keys for de");
        assertEquals(6, i18nData.get("it").size(), "Should be exactly 6 keys for it");
    }

    @Test
    @DisplayName("Csv Parsed with imported 'en' language")
    public void test5() {
        CsvProcessor spy = spy(new CsvProcessor(FAKE_PATH, getFilesUtilsMock("/process/csv/example-en-imported.csv")));

        spy.init();

        Map<String, Map<String, String>> i18nData = spy.getI18nData();

        assertFalse(i18nData.isEmpty());
        assertEquals(4, i18nData.size(), "Should be only 4 languages");

        i18nData.entrySet()
                .forEach(entry -> assertEquals(1, entry.getValue().size(), "Should be exactly 1 key for language " + entry.getValue()));
    }

    private FileUtils getFilesUtilsMock(String path) {
        FileUtils fileUtils = spy(new FileUtils());

        when(fileUtils.getFile(FAKE_PATH)).thenReturn(getTestFile(path));

        return fileUtils;
    }

    private File getTestFile(final String filePath) {
        URL url = this.getClass().getResource(filePath);
        return new File(url.getFile());
    }
}