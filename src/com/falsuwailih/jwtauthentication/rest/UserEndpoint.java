package com.falsuwailih.jwtauthentication.rest;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;

import com.falsuwailih.jwtauthentication.util.KeyGenerator;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@Path("/users")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
public class UserEndpoint {

	// ======================================
	// = Injection Points =
	// ======================================

	@Context
	private UriInfo uriInfo;

	@PersistenceContext
	private EntityManager em;

	// ======================================
	// = Business methods =
	// ======================================

	@POST
	@Path("/login")
	@Consumes(APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(@FormParam("login") String login, @FormParam("password") String password) {

		try {
			System.out.println("Hello");

			System.out.println("#### login/password : " + login + "/" + password);

			// Authenticate the user using the credentials provided
			authenticate(login, password);

			// Issue a token for the user
			String token = issueToken(login);

			// Return the token on the response
			return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

		} catch (Exception e) {
			return Response.status(UNAUTHORIZED).build();
		}
	}

	private void authenticate(String login, String password) throws Exception {

	}

	private String issueToken(String login) {

		System.out.println("This is issueToken");
		Key key = KeyGenerator.generateKey();
		String jwtToken = Jwts.builder().setSubject(login).setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
				.signWith(SignatureAlgorithm.HS512, key)
				.compact();
		System.out.println("#### generating token for a key : " + jwtToken);
		return jwtToken;
	}

	/*
	 * @POST public Response create(User user) { em.persist(user); return
	 * Response.created(uriInfo.getAbsolutePathBuilder().path(user.getId()).
	 * build()).build(); }
	 * 
	 * @GET
	 * 
	 * @Path("/{id}") public Response findById(@PathParam("id") String id) {
	 * User user = em.find(User.class, id);
	 * 
	 * if (user == null) return Response.status(NOT_FOUND).build();
	 * 
	 * return Response.ok(user).build(); }
	 * 
	 * @GET public Response findAllUsers() { TypedQuery<User> query =
	 * em.createNamedQuery(User.FIND_ALL, User.class); List<User> allUsers =
	 * query.getResultList();
	 * 
	 * if (allUsers == null) return Response.status(NOT_FOUND).build();
	 * 
	 * return Response.ok(allUsers).build(); }
	 * 
	 * @DELETE
	 * 
	 * @Path("/{id}") public Response remove(@PathParam("id") String id) {
	 * em.remove(em.getReference(User.class, id)); return
	 * Response.noContent().build(); }
	 */

	// ======================================
	// = Private methods =
	// ======================================

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
}
