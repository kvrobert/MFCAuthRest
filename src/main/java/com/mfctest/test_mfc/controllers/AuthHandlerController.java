package com.mfctest.test_mfc.controllers;

import com.mfctest.test_mfc.model.MCFAuthorizerException;
import com.mfctest.test_mfc.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthHandlerController {

    private List<Token> accessTokens;
    private static final Logger LOG = LoggerFactory.getLogger(AuthHandlerController.class);
    private String BASE_URL = "http://localhost:8345/mcf-authority-service";

    @RequestMapping("/auth")
    public /*List<Token> */  String getAllowsTokens(@RequestParam(value = "AuthenticatedUserName", defaultValue = "noUserName") String domain) throws IOException {

        StringBuilder urlBuffer = new StringBuilder(BASE_URL);
        urlBuffer.append("/UserACLs");

        // ha az érkezett domain nev > 0, akkor hibás legyen, csak 1 userrel dolgozunk

        urlBuffer.append("?");
        urlBuffer.append("username=");
        // ha a domain nem szabványos email cím...hiba
        urlBuffer.append(domain);


        URL authURL = new URL(urlBuffer.toString());
        HttpURLConnection connection = (HttpURLConnection) authURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("content-Type", "text/html");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        //        connection.setInstanceFollowRedirects(false);

        List<String> tokenList = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            System.out.println("ResponseCode: " + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                while (true) {
                    String line = in.readLine();
                    if (line == null)
                        break;
                    if (line.startsWith("TOKEN:")) {
                        tokenList.add(line.substring("TOKEN:".length()));
                    } else {
                        LOG.info("Authory response..." + line);
                    }
                }
            } else {
                LOG.warn("Authentication error.");
                throw new MCFAuthorizerException("Authentication error.");
            }


        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new MCFAuthorizerException(ex.getMessage());
        }
        System.out.println(tokenList);
        return tokenList.toString();
    }
}
