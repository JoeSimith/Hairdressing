package com.Hairdressing.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件
 * @version
 */
public class FileUpload {
	
	protected static final Logger logger = Logger.getLogger(FileUpload.class);

	/**
	 * @param file 			//文件对象
	 * @param filePath		//上传路径
	 * @param fileName		//文件名
	 * @return  文件名
	 */
	public static String fileUp(MultipartFile file, String filePath, String fileName){
		String extName = ""; // 扩展名格式：
		String returnVal ="";
		try {
			if (file.getOriginalFilename().lastIndexOf(".") >= 0){
				extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			}
			returnVal = copyFile(file.getInputStream(), filePath, fileName).replaceAll("-", "");
		} catch (IOException e) {
			logger.info(e);
			logger.info(returnVal);
		}
		return fileName;
	}
	
	/**
	 * 上传文件前先删除旧文件
	 * @param file
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static String fileUpBeforeDeleteOld(MultipartFile file, String filePath, String fileName){
		String extName = ""; // 扩展名格式：
		String returnVal ="";
		try {
			if (file.getOriginalFilename().lastIndexOf(".") >= 0){
				extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			}
			returnVal = copyFileBeforeDeleteOld(file.getInputStream(), filePath, fileName+extName).replaceAll("-", "");
		} catch (IOException e) {
			logger.info(e);
			logger.info(returnVal);
		}
		return fileName+extName;
	}
	
	/**
	 * 写文件到当前目录的upload目录中,先删除之前旧文件
	 * 
	 * @param in
	 * @param fileName
	 * @throws java.io.IOException
	 */
	private static String copyFileBeforeDeleteOld(InputStream in, String dir, String realName)
			throws IOException {
		File file = new File(dir, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}else{
			file.delete();
		}
		FileUtils.copyInputStreamToFile(in, file);
		return realName;
	}
	
	/**
	 * 写文件到当前目录的upload目录中
	 * 
	 * @param in
	 * @param fileName
	 * @throws java.io.IOException
	 */
	private static String copyFile(InputStream in, String dir, String realName)
			throws IOException {
		File file = new File(dir, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		FileUtils.copyInputStreamToFile(in, file);
		return realName;
	}
}
