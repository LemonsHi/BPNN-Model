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

//        double[][] input = new double[20][1];
//        double[][] target = new double[20][1];
//
//        for (int i = 0; i < 20; i++) {
//            input[i][0] = i;
//            target[i][0] = i;
//        }
//        DatamationTools inputMation = new DatamationTools(input);
//        DatamationTools targetMation = new DatamationTools(target);

        FileTools fileTools = new FileTools("public-train");
        String data = fileTools.readFile();
        DataTools dataTools = new DataTools(3, 1, data);

        double[][] input = dataTools.getDataList();
        double[][] target = dataTools.getTarget();

        System.out.println("input 一共有 " + input.length + " 行; " + input[0].length + " 列");
        System.out.println("target 一共有 " + target.length + " 行; " + target[0].length + " 列");

        DatamationTools inputMation = new DatamationTools(input);
        DatamationTools targetMation = new DatamationTools(target);

        BPNN bp = new BPNN(3,25,1,0.05,0.05);
        int p = 0;
        double error = 100;
        while(p<30000000 && error>0.011) {
            error = 0;
            for (int i = 0; i < 9000; i++) {
                double[] in = inputMation.getNormal()[i];
                double[] out = targetMation.getNormal()[i];
//                in[0] = inputMation.getNormal()[i][0];
//                in[1] = inputMation.getNormal()[i][1];
//                in[2] = inputMation.getNormal()[i][2];
//                out[0] = targetMation.getNormal()[i][0];
                bp.train(in, out);
                p++;
                error += bp.sqr_err;
            }
            System.out.println("训练次数:"+p+"     error:"+error);
        }

//        FileTools fileTools = new FileTools("public-train");
//        String data = fileTools.readFile();
//        DataTools dataTools = new DataTools(3, 1, data);
//
//        double[][] input = dataTools.getDataList();
//        double[][] target = dataTools.getTarget();
//
//        System.out.println("input 一共有 " + input.length + " 行; " + input[0].length + " 列");
//        System.out.println("target 一共有 " + target.length + " 行; " + target[0].length + " 列");
//
//        DatamationTools inputMation = new DatamationTools(input);
//        DatamationTools targetMation = new DatamationTools(target);
//        BP bp = new BP();
//        System.out.println("开始训练~~");
//        bp.train(inputMation.getNormal(), targetMation.getNormal());
//        System.out.println("训练结束~~");

//        double[] yName = new double[target.length];
//        double[] real = new double[target.length];
//        double[] out = new double[target.length];
//        for (int i = 0; i < target.length; i++) {
//            yName[i] = i;
//            real[i] = target[i][0];
//            out[i] = targetMation.getReal(bp.test(inputMation.getNormal()[i]), 0);
//
//        }

        /**
         * 生成图表过程
         */
//        SwingUtilities.invokeLater(() -> {
//            JFreeCharts example = new JFreeCharts("Line Chart Example", real, out, yName);
//            example.setAlwaysOnTop(true);
//            example.pack();
//            example.setSize(600, 400);
//            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            example.setVisible(true);
//        });

//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("Please enter the parameter of input:");
//        double parameter;
//        while((parameter = keyboard.nextDouble()) != -1)
//            System.out.println(parameter + "*" + parameter + "=" + targetMation.getReal(bp.bpNetOut(in)[0], 0));
    }
}
