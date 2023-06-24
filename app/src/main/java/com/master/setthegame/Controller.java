package com.master.setthegame;

import android.os.StrictMode;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class Controller {
    private String token;
    private String name;

    public Controller() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    private HttpURLConnection setConnSettings(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        return con;
    }

    private void sendRequest(String jsonInputString, HttpURLConnection con){
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject getResponse(HttpURLConnection con){
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String resp = response.toString();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(resp);
            return json;
        } catch (IOException | org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean register(String nick, String pass) throws IOException, JSONException {
        URL url = new URL("http://51.250.45.188:8080/user/register");
        //Настройка запроса
        HttpURLConnection con = setConnSettings(url);
        String jsonInputString = "{\"nickname\": \"" + nick + "\", \"password\": \"" + pass + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        if((boolean) json.get("success")){
            token = json.get("accessToken").toString();
            name = nick;
            return true;
        }
        return false;
    }

    public JSONArray playerGames() throws IOException, JSONException {
        URL url = new URL("https://51.250.45.188:8080/set/room/list");
        //Настройка запроса
        HttpURLConnection con = setConnSettings(url);
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (JSONArray) json.get("games");

    }

    public int createGame() throws IOException, JSONException {
        URL url = new URL("http://51.250.45.188:8080/set/room/create");
        //Настройка запроса
        HttpURLConnection con = setConnSettings(url);
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (int) json.get("gameId");
    }

    public boolean EnterInGame(int gameId) throws IOException, JSONException {
        URL url = new URL("http://51.250.45.188:8080/set/room/enter");
        //Настройка запроса
        HttpURLConnection con = setConnSettings(url);
        String jsonInputString = "{\"accessToken\": \"" + token + "\", \"gameId\":" + gameId + "}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (boolean) json.get("success");
    }

    public JSONArray getField() throws IOException, JSONException {
        URL url = new URL("http://51.250.45.188:8080/set/field");
        //Настройка запроса
        HttpURLConnection con = setConnSettings(url);
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (JSONArray) json.get("cards");
    }

    public int pickCards(int[] pickedCards) throws IOException, JSONException {
        URL url = new URL("http://51.250.45.188:8080/set/field");
        //Настройка запроса
        HttpURLConnection con = setConnSettings(url);
        String jsonInputString = "{\"accessToken\": \"" + token + "\", \"cards\":[" +
                pickedCards[0] + ", " +pickedCards[1]+ ", "+ pickedCards[2]+"]}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (int) json.get("score");
    }

    public boolean addCards() throws IOException, JSONException {
        URL url = new URL("http://51.250.45.188:8080/set/add");
        //Настройка запроса
        HttpURLConnection con = setConnSettings(url);
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (boolean) json.get("success");
    }

    public int getCurrentScore() throws IOException, JSONException {
        URL url = new URL("http://51.250.45.188:8080/set/scores");
        //Настройка запроса
        HttpURLConnection con = setConnSettings(url);
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        JSONArray users = (JSONArray) json.get("users");
        return users.indexOf(name);
    }

    public String getToken() {
        return token;
    }
}
