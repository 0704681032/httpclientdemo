package com.example.httpclientdemo.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class JsonApiController {
    private static final Logger LOG = LoggerFactory.getLogger(JsonApiController.class);
    private final RestTemplate restTemplate;

    @Autowired
    public JsonApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/fetch-json")
    public String fetchJsonData() {
        String jsonUrl = "https://jsonplaceholder.typicode.com/posts/1";
        String jsonResponse = restTemplate.getForObject(jsonUrl, String.class);
        return jsonResponse;
    }

    @RequestMapping(value = "/send-json",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String sendJsonData() {
        String remoteUrl = "http://localhost:8080/api/process-json?a=xx&b=yy&accessToken={token}&requestId={id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonBody = "{\"key\":\"valuejyy\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(remoteUrl, HttpMethod.POST, requestEntity, String.class, UUID.randomUUID().toString(),System.currentTimeMillis());
        String responseBody = responseEntity.getBody();
        return responseBody;
    }



    @PostMapping("/process-json")
    public String processJsonData(@RequestBody String jsonData, HttpServletRequest request) {
        LOG.info("get json from " + jsonData);
        LOG.info(request.getRequestURI());
        LOG.info(request.getQueryString());
        request.getHeaderNames().asIterator().forEachRemaining(header ->{
            LOG.info("{}---->{}" ,header,request.getHeader(header));
        });
        String responseJson = "{\"message\":\"BBBBJSON22333@@@@@@ data received and processedTETETE successfully. AADADDADAhaaha:() by devtool\"}";
        return responseJson;
    }


}
