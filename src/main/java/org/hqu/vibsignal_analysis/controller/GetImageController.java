package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.mapper.entity.ExpResult;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.service.ExpResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class GetImageController {
    @Autowired
    ExpResultService expResultService;

    @RequestMapping(value = "getImg")
    public String getImg(@RequestParam("expId") String expId, @RequestParam(required = false)Boolean isSwitch, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //取实际的expId
        expId = expId.split("\\.")[0];
        ExpResult expResult = new ExpResult();
        Experiment experiment = new Experiment();
        experiment.setExpId(expId);
        expResult.setExperiment(experiment);
        expResult.setResultDataType("1");
        String path = expResultService.getExpResultPicList(expResult).get(0).getResultIndex();
        if(path!=null){
            if(isSwitch!=null&&isSwitch){
                path = getNaturalPath(path);
            }
            File file = new File(path);
            if(file.isFile() && file.exists()){
                ServletOutputStream os = null;
                FileInputStream is = null;
                try{
                    String imgPath = path;
                    is = new FileInputStream(new File(imgPath));
                    response.setContentType("multipart/form-data");
                    os = response.getOutputStream();
                    //读取文件流
                    int len = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((len = is.read(buffer)) != -1){
                        os.write(buffer,0,len);
                    }
                    os.flush();
                }catch(IOException e){
                    e.printStackTrace();
                }finally{
                    is.close();
                    os.close();
                }
            }
        }
        return null;
    }

    String getNaturalPath(String path){
        String [] part = path.split("/");
        String picType = part[part.length-2];
        switch (picType){
            case "log":
                picType = "nature";
                break;
            case "nature":
                picType = "log";
                break;
            default:
                break;
        }
        part[part.length-2] = picType;
        path = "";

        for(int i = 0;i<part.length ;i++){
            if(path.length()==0){
                path = part[i];
            }else{
                path = path + "/" + part[i];
            }
        }

        return path;
    }

}
