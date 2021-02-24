package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.entity.User;
import org.hqu.vibsignal_analysis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("/home")
    public String login(User user, HttpServletRequest request){
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId == null || userId.length() == 0 ){
            return "redirect:/";
        }else{
            return "home";
        }
    }


    @RequestMapping("/validateUser")
    @ResponseBody
    public Boolean validateUser(User user, HttpServletRequest request){
        Boolean flag = userService.validateUser(user);
        if(flag==true){
            //用户存在则获取user的完整信息
            user = userService.getUser(user);
            HttpSession session = request.getSession();
            session.setAttribute("userId",user.getUserId());
            //前台显示用户名称需要
            session.setAttribute("userName",user.getUserName());
            return true;
        }else{
            return false;
        }
    }
    
  //注册页面
    @RequestMapping("/createUser")
    @ResponseBody
    public String createUser(User user){
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        User temp = new User();
        try{
            temp.setUserName(user.getUserName());
            User newUser = userService.getUser(temp);
            if(newUser!=null){
                return "exist";
            }else{
                String userId = user.getUserName() + time;
                user.setUserId(userId);
                user.setUserState("0");
                userService.addUser(user);
                return "ok";
            }
        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
