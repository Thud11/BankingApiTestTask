package com.banking.psd2.services.helpers;

import org.springframework.web.util.UriComponentsBuilder;

public class UrlBuilder {

    private UrlBuilder() {}

    public static String buildUrl(String baseUrl, String path, String iban, Integer limit) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path);

        if (limit != null) {
            builder.queryParam("limit", limit);
        }

        return builder.buildAndExpand(iban).toUriString();
    }


}
