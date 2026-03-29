package xml_parsing;

import csv_parsing.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Парсер XML с использованием технологии XPath.
 */
public class XPathXmlParser {

    private static final Logger LOGGER = Logger.getLogger(XPathXmlParser.class.getName());

    /**
     * Читает XML файл и возвращает список студентов, используя XPath запросы.
     *
     * @param filePath путь к XML файлу
     * @return список объектов Student
     */
    public static List<Student> parseAllStudents(Path filePath) {
        return executeXPathQuery(filePath, "//student");
    }

    /**
     * Делает запрос к XML и возвращает только тех студентов, у которых оценка выше указанной.
     * Это прямая реализация требования "Query students with grade > X" с помощью XPath.
     *
     * @param filePath путь к XML файлу
     * @param minGrade минимальная оценка
     * @return отфильтрованный список объектов Student
     */
    public static List<Student> parseStudentsByMinGrade(Path filePath, double minGrade) {
        String expression = "//student[grade > " + minGrade + "]";
        return executeXPathQuery(filePath, expression);
    }

    /**
     * Приватный метод для выполнения любого XPath запроса и сборки списка студентов.
     */
    private static List<Student> executeXPathQuery(Path filePath, String expressionStr) {
        List<Student> students = new ArrayList<>();

        try (InputStream is = Files.newInputStream(filePath)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expression = xpath.compile(expressionStr);

            NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                int id = Integer.parseInt(xpath.evaluate("@id", node));
                String name = xpath.evaluate("name", node).trim();
                int age = Integer.parseInt(xpath.evaluate("age", node).trim());
                double grade = Double.parseDouble(xpath.evaluate("grade", node).trim());

                students.add(new Student(id, name, age, grade));
            }
            LOGGER.info("XPath Парсер: по запросу '" + expressionStr + "' найдено " + students.size() + " студентов.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "XPath Парсер: Ошибка при обработке XML файла", e);
        }
        return students;
    }

    /**
     * Метод для вывода результатов работы парсера.
     */
    public static void processAndFilter(Path filePath, double minGrade) {
        System.out.println("\n=== XPath Парсер: Все студенты ===");
        List<Student> allStudents = parseAllStudents(filePath);
        allStudents.forEach(System.out::println);

        System.out.println("\n=== XPath Парсер: Студенты с оценкой выше " + minGrade + " (Запрос через XPath) ===");
        List<Student> topStudents = parseStudentsByMinGrade(filePath, minGrade);
        topStudents.forEach(System.out::println);
    }
}