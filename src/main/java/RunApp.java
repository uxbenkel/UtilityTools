import javax.swing.*;
import java.awt.*;

/**
 * @author hetao
 * @date 2019-06-07
 * 实用小工具
 */
public class RunApp {

    static String[] fileRenameType = {"顺序", "乱序"};
    static JComboBox<String> typeSelector = new JComboBox<>(fileRenameType);
    static JTextField prefixText = new JTextField(20);
    static JTextField suffixText = new JTextField(20);
    static JTextField extensionText = new JTextField(20);
    static JTextField filePathText = new JTextField(20);
    static JTextArea jTextArea = new JTextArea();

    public static void main(String[] args) {
        // 设置窗体信息
        JFrame frame = new JFrame("实用小工具");
        frame.setBounds(200, 200, 800, 500);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建控件
        JLabel typeLabel = new JLabel("类型/type：");
        JLabel typeTipLabel = new JLabel("---请输入要采取的命名规则，顺序或乱序");
        JLabel prefixLabel = new JLabel("前缀/prefix：");
        JLabel prefixTipLabel = new JLabel("---请输入要指定的前缀，不指定请留空");
        JLabel suffixLabel = new JLabel("后缀/suffix：");
        JLabel suffixTipLabel = new JLabel("---请输入要指定的后缀，不指定请留空");
        JLabel extensionLabel = new JLabel("扩展/extension：");
        JLabel extensionTipLabel = new JLabel("---请输入要过滤的扩展名，不过滤请留空");
        JLabel filePathLabel = new JLabel("路径/filePath：");
        JLabel filePathTipLabel = new JLabel("---请输入要修改/清理的文件夹路径");
        JButton renameButton = new JButton("批量改名");
        JButton cleanButton = new JButton("清理仓库");
        JScrollPane jscrollPane = new JScrollPane();

        // 控件布局
        int hight = 24;
        int base = 15;
        int gap = 30;
        int position_1 = 20;
        int position_2 = 150;
        int position_3 = 250;
        int width_1 = 140;
        int width_2 = 80;
        int width_3 = 280;
        typeLabel.setBounds(position_1, base, width_1, hight);
        typeSelector.setBounds(position_2, base, width_2, hight);
        typeTipLabel.setBounds(position_3, base, width_3, hight);
        prefixLabel.setBounds(position_1, base + gap, width_1, hight);
        prefixText.setBounds(position_2, base + gap, width_2, hight);
        prefixTipLabel.setBounds(position_3, base + gap, width_3, hight);
        suffixLabel.setBounds(position_1, base + gap * 2, width_1, hight);
        suffixText.setBounds(position_2, base + gap * 2, width_2, hight);
        suffixTipLabel.setBounds(position_3, base + gap * 2, width_3, hight);
        extensionLabel.setBounds(position_1, base + gap * 3, width_1, hight);
        extensionText.setBounds(position_2, base + gap * 3, width_2, hight);
        extensionTipLabel.setBounds(position_3, base + gap * 3, width_3, hight);
        filePathLabel.setBounds(position_1, base + gap * 4, width_1, hight);
        filePathText.setBounds(position_2, base + gap * 4, width_2, hight);
        filePathTipLabel.setBounds(position_3, base + gap * 4, width_3, hight);
        jTextArea.setBounds(20, 175, 760, 285);
        jscrollPane.setBounds(20, 175, 760, 285);
        renameButton.setBounds(620, 50, 80, 30);
        cleanButton.setBounds(620, 90, 80, 30);
        jscrollPane.setViewportView(jTextArea);
        jscrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jscrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        Font font = new Font("Microsoft Yahei", Font.PLAIN, 14);
        typeLabel.setFont(font);
        typeSelector.setFont(font);
        typeTipLabel.setFont(font);
        prefixLabel.setFont(font);
        prefixText.setFont(font);
        prefixTipLabel.setFont(font);
        suffixLabel.setFont(font);
        suffixText.setFont(font);
        suffixTipLabel.setFont(font);
        extensionLabel.setFont(font);
        extensionText.setFont(font);
        extensionTipLabel.setFont(font);
        filePathLabel.setFont(font);
        filePathText.setFont(font);
        filePathTipLabel.setFont(font);
        jTextArea.setFont(font);
        jscrollPane.setFont(font);
        renameButton.setFont(font);
        cleanButton.setFont(font);
        jscrollPane.setFont(font);

        // 添加控件到窗体
        frame.add(typeLabel);
        frame.add(typeSelector);
        frame.add(typeTipLabel);
        frame.add(prefixLabel);
        frame.add(prefixText);
        frame.add(prefixTipLabel);
        frame.add(suffixLabel);
        frame.add(suffixText);
        frame.add(suffixTipLabel);
        frame.add(extensionLabel);
        frame.add(extensionText);
        frame.add(extensionTipLabel);
        frame.add(filePathLabel);
        frame.add(filePathText);
        frame.add(filePathTipLabel);
        frame.add(jscrollPane);
        frame.add(renameButton);
        frame.add(cleanButton);
        frame.setVisible(true);

        jTextArea.append("实用小工具 v0.1\n");
        jTextArea.append("请输入信息后点击按钮进行操作...\n");
        // 执行程序线程控制
        renameButton.addActionListener(e -> {
            try {
                int result = JOptionPane.showConfirmDialog(frame, "确认操作吗？该操作无法撤销!", "操作确认", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    jTextArea.append(">>>开始执行批量改名...\n");
                    FileBatchRename.renameMain(fileRenameType, typeSelector, prefixText, suffixText, extensionText, filePathText, jTextArea);
                }
            } catch (Exception ex) {
                jTextArea.append("执行失败!请联系作者\n");
            }
        });
        cleanButton.addActionListener(e -> {
            try {
                int result = JOptionPane.showConfirmDialog(frame, "确认操作吗？该操作无法撤销!", "操作确认", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    jTextArea.append(">>>开始清理仓库...\n");
                    RepositoryClean.cleanMain(filePathText, jTextArea);
                }
            } catch (Exception ex) {
                jTextArea.append("清理失败!请联系作者\n");
            }
        });
    }
}