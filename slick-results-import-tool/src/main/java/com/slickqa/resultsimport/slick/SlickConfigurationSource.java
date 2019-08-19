package com.slickqa.resultsimport.slick;

public interface SlickConfigurationSource {
    String getConfigurationEntry(String name);
    String getConfigurationEntry(String name, String defaultValue);
}