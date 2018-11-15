package com.model;

import com.tools.FileTools;
import com.tools.JSONTools;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;

import static jdk.nashorn.internal.objects.NativeMath.max;

public class BP {
    private int IM = 1;                                     // 输入层数量 -- 通过 config.json 文件配置输入
    private int RM = 8;                                     // 隐含层数量 -- 通过 config.json 文件配置输入
    private int OM = 1;                                     // 输出层数量 -- 通过 config.json 文件配置输入
    private double learnRate = 0.55;                        // 学习速率 -- 通过 config.json 文件配置输入
    private double alfa = 0.67;                             // 动量因子 -- 通过 config.json 文件配置输入
    private double Win[][] = new double[IM][RM];            // 输入到隐含连接权值
    private double oldWin[][] = new double[IM][RM];         //
    private double old1Win[][] = new double[IM][RM];        //
    private double dWin[][] = new double[IM][RM];           //
    private double Wout[][] = new double[RM][OM];           // 隐含到输出连接权值
    private double oldWout[][] = new double[RM][OM];        //
    private double old1Wout[][] = new double[RM][OM];       //
    private double dWout[][] = new double[RM][OM];          //
    private double Xi[] = new double[IM];                   // 归一化后的输入值
    private double Xj[] = new double[RM];                   // 隐层计算后的值
    private double XjActive[] = new double[RM];             // 经过 S 激活函数后的值
    private double Xk[] = new double[OM];                   // 隐层到输出层的计算值
    private double Ek[] = new double[OM];                   //
    private double J = 0.1;                                 // 计算误差
    private double epsilon = Math.pow(10, -15);                         // 跳出流程阈值

    public BP () {
        FileTools fileTools = new FileTools("config");
        JSONTools jsonTools = new JSONTools(fileTools.getConfigData());
        JSONObject bpConfig = jsonTools.getBPConfig();
        IM = (int) bpConfig.get("inputSize");
        RM = (int) bpConfig.get("hiddenSize");
        OM = (int) bpConfig.get("outputSize");
        learnRate = (double) bpConfig.get("eta");
        alfa = (double) bpConfig.get("alfa");
        this.initNet();
    }

    public void train (double[][] input, double[][] target) {
        bpNetinit();

        while (evaluate()) {
            for (int i = 0, length = input.length; i < length; i++) {
                // 向前计算输出过程
                bpNetForwardProcess(input[i], target[i]);
                // 反向学习修改权值
                bpNetReturnProcess();
            }
        }
    }

    /**
     * BP 神经网络前向计算输出，训练结束后测试输出
     * @param input 测试的归一化后的输入数组
     * @return 返回归一化后的BP神经网络输出数组
     */
    public double[] bpNetOut (double[] input) {
        // 在线学习后输出
        for(int i = 0; i < IM; i++) {
            Xi[i] = input[i];
        }
        // 隐含层权值和计算
        for(int j = 0; j < RM; j++) {
            Xj[j] = 0;
            for(int i = 0; i < IM; i++) {
                Xj[j] += Xi[i] * Win[i][j];
            }
        }
        // 隐含层 S 激活输出
        for(int j = 0; j < RM; j++) {
            XjActive[j] = sigmoid(Xj[j]);
        }
        // 输出层权值和计算
        double Uk[] = new double[OM];
        for(int k = 0; k < OM; k++) {
            Xk[k] = 0;
            for(int j = 0; j < RM; j++) {
                Xk[k] += XjActive[j] * Wout[j][k];
                Uk[k] = Xk[k];
            }
        }
        return Uk;
    }

    private void initNet () {
        Win = new double[IM][RM];
        oldWin = new double[IM][RM];
        old1Win = new double[IM][RM];
        dWin = new double[IM][RM];
        Wout = new double[RM][OM];
        oldWout = new double[RM][OM];
        old1Wout = new double[RM][OM];
        dWout = new double[RM][OM];
        Xi = new double[IM];
        Xj = new double[RM];
        XjActive = new double[RM];
        Xk = new double[OM];
        Ek = new double[OM];
    }

