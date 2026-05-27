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

public class QueryInvoiceProb {
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "j7J4Mezf";
        String url = "http://36.50.135.242:2230/api/rest/path";
        
        HttpClient client = HttpClient.newHttpClient();
        
        //req
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        HttpResponse<String> getRes = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        
        JsonObject o1 = JsonParser.parseString(getRes.body()).getAsJsonObject();
        System.out.println(">>>>>>>>> " + o1);
        
        String invoiceId = o1.getAsJsonArray("data").get(0).getAsJsonObject().get("id").getAsString();
        
        HttpRequest req2 = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%s?studentCode=%s&qCode=%s&requestId=%s&currency=USD", 
                        url, invoiceId, sCode, qCode, o1.get("requestId").getAsString())))
                .GET()
                .build();
        
        client.send(req2, HttpResponse.BodyHandlers.ofString());
        
    }
}
