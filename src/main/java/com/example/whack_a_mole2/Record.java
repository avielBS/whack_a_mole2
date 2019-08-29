package com.example.whack_a_mole2;

import com.google.gson.Gson;

public class Record {

    private int seconds;
    private int score;
    private int miss;
    private int bombs;
    private String name;


    public Record(String name, int seconds, int score, int miss, int bombs) {
        this.name = name;
        this.seconds = seconds;
        this.score = score;
        this.miss = miss;
        this.bombs = bombs;
    }

    public String toJsonString() {
        return (new Gson()).toJson(this);
    }

    public static Record getRecordFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Record.class);
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMiss() {
        return miss;
    }

    public void setMiss(int miss) {
        this.miss = miss;
    }

    public int getBombs() {
        return bombs;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return
                "name=" + name +
                        " score=" + score +
                        ",seconds=" + seconds +
                        ", miss=" + miss +
                        ", bombs=" + bombs;
    }
}
