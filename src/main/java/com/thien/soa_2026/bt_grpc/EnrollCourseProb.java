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
import java.util.*;

import io.grpc.*;
import GRPC.*;

public class EnrollCourseProb {
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "P6hOAGMG";
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
        List<String> missingCourses = new ArrayList<>();
        
        for (String c : jRes.getEnrollment().getRequiredCoursesList()) {
            if (!jRes.getEnrollment().getCompletedCoursesList().contains(c)) {
                missingCourses.add(c);
            }
        }
       
        double gpaGap = Math.max(0, jRes.getEnrollment().getMinGpa()- jRes.getEnrollment().getGpa());
        boolean eligible = false;
        if (gpaGap == 0.0 && missingCourses.size() == 0) {
            eligible = true;
        }
        
        EnrollmentAnswer ans = EnrollmentAnswer.newBuilder()
                .setEligible(eligible)
                .addAllMissingCourses(missingCourses)
                .setGpaGap(gpaGap)
                .build();
        
        //sub
        TypedSubmitRequest subReq = TypedSubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setRequestId(jRes.getRequestId())
                .setEnrollmentAnswer(ans)
                .build();
        
        TypedSubmitResponse subRes = stub.submitTyped(subReq);
        System.out.println(">>>>>> " + subRes);
    }
    
}

/*
Nội dung
Kiểm tra điều kiện đăng ký học phần qua gRPC typed proto
Phase 1 nhận EnrollmentData gồm student_id, completed_courses, required_courses, gpa, min_gpa.

Yêu cầu xử lý
missing_courses: các môn trong required_courses nhưng chưa có trong completed_courses, sắp xếp tăng dần.
gpa_gap: max(0, min_gpa - gpa), làm tròn 2 chữ số thập phân.
eligible: true khi không thiếu môn và gpa_gap = 0, ngược lại false.
Submit
Gọi SubmitTyped với EnrollmentAnswer gồm eligible, missing_courses, gpa_gap.
*/