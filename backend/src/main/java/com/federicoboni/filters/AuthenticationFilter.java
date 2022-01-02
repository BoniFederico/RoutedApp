package com.federicoboni.filters;

import java.sql.Timestamp;

import java.util.Arrays;
import java.util.Date;

import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.logging.Level;
import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.federicoboni.constant.Constants;
import com.federicoboni.database.dao.UserDao;
import com.federicoboni.database.entities.User;
import com.federicoboni.services.JwtTokenService;
import com.federicoboni.services.ResponseManager;

import org.jose4j.jwt.consumer.InvalidJwtException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    public static final String HEADER_PROPERTY_ID = "id";
    public static final String HEADER_PROPERTY_USERNAME = "username";
    public static final String HEADER_PROPERTY_EMAIL = "email";
    public static final String HEADER_PROPERTY_TOKEN_TYPE = "token_type";

    public static final String AUTHORIZATION_PROPERTY = "Authorization"; // as per RFC6750

    private static final String ACCESS_INVALID_TOKEN = "Token invalid. Please authenticate again!";
    private static final String ACCESS_DENIED = "Not allowed to access this resource!";
    private static final String ACCESS_FORBIDDEN = "Access forbidden!";

    @Override
    public void filter(ContainerRequestContext requestContext) {

        Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

        // everybody can access
        if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
            return;
        }
        // nobody can access
        if (resourceInfo.getResourceMethod().isAnnotationPresent(DenyAll.class)) {
            requestContext.abortWith(
                    ResponseManager.buildResponse(Response.Status.FORBIDDEN, ACCESS_FORBIDDEN));
            return;
        }

        // get request headers to extract jwt token
        final MultivaluedMap<String, String> headers = requestContext.getHeaders();

        // block access if no authorization information is provided
        if (headers.get(AUTHORIZATION_PROPERTY) == null || headers.get(AUTHORIZATION_PROPERTY).isEmpty()) {
            logger.log(Level.INFO, (new Timestamp((new Date()).getTime())) + " : " + "No token provided!");
            requestContext.abortWith(
                    ResponseManager.buildResponse(Response.Status.UNAUTHORIZED, ACCESS_DENIED));
            return;
        }

        String id = null;
        String tokenType = JwtTokenService.ACCESS_TOKEN_TYPE;

        // try to decode the jwt - deny access if no valid token provided
        try {
            id = JwtTokenService.validateJwtToken(headers.get(AUTHORIZATION_PROPERTY).get(0),
                    JwtTokenService.ACCESS_TOKEN_TYPE);
        } catch (InvalidJwtException e1) {
            try {
                id = JwtTokenService.validateJwtToken(headers.get(AUTHORIZATION_PROPERTY).get(0),
                        JwtTokenService.REFRESH_TOKEN_TYPE);
                tokenType = JwtTokenService.REFRESH_TOKEN_TYPE;
            } catch (InvalidJwtException e2) {
                logger.log(Level.INFO, (new Timestamp((new Date()).getTime())) + " : " + "Invalid token provided!");
                requestContext.abortWith(
                        ResponseManager.buildResponse(Response.Status.UNAUTHORIZED, ACCESS_INVALID_TOKEN));
                return;
            }
        }

        // check if token matches an user token (set in user/authenticate)

        User user = null;
        try {
            user = (new UserDao()).getUser(Integer.parseInt(id));
        } catch (Exception e) {
            logger.log(Level.INFO, (new Timestamp((new Date()).getTime())) + " : " + "Token missmatch!");
            requestContext.abortWith(
                    ResponseManager.buildResponse(Response.Status.UNAUTHORIZED, ACCESS_DENIED));
            return;
        }

        // token does not match with token stored in database - enforce re
        // authentication

        if (resourceInfo.getResourceClass().isAnnotationPresent(Path.class) && resourceInfo.getResourceClass()
                .getAnnotation(Path.class).value().equals(Constants.RESOURCE_SERVER_PATH)
                && !tokenType.equals(JwtTokenService.ACCESS_TOKEN_TYPE)) {
            logger.log(Level.INFO,
                    (new Timestamp((new Date()).getTime())) + " : "
                            + "User have no rights to acces this resource!");
            requestContext.abortWith(
                    ResponseManager.buildResponse(Response.Status.UNAUTHORIZED, ACCESS_DENIED));
            return;

        }

        if (resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class))

        {

            if (!Arrays.stream(resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class).value())
                    .collect(Collectors.toSet()).contains(user.getRole())) {
                logger.log(Level.INFO,
                        (new Timestamp((new Date()).getTime())) + " : "
                                + "User have no rights to acces this resource!");
                requestContext.abortWith(
                        ResponseManager.buildResponse(Response.Status.UNAUTHORIZED, ACCESS_DENIED));
                return;
            }
        }

        // set header params for user identification in rest service - do not
        // decode jwt twice in rest services

        headers.put(HEADER_PROPERTY_ID, Arrays.asList(String.valueOf(user.getId())));
        headers.put(HEADER_PROPERTY_USERNAME, Arrays.asList(user.getUsername()));
        headers.put(HEADER_PROPERTY_EMAIL, Arrays.asList(user.getEmail()));
        headers.put(HEADER_PROPERTY_TOKEN_TYPE, Arrays.asList(tokenType));

    }

}
