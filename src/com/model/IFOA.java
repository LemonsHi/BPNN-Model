//package com.model;
//
//public class IFOA {
//
//    private int popsize;
//    private int maxgen;
//    private double LR;
//    private double FR;
//    private double WINIT;
//    private double WINITB;
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
//    double[][] Vx;
//    double[][] Vy;
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
//    public IFOA (int popsize, int maxgen, double LR, double FR, double WINIT, double WINITB, int DIM, int TYPE) {
//        this.popsize = popsize;
//        this.maxgen = maxgen;
//        this.LR = LR;
//        this.FR = FR;
//        this.WINIT = WINIT;
//        this.WINITB = WINITB;
//        this.DIM = DIM;
//        this.TYPE = TYPE;
//        x_best = new double[DIM];
//        y_best = new double[DIM];
//        smell_best = new double[maxgen];
//        init();
//    }
//
//    // FOA-BP 结合算法入口
//    public IFOA (int popsize, int maxgen, double LR, double FR, int TYPE, int inputNum, int hiddenNum, int outputNum, double[] input, double[] out) {
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
//        X  = new double[popsize][DIM];
//        Y  = new double[popsize][DIM];
//        Vx = new double[popsize][DIM];
//        Vy = new double[popsize][DIM];
//
//        x_axis = new double[DIM];
//        y_axis = new double[DIM];
//
//        // 基于混沌映射中的 Logistic 方程来确定初始值位置
//        // λ * Hi * (1 - Hi) -- λ = [0, 4]  Hi = [0, 1]
//        for (int i = 0; i < DIM; i++) {
////            double Hi_x = Math.random();
////            double Hi_y = Math.random();
////            x_axis[i] = 4 * Hi_x * (1 - Hi_x);
////            y_axis[i] = 4 * Hi_y * (1 - Hi_y);
//            x_axis[i] = FR * Math.random();
//            y_axis[i] = FR * Math.random();
//        }
//
//        for (int i = 0; i < popsize; i++) {
//            for (int j = 0; j < DIM; j++) {
//                // 使用 LGMS 给出果蝇个体的随机方向和距离
//                Vx[i][j] = LR * Math.random() * WINIT * Math.pow(WINITB, j);
//                Vy[i][j] = LR * Math.random() * WINIT * Math.pow(WINITB, j);
//                X[i][j] = x_axis[j] + Vx[i][j];
//                Y[i][j] = y_axis[j] + Vy[i][j];
//                double Di_x = Math.pow(X[i][j], 2);
//                double Di_y = Math.pow(Y[i][j], 2);
//                Di[i][j] = Math.pow((Di_x + Di_y), 0.5);
//                Si[i][j] = 1.0 / Di[i][j];
//            }
//        }
//
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
//    private double WW (int gen) {
//        double ww_min = 0.3, ww_max = 0.9;
//        return ww_min - ((ww_min - ww_max) / maxgen) * gen;
//    }
//
//    private void move (int gen) {
//        // 粒子群算法 PSO 位置移动方式
//        double C1 = 2, C2 = 2;
//        for (int i = 0; i < popsize; i++) {
//            for (int j = 0; j < DIM; j++) {
//                Vx[i][j] = WW(gen) * Vx[i][j] + C1 * Math.random() * (X[gBestIndex()][j] - X[i][j]) + C2 * Math.random() * (x_axis[j] - X[i][j]);
//                Vy[i][j] = WW(gen) * Vy[i][j] + C1 * Math.random() * (Y[gBestIndex()][j] - Y[i][j]) + C2 * Math.random() * (y_axis[j] - Y[i][j]);
//                X[i][j] = X[i][j] + Vx[i][j];
//                Y[i][j] = Y[i][j] + Vy[i][j];
//                double Di_x = Math.pow(X[i][j], 2);
//                double Di_y = Math.pow(Y[i][j], 2);
//                Di[i][j] = Math.pow((Di_x + Di_y), 0.5);
//                Si[i][j] = 1.0 / Di[i][j];
//            }
//        }
//    }
//
//    private int gBestIndex () {
//        int index = 0;
//        for (int i = 0, length = smell.length; i < length - 1; i++) {
//            boolean flag = true;
//            for (int j = 0; j < length - 1 - i; j++) {
//                if (smell[j] < smell[j + 1]) {
//                    index = j;
//                    flag = false;
//                }
//            }
//            if (flag) {
//                break;
//            }
//        }
//        return index;
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
//            move(i);
//            Fitness(Si);
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
//        IFOA foa = new IFOA(50, 200, 10, 2, 1, 0.95, 20, 0);
//        foa.beginMove();
//        double[] best = foa.getSmell_best();
//        for (int i = 0; i < best.length; i++) {
//            System.out.println(best[i]);
//        }
//    }
//}
