package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author thea
 * @since 2023-11-01
 */
public interface UserMapper extends BaseMapper<User> {

    IPage pageC(Page<User> page);

    IPage pageCC(IPage<User> page, @Param(Constants.WRAPPER) Wrapper wrapper);


}
