package com.slickqa;

import com.slickqa.resultsimport.junit.JUnitUnMarshaller;
import com.slickqa.resultsimport.junit.SlickJunitDataSource;
import com.slickqa.resultsimport.junit.model.JUnitTestCase;
import com.slickqa.resultsimport.junit.model.JUnitTestSuites;
import com.slickqa.resultsimport.slick.SlickController;
import com.slickqa.resultsimport.slick.SlickControllerFactory;
import com.slickqa.resultsimport.slick.SlickDataSource;

import java.util.List;

public class SlickResultsImportTool {
    private static String resultsFile = "";
    private static String automationTool = "";
    private static String testRunMappingFile = "";

    public static void main(String[] args) throws Exception {
        parseJavaOpts();

        final SlickController controller = SlickControllerFactory.getControllerInstance();

        if (System.getProperty(ConfigurationNames.AUTOMATION_TOOL).toLowerCase().equals(ConfigurationNames.JUNIT.toLowerCase())) {
            JUnitTestSuites jUnitTestSuites = JUnitUnMarshaller.unmarshalTestSuites(resultsFile);
            SlickDataSource dataSource = new SlickJunitDataSource();
            dataSource.setTestRunMappingFile(testRunMappingFile);

            if (jUnitTestSuites.getSuites() == null) {
                System.out.println("jUnitTestSuites.getSuites() is null, which means that no test results were found in the xml file. Cannot continue test!");
                System.exit(-1);
            }

            for (int x=0; x< jUnitTestSuites.getSuites().size(); x++) {
                List<JUnitTestCase> junitTests = jUnitTestSuites.getSuites().get(x).getTests();
                for (JUnitTestCase junitTest : junitTests) {
                    controller.getOrCreateResultFor(junitTest, dataSource);
                }
            }
        }
    }

    private static void parseJavaOpts() {
        resultsFile = System.getProperty(ConfigurationNames.RESULTS_FILE);
        automationTool = System.getProperty(ConfigurationNames.AUTOMATION_TOOL);
        testRunMappingFile = System.getProperty(ConfigurationNames.TESTRUN_MAPPING_FILE);

        if (resultsFile == null) {
            System.out.println("You need to pass in a Java Opt for: " + ConfigurationNames.RESULTS_FILE);
            System.exit(-1);
        }
        if (automationTool == null) {
            System.out.println("You need to pass in a Java Opt for: " + ConfigurationNames.AUTOMATION_TOOL);
            System.exit(-1);
        }
        if (automationTool.toLowerCase().equals(ConfigurationNames.JUNIT.toLowerCase())) {
            if (testRunMappingFile == null) {
                System.out.println("You need to pass in a Java Opt for: " + ConfigurationNames.TESTRUN_MAPPING_FILE);
                System.exit(-1);
            }
        }
        else {
            System.out.println("Tool currently only supports the value '" + ConfigurationNames.JUNIT + "' for the Java Opt: " + ConfigurationNames.AUTOMATION_TOOL);
            System.exit(-1);
        }
    }
}
