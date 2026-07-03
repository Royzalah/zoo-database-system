package com.example.zoo.ass3.models;

import java.io.Serializable;

public abstract class Animal implements Serializable {
    protected int age;

    protected int happiness = 100;

    public abstract double feed();

    public abstract String makeNoise();

    public abstract int getMaxAge();

    public void ageOneYear() { age += 1; };

    public int getAge() {
        return age;
    }

    public int getHappiness() { return happiness; }

    public void setHappiness(int happiness) { this.happiness = happiness; }
}
