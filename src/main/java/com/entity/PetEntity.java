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
 * 宠物
 *
 * @author 
 * @email
 */
@TableName("pets")
public class PetEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public PetEntity() {

	}

	public PetEntity(T t) {
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
     * 宠物名称
     */
    @ColumnInfo(comment="宠物名称",type="varchar(100)")
    @TableField(value = "name")

    private String name;


    /**
     * 分类ID
     */
    @ColumnInfo(comment="分类ID",type="int(11)")
    @TableField(value = "category_id")

    private Integer categoryId;

    /**
     * 分类名称
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 性别
     */
    @ColumnInfo(comment="性别",type="varchar(10)")
    @TableField(value = "gender")

    private String gender;


    /**
     * 年龄
     */
    @ColumnInfo(comment="年龄",type="varchar(20)")
    @TableField(value = "age")

    private String age;


    /**
     * 品种
     */
    @ColumnInfo(comment="品种",type="varchar(100)")
    @TableField(value = "breed")

    private String breed;


    /**
     * 价格
     */
    @ColumnInfo(comment="价格",type="decimal(10,2)")
    @TableField(value = "price")

    private Double price;


    /**
     * 描述
     */
    @ColumnInfo(comment="描述",type="text")
    @TableField(value = "description")

    private String description;


    /**
     * 图片URL
     */
    @ColumnInfo(comment="图片URL",type="varchar(255)")
    @TableField(value = "image_url")

    private String imageUrl;


    /**
     * 状态：1=上架，0=下架
     */
    @ColumnInfo(comment="状态：1=上架，0=下架",type="int(11)")
    @TableField(value = "status")

    private Integer status;


    /**
     * 点击数
     */
    @ColumnInfo(comment="点击数",type="int(11)")
    @TableField(value = "click_num")

    private Integer clickNum;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


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
	 * 获取：宠物名称
	 */
    public String getName() {
        return name;
    }
    /**
	 * 设置：宠物名称
	 */

    public void setName(String name) {
        this.name = name;
    }
    /**
	 * 获取：分类ID
	 */
    public Integer getCategoryId() {
        return categoryId;
    }
    /**
	 * 设置：分类ID
	 */

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    /**
     * 获取：分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }
    /**
     * 设置：分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    /**
	 * 获取：性别
	 */
    public String getGender() {
        return gender;
    }
    /**
	 * 设置：性别
	 */

    public void setGender(String gender) {
        this.gender = gender;
    }
    /**
	 * 获取：年龄
	 */
    public String getAge() {
        return age;
    }
    /**
	 * 设置：年龄
	 */

    public void setAge(String age) {
        this.age = age;
    }
    /**
	 * 获取：品种
	 */
    public String getBreed() {
        return breed;
    }
    /**
	 * 设置：品种
	 */

    public void setBreed(String breed) {
        this.breed = breed;
    }
    /**
	 * 获取：价格
	 */
    public Double getPrice() {
        return price;
    }
    /**
	 * 设置：价格
	 */

    public void setPrice(Double price) {
        this.price = price;
    }
    /**
	 * 获取：描述
	 */
    public String getDescription() {
        return description;
    }
    /**
	 * 设置：描述
	 */

    public void setDescription(String description) {
        this.description = description;
    }
    /**
	 * 获取：图片URL
	 */
    public String getImageUrl() {
        return imageUrl;
    }
    /**
	 * 设置：图片URL
	 */

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    /**
	 * 获取：状态：1=上架，0=下架
	 */
    public Integer getStatus() {
        return status;
    }
    /**
	 * 设置：状态：1=上架，0=下架
	 */

    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
	 * 获取：点击数
	 */
    public Integer getClickNum() {
        return clickNum;
    }
    /**
	 * 设置：点击数
	 */

    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
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

    @Override
    public String toString() {
        return "Pet{" +
            ", id=" + id +
            ", name=" + name +
            ", categoryId=" + categoryId +
            ", gender=" + gender +
            ", age=" + age +
            ", breed=" + breed +
            ", price=" + price +
            ", description=" + description +
            ", imageUrl=" + imageUrl +
            ", status=" + status +
            ", clickNum=" + clickNum +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}