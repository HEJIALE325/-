package com.dao;

import com.entity.PetCategoryEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;

/**
 * 宠物分类Dao
 *
 * @author 
 * @email
 */
public interface PetCategoryDao extends BaseMapper<PetCategoryEntity> {

    List<PetCategoryEntity> selectListView(Pagination page, @Param("params") Map<String, Object> params);

}
