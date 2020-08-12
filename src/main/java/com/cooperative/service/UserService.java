package com.cooperative.service;

import com.cooperative.dao.UserDao;
import com.cooperative.dao.UserInfoDao;
import com.cooperative.entity.RechargeOrder;
import com.cooperative.entity.User;
import com.cooperative.entity.UserInfo;
import com.cooperative.unit.DataException;
import com.cooperative.unit.wxpay.FundMode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service(value = "wx_service")
@Transactional
public class UserService {

    @Autowired
    @Qualifier(value = "wx_userDao")
    UserDao userDao;

    @Autowired
    UserInfoDao userInfoDao;

    public User addOrUpdateUser(User user) {
        return userDao.save(user);
    }

    public User addOrUpdateUser(User user, UserInfo userInfo) {
        user = userDao.save(user);
        if (null != userInfo) {
            userInfoDao.save(userInfo);
        }
        return user;
    }

    public User getUserById(Long userId) {
        return userDao.getOne(userId);
    }

    public User findByOpenId(String openId) {
        return userDao.findByOpenId(openId);
    }

    /**
     * 资金操作
     *
     * @param user
     * @param rechargeOrder
     */
    public void userRecharge(User user, RechargeOrder rechargeOrder, FundMode mode) {
        if (user == null) {
            throw new DataException("用户不存在");
        }
        if (null == rechargeOrder || rechargeOrder.getMoney() == null
                || rechargeOrder.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DataException("操作资金金额错误");
        }
        BigDecimal remainMoney = user.getRemainMoney() == null ? BigDecimal.ZERO
                : user.getRemainMoney();// 充值前金额
        BigDecimal afterRemainMoney;
        if (mode.equals(FundMode.IN)) {
            afterRemainMoney = remainMoney.add(rechargeOrder.getMoney());// 充值后余额
        } else if (mode.equals(FundMode.OUT)) {
            if (remainMoney.compareTo(rechargeOrder.getRefundMoney()) < 0) {
                throw new DataException("余额不足，不能操作用户资金！");
            }
            afterRemainMoney = remainMoney.subtract(rechargeOrder.getRefundMoney());
        } else {
            throw new DataException("操作资金金额错误");
        }

        user.setRemainMoney(afterRemainMoney);
        userDao.save(user);

        /******记录资金明细操作省略*******/

    }

    public String exportBill(Map<String, String> billMap) {

//        Gson gson = new Gson();
//        String result = gson.toJson(billMap);
//        Gson gson2 = new Gson();
//        BillDto dto = gson2.fromJson(result, new TypeToken<BillDto>(){}.getType());

        //创建临时文件存放的路径
        String temp = "d:\\temp\\excel\\";
        //创建工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet();
        xssfWorkbook.setSheetName(0, "下单对账表");
        //创建表头
        XSSFRow head = sheet.createRow(0);
        String[] heads = {"编号", "姓名", "年龄", "性别", "手机号"};
        for (int i = 0; i < 5; i++) {
            XSSFCell cell = head.createCell(i);
            cell.setCellValue(heads[i]);
        }
        for (int i = 1; i <= 4; i++) {
            //创建行,从第二行开始，所以for循环的i从1开始取
            XSSFRow row = sheet.createRow(i);
            //创建单元格，并填充数据
            XSSFCell cell = row.createCell(0);
//            cell.setCellValue(student.getS_id());
//            cell = row.createCell(1);
//            cell.setCellValue(student.getS_name());
//            cell = row.createCell(2);
//            cell.setCellValue(student.getS_age());
//            cell = row.createCell(3);
//            cell.setCellValue("男".equals(student.getS_gender().trim())?"男":"女");
//            cell = row.createCell(4);
//            cell.setCellValue(student.getS_tel());
        }
        //创建临时文件的目录
        File file = new File(temp);
        if (!file.exists()) {
            file.mkdirs();
        }
        //临时文件路径/文件名
        String downloadPath = file + "\\" + System.currentTimeMillis() + UUID.randomUUID();
        OutputStream outputStream = null;
        try {
            //使用FileOutputStream将内存中的数据写到本地，生成临时文件
            outputStream = new FileOutputStream(downloadPath);
            xssfWorkbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return downloadPath;
    }
}
