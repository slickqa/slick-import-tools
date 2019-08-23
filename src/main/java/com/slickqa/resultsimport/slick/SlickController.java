package com.slickqa.resultsimport.slick;

import com.slickqa.*;
import com.slickqa.client.SlickClient;
import com.slickqa.client.SlickClientFactory;
import com.slickqa.client.errors.SlickError;
import com.slickqa.client.model.*;
import com.slickqa.resultsimport.slick.model.SlickStep;
import com.slickqa.resultsimport.slick.model.SlickTestCase;

import java.util.*;

public class SlickController {
    protected SlickConfigurationSource configurationSource;
    protected SlickClient slickClient;
    protected Project project;
    protected Map<String, Result> results;
    protected ReleaseReference releaseReference;
    protected BuildReference buildReference;
    protected Map<String, String> testRuns;

    protected SlickController() throws Exception{
        configurationSource = initializeConfigurationSource();
        results = new HashMap<>();
        testRuns = new HashMap<>();
        initializeController();
    }

    protected SlickConfigurationSource initializeConfigurationSource() {
        return new SystemPropertyConfigurationSource();
    }

    protected void initializeController() throws Exception{
        String baseurl = configurationSource.getConfigurationEntry(ConfigurationNames.BASE_URL, null);
        String projectName = configurationSource.getConfigurationEntry(ConfigurationNames.PROJECT_NAME, null);

        if(baseurl != null && projectName != null) {
            try {
                if (!baseurl.endsWith("api") && !baseurl.endsWith("api/")) {
                    String add = "api/";
                    if (baseurl.endsWith("/")) {
                        baseurl = baseurl + add;
                    } else {
                        baseurl = baseurl + "/" + add;
                    }
                }
                slickClient = SlickClientFactory.getSlickClient(baseurl);
                ProjectReference projectReference = new ProjectReference();
                releaseReference = null;
                buildReference = null;

                try {
                    project = slickClient.project(projectName).get();
                } catch (SlickError e) {
                    project = new Project();
                    project.setName(projectName);
                    project = slickClient.projects().create(project);
                }
                projectReference.setName(project.getName());
                projectReference.setId(project.getId());

                String releaseName = configurationSource.getConfigurationEntry(ConfigurationNames.RELEASE_NAME, null);
                if (releaseName != null) {
                    releaseReference = new ReleaseReference();
                    releaseReference.setName(releaseName);
                }
                String buildName = configurationSource.getConfigurationEntry(ConfigurationNames.BUILD_NAME, null);
                if (buildName != null) {
                    buildReference = new BuildReference();
                    buildReference.setName(buildName);
                }
            } catch (SlickError e) {
                e.printStackTrace();
                System.err.println("!!!!!! Error occurred when initializing slick, no slick report will happen !!!!!!");
            }

        }
        else {
            throw new Exception("Base Url or Project Name was null, cannot initializeController!");
        }
    }

    public SlickClient getSlickClient() {
        return slickClient;
    }

    public String getAutomationId(SlickTestCase slickTestCase) {
        String automationId = slickTestCase.getTestName();

        if (slickTestCase.getTestClass() != null) {
            automationId = slickTestCase.getTestClass() + ":" + slickTestCase.getTestName();
        }

        return automationId;
    }

    public Result getResultFor(SlickTestCase slickTestCase) {
        String automationId = getAutomationId(slickTestCase);
        if(results.containsKey(automationId)) {
            return results.get(automationId);
        } else {
            return null;
        }
    }

