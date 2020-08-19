package com.cooperative.service;

/**
 * @Author: zhouliansheng
 * @Date: 2020/8/19 23:17
 */
public interface CreditSystemService {

    public int getUserCredit(int userId);

    public boolean addCedit(int userId, int score);
}
