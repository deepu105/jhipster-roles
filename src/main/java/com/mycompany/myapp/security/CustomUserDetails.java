package com.mycompany.myapp.security;

import com.mycompany.myapp.domain.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Deepu on 11-02-2016.
 */
public class CustomUserDetails extends User {

    private Set<Resource> resources;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Set<Resource> resources) {
        super(username, password, authorities);
        this.resources = resources;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }
}
