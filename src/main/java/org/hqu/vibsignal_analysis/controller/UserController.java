package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.entity.User;
import org.hqu.vibsignal_analysis.service.AuthorizationService;
import org.hqu.vibsignal_analysis.service.UserService;
import org.hqu.vibsignal_analysis.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorizationService authorizationService;


    @RequestMapping("/getUserList")
    @ResponseBody
    public Page getUserList(HttpSession session, int page, int rows){
        User user = new User();
        String userId = (String)session.getAttribute("userId");
        user.setUserId(userId);
        return userService.getPagewithoutSelf(page, rows, user);
    }

    @RequestMapping("/authorizeExp")
    @ResponseBody
    public void authorizeExp(@RequestParam String[] expId, @RequestParam String[] userId, @RequestParam(required = false)String newExpName){
        if(newExpName==null||newExpName.length()==0){
            authorizationService.setAuthorization(expId,userId);
        }else{
            authorizationService.setAuthorization(expId,userId,newExpName);
        }

    }

}
