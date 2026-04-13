package com.service.impl;

import com.entity.PetOrderEntity;
import com.entity.vo.PetOrderVO;
import com.dao.PetOrderDao;
import com.service.PetOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 宠物订单Service实现
 *
 * @author 
 * @email
 */
@Service("petOrderService")
public class PetOrderServiceImpl extends ServiceImpl<PetOrderDao, PetOrderEntity> implements PetOrderService {

    @Autowired
    private PetOrderDao petOrderDao;

    @Override
    public List<PetOrderVO> selectAllWithDetails() {
        List<PetOrderEntity> entities = petOrderDao.selectAllWithDetails();
        return entities.stream().map(PetOrderVO::new).collect(Collectors.toList());
    }

    @Override
    public List<PetOrderVO> selectByUserId(Integer userId) {
        List<PetOrderEntity> entities = petOrderDao.selectByUserId(userId);
        return entities.stream().map(PetOrderVO::new).collect(Collectors.toList());
    }

    @Override
    public List<PetOrderVO> selectByOrderStatus(Integer orderStatus) {
        List<PetOrderEntity> entities = petOrderDao.selectByOrderStatus(orderStatus);
        return entities.stream().map(PetOrderVO::new).collect(Collectors.toList());
    }

    @Override
    public PetOrderVO selectByOrderUuid(String orderUuid) {
        PetOrderEntity entity = petOrderDao.selectByOrderUuid(orderUuid);
        if (entity != null) {
            return new PetOrderVO(entity);
        }
        return null;
    }

    @Override
    public PetOrderVO selectByIdWithDetails(Integer id) {
        PetOrderEntity entity = petOrderDao.selectById(id);
        if (entity != null) {
            return new PetOrderVO(entity);
        }
        return null;
    }

    @Override
    public boolean create(PetOrderEntity petOrderEntity) {
        return this.insert(petOrderEntity);
    }

    @Override
    public boolean updateOrderStatus(Integer id, Integer orderStatus) {
        PetOrderEntity entity = this.selectById(id);
        if (entity != null) {
            entity.setOrderStatus(orderStatus);
            return this.updateById(entity);
        }
        return false;
    }

    @Override
    public boolean cancelOrder(Integer id) {
        return this.updateOrderStatus(id, 5); // 5表示已取消
    }

    @Override
    public boolean delete(Integer id) {
        return this.deleteById(id);
    }
}
