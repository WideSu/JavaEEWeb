package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.entity.DataStorage;
import org.hqu.vibsignal_analysis.service.DataStorageService;
import org.hqu.vibsignal_analysis.util.FileDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

@Controller
public class FileRecieveController {
    @Autowired
    DataStorageService dataStorageService;

    //Pdata, Tdata, Udata
    @RequestMapping(value = "/uploadData", method = RequestMethod.POST)
    @ResponseBody
    public String uploadData4(@RequestParam String param1, @RequestParam (required = false)String param2,
                              @RequestParam String expId, @RequestParam (required = false)String dataId,
                              @RequestParam (required = false)String expClass,
                              HttpServletRequest request, Model model)throws IOException {
    	HttpSession session = request.getSession();
        session.setAttribute("expClass", expClass);
        List<DataStorage> dataList = dataStorageService.save(param1,param2,"",dataId, expId, request);
        Iterator<DataStorage> it = dataList.iterator();
        while(it.hasNext()){
            DataStorage data = it.next();
            dataStorageService.addInfo(data,session);
            data.setDataId(dataId);
        }
        return dataStorageService.saveDataList(dataList);
    }


    @RequestMapping("/downloadFile")
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam("path") String path , HttpServletRequest request) throws IOException {
        FileDownload fileDownload = new FileDownload();
        return fileDownload.download(path);
    }
    
    //进行数据的更新操作
    @RequestMapping("/updateData")
    @ResponseBody
    public String updateData(@RequestParam("dataId") String dataId,@RequestParam("selectDataId") String selectDataId) throws ParseException{
    //	System.out.println(selectDataId);
    	return dataStorageService.updateData(dataId,selectDataId);
    }

}
