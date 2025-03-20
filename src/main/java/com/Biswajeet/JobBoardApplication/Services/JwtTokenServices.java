package com.Biswajeet.JobBoardApplication.Services;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.security.Key;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JwtTokenServices {
//
//        private String SECRET_KEY;
//        public JwtTokenServices(){
//            this.SECRET_KEY=generateSecretKey();
//        }
//
//         private String generateSecretKey() {
//           try{
//              KeyGenerator keyGen= KeyGenerator.getInstance("HmacSHA256");
//              SecretKey secretKey=keyGen.generateKey();
//              return Base64.getEncoder().encodeToString(secretKey.getEncoded());
//           }
//           catch (NoSuchAlgorithmException e){
//              throw new RuntimeException("Error generating Secret key!!");
//           }
//        }
//    Map<String,Object> claims= new HashMap<>();
//
//    public String generateToken(String username){
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis()+2000*60*5))
//                .signWith(getKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Key getKey() {
//        byte[] KeyBytes= Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(KeyBytes);
//    }
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
//        final Claims claims=extractAllClaims(token);
//        return claimResolver.apply(claims);
//    }
//    private Claims extractAllClaims(String token){
//        return (Jwts.parserBuilder()
//                .setSigningKey(getKey())
//                .build().parseClaimsJws(token).getBody());
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String userName=extractUsername(token);
//        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token,Claims::getExpiration);
//    }
//
//}
//
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenServices {

    private static final String SECRET_KEY=generateSecretKey();
    private static final SecretKey secretKey= Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
    private final Map<String, Object> claims = new HashMap<>();

//    public JwtTokenServices() {
//        this.SECRET_KEY = generateSecretKey();
//        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
//    }

    // Generate a random base64 encoded secret key
    private static String generateSecretKey() {
        byte[] keyBytes = new byte[32]; // 256 bits
        new java.security.SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public String generateToken(String username) {
        System.out.println("Generating token for user: " + username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 20000 * 60 * 5))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        System.out.println("Secret Key: " + SECRET_KEY);
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

