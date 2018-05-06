/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quangvn.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author quangvn
 */
public class VerifyRecaptchaUtils {

    public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public static final String SECRET_KEY = "6LelZAsTAAAAAKa_s6g2yuJzByYlk0FH_6iTkZJC";

    public static boolean verify(String responseRecaptcha) {
        if (responseRecaptcha == null || responseRecaptcha.length() == 0) {
            return false;
        } else {
            try {
                URL verifyUrl = new URL(SITE_VERIFY_URL);
                HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String postParams = "secret=" + SECRET_KEY + "&response=" + responseRecaptcha;

                conn.setDoOutput(true);

                OutputStream outStream = conn.getOutputStream();
                outStream.write(postParams.getBytes());

                outStream.flush();
                outStream.close();

                int responseCode = conn.getResponseCode();
                System.out.println("responseCode=" + responseCode);

                InputStream is = conn.getInputStream();

                JsonReader jsonReader = Json.createReader(is);
                JsonObject jsonObject = jsonReader.readObject();
                jsonReader.close();

                System.out.println("Response: " + jsonObject);

                boolean success = jsonObject.getBoolean("success");
                return success;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
