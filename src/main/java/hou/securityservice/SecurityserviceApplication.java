package hou.securityservice;

import hou.securityservice.entites.AppRoles;
import hou.securityservice.service.AccounteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class SecurityserviceApplication {


    public static void main(String[] args) {
        SpringApplication.run(SecurityserviceApplication.class, args);
    }

//
//    @Bean
//    CommandLineRunner start(AccounteService accounteService){
//        return args -> {
//            accounteService.save(new AppRoles(null,"USERE"));
//            accounteService.save(new AppRoles(null,"ADMINe"));
//            Stream.of("user1","user2","user3","admin").forEach(un-> {
//                accounteService.saveUser(un,"1234","1234");
//            });
//            accounteService.addRoleToUser("admine","ADMINe");
//        };
//    }
    @Bean
    BCryptPasswordEncoder getBCPE(){
        return  new BCryptPasswordEncoder();
    }
}

