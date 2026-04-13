package com.controller;

import com.entity.PetCategoryEntity;
import com.entity.vo.PetCategoryVO;
import com.service.PetCategoryService;
import com.utils.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物分类Controller
 *
 * @author 
 * @email
 */
@RestController
@RequestMapping("/api/petCategory")
public class PetCategoryController {

    @Autowired
    private PetCategoryService petCategoryService;

    /**
     * 查询所有分类
     */
    @RequestMapping("/list")
    public R list() {
        List<PetCategoryVO> list = petCategoryService.selectAllWithParentName();
        return R.ok().put("data", list);
    }

    /**
     * 查询顶级分类
     */
    @RequestMapping("/topLevel")
    public R topLevel() {
        List<PetCategoryEntity> list = petCategoryService.selectTopLevel();
        return R.ok().put("data", list);
    }

    /**
     * 根据父分类ID查询子分类
     */
    @RequestMapping("/children/{parentId}")
    public R children(@PathVariable Integer parentId) {
        List<PetCategoryEntity> list = petCategoryService.selectByParentId(parentId);
        return R.ok().put("data", list);
    }

    /**
     * 保存分类
     */
    @RequestMapping("/save")
    public R save(@RequestBody PetCategoryEntity petCategoryEntity) {
        petCategoryService.add(petCategoryEntity);
        return R.ok();
    }

    /**
     * 更新分类
     */
    @RequestMapping("/update")
    public R update(@RequestBody PetCategoryEntity petCategoryEntity) {
        petCategoryService.update(petCategoryEntity);
        return R.ok();
    }

    /**
     * 删除分类
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable Integer id) {
        petCategoryService.delete(id);
        return R.ok();
    }

    /**
     * 根据ID查询分类
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable Integer id) {
        PetCategoryEntity petCategoryEntity = petCategoryService.selectById(id);
        return R.ok().put("data", petCategoryEntity);
    }
}
