package com.jsloves.election.util;

/**
 * Created by juhyukkim on 2015. 7. 12..
 */

import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import android.util.Log;



public final class ServerUtil {

    private static final String TAG = "ServerUtil";


    public static String sendGet(String endpoint,Map<String, String> params) throws IOException {
        URL url;
        String paramString = "";

        if (params != null) {
            paramString = "?"+encodeString(params);
        }

        try {
            url = new URL(endpoint+paramString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint+paramString);
        }

        HttpURLConnection conn = null;
        String response = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setReadTimeout(1000);
            // handle the response
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new IOException("Get failed with error code " + status);
            } else {
                response = readStream(conn.getInputStream());
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response;
    }

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line+ "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public static String encodeString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            String key = param.getKey();
            String value = param.getValue();
            sb.append(URLEncoder.encode(key) + "=" + URLEncoder.encode(value) );
            if (iterator.hasNext()) {
                sb.append('&');
            }
        }
        return sb.toString();
    }


    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    public static void sendPost(String endpoint, Map<String, String> params)
            throws IOException {
        String body = "";
        URL url;

        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }

        if(params != null)
            body = encodeString(params);

        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
