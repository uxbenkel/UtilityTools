package com.hetao.tools;

import java.util.ArrayList;

/**
 * @author hetao
 * @date 2019-04-27
 */
public class CorrespondAuthor {
    public static void main(String[] args) {
        String text = " Huibin Chang,1,2,10 Pablo Enfedaque,2 Jie Zhang,1 Juliane Reinhardt,3,4 Bjoern Enders,5,6 Young-Sang Yu,5 David Shapiro,5 Christian G. Schroer,7,8 Tieyong Zeng,9 and Stefano Marchesini2,11 \n" +
                "1School of Mathematical Sciences, Tianjin Normal University, Tianjin, China\n" +
                "2Computational Research Division, Lawrence Berkeley National Laboratory, Berkeley, CA, \n" +
                "USA\n" +
                "3ARC Centre of Excellence for Advanced Molecular Imaging, Department of Chemistry and Physics, La Trobe Institute for Molecular Sciences, La Trobe University, Bundoora, Australia\n" +
                "4The Australian Synchrotron, Clayton, Australia\n" +
                "5Advanced Light Source, Lawrence Berkeley National Laboratory, Berkeley, CA, USA\n" +
                "6Physics Department, University of California at Berkeley, Berkeley, CA, USA\n" +
                "7Deutsches Elektronen-Synchrotron DESY, D-22607 Hamburg, Germany\n" +
                "8Department Physik, Universität Hamburg, Luruper Chaussee 149, 22761 Hamburg, Germany\n" +
                "9Department of Mathematics, The Chinese University of Hong Kong, Hong Kong\n" +
                "10changhuibin@gmail.com\n" +
                "11smarchesini@lbl.gov\n";
        String[] author_affiliations = text.split("Author Affiliations\n");
        // 一共有多少篇文章
        System.out.println(author_affiliations.length);
        int count = 1;

        // 循环查找每一篇文章的作者地址和邮箱
        for (String author_affiliation : author_affiliations) {
            System.out.println("第" + count + "个：");
            if (author_affiliation.contains("*Corresponding author")) {
                // 一、包含*Corresponding author的情况
                String[] lines = author_affiliation.split("\\*Corresponding author: ");
                String authorsAndAddress = lines[0];
                int startIndex = authorsAndAddress.indexOf("\n");
                // 1、以下是作者部分(仅有1个)
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
                            keyAddressNums.add(keyAuthor);
                            keyAuthor = correspondAuthor[correspondAuthor.length - i].replaceAll("\\d", "");
                            i++;
                        }
                    }
                } else {
                    String[] correspondAuthor = correspondAuthors.split(",");
                    int i = 1;
                    while (keyAuthor.matches(" |\\d")) {
                        if (keyAuthor.matches("\\d")) {
                            keyAddressNums.add(keyAuthor);
                        }
                        keyAuthor = correspondAuthor[correspondAuthor.length - i].replaceAll("\\d", "");
                        if (keyAuthor.replace(" ", "").matches("\\S+\\d")) {
                            keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1));
                        }
                        i++;
                    }
                }
                System.out.println(keyAuthor);

                // 2、以下是邮箱部分(仅有1个)
                String keyMail = lines[1].replace("\n", "");
                System.out.println(keyMail);

                // 3、以下是通讯地址部分(可能多个)
                String addresss = authorsAndAddress.substring(startIndex);
                String keyAddress;
                if (!addresss.startsWith("\n1")) {
                    keyAddress = addresss.replace("\n", "");
                    System.out.println(keyAddress);
                } else {
                    String[] keyAddresses = addresss.replace("\n1", "").split("\n\\d");
                    assert false;
                    for (String keyAddressNum : keyAddressNums) {
                        keyAddress = keyAddresses[Integer.parseInt(keyAddressNum) - 1].replace("\n", "");
                        System.out.println(keyAddress);
                    }
                }

            } else {
                // 二、不含*Corresponding author的情况
                int startIndex = author_affiliation.indexOf("\n");
                // 1、以下是作者部分(必有多个)
                String authors = author_affiliation.substring(0, startIndex);
                String keyAuthor = " ";
                // ArrayList<String> keyAddressNums = new ArrayList<>();
                ArrayList<String> authorArray = new ArrayList<>();
                String[] split1 = authors.split("and ");
                for (String s : split1) {
                }
                // 2、以下是邮箱和通讯地址部分(必有多个)
                String mailsAndAddresses = author_affiliation.substring(startIndex);
                String[] splits = mailsAndAddresses.replace("\n1", "").split("\n\\d");
                String keyMail;
                String keyAddress;
                for (String split : splits) {
                    if (split.contains("@")){
                        // 是邮箱
                    }else{
                        // 是地址
                    }
                }
            }
            count++;
        }

    }
}
