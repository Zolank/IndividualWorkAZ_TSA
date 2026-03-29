package json_parsing;

import csv_parsing.Student;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для обработки JSON файлов (чтение, модификация, сохранение).
 */
public class JsonProcessor {

    private static final Logger LOGGER = Logger.getLogger(JsonProcessor.class.getName());

    /**
     * Читает JSON, парсит в объекты, модифицирует и сохраняет обратно.
     *
     * @param filePath путь к JSON файлу
     */
    @SuppressWarnings("unchecked") // Подавляем предупреждения библиотеки json-simple
    public static void processAndModifyJson(Path filePath) {
        JSONParser parser = new JSONParser();
        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            List<Student> students = new ArrayList<>();

            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;

                int id = ((Number) jsonObj.get("id")).intValue();
                String name = (String) jsonObj.get("name");
                int age = ((Number) jsonObj.get("age")).intValue();
                double grade = ((Number) jsonObj.get("grade")).doubleValue();

                students.add(new Student(id, name, age, grade));
            }

            System.out.println("\n=== JSON: Исходные данные ===");
            students.forEach(System.out::println);

            // New sdudent
            JSONObject newStudent = new JSONObject();
            newStudent.put("id", 6);
            newStudent.put("name", "Елена Васильева");
            newStudent.put("age", 21);
            newStudent.put("grade", 91.5);
            jsonArray.add(newStudent);
            LOGGER.info("JSON: Добавлен новый студент (Елена Васильева).");

            // Change sdudent
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
                if (((Number) jsonObj.get("id")).intValue() == 1) {
                    jsonObj.put("grade", 99.9);
                    LOGGER.info("JSON: Изменена оценка студента с id=1.");
                    break;
                }
            }

            try (Writer writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                writer.write(jsonArray.toJSONString());
                LOGGER.info("JSON: Обновленный файл успешно сохранен.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при обработке JSON файла. Возможно, файл невалиден.", e);
        }
    }
}