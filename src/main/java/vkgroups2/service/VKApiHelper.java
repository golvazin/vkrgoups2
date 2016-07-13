package vkgroups2.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vkgroups2.domain.User;
import vkgroups2.exception.ServiceException;
import vkgroups2.exception.VKMethodException;


@Service
public class VKApiHelper {
    
    private static final String VERSION = "5.52";
    private static final String METHOD_URI = "https://api.vk.com/method/";
    

    public VKApiHelper() {
        
    }
    
    public User retrieveVkUserInfo(String userId, String accessToken) {
        String url = String.format(METHOD_URI + "users.get?user_ids=%s"
                + "&access_token=%s"
                + "&v=%s", userId, accessToken, VERSION);
        String userJson = applyVKMethod(url);
        User user = processUserJson(userJson);
        return user;
    }
    
    
    
    public boolean checkMembership(String groupUrl, String userId) {

        String url = String.format(METHOD_URI + "groups.isMember?group_id=%s"
                + "&user_id=%s" + "&v=%s", extractGroupIdFormUrl(groupUrl),
                userId, VERSION);
        String vkMethodResponse = applyVKMethod(url);
        int result = Integer.parseInt(vkMethodResponse);
        return result == 1;
    }
    
    public int retrieveGroupMessages(String groupUrl) {
        String url = String.format(METHOD_URI + "wall.get?domain=%s&count=1", 
                extractGroupIdFormUrl(groupUrl));
        return extractMessagesCount(applyVKMethod(url));
    }
    
    
    private String applyVKMethod(String url) {
        List<String> responseBody = performHttpRequest(url);
        if (responseBody == null || responseBody.size() == 0) {
            throw new ServiceException("No info found");
        } else {

            return extractResponseContent(responseBody.get(0));
        }
    }


    private List<String> performHttpRequest(String url) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        List<String> responseBody = null;
        try {
            response = client.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            responseBody = reader.lines().collect(Collectors.toList());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseBody;
    }
    
    private String extractGroupIdFormUrl(String groupUrl) {
        String[] parts = groupUrl.split("[/]");
        return parts[parts.length - 1];
    }
    
    private String extractResponseContent(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode response = objectMapper.readTree(json).findValue("error");
            if (response != null) {
                throw new VKMethodException(response.findValue("error_msg").toString());
            }
            response = objectMapper.readTree(json).findValue("response");
            if (response != null) {
                return response.toString();
            } else {
                throw new VKMethodException("Invalid response");
                
            }
            
        } catch (IOException e) {
            throw new ServiceException("Could not process json", e);
        }
    }
    
    private User processUserJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>() {});
            return users.get(0);
        } catch (IOException e) {
            throw new ServiceException("Could not process json", e);
        }
    }
    
    private int extractMessagesCount(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode response = objectMapper.readTree(json);
            return response.elements().next().asInt();
        } catch (IOException e) {
            throw new ServiceException("Could not process json", e);
        }
    }
    

    

}
