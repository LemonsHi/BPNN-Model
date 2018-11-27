//package com.model;
//
//import com.charts.JFreeCharts;
//
//import javax.swing.*;
//
//public class FOA {
//
//    private int popsize;
//    private int maxgen;
//    private double LR;
//    private double FR;
//    private int DIM;
//    private int TYPE;
//
//    // BP 神经网络，输入层、隐含层、输出层，层数
//    private int inputNum;
//    private int hiddenNum;
//    private int outputNum;
//    private double[] input;
//    private double[] out;
//
//    private double[] smell;
//
//    double[][] Di;
//    double[][] Si;
//    double[][] X;
//    double[][] Y;
//    private double[] x_axis;
//    private double[] y_axis;
//
//    private double[] smell_best;
//
//    private double[] x_best;
//    private double[] y_best;
//    private int index;
//    private double bestSmell;
//    private double smellBest;
//
//    public FOA (int popsize, int maxgen, double LR, double FR, int DIM) {
//        this.popsize = popsize;
//        this.maxgen = maxgen;
//        this.LR = LR;
//        this.FR = FR;
//        this.DIM = DIM;
//        x_best = new double[DIM];
//        y_best = new double[DIM];
//        smell_best = new double[maxgen];
//        init();
//    }
//
//    // FOA-BP 结合算法入口
//
//
//    public FOA (int popsize, int maxgen, double LR, double FR, int TYPE, int inputNum, int hiddenNum, int outputNum, double[] input, double[] out) {
//        this.popsize = popsize;
//        this.maxgen = maxgen;
//        this.LR = LR;
//        this.FR = FR;
//        this.TYPE = TYPE;
//        this.inputNum = inputNum;
//        this.hiddenNum = hiddenNum;
//        this.outputNum = outputNum;
//        this.input = input;
//        this.out = out;
//        this.DIM = inputNum * hiddenNum + hiddenNum * outputNum + hiddenNum + outputNum;
//        this.TYPE = 1;
//
//        x_best = new double[DIM];
//        y_best = new double[DIM];
//        smell_best = new double[maxgen];
//        init();
//    }
//
//    private void init () {
//        Di = new double[popsize][DIM];
//        Si = new double[popsize][DIM];
//        X = new double[popsize][DIM];
//        Y = new double[popsize][DIM];
//
//        x_axis = new double[DIM];
//        y_axis = new double[DIM];
//
//        for (int i = 0; i < DIM; i++) {
//            x_axis[i] = FR * Math.random();
//            y_axis[i] = FR * Math.random();
//        }
//
//        move();
//        switch (TYPE) {
//            case 1:
//                bp(Si);
//                break;
//            default:
//                Fitness(Si);
//        }
//        min();
//        change();
//    }
//
//    private void move () {
//        for (int i = 0; i < popsize; i++) {
//            for (int j = 0; j < DIM; j++) {
//                X[i][j] = x_axis[j] + LR * Math.random();
//                Y[i][j] = y_axis[j] + LR * Math.random();
//                double Di_x = Math.pow(X[i][j], 2);
//                double Di_y = Math.pow(Y[i][j], 2);
//                Di[i][j] = Math.pow((Di_x + Di_y), 0.5);
//                Si[i][j] = 1.0 / Di[i][j];
//            }
//        }
//    }
//
//    private void min () {
//        index = 0;
//        bestSmell = smell[0];
//        for (int i = 1; i < popsize; i++) {
//            if (bestSmell > smell[i]) {
//                index = i;
//                bestSmell = smell[i];
//            }
//        }
//    }
//
//    private void change () {
//        x_axis = X[index];
//        y_axis = Y[index];
//        smellBest = bestSmell;
//    }
//
//    private boolean isEnd () {
//        return smellBest > bestSmell ? true : false;
//    }
//
//    public void beginMove () {
//        for (int i = 0; i < maxgen; i++) {
//            move();
//            switch (TYPE) {
//                case 1:
//                    bp(Si);
//                    break;
//                default:
//                    Fitness(Si);
//            }
//            min();
//            if (isEnd()) {
//                change();
//            }
//            smell_best[i] = smellBest;
//            x_best = x_axis;
//            y_best = y_axis;
//        }
//    }
//
//    private void Fitness (double[][] s) {
//        smell = new double[popsize];
//        for (int i = 0; i < popsize; i++) {
//            double sum = 0;
//            for (int j = 0; j < DIM; j++) {
//                sum += Math.pow(s[i][j], 2) * j;
//            }
//            smell[i] = sum;
//        }
//    }
//
//    private void bp (double[][] s) {
//        smell = new double[popsize];
//        double[] hidden_out = new double[hiddenNum];
//        double[] out_out = new double[outputNum];
//        for (int i = 0; i < popsize; i++) {
//            double err = 0.0;
//            for (int j = 0; j < hiddenNum; j++) {
//                double sum = 0.0;
//                for (int k = j, n = 0; k < (inputNum * hiddenNum); k = k + hiddenNum, n++) {
//                    sum += s[i][k] * input[n];
//                }
//                hidden_out[j] = Sigmoid(sum + s[i][inputNum * hiddenNum + j]);
//            }
//            for (int j = 0; j < outputNum; j++) {
//                double sum = 0.0;
//                for (int k = (inputNum * hiddenNum + hiddenNum + outputNum), n = 0; k < DIM; k = k + outputNum, n++) {
//                    sum += s[i][k] * hidden_out[n];
//                }
//                out_out[j] = Sigmoid(sum + s[i][inputNum * hiddenNum + hiddenNum + j]);
//                err += (out[j] - out_out[j]) * (out[j] - out_out[j]);
//            }
//            err = err / 2;
//            smell[i] = err;
//        }
//    }
//
//    private double Sigmoid(double d) {
//        // TODO Auto-generated method stub
//        return 1 / (1 + Math.exp(-d));
//    }
//
//    public double[] getSmell_best() {
//        return smell_best;
//    }
//
//    public double[] getX_best() {
//        return x_best;
//    }
//
//    public double[] getY_best() {
//        return y_best;
//    }
//
//    public static void main(String[] args) {
//        FOA foa = new FOA(50, 200, 10, 2, 30);
//        foa.beginMove();
//        double[] best = foa.getSmell_best();
//        for (int i = 0; i < best.length; i++) {
//            System.out.println(best[i]);
//        }
//
//        IFOA ifoa = new IFOA(50, 200, 10, 2, 1, 0.95, 30, 0);
//        ifoa.beginMove();
//        double[] ibest = ifoa.getSmell_best();
//        for (int i = 0; i < ibest.length; i++) {
//            System.out.println(ibest[i]);
//        }
//
//        double[] real = new double[200];
//        double[] out = new  double[200];
//        double[] yName = new double[200];
//        for (int i = 0; i < 200; i++) {
//            real[i] = best[i];
//            out[i] = ibest[i];
//            yName[i] = i;
//        }
//        /**
//         * 生成图表过程
//         */
//        SwingUtilities.invokeLater(() -> {
//            JFreeCharts example = new JFreeCharts("Line Chart Example", real, out, yName);
//            example.setAlwaysOnTop(true);
//            example.pack();
//            example.setSize(600, 400);
//            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            example.setVisible(true);
//        });
//    }
//}
