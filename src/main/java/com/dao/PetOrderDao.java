package com.dao;

import com.entity.PetOrderEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 宠物订单Dao
 *
 * @author 
 * @email
 */
public interface PetOrderDao extends BaseMapper<PetOrderEntity> {

    /**
     * 查询所有订单，包括用户、宠物和地址信息
     * @return
     */
    List<PetOrderEntity> selectAllWithDetails();

    /**
     * 根据用户ID查询订单
     * @param userId
     * @return
     */
    List<PetOrderEntity> selectByUserId(Integer userId);

    /**
     * 根据订单状态查询订单
     * @param orderStatus
     * @return
     */
    List<PetOrderEntity> selectByOrderStatus(Integer orderStatus);

    /**
     * 根据订单号查询订单
     * @param orderUuid
     * @return
     */
    PetOrderEntity selectByOrderUuid(String orderUuid);
}
