package com.elbicon.coderscampus;

import java.util.ArrayList;
import java.util.List;

public class TaskDto {
    private List<Integer> number = new ArrayList<>();

    public void setNumber(Integer number) {
        this.number.add(number);
    }

    public List<Integer> getNumber() {
        return number;
    }
}
