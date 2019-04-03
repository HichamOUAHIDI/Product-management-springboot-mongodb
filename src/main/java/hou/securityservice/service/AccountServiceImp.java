package hou.securityservice.service;

import hou.securityservice.dao.AppRoleRepository;
import hou.securityservice.dao.AppUserRepository;
import hou.securityservice.entites.AppRoles;
import hou.securityservice.entites.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImp implements AccounteService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AppUser saveUser(String username, String password, String confirPassword) {
        AppUser user =appUserRepository.findByUsername(username);
         if (user!=null) throw new RuntimeException("user already exists");
         if(!password.equals(confirPassword)) throw  new RuntimeException("Please confirm your password");
         AppUser appUser=new AppUser();
         appUser.setUsername(username);
         appUser.setActived(true);
         appUser.setPassword(bCryptPasswordEncoder.encode(password));
         appUserRepository.save(appUser);
         addRoleToUser(username,"USER");
        return appUser;
    }
    @Override
    public AppRoles save(AppRoles role) {
        return appRoleRepository.save(role);
    }

    @Override
    public AppUser userByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser appUser=appUserRepository.findByUsername(username);
        AppRoles appRoles=appRoleRepository.findByRoleName(rolename);
        appUser.getRoles().add(appRoles);
    }
}
