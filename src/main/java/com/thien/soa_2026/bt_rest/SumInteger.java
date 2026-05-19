/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.google.gson.Gson;


/**
 *
 * @author ADMIN
 */
public class SumInteger {
    public static void main(String[] args) throws Exception {
        String url = "http://36.50.135.242:2230/api/rest/data";
        String sCode = "B22DCCN827";
        String qCode = "fGEBzYDw";
        
        //create client
        HttpClient client = HttpClient.newHttpClient();
        
        //create request
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "?studentCode=" + sCode + "&qCode=" + qCode))
                .GET()
                .build();
        
        //send
        HttpResponse<String> getResponse = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        
        String body = getResponse.body();
        
        System.out.println(">>>>>>>>>" + body);
        
        //parse json
        Gson gson = new Gson();
        DataResponse res = gson.fromJson(body, DataResponse.class);
        
        long sum = 0;
        for (int x : res.getData()) {
            sum += x;
        }
        
        //post
        DataSubmit sub = new DataSubmit(sCode, qCode, res.getRequestId(), sum);
        String postJson = gson.toJson(sub);
        
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postJson))
                .build();
        
        HttpResponse<String> postResponse = client.send(postReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(">>>>>>>>> " + postResponse.body());
    }
}

class DataResponse {
    private String requestId;
    private List<Integer> data;

    public DataResponse(String requestId, List<Integer> data) {
        this.requestId = requestId;
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public List<Integer> getData() {
        return data;
    }
}

class DataSubmit {
    private String studentCode;
    private String qCode;
    private String requestId;
    private long answer;
    
    public DataSubmit(String studentCode,String qCode, String requestId, long answer) {
        this.studentCode = studentCode;
        this.qCode = qCode;
        this.requestId = requestId;
        this.answer = answer;
    }
    
}

/*
Nội dung
Một dịch vụ REST được triển khai trên server tại URL http://<Exam_IP>:2230/api/rest/data để xử lý các bài toán với dữ liệu nguyên thủy.

Yêu cầu: Viết chương trình Java (REST client) để giao tiếp với DataService và thực hiện các công việc sau:

Gửi HTTP GET request tới /api/rest/data?studentCode=<mã_sv>&qCode=<qCode trong đề bài> để nhận về một đối tượng JSON từ server.

Response JSON có dạng:

{
  "requestId": "a1b2c3d4",
  "data": [7602, 9136, 1090, 3431, 7830, 6179]
}
Thực hiện tính tổng của tất cả các phần tử trong danh sách số nguyên nhận được.

Gửi HTTP POST request tới /api/rest/data/submit với body JSON:

{
  "studentCode": "B21DCCN001",
  "qCode": "<qCode trong đề bài>",
  "requestId": "a1b2c3d4",
  "answer": 35268
}
Trong body JSON trên, trường "requestId": "a1b2c3d4" là requestId nhận được ở bước 1.

Ví dụ: Nếu danh sách số nguyên nhận được là [1, 2, 3, 4, 5], chương trình client tính tổng là 15 và gửi "answer": 15.

Kết thúc chương trình client. Server trả về {"status":"AC"} hoặc {"status":"WA"}.
*/