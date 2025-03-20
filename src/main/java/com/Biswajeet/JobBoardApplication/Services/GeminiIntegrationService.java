package com.Biswajeet.JobBoardApplication.Services;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Map;

@Service
public class GeminiIntegrationService {

   private final WebClient webClient;

   public GeminiIntegrationService(WebClient.Builder webClient){
       this.webClient= webClient.build();
   }

   @Value("${gemini.api.url}")
   private String geminiApiUrl;

    @Value("${gemini.api.key}")
   private String geminiApiKey;

    public String[] getQuery(String message, String command) {
       // building the query
       Map<String,Object> payload=Map.of(
               "contents",new Object[] {
                       Map.of("parts",new Object[]{
                               Map.of("text",command+message)
                       })
               }
       );

       String response=  webClient
                .post()
                .uri(geminiApiUrl+geminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
       try{
           ObjectMapper objectMapper = new ObjectMapper();
           JsonNode rootNode = objectMapper.readTree(response);

           // Accessing nested text with simplified path notation
           String text = rootNode
                   .path("candidates") // Access candidates array
                   .get(0)              // Get first element in candidates array
                   .path("content")     // Access content object
                   .path("parts")       // Access parts array
                   .get(0)              // Get first element in parts array
                   .path("text")        // Access text field
                   .asText();           // Convert the text field to string

           // Return the extracted text in a proper JSON format

           return text.split("\n");
       }
       catch (Exception e){
           return new String[]{"Could not extract the usernames"};
       }
    }
}
