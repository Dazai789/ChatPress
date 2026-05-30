package com.chatpress.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class RateLimiterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerRateLimitExceededReturns429() throws Exception {
        // Register is limited to 5 requests per 60 seconds.
        // The RateLimiter state may already have consumed quota from prior tests,
        // so we fire a burst and verify that the rate limiter eventually kicks in.
        boolean sawRateLimited = false;
        boolean sawAllowed = false;

        for (int i = 1; i <= 12; i++) {
            String body = objectMapper.writeValueAsString(
                    Map.of("username", "ratelimit-reg-" + i, "password", "testpassword123"));
            int statusCode = mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            if (statusCode == 429) {
                sawRateLimited = true;
                // Subsequent requests should also be rate-limited
                if (i < 12) {
                    mockMvc.perform(post("/api/auth/register")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(
                                            Map.of("username", "ratelimit-reg-followup-" + i,
                                                    "password", "testpassword123"))))
                            .andExpect(status().is(429))
                            .andExpect(jsonPath("$.code").value("RATE_LIMITED"));
                    break;
                }
            } else if (statusCode == 200) {
                sawAllowed = true;
            }
        }

        if (!sawRateLimited) {
            throw new AssertionError(
                    "Expected at least one 429 response (RATE_LIMITED) for register. " +
                    "The burst may not have been large enough to exhaust the window.");
        }
        if (!sawAllowed) {
            throw new AssertionError(
                    "Expected at least one successful (200) register call before rate limiting kicked in.");
        }
    }

    @Test
    void loginRateLimitExceededReturns429() throws Exception {
        // Login is limited to 10 requests per 60 seconds.
        // The RateLimiter state may already have consumed quota from prior tests,
        // so we fire a burst and verify that the rate limiter eventually kicks in.
        boolean sawRateLimited = false;
        boolean sawNormalResponse = false;

        for (int i = 1; i <= 20; i++) {
            String body = objectMapper.writeValueAsString(
                    Map.of("username", "admin", "password", "wrong-password-" + i));
            int statusCode = mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andReturn()
                    .getResponse()
                    .getStatus();

            if (statusCode == 429) {
                sawRateLimited = true;
                // Verify 429 response body format
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        Map.of("username", "admin", "password", "wrong-final"))))
                        .andExpect(status().is(429))
                        .andExpect(jsonPath("$.code").value("RATE_LIMITED"));
                break;
            } else if (statusCode == 401 || statusCode == 200) {
                sawNormalResponse = true;
            }
        }

        if (!sawRateLimited) {
            throw new AssertionError(
                    "Expected at least one 429 response (RATE_LIMITED) for login. " +
                    "The burst may not have been large enough to exhaust the window.");
        }
        if (!sawNormalResponse) {
            throw new AssertionError(
                    "Expected at least one normal (200/401) login response before rate limiting kicked in.");
        }
    }
}
