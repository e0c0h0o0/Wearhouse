package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.entity.Storage;
import com.wms.mapper.StorageMapper;
import com.wms.service.StorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author thea
 * @since 2023-11-14
 */
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {
    @Resource
    private StorageMapper storageMapper;
    @Override
    public IPage pageCC(IPage<Storage> page, Wrapper wrapper) {
        return  storageMapper.pageCC(page,wrapper);
    }

}
