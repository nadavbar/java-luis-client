package com.microsoft.luis.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by nadavbar on 5/23/16.
 */
public class LUISClient {
    static final String LUIS_QUERY_URL_FORMAT = "https://api.projectoxford.ai/luis/v1/application?id=%s&subscription-key=%s&q=%s";

    private String _luisAppId;
    private String _luisSubscriptionId;

    public LUISClient(String luisAppId, String luisSubscriptionId) {
        _luisAppId = luisAppId;
        _luisSubscriptionId = luisSubscriptionId;
    }

    public LUISResult queryLUIS(String query) throws Exception {
        String jsonResult = queryLUISEndPoint(query);
        return parseLUISResult(jsonResult);

    }

    private String createLUISQueryURL(String query) throws UnsupportedEncodingException {
        // TODO: on android use Android.Uri?
        String encoded = URLEncoder.encode(query, "UTF-8");
        return String.format(LUIS_QUERY_URL_FORMAT, _luisAppId, _luisSubscriptionId, encoded);
    }

    private String queryLUISEndPoint(String query) throws Exception {
        String result = null;

        String url = createLUISQueryURL(query);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestProperty("Content-Type", "application/json");

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        result = response.toString();

        return result;
    }

    private LUISResult parseLUISResult(String result) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        LUISResult value = mapper.readValue(result, LUISResult.class);

        value.sortByIntent();
        return value;
    }
}
