package org.hqu.vibsignal_analysis.service.congfiguration;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Component
public class ExpConfig {

    public static String get(String key){
        try {
            InputStream is = ExpConfig.class.getClassLoader().getResourceAsStream("expConfig.properties");
            Properties prop = new Properties();
            prop.load(is);
            if (prop != null) {
                return prop.getProperty(key);
            }
            return "";
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String getPropFromFile(String fileName, String key){
        try {
            InputStream is = ExpConfig.class.getClassLoader().getResourceAsStream(fileName);
            Properties prop = new Properties();
            prop.load(is);
            if (prop != null) {
                return prop.getProperty(key);
            }
            return "";
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getProp(String key){
        return get(key);
    }

}
