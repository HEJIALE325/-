package com.controller;

import com.entity.PetEntity;
import com.entity.vo.PetVO;
import com.service.PetService;
import com.utils.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物Controller
 *
 * @author 
 * @email
 */
@RestController
@RequestMapping("/api/pet")
public class PetController {

    @Autowired
    private PetService petService;

    /**
     * 查询所有宠物
     */
    @RequestMapping("/list")
    public R list() {
        List<PetVO> list = petService.selectAllWithCategoryName();
        return R.ok().put("data", list);
    }

    /**
     * 查询上架的宠物
     */
    @RequestMapping("/onSale")
    public R onSale() {
        List<PetVO> list = petService.selectOnSale();
        return R.ok().put("data", list);
    }

    /**
     * 根据分类ID查询宠物
     */
    @RequestMapping("/byCategory/{categoryId}")
    public R byCategory(@PathVariable Integer categoryId) {
        List<PetVO> list = petService.selectByCategoryId(categoryId);
        return R.ok().put("data", list);
    }

    /**
     * 根据关键词搜索宠物
     */
    @RequestMapping("/search")
    public R search(@RequestParam String keyword) {
        List<PetVO> list = petService.selectByKeyword(keyword);
        return R.ok().put("data", list);
    }

    /**
     * 根据ID查询宠物详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable Integer id) {
        // 增加点击数
        petService.incrementClickNum(id);
        PetVO petVO = petService.selectByIdWithCategoryName(id);
        return R.ok().put("data", petVO);
    }

    /**
     * 保存宠物
     */
    @RequestMapping("/save")
    public R save(@RequestBody PetEntity petEntity) {
        petService.add(petEntity);
        return R.ok();
    }

    /**
     * 更新宠物
     */
    @RequestMapping("/update")
    public R update(@RequestBody PetEntity petEntity) {
        petService.update(petEntity);
        return R.ok();
    }

    /**
     * 删除宠物
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable Integer id) {
        petService.delete(id);
        return R.ok();
    }

    /**
     * 上架/下架宠物
     */
    @RequestMapping("/updateStatus/{id}/{status}")
    public R updateStatus(@PathVariable Integer id, @PathVariable Integer status) {
        petService.updateStatus(id, status);
        return R.ok();
    }
}
