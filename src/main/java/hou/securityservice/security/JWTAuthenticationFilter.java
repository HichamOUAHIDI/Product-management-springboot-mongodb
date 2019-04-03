package hou.securityservice.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import Util.SecurityParam;
import hou.securityservice.entites.AppUser;

public class JWTAuthenticationFilter  extends UsernamePasswordAuthenticationFilter{
	private AuthenticationManager authenticationManager; 
	public JWTAuthenticationFilter (AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager=authenticationManager;
	}
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)	throws AuthenticationException {
		// si  l 'utilisateur envoyer les infos de authentification sont envoyer 3URI_encoded 
/*
 * 		String username = request.getParameter("username");
 * 		String password = request.getParameter("password");
 */
		// dans ce cas je suppose que les donnes sont envoyer au format json 
		
		try {
			// on vas recupere les donnes de la requete on utilison objectMapper
			// et en désirialisé les donner de la requete d'un objet JSON a un objet java 

			AppUser appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	// cette methode pour génere le JWT  
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		/**
		 * getPrincipal retourn les utilisateur authentifier 
		 * gethorities retourn les roles de l'utilisateur authentifier 
		 * 
		 */
		
		/**
		 * NB: le JWT est constitué de trois parties séparées par un <<.>>
		 * - le Header : on trouve la lgorithem quand vas utilisé pour calculé la signature(HMAC OU RSA) 
		 *   le Header dois etre encode en base64URL(quelque chose comme ca ehejeizueoaiaoifeaejfe)   
		 * - Payload : contient l'ensemble de revendication (claims) il existe trois types de claims : 
		 *     > Enregistrées (issuer,expiration date ,subject,public cible,not befor,issed at,jwt ID)
		 *     > publique 
		 *     > provées 
		 * - Signature : utilisée pour verifier que l'expéditeur du jwt est celui qu'il pretebd etre 
		 *   et pour s'assurer que le message n'a pas été modifer en cours de route 
		 *   Exemple : HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)
		 * */
		
		User springUser = (User) authResult.getPrincipal();
		List<String> roles = new ArrayList<>();
		springUser.getAuthorities().forEach(a -> {
			roles.add(a.getAuthority());
		});
		String jwt = JWT.create()
				// l'application qui a genere le jwt 
				.withIssuer(request.getRequestURI())
				// ajouter le nom de utilisateur 
				.withSubject(springUser.getUsername())
				// ajouter les role d'utilisateur sous form d'un tableau 
				.withArrayClaim("roles",roles.toArray(new String[roles.size()]))
				// la date d'experation 
				.withExpiresAt(new Date(System.currentTimeMillis()+SecurityParam.EXPIRATION))
				// en ajoute la signature qui prend en parmettre le secret de notre jwt :) 
				.sign(Algorithm.HMAC256(SecurityParam.SECRET));
				/** 
				 * voila apres la géneration de notre jwt on dois l'ajoute a l'entete de la resquet 
				 * */
				// on donne un nom est une valeur au header 
				response.addHeader(SecurityParam.JWT_HEADER_NAMEE,jwt );
	
	}
}
