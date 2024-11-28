package com.example.hikelowna.core;

import androidx.annotation.NonNull;

public class Trail implements Comparable<Trail> {
    String name;
    String difficulty;
    float length;

    public Trail(){
        this.name = "";
        this.difficulty = "";
        this.length = -1.00f;
    }

    public Trail(String name, String difficulty, float length) {
        this.name = name;
        this.difficulty = difficulty;
        this.length = length;
    }
    @Override
    public int compareTo(Trail other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    private String getDifficultyStars() {
        String stars = "♢♢♢♢♢";
        if (difficulty.equalsIgnoreCase("easy")) {
            stars = "♦♢♢♢♢";
        } else if (difficulty.equalsIgnoreCase("moderate")) {
            stars = "♦♦♢♢♢";
        } else if (difficulty.equalsIgnoreCase("difficult")) {
            stars = "♦♦♦♢♢";
        } else if (difficulty.equalsIgnoreCase("extreme")) {
            stars = "♦♦♦♦♢";
        }
        return stars;
    }
    public String toStringShort() {
        return "" + getDifficultyStars() + ", " + length + "km";
    }

    @Override
    public String toString() {
        return name + "\n" + getDifficultyStars() + "\n" + length + " km";
    }


    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

