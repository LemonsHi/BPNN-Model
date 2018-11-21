package com.tools;

public class DataTools {

    private int paramsNum;

    private int targetNum;

    private String _dataStr = "";

    private double[][] dataList;

    private double[][] target;

    public DataTools(int paramsNum, int targetNum, String _dataStr) {
        this.paramsNum = paramsNum;
        this.targetNum = targetNum;
        this._dataStr = _dataStr;
        this.data();
    }

    private void data () {
        String[] _data_x = _dataStr.split("\n");
        int _length_ = _data_x.length;
        dataList = new double[_length_ - 1][paramsNum];
        target = new double[_length_ - 1][targetNum];
        // 从一开始的目的是为了：规避首行数据说明
        for (int i = 1; i < _length_; i++) {
            String[] _data_y = _data_x[i].split(",");

            // 生成输入数据集
            dataList[i - 1][0] = Double.valueOf(_data_y[2]);
            dataList[i - 1][1] = Double.valueOf(_data_y[18]);
            dataList[i - 1][2] = Math.sin(Double.valueOf(_data_y[19]) / 180 * Math.PI);
            dataList[i - 1][3] = Math.cos(Double.valueOf(_data_y[19]) / 180 * Math.PI);
//            System.out.println(Math.sin(dataList[i - 1][2] / 180 * Math.PI));

            // 生成目标数据集
            target[i - 1][0] = Double.valueOf(_data_y[17]);
        }
    }

    public double[][] getDataList() {
        return dataList;
    }

    public double[][] getTarget() {
        return target;
    }
}
