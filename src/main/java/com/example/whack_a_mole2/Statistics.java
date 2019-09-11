package com.example.whack_a_mole2;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        listView = findViewById(R.id.statistics_list);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                readData();
            }
        };

        Thread dbThread = new Thread(runnable);
        dbThread.setName("Read Data Thread");
        dbThread.start();

    }

    private void readData() {
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor data = db.getRecords();

        ArrayList<String> dataList = new ArrayList<>();

        while (data.moveToNext()) {
            int seconds = Integer.parseInt(data.getString(5));
            int score = Integer.parseInt(data.getString(2));
            int miss = Integer.parseInt(data.getString(3));
            int bombs = Integer.parseInt(data.getString(4));
            String name = data.getString(1);
            double latitude = Double.parseDouble(data.getString(6));
            double longitude = Double.parseDouble(data.getString(7));

            Record record = new Record(name,seconds ,score,miss,bombs,latitude,longitude);
            dataList.add("Name :"+record.getName()+"   Score : "+record.getScore());

        }
        Log.d(Thread.currentThread().getName(), dataList.toString());
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper db = new DatabaseHelper(this);
        db.close();

    }
}
