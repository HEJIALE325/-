package com.service;

import com.entity.PetCategoryEntity;
import com.entity.vo.PetCategoryVO;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * 宠物分类Service
 *
 * @author 
 * @email
 */
public interface PetCategoryService extends IService<PetCategoryEntity> {

    /**
     * 查询所有分类，包括父分类名称
     * @return
     */
    List<PetCategoryVO> selectAllWithParentName();

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

    /**
     * 添加分类
     * @param petCategoryEntity
     * @return
     */
    boolean add(PetCategoryEntity petCategoryEntity);

    /**
     * 更新分类
     * @param petCategoryEntity
     * @return
     */
    boolean update(PetCategoryEntity petCategoryEntity);

    /**
     * 删除分类
     * @param id
     * @return
     */
    boolean delete(Integer id);
}
