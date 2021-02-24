package org.hqu.vibsignal_analysis.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hqu.vibsignal_analysis.mapper.DataStorageMapper;
import org.hqu.vibsignal_analysis.mapper.ExpResultMapper;
import org.hqu.vibsignal_analysis.mapper.ExperimentMapper;
import org.hqu.vibsignal_analysis.mapper.entity.DataStorage;
import org.hqu.vibsignal_analysis.mapper.entity.ExpParameter;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.mapper.entity.User;
import org.hqu.vibsignal_analysis.service.ExpParameterService;
import org.hqu.vibsignal_analysis.service.ExperimentService;
import org.hqu.vibsignal_analysis.service.UserService;
import org.hqu.vibsignal_analysis.service.congfiguration.ExpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class FirstController {
	private static final Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	private ExperimentMapper experimentMapper;
	@Autowired
	private ExpParameterService expParameterService;
	@Autowired
	private ExpConfig expConfig;
	@Autowired
	private ExpResultMapper expResultMapper;
	@Autowired
	private ExperimentService experimentService;
	@Autowired
	private UserService userService;
	@Autowired
	private DataStorageMapper dataStorageMapper;

	//主页面
	@RequestMapping("/")
	public String home(Model model, HttpSession session){
		//跳转登录的时候清空session
		session.invalidate();
		User user = new User();
		model.addAttribute("user",user);
		return "index";
	}

	//注册页面
	@RequestMapping("/register")
	public String register(Model model){
		User user = new User();
		model.addAttribute("user",user);
		return "register";
	}

	//模型精度测试新建试验页面
	@RequestMapping("/uLPMExpAdd")
	public String first(@RequestParam("expName") String expName, Experiment experiment, Model model, HttpSession session){
		experiment.setExpName(expName);
		//unknown load point predict model
		experiment.setExpClass("ULPM");
		addExp(experiment, model,session);
		return "addExp";
	}

	//未知测点预测新建试验页面
	@RequestMapping("/uLPExpAdd")
	public String uPPExpAdd(@RequestParam("expName") String expName, Experiment experiment, Model model, HttpSession session){
		experiment.setExpName(expName);
		//unknown load point predict
		experiment.setExpClass("ULP");
		addExp(experiment, model,session);
		return "addRealExp";
	}

	//已知载荷响应预测模型测试新建试验页面
	@RequestMapping("/kLPMExpAdd")
	public String kLPMExpAdd(@RequestParam("expName") String expName, Experiment experiment, Model model, HttpSession session){
		experiment.setExpName(expName);
		experiment.setExpClass("KLPM");
		addExp(experiment, model,session);
		return "addKLPMExp";
	}

	//已知载荷响应预测新建试验页面
	@RequestMapping("/kLPExpAdd")
	public String kLPExpAdd(@RequestParam("expName") String expName, Experiment experiment, Model model, HttpSession session){
		experiment.setExpName(expName);
		experiment.setExpClass("KLP");
		addExp(experiment, model,session);
		return "addKLPExp";
	}

	//层次聚类(+)
	@RequestMapping("/tsCorAnalysisPos")
	public String tsCorAnalysisPos(Model model, HttpSession session){
		return "clustering/tSCorAnalysisPos";
	}

	//层次聚类(-)
	@RequestMapping("/tsCorAnalysisNeg")
	public String tsCorAnalysisNeg(Model model, HttpSession session){
		return "clustering/tSCorAnalysisNeg";
	}

	//密度聚类(+)
	@RequestMapping("/tsCorAnalysisDPos")
	public String tsCorAnalysisDPos(Model model, HttpSession session){
		return "clustering/negative/tSCorAnalysisDPos";
	}

	//密度聚类(-)
	@RequestMapping("/tsCorAnalysisDNeg")
	public String tsCorAnalysisDNeg(Model model, HttpSession session){
		return "clustering/negative/tSCorAnalysisDNeg";
	}

	//VDSI
	@RequestMapping("/VDSI")
	public String VDSI(Model model, HttpSession session){
		return "clustering/tSCorAnalysisVDSI";
	}

	@RequestMapping("/openULPMExp")
	public String openULPMExp(@RequestParam("expId") String expId, Experiment experiment, Model model){
		experiment.setExpId(expId);
		experiment = experimentService.getExp(experiment);
		model.addAttribute("experiment",experiment);
		return "addExp";
	}

	@RequestMapping("/openULPExp")
	public String openULPExp(@RequestParam("expId") String expId, Experiment experiment, Model model){
		experiment.setExpId(expId);
		experiment = experimentService.getExp(experiment);
		model.addAttribute("experiment",experiment);
		return "addRealExp";
	}

	@RequestMapping("/openKLPMExp")
	public String openKLPMExp(@RequestParam("expId") String expId, Experiment experiment, Model model){
		experiment.setExpId(expId);
		experiment = experimentService.getExp(experiment);
		model.addAttribute("experiment",experiment);
		return "addKLPMExp";
	}

	//openKLPExp
	@RequestMapping("/openKLPExp")
	public String openKLPExp(@RequestParam("expId") String expId, Experiment experiment, Model model){
		experiment.setExpId(expId);
		experiment = experimentService.getExp(experiment);
		model.addAttribute("experiment",experiment);
		return "addKLPExp";
	}

	//保存试验
	@RequestMapping("/save")
	@ResponseBody
	public String save(Experiment experiment, Model model){
		try{
			if(experiment.getData()!=null){
				String dataId = experiment.getData().getDataId();
				if(dataId!=null&&dataId.length()>0){
					if(!dataExist(dataId)){
						return "dataNotFound";
					}
				}
			}
			ExpParameter para = experiment.getExpParameter();
			para.setExperiment(experiment);
			experiment.setExpParameter(para);
			experimentService.save(experiment);
			expParameterService.savePara(para);
			model.addAttribute("experiment",experiment);
			return "true";
		}catch(Exception e){
			e.printStackTrace();
			return "false";
		}
	}

	//删除试验
	@RequestMapping("/delExpList")
	@ResponseBody
	public boolean delExpList(String[] expId, HttpSession session){
		List<Experiment> expList = new ArrayList<>();
		Experiment experiment = new Experiment();
		String userId = (String)session.getAttribute("userId");
		experiment.setUserId(userId);
		try {
			for (String id : expId) {
				experiment.setExpId(id);
				expList.add(experiment);
				experimentService.delete(expList);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void addExp(Experiment experiment, Model model, HttpSession session){
		if(experiment.getExpId()!=null){
			model.addAttribute("experiment",experiment);
		}else {
			String userId = (String)session.getAttribute("userId");
			experiment.setUserId(userId);
			ExpParameter para = new ExpParameter();
			para.setExperiment(experiment);
			experiment.setExpParameter(para);
			experimentService.save(experiment);
			expParameterService.savePara(para);
			model.addAttribute("experiment", experiment);
		}
	}

	@RequestMapping(value ="/showUserInfo")
	public String find(Model model,HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		User user = new User();
		user.setUserId(userId);
		//展示用户表信息
		user = userService.getUser(user);
		logger.info(user);
		model.addAttribute("user", user);
		return "maintenance/currentUser";
	}

	@RequestMapping("/user/check")
	@ResponseBody
	public String getItemByName(@RequestParam("userName") String userName){
		User user = new User();
		user.setUserName(userName);
		User user1 = userService.getByName(user);
		if(user1 == null) return "true";
		else return "false";
	}

	/*@RequestMapping("/update")
	@ResponseBody
	private void update(User user) throws Exception{
		userService.update(user);
	}*/

	@RequestMapping(value="/user/updateUserName", method=RequestMethod.POST)
	@ResponseBody
	private User updateUserName(User user,Model model) throws Exception{
		User userNameUpdated = new User();
		userNameUpdated.setUserId(user.getUserId());
		userNameUpdated = userService.getUser(userNameUpdated);
		userNameUpdated.setUserName(user.getUserName());
		userService.update(userNameUpdated);
		user = userService.getUser(userNameUpdated);
		model.addAttribute("user",user);
		return user;
	}

	@RequestMapping(value="/user/updateUserPassword", method=RequestMethod.POST)
	@ResponseBody
	private User updateUserPassword(User user,Model model) throws Exception{
		User userPwUpdated = new User();
		userPwUpdated.setUserId(user.getUserId());
		userPwUpdated = userService.getUser(userPwUpdated);
		userPwUpdated.setUserPassword(user.getUserPassword());
		userService.update(userPwUpdated);
		user = userService.getUser(userPwUpdated);
		model.addAttribute("user",user);
		return user;
	}

	/*
    @RequestMapping(value="/update", method=RequestMethod.POST)
    @ResponseBody
    private void update(User user, Model model) throws Exception{
        User user = user.getUserName;

        userService.update(user);
    }
    */

	public boolean dataExist(String dataId){
		DataStorage data = new DataStorage();
		data.setDataId(dataId);
		if(dataStorageMapper.findList(data).size()!=0){
			return true;
		}else{
			return false;
		}
	}

}	
