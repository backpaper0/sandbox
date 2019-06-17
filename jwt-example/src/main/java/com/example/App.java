package com.example;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class App {

    public static void main(final String[] args) throws Exception {

        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        final KeyPair keyPair = generator.generateKeyPair();

        final String token = Jwts.builder()
                .setSubject("hello world")
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();

        System.out.println(token);

        final String subject = Jwts.parser()
                .setSigningKey(keyPair.getPublic())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        System.out.println(subject);
    }
}
