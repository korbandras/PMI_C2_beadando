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

    public static void main(String[] args)
    {
        out.println("Grades.xml so far:");
        ArrayList<Grades> grades = readGradesFromXML(filepath);
        menu(grades);
        saveGradestoXML(grades, filepath);
    }

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
        }
    }

    private static void kki()
    {
        double kki, devider = 30;
        //out.println(sumOfDoneTimesGrade());
        //out.println(DoneCrd());
        //out.println(UnderTook());
        kki = (sumOfDoneTimesGrade()/devider)*(DoneCrd()/UnderTook());
        out.printf("Your KKI based on Grades.xml is: %.2f", kki);
    }

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

    private static void avg(/*ArrayList<Grades> grades*/)
    {
        double avg = gradesSum(/*grades*/)/gradesNo(/*grades*/);
        //out.println(gradesSum(grades));
        //out.println(gradesNo(grades));
        out.printf("Average of the grades: %.2f\n", avg);
    }

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

    private static void addNewGrade(ArrayList<Grades> grades)
    {
        grades.add(new Grades(inputSub(), inputCrd(), inputGrd()));
    }

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

    private static int inputCrd()
    {

        out.print("Enter the credit value of new subject: ");
        return be.nextInt();
    }

    private static String inputSub()
    {
        out.print("Enter new subject: ");
        return be.nextLine();
    }

    private static void listGrades()
    {
        readGradesFromXML(filepath);
    }

    private static ArrayList<Grades> readGradesFromXML(String filepath1)
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

    private static void childElement(Document document, Element par, String tag, String text)
    {
        Element element = document.createElement(tag);
        element.setTextContent(text);
        par.appendChild(element);
    }
}