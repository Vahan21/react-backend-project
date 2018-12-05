package config;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicTestNG {
    public void runTestNGTest(Map<String, String> testngParams, String[] classesArr, int threadCount) {   //Create an instance on TestNG
        TestNG myTestNG = new TestNG();

        //Print the parameter values
        for (Map.Entry<String, String> entry : testngParams.entrySet()) {
            System.out.println("====== " + entry.getKey() + " => " + entry.getValue() + " ============================================================");
        }
        System.out.println("====== thread count "+ threadCount + " ============================================================");
        for (String className: classesArr) {
            System.out.println("class " + className);
        }

        //Create an instance of XML Suite and assign a name for it.
        XmlSuite mySuite = new XmlSuite();
        mySuite.setName("MySuite");
        mySuite.setParallel(XmlSuite.ParallelMode.METHODS);

        //Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = null;
        XmlTest myTest2 = null;
        if (testngParams.containsKey("browser1")) {

            Map<String, String> paramsFor1 = testngParams;
            paramsFor1.remove("browser2");
            paramsFor1.remove("browserVersion2");

            myTest = new XmlTest(mySuite);
            myTest.setName("MyTest1");
            myTest.setParameters(paramsFor1);


            Map<String, String> paramsFor2 = testngParams;
            myTest2 = new XmlTest(mySuite);
            myTest.setName("MyTest2");
            paramsFor2.remove("browser1");
            paramsFor2.remove("browserVersion1");
            myTest2.setParameters(paramsFor2);

        } else {
            myTest = new XmlTest(mySuite);
            myTest.setName("MyTest");

            //Add any parameters that you want to set to the Test.
            myTest.setParameters(testngParams);
        }

        //Create a list which can contain the classes that you want to run.
        List<XmlClass> myClasses = new ArrayList<XmlClass>();
        for (String item : classesArr) {
            myClasses.add(new XmlClass("tests." + item));
        }

        //Assign that to the XmlTest Object created earlier.
        myTest.setXmlClasses(myClasses);
        if (myTest2 != null) {
            myTest2.setXmlClasses(myClasses);
        }

        //Create a list of XmlTests and add the Xmltest you created earlier to it.
        List<XmlTest> myTests = new ArrayList<XmlTest>();
        myTests.add(myTest);
        if (myTest2 != null) {
            myTests.add(myTest2);
        }

        //add the list of tests to your Suite.
        mySuite.setTests(myTests);

        //Add the suite to the list of suites.
        List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
        mySuites.add(mySuite);

        //Set the list of Suites to the testNG object you created earlier.
        myTestNG.setXmlSuites(mySuites);
        mySuite.setFileName("myTemp.xml");
        mySuite.setThreadCount(threadCount);
        myTestNG.run();

        //Create physical XML file based on the virtual XML content
        for (XmlSuite suite : mySuites) {
            createXmlFile(suite);
        }
        System.out.println("File created successfully.====================================================");

        //Print the parameter values
        Map<String, String> params = myTest.getParameters();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println("====== " + entry.getKey() + " => " + entry.getValue() + " ============================================================");
        }
    }

    //This method will create an Xml file based on the XmlSuite data
    private void createXmlFile(XmlSuite mSuite) {
        FileWriter writer;
        try {
            writer = new FileWriter(new File("myTemp.xml"));
            writer.write(mSuite.toXml());
            writer.flush();
            writer.close();
            System.out.println(new File("myTemp.xml").getAbsolutePath());
        } catch (IOException e) {
            System.out.println("exception ###################################################################################");
            System.out.println("exception ###################################################################################");
            System.out.println("exception ###################################################################################");
            System.out.println("exception ###################################################################################");
            e.printStackTrace();
        }
    }

    //Main Method
//    public static void main(String args[]) {
//        config.DynamicTestNG dt = new config.DynamicTestNG();
//
//        //This Map can hold your testng Parameters.
//        Map<String, String> testngParams = new HashMap<String, String>();
//        testngParams.put("","device1esktop");
//        testngParams.put("","device2obile");
//        testngParams.put("","device3ablet");
//        dt.runTestNGTest(testngParams);
//    }
}