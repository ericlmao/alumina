/*
 *  MIT License
 *
 * Copyright (C) 2025 Negative Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */


package games.negative.alumina.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Represents a utility class for HTTP requests.
 */
public class HTTPUtil {

    /**
     * Get the response from a GET request.
     * @param url The URL to send the request to.
     * @return The response.
     * @throws IOException If an error occurs while sending the request.
     */
    public static JsonObject get(@NotNull final String url) throws IOException {
        URL httpUrl = new URL(url);

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();

        // Set request method to GET
        connection.setRequestMethod("GET");

        // Get the response code
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response data

            try (Reader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                // Parse the JSON response using Gson
                return JsonParser.parseReader(reader).getAsJsonObject();
            }
        }
        return null;
    }

    /**
     * Send a POST request.
     * @param url The URL to send the request to.
     * @param body The body of the request.
     * @return The response.
     * @throws IOException If an error occurs while sending the request.
     */
    public static JsonObject post(@NotNull final String url, @NotNull final String body) throws IOException {
        URL httpUrl = new URL(url);

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();

        // Set request method to POST
        connection.setRequestMethod("POST");

        // Set the content type
        connection.setRequestProperty("Content-Type", "application/json; utf-8");

        // Enable output
        connection.setDoOutput(true);

        // Write the body
        try (var writer = connection.getOutputStream()) {
            writer.write(body.getBytes());
        }

        // Get the response code
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response data

            try (Reader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                // Parse the JSON response using Gson
                return JsonParser.parseReader(reader).getAsJsonObject();
            }
        }
        return null;
    }
}
