package com.teejo.server.intellicorri.admin.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordUtils {

	public void htmlToWord2() throws Exception {
	    InputStream bodyIs = new FileInputStream("f:\\1.html");
		InputStream cssIs = new FileInputStream("f:\\1.css");
		String body = this.getContent(bodyIs);
		String css = this.getContent(cssIs);
		//拼一个标准的HTML格式文档
		String content = "<html><head><style>" + css + "</style></head><body>" + body + "</body></html>";
		InputStream is = new ByteArrayInputStream(content.getBytes("GBK"));
		OutputStream os = new FileOutputStream("f:\\1.doc");
	    this.inputStreamToWord(is, os);
	}

	/**
	 * 把is写入到对应的word输出流os中
	 * 不考虑异常的捕获，直接抛出
	 * @param is
	* @param os
	* @throws IOException
	 */
	public static void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
	    try {
			POIFSFileSystem fs = new POIFSFileSystem();
			//对应于org.apache.poi.hdf.extractor.WordDocument
			fs.createDocument(is, "WordDocument");
			fs.writeFilesystem(os);
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把输入流里面的内容以UTF-8编码当文本取出。
	 * 不考虑异常，直接抛出
	 * @param ises
	* @return
	* @throws IOException
	 */
	public static String getContent(InputStream... ises) throws IOException {
	    if (ises != null) {
	        StringBuilder result = new StringBuilder();
			BufferedReader br;
			String line;
	        for (InputStream is : ises) {
	            br = new BufferedReader(new InputStreamReader(is, "GBK"));
	            while ((line=br.readLine()) != null) {
	                result.append(line);
	            }
	        }
	        return result.toString();
	    }
	    return null;
	}
	
	public static void main(String[] args) throws IOException{
		//拼一个标准的HTML格式文档
		InputStream bodyIs = new FileInputStream("E:\\test3.txt");
		String body = getContent(bodyIs);
		String content = "<html><head><style></style></head><body>" + body + "</body></html>";
		System.out.println(content);
		InputStream is = new ByteArrayInputStream(content.getBytes("GBK"));
		OutputStream os = new FileOutputStream("E:\\test3.doc");
	    inputStreamToWord(is, os);
		
//		String path = "E:/";
//		String filename = "/test.docx";
//		XWPFDocument doc = new XWPFDocument();
//		XWPFParagraph para= doc.createParagraph();
//		XWPFRun run = para.createRun();
//		run.setText(content);
//		run.set
//		
//		File file = new File(path + filename);
//		FileOutputStream out = new FileOutputStream(file);
//		doc.write(out);
//		out.close();

	}
	
}
