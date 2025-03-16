package com.neo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

/**
 * Created by summer on 2017/5/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceTest.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 测试简单邮件发送功能
     * 此测试方法旨在验证邮件服务是否能够成功发送一封简单的邮件
     * 它通过调用mailService的sendSimpleMail方法来发送邮件，并检查邮件是否发送成功
     */
    @Test
    public void testSimpleMail() {
        try {
            // 执行发送简单邮件的操作，指定收件人邮箱、邮件主题和邮件内容
            mailService.sendSimpleMail("ityouknow@126.com", "test simple mail", "hello this is simple mail");
            // 如果邮件发送成功，记录成功信息
            logger.info("Simple mail sent successfully.");
        } catch (Exception e) {
            // 如果邮件发送过程中发生异常，记录错误信息
            logger.error("Failed to send simple mail: ", e);
        }
    }

    @Test
    public void testHtmlMail() {
        String content = "<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        try {
            mailService.sendHtmlMail("ityouknow@126.com", "test html mail", content);
            logger.info("HTML mail sent successfully.");
        } catch (Exception e) {
            logger.error("Failed to send HTML mail: ", e);
        }
    }

    @Test
    public void sendAttachmentsMail() {
        String filePath = System.getProperty("user.home") + "/TaxCard.log"; // 动态路径
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("Attachment file does not exist: {}", filePath);
            return;
        }
        try {
            mailService.sendAttachmentsMail("ityouknow@126.com", "主题：带附件的邮件", "有附件，请查收！", filePath);
            logger.info("Attachment mail sent successfully.");
        } catch (Exception e) {
            logger.error("Failed to send attachment mail: ", e);
        }
    }

    @Test
    public void sendInlineResourceMail() {
        String rscId = "neo006";
        String content = "<html><body>这是有图片的邮件：<img src='cid:" + rscId + "' ></body></html>";
        String imgPath = System.getProperty("user.home") + "/logo/smilef.png"; // 动态路径
        File imgFile = new File(imgPath);
        if (!imgFile.exists()) {
            logger.error("Image file does not exist: {}", imgPath);
            return;
        }
        try {
            mailService.sendInlineResourceMail("ityouknow@126.com", "主题：这是有图片的邮件", content, imgPath, rscId);
            logger.info("Inline resource mail sent successfully.");
        } catch (Exception e) {
            logger.error("Failed to send inline resource mail: ", e);
        }
    }

    @Test
    public void sendTemplateMail() {
        Context context = new Context();
        context.setVariable("id", "006");
        String emailContent = templateEngine.process("emailTemplate", context);
        try {
            mailService.sendHtmlMail("ityouknow@126.com", "主题：这是模板邮件", emailContent);
            logger.info("Template mail sent successfully.");
        } catch (Exception e) {
            logger.error("Failed to send template mail: ", e);
        }
    }
}
