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

public class ShippingPriceProb {
    
    /*
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "ClaMyPgd";
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
        TypedJudgeRequest jReq = TypedJudgeRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .build();
        
        TypedJudgeResponse jRes = stub.requestTyped(jReq);
        System.out.println(">>>>>> " + jRes);
        
        //solve
        double totalFee = Double.MAX_VALUE;
        String carrier = "";
        int etaDays = 0;
        double reliability = -1;
        
        for (CarrierQuote quote : jRes.getShippingQuote().getQuotesList()) {
            double tmpT = quote.getBaseFee() + quote.getPerKgFee() * jRes.getShippingQuote().getWeightKg();
            if (tmpT < totalFee) {
                carrier = quote.getCarrier();
                totalFee = tmpT;
                etaDays = quote.getEtaDays();
                reliability = quote.getReliability();
            } else if (tmpT == totalFee) {
                if (quote.getReliability() > reliability) {
                    carrier = quote.getCarrier();
                    totalFee = tmpT;
                    etaDays = quote.getEtaDays();
                    reliability = quote.getReliability();
                }
            }
        }
        
        //sub
        ShippingQuoteAnswer ans = ShippingQuoteAnswer.newBuilder()
                .setCarrier(carrier)
                .setEtaDays(etaDays)
                .setTotalFee(totalFee)
                .build();
        
        TypedSubmitRequest subReq = TypedSubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setRequestId(jRes.getRequestId())
                .setShippingQuoteAnswer(ans)
                .build();
        
        TypedSubmitResponse subRes = stub.submitTyped(subReq);
        System.out.println(">>>>>>>> " + subRes);
                
    }
    */
}

/*
Nội dung
Chọn báo giá vận chuyển qua gRPC typed proto
Phase 1 nhận ShippingQuoteData gồm order_id, weight_kg, max_eta_days và danh sách quotes.

Yêu cầu xử lý
Chỉ xét quote có eta_days <= max_eta_days.
Tính total_fee = base_fee + weight_kg * per_kg_fee, làm tròn 2 chữ số thập phân.
Chọn quote có phí nhỏ nhất; nếu bằng nhau, chọn reliability cao hơn.
Submit
Gọi SubmitTyped với ShippingQuoteAnswer gồm carrier, total_fee, eta_days.
*/
