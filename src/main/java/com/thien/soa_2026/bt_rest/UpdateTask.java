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
import java.util.*;
import java.io.*;
import java.net.*;
import java.net.http.*;

public class UpdateTask {
    
    static class Task {
        private String id;
        private String title;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
    
    static class ResponseDTO {
        private String requestId;
        private Task data;

        public String getRequestId() {
            return requestId;
        }

        public Task getData() {
            return data;
        }
    }
    
    static class SubmitDTO {
        private String studentCode;
        private String qCode;
        private Task answer;

        public SubmitDTO(String studentCode, String qCode, Task answer) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.answer = answer;
        }
    }
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "86uwH0Hf";
        String url = "http://36.50.135.242:2230/api/rest/method";
        
        //get
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        HttpResponse<String> res1 = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        String body = res1.body();
        System.out.println(">>>>>> " + body);
        
        //json
        Gson gson = new Gson();
        ResponseDTO res = gson.fromJson(body, ResponseDTO.class);
        Task t = res.getData();
        t.setStatus("done");
        
        SubmitDTO sub = new SubmitDTO(sCode, qCode, t);
        String putJson = gson.toJson(sub);
        HttpRequest putReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + res.getRequestId()))
                .PUT(HttpRequest.BodyPublishers.ofString(putJson))
                .build();
        
        HttpResponse<String> res2 = client.send(putReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(">>>>>> " + res2.body());
    }
}

