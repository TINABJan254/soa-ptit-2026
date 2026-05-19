/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_grpc;

/**
 *
 * @author ADMIN
 */

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

//generated class
import GRPC.*;

import java.io.*;
import java.util.*;

public class DataSumIntegerProb {
    public static void main(String[] args) throws Exception{
        
        String host = "36.50.135.242";
        int port = 2240;
        
        String sCode = "B22DCCN827";
        String qCode = "9BlXf4IM";
        
        //create channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext() //tat SSL
                .build();
        
        JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);
        
        //request
        JudgeRequest request = JudgeRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .build();
        
        //send request
        JudgeResponse response = stub.request(request);
        
        //get data from response
        String reqId = response.getRequestId();
        String data  = response.getData();
        
        System.out.println(">>>>>> " + reqId + " " + data);
        
        //solve
        long sum = 0;
        String[] a = data.split(",");
        for (String x : a) {
            sum += Long.parseLong(x);
        }
        System.out.println(">>>>> " + sum);
        
        //submit
        SubmitRequest subReq = SubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setRequestId(reqId)
                .setAnswer(String.valueOf(sum))
                .build();
        
        SubmitResponse subRes = stub.submit(subReq);
        System.out.println(subRes);
    }
    
}