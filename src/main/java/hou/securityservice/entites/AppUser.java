package hou.securityservice.entites;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    // cette annotation pour ignore l'affichage de password sous format json 
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Boolean actived;
    @ManyToMany(fetch = FetchType.EAGER)
    // a chaque fois je demande un utilisateur il vas ramner ces roles
    private Collection<AppRoles> roles=new ArrayList<>();

}
