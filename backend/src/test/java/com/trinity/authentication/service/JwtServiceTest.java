package com.trinity.authentication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.model.UserType;
import com.trinity.user.infrastructure.security.AppUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


class JwtServiceTest {

    private static final String TEST_SECRET =
            "b24bfe05b10f876172094ffa542dd10b43437cd9934d95c844a5006e51a8038a";

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String jwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // The secret is now externalized (@Value("${jwt.secret}")), so it must be
        // injected explicitly in unit tests.
        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET);
        when(userDetails.getUsername()).thenReturn("testUser");

        jwt = Jwts.builder()
            .setSubject("testUser")
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET)), SignatureAlgorithm.HS256)
            .compact();
    }

    @Test
    void testExtractUsername() {
        // Given
        String expectedUsername = "testUser";

        // When
        String actualUsername = jwtService.extractUsername(jwt);

        // Then
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void testGenerateToken() {
        // Given
        Map<String, Object> extraClaims = new HashMap<>();

        // When
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Then
        assertNotNull(token);
    }

    @Test
    void testIsTokenValid() {
        // Given
        when(userDetails.getUsername()).thenReturn("testUser");

        // When
        boolean isValid = jwtService.isTokenValid(jwt, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        // Given
        String expiredJwt = Jwts.builder()
            .setSubject("testUser")
            .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60))
            .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 30))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET)), SignatureAlgorithm.HS256)
            .compact();

        // When & Then
        assertThrows(ExpiredJwtException.class, () -> {
            jwtService.isTokenExpired(expiredJwt);
        });
    }

    @Test
    void testExtractClaim() {
        // Given
        String expectedSubject = "testUser";

        // When
        String actualSubject = jwtService.extractClaim(jwt, Claims::getSubject);

        // Then
        assertEquals(expectedSubject, actualSubject);
    }

    @Test
    void generateToken_withAppUserDetails_embedsRoleIdFromDomainUser() {
        // Given a real AppUserDetails wrapping a domain user with a populated id —
        // this exercises the single-arg overload that dereferences getDomainUser().getId(),
        // the path the persistence adapter's id-on-save contract feeds into.
        UUID userId = UUID.randomUUID();
        Employee employee = Employee.builder()
                .id(userId)
                .email("employee@example.com")
                .hashedPassword("hash")
                .build();
        AppUserDetails appUserDetails = new AppUserDetails(employee);

        UUID expectedRoleId = UUID.nameUUIDFromBytes(
                (UserType.EMPLOYEE.name() + ":" + userId).getBytes());

        // When
        String token = jwtService.generateToken(appUserDetails);

        // Then — no NPE on getId(), subject is the email, and the roleId claim is derived.
        assertNotNull(token);
        assertEquals("employee@example.com", jwtService.extractUsername(token));
        String roleId = jwtService.extractClaim(token, claims -> claims.get("roleId", String.class));
        assertEquals(expectedRoleId.toString(), roleId);
    }
}