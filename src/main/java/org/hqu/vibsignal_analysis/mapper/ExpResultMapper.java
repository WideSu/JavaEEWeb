package org.hqu.vibsignal_analysis.mapper;

import org.hqu.vibsignal_analysis.mapper.entity.ExpResult;

import java.util.List;

/**
 * 试验结果表MAPPER接口
 * @author Ricky Zhang
 * @version 2019-3-13
 */
public interface ExpResultMapper {
    //base insert
    void insert(ExpResult expResult);

    void update(ExpResult expResult);

    void delete(ExpResult expResult);

    List<ExpResult> findExpResultList(ExpResult expResult);
    
    String findURLbyexpId(String expId);
    
    ExpResult findExpResultByResultId(ExpResult expResult);
    
    List<ExpResult> findExpResultData(ExpResult expResult);
}
