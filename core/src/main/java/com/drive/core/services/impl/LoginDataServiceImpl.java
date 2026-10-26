
package com.drive.core.services.impl;

import com.drive.core.services.LoginDataService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = LoginDataService.class)
public class LoginDataServiceImpl implements LoginDataService {

    private static final Logger LOG =
            LoggerFactory.getLogger(LoginDataServiceImpl.class);

    private static final String STORAGE_PATH =
            "/var/drive/login-submissions";

    @Override
    public boolean saveLoginData(
            ResourceResolver resourceResolver,
            String email,
            boolean passwordRequired) {

        try {
            Resource parentResource =
                    resourceResolver.getResource(STORAGE_PATH);

            if (parentResource == null) {
                LOG.error("Login storage folder does not exist: {}",
                        STORAGE_PATH);
                return false;
            }

            String submissionName =
                    "submission-" + UUID.randomUUID();

            Map<String, Object> properties = new HashMap<>();

            if (email != null && !email.trim().isEmpty()) {
                properties.put("email", email.trim());
            }

            properties.put("passwordFieldConfigured", passwordRequired);
            properties.put("passwordValidationPassed", passwordRequired);
            properties.put("submittedAt", Instant.now().toString());
            properties.put("submissionType", "login-form");

            Resource submissionResource =
                    resourceResolver.create(
                            parentResource,
                            submissionName,
                            properties);

            resourceResolver.commit();

            LOG.info("Login submission saved at {}",
                    submissionResource.getPath());

            return true;

        } catch (PersistenceException e) {
            LOG.error("Unable to save login submission.", e);
            return false;
        }
    }
}
