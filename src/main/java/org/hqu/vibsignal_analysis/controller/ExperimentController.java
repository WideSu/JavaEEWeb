package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.entity.ExpResult;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.service.ExpResultService;
import org.hqu.vibsignal_analysis.service.ExperimentService;
import org.hqu.vibsignal_analysis.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理实验CRUD请求
 * @author lenovo
 *
 */
@Controller
public class ExperimentController {

	@Autowired
	private ExperimentService experimentService;
	@Autowired
	private ExpResultService expResultService;
	
	@RequestMapping("/showExpInfo")
	//Annie跳转到试验表页面
	public String find() {
		return "maintenance/experiment_list";
	}
	
	@RequestMapping("/exp/update")
	@ResponseBody
	//Annie更新试验名
	public String update(@RequestParam("expId")String expId,@RequestParam("expName")String expName) throws Exception{
		//System.out.println("==2=========2========"+expName);
		experimentService.updateExpname(expId, expName);
		return "true";
	}

	@RequestMapping("/exps")
	@ResponseBody
	//Annie
	public Page getAllExpList(Integer page, int rows, HttpSession session) {
		String currentUser = (String)session.getAttribute("userId");
		Experiment experiment = new Experiment();
		experiment.setUserId(currentUser);
		Page resultPage = experimentService.getList(page, rows, experiment);
		return resultPage;
	}

	@RequestMapping("/exp/delete")
	@ResponseBody
	//Annie
	public boolean delExpList(String[] expId, HttpSession session){
		List<Experiment> expList = new ArrayList<>();
		String currentUser = (String)session.getAttribute("userId");
		try {
			for (String id : expId) {
				Experiment experiment = new Experiment();
				experiment.setExpId(id);
				experiment.setUserId(currentUser);
				expList.add(experiment);
				experimentService.delete(expList);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@RequestMapping("/exp/search")
	@ResponseBody
	//Annie
	public Page searchExp(Integer page, int rows, @RequestParam("expName")String expName, @RequestParam("expClass")String expClass, @RequestParam("userName")String userName, @RequestParam("algorithm")String algorithm, HttpSession session)throws Exception {
		String userId = (String) session.getAttribute("userId");
		Experiment experiment = new Experiment();
		experiment.setExpName(expName);
		experiment.setExpClass(expClass);
		experiment.setUserName(userName);
		experiment.setAlgorithm(algorithm);
		experiment.setUserId(userId);
		Page resultPage = experimentService.searchExpList(page,rows,experiment);
		return resultPage;
	}

	//校验是否有相同试验名
	@RequestMapping("/checkExpName")
	@ResponseBody
	public Boolean checkExpName(String expName, @RequestParam(required = false) String userId, HttpSession session){
		if(userId==null||userId.length()==0){
			userId = (String)session.getAttribute("userId");
		}
		Experiment exp = new Experiment();
		exp.setUserId(userId);exp.setExpName(expName);
		List<Experiment> list = experimentService.getExpList(exp);
		if(list.size()>0){
			return false;
		}else{
			return true;
		}
	}

	//校验是否有试验结果
	@RequestMapping("/hasResult")
	@ResponseBody
	public Boolean hasResult(String expId){
		ExpResult expResult = new ExpResult();
		Experiment experiment = new Experiment();
		experiment.setExpId(expId);
		expResult.setExperiment(experiment);
		List<ExpResult> list = expResultService.getExpResultList(expResult);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}

	//删除先前的试验结果
	@RequestMapping("/delPreExpResult")
	@ResponseBody
	public void delPreExpResult(String expId){
		ExpResult expResult = new ExpResult();
		Experiment experiment = new Experiment();
		experiment.setExpId(expId);
		expResult.setExperiment(experiment);
		expResultService.delete(expResult);
	}
}


