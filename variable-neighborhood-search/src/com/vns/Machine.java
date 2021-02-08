package com.vns;

import java.util.ArrayList;

public class Machine {
    private int id;
    private int value;
    ArrayList<Integer> details = new ArrayList<>();

    public Machine(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ArrayList<Integer> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<Integer> details) {
        this.details = details;
    }
}
