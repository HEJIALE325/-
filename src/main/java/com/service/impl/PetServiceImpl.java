package com.service.impl;

import com.entity.PetEntity;
import com.entity.vo.PetVO;
import com.dao.PetDao;
import com.service.PetService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 宠物Service实现
 *
 * @author 
 * @email
 */
@Service("petService")
public class PetServiceImpl extends ServiceImpl<PetDao, PetEntity> implements PetService {

    @Autowired
    private PetDao petDao;

    @Override
    public List<PetVO> selectAllWithCategoryName() {
        List<PetEntity> entities = petDao.selectAllWithCategoryName();
        return entities.stream().map(PetVO::new).collect(Collectors.toList());
    }

    @Override
    public List<PetVO> selectByCategoryId(Integer categoryId) {
        List<PetEntity> entities = petDao.selectByCategoryId(categoryId);
        return entities.stream().map(PetVO::new).collect(Collectors.toList());
    }

    @Override
    public List<PetVO> selectOnSale() {
        List<PetEntity> entities = petDao.selectOnSale();
        return entities.stream().map(PetVO::new).collect(Collectors.toList());
    }

    @Override
    public List<PetVO> selectByKeyword(String keyword) {
        List<PetEntity> entities = petDao.selectByKeyword(keyword);
        return entities.stream().map(PetVO::new).collect(Collectors.toList());
    }

    @Override
    public PetVO selectByIdWithCategoryName(Integer id) {
        PetEntity entity = petDao.selectById(id);
        if (entity != null) {
            PetVO vo = new PetVO(entity);
            // 这里可以添加分类名称的查询逻辑
            return vo;
        }
        return null;
    }

    @Override
    public boolean add(PetEntity petEntity) {
        return this.insert(petEntity);
    }

    @Override
    public boolean update(PetEntity petEntity) {
        return this.updateById(petEntity);
    }

    @Override
    public boolean delete(Integer id) {
        return this.deleteById(id);
    }

    @Override
    public boolean updateStatus(Integer id, Integer status) {
        PetEntity entity = this.selectById(id);
        if (entity != null) {
            entity.setStatus(status);
            return this.updateById(entity);
        }
        return false;
    }

    @Override
    public boolean incrementClickNum(Integer id) {
        PetEntity entity = this.selectById(id);
        if (entity != null) {
            entity.setClickNum(entity.getClickNum() + 1);
            return this.updateById(entity);
        }
        return false;
    }
}
