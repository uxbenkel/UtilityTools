import javax.swing.*;
import java.io.File;
import java.util.UUID;


/**
 * 文件批量重命名
 * @author hetao
 * @date 2019-01-13
 */
public class FileBatchRename {

    // 重命名总数量统计
    private static int renameNum;
    // 单文件夹内重命名计数器
    private static int count;

    // ORDER表示顺序命名，UUID表示使用UUID命名
    private enum FileRenameType {
        ORDER, UUID
    }

    public static void renameMain(String[] fileRenameType, JComboBox<String> typeSelector, JTextField prefixText, JTextField suffixText, JTextField extensionText, JTextField filePathText, JTextArea jTextArea) {

        //****************************自定义参数****************************
        // 重命名类型，默认为ORDER
        FileRenameType TYPE = fileRenameType[typeSelector.getSelectedIndex()].equals("顺序") ? FileRenameType.ORDER : FileRenameType.UUID;
        // 自定义文件名前缀(可选,不需要请填"")
        String PREFIX = prefixText.getText().trim();
        // 自定义文件名后缀(可选,不需要请填"")
        String SUFFIX = suffixText.getText().trim();
        // 过滤文件扩展名(可选,修改需要加点,示例如下,不需要修改请设置为null)
        String EXTENSION = extensionText.getText().trim();
        // 要批量重命名的文件所在文件夹路径
        String FILEPATH = filePathText.getText().trim();
        //****************************自定义参数****************************

        renameNum = 0;
        rename(new File(FILEPATH), TYPE, PREFIX, SUFFIX, EXTENSION, jTextArea);
        jTextArea.append("重命名文件 " + renameNum + "个,已全部完成\n");
        jTextArea.setCaretPosition(jTextArea.getText().length());
    }

    public static void rename(File file, FileRenameType TYPE, String PREFIX, String SUFFIX, String EXTENSION, JTextArea jTextArea) {
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            assert fs != null;
            count = 1;
            for (File f : fs) {
                // 过滤隐藏文件,先处理所有的子文件
                if (!f.isHidden() && !f.isDirectory()) {
                    // 如果开启扩展名过滤则进行过滤
                    if (!"".equals(EXTENSION) && !f.getName().endsWith(EXTENSION)) {
                        continue;
                    }
                    rename(f, TYPE, PREFIX, SUFFIX, EXTENSION, jTextArea);
                }
            }
            for (File f : fs) {
                // 过滤隐藏文件,处理所有的子文件夹
                if (!f.isHidden() && f.isDirectory()) {
                    rename(f, TYPE, PREFIX, SUFFIX, EXTENSION, jTextArea);
                }
            }
        } else {
            String[] name = file.getName().split("\\.");
            String extension = "." + name[name.length - 1].toLowerCase();
            if (!"".equals(EXTENSION)) {
                extension = "." + EXTENSION;
            }
            jTextArea.append("要重命名的文件是：" + file.getAbsolutePath() + "\n");
            jTextArea.setCaretPosition(jTextArea.getText().length());
            // filename 表示重命名后的名称
            String fileName;
            if (TYPE == FileRenameType.UUID) {
                fileName = file.getParent() + "/" + PREFIX + UUID.randomUUID().toString().replace("-", "") + SUFFIX + extension;
            } else {
                fileName = file.getParent() + "/" + PREFIX + count + SUFFIX + extension;
            }
            file.renameTo(new File(fileName));
            renameNum++;
            count++;
        }
    }
}
