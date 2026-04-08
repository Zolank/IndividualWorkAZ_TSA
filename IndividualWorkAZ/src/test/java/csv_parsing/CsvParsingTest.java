package csv_parsing;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvParsingTest {

    private Path csvFilePath;

    @BeforeClass
    public void setUp() {
        csvFilePath = Paths.get("src/main/resources/students_opencsv.csv");
        Assert.assertTrue(Files.exists(csvFilePath), "CSV файл для тестов не найден!");
    }
//Smoke
    @Test
    public void testReadUsingSplit() {
        CsvReaderService.readUsingSplit(csvFilePath);
    }

    @Test
    public void testReadUsingScanner() {
        CsvReaderService.readUsingScanner(csvFilePath);
    }

    @Test
    public void testReadUsingOpenCsv() {
        CsvReaderService.readUsingOpenCsv(csvFilePath);
    }

    @Test
    public void testReadUsingApacheCommons() {
        CsvReaderService.readUsingApacheCommons(csvFilePath);
    }
//Unit
    @Test
    public void testStudentModel() {
        Student student = new Student(1, "Тест", 20, 95.5);

        Assert.assertEquals(student.getId(), 1);
        Assert.assertEquals(student.getName(), "Тест");

        student.setAge(21);
        student.setGrade(100.0);

        Assert.assertEquals(student.getAge(), 21);
        Assert.assertEquals(student.getGrade(), 100.0);
        Assert.assertNotNull(student.toString());
    }

    @Test
    public void testCsvWriters() {
        List<Student> testStudents = java.util.List.of(
                new Student(99, "Тестовый Студент", 25, 88.8)
        );
        Path openCsvTestPath = Paths.get("src/main/resources/test_out_open.csv");
        Path apacheCsvTestPath = Paths.get("src/main/resources/test_out_apache.csv");

        OpenCsvWriter.writeStudents(testStudents, openCsvTestPath);
        ApacheCommonsCsvWriter.writeStudents(testStudents, apacheCsvTestPath);

        Assert.assertTrue(Files.exists(openCsvTestPath));
        Assert.assertTrue(Files.exists(apacheCsvTestPath));
    }

}