package org.hqu.vibsignal_analysis.controller;


import org.hqu.vibsignal_analysis.mapper.ExpResultMapper;
import org.hqu.vibsignal_analysis.mapper.ExperimentMapper;
import org.hqu.vibsignal_analysis.mapper.entity.ExpResult;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

/**
 * @author Yyb
 *	version 1.0 2019-3-28
 */

@Controller
public class TextDownController {
	@Autowired
	private ExpResultMapper expResultMapper;
	@Autowired
	private ExperimentMapper experimentMapper;
	//获取到expResult表内datatype为2的expResult 使用expId搜索，返回到dataTable中
	@RequestMapping("/GetDataTable")
	@ResponseBody
	public List<ExpResult> getDataTable(String expId,HttpSession session){
		ExpResult expResult = new ExpResult();
		Experiment experiment =new Experiment();
		experiment.setExpId(expId);
		experiment.setUserId((String)session.getAttribute("userId"));
		List<Experiment> experiments = experimentMapper.getExperimentsByExpId(experiment);
		experiment =experiments.get(0);
		expResult.setExperiment(experiment);
		List<ExpResult> expResults = expResultMapper.findExpResultData(expResult);
		for(int i = 0;i<expResults.size();i++){
			ExpResult expRes = expResults.get(i);
			expRes.setExperiment(experiment);
		}
		return expResults;
		
	}
	
	//文件下载，使用springmvc框架的ResponseEntity
	@RequestMapping("/downloadText")
	public ResponseEntity<byte[]> download(@RequestParam("expResultId") String expResultId ,HttpServletRequest request) throws IOException {
		
		ExpResult expResult = 	new ExpResult();
		expResult.setExpResultId(expResultId);
		expResult.setResultDataType("2");
		expResult = expResultMapper.findExpResultByResultId(expResult);
		String path=expResult.getResultIndex();
		String fileName=expResult.getResultMeaning();
        System.out.println(path);
        
        
        File f=new File(path);
        InputStream in;
        ResponseEntity<byte[]> response=null ;
        try {
            in = new FileInputStream(f);
            byte[] b=new byte[in.available()];
            in.read(b);
            HttpHeaders headers = new HttpHeaders();
            fileName = new String(fileName.getBytes("gbk"),"iso8859-1");
            headers.add("Content-Disposition", "attachment;filename="+fileName);
            HttpStatus statusCode=HttpStatus.OK;
            response = new ResponseEntity<byte[]>(b, headers, statusCode);  
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;

	}
		
	
}
