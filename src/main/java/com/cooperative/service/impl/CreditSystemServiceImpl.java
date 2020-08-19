package com.cooperative.service.impl;

import com.cooperative.service.CreditSystemService;
import org.springframework.stereotype.Service;

/**
 * @Author: zhouliansheng
 * @Date: 2020/8/19 23:18
 */
@Service
public class CreditSystemServiceImpl implements CreditSystemService {
    @Override
    public int getUserCredit(int userId) {
        throw new UnsupportedOperationException("积分系统未完成，不能调用");
    }

    @Override
    public boolean addCedit(int userId, int score) {
        throw new UnsupportedOperationException("积分系统未完成，不能调用");
    }
}
