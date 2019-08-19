package com.slickqa;

import com.slickqa.resultsimport.slick.SlickConfigurationSource;

public class SystemPropertyConfigurationSource implements SlickConfigurationSource {

    @Override
    public String getConfigurationEntry(String name) {
        return System.getProperty(name);
    }

    @Override
    public String getConfigurationEntry(String name, String defaultValue) {
        return System.getProperty(name, defaultValue);
    }
}