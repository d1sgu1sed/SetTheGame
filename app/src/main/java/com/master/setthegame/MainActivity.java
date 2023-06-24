package com.master.setthegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Controller controller = new Controller();
        try {
            controller.register("Andrey", "strongpassword1337");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}