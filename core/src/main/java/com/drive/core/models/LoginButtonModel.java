
package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class LoginButtonModel {

    @ValueMapValue
    private String buttonText;

    @ValueMapValue
    private String buttonLink;

    @ValueMapValue
    private String openInNewTab;

    public String getButtonText() {
        return buttonText != null && !buttonText.trim().isEmpty()
                ? buttonText
                : "Login";
    }

    public String getButtonLink() {
        return buttonLink;
    }

    public String getOpenInNewTab() {
        return openInNewTab != null ? openInNewTab : "false";
    }
}
