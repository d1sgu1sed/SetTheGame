package com.master.setthegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class PlayField extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);
        Controller controller = new Controller();
        controller.setName(getIntent().getStringExtra("name"));
        controller.setToken(getIntent().getStringExtra("token"));
        Long id = null;

        ImageView returnButton = findViewById(R.id.returnButton);
        ImageView addMore = findViewById(R.id.addButton);
        ImageView showRules = findViewById(R.id.ruleButton);
        TextView score = findViewById(R.id.score);
        int[] flagArray = new int[12];

        FrameLayout addCard1 = findViewById(R.id.addCard1);
        FrameLayout addCard2 = findViewById(R.id.addCard2);
        FrameLayout addCard3 = findViewById(R.id.addCard3);

        ImageView card1 = findViewById(R.id.card1);
        ImageView card2 = findViewById(R.id.card2);
        ImageView card3 = findViewById(R.id.card3);
        ImageView card4 = findViewById(R.id.card4);
        ImageView card5 = findViewById(R.id.card5);
        ImageView card6 = findViewById(R.id.card6);
        ImageView card7 = findViewById(R.id.card7);
        ImageView card8 = findViewById(R.id.card8);
        ImageView card9 = findViewById(R.id.card9);
        ImageView card10 = findViewById(R.id.card10);
        ImageView card11 = findViewById(R.id.card11);
        ImageView card12 = findViewById(R.id.card12);
        View activeCard1 = findViewById(R.id.activeCard1);
        View activeCard2 = findViewById(R.id.activeCard2);
        View activeCard3 = findViewById(R.id.activeCard3);
        View activeCard4 = findViewById(R.id.activeCard4);
        View activeCard5 = findViewById(R.id.activeCard5);
        View activeCard6 = findViewById(R.id.activeCard6);
        View activeCard7 = findViewById(R.id.activeCard7);
        View activeCard8 = findViewById(R.id.activeCard8);
        View activeCard9 = findViewById(R.id.activeCard9);
        View activeCard10 = findViewById(R.id.activeCard10);
        View activeCard11 = findViewById(R.id.activeCard11);
        View activeCard12 = findViewById(R.id.activeCard12);
        ArrayList<ImageView> imageList = new ArrayList<>();
        ArrayList<View> backgroundList = new ArrayList<>();
        backgroundList.add(activeCard1);
        backgroundList.add(activeCard2);
        backgroundList.add(activeCard3);
        backgroundList.add(activeCard4);
        backgroundList.add(activeCard5);
        backgroundList.add(activeCard6);
        backgroundList.add(activeCard7);
        backgroundList.add(activeCard8);
        backgroundList.add(activeCard9);
        backgroundList.add(activeCard10);
        backgroundList.add(activeCard11);
        backgroundList.add(activeCard12);
        imageList.add(card1);
        imageList.add(card2);
        imageList.add(card3);
        imageList.add(card4);
        imageList.add(card5);
        imageList.add(card6);
        imageList.add(card7);
        imageList.add(card8);
        imageList.add(card9);
        imageList.add(card10);
        imageList.add(card11);
        imageList.add(card12);

        score.setText("0");

        id = controller.createGame();
        controller.enterInGame(Math.toIntExact(id));
        final JSONArray[] fieldArr = {(JSONArray) controller.getField().get("cards")};
        showField(imageList, fieldArr[0]);

        View.OnClickListener onCardClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currId = getResources().getResourceName(v.getId());
                Log.e("card", currId);
                int l = 0;
                if(currId.charAt(currId.length() - 2) - 48 - 1 >= 0 &&
                        currId.charAt(currId.length() - 2) - 48 - 1 <= 9)
                    l = (currId.charAt(currId.length() - 2) - 48) * 10;
                l += currId.charAt(currId.length() - 1) - 48 - 1;
                View currCard = backgroundList.get(l);
                if(flagArray[l] == 0) {
                    currCard.setVisibility(View.VISIBLE);
                    flagArray[l]++;
                    if(getArraySum(flagArray) == 3){
                        int[] pickIds = new int[3];
                        int k = 0;
                        for(int i = 0; i < fieldArr[0].size() && k < 3; i++){
                            if(flagArray[i] == 1){
                                JSONObject obj = (JSONObject) fieldArr[0].get(i);
                                pickIds[k++] = Integer.parseInt(Objects.requireNonNull(obj.get("id")).toString());
                                View o = backgroundList.get(i);
                                o.setVisibility(View.INVISIBLE);
                                flagArray[i] = 0;
                            }
                        }
                            if(controller.pickCards(pickIds)){
                                JSONObject resultOfPick = (JSONObject) controller.getField();
                                if(resultOfPick.get("status") == "ended"){
                                    Intent intent = new Intent(PlayField.this, LeaderBoard.class);
                                    intent.putExtra("token", getIntent().getStringExtra("token"));
                                    intent.putExtra("name", getIntent().getStringExtra("name"));
                                    startActivity(intent);
                                }
                                fieldArr[0] = (JSONArray) resultOfPick.get("cards");
                                showField(imageList, fieldArr[0]);
                                score.setText(String.valueOf(controller.getCurrentScore()));
                            }else
                                Toast.makeText(getApplicationContext(), "Это не сет!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    currCard.setVisibility(View.INVISIBLE);
                    flagArray[l]--;
                }
            }
        };

        for(int i = 0; i < imageList.size(); i++)
            imageList.get(i).setOnClickListener(onCardClick);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.addCards();
                addCard1.setVisibility(View.VISIBLE);
                addCard2.setVisibility(View.VISIBLE);
                addCard3.setVisibility(View.VISIBLE);
                showField(imageList, fieldArr[0]);
            }
        });
        showRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayField.this, Rules.class);
                startActivity(intent);
            }
        });
    }

    public int getArraySum(int[] arr){
        int sum = 0;
        for (int ints : arr)
                sum += ints;
        return sum;
    }

    public void showField(ArrayList<ImageView> gameField, JSONArray field){
        int fieldLen = field.toArray().length;
        int i = 0;
        for(; i < fieldLen; i++){
            JSONObject currCard = (JSONObject) field.get(i);
            String cardChars = Objects.requireNonNull(currCard.get("shape")).toString();
            cardChars = cardChars + Objects.requireNonNull(currCard.get("color")).toString();
            cardChars = cardChars + Objects.requireNonNull(currCard.get("count")).toString();
            cardChars = cardChars + Objects.requireNonNull(currCard.get("fill")).toString();
            ImageView card = gameField.get(i);
            Resources res = getResources();
            int resId = res.getIdentifier("c"+cardChars, "drawable", getPackageName());
            Log.e("cardId", "c"+cardChars);
            card.setBackgroundResource(resId);
        }
        if(fieldLen < 12){
            for(int j = 11; j >= 9; j++)
                gameField.get(j).setVisibility(View.INVISIBLE);
        } else if (fieldLen < 9){
            for(int j = 8; j >= 6; j++)
                gameField.get(j).setVisibility(View.INVISIBLE);
        }
        else if (fieldLen < 6){
            for(int j = 5; j >= 3; j++)
                gameField.get(j).setVisibility(View.INVISIBLE);
        }
    }

    private void showErrorMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayField.this);
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