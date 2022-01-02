package com.federicoboni.services;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

public class JwtTokenService {

    private static final RsaJsonWebKey RSA_JSON_WEB_KEY = keyGen();
    private static final String JWT_KEY_NAME = "K1";
    private static final String ISSUER = "com.federicoboni";
    private static final int TIME_TO_EXPIRE = 30; // value for access token. Value for refresh token is 40 times this
                                                  // value
    private static final int RSA_KEY_BITS = 2048;
    private static final int SLACK_VALIDATING_SECONDS = 30; // Time slack for clock skew
    private static final String HEADER_PREFIX = "Bearer "; // as per RFC6750
    public static final String ACCESS_TOKEN_TYPE = "access_token";
    public static final String REFRESH_TOKEN_TYPE = "refresh_token";
    // Generate an RSA key pair, which will be used for signing and verification of
    // the JWT, wrapped in a JWK

    private static RsaJsonWebKey keyGen() {
        try {
            return RsaJwkGenerator.generateJwk(RSA_KEY_BITS);
        } catch (JoseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateJwtRefreshToken(String id) throws JoseException {
        // Give the JWK a Key ID (kid), which is just the polite thing to do
        RSA_JSON_WEB_KEY.setKeyId(JWT_KEY_NAME);

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        // who creates the token and signs it
        claims.setIssuer(ISSUER);
        // time when the token will expire (timeToExpire minutes from now)
        claims.setExpirationTimeMinutesInTheFuture(TIME_TO_EXPIRE * 40);
        // a unique identifier for the token
        claims.setGeneratedJwtId();
        // when the token was issued/created (now)
        claims.setIssuedAtToNow();
        // time before which the token is not yet valid (2 minutes ago)
        claims.setNotBeforeMinutesInThePast(2);
        // transmit the user id for later authentication
        claims.setClaim("id", id);
        claims.setClaim("type", REFRESH_TOKEN_TYPE);

        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();
        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());
        // The JWT is signed using the private key
        jws.setKey(RSA_JSON_WEB_KEY.getPrivateKey());

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.setKeyIdHeaderValue(RSA_JSON_WEB_KEY.getKeyId());

        // Set the signature algorithm on the JWT/JWS that will integrity protect the
        // claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
        String jwt = jws.getCompactSerialization();

        // Now you can do something with the JWT. Like send it to some other party
        return HEADER_PREFIX + jwt;
    }

    public static String generateJwtAccessToken(String id) throws JoseException {
        // Give the JWK a Key ID (kid), which is just the polite thing to do
        RSA_JSON_WEB_KEY.setKeyId(JWT_KEY_NAME);

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        // who creates the token and signs it
        claims.setIssuer(ISSUER);
        // time when the token will expire (timeToExpire minutes from now)
        claims.setExpirationTimeMinutesInTheFuture(TIME_TO_EXPIRE);
        // a unique identifier for the token
        claims.setGeneratedJwtId();
        // when the token was issued/created (now)
        claims.setIssuedAtToNow();
        // time before which the token is not yet valid (2 minutes ago)
        claims.setNotBeforeMinutesInThePast(2);
        // transmit the user id for later authentication
        claims.setClaim("id", id);
        claims.setClaim("type", ACCESS_TOKEN_TYPE);

        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();
        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());
        // The JWT is signed using the private key
        jws.setKey(RSA_JSON_WEB_KEY.getPrivateKey());

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.setKeyIdHeaderValue(RSA_JSON_WEB_KEY.getKeyId());

        // Set the signature algorithm on the JWT/JWS that will integrity protect the
        // claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
        String jwt = jws.getCompactSerialization();

        // Now you can do something with the JWT. Like send it to some other party
        return HEADER_PREFIX + jwt;
    }

    public static String validateJwtToken(String jwt) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                // the JWT must have an expiration time
                .setRequireExpirationTime()
                // but the expiration time can't be too crazy
                .setMaxFutureValidityInMinutes(TIME_TO_EXPIRE)
                // allow some leeway in validating time based claims to account for clock skew
                .setAllowedClockSkewInSeconds(SLACK_VALIDATING_SECONDS)
                // whom the JWT needs to have been issued by
                .setExpectedIssuer(ISSUER)
                // verify the signature with the public key
                .setVerificationKey(RSA_JSON_WEB_KEY.getKey())
                .build();

        // Validate the JWT and process it to the Claims
        JwtClaims jwtClaims = jwtConsumer.processToClaims(prefixRemover(jwt));
        System.out.println("JWT validation succeeded! " + jwtClaims);

        // validate and return the encoded user id
        return jwtClaims.getClaimsMap().get("id").toString();
    }

    public static String validateJwtToken(String jwt, String type) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                // the JWT must have an expiration time
                .setRequireExpirationTime()
                // but the expiration time can't be too crazy
                .setMaxFutureValidityInMinutes((type == ACCESS_TOKEN_TYPE) ? TIME_TO_EXPIRE : TIME_TO_EXPIRE * 40)
                // allow some leeway in validating time based claims to account for clock skew
                .setAllowedClockSkewInSeconds(SLACK_VALIDATING_SECONDS)
                // whom the JWT needs to have been issued by
                .setExpectedIssuer(ISSUER)
                // verify the signature with the public key
                .setVerificationKey(RSA_JSON_WEB_KEY.getKey())
                .build();

        // Validate the JWT and process it to the Claims
        JwtClaims jwtClaims = jwtConsumer.processToClaims(prefixRemover(jwt));
        if (!jwtClaims.getClaimsMap().get("type").toString().equals(type))
            throw new InvalidJwtException("Wrong token type!");
        System.out.println("JWT validation succeeded! " + jwtClaims);

        // validate and return the encoded user id
        return jwtClaims.getClaimsMap().get("id").toString();
    }

    private static String prefixRemover(String jwt) {
        return jwt.substring(HEADER_PREFIX.length());
    }
}