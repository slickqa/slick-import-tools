package com.slickqa.resultsimport.junit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@XmlRootElement(name = "testsuites")
public class JUnitTestSuites {
    @XmlElement(name = "testsuite")
    private List<JUnitTestSuite> testSuites;

    public void setTestSuites(List<JUnitTestSuite> suites) {
        this.testSuites = suites;
    }

    public List<JUnitTestSuite> getSuites() {
        return this.testSuites;
    }
}
