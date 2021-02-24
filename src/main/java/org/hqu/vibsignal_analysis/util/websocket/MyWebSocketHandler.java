package org.hqu.vibsignal_analysis.util.websocket;

import org.hqu.vibsignal_analysis.util.AlgorithmHandler;
import org.hqu.vibsignal_analysis.util.Session.MySessionContext;
import org.hqu.vibsignal_analysis.util.SocketMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyWebSocketHandler implements WebSocketHandler {

    private Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    //保存用户链接
    private static ConcurrentHashMap<String, WebSocketSession> users = new ConcurrentHashMap<String, WebSocketSession>();
    // 连接 就绪时
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        users.put(session.getId(), session);
    }

    // 处理信息
    @Override
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> message) throws Exception {
        String param = message.getPayload().toString();
        sendMessage(session,param);
    }

    // 处理传输时异常
    @Override
    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) throws Exception {

    }

    // 关闭 连接时
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) {
        logger.debug("用户： " + session.getRemoteAddress() + " is leaving, because:" + closeStatus);

    }

    //是否支持分包
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    //发送信息给前端
    public void sendMessage(WebSocketSession webSocketSession, String param){
        try{
            webSocketSession.sendMessage(new TextMessage("算法运行中..."));
            AlgorithmHandler algorithmHandler = new AlgorithmHandler();
            SocketMessageHandler message = new SocketMessageHandler();
            Map<String,String> map = message.stringToMap(param);
//            String sessionId = map.get("sessionId");
//            if(sessionId!=null){
//                MySessionContext.getSession(sessionId);
//            }
            //判断有没有expId，如果有则代表运行了试验算法，没有代表java聚类算法
            if(map.get("expId")!=null){
                algorithmHandler.runningPython(map,webSocketSession);
            }else{
                if(map.containsKey("sessionId")){
                    String sessionId = map.get("sessionId");
                    MySessionContext myc= MySessionContext.getInstance();
                    HttpSession httpSession = myc.getSession(sessionId);
                    logger.debug("sessionId: " + sessionId);
                    logger.debug("session: " + httpSession);
                    algorithmHandler.runClusteringAlgorithms(map,webSocketSession,httpSession);
                }else{
                    logger.debug("session is null ");
                    webSocketSession.sendMessage(new TextMessage("未获取到当前用户信息，请刷新页面。"));
                    webSocketSession.close(CloseStatus.NOT_ACCEPTABLE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}