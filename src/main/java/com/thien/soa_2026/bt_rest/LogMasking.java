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
import com.google.gson.Gson;

public class LogMasking {
    
    static class ResponseDTO {
        private String requestId;
        private String data;

        public String getRequestId() {
            return requestId;
        }

        public String getData() {
            return data;
        }
        
    }
    
    static class SubmitDTO {
        private String studentCode;
        private String qCode;
        private String requestId;
        private String answer;

        public SubmitDTO(String studentCode, String qCode, String requestId, String answer) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.requestId = requestId;
            this.answer = answer;
        }
        
    }
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "pgY7p3hk";
        String url = "http://36.50.135.242:2230/api/rest/character";
        
        //client
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        HttpResponse<String> res1 = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        String body = res1.body();
        
        System.out.println(">>>>> " + body);
        
        Gson gson = new Gson();
        ResponseDTO res = gson.fromJson(body, ResponseDTO.class);
        
        
    }
}
