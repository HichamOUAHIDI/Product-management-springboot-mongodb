package hou.securityservice.service;

import hou.securityservice.entites.AppRoles;
import hou.securityservice.entites.AppUser;

public interface AccounteService {
    public AppUser saveUser(String username,String password,String confirPassword);
    public AppRoles save (AppRoles role);
    public AppUser userByUsername(String username);
    public void addRoleToUser(String username,String rolename);
}

