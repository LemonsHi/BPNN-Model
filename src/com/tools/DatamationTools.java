package com.tools;

import java.util.Arrays;
import java.util.Collections;

public class DatamationTools {

    private double[][] input;

    private String inputStr;

    private Double[][] aimList;

    private double[][] normalList;

    public DatamationTools(double[][] input) {
        this.input = input;
        this.normalList = new double[input.length][input[0].length];
        this.setAimList();
    }

    private void setAimList () {
        aimList = new Double[input[0].length][input.length];
        for (int i = 0, length = input.length; i < length; i++) {
            for (int j = 0, _length = input[i].length; j < _length; j++) {
                aimList[j][i] = input[i][j];
            }
        }
    }

//    public void setInputStr () {
//        for (int i = 0, length = this.input.length; i < length; i++) {
//
//        }
//    }

    public double normal (double val, int i) {
        double max, min;
        max = Collections.max(Arrays.asList(aimList[i]));
        min = Collections.min(Arrays.asList(aimList[i]));
        return (val - min) / (max - min);
//        return val / 100.0;
    }

    public double getReal (double val, int i) {
        double max, min;
        max = Collections.max(Arrays.asList(aimList[i]));
        min = Collections.min(Arrays.asList(aimList[i]));
        return (val * (max - min) + min);
//        return val / 100.0;
    }

    public double[][] getNormal () {
        for (int i = 0, length = input.length; i < length; i++) {
            for (int j = 0, _length = input[i].length; j < _length; j++) {
                normalList[i][j] = normal(input[i][j], j);
            }
        }
        return normalList;
    }

    public Double[][] getAimList() {
        return aimList;
    }
}
