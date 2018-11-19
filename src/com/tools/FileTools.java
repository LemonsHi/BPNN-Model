package com.tools;

import java.io.*;

public class FileTools {
    // 私有 - 文件名
    private String _fileName = "";

    public FileTools(String fileName) {
        this._fileName = fileName;
    }

    /**
     * 创建文件目录
     * @param file 目标文件类
     */
    private void _createDir (File file) {
        File dir = new File(file.getParent());
        dir.mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入文件 - 大文件写入
     * @param path 文件路径 - 路径 + 文件名
     * @param fileContent 写入文件内容
     * @return true - 文件写入成功; false - 文件写入失败
     */
    private boolean _writeFile (String path, String fileContent) {
        File file = new File(path);
        // if file doesnt exists, then create it
        FileWriter fw = null;
        boolean flag = false;
        try {
            if (!file.exists()) {
                this._createDir(file);
            }
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fileContent);
            bw.close();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 读取文件私有方法 - 大文件读取
     * @param path 文件路径
     * @return 文件内容
     */
    private String _readFile (String path) {
        String data = "";
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file), 1024);   //如果是读大文件，设置缓存
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                System.out.println("读取数为: " + tempString);
                data += tempString;
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
        return data;
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

    public boolean writeFile (String content) {
        String path="src/com/data/" + this._fileName + ".txt";
        return this._writeFile(path, content);
    }

    public String readFile () {
        String path="src/com/data/" + this._fileName + ".txt";
        return this._readFile(path);
    }

//    public static void main(String[] args) {
//        FileTools fileTools = new FileTools("test");
//        if (fileTools.writeFile()) {
//            System.out.println("success!");
//            System.out.println("数据为: " + fileTools.readFile());
//        } else {
//            System.out.println("error!");
//        }
//    }
}
