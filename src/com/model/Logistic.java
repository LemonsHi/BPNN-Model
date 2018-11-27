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

    // Logistic 映射后的种群 out_x[维度] - x_axis[维度] || y_axis[维度]
    private double[] out_x;

    // x ∈ [min, max]
    private double max;
    private double min;

    // 个体维度
    private int DIM;

    // 混沌变量 Cx[维度]
    private double[] Cx;

    public Logistic(double[] x, int DIM) {
        this.x = x;
        this.DIM = DIM;
        this.Cx = new double[DIM];
        this.out_x = new double[DIM];

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
        Cx[0] = (x[0] - min) / (max - min);
        for (int i = 1; i < DIM; i++) {
             Cx[i] = 4.0 * Cx[i - 1] * (1 - Cx[i - 1]);
        }
    }

    private void setOut_x () {
        for (int i = 0; i < DIM; i++) {
            out_x[i] = min + Cx[i] * (max - min);
        }
    }

    private void begin () {
        setMax();
        setMin();
        setCx();
        setOut_x();
    }

    public double[] getOut_x() {
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
