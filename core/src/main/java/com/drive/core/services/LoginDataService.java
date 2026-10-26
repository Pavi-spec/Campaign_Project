
package com.drive.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface LoginDataService {

    boolean saveLoginData(
            ResourceResolver resourceResolver,
            String email,
            boolean passwordRequired);
}
