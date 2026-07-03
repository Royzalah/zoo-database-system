package com.example.zoo.ass3.models;

import java.util.Comparator;

public class ComparePenguinByAge implements Comparator<Penguin> {

    @Override
    public int compare(Penguin a, Penguin b) {
        return Integer.compare(a.getAge(), b.getAge());
    }
}
