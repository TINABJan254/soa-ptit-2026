/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_rest;

/**
 *
 * @author ADMIN
 */

import java.io.*;
import java.util.*;
import java.net.http.*;
import java.net.*;

import com.google.gson.*;

public class ShippingPriceSLAProb {
    
    static class Quote {
        private String carrier;
        private double baseFee;
        private double perKgFee;
        private int etaDays;
        private double reliability;
        
        public String getCarrier() {
            return carrier;
        }

        public double getBaseFee() {
            return baseFee;
        }

        public double getPerKgFee() {
            return perKgFee;
        }

        public int getEtaDays() {
            return etaDays;
        }

        public double getReliability() {
            return reliability;
        }
    }
    
    static class Order {
        private String orderId;
        private double weightKg;
        private int maxEtaDays;
        private List<Quote> quotes;

        public String getOrderId() {
            return orderId;
        }

        public double getWeightKg() {
            return weightKg;
        }

        public int getMaxEtaDays() {
            return maxEtaDays;
        }

        public List<Quote> getQuotes() {
            return quotes;
        }
    }
    
    static class ResponseDTO {
        private String requestId;   
        private Order data;

        public String getRequestId() {
            return requestId;
        }

        public Order getData() {
            return data;
        }
    }
    
    static class OrderStat {
        private String carrier;
        private double totalFee;
        private int etaDays;

        public OrderStat(String carrier, double totalFee, int etaDays) {
            this.carrier = carrier;
            this.totalFee = totalFee;
            this.etaDays = etaDays;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        public double getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(double totalFee) {
            this.totalFee = totalFee;
        }

        public int getEtaDays() {
            return etaDays;
        }

        public void setEtaDays(int etaDays) {
            this.etaDays = etaDays;
        }
    }
    
    static class SubmitDTO {
        private String studentCode, qCode, requestId;
        private OrderStat answer;

        public SubmitDTO(String studentCode, String qCode, String requestId, OrderStat answer) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.requestId = requestId;
            this.answer = answer;
        }
    }
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "GMAs2l6q";
        String url = "http://36.50.135.242:2230/api/rest/object";
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        HttpResponse<String> getRes = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(">>>>>> " + getRes.body());
        
        Gson gson = new Gson();
        ResponseDTO res = gson.fromJson(getRes.body(), ResponseDTO.class);
        
        
        double totalFee = Double.MAX_VALUE, reli = -1;
        String carrier = "";
        int etaDay = 0;
        for (Quote q : res.getData().getQuotes()) {
            double tmp = q.getBaseFee() + q.getPerKgFee() * res.getData().getWeightKg();
            if (tmp < totalFee) {
                totalFee = tmp;
                etaDay = q.getEtaDays();
                reli = q.getReliability();
                carrier = q.getCarrier();
            } else if (tmp == totalFee && q.getReliability() > reli) {
                totalFee = tmp;
                etaDay = q.getEtaDays();
                reli = q.getReliability();
                carrier = q.getCarrier();
            }
        }
        
        //sub
        OrderStat ans = new OrderStat(carrier, totalFee, etaDay);
        SubmitDTO sub = new SubmitDTO(sCode, qCode, res.getRequestId(), ans);
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "/submit"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub)))
                .build();
        HttpResponse postRes = client.send(postReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(">>>>> " + postRes.body());
    }
}
