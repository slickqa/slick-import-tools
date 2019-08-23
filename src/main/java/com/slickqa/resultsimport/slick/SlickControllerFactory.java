package com.slickqa.resultsimport.slick;

import com.slickqa.ConfigurationNames;

public class SlickControllerFactory {

    public static Class<? extends SlickController> ControllerClass = SlickController.class;

    public static SlickController INSTANCE = null;

    public static synchronized SlickController getControllerInstance() {
        if (System.getProperty(ConfigurationNames.BASE_URL) == null) {
            System.out.println("You need to pass in a Java Opt for: " + ConfigurationNames.BASE_URL);
            System.exit(-1);
        }
        if (System.getProperty(ConfigurationNames.PROJECT_NAME) == null) {
            System.out.println("You need to pass in a Java Opt for: " + ConfigurationNames.PROJECT_NAME);
            System.exit(-1);
        }
        if (System.getProperty(ConfigurationNames.RELEASE_NAME) == null) {
            System.out.println("You need to pass in a Java Opt for: " + ConfigurationNames.RELEASE_NAME);
            System.exit(-1);
        }
        if (System.getProperty(ConfigurationNames.BUILD_NAME) == null) {
            System.out.println("You need to pass in a Java Opt for: " + ConfigurationNames.BUILD_NAME);
            System.exit(-1);
        }

        if(SlickControllerFactory.INSTANCE == null) {
            try {
                INSTANCE = ControllerClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return SlickControllerFactory.INSTANCE;
    }
}
