package com.entity;

import com.annotation.ColumnInfo;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.*;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.utils.DateUtil;


/**
 * 宠物订单
 *
 * @author 
 * @email
 */
@TableName("pet_order")
public class PetOrderEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public PetOrderEntity() {

	}

	public PetOrderEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="int(11)")
    @TableField(value = "id")

    private Integer id;


    /**
     * 订单号
     */
    @ColumnInfo(comment="订单号",type="varchar(100)")
    @TableField(value = "order_uuid")

    private String orderUuid;


    /**
     * 用户ID
     */
    @ColumnInfo(comment="用户ID",type="int(11)")
    @TableField(value = "user_id")

    private Integer userId;


    /**
     * 宠物ID
     */
    @ColumnInfo(comment="宠物ID",type="int(11)")
    @TableField(value = "pet_id")

    private Integer petId;


    /**
     * 收货地址ID
     */
    @ColumnInfo(comment="收货地址ID",type="int(11)")
    @TableField(value = "address_id")

    private Integer addressId;


    /**
     * 订单价格
     */
    @ColumnInfo(comment="订单价格",type="decimal(10,2)")
    @TableField(value = "price")

    private Double price;


    /**
     * 订单类型
     */
    @ColumnInfo(comment="订单类型",type="int(11)")
    @TableField(value = "order_type")

    private Integer orderType;


    /**
     * 支付类型
     */
    @ColumnInfo(comment="支付类型",type="int(11)")
    @TableField(value = "payment_type")

    private Integer paymentType;


    /**
     * 订单状态：1=待付款，2=待发货，3=已发货，4=已完成，5=已取消
     */
    @ColumnInfo(comment="订单状态：1=待付款，2=待发货，3=已发货，4=已完成，5=已取消",type="int(11)")
    @TableField(value = "order_status")

    private Integer orderStatus;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
     * 更新时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="更新时间",type="timestamp")
    @TableField(value = "update_time",fill = FieldFill.UPDATE)

    private Date updateTime;

    /**
     * 宠物名称快照
     */
    @ColumnInfo(comment="宠物名称快照",type="varchar(100)")
    @TableField(value = "pet_name")

    private String petName;

    /**
     * 宠物图片URL快照
     */
    @ColumnInfo(comment="宠物图片URL快照",type="varchar(255)")
    @TableField(value = "pet_image_url")

    private String petImageUrl;

    /**
     * 宠物性别快照
     */
    @ColumnInfo(comment="宠物性别快照",type="varchar(10)")
    @TableField(value = "pet_gender")

    private String petGender;

    /**
     * 宠物年龄快照
     */
    @ColumnInfo(comment="宠物年龄快照",type="varchar(20)")
    @TableField(value = "pet_age")

    private String petAge;

    /**
     * 宠物描述快照
     */
    @ColumnInfo(comment="宠物描述快照",type="text")
    @TableField(value = "pet_description")

    private String petDescription;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }
    /**
	 * 设置：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：订单号
	 */
    public String getOrderUuid() {
        return orderUuid;
    }
    /**
	 * 设置：订单号
	 */

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }
    /**
	 * 获取：用户ID
	 */
    public Integer getUserId() {
        return userId;
    }
    /**
	 * 设置：用户ID
	 */

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    /**
	 * 获取：宠物ID
	 */
    public Integer getPetId() {
        return petId;
    }
    /**
	 * 设置：宠物ID
	 */

    public void setPetId(Integer petId) {
        this.petId = petId;
    }
    /**
	 * 获取：收货地址ID
	 */
    public Integer getAddressId() {
        return addressId;
    }
    /**
	 * 设置：收货地址ID
	 */

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
    /**
	 * 获取：订单价格
	 */
    public Double getPrice() {
        return price;
    }
    /**
	 * 设置：订单价格
	 */

    public void setPrice(Double price) {
        this.price = price;
    }
    /**
	 * 获取：订单类型
	 */
    public Integer getOrderType() {
        return orderType;
    }
    /**
	 * 设置：订单类型
	 */

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
    /**
	 * 获取：支付类型
	 */
    public Integer getPaymentType() {
        return paymentType;
    }
    /**
	 * 设置：支付类型
	 */

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
    /**
	 * 获取：订单状态：1=待付款，2=待发货，3=已发货，4=已完成，5=已取消
	 */
    public Integer getOrderStatus() {
        return orderStatus;
    }
    /**
	 * 设置：订单状态：1=待付款，2=待发货，3=已发货，4=已完成，5=已取消
	 */

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
    /**
	 * 获取：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }
    /**
	 * 设置：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
	 * 获取：更新时间
	 */
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
	 * 设置：更新时间
	 */

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
	 * 获取：宠物名称快照
	 */
    public String getPetName() {
        return petName;
    }
    /**
	 * 设置：宠物名称快照
	 */

    public void setPetName(String petName) {
        this.petName = petName;
    }
    /**
	 * 获取：宠物图片URL快照
	 */
    public String getPetImageUrl() {
        return petImageUrl;
    }
    /**
	 * 设置：宠物图片URL快照
	 */

    public void setPetImageUrl(String petImageUrl) {
        this.petImageUrl = petImageUrl;
    }
    /**
	 * 获取：宠物性别快照
	 */
    public String getPetGender() {
        return petGender;
    }
    /**
	 * 设置：宠物性别快照
	 */

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }
    /**
	 * 获取：宠物年龄快照
	 */
    public String getPetAge() {
        return petAge;
    }
    /**
	 * 设置：宠物年龄快照
	 */

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }
    /**
	 * 获取：宠物描述快照
	 */
    public String getPetDescription() {
        return petDescription;
    }
    /**
	 * 设置：宠物描述快照
	 */

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    @Override
    public String toString() {
        return "PetOrder{" +
            ", id=" + id +
            ", orderUuid=" + orderUuid +
            ", userId=" + userId +
            ", petId=" + petId +
            ", addressId=" + addressId +
            ", price=" + price +
            ", orderType=" + orderType +
            ", paymentType=" + paymentType +
            ", orderStatus=" + orderStatus +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
            ", updateTime=" + DateUtil.convertString(updateTime,"yyyy-MM-dd") +
        "}";
    }
}