package com.example.phoneapp.utils;

import android.graphics.Color;

public class AvatarUtils {

    private static final String[] AVATAR_COLORS = {
        "#F44336", "#E91E63", "#9C27B0", "#673AB7",
        "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
        "#009688", "#4CAF50", "#8BC34A", "#FFC107",
        "#FF9800", "#FF5722", "#795548", "#607D8B"
    };

    public static int getAvatarColor(String name) {
        if (name == null || name.isEmpty()) return Color.GRAY;
        int index = Math.abs(name.hashCode()) % AVATAR_COLORS.length;
        return Color.parseColor(AVATAR_COLORS[index]);
    }

    private AvatarUtils() { /* utility class */ }
}
