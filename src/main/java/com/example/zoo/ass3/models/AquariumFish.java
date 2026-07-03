package com.example.zoo.ass3.models;

import com.example.zoo.ass3.exceptions.GeneralException;
import com.example.zoo.ass3.general.enums.Color;
import com.example.zoo.ass3.general.enums.Pattern;

public class AquariumFish extends Fish {
    public final static int MAX_AGE = 25;
    private final static int ADULT = 3;
    private final static int CHILD_EAT = 3;
    private final Color[] colors;

    public AquariumFish(int age, double length, Pattern pattern, Color[] colors) throws GeneralException {
        super(age, length, pattern);
        this.colors = colors;
    }

    @Override
    public double feed() {
        if (age < ADULT) {
            return CHILD_EAT;
        }
        return length + CHILD_EAT;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(", colors: [");
        for (int i = 0; i < colors.length; i++) {
            sb.append(colors[i]);
            if (i < colors.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }


    public Color[] getColors() {
        return colors;
    }
}
