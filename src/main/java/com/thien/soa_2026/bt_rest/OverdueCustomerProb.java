/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_rest;

/**
 *
 * @author ADMIN
 */

import java.util.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import com.google.gson.*;

public class OverdueCustomerProb {
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "eQbPvzUw";
        String url = "http://36.50.135.242:2230/api/rest/path";
        
        HttpClient client = HttpClient.newHttpClient();
        
        //req
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        HttpResponse<String> getRes = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        JsonObject obj = JsonParser.parseString(getRes.body()).getAsJsonObject();
        System.out.println(">>>>> " + obj);
        
        JsonObject ans = new JsonObject();
        ans.addProperty("overdueAmount", -1);
        
        JsonArray a = obj.getAsJsonArray("data");
        for (JsonElement e : a) {
            
            JsonObject tmp = e.getAsJsonObject();
            
            if (tmp.get("status").getAsString().equals("OVERDUE")) {
                if (tmp.get("overdueAmount").getAsDouble() > ans.get("overdueAmount").getAsDouble()) {
                    ans.addProperty("overdueAmount", tmp.get("overdueAmount").getAsDouble());
                    ans.addProperty("customerId", tmp.get("customerId").getAsString());
                    ans.addProperty("page", tmp.get("page").getAsInt());
                }
            }
        }
        
        //sub
        HttpRequest req2 = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%s?studentCode=%s&qCode=%s&requestId=%s&status=OVERDUE&page=%d", 
                        url, ans.get("customerId").getAsString(),
                        sCode, qCode, obj.get("requestId").getAsString(),
                        ans.get("page").getAsInt())))
                .GET()
                .build();
        
        client.send(req2, HttpResponse.BodyHandlers.ofString());
    }
}