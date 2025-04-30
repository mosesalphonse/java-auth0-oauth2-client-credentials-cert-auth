import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Auth0Client {

    // Define Auth0 credentials
    private static final String CLIENT_ID = ""; // replace with your Auth0 client id
    private static final String CLIENT_SECRET = ""; // replace with your Auth0 client secret
    private static final String AUTH0_DOMAIN = "https://"; // replace with your Auth0 domain (e.g., https://dev-abc.auth0.com)
    private static final String CERT_PATH = "src/main/resources/private.key"; // Path to your .key private key

    public static void main(String[] args) {
        try {
            // Load the private key from .key PEM file
            PrivateKey privateKey = loadPrivateKeyFromPEM(CERT_PATH);

            // Build the URL to obtain the access token
            String tokenUrl = AUTH0_DOMAIN + "/oauth/token";
            String requestBody = "grant_type=client_credentials&client_id=" + CLIENT_ID +
                    "&client_secret=" + CLIENT_SECRET +
                    "&audience=" + ""; // Replace with your API identifier (optional)

            // Make the POST request to Auth0 to obtain the access token
            String accessToken = getAccessToken(tokenUrl, requestBody, privateKey);
            System.out.println("Access Token: " + accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load the private key from a PEM file
    private static PrivateKey loadPrivateKeyFromPEM(String keyFilePath) throws Exception {
        FileInputStream fis = new FileInputStream(keyFilePath);
        byte[] keyBytes = new byte[fis.available()];
        fis.read(keyBytes);
        fis.close();

        // Remove the "BEGIN" and "END" markers
        String key = new String(keyBytes);
        key = key.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");

        // Decode the key using Base64 (using Java's built-in Base64 class)
        byte[] decoded = Base64.getDecoder().decode(key);

        // Use BouncyCastle to parse the private key
        Security.addProvider(new BouncyCastleProvider());

        // Using PKCS8EncodedKeySpec to decode the private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Use "RSA" or "EC" depending on your key type
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

    private static String getAccessToken(String url, String requestBody, PrivateKey privateKey) {
        try {
            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            // Build the request entity
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // Create RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Send the POST request to Auth0
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // Extract access token from the response
            String responseBody = response.getBody();
            String accessToken = parseAccessToken(responseBody);
            return accessToken;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parseAccessToken(String responseBody) {
        // A simple method to extract the access token from the JSON response
        // You can use a JSON library like Jackson or Gson here
        String tokenPrefix = "\"access_token\":\"";
        int startIndex = responseBody.indexOf(tokenPrefix) + tokenPrefix.length();
        int endIndex = responseBody.indexOf("\"", startIndex);
        return responseBody.substring(startIndex, endIndex);
    }
}
