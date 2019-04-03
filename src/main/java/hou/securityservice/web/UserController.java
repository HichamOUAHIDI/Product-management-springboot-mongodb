package hou.securityservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hou.securityservice.entites.AppUser;
import hou.securityservice.service.AccounteService;
import lombok.Getter;
import lombok.Setter;

@RestController
public class UserController {
  
	@Autowired
	public AccounteService accounteService;
	@PostMapping("/registre")
	public AppUser registre(@RequestBody UserForm userForm) {
		return accounteService.saveUser(
		 userForm.getUsername(),userForm.getPassword(),userForm.getConfirmedPassword());
	}
	
	@Getter @Setter 
	class UserForm {
		String username; 
		String password; 
		String confirmedPassword; 
	}
}
