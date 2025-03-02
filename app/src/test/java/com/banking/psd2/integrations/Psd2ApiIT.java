package com.banking.psd2.integrations;

import com.banking.Main;
import com.banking.helpers.TokenProvider;
import com.banking.mock.banking.api.model.payment.SandboxPaymentResponse;
import com.banking.mock.banking.api.model.payment.SandboxTransactionStatus;
import com.banking.psd2.api.model.account.BalanceAmount;
import com.banking.psd2.api.model.payment.Payment;
import com.banking.psd2.integrations.helpers.TestDataHelper;
import com.banking.psd2.services.AccountService;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for PSD2 API.
 * Tests banking API functionality including:
 * - Account balance retrieval
 * - Transaction history retrieval
 * - Payment processing between accounts
 * - Error handling for edge cases
 */
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {Main.class})
class Psd2ApiIT {

    // Test constants
    private static final int ACCOUNT_TRANSACTION_SIZE_LIMIT = 10;
    private static final BigDecimal WITHDRAWAL_AMOUNT = new BigDecimal("100");
    private static final String SENDER_IBAN = "DE74500105172121713619";
    private static final String RECEIVER_IBAN = "DE50500105179598831884";
    private static final String UNKNOWN_IBAN = "NL18RABO8681965719";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    @LocalServerPort
    private int port;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TokenProvider tokenProvider;

    private String token;

    /**
     * PostgreSQL container for tests
     */
    @Container
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    /**
     * Registers PostgreSQL properties for Spring
     */
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    }

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        token = tokenProvider.getToken().getAccessToken();
    }

    /**
     * Main test method that runs all test scenarios
     */
    @Test
    void testPsd2ApiIntegration() {
        testAccountBalance();
        testAccountBalanceWithUnknownIban();
        testFetchTransactions();
        testCorrectPaymentProceed();
        testIbanNotFoundDuringPayment();
        testInsufficientBalanceDuringPayment();
    }

    /**
     * Creates a base request specification with authentication
     *
     * @return Configured request specification
     */
    private RequestSpecification givenAuthenticatedRequest() {
        return RestAssured.given()
                .contentType(CONTENT_TYPE_JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER_PREFIX + token);
    }

    /**
     * Tests account balance retrieval for a valid IBAN
     */
    private void testAccountBalance() {
        BigDecimal amount = new BigDecimal("1500.75");
        BalanceAmount expectedBalance = TestDataHelper.createBalanceAmount(amount);

        givenAuthenticatedRequest()
                .pathParam("accountId", SENDER_IBAN)
                .get("/api/accounts/{accountId}/balance")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", notNullValue())
                .body("currency", notNullValue())
                .body("amount", equalTo(amount.floatValue()))
                .body("currency", equalTo(expectedBalance.getCurrency().getCurrencyCode()));
    }

    /**
     * Tests error handling when requesting balance for an unknown IBAN
     */
    private void testAccountBalanceWithUnknownIban() {
        givenAuthenticatedRequest()
                .pathParam("accountId", UNKNOWN_IBAN)
                .get("/api/accounts/{accountId}/balance")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("Account not found"));
    }

    /**
     * Tests transaction history retrieval with limit parameter
     */
    private void testFetchTransactions() {
        givenAuthenticatedRequest()
                .pathParam("accountId", SENDER_IBAN)
                .queryParam("limit", ACCOUNT_TRANSACTION_SIZE_LIMIT)
                .get("/api/accounts/{accountId}/transactions")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("transactions", notNullValue())
                .body("transactions.size()", lessThanOrEqualTo(ACCOUNT_TRANSACTION_SIZE_LIMIT));
    }

    /**
     * Tests successful payment processing between accounts
     */
    private void testCorrectPaymentProceed() {
        // Prepare test data
        Payment payment = TestDataHelper.createPaymentRequest(SENDER_IBAN, RECEIVER_IBAN, WITHDRAWAL_AMOUNT);

        // Record initial balances
        BigDecimal initialSenderBalance = accountService.getAccountBalance(SENDER_IBAN).getAmount();
        BigDecimal initialRecipientBalance = accountService.getAccountBalance(RECEIVER_IBAN).getAmount();

        // Execute payment
        SandboxPaymentResponse paymentResponse = givenAuthenticatedRequest()
                .body(payment)
                .post("/api/payments/initiate")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(SandboxPaymentResponse.class);

        // Calculate expected balances after transaction
        BigDecimal expectedSenderBalance = initialSenderBalance.subtract(WITHDRAWAL_AMOUNT)
                .subtract(paymentResponse.getTransactionFees().getAmount());
        BigDecimal expectedRecipientBalance = initialRecipientBalance.add(WITHDRAWAL_AMOUNT);

        // Get actual balances after transaction
        BigDecimal actualSenderBalance = accountService.getAccountBalance(SENDER_IBAN).getAmount();
        BigDecimal actualRecipientBalance = accountService.getAccountBalance(RECEIVER_IBAN).getAmount();

        // Verify all expectations
        assertAll(
                () -> assertNotNull(paymentResponse.getPaymentId(), "Payment ID should not be null"),
                () -> assertEquals(SandboxTransactionStatus.ACSC, paymentResponse.getStatus(), "Transaction status should be ACSC"),
                () -> assertEquals(expectedSenderBalance, actualSenderBalance, "Sender balance should be correctly reduced"),
                () -> assertEquals(expectedRecipientBalance, actualRecipientBalance, "Recipient balance should be correctly increased")
        );
    }

    /**
     * Tests payment rejection when recipient IBAN is unknown
     */
    private void testIbanNotFoundDuringPayment() {
        Payment payment = TestDataHelper.createPaymentRequest(SENDER_IBAN, UNKNOWN_IBAN, WITHDRAWAL_AMOUNT);

        givenAuthenticatedRequest()
                .body(payment)
                .post("/api/payments/initiate")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("paymentId", notNullValue())
                .body("status", equalTo("RJCT"));
    }

    /**
     * Tests payment rejection when sender has insufficient funds
     */
    private void testInsufficientBalanceDuringPayment() {
        BalanceAmount balanceAmount = accountService.getAccountBalance(SENDER_IBAN);

        // Create payment request with amount greater than account balance
        Payment payment = TestDataHelper.createPaymentRequest(SENDER_IBAN,
                RECEIVER_IBAN,
                WITHDRAWAL_AMOUNT.add(balanceAmount.getAmount()));

        givenAuthenticatedRequest()
                .body(payment)
                .post("/api/payments/initiate")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("error", equalTo("Bad Request"))
                .body("message", equalTo("Insufficient funds for payment"));
    }
}