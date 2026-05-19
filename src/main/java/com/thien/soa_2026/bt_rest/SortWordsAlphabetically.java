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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class SortWordsAlphabetically {
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "R2hBIxsz";
        String url = "http://36.50.135.242:2230/api/rest/character";
        
        HttpClient client = HttpClient.newHttpClient();
        
        //req1
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        //send req1
        HttpResponse<String> getResponse = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        String body = getResponse.body();
        
        System.out.println(">>>> " + body);
        
        //parse json
        Gson gson = new Gson();
        ResponseDTO res = gson.fromJson(body, ResponseDTO.class);
        
        //solve
        String[] words = res.getData().trim().split("\\s+");
        Arrays.sort(words);
        String answer = String.join(" ", words);
        
        //post
        SubmitDTO sub = new SubmitDTO(sCode, qCode, res.getRequestId(), answer);
        String postJson = gson.toJson(sub);
        
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/submit?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .POST(HttpRequest.BodyPublishers.ofString(postJson))
                .build();
        
        HttpResponse<String> postRes = client.send(postReq, HttpResponse.BodyHandlers.ofString());
        
        System.out.println(">>>>>>> " + postRes.body());
        
    }
}

class ResponseDTO {
    private String requestId;
    private String data;

    public ResponseDTO(String requestId, String data) {
        this.requestId = requestId;
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getData() {
        return data;
    }
}

class SubmitDTO {
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

/*
Một dịch vụ REST được triển khai trên server tại URL http://<Exam_IP>:2230/api/rest/character để xử lý các bài toán về chuỗi và ký tự.

Yêu cầu: Viết chương trình Java (REST client) để giao tiếp với CharacterService và thực hiện các công việc sau:

Gửi HTTP GET request tới /api/rest/character?studentCode=<mã_sv>&qCode=<qCode trong đề bài> để nhận về một đối tượng JSON từ server.

Response JSON có dạng:

{
  "requestId": "x1y2z3w4",
  "data": "banana apple cherry date elderberry"
}
Tách chuỗi thành các từ dựa trên khoảng trắng, sau đó sắp xếp các từ theo thứ tự từ điển (alphabetical order, phân biệt hoa thường - case-sensitive).

Gửi HTTP POST request tới /api/rest/character/submit với body JSON:

{
  "studentCode": "B21DCCN001",
  "qCode": "<qCode trong đề bài>",
  "requestId": "x1y2z3w4",
  "answer": "apple banana cherry date elderberry"
}
Trong body JSON trên, trường "requestId": "x1y2z3w4" là requestId nhận được ở bước 1.

Các từ nối lại bằng dấu cách đơn.

Ví dụ 1: "banana apple cherry" -> sắp xếp -> "apple banana cherry"

Ví dụ 2 (case-sensitive): "Cherry apple Banana" -> sắp xếp theo thứ tự từ điển -> "Banana Cherry apple" (chữ hoa đứng trước chữ thường trong ASCII)

Kết thúc chương trình client. Server trả về {"status":"AC"} hoặc {"status":"WA"}.
*/