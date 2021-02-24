package org.hqu.vibsignal_analysis.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//mybatis返回的是ListMap，因此在这里转换ListMap为Map
public class ListMapToMap {
    public Map<String,String> listMap2Map(List<Map<String, String>> listMap){
        Map<String,String> map = new HashMap<>();
        for(Map<String,String> tempMap : listMap){
            map.put(tempMap.get("feature_name"),tempMap.get("feature_value"));
        }
        return map;
    }
}
