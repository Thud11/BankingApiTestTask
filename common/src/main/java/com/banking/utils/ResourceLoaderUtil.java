package com.banking.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class ResourceLoaderUtil {

    private ResourceLoaderUtil() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);



    public static <T> T readResource(String path, Class<T> clazz) {
        Resource resource = getResource(path);
        try {
            return MAPPER.readValue(resource.getInputStream(), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from JSON at path: " + path, e);
        }
    }


    public static <T> T readResource(String path, TypeReference<T> typeReference) {
        Resource resource = getResource(path);
        try {
            return MAPPER.readValue(resource.getInputStream(), typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from JSON at path: " + path, e);
        }
    }

    private static Resource getResource(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Resource resource = new DefaultResourceLoader(classLoader).getResource(path);
        if (!resource.exists()) {
            throw new RuntimeException("Resource not found: " + path);
        }
        return resource;
    }

}
