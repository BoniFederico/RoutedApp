package com.federicoboni.restapi;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;

import com.federicoboni.services.ResponseManager;
import com.federicoboni.utils.DateUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.federicoboni.constant.Constants;
import com.federicoboni.database.dao.RouteDao;
import com.federicoboni.database.dao.RouteTypeDao;
import com.federicoboni.database.entities.Route;

import com.federicoboni.database.entities.RouteType;

import com.federicoboni.filters.AuthenticationFilter;

import org.glassfish.jersey.server.ResourceConfig;

@DeclareRoles({ Constants.STANDARD_USER_ROLE, Constants.ADMIN_USER_ROLE })
@Path(Constants.RESOURCE_SERVER_PATH)
public class ResourceService extends ResourceConfig {
    private static final String AUTHENTICATION_ERR_MESSAGE = "Authentication error occours!";
    private static final String AUTHORIZATION_ERR_MESSAGE = "Unauthorized access to data!";
    private static final String GENERIC_ERR_MESSAGE = "Generic error occours!";
    private static final String NOT_VALID_PARAM_ERR_MESSAGE = "Parameters are not valid!";
    private static final String NOT_VALID_JSON_CONTENT = "Provided data are not valid!";

    @GET

    @Path("/routes/{routeDate1}/{routeDate2}")

    @RolesAllowed({ Constants.STANDARD_USER_ROLE, Constants.ADMIN_USER_ROLE })

    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoutes(@Context HttpHeaders headers, @PathParam("routeDate1") String date1,

            @PathParam("routeDate2") String date2) {
        String jsonRoutes = "";
        try {

            Map<String, String> userInfo = getInfoFromHeaders(headers);

            // checking for date validity:
            DateUtils.isValidDate(date1, DateUtils.DATE_PATTERN);
            DateUtils.isValidDate(date2, DateUtils.DATE_PATTERN);

            // parsing dates:
            final LocalDate startDate = LocalDate.parse(date1, DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN));
            final LocalDate endDate = LocalDate.parse(date2, DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN));

            RouteDao routeDao = new RouteDao();
            List<Route> routes = routeDao.getRoutes(Integer.parseInt(userInfo.get("id")));

