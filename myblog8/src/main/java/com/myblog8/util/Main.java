package com.myblog8.util;


import org.springframework.data.domain.Sort;

public class Main {
    public static void main(String[] args) {
        System.out.println(Sort.Direction.DESC.name());

    }

    public int test2(){
        Main m1=new Main();
       return m1.test3();
    }

    public int test3(){
     return 300;
    }
}
