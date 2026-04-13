package com.entity.vo;

import com.entity.PetOrderEntity;
import java.io.Serializable;
import java.util.Date;

/**
 * 宠物订单VO
 *
 * @author 
 * @email
 */
public class PetOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 订单号
     */
    private String orderUuid;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 宠物ID
     */
    private Integer petId;

    /**
     * 宠物名称
     */
    private String petName;

    /**
     * 收货地址ID
     */
    private Integer addressId;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 订单价格
     */
    private Double price;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 支付类型
     */
    private Integer paymentType;

    /**
     * 支付类型文本
     */
    private String paymentTypeText;

    /**
     * 订单状态：1=待付款，2=待发货，3=已发货，4=已完成，5=已取消
     */
    private Integer orderStatus;

    /**
     * 订单状态文本
     */
    private String orderStatusText;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public PetOrderVO() {
    }

    public PetOrderVO(PetOrderEntity entity) {
        this.id = entity.getId();
        this.orderUuid = entity.getOrderUuid();
        this.userId = entity.getUserId();
        this.petId = entity.getPetId();
        this.addressId = entity.getAddressId();
        this.price = entity.getPrice();
        this.orderType = entity.getOrderType();
        this.paymentType = entity.getPaymentType();
        this.orderStatus = entity.getOrderStatus();
        this.createTime = entity.getCreateTime();
        this.updateTime = entity.getUpdateTime();
        
        // 设置订单状态文本
        switch (orderStatus) {
            case 1:
                this.orderStatusText = "待付款";
                break;
            case 2:
                this.orderStatusText = "待发货";
                break;
            case 3:
                this.orderStatusText = "已发货";
                break;
            case 4:
                this.orderStatusText = "已完成";
                break;
            case 5:
                this.orderStatusText = "已取消";
                break;
            default:
                this.orderStatusText = "未知";
                break;
        }
        
        // 设置支付类型文本
        if (paymentType != null) {
            switch (paymentType) {
                case 1:
                    this.paymentTypeText = "微信支付";
                    break;
                case 2:
                    this.paymentTypeText = "支付宝";
                    break;
                default:
                    this.paymentTypeText = "其他";
                    break;
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
        // 更新支付类型文本
        if (paymentType != null) {
            switch (paymentType) {
                case 1:
                    this.paymentTypeText = "微信支付";
                    break;
                case 2:
                    this.paymentTypeText = "支付宝";
                    break;
                default:
                    this.paymentTypeText = "其他";
                    break;
            }
        }
    }

    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    public void setPaymentTypeText(String paymentTypeText) {
        this.paymentTypeText = paymentTypeText;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
        // 更新订单状态文本
        switch (orderStatus) {
            case 1:
                this.orderStatusText = "待付款";
                break;
            case 2:
                this.orderStatusText = "待发货";
                break;
            case 3:
                this.orderStatusText = "已发货";
                break;
            case 4:
                this.orderStatusText = "已完成";
                break;
            case 5:
                this.orderStatusText = "已取消";
                break;
            default:
                this.orderStatusText = "未知";
                break;
        }
    }

    public String getOrderStatusText() {
        return orderStatusText;
    }

    public void setOrderStatusText(String orderStatusText) {
        this.orderStatusText = orderStatusText;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}