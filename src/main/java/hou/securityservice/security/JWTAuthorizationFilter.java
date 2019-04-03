package hou.securityservice.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;


import Util.SecurityParam;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding.Verifier;


public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		//pour chaque reponse de requet envoyer je lui je authorise tous les domaine 
		response.addHeader("Access-Controle-Allow-Origin", "*");
		// quel sont les entes que je authorise 
		response.addHeader("Access-Controle-Allow-Origin", "Orgin, Accept, X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
		// les entes que je expose pour un client http 
		response.addHeader("Access-Controle-EXpose-Headers", "Access-Controle-Allow-Origin,Access-Control-Allow-Credential,Authorization");
		
		// si une requet qui contient OPTION Dans le header je n'est pas besoin d'un token 
		if(request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
	    /**
	     * pour l'authentification j'ai pas besoin d'un token 
	     * quand l'user tape login donc j'ai besoin de recuper un token 
	     * */
			
		} else if (request.getRequestURI().equals("/login")) {
			filterChain.doFilter(request, response);
		} 
		else {
			/** 
			 * ce traitement dois etre repeter dans tt les micro service de l'application 
			 * spring security vas verfier si le jwt existe dans la requet si oui il  vas singe et decode pour recupere 
			 * le username est ces roles, apres il vas definir cet utilisateur dans le context de spring 
			 * */
			String jwtToken = request.getHeader(SecurityParam.JWT_HEADER_NAMEE);
			if(jwtToken==null || !jwtToken.startsWith(SecurityParam.HEADER_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}
			// signe le jwt 
			JWTVerifier verfier=JWT.require(Algorithm.HMAC256(SecurityParam.SECRET)).build();
			// decode le jwt 
			String jwt=jwtToken.substring(SecurityParam.HEADER_PREFIX.length());
			
			DecodedJWT decodedJWT = verfier.verify(jwt);
			String username=decodedJWT.getSubject();
			java.util.List<String> roles=decodedJWT.getClaims().get("roles").asList(String.class);
			Collection<GrantedAuthority>  authorities=new ArrayList<GrantedAuthority>();
			roles.forEach(rn-> {
				authorities.add(new SimpleGrantedAuthority(rn));
			});
			/**
			 * je dis a spring voilla l'utilisateur porté par jwt a authentifier 
			 * spring security vas verfier si cet utlisateur a le droit d'accede aux resources
			 * */
			UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username,null,authorities);
			// je definie un user de spring 
			SecurityContextHolder.getContext().setAuthentication(user);
			
			filterChain.doFilter(request, response);
			
		}
		
	}

}