    public void addResultFor(SlickTestCase slickTestCase, SlickDataSource slickDataSource) throws SlickError {
        String automationId = getAutomationId(slickTestCase);
        Testcase testcase = null;
        HashMap<String, String> query = new HashMap<>();
        query.put("project.id", project.getId());
        query.put("automationId", automationId);
        ProjectReference projectReference = new ProjectReference();
        projectReference.setName(project.getName());
        projectReference.setId(project.getId());

        try {
            List<Testcase> testcases = slickClient.testcases(query).getList();
            if(testcases != null && testcases.size() > 0) {
                testcase = testcases.get(0);
            }
        } catch (SlickError e) {
            // ignore
        }

        if(testcase == null) {
            testcase = new Testcase();
            testcase.setName(slickTestCase.getTestName());
            testcase.setProject(projectReference);
            testcase = slickClient.testcases().create(testcase);
        }

        testcase.setName(slickTestCase.getTestName());
        testcase.setAutomated(true);
        testcase.setAutomationId(automationId);
        testcase.setAutomationTool(ConfigurationNames.JUNIT);
        ComponentReference componentReference = null;
        Component component = null;

        try {
            String testplanId = null;
            String testrunName = null;

            String testplanName = configurationSource.getConfigurationEntry(ConfigurationNames.TESTPLAN_NAME, null);
            if (testplanName == null) {
                // try and grab it from the testrun.mapping.file and class package
                testplanName = slickDataSource.getTestPlan(slickTestCase);
            }
            if (testplanName != null) {
                HashMap<String, String> query2 = new HashMap<>();
                query2.put("project.id", project.getId());
                query2.put("name", testplanName);
                TestPlan tplan = null;
                try {
                    List<TestPlan> tplans = slickClient.testplans(query2).getList();
                    if (tplans != null && tplans.size() > 0) {
                        tplan = tplans.get(0);
                    }
                } catch (SlickError e) {
                    // don't care
                }
                if (tplan == null) {
                    tplan = new TestPlan();
                    tplan.setName(testplanName);
                    tplan.setProject(projectReference);
                    tplan = slickClient.testplans().create(tplan);
                }
                testplanId = tplan.getId();
                if (testrunName == null) {
                    testrunName = tplan.getName();
                }
            }

            if (!testRuns.containsKey(testrunName)) {
                Testrun testrun = new Testrun();
                testrun.setName(testrunName);
                testrun.setTestplanId(testplanId);
                testrun.setProject(projectReference);
                testrun.setRelease(releaseReference);
                testrun.setBuild(buildReference);
                testrun = slickClient.testruns().create(testrun);
                testRuns.put(testrunName, testrun.getId());
            }
        } catch (SlickError e) {
                e.printStackTrace();
                System.err.println("!!!!!! Error occurred when initializing slick, no slick report will happen !!!!!!");
        }


        String slickComponent = slickDataSource.getComponent(slickTestCase);
        if(slickComponent != null && !"".equals(slickComponent)) {
            componentReference = new ComponentReference();
            componentReference.setName(slickComponent);
            if(project.getComponents() != null) {
                for (Component possible : project.getComponents()) {
                    if (slickComponent.equals(possible.getName())) {
                        componentReference.setId(possible.getId());
                        componentReference.setCode(possible.getCode());
                        component = possible;
                        break;
                    }
                }
            }
            if(componentReference.getId() == null) {
                component = new Component();
                component.setName(slickComponent);
                try {
                    component = slickClient.project(project.getId()).components().create(component);
                    componentReference.setId(component.getId());
                    project = slickClient.project(project.getId()).get();
                } catch (SlickError e) {
                    component = null;
                    componentReference = null;
                }
            }
        }
        testcase.setComponent(componentReference);

        FeatureReference featureReference = null;
        String slickFeature = slickDataSource.getFeature(slickTestCase);
        if(slickFeature != null && !"".equals(slickFeature) && component != null) {
            featureReference = new FeatureReference();
            featureReference.setName(slickFeature);
            Feature feature = null;
            if(component.getFeatures() != null) {
                for(Feature possible: component.getFeatures()) {
                    if(slickFeature.equals(possible.getName())) {
                        featureReference.setId(possible.getId());
                        feature = possible;
                        break;
                    }
                }
            }
            if(feature == null) {
                feature = new Feature();
                feature.setName(slickFeature);
                if(component.getFeatures() == null) {
                    component.setFeatures(new ArrayList<Feature>(1));
                }
                component.getFeatures().add(feature);
                try {
                    component = slickClient.project(project.getId()).component(component.getId()).update(component);
                    project = slickClient.project(project.getId()).get();
                    if(component.getFeatures() != null) {
                        for(Feature possible: component.getFeatures()) {
                            if(slickFeature.equals(possible.getName())) {
                                featureReference.setId(feature.getId());
                            }
                        }

                    } else {
                        // this shouldn't be possible which probably means it'll happen
                        feature = null;
                        featureReference = null;
                    }
                } catch (SlickError e) {
                    feature = null;
                    featureReference = null;
                }
            }
        }
        testcase.setFeature(featureReference);

        SlickStep[] slickSteps = slickDataSource.getSteps(slickTestCase);
        if(slickSteps != null && slickSteps.length > 0) {
            testcase.setSteps(new ArrayList<Step>(slickSteps.length));
            for(SlickStep slickStep : slickSteps) {
                Step step = new Step();
                step.setName(slickStep.getStep());
                step.setExpectedResult(slickStep.getExpectation());
                testcase.getSteps().add(step);
            }
        }

        testcase = slickClient.testcase(testcase.getId()).update(testcase);
        TestcaseReference testReference = new TestcaseReference();
        testReference.setName(testcase.getName());
        testReference.setAutomationId(testcase.getAutomationId());
        testReference.setAutomationKey(testcase.getAutomationKey());
        testReference.setTestcaseId(testcase.getId());
        testReference.setAutomationTool(testcase.getAutomationTool());

        TestrunReference testrunReference = new TestrunReference();
          // we set the test run name to the test plan name so we will use that to reference the test run id from the back
        String testplanName = configurationSource.getConfigurationEntry(ConfigurationNames.TESTPLAN_NAME, null);
        if (testplanName == null) {
            // try and grab it from the testrun.mapping.file and class package
            testplanName = slickDataSource.getTestPlan(slickTestCase);
        }
        testrunReference.setName(testplanName);
        testrunReference.setTestrunId(testRuns.get(testplanName));

        Result result = new Result();
        result.setProject(projectReference);
        result.setTestrun(testrunReference);
        result.setTestcase(testReference);

        if (slickTestCase.getStatus() != null && !"".equals(slickTestCase.getStatus().toString())) {
            result.setStatus(slickTestCase.getStatus().toString());
            if(slickTestCase.getReason() != null && !"".equals(slickTestCase.getReason())) {
                result.setReason(slickTestCase.getReason());
            }
        }
        else {
            result.setStatus("NO_RESULT");
            result.setReason("not run yet...");
        }

        if (slickTestCase.getLinks() != null && slickTestCase.getLinks().size() != 0){
            result.setLinks(slickTestCase.getLinks());
        }

        result.setRecorded(new Date());
        result = slickClient.results().create(result);
        results.put(automationId, result);
    }

    public Result getOrCreateResultFor(SlickTestCase slickTestCase, SlickDataSource slickDataSource) {
        Result result = getResultFor(slickTestCase);
        if(result == null) {
            try {
                addResultFor(slickTestCase, slickDataSource);
                return getResultFor(slickTestCase);
            } catch (SlickError e) {
                e.printStackTrace();
                System.err.println("!!!! ERROR creating slick result for " + slickTestCase.getTestName()+ " !!!!");
                return null;
            }
        } else {
            return result;
        }
    }

}
