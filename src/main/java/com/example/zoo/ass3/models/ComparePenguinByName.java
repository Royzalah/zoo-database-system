package com.example.zoo.ass3.models;

import java.util.Comparator;

public class ComparePenguinByName implements Comparator<Penguin> {

    @Override
    public int compare(Penguin a, Penguin b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
