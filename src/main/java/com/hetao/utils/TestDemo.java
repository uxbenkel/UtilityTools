package com.hetao.utils;

/**
 * @author hetao
 */
public class TestDemo {
    public static void main(String[] args) {
        String s = "Yan Zhang1,41,a1,b";
        System.out.println(s.replaceAll("[^0-9,]", ""));
    }
}
