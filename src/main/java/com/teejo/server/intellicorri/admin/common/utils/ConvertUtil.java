package com.teejo.server.intellicorri.admin.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConvertUtil {

    public static String map2json(Map<String,String[]> param){
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = new HashMap<>();
        for(Map.Entry<String, String[]> en:param.entrySet()){
            String[] value = en.getValue();
            if(null !=value){
                if(value.length == 1){
                    map.put(en.getKey(), value[0]);
                }else{
                    map.put(en.getKey(),  Arrays.asList(value));
                }
            }
        }
        String json = null;
        try {
             json = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } 
        return json;
    }
    
    public static String map2urlparam(Map<String,String[]> param){
        StringBuffer sb = new StringBuffer();  
        for(Map.Entry<String, String[]> en:param.entrySet()){
        	if(sb.length()>0) sb.append("&");
        	String[] value = en.getValue();
	        sb.append(en.getKey() + "=" +  value[0]);  
        }
        return sb.toString();
    }
    
    public static MultiValueMap<String, Object> requestMap2MultiValueMap(Map<String,String[]> param){
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        for(Map.Entry<String, String[]> en:param.entrySet()){
            String[] value = en.getValue();
            if(null !=value){
                if(value.length == 1){
                    paramMap.add(en.getKey(), value[0]);
                }else{
                    paramMap.add(en.getKey(),  Arrays.asList(value));
                }
            }
        }
        return paramMap;
    }

}
