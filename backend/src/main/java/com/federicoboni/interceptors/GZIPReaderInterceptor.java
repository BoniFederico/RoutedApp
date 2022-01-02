package com.federicoboni.interceptors;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;

import java.util.zip.GZIPInputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

//From jersey documentation: https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/user-guide.html#d0e9777
@Provider
public class GZIPReaderInterceptor implements ReaderInterceptor {
    private static final String CONTENT_ENCODING_VALUE = "gzip";

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context)
            throws IOException, WebApplicationException {
        List<String> contentEncoding = context.getHeaders().get(HttpHeaders.CONTENT_ENCODING);
        if (contentEncoding != null) {

            if (contentEncoding.contains(CONTENT_ENCODING_VALUE)) {
                final InputStream originalInputStream = context.getInputStream();
                context.setInputStream(new GZIPInputStream(originalInputStream));
            }

        }

        return context.proceed();
    }
}