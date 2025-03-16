package com.neo.service.impl;

import com.neo.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by summer on 2017/5/4.
 */
@Component
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
    private String from;

    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        if (null == to || null == subject || null == content) {
            logger.error("发送简单邮件时参数为空：to={}, subject={}, content={}", to, subject, content);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        sendMail(message, "简单邮件");
    }

    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        if (to == null || subject == null || content == null) {
            logger.error("发送HTML邮件时参数为空：to={}, subject={}, content={}", to, subject, content);
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            sendMail(message, "HTML邮件");
        } catch (MessagingException e) {
            logger.error("创建HTML邮件时发生异常：to={}, subject={}", to, subject, e);
        }
    }

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件路径
     */
    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        if (to == null || subject == null || content == null || filePath == null) {
            logger.error("发送带附件邮件时参数为空：to={}, subject={}, content={}, filePath={}", to, subject, content, filePath);
            return;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            logger.error("附件文件不存在或不是有效文件：filePath={}", filePath);
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource fileResource = new FileSystemResource(file);
            helper.addAttachment(file.getName(), fileResource);

            sendMail(message, "带附件的邮件");
        } catch (MessagingException e) {
            logger.error("创建带附件邮件时发生异常：to={}, subject={}, filePath={}", to, subject, filePath, e);
        }
    }

    /**
     * 发送正文中有静态资源（图片）的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param rscPath 静态资源路径
     * @param rscId 静态资源ID
     */
    @Override
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) {
        if (to == null || subject == null || content == null || rscPath == null || rscId == null) {
            logger.error("发送嵌入静态资源邮件时参数为空：to={}, subject={}, content={}, rscPath={}, rscId={}", to, subject, content, rscPath, rscId);
            return;
        }

        File resourceFile = new File(rscPath);
        if (!resourceFile.exists() || !resourceFile.isFile()) {
            logger.error("静态资源文件不存在或不是有效文件：rscPath={}", rscPath);
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(resourceFile);
            helper.addInline(rscId, res);

            sendMail(message, "嵌入静态资源的邮件");
        } catch (MessagingException e) {
            logger.error("创建嵌入静态资源邮件时发生异常：to={}, subject={}, rscPath={}, rscId={}", to, subject, rscPath, rscId, e);
        }
    }

    /**
     * 公共邮件发送方法
     * @param message 邮件对象
     * @param mailType 邮件类型描述
     */
    private void sendMail(MimeMessage message, String mailType) {
        try {
            mailSender.send(message);
            logger.info("{}已经发送。", mailType);
        } catch (Exception e) {
            logger.error("发送{}时发生异常！", mailType, e);
        }
    }

    /**
     * 发送电子邮件的方法
     *
     * @param message    要发送的邮件信息，包含邮件的发件人、收件人、主题和内容等
     * @param mailType   邮件类型描述，用于日志记录，例如"通知邮件"或"注册确认邮件"
     */
    private void sendMail(SimpleMailMessage message, String mailType) {
        try {
            // 使用MailSender发送邮件
            mailSender.send(message);
            // 邮件发送成功后，记录日志
            logger.info("{}已经发送。", mailType);
        } catch (Exception e) {
            // 如果邮件发送过程中发生异常，记录错误日志
            logger.error("发送{}时发生异常！", mailType, e);
        }
    }
}
