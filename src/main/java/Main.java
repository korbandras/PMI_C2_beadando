import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class Main
{

    private static final Scanner be = new Scanner(System.in);
    private static final String filepath = "src/main/resources/grades.xml";

    /**
     * The main where file components are called,
     * such as menu, where most of the program is.
     */
    public static void main(String[] args)
    {
        ArrayList<Grades> grades = readGradesFromXMLwithoutPrint(filepath);
        menu(grades);
        //out.println(grades);
    }

    /**
     * Menu sub-program, this calls the rest of the operations, not including the
     * first reading of the xml file. Here you can choose the action you want, or
     * exit the program. After every action, the program updates the original file,
     * thanks to saveGradesToXML.
     */
    private static void menu(ArrayList<Grades> grades)
    {
        int choice = -1;
        while(choice != 7)
        {
            out.println("1 - List Subjects and Grades\r\n2 - Add New Subject\r\n3 - Modify Subject\r\n" +
                    "4 - Delete Subject\r\n5 - Average Calculator\r\n6 - KKI Calculator\r\n7 - Exit");
            try
            {
                choice = be.nextInt();
            }
            catch (InputMismatchException e)
            {
                out.println("Not a number.");
            }
            be.nextLine();
            switch (choice)
            {
                case 1 -> listGrades();
                case 2 -> addNewGrade(grades);
                case 3 -> modifyGrade(grades);
                case 4 -> deleteGrade(grades);
                case 5 -> avg();
                case 6 -> kki();
            }
            saveGradestoXML(grades, filepath);
        }
    }

    /**
     * This component calculates the KKI based on a formula.
     * (∑(grade*credit) devided by 30) then multiplied by (completed credit devided by all credit you undertook)
     */
    private static void kki()
    {
        double kki, devider = 30;
        //out.println(sumOfDoneTimesGrade());
        //out.println(DoneCrd());
        //out.println(UnderTook());
        kki = (sumOfDoneTimesGrade()/devider)*(DoneCrd()/UnderTook());
        out.printf("Your KKI based on Grades.xml is: %.2f\n", kki);
    }

    /**
     * This helps the KKI(), return the amount of credit
     * you had, not regarding the incompleteness.
     */
    private static double UnderTook()
    {
        double undertook = 0;
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++)
            {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE){
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String crd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++){
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE){
                            if (childNodeOfGradesTag.getNodeName().equals("Credit")){
                                crd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }
                    undertook = undertook + Integer.parseInt(crd);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return undertook;
    }

    /**
     * This helps the KKI(), return the amount of credit you had,
     * but only those credit count, which is for a subject you completed.
     */
    private static double DoneCrd()
    {
        double done = 0;
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++){
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE){
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String grd ="", crd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++)
                    {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE)
                        {
                            switch (childNodeOfGradesTag.getNodeName())
                            {
                                case "Grade" -> grd = childNodeOfGradesTag.getTextContent();
                                case "Credit" -> crd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }

                    if(Integer.parseInt(grd) != 1)
                    {
                        done = done + Integer.parseInt(crd);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return done;
    }

    /**
     * This is the last sub-program, which is helping KKI(), this returns the
     * final component to the formula, the sum of credit times grade
     */
    private static double sumOfDoneTimesGrade()
    {
        double sum = 0;
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++)
            {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String grd ="", crd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++)
                    {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE)
                        {
                            switch (childNodeOfGradesTag.getNodeName()){
                                case "Grade" -> grd = childNodeOfGradesTag.getTextContent();
                                case "Credit" -> crd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }

                    sum = sum + Integer.parseInt(grd)*Integer.parseInt(crd);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sum;
    }

    /**
     * Returns the average of your grades.
     */
    private static void avg(/*ArrayList<Grades> grades*/)
    {
        double avg = gradesSum(/*grades*/)/gradesNo(/*grades*/);
        //out.println(gradesSum(grades));
        //out.println(gradesNo(grades));
        out.printf("Average of the grades: %.2f\n", avg);
    }

    /**
     * Sub-program of avg(), returns the number of grades you have in the grades.xml
     */
    private static double gradesNo() {
        double No = 0;
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++)
            {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++)
                    {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE)
                        {
                            if (childNodeOfGradesTag.getNodeName().equals("Grade"))
                            {
                                No++;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return No;
    }

    /**
     * Sub-program of avg(), returns the sum of your grades.
     */
    private static double gradesSum()
    {
        double sum = 0;
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++)
            {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String grd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++)
                    {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE)
                        {
                            if (childNodeOfGradesTag.getNodeName().equals("Grade"))
                            {
                                grd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }
                    sum = sum + Integer.parseInt(grd);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sum;
    }

    /**
     * Deletes the subject of your choice, as long as you know the name of the subject.
     */
    private static void deleteGrade(ArrayList<Grades> grades)
    {
        out.print("Subject you want to delete: ");
        try
        {
            grades.remove(findGrade(grades, be.nextLine()));
        }
        catch (IllegalArgumentException e)
        {
            out.println(e.getMessage());
        }
    }

    /**
     * Modifies the grade and credit value of the subject of your choosing.
     */
    private static void modifyGrade(ArrayList<Grades> grades)
    {
        out.print("Subject you want to modify: ");
        try
        {
            Grades grades1 = findGrade(grades, be.nextLine());
            grades.set(grades.indexOf(grades1), new Grades(grades1.getSubject(), inputCrd(), inputGrd()));
        }
        catch (IllegalArgumentException e)
        {
            out.println(e.getMessage());
        }
    }

    /**
     * Pinpoints the subject you are looking for, and helps the two programs above,
     * this gives them the subject to delete or modify. If no such subject is in the
     * grades.xml, will return with an error.
     */
    private static Grades findGrade(ArrayList<Grades> grade, String sub) throws IllegalArgumentException
    {
        for(Grades grades : grade)
        {
            if(grades.getSubject().equals(sub))
            {
                return grades;
            }
        }
        throw new IllegalArgumentException("No subject with given name: " + sub);
    }

    /**
     * Adds a new grade, needs 3 sub-programs.
     */
    private static void addNewGrade(ArrayList<Grades> grades)
    {
        grades.add(new Grades(inputSub(), inputCrd(), inputGrd()));
    }

    /**
     * Sub-program of addNewGrade(), this adds the grade, makes sure
     * it is an integer between 1 and 5, else gives an error message.
     */
    private static int inputGrd()
    {
        int grd = 0;
        while(grd < 1 || grd > 5)
        {
            out.print("Enter the grade of the new subject: ");
            try
            {
                grd = be.nextInt();
                if (grd > 5 || grd < 1)
                {
                    out.println("Grade must be between 1 and 5");
                }
            } catch (InputMismatchException e)
            {
                out.println("Grade must be a number.");
            }
        }
        return grd;
    }

    /**
     * Sub-program of addNewGrade(), this adds the credit value, makes sure it is an integer.
     */
    private static int inputCrd()
    {

        out.print("Enter the credit value of new subject: ");
        return be.nextInt();
    }

    /**
     * Sub-program of addNewGrade(), this adds the name of the subject.
     */
    private static String inputSub()
    {
        out.print("Enter new subject: ");
        return be.nextLine();
    }

    /**
     * Lists grades right from reading the XML. Uses a modified version of the reader.
     */
    private static void listGrades()
    {
        readGradesFromXMLwithPrint(filepath);
    }

    /**
     * Modified version of reader to print out the content.
     */
    private static ArrayList<Grades> readGradesFromXMLwithPrint(String filepath1)
    {
        ArrayList<Grades> grade = new ArrayList<>();
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath1);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++)
            {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String sub = "", crd = "", grd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++)
                    {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE)
                        {
                            switch (childNodeOfGradesTag.getNodeName()){
                                case "Subject" -> sub = childNodeOfGradesTag.getTextContent();
                                case "Credit" -> crd = childNodeOfGradesTag.getTextContent();
                                case "Grade" -> grd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }
                    out.println("Subject: " + sub);
                    out.println("Credit: " + crd);
                    out.println("Grade: " + grd);
                    out.println();
                    grade.add(new Grades(sub, Integer.parseInt(crd), Integer.parseInt(grd)));
                    //out.println(grade);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return grade;
    }

    /**
     * Original reader, this is the base of the modified
     * version, reads the contents of the XML file.
     */
    private static ArrayList<Grades> readGradesFromXMLwithoutPrint(String filepath1)
    {
        ArrayList<Grades> grade = new ArrayList<>();
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath1);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++)
            {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String sub = "", crd = "", grd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++)
                    {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE)
                        {
                            switch (childNodeOfGradesTag.getNodeName()){
                                case "Subject" -> sub = childNodeOfGradesTag.getTextContent();
                                case "Credit" -> crd = childNodeOfGradesTag.getTextContent();
                                case "Grade" -> grd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }
                    grade.add(new Grades(sub, Integer.parseInt(crd), Integer.parseInt(grd)));
                    //out.println(grade);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return grade;
    }

    /**
     * Saves the modifications of the grades.xml to the
     * same file, basically updates the file.
     */
    private static void saveGradestoXML(ArrayList<Grades> grade, String filepath1)
    {
        try
        {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = document.createElement("Semester");
            document.appendChild(root);
            for(Grades grades : grade)
            {
                Element gradeElement = document.createElement("Sub");
                root.appendChild(gradeElement);
                childElement(document, gradeElement, "Subject", grades.getSubject());
                childElement(document, gradeElement, "Credit", String.valueOf(grades.getCredit()));
                childElement(document, gradeElement, "Grade", String.valueOf(grades.getGrade()));
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(filepath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Sub-program of the program, which updates the grades.xml.
     * Creates a child element.
     */
    private static void childElement(Document document, Element par, String tag, String text)
    {
        Element element = document.createElement(tag);
        element.setTextContent(text);
        par.appendChild(element);
    }
}