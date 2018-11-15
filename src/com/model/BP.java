package com.model;

import com.tools.FileTools;
import com.tools.JSONTools;
import org.json.JSONObject;

public class BP {
    private int IM = 1;                  //输入层数量
    private int RM = 8;                  //隐含层数量
    private int OM = 1;                  //输出层数量
    private double learnRate = 0.55;                       //学习速率
    private double alfa = 0.67;                            //动量因子
    private double Win[][] = new double[IM][RM];     //输入到隐含连接权值
    private double oldWin[][] = new double[IM][RM];
    private double old1Win[][] = new double[IM][RM];
    private double dWin[][] = new double[IM][RM];
    private double Wout[][] = new double[RM][OM];    //隐含到输出连接权值
    private double oldWout[][] = new double[RM][OM];
    private double old1Wout[][] = new double[RM][OM];
    private double dWout[][] = new double[RM][OM];
    private double Xi[] = new double[IM];
    private double Xj[] = new double[RM];
    private double XjActive[] = new double[RM];
    private double Xk[] = new double[OM];
    private double Ek[] = new double[OM];
    private double J = 0.1;
    private double epsilon = Math.pow(10, -12);

    public BP () {
        FileTools fileTools = new FileTools("config");
        JSONTools jsonTools = new JSONTools(fileTools.getConfigData());
        JSONObject bpConfig = jsonTools.getBPConfig();
        IM = (int) bpConfig.get("inputSize");
        RM = (int) bpConfig.get("hiddenSize");
        OM = (int) bpConfig.get("outputSize");
        learnRate = (double) bpConfig.get("eta");
        alfa = (double) bpConfig.get("alfa");
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
            Xi[i] = normal(input[i]);
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

    /**
     * BP 神经网络前向计算输出过程
     * @param input
     * @param target
     */
    private void bpNetForwardProcess (double[] input, double[] target) {
        for (int i = 0; i < IM; i++) {
            Xi[i] = normal(input[i]);
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
            Ek[k] = normal(target[k]) - Xk[k];
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

    /**
     * 归一化公式
     * @param val
     * @return
     */
    private double normal (double val) {
        return val / 100.0;
    }

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
    private boolean evaluate() {
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
                Xj[j] = 0;
            }
        }
        for(int j = 0; j < RM; j++) {
            for(int k = 0; k < OM; k++) {
                Wout[j][k] = 0.5 - Math.random();
                Xk[k] = 0;
            }
        }
    }

    /**
     * Sigmoid 函数
     * @param val
     * @return
     */
    private double sigmoid(double val) {
        return 1.0 / (1.0 + Math.exp(-val));
    }
}
