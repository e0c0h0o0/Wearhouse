package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goodstype;
import com.wms.service.GoodstypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author thea
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private GoodstypeService goodstypeService;
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype) {
        return goodstypeService.save(goodstype)?Result.success():Result.fail();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Goodstype goodstype) {
        return goodstypeService.updateById(goodstype)?Result.success():Result.fail();
    }
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        return goodstypeService.removeById(id)?Result.success():Result.fail();
    }
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");

        Page<Goodstype> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goodstype> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Goodstype::getName,name);
        }

        IPage result = goodstypeService.pageCC(page,lambdaQueryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }
    @GetMapping("/list")
    public Result list(){
        List list = goodstypeService.list();
        return Result.success(list);
    }
}