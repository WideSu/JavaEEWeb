package org.hqu.vibsignal_analysis.util;

import org.hqu.vibsignal_analysis.mapper.entity.DataStorage;
import org.hqu.vibsignal_analysis.mapper.entity.ExpResult;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.service.DataStorageService;
import org.hqu.vibsignal_analysis.service.ExpResultService;
import org.hqu.vibsignal_analysis.service.congfiguration.ExpConfig;
import org.hqu.vibsignal_analysis.util.algorithm.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ricky Zhang
 * @version 1.0 2019-3-9
 */

@Controller
public class AlgorithmHandler {
    ExpConfig expConfig = new ExpConfig();
    //正常引用service
    @Resource
    private ExpResultService expResultService;
    @Resource
    private DataStorageService dataStorageService;
    //将自己作为静态私有变量引入，使其在springmvc初始化之前就被创建
    private static AlgorithmHandler algorithmHandler;
    //通过PostConstruct实现初始化bean之前的操作。predestory预销毁。
    @PostConstruct
    public void init(){
        algorithmHandler = this;
        algorithmHandler.expResultService = this.expResultService;
        algorithmHandler.dataStorageService = this.dataStorageService;
    }

    public String[] paramTest={"freqRangeMin", "freqRangeMax", "samplingFreq", "p2Predict", "expId","dataId"};

