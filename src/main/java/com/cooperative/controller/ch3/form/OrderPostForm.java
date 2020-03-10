package com.cooperative.controller.ch3.form;

import com.cooperative.entity.ch3.Order;
import com.cooperative.entity.ch3.OrderDetail;
import lombok.Data;

import java.util.List;

@Data
public class OrderPostForm {
    public Order order;
    public List<OrderDetail> details;

}
