/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_rest;

/**
 *
 * @author ADMIN
 */

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.*;
import java.net.http.*;

public class CaculateFinalPrice {
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "xsB8lBpn";
        String url = "http://36.50.135.242:2230/api/rest/object";
        
        //client
        HttpClient client = HttpClient.newHttpClient();
        
        //get
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        HttpResponse<String> getRes = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        String body = getRes.body();
        System.out.println(">>>>>> " + body);
        
        //parse json
        Gson gson = new Gson();
        ResponseObjectDTO res = gson.fromJson(body, ResponseObjectDTO.class);
        
        Product p = res.getData();
        p.setFinalPrice(p.getPrice() * (1 + p.getTaxRate()/100) * (1 - p.getDiscount()/100) );
        
        //post
        ObjectSubmitDTO sub = new ObjectSubmitDTO(sCode, qCode, res.getRequestId(), p);
        String answer = gson.toJson(sub);
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/submit", url)))
                .POST(HttpRequest.BodyPublishers.ofString(answer))
                .build();
        
        HttpResponse<String> postRes = client.send(postReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(">>>>>> " + postRes.body());
    }
}

class ResponseObjectDTO {
    private String requestId;
    private Product data;

    public ResponseObjectDTO(String requestId, Product data) {
        this.requestId = requestId;
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public Product getData() {
        return data;
    }
    
}

class Product {
    private String name;
    private double price;
    private double taxRate;
    private double discount;
    private double finalPrice;

    public Product(String name, double price, double taxRate, double discount, double finalPrice) {
        this.name = name;
        this.price = price;
        this.taxRate = taxRate;
        this.discount = discount;
        this.finalPrice = finalPrice;
    }
    
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
    
}

class ObjectSubmitDTO {
    private String studentCode;
    private String qCode;
    private String requestId;
    private Product answer;

    public ObjectSubmitDTO(String studentCode, String qCode, String requestId, Product answer) {
        this.studentCode = studentCode;
        this.qCode = qCode;
        this.requestId = requestId;
        this.answer = answer;
    }
    
    
}