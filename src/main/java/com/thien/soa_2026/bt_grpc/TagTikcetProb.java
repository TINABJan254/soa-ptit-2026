/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_grpc;

/**
 *
 * @author ADMIN
 */

import java.util.*;
import java.io.*;

import io.grpc.*;

import GRPC.*;

public class TagTikcetProb {
    
    /*
    public static void main(String[] args) {
        String sCode = "B22DCCN827";
        String qCode = "mYP6gDJr";
        String host = "36.50.135.242";
        int port = 2240;
        
        //channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        
        //stub
        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);
        
        //req
        TypedJudgeRequest judgeReq = TypedJudgeRequest.newBuilder()
                .setQuestionAlias(qCode)
                .setStudentCode(sCode)
                .build();
        
        TypedJudgeResponse judgeRes = stub.requestTyped(judgeReq);
        System.out.println(">>>>>> " + judgeRes);
        
        //solve
        TextBatchData data = judgeRes.getTextBatch();
        String[] keywords = {"account", "payment", "refund", "shipping"};
        Map<String, Integer> counts = new HashMap<>();
        
        for (String entry : data.getEntriesList()) {
            for (String key : keywords) {
                if (entry.toLowerCase().contains(key)) {
                    counts.put(key, counts.getOrDefault(key, 0) + 1);
                }
            }
        }
        
        //sub
        List<String> values = new ArrayList<>(counts.keySet());
        Collections.sort(values);
        
        TextBatchAnswer ans = TextBatchAnswer.newBuilder()
                .addAllValues(values)
                .putAllCounts(counts)
                .build();
        
        TypedSubmitRequest subReq = TypedSubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setRequestId(judgeRes.getRequestId())
                .setTextBatchAnswer(ans)
                .build();
        
        TypedSubmitResponse subRes = stub.submitTyped(subReq);
        System.out.println(">>>>>> " + subRes);
    }
    */
}
/*
Nội dung
Gắn tag ticket hỗ trợ qua gRPC typed proto
Phase 1 nhận TextBatchData với mode=ticket_tags và danh sách entries.

Yêu cầu xử lý
Đếm số entry chứa từng keyword: account, payment, refund, shipping.
counts: map từ keyword xuất hiện sang số lần xuất hiện.
values: danh sách các keyword xuất hiện, sắp xếp tăng dần theo alphabet.
Submit
Gọi SubmitTyped với TextBatchAnswer gồm values và counts.
*/