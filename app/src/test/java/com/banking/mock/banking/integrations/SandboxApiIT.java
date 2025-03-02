package com.banking.mock.banking.integrations;


import com.banking.Main;
import com.banking.mock.banking.api.model.account.SandboxAccount;
import com.banking.mock.banking.api.model.common.BaseAmount;
import com.banking.mock.banking.api.model.payment.SandboxPaymentRequest;
import com.banking.mock.banking.api.model.payment.SandboxPaymentResponse;
import com.banking.mock.banking.api.model.payment.SandboxTransactionStatus;
import com.banking.mock.banking.integrations.helpers.SandboxTestDataHelper;
import com.banking.mock.banking.services.SandboxAccountService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Main.class})
class SandboxApiIT {

    @LocalServerPort
    private int port;

    @Container
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Autowired
    private SandboxAccountService accountService;

    private static final List<SandboxAccount> SANDBOX_ACCOUNTS = SandboxTestDataHelper.getAccounts();

    private static final int ACCOUNT_TRANSACTIONS_LIMIT = 10;

    private static final BigDecimal AMOUNT = new BigDecimal("100");

    private static final String UNKNOWN_IBAN = "NL18RABO8681965719";

    @Test
    void test() {
        testAccountBalance();
        testFetchTransactions();
        testCorrectPaymentProceed();
        testIbanNotFoundDuringPayment();
        testInsufficientBalanceDuringPayment();
    }

    void testAccountBalance() {
        // Arrange
        SandboxAccount account = SANDBOX_ACCOUNTS.getFirst();
        String accountId = account.getIban();
        BaseAmount expectedBalance = account.getBalance();

        // Assert
        RestAssured.given()
                .contentType("application/json")
                .pathParam("accountId", accountId)
                .get("/mock/accounts/{accountId}/balance")
                .then()
                .statusCode(HttpStatus.OK.value())
                        .body("amount", equalTo(expectedBalance.getAmount().toString()))
                        .body("currency", equalTo(expectedBalance.getCurrency().toString()));
    }

    void testFetchTransactions() {
        // Arrange
        SandboxAccount account = SANDBOX_ACCOUNTS.getFirst();
        String accountId = account.getIban();

        // Assert
        RestAssured.given()
                .contentType("application/json")
                .pathParam("accountId", accountId)
                .queryParam("limit", ACCOUNT_TRANSACTIONS_LIMIT)
                .get("/mock/accounts/{accountId}/transactions")
                .then()
                        .body("size()", lessThanOrEqualTo(ACCOUNT_TRANSACTIONS_LIMIT));

    }

    void testCorrectPaymentProceed() {
        // Arrange
        SandboxAccount senderAccount = SANDBOX_ACCOUNTS.getFirst();
        SandboxAccount recipientAccount = SANDBOX_ACCOUNTS.getLast();

        SandboxPaymentRequest paymentRequest = SandboxTestDataHelper.createPaymentRequest(
                senderAccount.getIban(),
                recipientAccount.getIban(),
                AMOUNT
        );

        // Act
        SandboxPaymentResponse paymentResponse = RestAssured.given()
                .contentType("application/json")
                .body(paymentRequest)
                .post("/mock/payments/initiate")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(SandboxPaymentResponse.class);

        // Assert
        BigDecimal expectedSenderBalance = senderAccount.getBalance().getAmount()
                .subtract(AMOUNT)
                .subtract(paymentResponse.getTransactionFees().getAmount());
        BigDecimal expectedRecipientBalance = recipientAccount.getBalance().getAmount().add(AMOUNT);

        BigDecimal actualSenderBalance = accountService.getAccountBalance(senderAccount.getIban()).getAmount();
        BigDecimal actualRecipientBalance = accountService.getAccountBalance(recipientAccount.getIban()).getAmount();

        assertAll(
                () -> assertNotNull(paymentResponse.getPaymentId(), "Expected paymentId should not be null"),
                () -> assertEquals(SandboxTransactionStatus.ACSC, paymentResponse.getStatus(), "Payment status should be 'ACSC'"),
                () -> assertEquals(expectedSenderBalance, actualSenderBalance, "Sender's balance is incorrect after payment"),
                () -> assertEquals(expectedRecipientBalance, actualRecipientBalance, "Recipient's balance is incorrect after payment")
        );
    }


    void testIbanNotFoundDuringPayment() {
        // Arrange
        SandboxAccount senderAccount = SANDBOX_ACCOUNTS.getFirst();
        SandboxPaymentRequest paymentRequest = SandboxTestDataHelper.createPaymentRequest(senderAccount.getIban(),
                UNKNOWN_IBAN,
                AMOUNT);

        // Act
        SandboxPaymentResponse paymentResponse = RestAssured.given()
                .contentType("application/json")
                .body(paymentRequest)
                .post("/mock/payments/initiate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(SandboxPaymentResponse.class);

        // Assert

        assertAll(
                () -> assertNotNull(paymentResponse.getPaymentId(), "Expected paymentId should not be null"),
                () -> assertEquals(SandboxTransactionStatus.RJCT, paymentResponse.getStatus())
                );
    }

    void testInsufficientBalanceDuringPayment() {
        // Arrange
        SandboxAccount senderAccount = SANDBOX_ACCOUNTS.getFirst();
        SandboxAccount recipientAccount = SANDBOX_ACCOUNTS.getLast();
        SandboxPaymentRequest paymentRequest = SandboxTestDataHelper.createPaymentRequest(senderAccount.getIban(),
                recipientAccount.getIban(),
                senderAccount.getBalance().getAmount().add(AMOUNT.multiply(BigDecimal.TEN)));

        // Act
        SandboxPaymentResponse paymentResponse = RestAssured.given()
                .contentType("application/json")
                .body(paymentRequest)
                .post("/mock/payments/initiate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(SandboxPaymentResponse.class);

        // Assert
        assertAll(
                () -> assertNotNull(paymentResponse.getPaymentId(), "Expected paymentId should not be null"),
                () -> assertEquals(SandboxTransactionStatus.RJCT, paymentResponse.getStatus())
                );
    }
}