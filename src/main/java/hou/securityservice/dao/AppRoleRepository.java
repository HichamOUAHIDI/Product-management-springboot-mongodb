package hou.securityservice.dao;

import hou.securityservice.entites.AppRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AppRoleRepository extends JpaRepository<AppRoles,Long>{
    public AppRoles findByRoleName(String roleName);
}
