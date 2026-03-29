package csv_parsing;

import com.opencsv.CSVWriter;
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
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);

             CSVWriter csvWriter = new CSVWriter(writer,
                     CSVWriter.DEFAULT_SEPARATOR,
                     CSVWriter.NO_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {

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