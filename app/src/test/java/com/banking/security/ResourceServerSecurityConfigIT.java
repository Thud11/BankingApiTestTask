package com.banking.security;

import com.banking.helpers.TokenProvider;
import com.banking.psd2.services.AccountService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class ResourceServerSecurityConfigIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private AccountService accountService;

    private static final String SENDER_IBAN = "DE74500105172121713619";
    private static final String INVALID_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjdiOTcyODdhLTk1M2UtNGQ1Mi05MjZhLWMxMTg1MmY5ZTVhYyIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJzdWIiOiJjbGllbnQxIiwiYXVkIjoiY2xpZW50MSIsIm5iZiI6MTc0MDQ5NjgxNSwiZXhwIjoxNzQwNDk3MTE1LCJpYXQiOjE3NDA0OTY4MTV9.RR99Qd69V6EZVqJYHqIx2yZ5PGRyONipopkabstFSfFsSDu5iLpZC-VXTqhil3UCsppmoPIAIExm8WqFFbfGCM6d1F4-EA1aHIOaErjod9Qc_1DV5mJE1ObIEWH740yYrengfuTJU4-oT3RoBeL5NEjdhjJR5onyN7mmv7RLW29YuXj61n2RmgLkivjQZ5mGq7OB3GmB_3L8FJL6wEVogCov8M8PQBKwwox3Z6aKO-etfXZwO5Wpt43UH-6ukTfmNHYNmV5m0ySQ_MGYemnDYiWDHZC4inwWSBe0e92KDp5vOHH8LHhsftlO6DabvEWYv39rwIxZ6cdypORCo3A4yg";


    private static final String PUBLIC_ENDPOINT = "/mock/accounts/{accountId}/balance";
    private static final String PRIVATE_ENDPOINT = "/api/accounts/{accountId}/balance";

    @BeforeEach
    void setUp() {
        when(accountService.ibanExists(SENDER_IBAN)).thenReturn(true);
    }

    @Test
    void securityTest() {
        testPublicEndpointsAccessible();
        testPrivateEndpointRequiresAuthentication();
        testPrivateEndpointAccessibleWithValidToken();
        testPrivateEndpointAccessibleWithInvalidToken();
    }

    @SneakyThrows
    void testPublicEndpointsAccessible() {
        mockMvc.perform(get(PUBLIC_ENDPOINT, SENDER_IBAN))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    void testPrivateEndpointRequiresAuthentication() {
        mockMvc.perform(get(PRIVATE_ENDPOINT, SENDER_IBAN))
                .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    void testPrivateEndpointAccessibleWithValidToken() {
        String token = tokenProvider.getToken().getAccessToken();
        mockMvc.perform(get(PRIVATE_ENDPOINT, SENDER_IBAN)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    void testPrivateEndpointAccessibleWithInvalidToken() {
        mockMvc.perform(get(PRIVATE_ENDPOINT, SENDER_IBAN)
                        .header("Authorization", "Bearer " + INVALID_TOKEN))
                .andExpect(status().isUnauthorized());
    }


}
