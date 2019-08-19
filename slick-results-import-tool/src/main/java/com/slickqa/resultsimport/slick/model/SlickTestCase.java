package com.slickqa.resultsimport.slick.model;

import com.slickqa.client.model.Link;

import java.util.List;

public abstract class SlickTestCase {

    String title = "";
    String component = "";
    String feature = "";
    String automationId = "";
    String automationKey = "";
    SlickStep[] steps;
    String testplan = "";
    String triageNote = "";
    String purpose = "";
    String author = "";
    SlickStatus status;
    String reason = "";
    List<Link> links;
    String testName = "";
    String testClass = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getAutomationId() {
        return automationId;
    }

    public void setAutomationId(String automationId) {
        this.automationId = automationId;
    }

    public String getAutomationKey() {
        return automationKey;
    }

    public void setAutomationKey(String automationKey) {
        this.automationKey = automationKey;
    }

    public SlickStep[] getSteps() {
        return steps;
    }

    public void setSteps(SlickStep[] steps) {
        this.steps = steps;
    }

    public String getTestplan() {
        return testplan;
    }

    public void setTestplan(String testplan) {
        this.testplan = testplan;
    }

    public String getTriageNote() {
        return triageNote;
    }

    public void setTriageNote(String triageNote) {
        this.triageNote = triageNote;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public SlickStatus getStatus() {
        return status;
    }

    public void setStatus(SlickStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }
}
