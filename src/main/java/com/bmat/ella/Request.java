
package com.bmat.ella;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
     * An instance of JSONParser.
     * */
    private JSONParser jsonParser;
    /**
     * Specifies if the cache enabled.
     * */
    private boolean CACHE_ENABLE = true;

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
            if (this.CACHE_ENABLE && cacheable) {
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
        String params = "";
        String sep = "";
        for (String key : searchTerms.keySet()) {
            params += sep + key + "="
            + URLEncoder.encode(searchTerms.get(key), "utf-8");
            sep = "&";
        }
        String url = this.ellaConnection.getEllaws();
        if (collection != null) {
            url += "/collections/" + collection;
        }
        url += method + "?" + params;
//        System.out.println("url: " + url);
        URLConnection urlCon = new URL(url).openConnection();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(urlCon.getInputStream()));
        String inputLine, jsonResponse = "";
        while ((inputLine = bufferedReader.readLine()) != null) {
            jsonResponse += inputLine;
        }
        bufferedReader.close();
//        System.out.println("JSON RESPONSE: " + jsonResponse);
        if (this.CACHE_ENABLE) {
            try {
                String cacheKey = this.getCacheKey(
                        searchTerms, method, collection);
                if (cacheKey != null) {
                    FileWriter cacheFile = new FileWriter(
                            new File(this.getCacheDir(), cacheKey));
                    cacheFile.write(jsonResponse);
                    cacheFile.close();
                }
            } catch (Exception e) { }
        }
        return jsonResponse;
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
            String jsonResponse = "";
            BufferedReader reader = new BufferedReader(
                    new FileReader(new File(Jella.getJELLA_CACHE_DIR(), cacheKey)));
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse += line;
            }
            reader.close();
            return jsonResponse;
        } catch (Exception e) {
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
        Object[] keys = params.keySet().toArray();
        Arrays.sort(keys);
        String cacheKey = "";
        if (collection != null && !collection.equals("")) {
            cacheKey = collection + method;
        } else {
            cacheKey = method;
        }
        for (Object key : keys) {
            cacheKey += key.toString() + params.get(key.toString());
        }
        try {
            return URLEncoder.encode(this.getMD5(cacheKey), "utf-8");
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
        return (new File(Jella.getJELLA_CACHE_DIR(), cacheKey)).exists();
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
        File directory = new File(Jella.getJELLA_CACHE_DIR());
        if (!directory.exists()) {
            directory.mkdir();
        }
        return Jella.getJELLA_CACHE_DIR();
    }
}
