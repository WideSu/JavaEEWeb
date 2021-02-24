package org.hqu.vibsignal_analysis.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hqu.vibsignal_analysis.mapper.DataStorageMapper;
import org.hqu.vibsignal_analysis.mapper.ExperimentMapper;
import org.hqu.vibsignal_analysis.mapper.entity.*;
import org.hqu.vibsignal_analysis.util.CodeGen;
import org.hqu.vibsignal_analysis.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class ExperimentService {
    @Autowired
    private ExperimentMapper experimentMapper;
    @Autowired
    private ExpParameterService expParameterService;
    @Autowired
    private DataStorageService dataStorageService;
    @Autowired
    private ExpResultService expResultService;
    @Autowired
    private DataStorageMapper dataStorageMapper;

    public Page getListwithPageInfo(int page, int rows, String expClass, HttpSession session){
    	Experiment experiment = new Experiment();
    	experiment.setExpClass(expClass);
    	experiment.setUserId((String)session.getAttribute("userId"));
    	//创建分页对象Pagehelper
        PageHelper.startPage(page,rows);
        List<Experiment> experimentList = experimentMapper.getExpList(experiment);
       // System.out.println(experimentList.get(0).getExpId());
        //将PageInfo对象中的转化成自定义Page对象，符合带有分页信息的datagrid的输入要求。
        PageInfo<Experiment> pageInfo = new PageInfo<>(experimentList);
        Page resultPage = new Page();
        resultPage.setRows(experimentList);
        resultPage.setTotal(pageInfo.getTotal());

        return resultPage;
    }

    public Page getCreatedListwithPageInfo(int page, int rows, String expClass, HttpSession session){
        Experiment experiment = new Experiment();
        experiment.setExpClass(expClass);
        experiment.setUserId((String)session.getAttribute("userId"));
        //创建分页对象Pagehelper
        PageHelper.startPage(page,rows);
        List<Experiment> experimentList = experimentMapper.getCreatedExpList(experiment);
        // System.out.println(experimentList.get(0).getExpId());
        //将PageInfo对象中的转化成自定义Page对象，符合带有分页信息的datagrid的输入要求。
        PageInfo<Experiment> pageInfo = new PageInfo<>(experimentList);
        Page resultPage = new Page();
        resultPage.setRows(experimentList);
        resultPage.setTotal(pageInfo.getTotal());

        return resultPage;
    }


    public Experiment save(Experiment experiment){
        try {
            //如果试验是新创建的
            if(experiment.getExpId()==null) {
                experiment.setUUID(java.util.UUID.randomUUID().toString());
                experiment.setExpId(CodeGen.genExpId(experiment.getExpClass()));
                experiment.setExpCreateDate(new Date());
                experiment.setExpUpdateDate(new Date());
                experiment.setExpState("1");
                experimentMapper.insert(experiment);
                return experiment;
            }else{
                experiment.setExpUpdateDate(new Date());
                if(getExp(experiment)==null){
                    experimentMapper.insert(experiment);
                }else{
                    experimentMapper.update(experiment);
                }
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    //i did it!! 22:50 2019-04-08
    public List<UploadedData> getDataList(HttpSession session){
        DataStorage data = new DataStorage();
        data.setUserId((String)session.getAttribute("userId"));
        data = dataStorageService.setDataApplicationRange(data,session);
        List<UploadedData> list = experimentMapper.getUploadedDataList(data);
        if(list.size()>0) {
            Iterator<UploadedData> it = list.iterator();
            UploadedData first = it.next();
            while (it.hasNext()) {
                UploadedData temp = it.next();
                if (first.getDataId().equals(temp.getDataId())) {
                    first.setP2Predict(first.getP2Predict() + "," + temp.getP2Predict());
                    it.remove();
                } else {
                    first = temp;
                }
            }
        }
        return list;
    }
    
    public List<UploadedData> getLRDataList(HttpSession session){
        DataStorage data = new DataStorage();
        data.setUserId((String)session.getAttribute("userId"));
        data = dataStorageService.setDataApplicationRange(data,session);
        List<UploadedData> list = experimentMapper.getLpUploadedDataList(data);
        if(list.size()>0) {
            Iterator<UploadedData> it = list.iterator();
            UploadedData first = it.next();
            while (it.hasNext()) {
                UploadedData temp = it.next();
                if (first.getDataId().equals(temp.getDataId())) {
                    first.setP2Predict(first.getP2Predict() + "," + temp.getP2Predict());
                    it.remove();
                } else {
                    first = temp;
                }
            }
        }
       return list;
    }


    public Experiment getExp(Experiment experiment){
        //因为expId在表中不唯一，因此选择查询同一个试验id的多个相同的试验（授权Id不同，其余相同），然后取第一个
        List<Experiment> expList = experimentMapper.getExpriment(experiment);
        if(expList.size()!=0){
            Experiment exp = expList.get(0);
            ExpParameter expParameter = expParameterService.getExpParameter(experiment);
            exp.setExpParameter(expParameter);

            ExpResult expResult = new ExpResult();
            expResult.setExperiment(exp);
            List<ExpResult> expResultList = expResultService.getExpResultList(expResult);
            exp.setExpResult(expResultList);

            if(exp.getData()!=null){
                DataStorage dataStorage = exp.getData();
                List<DataStorage> dataList = dataStorageMapper.findList(dataStorage);
                exp.setDataStorageList(dataList);
            }

            return exp;
        }else{
            return null;
        }
    }

    public void insert(Experiment experiment){
        String uuid = java.util.UUID.randomUUID().toString();
        experiment.setUUID(uuid);
        experimentMapper.insert(experiment);
    }

    public void insertAllRelatedInfo(Experiment experiment){
        insert(experiment);
        //参数表插入
        expParameterService.savePara(experiment.getExpParameter());
        //结果表插入
        List<ExpResult> expResultList = experiment.getExpResult();
        if(expResultList!=null){
            for(ExpResult expResult : expResultList){
                expResultService.insert(expResult);
            }
        }

        //数据表插入
        List<DataStorage> dataStorageList = experiment.getDataStorageList();
        if(dataStorageList!=null){
            for(DataStorage data : dataStorageList){
                //uuid在entity中创建
                dataStorageMapper.insert(data);
            }
        }
    }

    //多表将关联删除
    public void delete(List<Experiment> expList){
        for(Experiment exp : expList){
            experimentMapper.delete(exp);
        }
    }
    
    
    public List<Experiment> getExpList(Experiment experiment){
        return experimentMapper.getExpList(experiment);
    }

    public void updateExpname(String expId, String expName) {
        // Annie按照expId更新expName
        Experiment experiment = new Experiment();
        experiment.setExpId(expId);
        experiment.setExpName(expName);
        experimentMapper.updateExpname(experiment);
    }

    public Page getList(Integer page, int rows, Experiment experiment){
        //Annie
        PageHelper.startPage(page, rows);
        List<Experiment> experimentList = experimentMapper.queryExpList(experiment);
        PageInfo<Experiment> pageInfo = new PageInfo<>(experimentList);
        Page resultPage = new Page();
        resultPage.setRows(experimentList);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }

    public void deleteJointly(String expId) {
        // Annie按照expId多表关联删除
        Experiment experiment = new Experiment();
        experiment.setExpId(expId);
        experimentMapper.delete(experiment);
    }

    // Annie
    public Page getExpPList(Integer page, Integer rows, Experiment experiment) {
        PageHelper.startPage(page, rows);
        List<Experiment> experimentList = experimentMapper.getExpPList(experiment);
        PageInfo<Experiment> pageInfo = new PageInfo<>(experimentList);
        Page resultPage = new Page();
        resultPage.setRows(experimentList);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }

    public Page searchExpList(Integer page, int rows, Experiment experiment) {
        PageHelper.startPage(page, rows);
        List<Experiment> experimentList = experimentMapper.queryExpList(experiment);
        PageInfo<Experiment> pageInfo = new PageInfo<>(experimentList);
        Page resultPage = new Page();
        resultPage.setRows(experimentList);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }

    public Page searchExpPList(Integer page, int rows, Experiment experiment) {
        PageHelper.startPage(page, rows);
        List<Experiment> experimentList = experimentMapper.getExpPList(experiment);
        PageInfo<Experiment> pageInfo = new PageInfo<>(experimentList);
        Page resultPage = new Page();
        resultPage.setRows(experimentList);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }

    public Page getAllExpPList(Integer page, Integer rows) {
        // Annie查出所有实验参数信息
        PageHelper.startPage(page, rows);
        List<Experiment> experimentList = experimentMapper.allExpP();
        PageInfo<Experiment> pageInfo = new PageInfo<>(experimentList);
        Page resultPage = new Page();
        resultPage.setRows(experimentList);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }

    public Page getExpListWithPageInfo(Integer page, int rows, String expClass) {
        // 创建分页对象Pagehelper
        PageHelper.startPage(page, rows);
        List<Experiment> experimentList = experimentMapper.getExpListWithAlgoUserName(expClass);
        //将PageInfo对象中的转化为自定义的Page对象，符合带有分页信息的datagrid的要求
        PageInfo<Experiment> pageInfo = new PageInfo<>(experimentList);
        Page resultPage = new Page();
        resultPage.setRows(experimentList);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }


}
