package com.slickqa.resultsimport.slick.model;

public enum SlickStatus {
    PASS("PASS"),
    FAIL("FAIL"),
    BROKEN_TEST("BROKEN_TEST"),
    SKIPPED("SKIPPED");

    private final String description;

    SlickStatus(String description) { this.description = description; }

    public String getDescription() { return description; }
}
