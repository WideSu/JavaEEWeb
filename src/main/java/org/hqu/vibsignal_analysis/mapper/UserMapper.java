package org.hqu.vibsignal_analysis.mapper;

import org.apache.ibatis.annotations.Param;
import org.hqu.vibsignal_analysis.mapper.entity.User;
import org.hqu.vibsignal_analysis.mapper.entity.UserExample;

import java.util.List;

/**
 * 试验结果表MAPPER接口
 * @author Ricky Zhang
 * @version 2019-3-13
 */
public interface UserMapper {
    List<User> getUser(User user);

    void insert(User user);

    List<User> getUserList(User user);

    List<User> getUserListwithoutSelf(User user);

    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(String userId);

    int insertA(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    //按照用户名进行查找
    User selectByName(String userName);

    //本工程唯一使用的方法
    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);
}
