package com.imooc.o2o.heihei;

import com.imooc.o2o.BaseTest;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @Author w1586
 * @Date 2020/3/1 16:15
 * @Cersion 1.0
 */
public class a extends BaseTest {

    @Test
    public void test1(){
        BigDecimal a1 = new BigDecimal(0);
        BigDecimal a2 = new BigDecimal(1);
        BigDecimal result = null;
        for(int i=3; i<=100;i++){
            result = a1.add(a2);
            a1 = a2;
            a2 = result;
        }
        System.out.println(result);
    }
}
