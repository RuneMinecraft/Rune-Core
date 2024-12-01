package com.dank1234.plugin.global.npcs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SkinFetcher {

    public static String[] fetchSkinData(String username) throws Exception {
        // Step 1: Get the player's UUID from their username.
        String uuid = fetchUUID(username);
        if (uuid == null) {
            throw new Exception("Failed to fetch UUID. The username may not exist or the Mojang API returned an invalid response.");
        }

        // Step 2: Get the player's skin data using their UUID.
        return fetchSkinProperties(uuid);
    }

    private static String fetchUUID(String username) throws Exception {
        URL uuidUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
        HttpURLConnection uuidConnection = (HttpURLConnection) uuidUrl.openConnection();
        uuidConnection.setRequestMethod("GET");

        int responseCode = uuidConnection.getResponseCode();
        if (responseCode != 200) {
            System.err.println("Failed to fetch UUID. Response code: " + responseCode);
            return null;
        }

        BufferedReader uuidReader = new BufferedReader(new InputStreamReader(uuidConnection.getInputStream()));
        List<String> str = uuidReader.lines().toList();
        return str.get(1).substring(10, str.get(1).length()-2);
    }

    private static String[] fetchSkinProperties(String uuid) throws Exception {
        URL skinUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
        HttpURLConnection skinConnection = (HttpURLConnection) skinUrl.openConnection();
        skinConnection.setRequestMethod("GET");

        // Check for valid response
        if (skinConnection.getResponseCode() != 200) {
            System.err.println("Failed to fetch skin data. Response code: " + skinConnection.getResponseCode());
            return null;
        }

        BufferedReader skinReader = new BufferedReader(new InputStreamReader(skinConnection.getInputStream()));
        StringBuilder skinResponse = new StringBuilder();
        String line;
        while ((line = skinReader.readLine()) != null) {
            skinResponse.append(line);
        }
        skinReader.close();

        System.out.println("Raw Skin Data Response: " + skinResponse);

        if (skinResponse.length() == 0) {
            System.err.println("Skin data response is empty. The player's skin data may not exist.");
            return null;
        }

        try {
            JsonObject skinJson = JsonParser.parseString(skinResponse.toString()).getAsJsonObject();
            JsonObject properties = skinJson.getAsJsonArray("properties").get(0).getAsJsonObject();
            String value = properties.get("value").getAsString();
            String signature = properties.get("signature").getAsString();
            return new String[]{value, signature};
        } catch (Exception e) {
            System.err.println("Error parsing skin data response: " + skinResponse);
            throw e;
        }
    }
}