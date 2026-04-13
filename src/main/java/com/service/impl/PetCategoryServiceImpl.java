package com.service.impl;

import com.entity.PetCategoryEntity;
import com.entity.vo.PetCategoryVO;
import com.dao.PetCategoryDao;
import com.service.PetCategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 宠物分类Service实现
 *
 * @author 
 * @email
 */
@Service("petCategoryService")
public class PetCategoryServiceImpl extends ServiceImpl<PetCategoryDao, PetCategoryEntity> implements PetCategoryService {

    @Autowired
    private PetCategoryDao petCategoryDao;

    @Override
    public List<PetCategoryVO> selectAllWithParentName() {
        List<PetCategoryEntity> entities = petCategoryDao.selectAllWithParentName();
        return entities.stream().map(PetCategoryVO::new).collect(Collectors.toList());
    }

    @Override
    public List<PetCategoryEntity> selectByParentId(Integer parentId) {
        return petCategoryDao.selectByParentId(parentId);
    }

    @Override
    public List<PetCategoryEntity> selectTopLevel() {
        return petCategoryDao.selectTopLevel();
    }

    @Override
    public boolean add(PetCategoryEntity petCategoryEntity) {
        return this.insert(petCategoryEntity);
    }

    @Override
    public boolean update(PetCategoryEntity petCategoryEntity) {
        return this.updateById(petCategoryEntity);
    }

    @Override
    public boolean delete(Integer id) {
        return this.deleteById(id);
    }
}
