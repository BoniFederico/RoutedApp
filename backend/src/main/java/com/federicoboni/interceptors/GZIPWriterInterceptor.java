package com.federicoboni.interceptors;

import java.io.IOException;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.ext.Provider;

import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

//From jersey documentation: https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/user-guide.html#d0e9777
@Provider
public class GZIPWriterInterceptor implements WriterInterceptor {
    private HttpHeaders httpHeaders;
    private static final String CONTENT_ENCODING_VALUE = "gzip";

    public GZIPWriterInterceptor(@Context @NotNull HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        List<String> acceptEncodingHeaders = this.httpHeaders.getRequestHeader(HttpHeaders.ACCEPT_ENCODING);
        List<String> acceptEncodingValues = new ArrayList<String>();

        if (acceptEncodingHeaders != null) {
            acceptEncodingHeaders.stream()
                    .forEach(s -> acceptEncodingValues.addAll(
                            Arrays.stream(s.replaceAll("\\s+", "").split(",", -1)).collect(Collectors.toList())));
            if (acceptEncodingValues.contains(CONTENT_ENCODING_VALUE)) {

                (context.getHeaders()).add(HttpHeaders.CONTENT_ENCODING, CONTENT_ENCODING_VALUE);
                final OutputStream outputStream = context.getOutputStream();
                context.setOutputStream(new GZIPOutputStream(outputStream));
            }
        }

        context.proceed();
    }
}
