
package com.bmat.ella;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Java Class Request.
 * Represents a web service request.
 * @author Harrington Joseph (Harph)
 * */
public class Request {
    /**
     * An instance of Ella WS connection.
     * */
    private EllaConnection ellaConnection;
    /**
     * An instance of JSONParser
     * */
    private JSONParser jsonParser;

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service. 
     * */
    public Request(EllaConnection ellaConnection) {
        this.ellaConnection = ellaConnection;
        this.jsonParser = new JSONParser();
    }

    /**
     * Executes the web service request.
     * @param method type of search that will be executed.
     * @param collection name of the queried collection.
     * @param searchTerms request parameters. 
     * @return an Object instance of JSONObject or JSONArray.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public Object execute(String method, String collection, HashMap<String, String> searchTerms)throws ServiceException, IOException{
        try {
            JSONObject jsonObj = (JSONObject) this.jsonParser.parse(this.downloadResponse(method, collection, searchTerms));
            Object response = jsonObj.get("response");
            if (response != null) {
                return response;
            } else {
                JSONObject error = (JSONObject) jsonObj.get("error");
                throw new ServiceException((String) error.get("type"), (String)error.get("message"));
            }
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Downloads the request response.
     * @param method type of search that will be executed.
     * @param collection name of the queried collection.
     * @param searchTerms request parameters. 
     * @return a String that contains the web service response.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * */
    private String downloadResponse(String method, String collection, HashMap<String, String> searchTerms) throws IOException{
        String params = "";
        String sep = "";
        for (String key : searchTerms.keySet()) {
            params += sep + key + "=" + URLEncoder.encode(searchTerms.get(key), "utf-8"); 
            sep = "&";
        }
        String url = this.ellaConnection.getEllaws();
        if (collection != null) {
            url += "/collections/" + collection;
        }
        url += method + "?" + params;
//        System.out.println("url: " + url);

        URLConnection urlCon = new URL(url).openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
        String inputLine, jsonResponse = "";		
        while((inputLine = bufferedReader.readLine()) != null) {
            jsonResponse += inputLine;
        }
        bufferedReader.close();
        //System.out.println("JSON RESPONSE: " + jsonResponse);
        return jsonResponse;
    }

    /**
     * @return The Ella connection instance.
     * */
    public EllaConnection getEllaConnection() {
        return this.ellaConnection;
    }
}
