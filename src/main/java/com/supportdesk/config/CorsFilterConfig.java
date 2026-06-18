package com.supportdesk.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class CorsFilterConfig implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) servletRequest;

        HttpServletResponse response =
                (HttpServletResponse) servletResponse;

        String origin = request.getHeader("Origin");

        if (origin != null &&
                (origin.equals("http://localhost:5173")
                        || origin.equals("https://supportdesk-frontend-eight.vercel.app")
                        || origin.endsWith(".vercel.app"))) {

            response.setHeader(
                    "Access-Control-Allow-Origin",
                    origin
            );
        }

        response.setHeader(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );

        response.setHeader(
                "Access-Control-Allow-Headers",
                "Authorization, Content-Type, Accept, Origin"
        );

        response.setHeader(
                "Access-Control-Allow-Credentials",
                "true"
        );

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(request, response);
    }
}