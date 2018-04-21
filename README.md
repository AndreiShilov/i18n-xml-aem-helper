# i18n-xml-aem-helper


## For what

To avoid pain of manually addition of new i18n keys with translations during aem development

### Available parameters

1. ```processor.type``` - to choose available processor type
    * processor.type=xml
    * processor.type=html
    * processor.type=csv
2. ```dictionary.base.name``` if you want to set utp dictionary base name
    * dictionary.base.name=dictionaryBaseName
3. ```xml.folder.path``` - path to your folder with dictionaries
    * for example xml.folder.path=/path/to/the/folder -> under ```/path/to/the/folder``` you should have your en.xml, de.xml ...
4. ```csv.file``` - path to csv file for csv processor
    * csv.file=/path/to/the/file.csv
5. ```html.url``` - url path for html processor
    * html.url=https://github.com/AndreiShilov/i18n-xml-aem-helper/blob/master/example/ExampleTable.md
6. ```html.login``` and ```html.password``` will be used for basic authorization header in case your page is not public (your confluence for example)


### HOW-TO

#### CSV

Csv format : ```key,en||no,de,fr,it```

* With base name

```
java -jar app processor.type=csv csv.file=<PATH_TO_PROJECT>/i18ndictionaryhelper/example/example.csv xml.folder.path=<PATH_TO_PROJECT>/i18ndictionaryhelper/example dictionary.base.name=dictionaryBaseName
```

* Without base name

```
java -jar app processor.type=csv csv.file=<PATH_TO_PROJECT>/i18ndictionaryhelper/example/example.csv xml.folder.path=<PATH_TO_PROJECT>/i18ndictionaryhelper/example
```
 
#### HTML

HTML format: just ana html table. Example could be found -> [here](https://github.com/AndreiShilov/i18n-xml-aem-helper/blob/master/example/ExampleTable.md)

* Without credentials 

```
java -jar app processor.type=html xml.folder.path=<PATH_TO_PROJECT>/i18ndictionaryhelper/example html.url=https://github.com/AndreiShilov/i18n-xml-aem-helper/blob/master/example/ExampleTable.md

```

* With credentials

```
java -jar app processor.type=html xml.folder.path=<PATH_TO_PROJECT>/i18ndictionaryhelper/example html.url=https://github.com/AndreiShilov/i18n-xml-aem-helper/blob/master/example/ExampleTable.md html.login=myLogin html.password=myPassword

```
 
### Resulting XML

* Without base name

```
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
          xmlns:nt="http://www.jcp.org/jcr/nt/1.0"
          jcr:language="en"
          jcr:mixinTypes="[mix:language]"
          jcr:primaryType="sling:Folder">
    <app.key.1
            jcr:mixinTypes="[sling:Message]"
            jcr:primaryType="nt:folder"
            sling:message="Key 1"/>
</jcr:root>            
```

* With base name

```
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
          xmlns:nt="http://www.jcp.org/jcr/nt/1.0"
          jcr:language="en"
          jcr:mixinTypes="[mix:language]"
          sling:basename="test"
          jcr:primaryType="sling:Folder">
    <app.key.1
            jcr:mixinTypes="[sling:Message]"
            jcr:primaryType="nt:folder"
            sling:message="Key 1"/>
</jcr:root>            
```