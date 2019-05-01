package com.hetao.tools;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hetao
 * @date 2019-04-27
 */
public class CorrespondAuthor {
    public static void main(String[] args) throws Exception {

        // 读取word内容，提取文本
        String inputPath = "/Users/hetao/Downloads/Inbox/correspond8.docx";
        FileInputStream is = new FileInputStream(inputPath);
        String outputPath = inputPath.substring(0, inputPath.lastIndexOf("/") + 1) + "result.xlsx";
        FileOutputStream fos = new FileOutputStream(outputPath);
        String text = new XWPFWordExtractor(new XWPFDocument(is)).getText().replaceAll("\n\n+", "\n").replaceAll(",\\s*\n", ",").trim();
        // 创建Excel对象，等待写入数据
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(inputPath.substring(inputPath.lastIndexOf("/") + 1, inputPath.lastIndexOf(".")));
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        // 设置Excel格式
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);
        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setFont(font);
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setWrapText(true);
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 40 * 256);
        sheet.setColumnWidth(2, 40 * 256);
        sheet.setColumnWidth(3, 255 * 256);
        sheet.setZoom(3, 2);
        System.out.println("开始写入文件......");
        System.out.println("写入中......");

        // 写入表头数据
        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex);
        Cell cellT0 = titleRow.createCell(0);
        cellT0.setCellValue("No.");
        cellT0.setCellStyle(titleStyle);
        Cell cellT1 = titleRow.createCell(1);
        cellT1.setCellValue("corresponding author");
        cellT1.setCellStyle(titleStyle);
        Cell cellT2 = titleRow.createCell(2);
        cellT2.setCellValue("email");
        cellT2.setCellStyle(titleStyle);
        Cell cellT3 = titleRow.createCell(3);
        cellT3.setCellValue("affiliation");
        cellT3.setCellStyle(titleStyle);
        rowIndex++;
        String[] author_affiliations = text.split("Author Affiliations\n");

        // 循环查找每一篇文章的作者地址和邮箱
        for (String author_affiliation : author_affiliations) {
            if ("".equals(author_affiliation)) {
                continue;
            }
            // System.out.println("第" + articleCount + "篇：");
            if (author_affiliation.contains("*Corresponding author")) {
                Row row = sheet.createRow(rowIndex);
                Cell cellC0 = row.createCell(0);
                cellC0.setCellValue(rowIndex);
                cellC0.setCellStyle(cellStyle);
                // 一、包含*Corresponding author的情况
                String[] lines = author_affiliation.split("\\*Corresponding author: ");
                // 1、以下是邮箱部分(仅有1个)
                String keyMail = lines[1].replace("\n", "");
                Cell cellC2 = row.createCell(2);
                cellC2.setCellValue(keyMail);
                cellC2.setCellStyle(cellStyle);
                // 2、以下是作者部分(仅有1个)
                String authorsAndAddress = lines[0];
                int startIndex = authorsAndAddress.indexOf("\n");
                String authors = authorsAndAddress.substring(0, startIndex);
                String keyAuthor = " ";
                HashSet<String> keyAddressNums = new HashSet<>();
                String[] authorArray = authors.split("\\*");
                String correspondAuthors = authorArray[0];
                if (correspondAuthors.contains("and")) {
                    String[] correspondAuthor = correspondAuthors.split("and ");
                    int length = correspondAuthor.length;
                    String lastCorrespondAuthor = correspondAuthor[length - 1];
                    if (lastCorrespondAuthor.split(",").length == 1) {
                        keyAuthor = lastCorrespondAuthor.replace(",", "");
                        if (keyAuthor.replace(" ", "").matches("\\S*\\d")) {
                            keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1) + "");
                        }
                    } else {
                        correspondAuthor = correspondAuthors.split(",");
                        length = correspondAuthor.length;
                        int i = 1;
                        while (keyAuthor.matches(" |\\d")) {
                            if (keyAuthor.matches("\\d")) {
                                keyAddressNums.add(keyAuthor);
                            }
                            keyAuthor = "".equals(correspondAuthor[length - i]) ? correspondAuthor[length - ++i] : correspondAuthor[length - i];
                            if (keyAuthor.replace(" ", "").matches("\\S*\\d")) {
                                keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1));
                            }
                            i++;
                        }
                    }
                } else {
                    String[] correspondAuthor = correspondAuthors.split(",");
                    int i = 1;
                    int length = correspondAuthor.length;
                    while (keyAuthor.matches(" |\\d")) {
                        if (keyAuthor.matches("\\d")) {
                            keyAddressNums.add(keyAuthor);
                        }
                        keyAuthor = "".equals(correspondAuthor[length - i]) ? correspondAuthor[length - ++i] : correspondAuthor[length - i];
                        if (keyAuthor.replace(" ", "").matches("\\S*\\d")) {
                            keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1));
                        }
                        i++;
                    }
                }
                Cell cellC1 = row.createCell(1);
                cellC1.setCellValue(keyAuthor.replace("and ", "").replaceAll("\\d", "").trim());
                cellC1.setCellStyle(cellStyle);

                // 3、以下是通讯地址部分(可能多个)
                String addresss = authorsAndAddress.substring(startIndex);
                String keyAddress;
                StringBuilder printAddress = new StringBuilder();
                if (!addresss.startsWith("\n1")) {
                    keyAddress = addresss.replace("\n", "");
                    Cell cellC3 = row.createCell(3);
                    cellC3.setCellValue(keyAddress);
                    cellC3.setCellStyle(cellStyle);
                } else {
                    String[] keyAddresses = addresss.replace("\n1", "").split("\n\\d");
                    for (String keyAddressNum : keyAddressNums) {
                        keyAddress = keyAddresses[Integer.parseInt(keyAddressNum) - 1].trim();
                        if ("These authors contributed equally to this work".equals(keyAddress)) {
                            continue;
                        }
                        if (!"".contentEquals(printAddress)) {
                            printAddress.append("\n");
                        }
                        printAddress.append(keyAddress);
                    }
                    Cell cellC3 = row.createCell(3);
                    cellC3.setCellValue(printAddress.toString());
                    cellC3.setCellStyle(cellStyle);
                }
                rowIndex++;
            } else {
                // 二、不含*Corresponding author的情况
                int startIndex = author_affiliation.indexOf("\n");
                StringBuilder printAddress = new StringBuilder();
                // 1、首先找到邮箱及邮箱编号(必有多个)
                String mailsAndAddresses = author_affiliation.substring(startIndex);
                String[] splits = mailsAndAddresses.replaceFirst("\n1", "").split("\n\\d+");
                HashMap<Integer, String> mailMap = new HashMap<>();
                HashMap<Integer, String> addressMap = new HashMap<>();
                int count = 1;
                for (String split : splits) {
                    if (split.contains("@")) {
                        mailMap.put(count, split);
                    } else {
                        if ("These authors contributed equally to this work".equals(split)) {
                            count++;
                            continue;
                        }
                        addressMap.put(count, split);
                    }
                    count++;
                }

                // 2、然后通过邮箱编号找到作者(必有多个),最后通过作者找到地址编号(可能多个)
                String authors = author_affiliation.substring(0, startIndex);
                HashSet<String> authorArray = new HashSet<>();
                String[] split1 = authors.split("and ");
                for (String split11 : split1) {
                    String[] split2 = split11.split(",\\d+ ");
                    for (String split22 : split2) {
                        int index = split11.indexOf(split22);
                        if (split22.length() + index + 2 < split11.length()) {
                            authorArray.add(split22 + split11.substring(split22.length() + index, split22.length() + index + 3));
                        } else {
                            authorArray.add(split22);
                        }
                    }
                }
                Set<Integer> mailNums = mailMap.keySet();
                Set<Integer> addressNums = addressMap.keySet();
                for (Integer mailNum : mailNums) {
                    Row row = sheet.createRow(rowIndex);
                    for (String author : authorArray) {
                        if (author.contains(mailNum + "")) {
                            Cell cellC0 = row.createCell(0);
                            cellC0.setCellValue(rowIndex);
                            cellC0.setCellStyle(cellStyle);
                            Cell cellC2 = row.createCell(2);
                            cellC2.setCellValue(mailMap.get(mailNum).replace("\n", ""));
                            cellC2.setCellStyle(cellStyle);
                            Cell cellC1 = row.createCell(1);
                            cellC1.setCellValue(author.replaceAll("\\d|,", "").trim());
                            cellC1.setCellStyle(cellStyle);
                            String authorNumber = author.replaceAll("[^0-9,]", "").replace(mailNum + "", "");
                            String[] authorNums = authorNumber.replaceFirst(",", "").split(",");
                            for (String authorNum : authorNums) {
                                for (Integer addressNum : addressNums) {
                                    if (Integer.parseInt(authorNum + "") == addressNum) {
                                        if (!printAddress.toString().contains(addressMap.get(addressNum).trim())) {
                                            if (!"".equals(printAddress.toString())) {
                                                printAddress.append("\n");
                                            }
                                            printAddress.append(addressMap.get(addressNum).trim());
                                        }
                                    }
                                }
                            }
                            Cell cellC3 = row.createCell(3);
                            cellC3.setCellValue(printAddress.toString());
                            cellC3.setCellStyle(cellStyle);
                        }
                    }
                    rowIndex++;
                }
            }
        }

        workbook.write(fos);
        fos.flush();
        fos.close();
        System.out.println("写入文件成功!");
    }
}
