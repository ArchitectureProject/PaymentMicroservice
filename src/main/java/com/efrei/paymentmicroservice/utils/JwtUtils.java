package com.efrei.paymentmicroservice.utils;

import com.efrei.paymentmicroservice.config.Properties;
import com.efrei.paymentmicroservice.model.UserRole;
import com.efrei.paymentmicroservice.exception.custom.JWTException;
import com.efrei.paymentmicroservice.exception.custom.WrongUserRoleException;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.springframework.stereotype.Service;

@Service
public class JwtUtils {
    private final Properties properties;

    public JwtUtils(Properties properties) {
        this.properties = properties;
    }

    public boolean validateJwt(String jwt, UserRole expectedRole) {
        HttpsJwks httpsJkws = new HttpsJwks(properties.getJwkUrl());

        HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setExpectedIssuer("UserMicroservice") // whom the JWT needs to have been issued by
                .setExpectedAudience("OtherMicroservices") // to whom the JWT is intended for
                .setVerificationKeyResolver(httpsJwksKeyResolver)
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
                .build();

        JwtClaims jwtClaims;
        try
        {
            jwtClaims = jwtConsumer.processToClaims(jwt);
        }
        catch (InvalidJwtException e)
        {
            if (e.hasExpired())
            {
                throw new JWTException("JWT expiré");
            }

            throw new JWTException("JWT invalide", e);
        }

        if(expectedRole != null){
            return checkRole(jwtClaims, expectedRole);
        }

        return true;
    }

    private boolean checkRole(JwtClaims jwtClaims, UserRole expectedRole){
        if(!jwtClaims.hasClaim("role")){
            return false;
        }

        String jwtRole;
        try {
            jwtRole = jwtClaims.getStringClaimValue("role");
        }
        catch (MalformedClaimException e){
            throw new WrongUserRoleException("JWT does not carry informations about user role");
        }

        return jwtRole.equals(expectedRole.toString());
    }


}
