package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.entity.DataStorage;
import org.hqu.vibsignal_analysis.service.DataStorageService;
import org.hqu.vibsignal_analysis.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class DataStorageController {

	@Autowired
	private DataStorageService dataStorageService;

	@RequestMapping("/showDataInfo")
	public String getDataSList() {
		return "maintenance/dataStor_list";
	}

	@RequestMapping("/datStrs")
	@ResponseBody
	//Annie查询所有数据表
	public Page getAllDataList(Integer page, int rows, HttpSession session) {
		String currentUser = (String)session.getAttribute("userId");
		DataStorage datastorage = new DataStorage();
		datastorage.setUserId(currentUser);
		Page resultPage = dataStorageService.getList(page, rows, datastorage);
		return resultPage;
	}

	@RequestMapping("/datStrs/search")
	@ResponseBody
	//Annie根据查询条件查询数据表
	public Page searchDataSList(Integer page, int rows, @RequestParam("dataName")String dataName, @RequestParam("dARange")String dARange, HttpSession session)throws Exception {
		String currentUser = (String)session.getAttribute("userId");
		DataStorage datastorage = new DataStorage();
		datastorage.setDataName(dataName);
		datastorage.setDataApplicationRange(dARange);
		datastorage.setUserId(currentUser);
		Page resultPage = dataStorageService.getList(page,rows,datastorage);
		return resultPage;
	}

	@RequestMapping(value="/datStr/update", method= {RequestMethod.POST})
	@ResponseBody
	//Annie更新数据表
	public String updateDataSList(@RequestParam("dataId")String dataId,@RequestParam("dataName")String dataName)throws Exception {
		dataStorageService.updateDataName(dataId,dataName);
		return "true";
	}


	@RequestMapping(value="/datStr/delete", method= {RequestMethod.POST})
	@ResponseBody
	//Annie按照主键联表删除某试验
	public boolean delDataList(String[] dataId, HttpSession session){
		List<DataStorage> dataList = new ArrayList<>();
		String currentUser = (String)session.getAttribute("userId");
		try {
			for (String id : dataId) {
				DataStorage dataStorage = new DataStorage();
				dataStorage.setDataId(id);
				dataStorage.setUserId(currentUser);
				dataList.add(dataStorage);
				dataStorageService.delete(dataList);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
