package csv_parsing;

import xml_parsing.DomXmlParser;
import xml_parsing.SaxXmlParser;
import xml_parsing.XPathXmlParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Главный класс для запуска и проверки записи CSV.
 */
public class Main {
    /**
     * Точка входа в программу.
     */
    public static void main(String[] args) {

        List<Student> students = List.of(
                new Student(1, "Иван Иванов", 20, 85.5),
                new Student(2, "Анна Смирнова", 19, 92.0),
                new Student(3, "Петр Петров", 21, 78.0),
                new Student(4, "Мария Иванова", 22, 88.5),
                new Student(5, "Алексей Сидоров", 20, 95.0)
        );

        Path resourcesDir = Paths.get("src/main/resources");
        try {
            if (Files.notExists(resourcesDir)) {
                Files.createDirectories(resourcesDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Path openCsvPath = resourcesDir.resolve("students_opencsv.csv");
        Path apacheCsvPath = resourcesDir.resolve("students_apache.csv");

        OpenCsvWriter.writeStudents(students, openCsvPath);
        ApacheCommonsCsvWriter.writeStudents(students, apacheCsvPath);

        System.out.println("=== НАЧАЛО ТЕСТИРОВАНИЯ ЧТЕНИЯ ===");
        CsvReaderService.readUsingSplit(openCsvPath);
        CsvReaderService.readUsingScanner(openCsvPath);
        CsvReaderService.readUsingOpenCsv(openCsvPath);
        CsvReaderService.readUsingApacheCommons(openCsvPath);

        // DOM call
        Path xmlPath = Paths.get("src/main/resources/students.xml");
        System.out.println("=== ТЕСТИРОВАНИЕ XML (DOM) ===");
        DomXmlParser.processAndFilter(xmlPath, 90.0);

        // SAX call
        System.out.println("=== ТЕСТИРОВАНИЕ XML (SAX) ===");
        SaxXmlParser.processAndFilter(xmlPath, 90.0);

        // XPath call
        System.out.println("=== ТЕСТИРОВАНИЕ XML (XPath) ===");
        XPathXmlParser.processAndFilter(xmlPath, 90.0);

        // Импортируй класс JsonProcessor, если нужно
        System.out.println("\n=== ТЕСТИРОВАНИЕ JSON ===");
        Path jsonPath = Paths.get("src/main/resources/students.json");
        json_parsing.JsonProcessor.processAndModifyJson(jsonPath);
        // Запуск конвертера
        json_parsing.XmlToJsonConverter.convert(xmlPath);
    }

}
