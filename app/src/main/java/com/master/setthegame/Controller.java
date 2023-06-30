package com.master.setthegame;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

    public boolean register(String nick, String pass) {
        URL url = null;
        try {
            url = new URL("http://51.250.45.188:8080/user/register");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Настройка запроса
        HttpURLConnection con = null;
        try {
            con = setConnSettings(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public JSONArray playerGames() {
        URL url = null;
        try {
            url = new URL("https://51.250.45.188:8080/set/room/list");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Настройка запроса
        HttpURLConnection con = null;
        try {
            con = setConnSettings(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (JSONArray) json.get("games");

    }

    public Long createGame(){
        URL url = null;
        try {
            url = new URL("http://51.250.45.188:8080/set/room/create");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Настройка запроса
        HttpURLConnection con = null;
        try {
            con = setConnSettings(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (Long) json.get("gameId");
    }

    public boolean enterInGame(int gameId){
        URL url = null;
        try {
            url = new URL("http://51.250.45.188:8080/set/room/enter");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Настройка запроса
        HttpURLConnection con = null;
        try {
            con = setConnSettings(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonInputString = "{\"accessToken\": \"" + token + "\", \"gameId\":" + gameId + "}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (boolean) json.get("success");
    }

    public JSONObject getField(){
        URL url = null;
        try {
            url = new URL("http://51.250.45.188:8080/set/field");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Настройка запроса
        HttpURLConnection con = null;
        try {
            con = setConnSettings(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (JSONObject) json;
    }

    public boolean pickCards(int[] pickedCards) {
        URL url = null;
        try {
            url = new URL("http://51.250.45.188:8080/set/pick");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Настройка запроса
        HttpURLConnection con = null;
        try {
            con = setConnSettings(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonInputString = "{\"accessToken\": \"" + token + "\", \"cards\":[" +
                pickedCards[0] + ", " +pickedCards[1]+ ", "+ pickedCards[2]+"]}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        Log.e("PickedRes", String.valueOf(json.get("isSet")));
        if(!(boolean)json.get("success"))
            return false;
        return (boolean) json.get("isSet");
    }

    public boolean addCards(){
        URL url = null;
        try {
            url = new URL("http://51.250.45.188:8080/set/add");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Настройка запроса
        HttpURLConnection con = null;
        try {
            con = setConnSettings(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (boolean) json.get("success");
    }

    public int getCurrentScore(){
        URL url = null;
        try {
            url = new URL("http://51.250.45.188:8080/set/scores");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Настройка запроса
        HttpURLConnection con = null;
        try {
            con = setConnSettings(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        JSONArray users = (JSONArray) json.get("users");
        JSONObject user = (JSONObject) users.get(0);
        return Math.toIntExact((Long) user.get("score"));
    }

    public JSONArray getScores(){
        URL url = null;
        try {
            url = new URL("http://51.250.45.188:8080/set/scores");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Настройка запроса
        HttpURLConnection con = null;
        try {
            con = setConnSettings(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonInputString = "{\"accessToken\": \"" + token + "\"}";
        //Отправка запроса
        sendRequest(jsonInputString, con);
        //Принятие запроса
        JSONObject json = getResponse(con);
        return (JSONArray) json.get("users");
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
