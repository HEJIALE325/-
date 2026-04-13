package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.PetOrderEntity;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import java.util.List;

/**
 * 宠物订单Service
 *
 * @author 
 * @email
 */
public interface PetOrderService extends IService<PetOrderEntity> {

    /**
    * @param params 查询参数
    * @return 带分页的查询出来的数据
    */
    PageUtils queryPage(Map<String, Object> params);

}
