package com.model;

/**
 * @author pengxingxiong
 * 多层BP神经网络算法（随机梯度下降法版）<br>
 * 默认为三层BP神经网络，即输入层，隐含层和输出层；
 * 如果要构建多层BP神经网络，则进行多次前向传播过程即可。
 *
 * */
import java.util.Random;
public class BPNN {
    /**输入层输入向量*/
    private double[] input;
    /**神经元个数[输入层神经元个数，隐含层神经元个数，输出层神经元个数]*/
    private int[] Size;
    /**隐含层输出向量*/
    private double[] hidden;
    /**输出层输出向量*/
    private double[] output;
    /**输出层期望输出向量*/
    private double[] outputExcept;
    /**输入层与隐含层的连接权重*/
    private double[][] Wih;
    /**修改后的输入层与隐含层的连接权重*/
    private double[][] updateWih;
    /**隐含层与输出层的连接权重*/
    private double[][] Who;
    /**修改后的隐含层与输出层的连接权重*/
    private double[][] updateWho;
    /**每个输出层神经元的误差*/
    private double[] Oe;
    /**每个隐含层神经元的误差*/
    private double[] He;
    /**最终输出的总误差*/
    public double Se;
    /**隐含层各神经元的阈值*/
    private double[] Th;
    /**输出层各神经元的阈值*/
    private double[] To;
    /**学习率*/
    private double L;
    /**动量*/
    private double momentum;
    /**随机化参数*/
    private Random random;
    /**构造函数
     * @param input <输入值，期望输出值>
     * @param Size [输入层神经元个数，隐含层神经元个数，输出层神经元个数]
     * @param L 学习率
     * @param momentum 动量
     * */
    public BPNN (int[] Size, double L, double momentum) {
        this.Size		= new int[Size.length];
        System.arraycopy(Size, 0, this.Size, 0, Size.length);
        this.hidden 	= new double[Size[1]];
        this.output 	= new double[Size[2]];
        this.Wih		= new double[Size[0]][Size[1]];
        this.Who		= new double[Size[1]][Size[2]];
        this.updateWih	= new double[Size[0]][Size[1]];
        this.updateWho 	= new double[Size[1]][Size[2]];
        this.Oe			= new double[Size[2]];
        this.He			= new double[Size[1]];
        this.Th			= new double[Size[1]];
        this.To			= new double[Size[2]];
        this.L			= L;
        this.momentum 	= momentum;
        random 			=new Random();
        initWeight(Wih);
        initWeight(Who);
        initThreshold(Th);
        initThreshold(To);

    }
    /**
     * 初始化权值
     */
    private void initWeight (double[][] W) {
        for(int i = 0;i < W.length;i++){
            for(int j = 0;j < W[i].length;j++){
                double R = random.nextDouble();
                W[i][j] = random.nextDouble()> 0.5 ? R : -R;
            }
        }
    }
    /**
     * 初始化神经元阈值
     */
    private void initThreshold (double[] T) {
        for(int i=0;i < T.length;i++){
            double R = random.nextDouble();
            T[i] = random.nextDouble()> 0.5 ? R : -R;
        }
    }

    /**
     * 利用训练集数据训练神经网络
     * @param dataInput 一组训练样本的输入值
     * @param dataOutput 一组训练样本的输出值
     */
    public void train (double[] dataInput,double[] dataOutput) {
        input = new double[dataInput.length];
        outputExcept = new double[dataOutput.length];
        System.arraycopy(dataInput, 0, input, 0, dataInput.length);
        System.arraycopy(dataOutput, 0, outputExcept, 0, dataOutput.length);
        forwardPropagation(input,hidden,Wih,Th);
        forwardPropagation(hidden,output,Who,To);
        reverseSpread();
        updateWeight();
        updateThreshold();
    }
    /**
     * 利用测试集数据来预测输出值
     * @param dataInput 一组测试样本的输入值
     */
    public double[] test (double[] dataInput) {
        System.arraycopy(dataInput, 0, input, 0, dataInput.length);
        forwardPropagation(input,hidden,Wih,Th);
        forwardPropagation(hidden,output,Who,To);
        return output;
    }


    /**
     * 向前传播输入
     */
    private void forwardPropagation (double[] input,double[]output,double[][] weight,double[] threshold) {
        for(int j = 0;j < output.length;j++){
            output[j] = 0;
            for(int i = 0;i < input.length;i++){
                output[j] += input[i] * weight[i][j];
            }
            output[j] = sigmoid(output[j]+threshold[j]);
        }
    }
    /**
     * 反向误差传播
     */
    private void reverseSpread () {
        //计算输出层误差
        Se = 0;
        for(int i = 0;i < output.length;i++){
            Oe[i] = output[i] * (1 - output[i]) * (outputExcept[i]-output[i]);
            Se += Math.abs(Oe[i]);
        }
        //计算隐含层误差
        for(int i = 0;i < hidden.length;i++){
            He[i] = 0;
            for(int j = 0;j < output.length;j++){
                He[i] += hidden[i] * (1-hidden[i]) * Who[i][j] * Oe[j];
            }
        }
    }
    /**
     * 更新网络权重
     */
    private void updateWeight () {
        //更新隐含层与输出层间的权值(为了使算法加速收敛，增加动量项)
        for(int i = 0;i < hidden.length;i++){
            for(int j = 0;j < output.length;j++){
                updateWho[i][j] =  (L * hidden[i] * Oe[j]) + (momentum * updateWho[i][j]);
                Who[i][j] += updateWho[i][j];
            }
        }
        //更新输入层与隐含层间的权值(为了使算法加速收敛，增加动量项)
        for(int i = 0;i < input.length;i++){
            for(int j = 0;j < hidden.length;j++){
                updateWih[i][j] =  (L * input[i] * He[j]) + (momentum * updateWih[i][j]);
                Wih[i][j] += updateWih[i][j];
            }
        }
    }
    /**
     * 更新神经元阈值
     */
    private void updateThreshold () {
        //更新隐含层各神经元阈值
        for(int i = 0;i < input.length;i++){
            Th[i] += L * He[i];
        }
        //更新输出层各神经元阈值
        for(int i = 0;i < output.length;i++){
            To[i] += L * Oe[i];
        }
    }
    /**
     * 激活函数，用于求神经元的输出值
     */
    private double sigmoid (double val) {
        return 1.0 / (1 + Math.exp(-val));
    }
    /**相关属性的Get方法*/
    public double[] getOutput() {
        return output;
    }
    public double[][] getWih() {
        return Wih;
    }
    public double[][] getWho() {
        return Who;
    }
    public double[] getTh() {
        return Th;
    }
    public double[] getTo() {
        return To;
    }
}
