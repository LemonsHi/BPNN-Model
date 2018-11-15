package com.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileTools {
    // 私有 - 文件名
    private String _fileName = "";

    public FileTools(String fileName) {
        this._fileName = fileName;
    }

    /**
     * 读取文件私有方法
     * @param path 文件路径
     * @return 文件内容
     */
    private String _readFile (String path) {
        String laststr = "";
        BufferedReader reader = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

    /**
     * 获取 Config 配置文件
     * @return String 配置信息
     */
    public String getConfigData () {
        String path="src/com/config/" + this._fileName + ".json";
        String laststr = this._readFile(path);
        return laststr;
    }
}
