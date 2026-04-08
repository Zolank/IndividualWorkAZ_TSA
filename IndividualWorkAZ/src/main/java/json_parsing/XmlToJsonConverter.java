package json_parsing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для конвертации XML файла в JSON формат.
 */
public class XmlToJsonConverter {

    private static final Logger LOGGER = Logger.getLogger(XmlToJsonConverter.class.getName());

    /**
     * Конвертирует XML в JSON и выводит в консоль.
     *
     * @param xmlFilePath путь к исходному XML файлу
     */
    @SuppressWarnings("unchecked")
    public static void convert(Path xmlFilePath) {

        // REFACTORING: Заменен старый FileInputStream на современный NIO.2 API (Files.newInputStream).
        // REFACTORING: Использован try-with-resources для автоматического закрытия InputStream.
        try (InputStream is = Files.newInputStream(xmlFilePath)) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            document.getDocumentElement().normalize();

            JSONArray jsonArray = new JSONArray();
            NodeList nodeList = document.getElementsByTagName("student");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    JSONObject studentJson = new JSONObject();

                    studentJson.put("id", Integer.parseInt(element.getAttribute("id")));
                    studentJson.put("name", element.getElementsByTagName("name").item(0).getTextContent());
                    studentJson.put("age", Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent()));
                    studentJson.put("grade", Double.parseDouble(element.getElementsByTagName("grade").item(0).getTextContent()));

                    jsonArray.add(studentJson);
                }
            }

            String jsonResult = jsonArray.toJSONString();
            System.out.println("\n=== Конвертация XML -> JSON ===");
            System.out.println(jsonResult);

            JSONParser validator = new JSONParser();
            validator.parse(jsonResult);
            LOGGER.info("Структура сконвертированного JSON успешно валидирована.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка конвертации: Невалидный XML файл или ошибка структуры.", e);
        }
    }
}