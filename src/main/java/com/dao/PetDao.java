package com.dao;

import com.entity.PetEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 宠物Dao
 *
 * @author 
 * @email
 */
public interface PetDao extends BaseMapper<PetEntity> {

    /**
     * 查询所有宠物，包括分类名称
     * @return
     */
    List<PetEntity> selectAllWithCategoryName();

    /**
     * 根据分类ID查询宠物
     * @param categoryId
     * @return
     */
    List<PetEntity> selectByCategoryId(Integer categoryId);

    /**
     * 查询上架的宠物
     * @return
     */
    List<PetEntity> selectOnSale();

    /**
     * 根据关键词搜索宠物
     * @param keyword
     * @return
     */
    List<PetEntity> selectByKeyword(String keyword);
}
