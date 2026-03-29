package json_parsing;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonParsingTest {

    @Test
    public void testJsonProcessing() {
        Path jsonFilePath = Paths.get("src/main/resources/students.json");
        Assert.assertTrue(Files.exists(jsonFilePath), "JSON файл для тестов не найден!");
        JsonProcessor.processAndModifyJson(jsonFilePath);
    }

    @Test
    public void testXmlToJsonConversion() {
        Path xmlFilePath = Paths.get("src/main/resources/students.xml");
        Assert.assertTrue(Files.exists(xmlFilePath), "XML файл для конвертации не найден!");
        XmlToJsonConverter.convert(xmlFilePath);
    }
}