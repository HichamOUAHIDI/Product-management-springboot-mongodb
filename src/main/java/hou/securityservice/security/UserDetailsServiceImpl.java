package hou.securityservice.security;

import hou.securityservice.entites.AppUser;
import hou.securityservice.service.AccounteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccounteService accounteService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // je recupére un utilisateur et ses roles 
    	AppUser appUser=accounteService.userByUsername(username);
        if (appUser==null) throw new UsernameNotFoundException("invalid user");
        Collection<GrantedAuthority> authorities=new ArrayList<>();
        appUser.getRoles().forEach(r->{
             authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
        });
        // je retourn un utlisateur de type spring 
        return new User (appUser.getUsername(),appUser.getPassword(),authorities);
    }
}
