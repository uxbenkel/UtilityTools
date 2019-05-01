package com.hetao.tools;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @author hetao
 * @date 2019-04-27
 */
public class CorrespondAuthor {
    public static void main(String[] args) throws Exception {
        // 读取word内容，提取文本
        String inputPath = "/Users/hetao/Downloads/Inbox/correspond8.docx";
        String outputPath = inputPath.substring(0, inputPath.lastIndexOf("/") + 1) + "result.xlsx";
        OPCPackage opcPackage = POIXMLDocument.openPackage(inputPath);
        String text = new XWPFWordExtractor(opcPackage).getText().replaceAll("\n\n+", "\n").trim();
        // 创建Excel对象，等待写入数据
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(inputPath.substring(inputPath.lastIndexOf("/") + 1, inputPath.lastIndexOf(".")));
        FileOutputStream fos = new FileOutputStream(outputPath);
        System.out.println("开始写入文件>>>>>>>>>>>>");
        // 写入表头数据
        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex);
        titleRow.createCell(0).setCellValue("No.");
        titleRow.createCell(1).setCellValue("corresponding author");
        titleRow.createCell(2).setCellValue("email");
        titleRow.createCell(3).setCellValue("affiliation");
        rowIndex++;
        String[] author_affiliations = text.split("Author Affiliations\n");
        // 一共有多少篇文章
        // System.out.println(author_affiliations.length);
        // int articleCount = 1;

        // 循环查找每一篇文章的作者地址和邮箱
        for (String author_affiliation : author_affiliations) {
            if ("".equals(author_affiliation)) {
                continue;
            }
            // System.out.println("第" + articleCount + "篇：");
            if (author_affiliation.contains("*Corresponding author")) {
                Row row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(rowIndex - 1);
                // 一、包含*Corresponding author的情况
                String[] lines = author_affiliation.split("\\*Corresponding author: ");
                // 1、以下是邮箱部分(仅有1个)
                String keyMail = lines[1].replace("\n", "");
                // System.out.println(keyMail);
                row.createCell(2).setCellValue(keyMail);
                // 2、以下是作者部分(仅有1个)
                String authorsAndAddress = lines[0];
                int startIndex = authorsAndAddress.indexOf("\n");
                String authors = authorsAndAddress.substring(0, startIndex);
                String keyAuthor = " ";
                ArrayList<String> keyAddressNums = new ArrayList<>();
                String[] authorArray = authors.split("\\*");
                String correspondAuthors = authorArray[0];
                if (correspondAuthors.contains("and")) {
                    String[] correspondAuthor = correspondAuthors.split("and ");
                    String lastCorrespondAuthor = correspondAuthor[correspondAuthor.length - 1];
                    if (lastCorrespondAuthor.split(",").length == 1) {
                        keyAuthor = lastCorrespondAuthor.replace(",", "");
                        if (keyAuthor.replace(" ", "").matches("\\S*\\d")) {
                            keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1) + "");
                        }
                    } else {
                        correspondAuthor = correspondAuthors.split(",");
                        int i = 1;
                        while (keyAuthor.matches(" |\\d")) {
                            if (keyAuthor.matches("\\d")) {
                                if (!keyAddressNums.contains(keyAuthor)) {
                                    keyAddressNums.add(keyAuthor);
                                }
                            }
                            keyAuthor = "".equals(correspondAuthor[correspondAuthor.length - i]) ? correspondAuthor[correspondAuthor.length - ++i] : correspondAuthor[correspondAuthor.length - i];
                            if (keyAuthor.replace(" ", "").matches("\\S*\\d")) {
                                keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1));
                            }
                            i++;
                        }
                    }
                } else {
                    String[] correspondAuthor = correspondAuthors.split(",");
                    int i = 1;
                    while (keyAuthor.matches(" |\\d")) {
                        if (keyAuthor.matches("\\d")) {
                            if (!keyAddressNums.contains(keyAuthor)) {
                                keyAddressNums.add(keyAuthor);
                            }
                        }
                        keyAuthor = "".equals(correspondAuthor[correspondAuthor.length - i]) ? correspondAuthor[correspondAuthor.length - ++i] : correspondAuthor[correspondAuthor.length - i];
                        if (keyAuthor.replace(" ", "").matches("\\S*\\d")) {
                            keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1));
                        }
                        i++;
                    }
                }
                // System.out.println(keyAuthor.replaceAll("\\d", "").trim());
                row.createCell(1).setCellValue(keyAuthor.replaceAll("\\d", "").trim());

                // 3、以下是通讯地址部分(可能多个)
                String addresss = authorsAndAddress.substring(startIndex);
                String keyAddress = null;
                if (!addresss.startsWith("\n1")) {
                    keyAddress = addresss.replace("\n", "");
                    // System.out.println(keyAddress);
                    row.createCell(3).setCellValue(keyAddress);
                } else {
                    String[] keyAddresses = addresss.replace("\n1", "").split("\n\\d");
                    assert false;
                    for (String keyAddressNum : keyAddressNums) {
                        keyAddress = keyAddresses[Integer.parseInt(keyAddressNum) - 1];
                        keyAddress += keyAddress;
                        // System.out.println(keyAddress);
                    }
                    row.createCell(3).setCellValue(keyAddress);
                }
                rowIndex++;
                // System.out.println();
            } else {
                // 二、不含*Corresponding author的情况
                int startIndex = author_affiliation.indexOf("\n");
                String keyAddress = null;
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
                        addressMap.put(count, split);
                    }
                    count++;
                }

                // 2、然后通过邮箱编号找到作者(必有多个),最后通过作者找到地址编号(可能多个)
                String authors = author_affiliation.substring(0, startIndex);
                // ArrayList<String> keyAddressNums = new ArrayList<>();
                ArrayList<String> authorArray = new ArrayList<>();
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
                            row.createCell(0).setCellValue(rowIndex - 1);
                            // System.out.println(mailMap.get(mailNum).replace("\n", ""));
                            row.createCell(2).setCellValue(mailMap.get(mailNum).replace("\n", ""));
                            // System.out.println(author.replaceAll("\\d|,", "").trim());
                            row.createCell(1).setCellValue(author.replaceAll("\\d|,", "").trim());
                            String nums = author.replaceAll("[^0-9,]", "").replace(mailNum + "", "");
                            String[] numArray = nums.replaceFirst(",", "").split(",");
                            for (String num : numArray) {
                                for (Integer addressNum : addressNums) {
                                    if (Integer.parseInt(num + "") == addressNum) {
                                        // System.out.println(addressMap.get(addressNum));
                                        keyAddress += addressMap.get(addressNum);
                                    }
                                }
                            }
                            row.createCell(3).setCellValue(keyAddress);
                        }
                    }
                    // System.out.println();
                    rowIndex++;
                }
            }
            // articleCount++;
        }
        System.out.println("主表数据写入完成>>>>>>>>");
        workbook.write(fos);
        fos.flush();
        fos.close();
        System.out.println(outputPath + "写入文件成功>>>>>>>>>>>");
    }
}
