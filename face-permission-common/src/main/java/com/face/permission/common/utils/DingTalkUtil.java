package com.face.permission.common.utils;

import com.dingtalk.chatbot.DingtalkChatbotClient;
import com.dingtalk.chatbot.SendResult;
import com.dingtalk.chatbot.message.TextMessage;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-06-21 15:59
 */
public class DingTalkUtil {

    private static DingtalkChatbotClient client = new DingtalkChatbotClient();

    /**
     * 发送钉钉消息通知
     * @param content
     * @param url
     */
    public static void sendDingTalkMsg(Logger logger, String content, String url) {
        String errMsg="钉钉发送失败";
        try {
            TextMessage message = new TextMessage(content);
            SendResult result = client.send(url, message);
            if (result.isSuccess()){
                logger.info("钉钉消息发送成功");
            }else {
                errMsg=result.getErrorMsg();
                logger.info("钉钉消息发送失败");
            }
        }catch (Exception e){
            logger.error(errMsg);
        }

    }
}
