package com.face.permission.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * EmailUtil
 *
 * @Author ZKT
 * @CreateDate 2018年6月18日
 */
public class EmailUtil {

	private static final Logger logger = LoggerUtil.COMMON_DEFAULT;

	public static void send(EmailAccount account, String subject, String content, String... receivers) {
		Assert.notNull(receivers, "收件人不能为空");
		Assert.isTrue(StringUtils.isNotBlank(subject), "标题不能为空");
		Assert.isTrue(StringUtils.isNotBlank(content), "内容不能为空");

		Set<String> receiverAccounts = new HashSet<String>();
		for (String receiver : receivers) {
			receiverAccounts.add(receiver);
		}

		send(account, subject, content, receiverAccounts);
	}

	public static void send(EmailAccount account, String subject, String content, MailGroup mailGroup) {
		Assert.notNull(mailGroup, "邮件组不能为空");
		Assert.isTrue(StringUtils.isNotBlank(subject), "标题不能为空");
		Assert.isTrue(StringUtils.isNotBlank(content), "内容不能为空");

		send(account, subject, content, mailGroup.getAccounts());
	}

	private static void send(EmailAccount account, String subject, String content, Set<String> receivers) {

		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(account.getHost());
			email.setSmtpPort(account.getPort());
			email.setAuthentication(account.getUsername(), account.getPassword());
			email.setSSLOnConnect(true);
			email.setCharset("UTF-8");

			email.setFrom(account.getUsername());
			email.setSubject(subject);
			email.setMsg(content);
			for (String receiver : receivers) {
				email.addTo(receiver);
			}
			email.send();
			logger.info(String.format("[Email] Success! Receivers: %s Subject: %s Content: %s", receivers, subject, content));
		} catch (EmailException e) {
			logger.error(String.format("[Email] Fail! Receivers: %s Subject: %s Content: %s", receivers, subject, content), e);
		}
	}

	public static void send(EmailAccount account, String subject, String content, Set<String> receivers, String name, String Path) {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(account.getHost());
			email.setSmtpPort(account.getPort());
			email.setAuthentication(account.getUsername(), account.getPassword());
			email.setSSLOnConnect(true);
			email.setCharset("UTF-8");

			email.setFrom(account.getUsername());
			email.setSubject(subject);
			email.setMsg(content);
			EmailAttachment attachment = new EmailAttachment();// 创建附件
			attachment.setPath(Path);// 本地附件，绝对路径
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setName(name);
			email.attach(attachment);
			for (String receiver : receivers) {
				email.addTo(receiver);
			}
			email.send();
			logger.info(String.format("[Email] Success! Receivers: %s Subject: %s Content: %s", receivers, subject, content));
		} catch (EmailException e) {
			logger.error(String.format("[Email] Fail! Receivers: %s Subject: %s Content: %s", receivers, subject, content), e);
		}
	}

	public static void send(EmailAccount account, String subject, String content, Set<String> receivers, Map<String,String> attachments) {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(account.getHost());
			email.setSmtpPort(account.getPort());
			email.setAuthentication(account.getUsername(), account.getPassword());
			email.setSSLOnConnect(true);
			email.setCharset("UTF-8");

			email.setFrom(account.getUsername());
			email.setSubject(subject);
			email.setMsg(content);
			Set<String> keys = attachments.keySet();
			for(String key : keys){
				EmailAttachment attachment = new EmailAttachment();// 创建附件
				attachment.setPath(attachments.get(key));// 本地附件，绝对路径
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setName(key);
				email.attach(attachment);
			}
			for (String receiver : receivers) {
				email.addTo(receiver);
			}
			email.send();
			logger.info(String.format("[Email] Success! Receivers: %s Subject: %s Content: %s", receivers, subject, content));
		} catch (EmailException e) {
			logger.error(String.format("[Email] Fail! Receivers: %s Subject: %s Content: %s", receivers, subject, content), e);
		}
	}

	/**
	 * 邮件账户
	 */
	public enum EmailAccount {
		/** 系统系统 */
		XT("smtp.mxhichina.com", 465, "xitongtongzhi@fenxianglife.com", "Xitongtongzhi2019");

		private String host;

		private int port;

		private String username;

		private String password;

		private EmailAccount(String host, int port, String username, String password) {
			this.host = host;
			this.port = port;
			this.username = username;
			this.password = password;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
	}

	/**
	 * 邮件组
	 */
	public enum MailGroup {
	
		SERVER("xyxd@fenxianglife.com","xyxd@fenxianglife.com");

		private Set<String> accounts;

		private MailGroup(String... receivers) {
			Set<String> receiverAccounts = new HashSet<String>();
			for (String receiver : receivers) {
				receiverAccounts.add(receiver);
			}
			this.accounts = receiverAccounts;
		}

		public Set<String> getAccounts() {
			return accounts;
		}
	}
}