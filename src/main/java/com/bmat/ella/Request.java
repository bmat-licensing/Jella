
package com.bmat.ella;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
     * An instance of JSONParser.
     * */
    private JSONParser jsonParser;

    /**
     * Class constructor.
     * @param ellaConnectionValue A connection to the Ella web service.
     * */
    public Request(final EllaConnection ellaConnectionValue) {
        this.ellaConnection = ellaConnectionValue;
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
    public final Object execute(final String method, final String collection,
            final HashMap<String, String> searchTerms)
    throws ServiceException, IOException {
        return this.execute(method, collection, searchTerms, false);
    }

    /**
     * Executes the web service request.
     * @param method type of search that will be executed.
     * @param collection name of the queried collection.
     * @param searchTerms request parameters.
     * @param cacheable Say if the request can be found in the cache.
     * @return an Object instance of JSONObject or JSONArray.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final Object execute(final String method, final String collection,
            final HashMap<String, String> searchTerms,
            final boolean cacheable)throws ServiceException, IOException {
        try {
            JSONObject jsonObj;
            if (Jella.isCacheEnable() && cacheable) {
                String cacheKey = this.getCacheKey(searchTerms, method,
                        collection);
                jsonObj = (JSONObject) this.jsonParser.parse(
                        this.getCachedResponse(cacheKey,
                                searchTerms, method, collection));
            } else {
                jsonObj = (JSONObject) this.jsonParser.parse(
                        this.downloadResponse(method,
                                collection, searchTerms));
            }
            Object response = jsonObj.get("response");
            if (response != null) {
                return response;
            } else {
                JSONObject error = (JSONObject) jsonObj.get("error");
                throw new ServiceException(
                        (String) error.get("type"),
                        (String) error.get("message"));
            }
        } catch (ParseException e) {
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
    private String downloadResponse(final String method,
            final String collection,
            final HashMap<String, String> searchTerms) throws IOException {
        StringBuffer params = new StringBuffer();
        String sep = "";
        if (searchTerms != null) {
            for (Map.Entry<String, String> param : searchTerms.entrySet()) {
                params.append(sep + param.getKey() + "="
                + URLEncoder.encode(param.getValue(), "utf-8"));
                sep = "&";
            }
        }
        String url = this.ellaConnection.getEllaws();
        if (collection != null) {
            url += "/collections/" + collection;
        }
        url += method + "?" + params.toString();
//        System.out.println("URL: " + url);
        HttpURLConnection urlCon = (HttpURLConnection) new URL(
                url).openConnection();
        BufferedReader bufferedReader;
        boolean error = false;
        try {
            bufferedReader = new BufferedReader(
                new InputStreamReader(urlCon.getInputStream()));
        } catch (FileNotFoundException fnfe) {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(urlCon.getErrorStream()));
            error = true;
        }
        String inputLine, jsonResponseStr; 
        StringBuffer jsonResponse = new StringBuffer();
        while ((inputLine = bufferedReader.readLine()) != null) {
            jsonResponse.append(inputLine);
        }
        bufferedReader.close();
        jsonResponseStr = jsonResponse.toString();
//        System.out.println("JSON RESPONSE: " + jsonResponse);
        if (!error && Jella.isCacheEnable()) {
            FileWriter cacheFile = null;
            try {
                String cacheKey = this.getCacheKey(
                        searchTerms, method, collection);
                if (cacheKey != null) {
                    cacheFile = new FileWriter(
                            new File(this.getCacheDir(), cacheKey));
                    cacheFile.write(jsonResponseStr);
                    cacheFile.close();
                }
            } catch (IOException e) {
                if (cacheFile != null) {
                    cacheFile.close();
                }
            }
        }
        return jsonResponseStr;
    }

    /**
     * @param cacheKey The request key that is going to be checked.
     * @param params request parameters.
     * @param method type of search that will be executed.
     * @param collection name of the queried collection.
     * @return A string with the request response.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * */
    private String getCachedResponse(final String cacheKey,
            final HashMap<String, String> params, final String method,
            final String collection) throws IOException {
        if (cacheKey != null && !this.isCached(cacheKey)) {
            return this.downloadResponse(method, collection, params);
        }
        try {
            StringBuffer jsonResponse = new StringBuffer();
            BufferedReader reader = new BufferedReader(
                    new FileReader(
                            new File(Jella.getJellaCacheDir(), cacheKey)));
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            reader.close();
            return jsonResponse.toString();
        } catch (IOException e) {
            return this.downloadResponse(method, collection, params);
        }
    }

    /**
     * @return The Ella connection instance.
     * */
    public final EllaConnection getEllaConnection() {
        return this.ellaConnection;
    }

    /**
     * @param params request parameters.
     * @param method type of search that will be executed.
     * @param collection name of the queried collection.
     * @return A string with the generated request key.
     * */
    private String getCacheKey(final HashMap<String, String> params,
            final String method, final String collection) {
        StringBuffer cacheKey = new StringBuffer();
        if (collection != null && !collection.equals("")) {
            cacheKey.append(collection + method);
        } else {
            cacheKey.append(method);
        }
        if (params != null) {
            Object[] keys = params.keySet().toArray();
            Arrays.sort(keys);
            for (Object key : keys) {
                cacheKey.append(key.toString() + params.get(key.toString()));
            }
        }
        try {
            return URLEncoder.encode(this.getMD5(cacheKey.toString()),
                    "utf-8");
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * @param cacheKey The request key that is going to be checked.
     * @return True if the request is cached. False if not.
     * */
    private boolean isCached(final String cacheKey) {
        if (cacheKey == null) {
            return false;
        }
        return (new File(Jella.getJellaCacheDir(), cacheKey)).exists();
    }

    /**
     * @param input A string from which you want to get the MD5 value.
     * @throws NoSuchAlgorithmException when the MD5 algorithms is not found.
     * @throws UnsupportedEncodingException when encoding UTF-8 fails.
     * @return A string with the MD5 value for the input.
     * */
    private String getMD5(final String input)
    throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String output = "";
        byte[] textBytes = input.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(textBytes);
        byte[] hash = md.digest();
        output = new String(hash);
        return output;
    }

    /**
     * @return A string that represents the cache directory path.
     * It creates the directory if it does not exist.
     * */
    private String getCacheDir() {
        File directory = new File(Jella.getJellaCacheDir());
        if (!directory.exists() && !directory.mkdir()) {
            return null;
        }
        return Jella.getJellaCacheDir();
    }
}
