# java-auth0-oauth2-client-credentials-cert-auth

This repository demonstrates how to implement OAuth2 client credentials flow using Auth0 and certificate-based authentication in a Java application. It provides a reference implementation for securely obtaining an access token from Auth0, leveraging a private key for authentication in a typical machine-to-machine scenario.

## POC:
<li>
Authenticate Auth0 using Certificate for Client Credentials Flow
</li>
<li>
Get Access Token using Certificate authentication instead of Client secrets so that we can access our business API, this can be used for backend components
</li>

## Prerequisite

<li>
Auth0 Account
 </li>


## Step One - Create selft signed Certificate

1) Clone the project and get into the resources folder

```
git clone https://github.com/mosesalphonse/java-auth0-oauth2-client-credentials-cert-auth.git

cd src/main/resources
```

2) Generate the private and public keys

```
openssl genpkey -algorithm RSA -out private.key

openssl rsa -pubout -in private.key -out public.key
```

## Step Two - Set Up Auth0 Application

Go to the Auth0 Dashboard.

Create a new Machine-to-Machine Application.

Choose the appropriate API (Makesure you copy the 'IDENTIFIER' of your API).

Under the Settings, you'll get your Client ID and Client Secret. These are useful for client credentials flow.

## Step Three - Configure Auth0 with Certificate for Client Authentication

Upload the public key of your certificate in Auth0.

    Go to Applications → Certificates → Add Certificate.

    Upload the public key or paste the certificate into the form.

  Configure the client secret for machine-to-machine communication (you can optionally use a certificate for JWT signing).

## Step Four - Update the code

Update Auth0Client.java for the following value you have copied in the previous steps
 <li>
CLIENT_ID
 </li>
 <li>
CLIENT_SECRET
 </li>
 <li>
AUTH0_DOMAIN
 </li>
 <li>
audience (value of the identifier in your API)
 </li>

 ## Step Five - Get Access token using Certificate based authentication

 Run Auth0Client.java's main method, if everything are configured correctly, you may get the access token using client credentials flow by authenticating certificate instead of client secret
