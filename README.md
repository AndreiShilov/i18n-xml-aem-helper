# i18n-xml-aem-helper


## For what

ToaAvoid pain of manually addition of new i18n keys with translations during aem development

## How To

Easy to use -> ```java -jar app.jar path/to/file.csv path/to/i18n/folder```

#### Example

```java -jar app.jar /Users/andreishilov/git/translations.csv /Users/andreishilov/git/app/app-components/app/src/main/jcr_root/apps/app/i18n```

### csv format

```key,en||no,de,fr,it```

* header values are case sensetive
* ```en||no``` means that we do not want to import en language

### xml format

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
