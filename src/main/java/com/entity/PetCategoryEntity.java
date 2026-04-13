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
 * 宠物分类
 *
 * @author 
 * @email
 */
@TableName("pet_category")
public class PetCategoryEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public PetCategoryEntity() {

	}

	public PetCategoryEntity(T t) {
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
     * 分类名称
     */
    @ColumnInfo(comment="分类名称",type="varchar(100)")
    @TableField(value = "name")

    private String name;


    /**
     * 父分类ID，0表示顶级分类
     */
    @ColumnInfo(comment="父分类ID，0表示顶级分类",type="int(11)")
    @TableField(value = "parent_id")

    private Integer parentId;


    /**
     * 分类层级
     */
    @ColumnInfo(comment="分类层级",type="int(11)")
    @TableField(value = "level")

    private Integer level;


    /**
     * 排序
     */
    @ColumnInfo(comment="排序",type="int(11)")
    @TableField(value = "sort")

    private Integer sort;


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
	 * 获取：分类名称
	 */
    public String getName() {
        return name;
    }
    /**
	 * 设置：分类名称
	 */

    public void setName(String name) {
        this.name = name;
    }
    /**
	 * 获取：父分类ID，0表示顶级分类
	 */
    public Integer getParentId() {
        return parentId;
    }
    /**
	 * 设置：父分类ID，0表示顶级分类
	 */

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    /**
	 * 获取：分类层级
	 */
    public Integer getLevel() {
        return level;
    }
    /**
	 * 设置：分类层级
	 */

    public void setLevel(Integer level) {
        this.level = level;
    }
    /**
	 * 获取：排序
	 */
    public Integer getSort() {
        return sort;
    }
    /**
	 * 设置：排序
	 */

    public void setSort(Integer sort) {
        this.sort = sort;
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
        return "PetCategory{" +
            ", id=" + id +
            ", name=" + name +
            ", parentId=" + parentId +
            ", level=" + level +
            ", sort=" + sort +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}