package com.trinity.common.interfaces.rest;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.exception.DomainException;
import com.trinity.common.domain.exception.ExternalServiceException;
import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.product.domain.exception.ProductNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @RestController
    static class StubController {
        @GetMapping("/boom/not-found")
        String notFound() {
            throw new NotFoundException("resource missing");
        }

        @GetMapping("/boom/product-not-found")
        String productNotFound() {
            throw new ProductNotFoundException("product missing");
        }

        @GetMapping("/boom/external-service")
        String externalService() {
            throw new ExternalServiceException("upstream down");
        }

        @GetMapping("/boom/business-rule")
        String businessRule() {
            throw new BusinessRuleViolation("invariant violated");
        }

        @GetMapping("/boom/illegal-argument")
        String illegalArgument() {
            throw new IllegalArgumentException("bad input");
        }

        @GetMapping("/boom/illegal-state")
        String illegalState() {
            throw new IllegalStateException("bad state");
        }

        @GetMapping("/boom/data-integrity")
        String dataIntegrity() {
            throw new DataIntegrityViolationException("unique constraint");
        }

        @GetMapping("/boom/domain")
        String domain() {
            throw new SampleDomainException("external domain failure");
        }

        @GetMapping("/boom/unexpected")
        String unexpected() {
            throw new RuntimeException("kaboom");
        }

        @PostMapping("/boom/validation")
        String validation(@Valid @RequestBody ValidatedPayload payload) {
            return "ok";
        }
    }

    @Data
    static class ValidatedPayload {
        @NotEmpty(message = "must not be empty")
        private String field;

        @NotEmpty(message = "must not be empty")
        private String other;
    }

    static class SampleDomainException extends DomainException {
        SampleDomainException(String message) {
            super(message);
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StubController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void notFoundException_mapsTo404() throws Exception {
        mockMvc.perform(get("/boom/not-found").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("resource missing"))
                .andExpect(jsonPath("$.path").value("/boom/not-found"));
    }

    @Test
    void productNotFound_mapsTo404_notDomain422() throws Exception {
        mockMvc.perform(get("/boom/product-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("product missing"));
    }

    @Test
    void externalServiceException_mapsTo502() throws Exception {
        mockMvc.perform(get("/boom/external-service"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.message").value("upstream down"));
    }

    @Test
    void businessRuleViolation_mapsTo409() throws Exception {
        mockMvc.perform(get("/boom/business-rule"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("invariant violated"));
    }

    @Test
    void illegalArgument_mapsTo400() throws Exception {
        mockMvc.perform(get("/boom/illegal-argument"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void illegalState_mapsTo409() throws Exception {
        mockMvc.perform(get("/boom/illegal-state"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void dataIntegrityViolation_mapsTo409() throws Exception {
        mockMvc.perform(get("/boom/data-integrity"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void domainException_mapsTo422() throws Exception {
        mockMvc.perform(get("/boom/domain"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    void unexpectedException_mapsTo500() throws Exception {
        mockMvc.perform(get("/boom/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    void methodArgumentNotValid_mapsTo400_andFormatsFieldError() throws Exception {
        // "field" is empty -> a single field error rendered as "field: message".
        mockMvc.perform(post("/boom/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\":\"\",\"other\":\"present\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(containsString("field: must not be empty")));
    }

    @Test
    void methodArgumentNotValid_multipleErrors_areJoinedByComma() throws Exception {
        // Both fields empty -> the two field errors are concatenated with ", ".
        mockMvc.perform(post("/boom/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\":\"\",\"other\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(containsString("field: must not be empty")))
                .andExpect(jsonPath("$.message").value(containsString("other: must not be empty")))
                .andExpect(jsonPath("$.message").value(containsString(", ")));
    }
}
