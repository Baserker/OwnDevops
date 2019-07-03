package com.caimao.devops.contants;

/**
 * 模块名称：CaimaoDevOps com.caimao.devops.contants
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 16:29
 * 系统版本：1.0.0
 **/

public interface CodeContants {
    //成功
    int SUCCESS = 1;
    //未登录
    int NO_LOGIN = 401;

    //分布式并发锁，  同用户Controller为执行完， 又请求时 返回该错误
    int DISTRIBUTED_LOCK = 409;

    //参数错误
    int PARAMS_ERROR = 412;

    //服务器内部错误
    int EXCEPTION_ERROR = 500;
    //默认错误
    int COMMON_ERROR = 510;
    //已存在， 重复  错误
    int ALREADY_EXISTS_ERROR = 511;
    //未找到
    int NOT_FIND_ERROR = 512;
    //需要验证码
    int NEED_VERIFICATION_ERROR = 513;
    //上传文件过
    int UPLOAD_IMG_TOO_LARGE = 514;
}