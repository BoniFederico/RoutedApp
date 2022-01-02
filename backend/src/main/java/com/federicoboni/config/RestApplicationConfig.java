package com.federicoboni.config;

import com.federicoboni.filters.AuthenticationFilter;
import com.federicoboni.interceptors.GZIPReaderInterceptor;
import com.federicoboni.interceptors.GZIPWriterInterceptor;

import org.glassfish.jersey.server.ResourceConfig;

public class RestApplicationConfig extends ResourceConfig {

    public RestApplicationConfig() {

        packages("com.federicoboni.filters");

        register(AuthenticationFilter.class);

        register(GZIPWriterInterceptor.class);
        register(GZIPReaderInterceptor.class);

    }
}