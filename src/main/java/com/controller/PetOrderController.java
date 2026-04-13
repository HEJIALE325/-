package com.controller;

import com.entity.PetOrderEntity;
import com.entity.vo.PetOrderVO;
import com.service.PetOrderService;
import com.utils.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物订单Controller
 *
 * @author 
 * @email
 */
@RestController
@RequestMapping("/api/petOrder")
public class PetOrderController {

    @Autowired
    private PetOrderService petOrderService;

    /**
     * 查询所有订单
     */
    @RequestMapping("/list")
    public R list() {
        List<PetOrderVO> list = petOrderService.selectAllWithDetails();
        return R.ok().put("data", list);
    }

    /**
     * 根据用户ID查询订单
     */
    @RequestMapping("/byUser/{userId}")
    public R byUser(@PathVariable Integer userId) {
        List<PetOrderVO> list = petOrderService.selectByUserId(userId);
        return R.ok().put("data", list);
    }

    /**
     * 根据订单状态查询订单
     */
    @RequestMapping("/byStatus/{orderStatus}")
    public R byStatus(@PathVariable Integer orderStatus) {
        List<PetOrderVO> list = petOrderService.selectByOrderStatus(orderStatus);
        return R.ok().put("data", list);
    }

    /**
     * 根据订单号查询订单
     */
    @RequestMapping("/byUuid/{orderUuid}")
    public R byUuid(@PathVariable String orderUuid) {
        PetOrderVO petOrderVO = petOrderService.selectByOrderUuid(orderUuid);
        return R.ok().put("data", petOrderVO);
    }

    /**
     * 根据ID查询订单详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable Integer id) {
        PetOrderVO petOrderVO = petOrderService.selectByIdWithDetails(id);
        return R.ok().put("data", petOrderVO);
    }

    /**
     * 创建订单
     */
    @RequestMapping("/create")
    public R create(@RequestBody PetOrderEntity petOrderEntity) {
        petOrderService.create(petOrderEntity);
        return R.ok();
    }

    /**
     * 更新订单状态
     */
    @RequestMapping("/updateStatus/{id}/{orderStatus}")
    public R updateStatus(@PathVariable Integer id, @PathVariable Integer orderStatus) {
        petOrderService.updateOrderStatus(id, orderStatus);
        return R.ok();
    }

    /**
     * 取消订单
     */
    @RequestMapping("/cancel/{id}")
    public R cancel(@PathVariable Integer id) {
        petOrderService.cancelOrder(id);
        return R.ok();
    }

    /**
     * 删除订单
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable Integer id) {
        petOrderService.delete(id);
        return R.ok();
    }
}
