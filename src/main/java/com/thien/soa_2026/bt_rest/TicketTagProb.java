/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_rest;

/**
 *
 * @author ADMIN
 */

import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.net.http.*;
import java.net.*;

public class TicketTagProb {
    
    static class Ticket {
        private String ticketId, status, targetStatus, etag;
        private int version;

        public String getTicketId() {
            return ticketId;
        }

        public String getStatus() {
            return status;
        }

        public String getTargetStatus() {
            return targetStatus;
        }

        public String getEtag() {
            return etag;
        }

        public int getVersion() {
            return version;
        }       
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
    
    static class ResponseDTO {
        private String requestId;
        private Ticket data;

        public String getRequestId() {
            return requestId;
        }

        public Ticket getData() {
            return data;
        }
    }
    
    static class SubmitDTO {
        private String studentCode, qCode;
        private Ticket answer;

        public SubmitDTO(String studentCode, String qCode, Ticket answer) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.answer = answer;
        }
    }
    
    public static void main(String[] args) throws Exception{
        
        String sCode = "B22DCCN827";
        String qCode = "wdLVyLgI";
        String url = "http://36.50.135.242:2230/api/rest/method";
        
        HttpClient client = HttpClient.newHttpClient();
        
        //req
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        HttpResponse<String> getRes = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(">>>>>>> " + getRes.body());
        
        Gson gson = new Gson();
        ResponseDTO res = gson.fromJson(getRes.body(), ResponseDTO.class);
        
        Ticket t = res.getData();
        t.setStatus("RESOLVED");
        
        //sub
        SubmitDTO sub = new SubmitDTO(sCode, qCode, t);
        
        HttpRequest patchReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + res.getRequestId()))
                .header("If-Match", res.getData().getEtag())
                .method("PATCH", HttpRequest.BodyPublishers.ofString(gson.toJson(sub)))
                .build();
        
        client.send(patchReq, HttpResponse.BodyHandlers.ofString());
        
    }
}
