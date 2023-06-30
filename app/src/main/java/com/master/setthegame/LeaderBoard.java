package com.master.setthegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Objects;

public class LeaderBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        Controller controller = new Controller();
        controller.setName(getIntent().getStringExtra("name"));
        controller.setToken(getIntent().getStringExtra("token"));
        JSONArray playerScores = controller.getScores();

        TableLayout tableLayout = findViewById(R.id.leaderBoard);
        TextView textView;

        int k = 1;

        for (Object row: playerScores) {
            String name = Objects.requireNonNull(((JSONObject) row).get("name")).toString();
            String score = Objects.requireNonNull(((JSONObject) row).get("score")).toString();
            TableRow tableRow = new TableRow(this);
            tableLayout.addView(tableRow);
            ViewGroup.LayoutParams lp = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(lp);
            textView = new TextView(this);
            textView.setTextSize(16);
            textView.setTextColor(getResources().getColor(R.color.white));
            String num = String.valueOf(k);
            textView.setText(num);
            textView.append("              ");
            textView.append(name);
            textView.append("             ");
            textView.append(score);
            tableRow.addView(textView);
        }
    }
}