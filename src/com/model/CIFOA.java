package com.model;

public class CIFOA {

    private int popsize;
    private int maxgen;
    private int DIM;
    private double w;
    private double a;
    private double LR;
    private double FR;

    private double[] smell;
    private double[] x_axis;
    private double[] y_axis;

    private double[][] Dist;
    double[][] Si;
    double[][] X;
    double[][] Y;

    private double[] smell_best;

    private double[] x_best;
    private double[] y_best;
    private int index;
    private double bestSmell;
    private double smellBest;

    public CIFOA (int popsize, int maxgen, int DIM, double w, double a, double LR, double FR) {
        this.popsize = popsize;
        this.maxgen = maxgen;
        this.DIM = DIM;
        this.w = w;
        this.a = a;
        this.LR = LR;
        this.FR = FR;
        this.smell_best = new double[maxgen];

        this.x_axis = new double[DIM];
        this.y_axis = new double[DIM];
        this.Dist = new double[popsize][DIM];
        this.Si = new double[popsize][DIM];
        this.X = new double[popsize][DIM];
        this.Y = new double[popsize][DIM];

        init();
    }

    // 初始化种群位置
    private void init () {
        for (int i = 0; i < DIM; i++) {
            x_axis[i] = FR * Math.random();
            y_axis[i] = FR * Math.random();
        }
        // 使用 Logistic 映射，初始化种群 x_axis[维度]
//        Logistic logistic_x = new Logistic(x_axis, DIM);
//        x_axis = logistic_x.getOut_x();
//        Logistic logistic_y = new Logistic(y_axis, DIM);
//        y_axis = logistic_y.getOut_x();

        move(0);
        Fitness(Si);
        min();
        change();
    }

    // 种群移动
    private void move (int gan) {
        for (int i = 0; i < popsize; i++) {
            for (int j = 0; j < DIM; j++) {
                // 给每个果蝇赋予飞行距离与方向
                X[i][j] = x_axis[j] + (LR - 2 * Math.random() * LR);
                Y[i][j] = y_axis[j] + (LR - 2 * Math.random() * LR);
                Dist[i][j] = Math.pow(X[i][j] * X[i][j] + Y[i][j] * Y[i][j], 0.5);
                Si[i][j] = 1.0 / Dist[i][j];
            }
        }
        // 味道浓度判定值
//        Si = X;
    }

    // 浓度判断函数
//    private void Fitness (double[][] s) {
//        smell = new double[popsize];
//        for (int i = 0; i < popsize; i++) {
//            double sum = 0;
//            for (int j = 0; j < DIM; j++) {
//                sum += Math.pow(s[i][j], 2);
//            }
//            // 每个个体味道浓度
//            smell[i] = sum;
//        }
//    }
    private void Fitness (double[][] s) {
        smell = new double[popsize];
        for (int i = 0; i < popsize; i++) {
            double sum = 0;
            for (int j = 0; j < DIM; j++) {
//                sum += s[i][j] * s[i][j];
                sum += s[i][j] * s[i][j] - 10 * Math.cos(2 * Math.PI * s[i][j]) + 10;
            }
            // 每个个体味道浓度
            smell[i] = sum;
        }
    }

    // 最佳味道浓度值的果蝇
    private void min () {
        index = 0;
        bestSmell = smell[0];
        for (int i = 1; i < popsize; i++) {
            if (bestSmell > smell[i]) {
                index = i;
                bestSmell = smell[i];
            }
        }
    }

    // 视觉搜索过程
    private void change () {
        x_axis = X[index];
        smellBest = bestSmell;
        System.out.println("最佳味道：" + smellBest);
    }

    private boolean isEnd () {
        return smellBest > bestSmell ? true : false;
    }

    private double getW (int gan) {
        return w * Math.pow(a, gan);
    }

    public void begin () {
        for (int i = 1; i < maxgen; i++) {
            move(i);
            Fitness(Si);
            min();
            if (isEnd()) {
                change();
            }
//            smell_best[i] = smellBest;
            x_best = x_axis;
        }
    }

    public double[] getSmell_best() {
        return smell_best;
    }

    public double[] getX_best() {
        return x_best;
    }

    public double getSmellBest() {
        return smellBest;
    }

    public static void main(String[] args) {
        CIFOA cifoa = new CIFOA(100, 3000, 50, 1, 0.5, 10, 1);
        cifoa.begin();
        double bastSmell = cifoa.getSmellBest();
        System.out.println(bastSmell);
    }
}
