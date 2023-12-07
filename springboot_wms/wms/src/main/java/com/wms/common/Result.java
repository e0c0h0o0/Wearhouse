package com.wms.common;

import lombok.Data;

@Data
public class Result {
    private int code;//200/400
    private String msg;//success/fail
    private Long total;
    private Object data;
    public static Result fail(){
        return result(400,"Fail",0L ,null);
    }
    public static Result success(){
        return result(200,"Success  ",0L ,null);
    }
    public static Result success(Object data){
        return result(200,"Success",0L ,data );
    }
    public static Result success(Object data,Long total){
        return result(200,"Success",total  ,data );
    }
    private static Result result(int code,String msg,Long total,Object data){
         Result res = new Result();
         res.setData(data);
         res.setMsg(msg);
         res.setCode(code);
         res.setTotal(total);
         return res;
    }
}
