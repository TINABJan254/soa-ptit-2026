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
import io.grpc.*;
import java.io.*;

import GRPC.*;

public class SensorTelemetryProb {
    
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "w0pAnm7a";
        String host = "36.50.135.242";
        int port = 2240;
        
        // channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        
        //stub
        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);
        
        
        //request (get data)
        TypedJudgeRequest judgeReq = TypedJudgeRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .build();
        
        TypedJudgeResponse judgeRes = stub.requestTyped(judgeReq);
        System.out.println(">>>>>> " + judgeRes);
        
        //solve
        double avg = 0;
        int cnt = 0;    
        List<SensorReading> a = new ArrayList<>(judgeRes.getSensorTelemetry().getReadingsList()); //returned list is immutable

        Collections.sort(a, new Comparator<>(){
            @Override
            public int compare(SensorReading o1, SensorReading o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        
        for (SensorReading x : a) {
            avg += x.getValue();
            if (x.getValue() > judgeRes.getSensorTelemetry().getThreshold()) {
                cnt++;
            }
        }
        int pos = (int) Math.ceil(a.size() * 0.95) - 1;
        double p95 = Math.round(a.get(pos).getValue() * 100.0) / 100.0;
        avg = Math.round(avg / a.size() * 100.0) / 100.0;
        
        //result
        SensorTelemetryAnswer ans = SensorTelemetryAnswer.newBuilder()
                .setAverage(avg)
                .setP95(p95)
                .setAnomalyCount(cnt)
                .build();
        
        TypedSubmitRequest subReq = TypedSubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setRequestId(judgeRes.getRequestId())
                .setSensorTelemetryAnswer(ans)
                .build();
        
        TypedSubmitResponse subRes = stub.submitTyped(subReq);
        System.out.println(">>>>>> " + subRes);

    }   
    
}

/*
Nội dung
Tổng hợp telemetry cảm biến qua gRPC typed proto
Sử dụng TypedJudgeService.RequestTyped để nhận SensorTelemetryData gồm threshold và danh sách readings.

Yêu cầu xử lý
average: trung bình tất cả value, làm tròn 2 chữ số thập phân.
p95: sắp xếp value tăng dần, lấy phần tử tại vị trí ceil(n * 0.95) - 1, làm tròn 2 chữ số thập phân.
anomaly_count: số reading có value > threshold.
Submit
Gọi SubmitTyped với SensorTelemetryAnswer. Judge chấp nhận sai số nhỏ cho số thực theo handler.
*/