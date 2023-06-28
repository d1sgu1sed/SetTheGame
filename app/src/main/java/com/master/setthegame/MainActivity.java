package com.master.setthegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText nicknameView = findViewById(R.id.nickname);
        EditText passwordView = findViewById(R.id.password);
        ImageView rulesButton = findViewById(R.id.ruleBut);
        ImageView playButton = findViewById(R.id.playBut);
        ImageView exitButton = findViewById(R.id.exitBut);

        Controller controller = new Controller();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameView.getText().toString();
                String password = passwordView.getText().toString();
                if(!nickname.replaceAll(" ", "").equals("") &&
                        !password.replaceAll(" ", "").equals("")){
                        boolean req = controller.register(nickname, password);
                        if (!req)
                            showErrorMessage("Введеные неверные логин или пароль!");
                        else{
                            Intent intent = new Intent(MainActivity.this, PlayField.class);
                            String token = controller.getToken();
                            String name = controller.getName();
                            intent.putExtra("token", token);
                            intent.putExtra("name", name);
                            startActivity(intent);
                        }

                }
                else{
                    showErrorMessage("Введите данные!");
                }
            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Переход на активность правил
                /*Intent intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent);*/
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    private void showErrorMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ошибка!")
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}