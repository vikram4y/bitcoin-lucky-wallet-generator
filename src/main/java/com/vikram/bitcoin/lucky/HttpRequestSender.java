package com.vikram.bitcoin.lucky;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
public class HttpRequestSender {

    public static String sendPostRequest(String url) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                return "No response received.";
            }
        } catch (Exception e) {
            System.out.println("Exception occured while calling HTTPS request: "+ e.getMessage());
            e.printStackTrace();
            return "NA";
        }
    }
}
