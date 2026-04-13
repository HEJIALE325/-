package com.service;

import com.entity.PetOrderEntity;
import com.entity.vo.PetOrderVO;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * 宠物订单Service
 *
 * @author 
 * @email
 */
public interface PetOrderService extends IService<PetOrderEntity> {

    /**
     * 查询所有订单，包括用户、宠物和地址信息
     * @return
     */
    List<PetOrderVO> selectAllWithDetails();

    /**
     * 根据用户ID查询订单
     * @param userId
     * @return
     */
    List<PetOrderVO> selectByUserId(Integer userId);

    /**
     * 根据订单状态查询订单
     * @param orderStatus
     * @return
     */
    List<PetOrderVO> selectByOrderStatus(Integer orderStatus);

    /**
     * 根据订单号查询订单
     * @param orderUuid
     * @return
     */
    PetOrderVO selectByOrderUuid(String orderUuid);

    /**
     * 根据ID查询订单详情
     * @param id
     * @return
     */
    PetOrderVO selectByIdWithDetails(Integer id);

    /**
     * 创建订单
     * @param petOrderEntity
     * @return
     */
    boolean create(PetOrderEntity petOrderEntity);

    /**
     * 更新订单状态
     * @param id
     * @param orderStatus
     * @return
     */
    boolean updateOrderStatus(Integer id, Integer orderStatus);

    /**
     * 取消订单
     * @param id
     * @return
     */
    boolean cancelOrder(Integer id);

    /**
     * 删除订单
     * @param id
     * @return
     */
    boolean delete(Integer id);
}
