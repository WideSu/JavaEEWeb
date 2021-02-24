package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.ExperimentMapper;
import org.hqu.vibsignal_analysis.mapper.entity.ExpParameter;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.service.ExpParameterService;
import org.hqu.vibsignal_analysis.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Yyb
 * @version 1.0 2019-3-22
 * **/
@Controller
public class LR_Controller {
	@Autowired
	ExperimentService experimentService;
	@Autowired
	private ExperimentMapper experimentMapper;
	@Autowired
	private ExpParameterService expParameterService;
	@Autowired
	private FirstController firstController;
	@RequestMapping("/addLrExp")
	public String open(@RequestParam("expName") String expName,Experiment experiment, Model model,HttpSession session) throws Exception{
		experiment.setExpName(expName);
		experiment.setExpClass("UKLT");
		firstController.addExp(experiment, model,session);
			return "addExp_Load";
		
		
	}
	@RequestMapping("/addLrpExp")
	public String openLrp(@RequestParam("expName") String expName,Experiment experiment, Model model,HttpSession session){
		experiment.setExpName(expName);
		experiment.setExpClass("UKLP");
		firstController.addExp(experiment, model,session);
		return "addExp_LoadPred";
	}
	
	
	
	
	
	//获取exparameter 返回到试验界面
	@RequestMapping("/getExpByExpId")
	public String getExpByExpId(@RequestParam("expId")String expId,Model model, HttpSession session){
		Experiment experiment = getExp(expId,session);
		model.addAttribute("experiment", experiment);
		//用于控制查看图片按钮的开启
		model.addAttribute("OpenPic","1");
		
		if("UKLT".equals(experiment.getExpClass())){
			return "addExp_Load";}
		else if("UKLP".equals(experiment.getExpClass())){
			return "addExp_LoadPred";
		}
		else{
			return "home";
		}		
	}

	public void addExp(Experiment experiment, Model model, HttpSession session){
		if(experiment.getExpId()!=null){
			model.addAttribute("experiment",experiment);
		}else {
			String userId = (String)session.getAttribute("userId");
			experiment.setUserId(userId);
			experimentService.save(experiment);
			model.addAttribute("experiment", experiment);
		}
	}
	
	public Experiment getExp(String expId,HttpSession session){
		
		ExpParameter expParameter = new ExpParameter();
		Experiment experiment =new Experiment();
		//将前台传来的expId放入experiment中，再将该experiment放入exparameter中
		experiment.setExpId(expId);
		experiment.setUserId((String)session.getAttribute("userId"));
		expParameter.setExperiment(experiment);
		//通过expId获取相应参数
		expParameter = expParameterService.findExpParameter(expParameter);
		List<Experiment> experiments = experimentMapper.getExperimentsByExpId(experiment);
		experiment =experiments.get(0);
		experiment.setExpParameter(expParameter);
		expParameter.setExperiment(experiment);
		
		return experiment;
	}
	
}
