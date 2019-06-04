package com.hetao.tools;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GetAuthorInfo {
    public static void main(String[] args) throws Exception {
        // 定义要抓取的链接
        String baseURL = "https://www.osapublishing.org"; // 固定网站
        String journal = "oe"; // 杂志
        String volume = "27"; // 卷
        String issue = "10"; // 期
        String URL = baseURL + "/" + journal + "/issue.cfm?volume=" + volume + "&issue=" + issue;

        // 定义存储的文件路径
        String path = "/Users/hetao/Downloads/Inbox/";
        String fileName = journal.toUpperCase() + " " + "vol." + volume + " " + "issue" + issue + ".xlsx";
        String txtName = journal.toUpperCase() + " " + "vol." + volume + " " + "issue" + issue + ".txt";
        FileOutputStream fos = new FileOutputStream(path + fileName);
        FileOutputStream tfos = new FileOutputStream(path + txtName, true);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 创建Excel对象，等待写入数据
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(fileName.replace(".xlsx", ""));
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        // 设置Excel格式
        font.setFontName("Calibri");
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
        System.out.println(">>>开始写入文件:");
        System.out.println(">>>写入中......");

        // 获取页面对象
        HttpResponse response = getResponse(URL);
        Document doc = getDocument(response);
        Elements contents = doc.select(".article-title a");

        // 循环获取页面并写入文件
        for (Element content : contents) {
            String href = content.attr("href");
            String articleURL = baseURL + href;
            System.out.println(">>>开始抓取>>>" + articleURL.substring(50) + "......");
            HttpResponse articleResponse = getResponse(articleURL);
            Document articleDoc = getDocument(articleResponse);
            String article = articleDoc.select("#authorAffiliations").get(0).toString();
            if (article.contains("ORCID")) {
                article = article.substring(0, article.indexOf("ORCID"));
            }
            String authors = parseHTML(article);
            tfos.write(authors.getBytes());
            tfos.flush();
            rowIndex = writeToExcel(authors, sheet, cellStyle, rowIndex);
        }

        workbook.write(fos);
        fos.flush();
        fos.close();
        tfos.close();
        System.out.println(">>>写入文件成功!");
    }

    private static int writeToExcel(String article, XSSFSheet sheet, XSSFCellStyle cellStyle, int rowIndex) {
        if (article.contains("Corresponding author")) {
            Row row = sheet.createRow(rowIndex);
            Cell cellC0 = row.createCell(0);
            cellC0.setCellValue(rowIndex);
            cellC0.setCellStyle(cellStyle);
            if (!article.contains("*Corresponding author")) {
                String[] lines = article.split("Corresponding author: ");
                // 1、以下是邮箱部分(仅有1个)
                String keyMail = lines[1].replace("\n", "");
                Cell cellC2 = row.createCell(2);
                cellC2.setCellValue(keyMail.trim());
                cellC2.setCellStyle(cellStyle);
                Cell cellC1 = row.createCell(1);
                cellC1.setCellValue("需手动查找author信息！");
                cellC1.setCellStyle(cellStyle);
                Cell cellC3 = row.createCell(3);
                cellC3.setCellValue("需手动查找author信息！");
                cellC3.setCellStyle(cellStyle);
                rowIndex++;
                return rowIndex;
            }
            // 一、包含*Corresponding author的情况
            String[] lines = article.split("\\*Corresponding author: ");
            // 1、以下是邮箱部分(仅有1个)
            String keyMail = lines[1].replace("\n", "");
            Cell cellC2 = row.createCell(2);
            cellC2.setCellValue(keyMail.trim());
            cellC2.setCellStyle(cellStyle);
            // 2、以下是作者部分(仅有1个)
            String authorsAndAddress = lines[0].replace("\n Author Affiliations \n", "");
            int startIndex = authorsAndAddress.indexOf("\n");
            String keyAuthor = " ";
            HashSet<String> keyAddressNums = new HashSet<>();
            String authors_ = authorsAndAddress.substring(0, startIndex).split("\\*")[0];
            if (authors_.contains("and")) {
                String[] authors = authors_.split("and ");
                int length = authors.length;
                String lastAuthor = authors[length - 1];
                if (lastAuthor.split(",").length == 1) {
                    keyAuthor = lastAuthor.replace(",", "");
                    if (keyAuthor.replace(" ", "").matches("\\S*\\d")) {
                        keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1) + "");
                    }
                } else {
                    authors = authors_.split(",");
                    length = authors.length;
                    int i = 1;
                    while (keyAuthor.matches(" |\\d")) {
                        if (keyAuthor.matches("\\d")) {
                            keyAddressNums.add(keyAuthor);
                        }
                        keyAuthor = "".equals(authors[length - i]) ? authors[length - ++i] : authors[length - i];
                        if (keyAuthor.replace(" ", "").matches("\\S*\\d")) {
                            keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1));
                        }
                        i++;
                    }
                }
            } else {
                String[] authors = authors_.split(",");
                int i = 1;
                int length = authors.length;
                while (keyAuthor.matches(" |\\d")) {
                    if (keyAuthor.matches("\\d")) {
                        keyAddressNums.add(keyAuthor);
                    }
                    keyAuthor = "".equals(authors[length - i]) ? authors[length - ++i] : authors[length - i];
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
            if (!addresss.startsWith("\n  1")) {
                keyAddress = addresss.replace("\n", "");
                Cell cellC3 = row.createCell(3);
                cellC3.setCellValue(keyAddress.trim());
                cellC3.setCellStyle(cellStyle);
            } else {
                String[] keyAddresses = addresss.replace("\n  1", "").split("\n\\s*\\d");
                for (String keyAddressNum : keyAddressNums) {
                    if ("".equals(keyAddressNum.replace(" ", ""))) {
                        continue;
                    }
                    keyAddress = keyAddresses[Integer.parseInt(keyAddressNum) - 1].trim();
                    if ("These authors_ contributed equally to this work".equals(keyAddress)) {
                        continue;
                    }
                    if (!"".contentEquals(printAddress)) {
                        printAddress.append("\n");
                    }
                    printAddress.append(keyAddress);
                }
                Cell cellC3 = row.createCell(3);
                cellC3.setCellValue(printAddress.toString().replaceAll("http://\\S*|https://\\S*","").trim());
                cellC3.setCellStyle(cellStyle);
            }
            rowIndex++;
        } else {
            // 二、不含*Corresponding author的情况
            article =  article.replace("\n Author Affiliations \n", "");
            int startIndex = article.indexOf("\n");
            StringBuilder printAddress = new StringBuilder();
            // 1、首先找到邮箱及邮箱编号(必有多个)
            String mailsAndAddresses = article.substring(startIndex);
            String[] splits = mailsAndAddresses.replace("\n  1", "").split("\n\\s*\\d+");
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
            String authors = article.substring(0, startIndex);
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
                        cellC2.setCellValue(mailMap.get(mailNum).replace("\n", "").trim());
                        cellC2.setCellStyle(cellStyle);
                        Cell cellC1 = row.createCell(1);
                        cellC1.setCellValue(author.replaceAll("\\d|,", "").trim());
                        cellC1.setCellStyle(cellStyle);
                        String authorNumber = author.replaceAll("[^0-9,]", "").replace(mailNum + "", "");
                        String[] authorNums = authorNumber.replaceFirst(",", "").split(",");
                        for (String authorNum : authorNums) {
                            for (Integer addressNum : addressNums) {
                                if ("".equals(authorNum.replace(" ", ""))) {
                                    continue;
                                }
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
                        cellC3.setCellValue(printAddress.toString().trim());
                        cellC3.setCellStyle(cellStyle);
                    }
                }
                rowIndex++;
            }
        }
        return rowIndex;
    }

    // 发起请求获取页面response
    private static HttpResponse getResponse(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        return httpClient.execute(httpGet);
    }

    // 根据response获取document对象
    private static Document getDocument(HttpResponse response) throws Exception {
        return Jsoup.parse(EntityUtils.toString(response.getEntity(), Charset.forName("utf-8")));
    }

    // jsoup解析HTML保留换行
    public static String parseHTML(String html) {
        Document doc = Jsoup.parse(html);
        doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
        // doc.select("br").append("\\n");
        // doc.select("p").prepend("\\n");
        String str = doc.html();
        return Jsoup.clean(str, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
}
