package com.falsuwailih.jwtauthentication.rest;

import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


import java.io.IOException;
import java.security.Key;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

	// ======================================
	// = Injection Points =
	// ======================================

	/*
	 * @Inject private Logger logger;
	 */

	// ======================================
	// = Business methods =
	// ======================================

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		System.out.println("#### authorizationHeader : " + authorizationHeader);

		// Check if the HTTP Authorization header is present and formatted
		// correctly
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			System.out.println("#### invalid authorizationHeader : " + authorizationHeader);
			throw new NotAuthorizedException("Authorization header must be provided");
		}

		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim();

		try {

			// Validate the token

			/*Key key = keyGenerator.generateKey();
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			System.out.println("#### valid token : " + token);*/

		} catch (Exception e) {
			System.out.println("#### invalid token : " + token);
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}
}