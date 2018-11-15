package com.main;

import com.charts.JFreeCharts;
import com.model.BP;
import com.tools.DatamationTools;

import javax.swing.*;

public class Start {
    public static void main(String[] args) {

        double[][] input = new double[10][2];
        double[][] target = new double[10][1];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                input[i][j] = i;
            }
            target[i][0] = 2 * input[i][0] + 3 * input[i][1] + 10;
        }
        DatamationTools inputMation = new DatamationTools(input);
        DatamationTools targetMation = new DatamationTools(target);
        BP bp = new BP();
        System.out.println("开始训练~~");
        bp.train(inputMation.getNormal(), targetMation.getNormal());
        System.out.println("训练结束~~");


        double[] in = new double[2];
        double[] out = new double[10];
        double[] real = new double[10];
        double[] yName = new double[10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                in[j] = i / 100;
            }
            out[i] = bp.bpNetOut(in)[0] * 100;
            real[i] = (2 * in[0] + 3 * in[1]) * 100 + 10;
            yName[i] = i;
        }
//        for (int i = 0; i < 10; i++) {
//            in[0] = i / 100.0;
//            yName[i] = i;
//            real[i] = 2 * i + 10;
//            out[i] = bp.bpNetOut(in)[0];
//            out[i] = bp.bpNetOut(in)[0] * 100;
//        }
//        DatamationTools inMation = new DatamationTools(in);

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
