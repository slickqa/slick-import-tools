package com.slickqa.resultsimport.junit.model;

import com.slickqa.resultsimport.slick.model.SlickTestCase;
import com.slickqa.client.model.Link;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

import com.slickqa.resultsimport.slick.model.SlickStatus;

@Getter
@Setter
@Accessors(fluent = true)
public class JUnitTestCase extends SlickTestCase {
    @XmlAttribute
    private int assertions;
    @XmlAttribute(name = "classname", required = true)
    private String className;
    @XmlAttribute(required = true)
    private String name;
    @XmlAttribute
    private String status;
    @XmlAttribute
    private Double time;

    @XmlElement
    private JUnitSkipped skipped;
    @XmlElement(name = "error")
    private List<JUnitError> errors;
    @XmlElement(name = "failure")
    private List<JUnitFailure> failures;
    @XmlElement(name = "webLink")
    private String webLink;

    @XmlElement(name = "system-out")
    private String systemOut;
    @XmlElement(name = "system-err")
    private String systemErr;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "systemOut", "systemErr");
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }

    @Override
    public SlickStatus getStatus() {
        if (failures != null)
        {
            return SlickStatus.FAIL;
        }
        if (errors != null) {
            return SlickStatus.BROKEN_TEST;
        }
        if (skipped != null) {
            return SlickStatus.SKIPPED;
        }
        return SlickStatus.PASS;
    }

    @Override
    public String getReason() {
        String reason = "";
        if (failures != null)
        {
            for (JUnitFailure failure : failures) {
                reason += failure.getValue() + "\n";
            }
        }
        if (errors != null) {
            for (JUnitError error : errors) {
                reason += error.getValue() + "\n";
            }
        }
        return reason;
    }

    @Override
    public String getTestName() {
        return name;
    }

    @Override
    public String getTestClass() {
        return className;
    }

    @Override
    public List<Link> getLinks() {
        List<Link> links = new ArrayList();

        if (webLink != null) {
            Link link = new Link();
            link.setName("Firebase Test Lab Link");
            link.setUrl(webLink);
            links.add(link);
        }
        return links;
    }
}