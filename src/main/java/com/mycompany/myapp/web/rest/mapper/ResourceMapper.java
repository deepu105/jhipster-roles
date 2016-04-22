package com.mycompany.myapp.web.rest.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.web.rest.dto.ResourceDTO;

import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity Resource and its DTO ResourceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ResourceMapper {

    ResourceDTO resourceToResourceDTO(Resource resource);

    Set<ResourceDTO> resourcesToResourceDTOs(Set<Resource> resources);

    default Resource resourceDTOToResource(ResourceDTO resourceDTO, Authority auth) {
    	Resource res = new Resource();
        res.setId(resourceDTO.getId());
        res.setName(resourceDTO.getName());
        res.setPermission(resourceDTO.getPermission());
        res.setAuthority(auth);

        return res;
    }

    default Set<Resource> resoursesFromResourceDTOs(Set<ResourceDTO> resourceDTOs, Authority auth) {
        return resourceDTOs.stream().map(resourceDTO -> {
            return resourceDTOToResource(resourceDTO, auth);
        }).collect(Collectors.toSet());
    }
}
