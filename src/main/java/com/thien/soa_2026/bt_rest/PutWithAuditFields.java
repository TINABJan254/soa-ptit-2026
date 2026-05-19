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

public class PutWithAuditFields {
    
    static class Account {
        private String status;
        private String activatedBy;
        private String auditNote;

        public void setStatus(String status) {
            this.status = status;
        }

        public void setActivatedBy(String activatedBy) {
            this.activatedBy = activatedBy;
        }

        public void setAuditNote(String auditNote) {
            this.auditNote = auditNote;
        }
        
    }
    
    static class ResponseDTO {
        private String requestId;
        private Account data;

        public String getRequestId() {
            return requestId;
        }

        public Account getData() {
            return data;
        }
    }
    
    static class SubmitDTO {
        private String studentCode;
        private String qCode;
        private Account answer;

        public SubmitDTO(String studentCode, String qCode, Account answer) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.answer = answer;
        }
    }
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "XrgRADcN";
        String url = "http://36.50.135.242:2230/api/rest/method";
        
        //client
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
        Account acc = res.getData();
        
        acc.setActivatedBy(sCode);
        acc.setStatus("ACTIVE");
        acc.setAuditNote("manual-review-ok");
        
        //put
        SubmitDTO sub = new SubmitDTO(sCode, qCode, acc);
        String putJson = gson.toJson(sub);
        
        HttpRequest putReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + res.getRequestId()))
                .PUT(HttpRequest.BodyPublishers.ofString(putJson))
                .build();
        
        client.send(putReq, HttpResponse.BodyHandlers.ofString());
    }
}

