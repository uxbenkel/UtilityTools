package com.hetao.utils;

import java.io.File;
import java.util.UUID;


/**
 * 文件批量重命名
 * @author hetao
 * @date 2019-01-13
 */
@SuppressWarnings("all")
public class FileBatchRename {

    //****************************自定义参数****************************
    // 自定义文件名前缀(可选,不需要请填"")
    private static final String PREFIX    = "wallpaper_";
    // 自定义文件名后缀(可选,不需要请填"")
    private static final String SUFFIX    = "";
    // 过滤文件扩展名(可选,修改需要加点,示例如下,不需要修改请设置为null)
    private static final String extension = ".jpg";
    // 重命名类型，默认为ORDER
    private static FileRenameType TYPE    = FileRenameType.ORDER;
    // 要批量重命名的文件所在文件夹路径
    private static final String FILEPATH  = "/Users/hetao/Downloads/123";
    //****************************自定义参数****************************

    // 重命名总数量统计
    private static int renameNum = 0;
    // 单文件夹内重命名计数器
    private static int count;

    // ORDER表示顺序命名，UUID表示使用UUID命名
    private enum FileRenameType {
        ORDER, UUID
    }

    public static void main(String[] args) {
        rename(new File(FILEPATH));
        System.out.println("重命名文件 " + renameNum + "个,已全部完成");
    }

    public static void rename(File file) {
        if (!file.isDirectory()) {
            String[] name = file.getName().split("\\.");
            String EXTENSION = "." + name[name.length - 1];
            if (extension != null) {
                EXTENSION = extension;
            }
            System.out.println("要重命名的文件是：" + file.getAbsolutePath());
            // filename 表示重命名后的名称
            String fileName;
            switch (TYPE) {
                case UUID:
                    fileName = (file.getParent() + "/" + PREFIX + UUID.randomUUID() + SUFFIX + EXTENSION).replace("-", "");
                    break;
                default:
                    fileName = (file.getParent() + "/" + PREFIX + count + SUFFIX + EXTENSION).replace("-", "");
                    break;
            }
            file.renameTo(new File(fileName));
            renameNum++;
            count++;
        } else {
            File[] fs = file.listFiles();
            assert fs != null;
            count = 1;
            for (File f : fs) {
                // 过滤隐藏文件,先处理所有的子文件
                if (!f.isHidden() && !f.isDirectory()) {
                    // 如果开启扩展名过滤则进行过滤
                    if (extension != null && !f.getName().endsWith(extension)) {
                        continue;
                    }
                    rename(f);
                }
            }
            for (File f : fs) {
                // 过滤隐藏文件,处理所有的子文件夹
                if (!f.isHidden() && f.isDirectory()) {
                    rename(f);
                }
            }
        }
    }
}
