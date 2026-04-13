
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
 * 宠物分类
 * 后端接口
 * @author
 * @email
*/
@RestController
@RequestMapping("/petCategory")
public class PetCategoryController {
    private static final Logger logger = LoggerFactory.getLogger(PetCategoryController.class);

    private static final String TABLE_NAME = "pet_category";

    @Autowired
    private PetCategoryService petCategoryService;


    @Autowired
    private TokenService tokenService;

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
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = petCategoryService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        PetCategoryEntity petCategory = petCategoryService.selectById(id);
        if(petCategory !=null){
            return R.ok().put("data", petCategory);
        }else {
            return R.error(511,"查不到数据");
        }
    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody PetCategoryEntity petCategory, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,petCategory:{}",this.getClass().getName(),petCategory.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<PetCategoryEntity> queryWrapper = new EntityWrapper<PetCategoryEntity>()
            .eq("name", petCategory.getName())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        PetCategoryEntity petCategoryEntity = petCategoryService.selectOne(queryWrapper);
        if(petCategoryEntity==null){
            petCategory.setCreateTime(new Date());
            petCategoryService.insert(petCategory);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody PetCategoryEntity petCategory, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,petCategory:{}",this.getClass().getName(),petCategory.toString());
        PetCategoryEntity oldPetCategoryEntity = petCategoryService.selectById(petCategory.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");

            petCategoryService.updateById(petCategory);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<PetCategoryEntity> oldPetCategoryList =petCategoryService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        petCategoryService.deleteBatchIds(Arrays.asList(ids));

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
            List<PetCategoryEntity> petCategoryList = new ArrayList<>();//上传的东西
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
                            PetCategoryEntity petCategoryEntity = new PetCategoryEntity();
//                            petCategoryEntity.setName(data.get(0));                    //分类名称 要改的
//                            petCategoryEntity.setParentId(Integer.valueOf(data.get(0)));   //父分类ID 要改的
//                            petCategoryEntity.setLevel(Integer.valueOf(data.get(0)));   //分类层级 要改的
//                            petCategoryEntity.setSort(Integer.valueOf(data.get(0)));   //排序 要改的
//                            petCategoryEntity.setCreateTime(date);//时间
                            petCategoryList.add(petCategoryEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        petCategoryService.insertBatch(petCategoryList);
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
        PageUtils page = petCategoryService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        PetCategoryEntity petCategory = petCategoryService.selectById(id);
            if(petCategory !=null){
                return R.ok().put("data", petCategory);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody PetCategoryEntity petCategory, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,petCategory:{}",this.getClass().getName(),petCategory.toString());
        Wrapper<PetCategoryEntity> queryWrapper = new EntityWrapper<PetCategoryEntity>()
            .eq("name", petCategory.getName())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        PetCategoryEntity petCategoryEntity = petCategoryService.selectOne(queryWrapper);
        if(petCategoryEntity==null){
            petCategory.setCreateTime(new Date());
            petCategoryService.insert(petCategory);

            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

}
