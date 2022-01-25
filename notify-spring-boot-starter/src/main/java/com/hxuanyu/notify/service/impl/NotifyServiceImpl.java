package com.hxuanyu.notify.service.impl;

import com.hxuanyu.notify.enums.NotifyType;
import com.hxuanyu.notify.model.Mail;
import com.hxuanyu.notify.service.MailService;
import com.hxuanyu.notify.service.NotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 通知服务
 *
 * @author hanxuanyu
 * @version 1.0
 */
@Service
public class NotifyServiceImpl implements NotifyService {
    private final Logger logger = LoggerFactory.getLogger(NotifyServiceImpl.class);

    @Resource
    MailService mailService;

    @Override
    public void notify(Object content, NotifyType notifyType) {
        logger.debug("通知内容：{}，通知类型：{}", notifyType, notifyType);
        if (NotifyType.TYPE_MAIL.equals(notifyType)) {
            sendMail(content);
        } else if (NotifyType.TYPE_MSG.equals(notifyType)) {
            sendSms(content);
        } else if (NotifyType.TYPE_LOG.equals(notifyType)) {
            logger.info("新通知：{}", content);
        } else if (NotifyType.TYPE_CUSTOM.equals(notifyType)) {
            logger.warn("您选择了自定义通知，请实现CustomNotify接口并调用 notify(CustomNotify customNotify方法)");
        } else {
            logger.info("未匹配到通知类型：[{}]", content);
        }
    }

    @Override
    public void notify(CustomNotify customNotify) {
        logger.debug("执行自定义通知");
        customNotify.onNotify();
    }

    private void sendSms(Object content) {
        logger.info("即将发送短信通知，通知内容：{}", content);
    }

    private void sendMail(Object content) {
        logger.info("即将发送邮件通知，通知内容：{}", content);
        if (content instanceof Mail) {
            try {
                mailService.sendMail((Mail) content);
            } catch (InterruptedException e) {
                logger.warn("邮件发送失败：{}", e.getLocalizedMessage());
            }
        } else {
            logger.warn("通知内容格式不符");
        }
    }
}
