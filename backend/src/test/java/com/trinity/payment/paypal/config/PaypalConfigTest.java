package com.trinity.payment.paypal.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.paypal.base.rest.APIContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.util.ReflectionTestUtils;

// @SpringBootTest
// @ActiveProfiles("dev")
class PaypalConfigTest {

    @Autowired
    private PaypalConfig paypalConfig;

    @BeforeEach
    public void setUp() {
        paypalConfig = new PaypalConfig();
        ReflectionTestUtils.setField(paypalConfig, "clientId", "secret");
        ReflectionTestUtils.setField(paypalConfig, "clientSecret", "secretmdp");
        ReflectionTestUtils.setField(paypalConfig, "mode", "sandbox");
    }

    @Test
    void testGetAPIContext() {
        APIContext apiContext = paypalConfig.getAPIContext();
        assertNotNull(apiContext);
        assertEquals("secret", apiContext.getClientID());
        assertEquals("secretmdp", apiContext.getClientSecret());
        assertEquals("sandbox", apiContext.getConfigurationMap().get("mode"));
    }
}