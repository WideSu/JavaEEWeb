package org.hqu.vibsignal_analysis.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hqu.vibsignal_analysis.mapper.UserMapper;
import org.hqu.vibsignal_analysis.mapper.entity.User;
import org.hqu.vibsignal_analysis.mapper.entity.UserResult;
import org.hqu.vibsignal_analysis.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public Boolean validateUser(User user){
        User exist = getUser(user);
        if(exist!=null){
            return true;
        }
        else{
            return false;
        }
    }
    public User getUser(User user){
        List<User> userlist = userMapper.getUser(user);
        if (userlist.size()!=0){
            return userlist.get(0);
        }else{
            return null;
        }
    }

    public void addUser(User user) {
        userMapper.insert(user);
    }

    public List<User> getUserList(User user){
        try{
            return userMapper.getUserList(user);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getUserListwithoutSelf(User user){
        try{
            return userMapper.getUserListwithoutSelf(user);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Page getPage(int page, int rows, User user){
        //创建分页对象Pagehelper
        PageHelper.startPage(page,rows);
        List<User> userList = getUserList(user);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return new Page(userList,pageInfo.getTotal());
    }

    public Page getPagewithoutSelf(int page, int rows, User user){
        //创建分页对象Pagehelper
        PageHelper.startPage(page,rows);
        List<User> userList = getUserListwithoutSelf(user);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return new Page(userList,pageInfo.getTotal());
    }
    //-------------------------------------------------------------------------------


    public User getByName(User user) {
        List<User> userList = userMapper.getUser(user);
        if(userList.size()>0){
            return userList.get(0);
        }else{
            return null;
        }

    }


    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    public UserResult insert(User user) {
        int i = userMapper.insertA(user);
        if(i>0){
            return UserResult.ok();
        }else{
            return null;
        }
    }
    
}
