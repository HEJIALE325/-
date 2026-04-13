package com.dao;

import com.entity.PetCategoryEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 宠物分类Dao
 *
 * @author 
 * @email
 */
public interface PetCategoryDao extends BaseMapper<PetCategoryEntity> {

    /**
     * 查询所有分类，包括父分类名称
     * @return
     */
    List<PetCategoryEntity> selectAllWithParentName();

    /**
     * 根据父分类ID查询子分类
     * @param parentId
     * @return
     */
    List<PetCategoryEntity> selectByParentId(Integer parentId);

    /**
     * 查询顶级分类
     * @return
     */
    List<PetCategoryEntity> selectTopLevel();
}
