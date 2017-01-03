package com.grandtech.map.utils.commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zy on 2016/10/18.
 */

public class FileHelper {

    public static boolean isExist(String path){
        File f = new File(path);
        return f.exists();
    }

    /**
     * 给定文件路径 获取文件名
     * @param path
     * @return
     */
    public static String getFileNameByPath(String path){
        try {
            String fileName = path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));
            return fileName;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 给定文件路径 获取文件后缀名
     * @param path
     * @return
     */
    public static String getFileExtendNameByPath(String path){
        try {
            String fileName = path.substring(path.lastIndexOf('.'));
            return fileName;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除指定文件所在文件夹 所有同名的文件 后缀可以不同
     * @param path
     * @return
     */
    public static boolean deleteSameNameFile(String path){
        boolean flag;
        if(isExist(path)) {
            try {
                File parentFile = new File(path).getParentFile();
                String fileName = getFileNameByPath(path);
                String fileExtendName = getFileExtendNameByPath(path);
                File[] files = parentFile.listFiles();
                for (File f : files) {
                    String name=f.getName().substring(0,f.getName().lastIndexOf('.'));
                    if (name.equals(fileName)) {
                        f.delete();
                    }
                }
                flag =true;
            }catch (Exception e){
                flag=false;
            }
        }else {
            flag=false;
        }
        return false;
    }

    /**
     * 文件删除成功 true；不存在或者未成功返回 false
     * @param path
     * @return
     */
    public static boolean deleteFile(String path){
        if(isExist(path)) {
            File f = new File(path);
            return f.delete();
        }else {
            return false;
        }
    }

    /**
     * 创建指定文件夹
     *
     * @param path
     */
    public static void createFile(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    /**
     * 迭代打印文件名称
     *
     * @param path
     */
    public static void getFileNameInFolider(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f : files) {
            System.out.println(f.getAbsoluteFile().toString());
            if (f.isDirectory()) {
                path = f.getAbsolutePath();
                getFileNameInFolider(path);
            }
        }
    }

    public static String readRemoteFile(String filePath) {
        StringBuilder fileContent = new StringBuilder();
        try {
            InputStream is = new FileInputStream(filePath);
            String line; // 用来保存每行读取的内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine(); // 读取第一行
            while (line != null) { // 如果 line 为空说明读完了
                fileContent.append(line); // 将读到的内容添加到 buffer 中
                fileContent.append("\n"); // 添加换行符
                line = reader.readLine(); // 读取下一行
            }
            reader.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }


}
