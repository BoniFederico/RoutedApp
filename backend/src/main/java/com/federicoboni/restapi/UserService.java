package com.federicoboni.restapi;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.federicoboni.database.dao.UserDao;
import com.federicoboni.database.entities.User;
import com.federicoboni.filters.AuthenticationFilter;
import com.federicoboni.services.JwtTokenService;
import com.federicoboni.services.PasswordService;
import com.federicoboni.services.ResponseManager;
import com.federicoboni.utils.StringUtils;
import com.federicoboni.constant.Constants;
import org.glassfish.jersey.server.ResourceConfig;

@DeclareRoles({ Constants.STANDARD_USER_ROLE, Constants.ADMIN_USER_ROLE })
@Path(Constants.AUTH_SERVER_PATH)
public class UserService extends ResourceConfig {
    private static final String AUTHORIZATION_ERR_MESSAGE = "User already exists!";
    private static final String GENERIC_ERR_MESSAGE = "Generic error occours!";
    private static final String NOT_SPEC_PARAM_ERR_MESSAGE = "Parameters are not specified!";
    private static final String NOT_VALID_PARAM_ERR_MESSAGE = "Username or email are not valid!";

    @POST
    @Path("/register")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User userProvided) {
        try {
            UserDao userDao = new UserDao();
            try {
                userProvided.getEmail();
                userProvided.getUsername();
                userProvided.getPassword();
            } catch (NullPointerException e) {
                return ResponseManager.buildResponse(Response.Status.PARTIAL_CONTENT,
                        NOT_SPEC_PARAM_ERR_MESSAGE);
            }

            StringUtils.isCorrectUsername(userProvided.getUsername());
            StringUtils.isCorrectEmail(userProvided.getEmail());

            User user = userDao.getUser(userProvided.getEmail());
            if (user == null) {
                user = userDao.getUser(userProvided.getUsername());
            }

            if (user != null) {
                return ResponseManager.buildResponse(Response.Status.CONFLICT, AUTHORIZATION_ERR_MESSAGE);
            }

            userProvided.setRole(Constants.STANDARD_USER_ROLE);
            String plainPassword = userProvided.getPassword();
            userProvided.setPassword(PasswordService.generateHash(userProvided.getPassword()));
            userDao.saveUser(userProvided);
            userProvided.setPassword(plainPassword);
            return authenticate(userProvided);
        } catch (ParseException e) {
            return ResponseManager.buildResponse(Response.Status.BAD_REQUEST,
                    NOT_VALID_PARAM_ERR_MESSAGE);
        } catch (Exception e) {
            return ResponseManager.buildResponse(Response.Status.INTERNAL_SERVER_ERROR, GENERIC_ERR_MESSAGE);
        }

    }

    @POST
    @Path("/authenticate")
    @PermitAll
    @Produces("application/json")
    @Consumes("application/json")
    public Response authenticate(User user) {
        UserDao userDao = new UserDao();

        try {
            int id;
            try {
                id = userDao.getUser(user.getEmail()).getId();
            } catch (NullPointerException e1) {

                id = userDao.getUser(user.getUsername()).getId();
            }

            User userFounded = userDao.getUser(id);

            if (PasswordService.validatePassword(user.getPassword(), userFounded.getPassword()) == false) {
                return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED);
            }

            // generate a token for the user
            String accessToken = JwtTokenService.generateJwtAccessToken(String.valueOf(id));
            String refreshToken = JwtTokenService.generateJwtRefreshToken(String.valueOf(id));

            Map<String, String> map = new HashMap<String, String>();
            map.put(AuthenticationFilter.AUTHORIZATION_PROPERTY, accessToken);
            map.put(AuthenticationFilter.AUTHORIZATION_PROPERTY + "_Refresh", refreshToken);
            // Return the token on the response
            return ResponseManager.buildResponse(Response.Status.OK, map);
        } catch (Exception e) {
            return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED);
        }

    }

    @GET
    @Path("/refresh")
    @RolesAllowed({ Constants.STANDARD_USER_ROLE, Constants.ADMIN_USER_ROLE })
    @Produces("application/json")

    public Response refresh(@Context HttpHeaders headers) {
        UserDao userDao = new UserDao();

        try {
            Map<String, String> userInfo = getInfoFromHeaders(headers);

            User user = userDao.getUser(Integer.parseInt(userInfo.get(AuthenticationFilter.HEADER_PROPERTY_ID)));

            if (!userInfo.get(AuthenticationFilter.HEADER_PROPERTY_TOKEN_TYPE)
                    .equals(JwtTokenService.REFRESH_TOKEN_TYPE))
                return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED);
            // generate a token for the user
            String accessToken = JwtTokenService.generateJwtAccessToken(String.valueOf(user.getId()));

            Map<String, String> map = new HashMap<String, String>();
            map.put(AuthenticationFilter.AUTHORIZATION_PROPERTY, accessToken);
            // Return the token on the response
            return ResponseManager.buildResponse(Response.Status.OK, map);
        } catch (Exception e) {
            return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED);
        }

    }

    private Map<String, String> getInfoFromHeaders(HttpHeaders headers) throws NotAuthorizedException {
        Map<String, String> info = new HashMap<String, String>();
        List<String> ids = headers.getRequestHeader(AuthenticationFilter.HEADER_PROPERTY_ID);
        List<String> usernames = headers.getRequestHeader(AuthenticationFilter.HEADER_PROPERTY_USERNAME);
        List<String> emails = headers.getRequestHeader(AuthenticationFilter.HEADER_PROPERTY_EMAIL);
        List<String> tokens = headers.getRequestHeader(AuthenticationFilter.HEADER_PROPERTY_TOKEN_TYPE);
        if (ids.size() != 1 || ids == null || usernames.size() != 1 || usernames == null || emails.size() != 1
                || emails == null || tokens.size() != 1 || tokens == null) {
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
        }
        info.put(AuthenticationFilter.HEADER_PROPERTY_ID, ids.get(0));
        info.put(AuthenticationFilter.HEADER_PROPERTY_USERNAME, usernames.get(0));
        info.put(AuthenticationFilter.HEADER_PROPERTY_EMAIL, emails.get(0));
        info.put(AuthenticationFilter.HEADER_PROPERTY_TOKEN_TYPE, tokens.get(0));

        return info;
    }
}