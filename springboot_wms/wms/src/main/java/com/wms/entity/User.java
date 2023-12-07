package com.wms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author thea
 * @since 2023-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("USER")
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "Account")
    @TableField("NO")
    private String no;

    @ApiModelProperty(value = "Name")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "Password")
    @TableField("PASSWORD")
    private String password;

    private Integer age;

    private String sex;

    @ApiModelProperty(value = "phonenumber")
    private String phone;

    @ApiModelProperty(value = "0-supervisor 1-manger 2-normal account")
    private Integer roleId;

    @ApiModelProperty(value = "Y is valid,others are not valid")
    @TableField("isValid")
    private String isvalid;


}