    public void runningPython(Map<String,String> map, WebSocketSession session) throws Exception{

        Experiment experiment = new Experiment();
        experiment.setExpId(map.get("expId"));
        //获取图片与文本数据的path
        map = getDataIndex(map);
        map = mapToPyScript(map);
        if(!map.isEmpty() && containsKeyList(paramTest,map)){
            //执行算法文件
            Process proc = null;
            try {
                /*
                 * 通过runtime获得与Java程序相关的运行时对象, exec执行一段终端命令调用ipython环境,
                 * 执行指定路径下的python脚本 并传递参数，python通过sys.argv读取参数数组*/
                //E:\SSM\pycaller\algorithms\
                String command = "ipython3 " + expConfig.getProp("pyScriptPath") + map.get("pyScriptName") + " "
                        +map.get(paramTest[0])+" " +map.get(paramTest[1])+ " " +map.get(paramTest[2])+ " "+map.get(paramTest[3])+" "+map.get(paramTest[4])+" "
                        +expConfig.getProp("picPath")+" "+expConfig.getProp("dataPath")+" "
                        +map.get(paramTest[5])+" "
                        +map.get("Tdata")+" "+map.get("Pdata") + " "
                        +map.get("Udata");
                proc = Runtime.getRuntime()
                        .exec(command);
                InputStreamReader stdin = new InputStreamReader(proc.getInputStream(),"GBK");
                LineNumberReader input = new LineNumberReader(stdin);
                String line;
                Map<String,String> tempMap = new HashMap<>();
                while ((line = input.readLine()) != null) {
                    // 通过websocketsession将java读取到的python控制台输出输出至前端
                    //结尾输出图片名称与路径
                    if("picName".equals(line.split("=")[0])||"dataName".equals(line.split("=")[0])
                            ||"picPath".equals(line.split("=")[0])||"dataPath".equals(line.split("=")[0])){
                        tempMap.put(line.split("=")[0],line.split("=")[1]);
                    }else if("error".equals(line)){
                        session.close(CloseStatus.BAD_DATA);
                        proc.destroy();
                        return;
                    }else{
                        session.sendMessage(new TextMessage(line));
                    }
                }
                algorithmHandler.expResultService.saveResult(experiment,tempMap);
                proc.waitFor();
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void runClusteringAlgorithms(Map<String,String> map, WebSocketSession webSocketSession, HttpSession httpSession) throws Exception {
        try{
            double correlation = Double.valueOf(map.get("corThreshold"));
            int windowSize = Integer.valueOf(map.get("windowSize"));
            String dataId = map.get("dataId");
            int clusterSize = Integer.valueOf(map.get("clusterSize"));
            String algorithm = map.get("algorithm");
            //将时间序列文件信息存入data_storage

            DataStorage data = new DataStorage();
            data.setDataId(dataId);
            data.setDataClass("Tdata");
            String filePath1 = algorithmHandler.dataStorageService.getDataPath(data);
            data.setDataClass("Pdata");
            String filePath2 = algorithmHandler.dataStorageService.getDataPath(data);

            ExpResult expResult = new ExpResult();
            //运行算法
            switch (algorithm){
                case "HCP":
                    HierarchicalClusteringPos hierarchicalClusteringPos = new HierarchicalClusteringPos();
                    expResult = hierarchicalClusteringPos.hcp(webSocketSession,httpSession,correlation,windowSize,clusterSize,filePath1,filePath2,dataId);
                    break;
                case "DCP":
                    DensityClusteringPos densityClusteringPos = new DensityClusteringPos();
                    expResult = densityClusteringPos.dcp(webSocketSession,httpSession,correlation,windowSize,clusterSize,filePath1,filePath2,dataId);
                    break;
                case "HCN":
                    HierachicalClusteringNeg hierachicalClusteringNeg = new HierachicalClusteringNeg();
                    expResult = hierachicalClusteringNeg.hcn(webSocketSession,httpSession,correlation,windowSize,clusterSize,filePath1,filePath2,dataId);
                    break;
                case "DCN":
                    DensityClusteringNeg densityClusteringNeg = new DensityClusteringNeg();
                    expResult = densityClusteringNeg.dcn(webSocketSession,httpSession,correlation,windowSize,clusterSize,filePath1,filePath2,dataId);
                    break;
                case "VDSI":
                    VDSI vdsi = new VDSI();
                    expResult = vdsi.vdsi(webSocketSession,httpSession,correlation,windowSize,clusterSize,filePath1,dataId);
                    break;
                default:
                    webSocketSession.close();
                    return;
            }
            algorithmHandler.expResultService.saveResult(expResult);
            webSocketSession.close();
        }catch(Exception e){
            webSocketSession.sendMessage(new TextMessage("算法运行出错，请检查上传数据与输入参数！"));
            webSocketSession.close(CloseStatus.BAD_DATA);
            e.printStackTrace();
        }
    }

    public Boolean containsKeyList(String[] paramList, Map<String,String> map){
        if(map.size()==0){
            return false;
        }
        for(String i : paramList){
            if(!map.containsKey(i)){
                return false;
            }
        }
        return true;
    }

    public Map<String, String> getDataIndex(Map<String, String> map){
        String dataId = map.get("dataId");
        DataStorage data = new DataStorage();
        data.setDataId(dataId);
        List<DataStorage> list = algorithmHandler.dataStorageService.getDataIndex(data);
        for(DataStorage temp : list){
        	if("LP_Tdata".equals(temp.getDataClass())){
        		map.put("Tdata", temp.getDataIndex());
        	}else if("LP_Sdata".equals(temp.getDataClass())){
        		map.put("Pdata", temp.getDataIndex());
        	}else if("LP_Pdata".equals(temp.getDataClass())){
        		map.put("Udata", temp.getDataIndex());
        	}else{
            map.put(temp.getDataClass(),temp.getDataIndex());}
        }
        return map;
    }

    public Map<String, String> mapToPyScript(Map<String, String> map){
        if(map.containsKey("algorithm")){
            switch(map.get("algorithm")){
                case "线性回归":
                    map.put("pyScriptName", map.get("expClass") + "LR.py");
                    break;
                case "SVM":
                    map.put("pyScriptName", map.get("expClass") + "SVR.py");
                    break;
                case "决策树":
                    map.put("pyScriptName", map.get("expClass") + "DTree.py");
                    break;
                case "KNN":
                	map.put("pyScriptName", map.get("expClass")+"KNN.py");
                default:
                    break;
            }
            return map;
        }
        return map;
    }
}
