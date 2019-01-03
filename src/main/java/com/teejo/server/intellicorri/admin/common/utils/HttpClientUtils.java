package com.teejo.server.intellicorri.admin.common.utils;

import com.google.gson.Gson;
import com.teejo.server.intellicorri.admin.common.beans.CommonResult;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpClientUtils {

    /**
     * 接口调用 GET
     */
    public static CommonResult httpURLConnectionGET(final String GET_URL ,Map map,String requestMethod) throws Exception {
        CommonResult result = null;
        String parm = mapToStr(map);
        // 把字符串转换为URL请求地址
        URL url = new URL(GET_URL+"?"+parm);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方式为post
        connection.setRequestMethod(requestMethod);
        // 连接会话
        connection.connect();
        // 获取输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        // 循环读取流
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        int responseCode = connection.getResponseCode();
        result = new CommonResult(true,responseCode,sb.toString(),"访问成功！");

        // 关闭流
        br.close();
        // 断开连接
        connection.disconnect();

        return result;
    }

    /**
     * 接口调用  POST
     */
    public static CommonResult httpURLConnectionPOST (final String POST_URL,Map map,String requestMethod,String contentType) throws Exception {
        CommonResult result = null;
        URL url = new URL(POST_URL);
        String parm = "";
        if(contentType.equals("form")){
            parm = mapToStr(map);
            contentType = "application/x-www-form-urlencoded";
        }else if (contentType.equals("json")){
            parm = mapToJson(map);
            contentType = "application/json";
        }else {
            return result = new CommonResult(true,500,null,"访问失败！头信息错误");
        }

        // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
        // 此时cnnection只是为一个连接对象,待连接中
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
        connection.setDoOutput(true);

        // 设置连接输入流为true
        connection.setDoInput(true);

        // 设置请求方式为post
        connection.setRequestMethod(requestMethod);

        // post请求缓存设为false
        connection.setUseCaches(false);

        // 设置该HttpURLConnection实例是否自动执行重定向
        connection.setInstanceFollowRedirects(true);

        // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
        // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Accept-Charset", "utf-8");

        // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
        connection.connect();

        // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
        DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
        //URLEncoder.encode()方法  为字符串进行编码
        //在此添加参数

//        parm = URLEncoder.encode(parm, "utf-8");

        // 将参数输出到连接
        dataout.write(parm.getBytes("utf-8"));
//        dataout.writeBytes(parm);

        // 输出完成后刷新并关闭流
        dataout.flush();
        // 重要且易忽略步骤 (关闭流,切记!)
        dataout.close();

        // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
        BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        // 用来存储响应数据
        StringBuilder sb = new StringBuilder();

        // 循环读取流,若不到结尾处
        while ((line = bf.readLine()) != null) {
            System.out.printf("BufferedReader读取到的信息:"+line);
            sb.append(line);
        }
        int responseCode = connection.getResponseCode();
        result = new CommonResult(true,responseCode,sb.toString(),"访问成功！");

        // 重要且易忽略步骤 (关闭流,切记!)
        bf.close();
        // 销毁连接
        connection.disconnect();

        return result;
    }

    private static String mapToStr(Map<Object,Object> map){
        if(map == null){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        int i= 0;
        for(Map.Entry<Object,Object> entry:map.entrySet()){
            if(entry.getValue() != null){
                if(i>0){
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                i++;
            }
        }
        i = 0;
        return sb.toString();
    }

    private static String mapToJson(Map<Object,Object> map){
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

}
