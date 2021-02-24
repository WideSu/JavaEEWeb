package org.hqu.vibsignal_analysis.service;

import org.hqu.vibsignal_analysis.mapper.ExpParameterMapper;
import org.hqu.vibsignal_analysis.mapper.entity.ExpParameter;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.util.ListMapToMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ExpParameterService {
    @Autowired
    private ExpParameterMapper expParameterMapper;

    ListMapToMap listm2map = new ListMapToMap();

    public void savePara(ExpParameter expParameter){
        Map<String, String> map;
        map = expParameter.getFeature();
        map.entrySet();
        //判定该试验id在参数表中有没有对应的参数，如果没有就插入
        if(isEmptyPara(expParameter)){
            //map形式存储特征数据
            expParameterMapper.insertInMap(expParameter, map);
        }else{
            //否则更新该试验的所有参数
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, String> entry = it.next();
                expParameter.setFeatureName(entry.getKey());
                expParameter.setFeatureValue(entry.getValue());
                expParameterMapper.update(expParameter);
            }
        }
    }


    public boolean isEmptyPara(ExpParameter expParameter){
        List<ExpParameter> list = expParameterMapper.findExpParameterList(expParameter);
        if(list.size()==0){
            return true;
        }
        return false;
    }

    public ExpParameter getExpParameter(Experiment experiment){
        ExpParameter expParameter = new ExpParameter();
        expParameter.setExperiment(experiment);
        List<Map<String,String>> expParaMap = expParameterMapper.findExpParameterMap(expParameter);
        //mybatis的ListMap转map
        Map<String,String> map = listm2map.listMap2Map(expParaMap);
        expParameter.setFeature(map);

        return expParameter;
    }
    
    public ExpParameter findExpParameter(ExpParameter expParameter){
    	//获取ExpParameterList，其中每个ExpParameter的featurename和featurevalue都是不同值
    	List<ExpParameter> findExpParameterList = expParameterMapper.findExpParameterList(expParameter);
    	System.out.println(findExpParameterList);
    	
    	//将所有参数放入map中
    	Map<String,String> map = new HashMap<String, String>();
    	for (ExpParameter expParameterVO : findExpParameterList) {
			map.put(expParameterVO.getFeatureName(), expParameterVO.getFeatureValue());
		}
    	if(map.containsKey("LP_Predict")){
    		expParameter.setLP_Predict(map.get("LP_Predict"));
    	}
    	if(map.containsKey("LP_KnownResponse")){
    		expParameter.setLP_KnownResponse(map.get("LP_KnownResponse"));
    	}
    	if(map.containsKey("LP_Source")){
    		expParameter.setLP_Source(map.get("LP_Source"));
    	}
    	expParameter.setAlgorithm(map.get("algorithm"));
    	expParameter.setP2Predict(map.get("p2Predict"));
    	expParameter.setFreqRangeMax(map.get("freqRangeMax"));
    	expParameter.setFreqRangeMin(map.get("freqRangeMin"));
    	expParameter.setSamplingFreq(map.get("samplingFreq"));
    	return expParameter;
    }
}
