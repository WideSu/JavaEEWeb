package org.hqu.vibsignal_analysis.controller;

import org.hqu.vibsignal_analysis.service.congfiguration.ExpConfig;
import org.hqu.vibsignal_analysis.util.ServerInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class WebSocketController {

    @RequestMapping("/getServerIP")
    @ResponseBody
    public String getServerIP(){
        String ip = ServerInfo.getCurrentServerIP();
        String port = ExpConfig.getPropFromFile("serverInfo.properties","serverPort");
        return ip + ":" + port;
    }
}
