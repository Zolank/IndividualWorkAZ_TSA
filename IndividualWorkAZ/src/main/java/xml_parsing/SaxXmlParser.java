package xml_parsing;

import csv_parsing.Student;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Парсер XML с использованием технологии SAX.
 */
public class SaxXmlParser {

    private static final Logger LOGGER = Logger.getLogger(SaxXmlParser.class.getName());

    /**
     * Читает XML файл и возвращает список студентов.
     *
     * @param filePath путь к XML файлу
     * @return список объектов Student
     */
    public static List<Student> parseStudents(Path filePath) {
        try (InputStream is = Files.newInputStream(filePath)) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            StudentHandler handler = new StudentHandler();
            saxParser.parse(is, handler);

            List<Student> students = handler.getStudents();
            LOGGER.info("SAX Парсер: успешно прочитано " + students.size() + " студентов.");
            return students;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SAX Парсер: Ошибка при чтении или парсинге XML. Возможно, файл поврежден.", e);
            return new ArrayList<>(); // Возвращаем пустой список в случае ошибки
        }
    }

    /**
     * Выводит всех студентов и делает запрос по минимальной оценке (grade > X).
     *
     * @param filePath путь к файлу
     * @param minGrade минимальный балл для фильтрации
     */
    public static void processAndFilter(Path filePath, double minGrade) {
        List<Student> students = parseStudents(filePath);

        if (students.isEmpty()) {
            System.out.println("Список студентов пуст или произошла ошибка чтения.");
            return;
        }

        System.out.println("\n=== SAX Парсер: Все студенты ===");
        students.forEach(System.out::println);

        System.out.println("\n=== SAX Парсер: Студенты с оценкой выше " + minGrade + " ===");
        students.stream()
                .filter(s -> s.getGrade() > minGrade)
                .forEach(System.out::println);
    }

    /**
     * Внутренний класс-обработчик событий SAX.
     */
    private static class StudentHandler extends DefaultHandler {
        private final List<Student> students = new ArrayList<>();
        private StringBuilder currentText;

        private int id;
        private String name;
        private int age;
        private double grade;

        public List<Student> getStudents() {
            return students;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            currentText = new StringBuilder();
            if ("student".equals(qName)) {
                id = Integer.parseInt(attributes.getValue("id"));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (currentText != null) {
                currentText.append(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            String text = currentText.toString().trim();
            switch (qName) {
                case "name":
                    name = text;
                    break;
                case "age":
                    age = Integer.parseInt(text);
                    break;
                case "grade":
                    grade = Double.parseDouble(text);
                    break;
                case "student":
                    students.add(new Student(id, name, age, grade));
                    break;
            }
        }
    }
}