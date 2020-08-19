package com.test.ch9.mock;

import com.cooperative.service.CreditSystemService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

/**
 * @Author: zhouliansheng
 * @Date: 2020/8/19 23:58
 */
@RunWith(MockitoJUnitRunner.class)
public class CreditServiceMockTest {

    @Test
    public void test() throws IOException {


        int userId = 10;
        // 创建mock对象
        CreditSystemService creditService = Mockito.mock(CreditSystemService.class);
        // 模拟mock对象调用
        Mockito.when(creditService.getUserCredit(ArgumentMatchers.eq(userId))).thenReturn(1000);
        // 实际调用
        int ret = creditService.getUserCredit(userId);
        //注释如下行，单元测试会失败
        creditService.getUserCredit(userId);
        // 比较期望值和返回值
        Assert.assertEquals(1000, ret);
        Mockito.verify(creditService, Mockito.times(2))
                .getUserCredit(ArgumentMatchers.eq(userId));

    }

    @Test
    public void test2() {
        int userId = 10;
        // 创建mock对象
        CreditSystemService creditService = Mockito.mock(CreditSystemService.class);
        // 模拟mock对象调用
        Mockito.when(creditService.getUserCredit(ArgumentMatchers.eq(userId))).thenReturn(1000);
        Mockito.when(creditService.getUserCredit(AdditionalMatchers.lt(0)))
                .thenThrow(new IllegalArgumentException("userId不能小于0"));

        try {
            // 实际调用
            int ret = creditService.getUserCredit(10);
            Assert.assertEquals(1000, ret);
            ret = creditService.getUserCredit(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            //应该抛出异常
        }


    }

    @Test
    public void test3() {

        // 创建mock对象
        List list = Mockito.mock(List.class);
        Mockito.doThrow(new UnsupportedOperationException("不支持clear方法调用")).when(list).clear();
        try {
            list.clear();

        } catch (UnsupportedOperationException x) {
            return;
        }
        Assert.fail();


    }
}
