package com.dao;

import com.entity.PetEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;

/**
 * 宠物Dao
 *
 * @author 
 * @email
 */
public interface PetDao extends BaseMapper<PetEntity> {

    List<PetEntity> selectListView(Pagination page, @Param("params") Map<String, Object> params);

}
