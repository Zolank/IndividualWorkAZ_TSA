package csv_parsing;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Сервис для чтения CSV файлов различными способами.
 */
public class CsvReaderService {

    private static void printStats(String methodName, List<Student> students) {
        System.out.println("=== Чтение с помощью: " + methodName + " ===");
        students.forEach(System.out::println);
        System.out.println("Количество записей: " + students.size());

        double averageGrade = students.stream()
                .mapToDouble(Student::getGrade)
                .average()
                .orElse(0.0);
        System.out.printf("Средний балл: %.2f\n\n", averageGrade);
    }

    /**
     * 1. Чтение с использованием String.split()
     */
    public static void readUsingSplit(Path filePath) {
        // REFACTORING: Использован try-with-resources для автоматического закрытия Stream.
        // REFACTORING: Явно указана кодировка StandardCharsets.UTF_8.
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            List<Student> students = lines.skip(1)
                    .map(line -> {
                        String[] parts = line.split(",");
                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].replace("\"", "").trim();
                        }
                        return new Student(
                                Integer.parseInt(parts[0]),
                                parts[1],
                                Integer.parseInt(parts[2]),
                                Double.parseDouble(parts[3])
                        );
                    }).toList();
            printStats("String.split()", students);
        } catch (Exception e) {
            System.err.println("Ошибка при чтении через split: " + e.getMessage());
        }
    }

    /**
     * 2. Чтение с использованием Scanner
     */
    public static void readUsingScanner(Path filePath) {
        List<Student> students = new ArrayList<>();
        // REFACTORING: Использован try-with-resources для автоматического закрытия Scanner.
        // REFACTORING: Явно указана кодировка StandardCharsets.UTF_8.
        try (Scanner scanner = new Scanner(filePath, StandardCharsets.UTF_8)) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].replace("\"", "").trim();
                }
                students.add(new Student(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        Integer.parseInt(parts[2]),
                        Double.parseDouble(parts[3])
                ));
            }
            printStats("Scanner", students);
        } catch (Exception e) {
            System.err.println("Ошибка при чтении через Scanner: " + e.getMessage());
        }
    }

    /**
     * 3. Чтение с использованием OpenCSV
     */
    public static void readUsingOpenCsv(Path filePath) {
        List<Student> students = new ArrayList<>();
        // REFACTORING: Использован try-with-resources для BufferedReader и CSVReader.
        // REFACTORING: Явно указана кодировка StandardCharsets.UTF_8.
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                students.add(new Student(
                        Integer.parseInt(line[0]),
                        line[1],
                        Integer.parseInt(line[2]),
                        Double.parseDouble(line[3])
                ));
            }
            printStats("OpenCSV", students);
        } catch (Exception e) {
            System.err.println("Ошибка при чтении через OpenCSV: " + e.getMessage());
        }
    }

    /**
     * 4. Чтение с использованием Apache Commons CSV
     */
    public static void readUsingApacheCommons(Path filePath) {
        List<Student> students = new ArrayList<>();

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        // REFACTORING: Использован try-with-resources для автоматического закрытия парсера.
        // REFACTORING: Явно указана кодировка StandardCharsets.UTF_8.
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(reader, format)) {

            for (CSVRecord record : parser) {
                students.add(new Student(
                        Integer.parseInt(record.get("id")),
                        record.get("name"),
                        Integer.parseInt(record.get("age")),
                        Double.parseDouble(record.get("grade"))
                ));
            }
            printStats("Apache Commons CSV", students);
        } catch (Exception e) {
            System.err.println("Ошибка при чтении через Apache Commons CSV: " + e.getMessage());
        }
    }
}