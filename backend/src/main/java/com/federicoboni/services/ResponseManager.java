package com.federicoboni.services;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;

public class ResponseManager {

    public static final String MESSAGE_KEY = "msg";
    private static GsonBuilder gsonBuilder = new GsonBuilder();

    public static Response buildResponse(Response.Status status) {

        try {
            String message = gsonBuilder.setPrettyPrinting().create()
                    .toJson(new SimpleEntry<String, String>(MESSAGE_KEY, status.toString()));
            return Response.status(status).entity(message).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    public static Response buildResponse(Response.Status status, String msg) {

        try {
            String message = gsonBuilder.setPrettyPrinting().create()
                    .toJson(new SimpleEntry<String, String>(MESSAGE_KEY, msg));
            return Response.status(status).entity(message).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    public static Response buildResponse(Response.Status status, String msg, boolean alreadyJson) {

        try {

            if (alreadyJson) {
                return Response.status(status).entity(msg).build();
            } else {
                return buildResponse(status, msg);
            }

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    public static Response buildResponse(Response.Status status, Map<String, String> map) {

        try {
            String message = gsonBuilder.setPrettyPrinting().create()
                    .toJson(map);
            return Response.status(status).entity(message).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
