package com.slickqa.resultsimport.junit;

import com.slickqa.ConfigurationNames;
import com.slickqa.resultsimport.slick.SlickDataSource;
import com.slickqa.resultsimport.slick.model.SlickTestCase;
import com.slickqa.resultsimport.slick.model.SlickStep;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SlickJunitDataSource implements SlickDataSource {

    private HashMap<String, String> testPlans;
    private HashMap<String, String> components;
    private String testRunMappingFile = "";

    @Override
    public String getFeature(SlickTestCase slickTestCase) {
        return "";
    }

    @Override
    public String getComponent(SlickTestCase slickTestCase) {
        String component = "";

        loadComponents();
        for (Map.Entry<String,String> entry : components.entrySet()) {
            if (slickTestCase.getTestClass().startsWith(entry.getKey())) {
                component = entry.getValue();
            }
        }
        if (component.equals("")) {
            if (components.containsKey(ConfigurationNames.DEFAULT)) {
                component = components.get(ConfigurationNames.DEFAULT);
            }
        }

        return component;
    }

    @Override
    public String getTestPlan(SlickTestCase slickTestCase){
        String testPlan = "";

        loadTestPlans();
        for (Map.Entry<String,String> entry : testPlans.entrySet()) {
            if (slickTestCase.getTestClass().startsWith(entry.getKey())) {
                testPlan = entry.getValue();
                break;
            }
        }
        if (testPlan.equals("")) {
            if (testPlans.containsKey(ConfigurationNames.DEFAULT)) {
                testPlan = testPlans.get(ConfigurationNames.DEFAULT);
            }
        }

        return testPlan;
    }

    @Override
    public SlickStep[] getSteps(SlickTestCase slickTestCase) {
        return new SlickStep[0];
    }


    public void loadTestPlans() {
        if (testPlans == null) {
            testPlans = new HashMap<>();

            try
            {
                FileInputStream fileInputStream =  new FileInputStream(Paths.get(testRunMappingFile).toFile());
                String line = null;

                BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                while ((line = br.readLine()) != null) {
                    String delimiter = "=";
                    if (line.split(delimiter).length == 2) {
                        String packageName = line.split("=")[0];
                        String component = line.split("=")[1];
                        testPlans.put(packageName, component);
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public void loadComponents() {
        if (components == null) {
            components = new HashMap<>();

            try
            {
                FileInputStream fileInputStream =  new FileInputStream(Paths.get(testRunMappingFile).toFile());
                String line = null;

                BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                while ((line = br.readLine()) != null) {
                    String delimiter = "=";
                    if (line.split(delimiter).length == 2) {
                        String packageName = line.split("=")[0];
                        String component = line.split("=")[1];
                        components.put(packageName, component);
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }

    @Override
    public void setTestRunMappingFile(String testRunMappingFile) {
        this.testRunMappingFile = testRunMappingFile;
    }

}
