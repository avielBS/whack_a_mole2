package com.example.whack_a_mole2;



public class Record {

    private int seconds;
    private int score;
    private int miss;
    private int bombs;
    private String name;

    private double latitude;
    private double longitude;

    public Record(String name, int seconds, int score, int miss, int bombs ,double latitude , double longitude) {
        this.name = name;
        this.seconds = seconds;
        this.score = score;
        this.miss = miss;
        this.bombs = bombs;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
