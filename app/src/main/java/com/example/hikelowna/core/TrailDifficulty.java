package com.example.hikelowna.core;

import java.io.Serializable;

public class TrailDifficulty implements Serializable {

    public static String toStars(int difficulty) {
        String stars = "⬦⬦⬦⬦⬦";
        if (difficulty == 1) {
            stars = "⬥⬦⬦⬦⬦";
        } else if (difficulty == 2) {
            stars = "⬥⬥⬦⬦⬦";
        } else if (difficulty == 3) {
            stars = "⬥⬥⬥⬦⬦";
        } else if (difficulty == 4) {
            stars = "⬥⬥⬥⬥⬦";
        } else if (difficulty == 5) {
            stars = "⬥⬥⬥⬥⬥";
        }
        return stars;
    }

}