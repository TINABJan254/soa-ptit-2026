/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_grpc;

/**
 *
 * @author ADMIN
 */

import java.io.*;

//generated class
import GRPC.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.*;

public class BatchTransactionProb {
    /*
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "8CLf3vs9";
        
        String host = "36.50.135.242";
        int port = 2240;
        
        //create channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        
        //create stub (note: suffix Grpc)
        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);
        
        //request
        TypedJudgeRequest request = TypedJudgeRequest.newBuilder() //must use newBuilder() because Generated Object is immutable
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .build();
        
        
        //send
        TypedJudgeResponse judgeResponse = stub.requestTyped(request);
        
        TransactionRiskBatchData data = judgeResponse.getTransactionRiskBatch();
        
        List<String> highRiskTransId = new ArrayList<>();
        double totalAmount = 0;
        int reviewCount = 0;
        
        for (TransactionRecord x : data.getTransactionsList()){
            boolean risk = (x.getAmount() >= 5000) || (x.getChargebackCount() >= 2) || (x.getNewDevice() && !(x.getCountry().equals("VN")));
            
            if (risk) {
                highRiskTransId.add(x.getTransactionId());
                reviewCount++;
                totalAmount += x.getAmount();
            }
        }
        
        totalAmount = Math.round(totalAmount * 100.0) / 100.0;
        
        
        //result
        //create answer
        TransactionRiskAnswer ans = TransactionRiskAnswer.newBuilder()
                .addAllHighRiskTransactionIds(highRiskTransId)
                .setReviewCount(reviewCount)
                .setTotalHighRiskAmount(totalAmount)
                .build();
        
        //request
        TypedSubmitRequest subReq = TypedSubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setRequestId(judgeResponse.getRequestId())
                .setTransactionRiskAnswer(ans)
                .build();
        
        //send
        TypedSubmitResponse subRes = stub.submitTyped(subReq);
        
        System.out.println(">>>>>>>> " + subRes);
        
    }
    */
}

/*
Nội dung
Batch đánh giá rủi ro giao dịch qua gRPC typed proto
Sử dụng TypedJudgeService. Phase 1 gọi RequestTyped với student_code và question_alias runtime để nhận TransactionRiskBatchData.

Quy tắc rủi ro
Một giao dịch cần review nếu thỏa ít nhất một điều kiện: amount >= 5000, hoặc chargeback_count >= 2, hoặc new_device = true và country != "VN".

Submit
Gọi SubmitTyped với TransactionRiskAnswer: high_risk_transaction_ids theo đúng thứ tự input, review_count, và total_high_risk_amount làm tròn 2 chữ số thập phân.
*/