package com.main;

import com.charts.JFreeCharts;
import com.model.BP;

import javax.swing.*;

public class Start {
    public static void main(String[] args) {
        BP bp = new BP();
        double[][] input = new double[10][1];
        double[][] target = new double[10][1];
        System.out.println("开始训练~~");
        for (int i = 0; i < 10; i++) {
            input[i][0] = i;
            target[i][0] = Math.pow(i, 2);
        }
        bp.train(input, target);
        System.out.println("训练结束~~");
        double[] in = new double[1];
        double[] out = new double[10];
        double[] real = new double[10];
        double[] yName = new double[10];
        for (int i = 0; i < 10; i++) {
            in[0] = i;
            yName[i] = i;
            real[i] = Math.pow(i, 2);
            out[i] = bp.bpNetOut(in)[0] * 100;
        }

        /**
         * 生成图表过程
         */
        SwingUtilities.invokeLater(() -> {
            JFreeCharts example = new JFreeCharts("Line Chart Example", real, out, yName);
            example.setAlwaysOnTop(true);
            example.pack();
            example.setSize(600, 400);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
