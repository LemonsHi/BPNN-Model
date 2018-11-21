package com.model;

public class FOA {

    private int popsize;
    private int maxgen;
    private double LR;
    private double FR;

    private double[] x;
    private double[] y;
    private double[] d;
    private double[] s;

    private double[] smell;

    private double x_axis;
    private double y_axis;
    private int preBestIndex;
    private double preBestSmell;
    private double nowBestSmell;

    public FOA(int popsize, int maxgen, double LR, double FR) {
        this.popsize = popsize;
        this.maxgen = maxgen;
        this.LR = LR;
        this.FR = FR;
        init();
    }

    private void init () {
        x_axis = LR * Math.random();
        y_axis = LR * Math.random();
        x     = new double[popsize];
        y     = new double[popsize];
        d     = new double[popsize];
        s     = new double[popsize];
        smell = new double[popsize];

        move();
        min();
        change();
    }

    private void move () {
        for (int i = 0; i < popsize; i++) {
            x[i] = x_axis + FR * Math.random();
            y[i] = y_axis + FR * Math.random();
            double x_y = Math.pow(x[i], 2) + Math.pow(y[i], 2);
            d[i] = Math.pow(x_y, 0.5);
            s[i] = 1.0 / d[i];
            smell[i] = Fitness(s[i]);
        }
    }

    private void min () {
        preBestSmell = smell[0];
        preBestIndex = 0;
        for (int i = 1; i < popsize; i++) {
            if (preBestSmell > smell[i]) {
                preBestSmell = smell[i];
                preBestIndex = i;
            }
        }
    }

    private void change () {
        x_axis = x[preBestIndex];
        y_axis = y[preBestIndex];
        nowBestSmell = preBestSmell;
    }

    private boolean isEnd () {
        return nowBestSmell > preBestSmell ? true : false;
    }

    public void beginMove () {
        for (int i = 0; i < maxgen; i++) {
            move();
            min();
            if (isEnd()) {
                change();
            }
        }
    }

    private double Fitness (double s) {
        return Math.pow(s, 2);
    }

    public double getX_axis() {
        return x_axis;
    }

    public double getY_axis() {
        return y_axis;
    }

    public double getNowBestSmell() {
        return nowBestSmell;
    }

    public static void main(String[] args) {
        FOA foa = new FOA(50, 200, 10, 2);
        foa.beginMove();
        System.out.println(foa.getNowBestSmell());
    }
}
