package com.mycompany.myapp.security;

import com.mycompany.myapp.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Custom service to evaluate authorization requests
 * Usage:
 * '@PreAuthorize("@jhiAuth.hasPermission('USER_RESOURCE', 'GET')")'
 */
@Component("jhiAuth")
public class CustomPermissionEvaluatorService {

    private final Logger log = LoggerFactory.getLogger(CustomPermissionEvaluatorService.class);

    /**
     * Evaluates permission for the given resource and access
     *
     * @param resource
     * @param access
     * @return
     */
    public boolean hasPermission(String resource, String access){
        CustomUserDetails user = getCurrentUser();
        return user != null && hasAccessToResource(user, access, resource);
    }

    /**
     * get the current user token from spring SecurityContextHolder
     * @return
     */
    private CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            return (CustomUserDetails) auth.getPrincipal();
        } else {
            throw new AccessDeniedException("No Valid User Found");
        }
    }

    /**
     * checks if the resource has grants in any roles for the user
     *
     * @param user
     * @param access
     * @param resourceName
     * @return
     */
    private boolean hasAccessToResource(CustomUserDetails user, String access, String resourceName) {

        final boolean[] hasAccess = {false};
        Set<Resource> resources = user.getResources();
        if (resources != null && !resources.isEmpty()){
            resources.forEach(resource -> {
                if (hasGrants(resource, resourceName, access)){
                    log.debug("Role has access to resources: {} ", resourceName);
                    hasAccess[0] = true;
                }
            });
        }
        return hasAccess[0];
    }

    /**
     * checks for grant for HTTP methods
     * 0 = NONE
     * 1 = VIEW,
     * 2 = CREATE,
     * 3 = EDIT,
     * 4 = DELETE
     *
     * @param resource
     * @param resourceName
     * @param access
     * @return
     */
    private boolean hasGrants(Resource resource, String resourceName, String access) {

        if (!resourceName.equals(resource.getName())) return false;

        int grant;
        switch (access) {
            case "VIEW":
                grant = 1;
                break;
            case "CREATE":
                grant = 2;
                break;
            case "EDIT":
                grant = 3;
                break;
            case "DELETE":
                grant = 4;
                break;
            default:
                grant = 0;
        }

        return resource.getPermission() >= grant;
    }
}
