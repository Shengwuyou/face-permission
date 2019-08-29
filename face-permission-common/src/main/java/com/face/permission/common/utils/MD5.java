package com.face.permission.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class MD5 {
	private static Logger log = LogManager.getLogger(MD5.class);

	static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	public static String getMD5(byte[] source) {
		String md5 = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte[] tmp = md.digest();
			char[] str = new char[16 * 2];
			int k = 0;
			
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[(byte0 >>> 4) & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			md5 = new String(str);
		} catch (Exception e) {
			log.error(e);
		}
		return md5;
	}

	public static String getMD5(String source) {
		return getMD5(source.getBytes());
	}

	public static String getMD5New(String source) {
		try {
			return getMD5(source.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error(source + "转换为UTF-8错误" + e, e);
		}
		return null;
	}

	public static String getMD5ByFile(File file) {
		String md5 = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			MappedByteBuffer byteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(byteBuffer);
			BigInteger bigInteger = new BigInteger(1, messageDigest.digest());
			md5 = bigInteger.toString(16);
		} catch (FileNotFoundException e) {
			log.error("GetMD5ByFile FileNotFoundException.", e);
		} catch (Exception e) {
			log.error("GetMD5ByFile Exception.", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error("GetMD5ByFile IOException.", e);
				}
			}
		}
		return md5;
	}


	public static String getPartnerSign(String partnerIdStr, String partnerUserIdStr) {
		return MD5.getMD5(String.format("%s_duoshoubang20161103_%s", partnerIdStr, partnerUserIdStr));
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getMD5("Admin123"));
		// System.out.println("MD5:"+DigestUtils.md5Hex("WANGQIUYUN"));
//		long currentTime = System.currentTimeMillis();
//		String timestamp = String.valueOf(currentTime / 1000);
//		System.out.println(getMD5("appKey" + "QjVDMkNBNzdCMTAyN0NBOA==" + "params" + "{\"orderCodes\":[\"260013589860174\"]}" + "timestamp" + timestamp + "userKey" + "MjBjYzAwYzgzM2FmMTlmMjYwOGVkY2FmYTI2NTI3MDc=" + "75f6bf44af374e20522503b0c76eea70" + "825998743f0d9f0ebcfef25383e4e386").toLowerCase());
	}
}