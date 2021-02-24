package org.hqu.vibsignal_analysis.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ricky Zhang
 * @version 1.0 2019-3-8
 */

public class SocketMessageHandler {

    //将websocket传回的变量拼接的String转为map
    public Map<String,String> stringToMap(String str){
        try{
            String[] keyValue = str.split("&");
            Map<String,String> map = new HashMap<>();
            //循环加入map集合
            for(String temp : keyValue){
                String[] tempKV = temp.split("=");
                if(tempKV.length>1){
                    map.put(tempKV[0],tempKV[1]);
                }else{
                    map.put(tempKV[0], null);
                }
            }
            return map;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
