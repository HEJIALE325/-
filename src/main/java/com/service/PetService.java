package com.service;

import com.entity.PetEntity;
import com.entity.vo.PetVO;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * 宠物Service
 *
 * @author 
 * @email
 */
public interface PetService extends IService<PetEntity> {

    /**
     * 查询所有宠物，包括分类名称
     * @return
     */
    List<PetVO> selectAllWithCategoryName();

    /**
     * 根据分类ID查询宠物
     * @param categoryId
     * @return
     */
    List<PetVO> selectByCategoryId(Integer categoryId);

    /**
     * 查询上架的宠物
     * @return
     */
    List<PetVO> selectOnSale();

    /**
     * 根据关键词搜索宠物
     * @param keyword
     * @return
     */
    List<PetVO> selectByKeyword(String keyword);

    /**
     * 根据ID查询宠物详情
     * @param id
     * @return
     */
    PetVO selectByIdWithCategoryName(Integer id);

    /**
     * 添加宠物
     * @param petEntity
     * @return
     */
    boolean add(PetEntity petEntity);

    /**
     * 更新宠物
     * @param petEntity
     * @return
     */
    boolean update(PetEntity petEntity);

    /**
     * 删除宠物
     * @param id
     * @return
     */
    boolean delete(Integer id);

    /**
     * 上架/下架宠物
     * @param id
     * @param status
     * @return
     */
    boolean updateStatus(Integer id, Integer status);

    /**
     * 增加点击数
     * @param id
     * @return
     */
    boolean incrementClickNum(Integer id);
}
