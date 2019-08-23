package com.slickqa.resultsimport.slick;

import com.slickqa.resultsimport.slick.model.SlickStep;
import com.slickqa.resultsimport.slick.model.SlickTestCase;

public interface SlickDataSource {

    String getFeature(SlickTestCase slickTestCase);
    String getComponent(SlickTestCase slickTestCase);
    String getTestPlan(SlickTestCase slickTestCase);
    SlickStep[] getSteps(SlickTestCase slickTestCase);
    void setTestRunMappingFile(String testRunMappingFile);
}
