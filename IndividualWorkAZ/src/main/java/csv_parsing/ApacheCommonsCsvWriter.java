package csv_parsing;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Утилитный класс для записи данных студентов в CSV файл с помощью Apache Commons CSV.
 */
public class ApacheCommonsCsvWriter {

    /**
     * Записывает список студентов в CSV файл.
     *
     * @param students список студентов для записи
     * @param filePath путь к файлу для сохранения
     */
    public static void writeStudents(List<Student> students, Path filePath) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("id", "name", "age", "grade")
                .build();

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {

            for (Student student : students) {
                csvPrinter.printRecord(
                        student.getId(),
                        student.getName(),
                        student.getAge(),
                        student.getGrade()
                );
            }
            System.out.println("Файл успешно записан через Apache Commons CSV: " + filePath.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("Ошибка при записи файла через Apache Commons: " + e.getMessage());
            e.printStackTrace();
        }
    }
}