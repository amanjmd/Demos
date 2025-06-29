package com.demo;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class RequestEchoController {

    private static final Logger log = LoggerFactory.getLogger(RequestEchoController.class);

    /**
     * Handles *any* HTTP method on /echo — handy for quick debugging.
     */
    @RequestMapping(
            value = "/echo",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.ALL_VALUE)
    public Map<String, Object> echo(HttpServletRequest request,
                                    @RequestHeader Map<String, String> headers,
                                    @RequestParam Map<String, String> params) throws IOException {

        // ---------- Query parameters & form fields ----------
        log.info("==============Request received========================");
        log.info("=== Parameters & Form Fields ===");
        params.forEach((k, v) -> log.info("{}: {}", k, v));

        // ---------- Headers ----------
        log.info("=== Headers ===");
        headers.forEach((k, v) -> log.info("{}: {}", k, v));

        // ---------- Raw body (optional) ----------
        String body = "";
        if (!Objects.equals(request.getMethod(), "GET")) {  // only attempt for non‑GET
            body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            log.info("=== Raw Body ===\n{}", body);
        }

        // ---------- Build a JSON response ----------
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("method", request.getMethod());
        response.put("path",   request.getRequestURI());
        response.put("params", params);
        response.put("headers", headers);
        response.put("body", body);
        return response;
    }
}
