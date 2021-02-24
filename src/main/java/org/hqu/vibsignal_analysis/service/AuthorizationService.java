package org.hqu.vibsignal_analysis.service;

import org.hqu.vibsignal_analysis.mapper.ExperimentMapper;
import org.hqu.vibsignal_analysis.mapper.entity.DataStorage;
import org.hqu.vibsignal_analysis.mapper.entity.ExpParameter;
import org.hqu.vibsignal_analysis.mapper.entity.ExpResult;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.util.CodeGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class AuthorizationService {
    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private ExperimentMapper experimentMapper;


    public void setAuthorization(String[] expId, String[] userId) {
        for(String id : userId){
            for(String eId : expId){
                //先获得已有id的试验，复制其数据，更新需要创建的expid
                Experiment temp = new Experiment();
                temp.setExpId(eId);
                temp = copyExpInfo(temp,id);
                temp.setAuthorizedUserId(id);
                experimentService.insertAllRelatedInfo(temp);
            }
        }
    }

    public void setAuthorization(String[] expId, String[] userId, String newExpName) {
        for(String id : userId){
            for(String eId : expId){
                //先获得已有id的试验，复制其数据，更新需要创建的expid
                Experiment temp = new Experiment();
                temp.setExpId(eId);
                temp = copyExpInfo(temp,id);
                temp.setAuthorizedUserId(id);
                temp.setExpName(newExpName);
                experimentService.insertAllRelatedInfo(temp);
            }
        }
    }

    public Experiment copyExpInfo(Experiment experiment, String id2Au){
        //获取需要被授权的试验
        experiment = experimentService.getExp(experiment);
        String newExpId = "";
        String newDataId;
        if(experiment!=null){
            newExpId = CodeGen.genExpId(experiment.getExpClass());
            newDataId = CodeGen.genDataId(experiment.getUserId());
            experiment.setExpId(newExpId);
            DataStorage data = new DataStorage();
            if(experiment.getData()!=null){
                data.setDataId(newDataId);
                experiment.setData(data);
            }

            ExpParameter expParameter = experiment.getExpParameter();
            if(expParameter!=null){
                expParameter.setExperiment(experiment);
            }

            List<ExpResult> expResultList = experiment.getExpResult();
            if(expResultList!=null&&expResultList.size()>0){
                Iterator<ExpResult> it = expResultList.iterator();
                while(it.hasNext()){
                    ExpResult expResult = it.next();
                    expResult.setExperiment(experiment);
                }
            }

            List<DataStorage> dataStorageList = experiment.getDataStorageList();
            if(dataStorageList!=null&&dataStorageList.size()>0){
                Iterator<DataStorage> it = dataStorageList.iterator();
                while(it.hasNext()){
                    DataStorage dataTemp = it.next();
                    dataTemp.setDataId(newDataId);
                    dataTemp.setUserId(id2Au);
                }
            }

            return experiment;
        }else{
            experiment.setExpId(newExpId);
            ExpParameter expParameter = new ExpParameter();
            expParameter.setExperiment(experiment);
            experiment.setExpParameter(expParameter);
            return experiment;
        }
    }
}
