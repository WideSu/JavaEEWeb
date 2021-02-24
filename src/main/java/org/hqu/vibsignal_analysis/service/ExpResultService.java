package org.hqu.vibsignal_analysis.service;

import org.hqu.vibsignal_analysis.mapper.ExpResultMapper;
import org.hqu.vibsignal_analysis.mapper.entity.ExpResult;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ExpResultService {
    @Autowired
    private ExpResultMapper expResultMapper;

    public void saveResult(ExpResult expResult){
        List<ExpResult> list = expResultMapper.findExpResultList(expResult);
        if(list.size()!=0){
            expResultMapper.update(expResult);
        }else{
        	//首先判读是否为目录
        	String path = expResult.getResultIndex();
        	File file = new File(path);
        	if(file.isDirectory()){
        		//如果resultIndex是目录 设置resultDataType为3
        		expResult.setResultDataType("3");
        	}
            //根据格式后缀判断结果类型
        	else{
	            //根据格式后缀判断结果类型
                if(expResult.getResultDataType()!=null){
                    String temp = expResult.getResultMeaning().split("\\.")[1];
                    if("png".equals(temp)){
                        expResult.setResultDataType("1");
                    }else{
                        expResult.setResultDataType("2");
                    }
                }
        	}
            if(expResult.getExpResultId()==null){
                try{
                    expResult.setExpResultId(UUID.randomUUID().toString());
                    expResult.setResultCreateDate(new Date());
                }catch(ParseException e){
                    e.printStackTrace();
                }
            }
            expResultMapper.insert(expResult);
        }
    }

    public void saveResult(Experiment experiment, Map<String,String> map) throws ParseException{
        for(String key : map.keySet()){
            if(key.equals("picName")){
                ExpResult expTempResult = new ExpResult();
                expTempResult.setExperiment(experiment);
                expTempResult.setResultMeaning(map.get("picName"));
                expTempResult.setResultIndex(map.get("picPath"));
                File file = new File(expTempResult.getResultIndex());
                if(file.isDirectory()){
                	expTempResult.setResultDataType("3");
                }else{
                    expTempResult.setResultDataType("1");
                }
                expTempResult.setResultCreateDate(new Date());
                saveResult(expTempResult);
            }else if(key.equals("dataName")){
                ExpResult expTempResult = new ExpResult();
                expTempResult.setExperiment(experiment);
                expTempResult.setResultMeaning(map.get("dataName"));
                expTempResult.setResultIndex(map.get("dataPath"));
                expTempResult.setResultDataType("2");
                expTempResult.setResultCreateDate(new Date());
                saveResult(expTempResult);
            }
        }
    }

    public List<ExpResult> getExpResultDataList(ExpResult expResult){
        //2是数据
        expResult.setResultDataType("2");
        List<ExpResult> list = expResultMapper.findExpResultList(expResult);
        return list;
    }

    public List<ExpResult> getExpResultList(ExpResult expResult){
        List<ExpResult> list = expResultMapper.findExpResultList(expResult);
        return list;
    }

    public String getDataIndex(ExpResult expResult){
        List<ExpResult> list = expResultMapper.findExpResultList(expResult);
        if(list.size()!=0){
            expResult = list.get(0);
            return expResult.getResultIndex();
        }else{
            return "";
        }
    }

    public List<ExpResult> getExpResultPicList(ExpResult expResult){
        expResult.setResultDataType("1");
        List<ExpResult> list = expResultMapper.findExpResultList(expResult);
        return list;
    }

    public void delete(ExpResult expResult){
        expResultMapper.delete(expResult);
    }

    public void insert(ExpResult expResult){
        expResult.setExpResultId(UUID.randomUUID().toString());
        expResultMapper.insert(expResult);
    }
}
