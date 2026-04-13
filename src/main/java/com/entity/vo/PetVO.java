package com.entity.vo;

import com.entity.PetEntity;
import java.io.Serializable;
import java.util.Date;

/**
 * 宠物VO
 *
 * @author 
 * @email
 */
public class PetVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 宠物名称
     */
    private String name;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private String age;

    /**
     * 价格
     */
    private Double price;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 状态：1=上架，0=下架
     */
    private Integer status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 点击数
     */
    private Integer clickNum;

    /**
     * 创建时间
     */
    private Date createTime;

    public PetVO() {
    }

    public PetVO(PetEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.categoryId = entity.getCategoryId();
        this.gender = entity.getGender();
        this.age = entity.getAge();
        this.price = entity.getPrice();
        this.description = entity.getDescription();
        this.imageUrl = entity.getImageUrl();
        this.status = entity.getStatus();
        this.clickNum = entity.getClickNum();
        this.createTime = entity.getCreateTime();
        
        // 设置状态文本
        if (status == 1) {
            this.statusText = "上架";
        } else {
            this.statusText = "下架";
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        // 更新状态文本
        if (status == 1) {
            this.statusText = "上架";
        } else {
            this.statusText = "下架";
        }
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Integer getClickNum() {
        return clickNum;
    }

    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}