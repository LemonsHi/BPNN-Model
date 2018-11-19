package com.main;

import com.charts.JFreeCharts;
import com.model.BP;
import com.tools.DatamationTools;

import javax.swing.*;
import java.util.Scanner;

public class Start {
    public static void main(String[] args) {

        double[][] input = new double[10][1];
        double[][] target = new double[10][1];

        for (int i = 0; i < 10; i++) {
            input[i][0] = i;
            target[i][0] = i;
        }

        DatamationTools inputMation = new DatamationTools(input);
        DatamationTools targetMation = new DatamationTools(target);
        BP bp = new BP();
        System.out.println("开始训练~~");
        bp.train(inputMation.getNormal(), targetMation.getNormal());
        System.out.println("训练结束~~");

        double[] out = new double[10];
        double[] real = new double[10];
        double[] yName = new double[10];
        double[] in = new double[1];
        for (int i = 0; i < 10; i++) {
            real[i] = i;
            yName[i] = i;
            in[0] = inputMation.normal(i, 0);
            out[i] = targetMation.getReal(bp.bpNetOut(in)[0], 0);
            System.out.println(out[i]);
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

//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("Please enter the parameter of input:");
//        double parameter;
//        while((parameter = keyboard.nextDouble()) != -1)
//            System.out.println(parameter + "*" + parameter + "=" + targetMation.getReal(bp.bpNetOut(in)[0], 0));
    }
}
