package com.service.impl;

import com.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.util.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import com.dao.PetCategoryDao;
import com.entity.PetCategoryEntity;
import com.service.PetCategoryService;

/**
 * 宠物分类 服务实现类
 */
@Service("petCategoryService")
@Transactional
public class PetCategoryServiceImpl extends ServiceImpl<PetCategoryDao, PetCategoryEntity> implements PetCategoryService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        Page<PetCategoryEntity> page =new Query<PetCategoryEntity>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }


}
