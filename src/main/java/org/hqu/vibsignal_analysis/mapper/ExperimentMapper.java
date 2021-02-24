package org.hqu.vibsignal_analysis.mapper;

import org.hqu.vibsignal_analysis.mapper.entity.DataStorage;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.mapper.entity.UploadedData;

import java.util.List;

/**
 * 试验MAPPER接口
 * @author Ricky Zhang
 * @version 2019-3-3
 */
public interface ExperimentMapper {

    //寻找experiment
    List<Experiment> getExpriment(Experiment experiment);

    //Base insert
    void insert(Experiment experiment);
    //Base update
    void update(Experiment experiment);
    //Base delete
    void delete(Experiment experiment);

    //获取已上传的训练与待预测数据
    List<UploadedData> getUploadedDataList(DataStorage data);

    //根据试验类别获取已有的试验
    List<Experiment> getExpList(Experiment experiment);

    //根据试验类别获取用户创建的试验(非授权)
    List<Experiment> getCreatedExpList(Experiment experiment);

    List<Experiment> getExperimentsByExpId(Experiment experiment);
    
    
  //获取载荷识别相关的已下载数据
    List<UploadedData> getLpUploadedDataList(DataStorage data);

    //Annie获取所有带有Algo和userName的实验
    List<Experiment> getExpListWithAlgoUserName(String expClass);

    //Annie实验表查询
    List<Experiment>selectBySearchInfo(Experiment experiment);

    //Annie实验参数表查询
    List<Experiment> expPSelectBySearchInfo(Experiment experiment);

    //Annie查出实验参数表所有信息
    List<Experiment> allExpP();

    //Annie按照expId更新expName的update
    void updateExpname(Experiment experiment);

    //Annie
    List<Experiment> getExpPList(Experiment experiment);

    //Annie
    List<Experiment> queryExpList(Experiment experiment);

    void deleteDataInfoByUpdate(String dataId);

}
