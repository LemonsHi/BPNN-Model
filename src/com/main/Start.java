package com.main;

import com.charts.JFreeCharts;
import com.model.BP;
import com.model.BPNN;
import com.tools.DataTools;
import com.tools.DatamationTools;
import com.tools.FileTools;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Start {
    public static void main(String[] args) throws IOException {

//        double[][] input = new double[200][1];
//        double[][] target = new double[200][1];
//
//        for (int i = 0; i < 200; i++) {
//            input[i][0] = i;
//            target[i][0] = i * i;
//        }
//        DatamationTools inputMation = new DatamationTools(input);
//        DatamationTools targetMation = new DatamationTools(target);

        FileTools fileTools = new FileTools("public-train");
        String data = fileTools.readFile();
        DataTools dataTools = new DataTools(4, 1, data);

        double[][] input = dataTools.getDataList();
        double[][] target = dataTools.getTarget();

        System.out.println("input 一共有 " + input.length + " 行; " + input[0].length + " 列");
        System.out.println("target 一共有 " + target.length + " 行; " + target[0].length + " 列");

        DatamationTools inputMation = new DatamationTools(input);
        DatamationTools targetMation = new DatamationTools(target);
        double[][] inputD = inputMation.getNormal();
        double[][] outD = targetMation.getNormal();

        BPNN bp = new BPNN(4,1,1,0.99,0.99);
        int p = 0;
        double error = 100;
//        p < 30000000
        while(true && error > 0.011) {
            error = 0;
            for (int i = 0; i < 9000; i++) {
                double[] in = inputD[i];
                double[] out = outD[i];
                bp.train(in, out);
                p++;
                error += bp.sqr_err;
            }
            System.out.println("训练次数:" + p + "     error:" + error);
        }

        double[] yName = new double[target.length];
        double[] real = new double[target.length];
        double[] out = new double[target.length];
        for (int i = 0; i < 9000; i++) {
            yName[i] = i;
            real[i] = target[i][0];
            out[i] = targetMation.getReal(bp.test(inputD[i])[0], 0);
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
