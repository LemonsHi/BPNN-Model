//package com.model;
//
//import com.tools.FileTools;
//import com.tools.JSONTools;
//import org.json.JSONObject;
//
//public class BP {
//    private int Mount = 1000000;
//    private int mout = 0;
//    private int IM = 1;                                     // 输入层数量 -- 通过 config.json 文件配置输入
//    private int RM = 8;                                     // 隐含层数量 -- 通过 config.json 文件配置输入
//    private int OM = 1;                                     // 输出层数量 -- 通过 config.json 文件配置输入
//    private double learnRate = 0.55;                        // 学习速率 -- 通过 config.json 文件配置输入
//    private double alfa = 0.67;                             // 动量因子 -- 通过 config.json 文件配置输入
//    private double Win[][] = new double[IM][RM];            // 输入到隐含连接权值
//    private double oldWin[][] = new double[IM][RM];         //
//    private double old1Win[][] = new double[IM][RM];        //
//    private double dWin[][] = new double[IM][RM];           //
//    private double Wout[][] = new double[RM][OM];           // 隐含到输出连接权值
//    private double oldWout[][] = new double[RM][OM];        //
//    private double old1Wout[][] = new double[RM][OM];       //
//    private double dWout[][] = new double[RM][OM];          //
//    private double Xi[] = new double[IM];                   // 归一化后的输入值
//    private double Xj[] = new double[RM];                   // 隐层计算后的值
//    private double XjActive[] = new double[RM];             // 经过 S 激活函数后的值
//    private double Xk[] = new double[OM];                   // 隐层到输出层的计算值
//    private double Ek[] = new double[OM];                   //
//    private double J = 0.1;                                 // 计算误差
//    private double epsilon = Math.pow(10, -17);             // 跳出流程阈值
//
//    public BP () {
//        FileTools fileTools = new FileTools("config");
//        JSONTools jsonTools = new JSONTools(fileTools.getConfigData());
//        JSONObject bpConfig = jsonTools.getBPConfig();
//        IM = (int) bpConfig.get("inputSize");
//        RM = (int) bpConfig.get("hiddenSize");
//        OM = (int) bpConfig.get("outputSize");
//        learnRate = (double) bpConfig.get("eta");
//        alfa = (double) bpConfig.get("alfa");
//        this.initNet();
//    }
//
//    public void train (double[][] input, double[][] target) {
//        bpNetinit();
//
//        while (evaluate()) {
//            for (int i = 0, length = input.length; i < length; i++) {
//                // 向前计算输出过程
//                bpNetForwardProcess(input[i], target[i]);
//                // 反向学习修改权值
//                bpNetReturnProcess();
//            }
//        }
//    }
//
//    /**
//     * BP 神经网络前向计算输出，训练结束后测试输出
//     * @param input 测试的归一化后的输入数组
//     * @return 返回归一化后的BP神经网络输出数组
//     */
//    public double[] bpNetOut (double[] input) {
//        // 在线学习后输出
//        for(int i = 0; i < IM; i++) {
//            Xi[i] = input[i];
//        }
//        // 隐含层权值和计算
//        for(int j = 0; j < RM; j++) {
//            Xj[j] = 0;
//            for(int i = 0; i < IM; i++) {
//                Xj[j] = Xj[j] + Xi[i] * Win[i][j];
//            }
//        }
//        // 隐含层 S 激活输出
//        for(int j = 0; j < RM; j++) {
//            XjActive[j] = sigmoid(Xj[j]);
//        }
//        // 输出层权值和计算
//        double Uk[] = new double[OM];
//        for(int k = 0; k < OM; k++) {
//            Xk[k] = 0;
//            for(int j = 0; j < RM; j++) {
//                Xk[k] = Xk[k] + XjActive[j] * Wout[j][k];
//                Uk[k] = Xk[k];
//            }
//        }
//        return Uk;
//    }
//
//    private void initNet () {
//        Win = new double[IM][RM];
//        oldWin = new double[IM][RM];
//        old1Win = new double[IM][RM];
//        dWin = new double[IM][RM];
//        Wout = new double[RM][OM];
//        oldWout = new double[RM][OM];
//        old1Wout = new double[RM][OM];
//        dWout = new double[RM][OM];
//        Xi = new double[IM];
//        Xj = new double[RM];
//        XjActive = new double[RM];
//        Xk = new double[OM];
//        Ek = new double[OM];
//    }
//
//    /**
//     * BP 神经网络前向计算输出过程
//     * @param input
//     * @param target
//     */
//    private void bpNetForwardProcess (double[] input, double[] target) {
//        for (int i = 0; i < IM; i++) {
//            Xi[i] = input[i];
//        }
//        // 隐含层权值和计算
//        for (int j = 0; j < RM; j++) {
//            // 阀值为 0
//            Xj[j] = 0;
//            for (int i = 0; i < IM; i++) {
//                Xj[j] = Xj[j] + Xi[i] * Win[i][j];
//            }
//        }
//        // 隐含层 S 激活输出
//        for (int i = 0; i < RM; i++) {
//            XjActive[i] = sigmoid(Xj[i]);
//        }
//        // 输出层权值和计算
//        for (int k = 0; k < OM; k++) {
//            Xk[k] = 0;
//            for (int j = 0; j < RM; j++) {
//                Xk[k] = Xk[k] + XjActive[j] * Wout[j][k];
//            }
//        }
//        // 计算输出与理想输出的偏差
//        for(int k = 0; k < OM; k++) {
//            Ek[k] = target[k] - Xk[k];
//        }
//        J = 0d;
//        // 采用均方差
//        for(int k = 0; k < OM; k++) {
//            J = J + Ek[k] * Ek[k] / 2d;
//        }
//        // 优化写法
////        J = 0;
////        for(int k = 0; k < OM; k++) {
////            J += 0.5 * (Math.pow(target[k] - Xk[k], 2));
////        }
//    }
//
//    /**
//     * BP 神经网络反向学习修改连接权值过程
//     */
//    private void bpNetReturnProcess () {
//        //输入到隐含权值修正
//        for(int i = 0; i < IM; i++) {
//            for(int j = 0; j < RM; j++) {
//                for(int k = 0; k < OM; k++) {
//                    dWin[i][j] = dWin[i][j] + learnRate * (Ek[k] * Wout[j][k] * XjActive[j] * (1 - XjActive[j]) * Xi[i]);
//                }
//                Win[i][j] = Win[i][j] + dWin[i][j] + alfa * (oldWin[i][j] - old1Win[i][j]);
////                Win[i][j] = Win[i][j] + dWin[i][j];
//                old1Win[i][j] = oldWin[i][j];
//                oldWin[i][j] = Win[i][j];
//            }
//        }
//        //隐含到输出权值修正
//        for(int j = 0; j < RM; j++) {
//            for(int k = 0; k < OM; k++) {
////                dWout[j][k] = learnRate * Ek[k] * XjActive[j];
//                Wout[j][k] = Wout[j][k] + dWout[j][k] + alfa * (oldWout[j][k] - old1Wout[j][k]);
//                Wout[j][k] = Wout[j][k] + dWout[j][k];
//                old1Wout[j][k] = oldWout[j][k];
//                oldWout[j][k] = Wout[j][k];
//            }
//        }
//    }
//
//    /**
//     * 判断是否满足精度条件
//     * @return
//     */
//    private boolean evaluate () {
//        if (J < epsilon || mout > Mount) {
//            for (int i = 0; i < IM; i++) {
//                for (int j = 0; j < RM; j++) {
//                    System.out.println("Win: [" + i + ", " + j + "] = " + Win[i][j]);
//                }
//            };
//            for (int k = 0; k < OM; k++) {
//                for (int j = 0; j < RM; j++) {
//                    System.out.println("Wout: [" + j + ", " + k + "] = " + Wout[j][k]);
//                }
//            }
//            return false;
//        }
//        else {
//            mout++;
//            return true;
//        }
//    }
//
//    /**
//     * BP 神经网络权值随机初始化
//     * Win[i][j] 和 Wout[j][k] 权值初始化为 [-0.5,0.5] 之间
//     */
//    public void bpNetinit () {
//        for(int i = 0; i < IM; i++) {
//            for(int j = 0; j < RM; j++) {
//                Win[i][j] = 0.5 - Math.random();
////                Win[i][j] = (0.5 - Math.random()) * 2;
//                Xj[j] = 0;
//            }
//        }
//        nguyenwidrow(IM, RM, Win);
//        for(int j = 0; j < RM; j++) {
//            for(int k = 0; k < OM; k++) {
//                Wout[j][k] = 0.5 - Math.random();
////                Wout[j][k] = (0.5 - Math.random()) * 2;
//                Xk[k] = 0;
//            }
//        }
//        nguyenwidrow(RM, OM, Wout);
////        Win[0][0] = 212.71055165310764;
////        Win[0][1] = -258.83370748971777;
////        Win[0][2] = -255.9938223882389;
////        Win[0][3] = -493.48105640945056;
////        Win[0][4] = -104.61654620639727;
////        Win[0][5] = 5074.928271606507;
////
////        Win[1][0] = 56.42554097891643;
////        Win[1][1] = -215.0702478897572;
////        Win[1][2] = -85.97823064774722;
////        Win[1][3] = 107.5076768990725;
////        Win[1][4] = -143.07249634721381;
////        Win[1][5] = 868.1561664643829;
////
////        Win[2][0] = 207.60310557355473;
////        Win[2][1] = -262.94035865852055;
////        Win[2][2] = -191.98688534711792;
////        Win[2][3] = -185.53191151378186;
////        Win[2][4] = -36.96107998815517;
////        Win[2][5] = 3668.6533394445773;
////
////        Wout[0][0] = -2.1212121212160824;
////        Wout[1][0] = -2.1212121212160824;;
////        Wout[2][0] = 2.1212121212160824;
////        Wout[3][0] = 2.1212121212160824;
////        Wout[4][0] = -2.1212121212160824;
////        Wout[5][0] = 2.1212121212160824;
//    }
//
//    /**
//     * Nguyen-widrow 算法 - 初始化权值
//     * @param in 输入层数
//     * @param out 输出层数
//     * @param w 范围是 [-0.5, 0.5] 之间的权值
//     */
//    private void nguyenwidrow (int in, int out, double[][] w) {
//        double v = 0.7 * Math.pow(out, in);
//        double[] t = new double[w.length];
//        for (int i = 0; i < in; i++) {
//            for (int j = 0; j < out; j++) {
//                t[i] += Math.pow(w[i][j], 2);
//            }
//        }
//        for (int i = 0; i < in; i++) {
//            for (int j = 0; j < out; j++) {
//                w[i][j] = v * (w[i][j] / Math.pow(t[i], 0.5));
//            }
//        }
//    }
//
//    /**
//     * Sigmoid 函数
//     * @param val
//     * @return
//     */
//    private double sigmoid (double val) {
//        return 1d / (1d + Math.exp(-val));
//    }
//}