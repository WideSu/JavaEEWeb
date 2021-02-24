package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.entity.ExpResult;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.mapper.entity.UploadedData;
import org.hqu.vibsignal_analysis.service.ExpResultService;
import org.hqu.vibsignal_analysis.service.ExperimentService;
import org.hqu.vibsignal_analysis.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class GetDataListController {
    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private ExpResultService expResultService;

    @RequestMapping("/getUploadedDataList")
    public @ResponseBody Page getUploadedDataList(HttpServletRequest request,int page, int rows, @RequestParam("expClass") String expClass){
        HttpSession session = request.getSession();
        session.setAttribute("expClass", expClass);
        List<UploadedData> uploadedDataList = experimentService.getDataList(session);
        List<UploadedData> tempList = new ArrayList<>();
        int total = uploadedDataList.size();
        int pageSize = total/rows + 1;
        if(pageSize==1){
            tempList = uploadedDataList;
        }else{
            for(int i=(page-1)*rows; i<page*rows && i<total; i++){
                tempList.add(uploadedDataList.get(i));
            }
        }
        Page resultPage = new Page();
        resultPage.setRows(tempList);
        resultPage.setTotal(total);

        return resultPage;
    }


    @RequestMapping("/getExpList")
    @ResponseBody
    public Page getExpList(@RequestParam("expClass") String expClass, int page, int rows, HttpServletRequest request){
    	HttpSession session = request.getSession();
    	expClass = expClass.split("\\.")[0];

        Page resultPage = experimentService.getListwithPageInfo(page,rows,expClass,session);
        return resultPage;
    }

    @RequestMapping("/getCreatedExpList")
    @ResponseBody
    public Page getCreatedExpList(@RequestParam("expClass") String expClass, int page, int rows, HttpServletRequest request){
        HttpSession session = request.getSession();
        expClass = expClass.split("\\.")[0];
        Page resultPage = experimentService.getCreatedListwithPageInfo(page,rows,expClass,session);
        return resultPage;
    }

    @RequestMapping("/getPredictedDataList")
    public @ResponseBody List<ExpResult> getResultDataList(@RequestParam("expId") String expId){
        ExpResult expResult = new ExpResult();
        Experiment experiment = new Experiment();
        experiment.setExpId(expId);
        expResult.setExperiment(experiment);
        List<ExpResult> expResultList = expResultService.getExpResultDataList(expResult);
        return expResultList;
    }
    
	@RequestMapping("/getLpUploadedDataList")
	@ResponseBody
	public Page getLpUploadedDataList(HttpServletRequest request,int page, int rows, @RequestParam("expClass") String expClass){
        HttpSession session = request.getSession();
        session.setAttribute("expClass", expClass);
		List<UploadedData> uploadedDatas = experimentService.getLRDataList(session);
		Iterator<UploadedData> it = uploadedDatas.iterator();
		while(it.hasNext()){
			UploadedData uploadedData = it.next();
			if(uploadedData ==null){}
			else{
			if(null == uploadedData.getLp_KnownResponse() || "".equals(uploadedData.getLp_KnownResponse())){
				it.remove();
			}}
		}
		List<UploadedData> tempList = new ArrayList<>();
        int total = uploadedDatas.size();
        int pageSize = total/rows + 1;
        if(pageSize==1){
            tempList = uploadedDatas;
        }else{
            for(int i=(page-1)*rows; i<page*rows && i<total; i++){
                tempList.add(uploadedDatas.get(i));
            }
        }
        Page resultPage = new Page();
        resultPage.setRows(tempList);
        resultPage.setTotal(total);
		return resultPage;
	}

    @RequestMapping("/findDataIndex")
    @ResponseBody
    public String getDataIndex(@RequestParam("dataId") String dataId){
        ExpResult expResult = new ExpResult();
        Experiment experiment = new Experiment();
        experiment.setExpId(dataId);
        expResult.setExperiment(experiment);
        String dataIndex = expResultService.getDataIndex(expResult);
        return dataIndex;
    }
}
