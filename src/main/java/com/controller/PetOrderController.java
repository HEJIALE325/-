

package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 宠物订单
 * 后端接口
 * @author
 * @email
*/
@RestController
@RequestMapping("/petOrder")
public class PetOrderController {
    private static final Logger logger = LoggerFactory.getLogger(PetOrderController.class);

    private static final String TABLE_NAME = "pet_order";

    @Autowired
    private PetOrderService petOrderService;


    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private PetService petService;
    
    @Autowired
    private YonghuService yonghuService;

    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("userId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = petOrderService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        PetOrderEntity petOrder = petOrderService.selectById(id);
        if(petOrder !=null){
            return R.ok().put("data", petOrder);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody PetOrderEntity petOrder, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,petOrder:{}",this.getClass().getName(),petOrder.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<PetOrderEntity> queryWrapper = new EntityWrapper<PetOrderEntity>()
            .eq("order_uuid", petOrder.getOrderUuid())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        PetOrderEntity petOrderEntity = petOrderService.selectOne(queryWrapper);
        if(petOrderEntity==null){
            petOrder.setCreateTime(new Date());
            petOrderService.insert(petOrder);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody PetOrderEntity petOrder, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,petOrder:{}",this.getClass().getName(),petOrder.toString());
        PetOrderEntity oldPetOrderEntity = petOrderService.selectById(petOrder.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));

            petOrderService.updateById(petOrder);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<PetOrderEntity> oldPetOrderList =petOrderService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        petOrderService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<PetOrderEntity> petOrderList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            PetOrderEntity petOrderEntity = new PetOrderEntity();
                            petOrderList.add(petOrderEntity);
                        }

                        petOrderService.insertBatch(petOrderList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }


    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = petOrderService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        PetOrderEntity petOrder = petOrderService.selectById(id);
            if(petOrder !=null){
                return R.ok().put("data", petOrder);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody PetOrderEntity petOrder, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,petOrder:{}",this.getClass().getName(),petOrder.toString());
        Wrapper<PetOrderEntity> queryWrapper = new EntityWrapper<PetOrderEntity>()
            .eq("order_uuid", petOrder.getOrderUuid())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        PetOrderEntity petOrderEntity = petOrderService.selectOne(queryWrapper);
        if(petOrderEntity==null){
            petOrder.setCreateTime(new Date());
            petOrderService.insert(petOrder);

            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }
    
    /**
     * 添加订单
     */
    @RequestMapping("/order")
    public R order(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("order方法:,,Controller:{},,params:{}",this.getClass().getName(),params.toString());
        String orderUuid = String.valueOf(new Date().getTime());

        //获取当前登录用户的id
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if(userId == null) {
            return R.error(511, "请先登录");
        }
        
        Integer addressId = Integer.valueOf(String.valueOf(params.get("addressId")));
        Integer petId = Integer.valueOf(String.valueOf(params.get("petId")));
        Double price = Double.valueOf(String.valueOf(params.get("price")));
        Integer paymentType = Integer.valueOf(String.valueOf(params.get("paymentType")));

        //获取当前登录用户的个人信息
        YonghuEntity yonghuEntity = yonghuService.selectById(userId);
        if(yonghuEntity == null) {
            return R.error(511, "用户不存在");
        }

        //获取宠物信息
        PetEntity petEntity = petService.selectById(petId);
        if(petEntity == null) {
            return R.error(511, "查不到该宠物");
        }

        if(petEntity.getStatus() != 1) {
            return R.error(511, "该宠物已下架");
        }

        //创建订单
        PetOrderEntity petOrderEntity = new PetOrderEntity();
        petOrderEntity.setOrderUuid(orderUuid);
        petOrderEntity.setUserId(userId);
        petOrderEntity.setPetId(petId);
        petOrderEntity.setAddressId(addressId);
        petOrderEntity.setPrice(price);
        petOrderEntity.setOrderType(1);
        petOrderEntity.setPaymentType(paymentType);
        petOrderEntity.setOrderStatus(1); // 1=待付款
        petOrderEntity.setCreateTime(new Date());
        petOrderEntity.setUpdateTime(new Date());
        
        //插入订单
        petOrderService.insert(petOrderEntity);
        
        //将宠物状态设置为已售（下架）
        petEntity.setStatus(0);
        petService.updateById(petEntity);

        return R.ok();
    }
    
    /**
     * 支付订单
     */
    @RequestMapping("/pay")
    public R pay(Integer id, HttpServletRequest request){
        logger.debug("pay方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        
        PetOrderEntity petOrderEntity = petOrderService.selectById(id);
        if(petOrderEntity == null) {
            return R.error(511, "查不到该订单");
        }
        
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if(userId == null || !userId.equals(petOrderEntity.getUserId())) {
            return R.error(511, "无权操作此订单");
        }
        
        if(petOrderEntity.getOrderStatus() != 1) {
            return R.error(511, "订单状态不允许支付");
        }
        
        //模拟支付，直接更新订单状态为已支付（待发货）
        petOrderEntity.setOrderStatus(2); // 2=待发货
        petOrderEntity.setUpdateTime(new Date());
        petOrderService.updateById(petOrderEntity);
        
        return R.ok();
    }

}

