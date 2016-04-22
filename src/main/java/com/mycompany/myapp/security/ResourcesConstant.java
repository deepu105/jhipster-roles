package com.mycompany.myapp.security;

import java.util.Arrays;
import java.util.List;

/**
 * Constants for secured resources.
 */
public final class ResourcesConstant {
    public enum Resource {
        USER_RESOURCE,
        ROLE_RESOURCE
        /* jhipster-needle-resource-add-item */
    }

    public static List<Resource> getAllResources() {
        return Arrays.asList(Resource.values());
    }
}
