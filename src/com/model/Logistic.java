package com.model;

/**
 * Logistic 映射 Java 实现版本
 * Cx_i = (x_i - x_min) / (x_max - x_min)
 * X_i = x_min + Cx_i * (x_max - x_min)
 * Cx_i 为混沌变量；X_i 为混沌映射后的值
 */
public class Logistic {
    // 初始位置 x[维度] - x_axis || y_axis
    private double[] x;

    // Logistic 映射后的种群 out_x[种群数][维度] - X[维度] || Y[维度]
    private double[][] out_x;

    // x ∈ [min, max]
    private double max;
    private double min;

    // 个体维度
    private int DIM;
    private int SIZE;

    // 混沌变量 Cx[种群数][维度]
    private double[][] Cx;

    public Logistic(double[] x, int SIZE, int DIM) {
        this.x = x;
        this.SIZE = SIZE;
        this.DIM = DIM;
        this.Cx = new double[SIZE][DIM];
        this.out_x = new double[SIZE][DIM];

        begin();
    }

    private void setMax () {
        int index = 0;
        for (int i = 1, length = x.length; i < length; i++) {
            if (x[index] < x[i]) {
                index = i;
            }
        }
        max = x[index];
    }

    private void setMin () {
        int index = 0;
        for (int i = 1, length = x.length; i < length; i++) {
            if (x[index] > x[i]) {
                index = i;
            }
        }
        min = x[index];
    }

    private void setCx () {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < DIM; j++) {
                if (i == 0) {
                    Cx[0][j] = (x[0] - min) / (max - min);
                } else {
                    Cx[i][j] = 4.0 * Cx[i - 1][j] * (1 - Cx[i - 1][j]);
                }
            }
        }
    }

    private void setOut_x () {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < DIM; j++) {
                out_x[i][j] = min + Cx[i][j] * (max - min);
            }
        }
    }

    private void begin () {
        setMax();
        setMin();
        setCx();
        setOut_x();
    }

    public double[][] getOut_x() {
        return out_x;
    }

    public static void main(String[] args) {
        int DIM = 30;
        double[] x_axis = new double[30];
        double[] y_axis = new double[30];
        for (int i = 0; i < DIM; i++) {
            x_axis[i] = Math.random();
            y_axis[i] = Math.random();
        }
//        Logistic logistic_x = new Logistic(x_axis, SIZE, DIM);
//        Logistic logistic_y = new Logistic(y_axis, SIZE, DIM);
    }
}