    /**
     * BP 神经网络前向计算输出过程
     * @param input
     * @param target
     */
    private void bpNetForwardProcess (double[] input, double[] target) {
        for (int i = 0; i < IM; i++) {
            Xi[i] = input[i];
        }
        // 隐含层权值和计算
        for (int j = 0; j < RM; j++) {
            // 阀值为 0
            Xj[j] = 0;
            for (int i = 0; i < IM; i++) {
                Xj[j] += Xi[i] * Win[i][j];
            }
        }
        // 隐含层 S 激活输出
        for (int i = 0; i < RM; i++) {
            XjActive[i] = sigmoid(Xj[i]);
        }
        // 输出层权值和计算
        for (int k = 0; k < OM; k++) {
            Xk[k] = 0;
            for (int j = 0; j < RM; j++) {
                Xk[k] += XjActive[j] * Wout[j][k];
            }
        }
        // 计算输出与理想输出的偏差
        for(int k = 0; k < OM; k++) {
            Ek[k] = target[k] - Xk[k];
        }
        J = 0;
        // 采用均方差
        for(int k = 0; k < OM; k++) {
            J += Ek[k] * Ek[k] / 2.0;
        }
        // 优化写法
//        J = 0;
//        for(int k = 0; k < OM; k++) {
//            J += 0.5 * (Math.pow(target[k] - Xk[k], 2));
//        }
    }

//    /**
//     * 归一化公式
//     * @param val
//     * @return
//     */
//    private double normal (double val, int i, boolean type) {
////        double max, min, value;
////        if (type) {
////            max = (double) Collections.max(Arrays.asList(aimInList[i]));
////            min = (double) Collections.min(Arrays.asList(aimInList[i]));
////        } else {
////            max = (double) Collections.max(Arrays.asList(aimTarList[i]));
////            min = (double) Collections.min(Arrays.asList(aimTarList[i]));
////        }
////        return (val - min) / (max - min);
//    }

    /**
     * BP 神经网络反向学习修改连接权值过程
     */
    private void bpNetReturnProcess () {
        //输入到隐含权值修正
        for(int i = 0; i < IM; i++) {
            for(int j = 0; j < RM; j++) {
                for(int k = 0; k < OM; k++) {
                    dWin[i][j] = dWin[i][j] + learnRate * (Ek[k] * Wout[j][k] * XjActive[j] * (1 - XjActive[j]) * Xi[i]);
                }
                Win[i][j] = Win[i][j] + dWin[i][j] + alfa * (oldWin[i][j] - old1Win[i][j]);
                old1Win[i][j] = oldWin[i][j];
                oldWin[i][j] = Win[i][j];
            }
        }
        //隐含到输出权值修正
        for(int j = 0; j < RM; j++) {
            for(int k = 0; k < OM; k++) {
                dWout[j][k] = learnRate * Ek[k] * XjActive[j];
                Wout[j][k] = Wout[j][k] + dWout[j][k] + alfa * (oldWout[j][k] - old1Wout[j][k]);
                old1Wout[j][k] = oldWout[j][k];
                oldWout[j][k] = Wout[j][k];
            }
        }
    }

    /**
     * 判断是否满足精度条件
     * @return
     */
    private boolean evaluate () {
        if (J < epsilon)
            return false;
        else
            return true;
    }

    /**
     * BP 神经网络权值随机初始化
     * Win[i][j] 和 Wout[j][k] 权值初始化为 [-0.5,0.5] 之间
     */
    public void bpNetinit () {
        for(int i = 0; i < IM; i++) {
            for(int j = 0; j < RM; j++) {
                Win[i][j] = 0.5 - Math.random();
//                Win[i][j] = 0;
                Xj[j] = 0;
            }
        }
        for(int j = 0; j < RM; j++) {
            for(int k = 0; k < OM; k++) {
                Wout[j][k] = 0.5 - Math.random();
//                Wout[j][k] = 0;
                Xk[k] = 0;
            }
        }
    }

    /**
     * Sigmoid 函数
     * @param val
     * @return
     */
    private double sigmoid (double val) {
        return 1.0 / (1.0 + Math.exp(-val));
    }
}
