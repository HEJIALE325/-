package com.entity.vo;

import com.entity.PetCategoryEntity;
import java.io.Serializable;
import java.util.Date;

/**
 * 宠物分类VO
 *
 * @author 
 * @email
 */
public class PetCategoryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类ID，0表示顶级分类
     */
    private Integer parentId;

    /**
     * 父分类名称
     */
    private String parentName;

    /**
     * 分类层级
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    public PetCategoryVO() {
    }

    public PetCategoryVO(PetCategoryEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.parentId = entity.getParentId();
        this.level = entity.getLevel();
        this.sort = entity.getSort();
        this.createTime = entity.getCreateTime();
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}