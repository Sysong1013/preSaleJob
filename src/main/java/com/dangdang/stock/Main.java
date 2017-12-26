package com.dangdang.stock;

import com.dangdang.ddframe.container.spring.SpringContainer;

/**
 * Created by songyisong on 2017/11/21.
 */
public class Main {
    public static void main(String[] args) {
        new SpringContainer().startAsDeamon();
    }
}
