package com.hetao.utils;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

/**
 * @author hetao
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {
        OPCPackage opcPackage = POIXMLDocument.openPackage("/Users/hetao/Downloads/Inbox/correspond8.docx");
        String text1 = new XWPFWordExtractor(opcPackage).getText();
        System.out.println(text1);
    }
}
