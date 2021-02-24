package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.service.ClusterService;
import org.hqu.vibsignal_analysis.util.algorithm.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class ClusterController {
	
	@Autowired
	ClusterService clusterService;

	@RequestMapping("getSerial")
	@ResponseBody
	public LinkedHashMap<String,List<float[]>> getSerial(String algorithm, HttpServletRequest request){
		HttpSession session = request.getSession();
		List<Cluster> clusters = (List<Cluster>) session.getAttribute(algorithm);
		LinkedHashMap<String, List<float[]>> serial = clusterService.getSerial(clusters);
		return serial;
	}
}
