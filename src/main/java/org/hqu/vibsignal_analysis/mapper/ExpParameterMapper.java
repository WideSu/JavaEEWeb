package org.hqu.vibsignal_analysis.mapper;

import org.apache.ibatis.annotations.Param;
import org.hqu.vibsignal_analysis.mapper.entity.ExpParameter;

import java.util.List;
import java.util.Map;
/**
 * 试验参数表MAPPER接口
 * @author Ricky Zhang
 * @version 2019-3-13
 */
public interface ExpParameterMapper {

    //根据expId寻找试验参数
    List<ExpParameter> findExpParameterList(ExpParameter expParameter);

    //根据expId寻找试验参数
    List<Map<String,String>> findExpParameterMap(ExpParameter expParameter);
    //Base insert
    void insert(ExpParameter expParameter);


    void insertInMap(@Param("expParameter")ExpParameter expParameter, @Param("map")Map map);


    void updateInMap(@Param("expParameter")ExpParameter expParameter, @Param("map")Map map);

    void update(ExpParameter expParameter);
    

}
