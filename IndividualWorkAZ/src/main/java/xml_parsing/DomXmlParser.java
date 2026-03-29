package xml_parsing;

import csv_parsing.Student; // Импортируем нашу модель данных
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Парсер XML с использованием технологии DOM.
 */
public class DomXmlParser {

    private static final Logger LOGGER = Logger.getLogger(DomXmlParser.class.getName());

    /**
     * Читает XML файл и возвращает список студентов.
     *
     * @param filePath путь к XML файлу
     * @return список объектов Student
     */
    public static List<Student> parseStudents(Path filePath) {
        List<Student> students = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try (InputStream is = Files.newInputStream(filePath)) {

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("student");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int id = Integer.parseInt(element.getAttribute("id"));

                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                    double grade = Double.parseDouble(element.getElementsByTagName("grade").item(0).getTextContent());

                    students.add(new Student(id, name, age, grade));
                }
            }
            LOGGER.info("DOM Парсер: успешно прочитано " + students.size() + " студентов.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "DOM Парсер: Ошибка при чтении или парсинге XML. Возможно, файл поврежден.", e);
        }
        return students;
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

        System.out.println("\n=== DOM Парсер: Все студенты ===");
        students.forEach(System.out::println);

        System.out.println("\n=== DOM Парсер: Студенты с оценкой выше " + minGrade + " ===");
        students.stream()
                .filter(s -> s.getGrade() > minGrade)
                .forEach(System.out::println);
    }


}