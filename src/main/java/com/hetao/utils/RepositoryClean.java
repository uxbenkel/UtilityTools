package com.hetao.utils;

import java.io.File;

/**
 * 清理maven仓库，要清理的内容包括:
 * 1.以".lastUpdated"结尾的文件
 * 2.不含".jar"文件的无效文件夹
 * 3.空文件夹
 * @author hetao
 * @date 2019-01-13
 */
public class RepositoryClean {

    // 本次操作删除以".lastUpdated"结尾的文件数量
    private static int lastUpdateNum = 0;
    // 本次操作删除的无效文件夹数量
    private static int invalidDirNum = 0;
    // 本次操作删除的空文件夹数量
    private static int blankDirNum = 0;

    // 多次执行本方法，直到打印出三个0
    public static void main(String[] args) {
        final String mavenRepoPath = "/Users/hetao/Maven/Maven_Repository";
        File file = new File(mavenRepoPath);
        RepositoryClean rc = new RepositoryClean();
        // 这里一次执行清理不完 多次执行直到打印结果全部为 0
        rc.cleanLastUpdate(file);
        rc.cleanInvalidDir(file);
        System.out.println("操作完成：删除lastUpdated文件 " + lastUpdateNum + "个, 删除无效文件夹" + invalidDirNum + "个, 删除空文件夹" + blankDirNum + "个");
    }

    /**
     * 删除所有以".lastUpdated"结尾的文件
     * @param file 需要清理的文件夹
     */
    public void cleanLastUpdate(File file) {
        if (file.getName().endsWith("lastUpdated")) {
            file.delete();
            System.out.println(file.getAbsolutePath() + "以'lastupdated'结尾被删除");
            lastUpdateNum++;
        }
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            assert fs != null;
            for (File f : fs) {
                cleanLastUpdate(f);
            }
        }
    }

    /**
     * 删除无效文件夹
     * @param file 需要清理的文件夹
     */
    private void cleanInvalidDir(File file) {
        if (file != null && file.isDirectory()) {
            File[] fs = file.listFiles();
            assert fs != null;
            if (fs.length == 0) {
                file.delete();
                System.out.println(file.getAbsolutePath() + " 是空文件夹被删除");
                blankDirNum++;
            } else {
                if (checkFiles(fs)) {
                    deleteDir(file);
                    System.out.println(file.getAbsolutePath() + " 是无效文件夹被删除");
                    invalidDirNum++;
                } else {
                    for (File f : fs) {
                        cleanInvalidDir(f);
                    }
                }
            }
        }
    }

    /**
     * 检查文件夹内容
     * @param fs 需要检查的文件夹内容list
     * @return 检查结果
     */
    private boolean checkFiles(File[] fs) {
        for (File f : fs) {
            // 判断当前文件夹是否包含子文件夹
            if (f.isDirectory()) {
                return false;
            }
            // 判断当前文件夹是否包含".jar"文件
            if (f.getName().endsWith("jar")) {
                return false;
            }
        }
        // 若以上两点都不满足,则判定为无效文件夹,返回true
        return true;
    }

    /**
     * 删除非空文件夹
     * @param file 待删除的文件夹
     */
    private void deleteDir(File file) {
        assert file != null;
        File[] fs = file.listFiles();
        if (fs != null) {
            for (File f : fs) {
                f.delete();
            }
        }
    }
}
