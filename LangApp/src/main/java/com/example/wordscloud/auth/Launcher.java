package com.example.wordscloud.auth;

import com.example.wordscloud.db.DBCreator;
import com.example.wordscloud.models.AppPrefs;
import javafx.application.Application;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class Launcher {
    private static final String pathToSettings = "preferences.xml";

    public static void main(String[] args) {
        setupDatabase();
        Application.launch(HelloApplication.class, args);
    }

    private static void setupDatabase() {
        if (!preferencesExist())
            createPreferences();

        AppPrefs prefs = getAppPrefs();
        var creator = DBCreator.getInstance();
        creator.setAppPrefs(prefs);
    }

    private static String getPathToSettings() {
        return pathToSettings;
    }

    private static boolean preferencesExist() {
        return new File(getPathToSettings()).exists();
    }

    private static void createPreferences() {
        try {
            // Create a DocumentBuilderFactory
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Create a new Document
            Document doc = docBuilder.newDocument();

            // Create the root element
            Element rootElement = doc.createElement("db_creds");
            doc.appendChild(rootElement);

            // Create driver path element
            Element driverPath = doc.createElement("driver_path");
            driverPath.appendChild(doc.createTextNode("com.mysql.cj.jdbc.Driver"));
            rootElement.appendChild(driverPath);

            // Create driver prefix element
            Element driverPrefix = doc.createElement("driver_prefix");
            driverPrefix.appendChild(doc.createTextNode("jdbc:mysql"));
            rootElement.appendChild(driverPrefix);

            // Create db type element
            Element dbtype = doc.createElement("dbtype");
            dbtype.appendChild(doc.createTextNode("0"));
            rootElement.appendChild(dbtype);

            // Create db type element
            Element database = doc.createElement("database");
            database.appendChild(doc.createTextNode("test_db"));
            rootElement.appendChild(database);

            // Create login element
            Element login = doc.createElement("login");
            login.appendChild(doc.createTextNode("login_example"));
            rootElement.appendChild(login);

            // Create password element
            Element password = doc.createElement("password");
            password.appendChild(doc.createTextNode("password_example"));
            rootElement.appendChild(password);

            // Create host element
            Element host = doc.createElement("host");
            host.appendChild(doc.createTextNode("localhost"));
            rootElement.appendChild(host);

            // Create port element
            Element port = doc.createElement("port");
            port.appendChild(doc.createTextNode("3306"));
            rootElement.appendChild(port);

            // Write the content to an XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(getPathToSettings()));

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private AppPrefs getAppPrefs() {
        try {
            // Specify the file path
            File inputFile = new File(getPathToSettings());

            // Create a DocumentBuilderFactory
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Parse the XML file and get the Document
            Document doc = dBuilder.parse(inputFile);

            // Normalize the XML structure
            doc.getDocumentElement().normalize();

            // Get the root element
            Element rootElement = doc.getDocumentElement();

            AppPrefs appPrefs = new AppPrefs();
            // Get dbtype node
            NodeList list = rootElement.getElementsByTagName("driver_path");
            if (list.getLength() == 0)
                return AppPrefs.getEmpty();

            String driverPath = list.item(0).getTextContent();
            appPrefs.put("driver_path", driverPath);

            // Get dbtype node
            list = rootElement.getElementsByTagName("driver_prefix");
            if (list.getLength() == 0)
                return AppPrefs.getEmpty();

            String driverPrefix = list.item(0).getTextContent();
            appPrefs.put("driver_prefix", driverPrefix);

            // Get dbtype node
            list = rootElement.getElementsByTagName("dbtype");
            if (list.getLength() == 0)
                return AppPrefs.getEmpty();

            appPrefs.put("dbtype", list.item(0).getTextContent());

            // Get database node
            list = rootElement.getElementsByTagName("database");
            if (list.getLength() == 0)
                return AppPrefs.getEmpty();

            String database = list.item(0).getTextContent();
            appPrefs.put("database", database);

            // Get login node
            list = rootElement.getElementsByTagName("login");
            if (list.getLength() == 0)
                return AppPrefs.getEmpty();

            String login = list.item(0).getTextContent();
            appPrefs.put("login", login);

            // Get password node
            list = rootElement.getElementsByTagName("password");
            if (list.getLength() == 0)
                return AppPrefs.getEmpty();

            String password = list.item(0).getTextContent();
            appPrefs.put("password", password);

            // Get dbtype node
            list = rootElement.getElementsByTagName("host");
            if (list.getLength() == 0)
                return AppPrefs.getEmpty();

            String host = list.item(0).getTextContent();
            appPrefs.put("host", host);

            // Get dbtype node
            list = rootElement.getElementsByTagName("port");
            if (list.getLength() == 0)
                return AppPrefs.getEmpty();

            String port = list.item(0).getTextContent();
            appPrefs.put("port", port);

            return appPrefs;
        } catch (Exception e) {
            e.printStackTrace();
            return AppPrefs.getEmpty();
        }
    }
}