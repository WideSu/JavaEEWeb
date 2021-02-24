package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.entity.ExpParameter;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.service.ExperimentService;
import org.hqu.vibsignal_analysis.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class ExperimentParamController {
	
	@Autowired
	private ExperimentService experimentService;

	@RequestMapping("showExpParaInfo")
	public String find() {
		return "maintenance/expParam_list";
	}

	@RequestMapping(value="/expPs",method=RequestMethod.POST)
	@ResponseBody
	//Annie试验参数表查询
	public Page getAllExpParamList(Integer page, Integer rows, HttpSession session)throws Exception {
		String currentUser = (String)session.getAttribute("userId");
		Experiment experiment = new Experiment();
		experiment.setUserId(currentUser);
		ExpParameter expParameter = new ExpParameter();
		experiment.setExpParameter(expParameter);
		Page resultPage = experimentService.getExpPList(page, rows, experiment);
		return resultPage;
	}

	@RequestMapping(value="/expP/search",method=RequestMethod.POST)
	@ResponseBody
	//Annie实验参数表查询
	public Page searchExpP(Integer page, int rows, @RequestParam("expName")String expName, @RequestParam("expClass")String expClass, @RequestParam("algorithm")String algorithm, HttpSession session)throws Exception {
		String currentUser = (String)session.getAttribute("userId");
		Experiment experiment = new Experiment();
		experiment.setUserId(currentUser);
		experiment.setExpName(expName);
		experiment.setExpClass(expClass);
		experiment.setAlgorithm(algorithm);
		ExpParameter expParameter = new ExpParameter();
		expParameter.setAlgorithm(algorithm);
		experiment.setExpParameter(expParameter);
		Page resultPage = experimentService.searchExpPList(page,rows,experiment);
		return resultPage;
	}
}


