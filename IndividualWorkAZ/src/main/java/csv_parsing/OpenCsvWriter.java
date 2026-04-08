package csv_parsing;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Утилитный класс для записи данных студентов в CSV файл с помощью OpenCSV.
 */
public class OpenCsvWriter {

    /**
     * Записывает список студентов в CSV файл.
     *
     * @param students список студентов для записи
     * @param filePath путь к файлу для сохранения
     */
    public static void writeStudents(List<Student> students, Path filePath) {

        // REFACTORING: Использован try-with-resources для безопасного закрытия потоков.
        // REFACTORING: Явно указана кодировка StandardCharsets.UTF_8.
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);

             // REFACTORING: Заменен устаревший конструктор CSVWriter на современный ICSVWriter через CSVWriterBuilder.
             ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                     .withSeparator(ICSVWriter.DEFAULT_SEPARATOR)
                     .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER)
                     .withEscapeChar(ICSVWriter.DEFAULT_ESCAPE_CHARACTER)
                     .withLineEnd(ICSVWriter.DEFAULT_LINE_END)
                     .build()) {

            csvWriter.writeNext(new String[]{"id", "name", "age", "grade"});

            for (Student student : students) {
                String[] data = {
                        String.valueOf(student.getId()),
                        student.getName(),
                        String.valueOf(student.getAge()),
                        String.valueOf(student.getGrade())
                };
                csvWriter.writeNext(data);
            }
            System.out.println("Файл успешно записан через OpenCSV: " + filePath.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("Ошибка при записи файла через OpenCSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}