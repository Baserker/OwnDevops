package com.caimao.devops.controller;

import com.caimao.devops.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请求页面的控制层
 * 模块名称：CaimaoDevOps com.caimao.devops.controller
 * 功能说明：<br>
 * 开发人员：Wangyq
 * 创建时间： 2019-05-28 15:26
 * 系统版本：1.0.0
 **/

@Controller
@RequestMapping("/req")
public class ReqController extends BaseController {

    /**
     * 跳转至首页
     */
    @RequestMapping("/main")
    public String main(){
        return "static/main";
    }


    /**
     * 跳转至客户端版本页面
     * @return
     */
    @RequestMapping("/appClient")
    public String appClient(){
        return "appClient/clientsCon";
    }

    /**
     * 跳转到对应的历史版本界面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/hisAppClient")
    public String hisAppClient(Integer id, Model model){
        model.addAttribute("id",id);
        return "appClient/clientsHis";
    }

    /**
     * 跳转至ios的plists页面
     * @return
     */
    @RequestMapping("/plists")
    public String plists(){
        return "appClient/plistsCon";
    }

    /**
     * 跳转到plist的详情页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/plistDetail")
    public String plistDetail(Integer id, Model model){
        model.addAttribute("id",id);
        return "appClient/plistDetail";
    }

    /**
     * 跳转至ios的下载界面
     * @param id  ios的客户端id
     * @param model
     * @return
     */
    @RequestMapping("/iosDown")
    public String iosDown(Integer id,Model model){
        model.addAttribute("id",id);
        return "appClient/downloadClient";
    }

    /**
     * 跳转到页面静态文件上传
     * @return
     */
    @RequestMapping("/staticPages")
    public String staticPages(){
        return "staticDoc/pageDocMana";
    }
}