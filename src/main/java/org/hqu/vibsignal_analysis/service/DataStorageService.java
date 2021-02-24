package org.hqu.vibsignal_analysis.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hqu.vibsignal_analysis.mapper.DataStorageMapper;
import org.hqu.vibsignal_analysis.mapper.ExperimentMapper;
import org.hqu.vibsignal_analysis.mapper.entity.DataStorage;
import org.hqu.vibsignal_analysis.service.congfiguration.ExpConfig;
import org.hqu.vibsignal_analysis.util.CodeGen;
import org.hqu.vibsignal_analysis.util.FileStoreToLocal;
import org.hqu.vibsignal_analysis.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DataStorageService {
    @Autowired
    DataStorageMapper dataStorageMapper;
    @Autowired
    ExperimentMapper experimentMapper;

    FileStoreToLocal fsLocal = new FileStoreToLocal();

    ExpConfig expConfig = new ExpConfig();
    String rootPath = expConfig.getProp("dataPath");


    public DataStorage addInfo(DataStorage data,HttpSession session){
        try {
        	data.setUserId((String)session.getAttribute("userId"));
            data = setDataApplicationRange(data,session);
            data.setUploadDate(new Date());
            return data;
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }
    
    public DataStorage setDataApplicationRange(DataStorage data, HttpSession session){
        String expClass = (String)session.getAttribute("expClass");
        switch (expClass){
            case "ULPM":
                data.setDataApplicationRange("1");
                break;
            case "ULP":
                data.setDataApplicationRange("1");
                break;
            case "KLPM":
                data.setDataApplicationRange("2");
                break;
            case "KLP":
                data.setDataApplicationRange("2");
                break;
            case "UKLT":
                data.setDataApplicationRange("3");
                break;
            case "UKLP":
                data.setDataApplicationRange("3");
                break;
            default:
                data.setDataApplicationRange("");
        }
        return data;
    }
    
    public String saveDataList(List<DataStorage> dataList) {
        if (dataList.size() > 0) {
            String genedDataId = CodeGen.genDataId(dataList.get(0).getUserId());
            String dataId = "";
            if(dataList.get(0).getDataId()!=null&&dataList.get(0).getDataId().length()!=0){
                dataId = dataList.get(0).getDataId();
            }
            List<DataStorage> tempList;
            if(dataId.length()==0){
                dataId = genedDataId;
                for(DataStorage data : dataList){
                    data.setDataId(dataId);
                    dataStorageMapper.insert(data);
                }
            }else{
                for(DataStorage data : dataList){
                    tempList = dataStorageMapper.findList(data);
                    if(tempList.size()!=0){
                        dataStorageMapper.update(data);
                    }else{
                        dataStorageMapper.insert(data);
                    }
                }
            }
            return dataId;
        }
        return "";
    }


    public List<DataStorage> save(String param1, String param2, String param3, String dataId, String expId, HttpServletRequest request) throws IOException {
        //将前台ajax请求分成多部分请求
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //读取多请求中的数据文件
        List<MultipartFile> list = new ArrayList<>();
            if(param1!=null && param1.length()!=0) list.add(multipartRequest.getFile(param1));
            if(param2!=null && param2.length()!=0) list.add(multipartRequest.getFile(param2));
            if(param3!=null && param3.length()!=0) list.add(multipartRequest.getFile(param3));
            //指定数据上传路径
            if(expId!=null&&expId.length()!=0) expId = expId + "/";
            String uploadDataPath = expConfig.getProp("uploadDataPath") +expId;
            //上传数据
            fsLocal.storeFile(list,uploadDataPath);

        List<DataStorage> dataList = new ArrayList<>();
        for (MultipartFile mfile: list) {
            if(mfile!=null) {
                DataStorage data = new DataStorage();
                data.setDataId(dataId);
                data.setDataName(mfile.getOriginalFilename());

                data.setDataIndex(uploadDataPath + data.getDataName());

                //取Tdata, Pdata, Udata
                data.setDataClass(mfile.getName().split("=")[1]);
                dataList.add(data);
            }
        }
        return dataList;
    }

    public List<DataStorage> getDataIndex(DataStorage dataStorage){
        List<DataStorage> list = new ArrayList<>();
        list = dataStorageMapper.findDataIndex(dataStorage);
        return list;
    }

    public String getDataPath(DataStorage dataStorage){
        String filePath = dataStorageMapper.findDataPath(dataStorage);
        return filePath;
    }
    
    //更新数据操作
    public String updateData(String dataId,String selectDataId) throws ParseException{
    	DataStorage data = new DataStorage();
    	data.setDataId(selectDataId);
    	List<DataStorage> list = dataStorageMapper.findDataIndex(data);
    	for (DataStorage dataStorage : list) {
			String dataClass = dataStorage.getDataClass();
			if("Tdata".equals(dataClass) || "Pdata".equals(dataClass) || "LP_Tdata".equals(dataClass) || "LP_Sdata".equals(dataClass)){
				dataStorage.setDataId(dataId);
				dataStorage.setUploadDate(new Date());
				System.out.println(dataStorage);
				dataStorageMapper.update(dataStorage);
			}
			
		}
    	return dataId;
    	
    }

    public Page getList(Integer page, int rows, DataStorage dataStorage) {
        // Annie写的数据表查询
        PageHelper.startPage(page, rows);
        List<DataStorage> dataStorageList = dataStorageMapper.getList(dataStorage);
        //将PageInfo对象中的转化为自定义的Page对象，符合带有分页信息的datagrid的要求
        PageInfo<DataStorage> pageInfo = new PageInfo<>(dataStorageList);
        Page resultPage = new Page();
        resultPage.setRows(dataStorageList);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }

    public void updateDataName(String dataId, String dataName) {
        // Annie根据dataId更新dataName
        DataStorage dataStorage = new DataStorage();
        dataStorage.setDataId(dataId);
        dataStorage.setDataName(dataName);
        dataStorageMapper.updateDataName(dataStorage);
    }

    public void delete(List<DataStorage> dataList) {
        for(DataStorage data : dataList){
            dataStorageMapper.delete(data);
            experimentMapper.deleteDataInfoByUpdate(data.getDataId());
        }
    }


}
