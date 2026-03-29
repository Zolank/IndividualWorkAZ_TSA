package xml_parsing;

import csv_parsing.Student;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class XmlParsingTest {

    private Path xmlFilePath;

    @BeforeClass
    public void setUp() {
        xmlFilePath = Paths.get("src/main/resources/students.xml");
        Assert.assertTrue(Files.exists(xmlFilePath), "XML файл для тестов не найден!");
    }

    @Test
    public void testDomParser() {
        List<Student> students = DomXmlParser.parseStudents(xmlFilePath);
        Assert.assertFalse(students.isEmpty(), "DOM парсер вернул пустой список!");
        DomXmlParser.processAndFilter(xmlFilePath, 90.0);
    }

    @Test
    public void testSaxParser() {
        List<Student> students = SaxXmlParser.parseStudents(xmlFilePath);
        Assert.assertFalse(students.isEmpty(), "SAX парсер вернул пустой список!");
        SaxXmlParser.processAndFilter(xmlFilePath, 90.0);
    }

    @Test
    public void testXPathParser() {
        List<Student> allStudents = XPathXmlParser.parseAllStudents(xmlFilePath);
        Assert.assertFalse(allStudents.isEmpty(), "XPath парсер вернул пустой список!");

        List<Student> filteredStudents = XPathXmlParser.parseStudentsByMinGrade(xmlFilePath, 90.0);
        Assert.assertNotNull(filteredStudents, "Отфильтрованный список не должен быть null");
    }
}