            List<Route> selectedRoutes = routes.stream().filter(route -> {
                LocalDate index = LocalDate.parse(route.getDate(),
                        DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN1));
                if (index.isAfter(startDate.minusDays(1)) && index.isBefore(endDate.plusDays(1))) {
                    return true;
                } else {
                    return false;
                }

            }).collect(Collectors.toList());

            jsonRoutes = (new Gson()).toJson(selectedRoutes).replace("\\", "").replace("\"[", "[").replace("]\"", "]")
                    .replace("\"{", "{").replace("}\"", "}");

        } catch (NotAuthorizedException e) {
            return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED,
                    AUTHENTICATION_ERR_MESSAGE);
        } catch (ParseException | DateTimeParseException e) {
            return ResponseManager.buildResponse(Response.Status.BAD_REQUEST,
                    NOT_VALID_PARAM_ERR_MESSAGE);
        } catch (Exception e) {
            return ResponseManager.buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    GENERIC_ERR_MESSAGE);
        }

        return ResponseManager.buildResponse(Response.Status.OK, jsonRoutes, true);

    }

    @GET
    @Path("/userinfo")
    @RolesAllowed({ Constants.STANDARD_USER_ROLE, Constants.ADMIN_USER_ROLE })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@Context HttpHeaders headers) {

        Map<String, String> userInfo;
        try {

            userInfo = getInfoFromHeaders(headers);

        } catch (NotAuthorizedException e) {
            return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED, AUTHENTICATION_ERR_MESSAGE);
        } catch (Exception e) {
            return ResponseManager.buildResponse(Response.Status.INTERNAL_SERVER_ERROR, GENERIC_ERR_MESSAGE);
        }

        return ResponseManager.buildResponse(Response.Status.OK, userInfo);
    }

    @GET
    @Path("/types")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypes(@Context HttpHeaders headers) {

        List<RouteType> types;
        try {

            RouteTypeDao typeDao = new RouteTypeDao();
            types = typeDao.getAllType();

        } catch (NotAuthorizedException e) {
            return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED, AUTHENTICATION_ERR_MESSAGE);
        } catch (Exception e) {
            return ResponseManager.buildResponse(Response.Status.INTERNAL_SERVER_ERROR, GENERIC_ERR_MESSAGE);
        }

        return ResponseManager.buildResponse(Response.Status.OK, new Gson().toJson(types));
    }

    @POST
    @Path("/route")
    @RolesAllowed({ Constants.STANDARD_USER_ROLE, Constants.ADMIN_USER_ROLE })

    @Consumes("application/json")
    public Response setRoute(@Context HttpHeaders headers, String body) {

        try {
            Gson gson = new Gson();

            Map<String, String> userInfo = getInfoFromHeaders(headers);

            // Get the route object from body string:
            Route route = gson.fromJson(body, Route.class);

            // Check if geojson route is a valid json:
            gson.fromJson(route.getRoute(), Object.class);
            // Set route user Id from header:
            route.setUser(Integer.parseInt(userInfo.get("id")));

            // Check on type:
            if (new RouteTypeDao().getAllType().stream().filter(t -> t.getType().equals(route.getType()))
                    .collect(Collectors.toList()).isEmpty()) {
                throw new ParseException("", 0);
            }
            // Check on date:
            DateUtils.isValidDate(route.getDate(), DateUtils.DATE_PATTERN1);

            RouteDao routeDao = new RouteDao();
            routeDao.saveRoute(route);

        } catch (NotAuthorizedException e) {
            return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED, AUTHORIZATION_ERR_MESSAGE);
        } catch (JsonSyntaxException | ParseException e) {
            return ResponseManager.buildResponse(Response.Status.BAD_REQUEST, NOT_VALID_JSON_CONTENT);
        } catch (Exception e) {
            return ResponseManager.buildResponse(Response.Status.INTERNAL_SERVER_ERROR, GENERIC_ERR_MESSAGE);
        }
        return ResponseManager.buildResponse(Response.Status.OK);
    }

    @PUT
    @Path("/route")
    @RolesAllowed({ Constants.STANDARD_USER_ROLE, Constants.ADMIN_USER_ROLE })

    @Consumes("application/json")
    public Response updateRoute(@Context HttpHeaders headers, String body) {

        Gson gson = new Gson();
        try {
            Map<String, String> userInfo = getInfoFromHeaders(headers);

            // Get the route object from body string:
            Route route = gson.fromJson(body, Route.class);
            // Set the user id:
            route.setUser(Integer.parseInt(userInfo.get("id")));
            // Check on type:
            if (new RouteTypeDao().getAllType().stream().filter(t -> t.getType().equals(route.getType()))
                    .collect(Collectors.toList()).isEmpty()) {
                throw new ParseException("", 0);
            }
            // Check on date:
            DateUtils.isValidDate(route.getDate(), DateUtils.DATE_PATTERN1);

            // Getting the old route:
            RouteDao routeDao = new RouteDao();
            Route routeOld = routeDao.getRoute(route.getId());
            // Checking if route exists:
            if (routeOld == null) {
                routeDao.saveRoute(route);
            }
            // Checking if is route of the user:
            else if (routeOld.getUser() != Integer.parseInt(userInfo.get("id"))) {
                throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
            } else {
                routeDao.updateRoute(route);
            }

        } catch (NotAuthorizedException e) {
            return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED,
                    AUTHORIZATION_ERR_MESSAGE);
        } catch (JsonSyntaxException e) {
            return ResponseManager.buildResponse(Response.Status.BAD_REQUEST,
                    NOT_VALID_JSON_CONTENT);
        } catch (NumberFormatException e) {
            return ResponseManager.buildResponse(Response.Status.BAD_REQUEST,
                    NOT_VALID_PARAM_ERR_MESSAGE);
        } catch (Exception e) {
            return ResponseManager.buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    GENERIC_ERR_MESSAGE);
        }
        return ResponseManager.buildResponse(Response.Status.OK);
    }

    @DELETE

    @Path("/route/{id}")
    @RolesAllowed({ Constants.STANDARD_USER_ROLE, Constants.ADMIN_USER_ROLE })

    public Response deleteRoute(@Context HttpHeaders headers, @PathParam("id") String routeId) {

        try {
            Map<String, String> userInfo = getInfoFromHeaders(headers);

            // Getting the old route:
            RouteDao routeDao = new RouteDao();

            Route route = routeDao.getRoute(Integer.parseInt(routeId));

            // Checking if route exist and if is the route of the user:
            if (route != null && route.getUser() == Integer.parseInt(userInfo.get("id"))) {
                routeDao.deleteRoute(route.getId());
            } else {
                throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);

            }

        } catch (NotAuthorizedException e) {
            return ResponseManager.buildResponse(Response.Status.UNAUTHORIZED,
                    AUTHORIZATION_ERR_MESSAGE);
        } catch (NumberFormatException e) {
            return ResponseManager.buildResponse(Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseManager.buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    GENERIC_ERR_MESSAGE);
        }
        return ResponseManager.buildResponse(Response.Status.OK);
    }

    private Map<String, String> getInfoFromHeaders(HttpHeaders headers) throws NotAuthorizedException {
        Map<String, String> info = new HashMap<String, String>();
        List<String> ids = headers.getRequestHeader(AuthenticationFilter.HEADER_PROPERTY_ID);
        List<String> usernames = headers.getRequestHeader(AuthenticationFilter.HEADER_PROPERTY_USERNAME);
        List<String> emails = headers.getRequestHeader(AuthenticationFilter.HEADER_PROPERTY_EMAIL);
        if (ids.size() != 1 || ids == null || usernames.size() != 1 || usernames == null || emails.size() != 1
                || emails == null) {
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
        }
        info.put("id", ids.get(0));
        info.put("username", usernames.get(0));
        info.put("email", emails.get(0));

        return info;
    }
}