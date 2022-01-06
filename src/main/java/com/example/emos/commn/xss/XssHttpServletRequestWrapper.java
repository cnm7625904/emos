package com.example.emos.commn.xss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;
import io.netty.util.internal.StringUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * copyright (C), 2022, 运达科技有限公司
 * fileName  XssHttpServletRequestWrapper
 *
 * @author 王玺权
 * date  2022-01-02 20:52
 * description
 * reviser
 * revisionTime
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper{

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value= super.getParameter(name);
        if(!StrUtil.hasEmpty(value)){
            value= HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if(parameterValues!=null){
            for (int i = 0; i < parameterValues.length; i++) {
                String value=parameterValues[i];
                if(!StrUtil.hasEmpty(value)){
                    value= HtmlUtil.filter(value);
                }
                parameterValues[i]=value;
            }
        }
        return parameterValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMaps = super.getParameterMap();
        LinkedHashMap<String,String[]> map=new LinkedHashMap<>();
        if(parameterMaps!=null){
            for(String key:parameterMaps.keySet()){
                String[] values=parameterMaps.get(key);
                for (int i = 0; i < values.length; i++) {
                    String value=values[i];
                    if(!StrUtil.hasEmpty(value)){
                        value= HtmlUtil.filter(value);
                    }
                    values[i]=value;
                }
                map.put(key,values);
            }
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        if(!StrUtil.hasEmpty(header)){
            header=HtmlUtil.filter(header);
        }
        return header;
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        InputStream in= super.getInputStream();
        InputStreamReader reader=new InputStreamReader(in, Charset.forName("UTF-8"));
        BufferedReader buffer=new BufferedReader(reader);
        StringBuffer body=new StringBuffer();
        String line=buffer.readLine();
        while(line!=null){
            body.append(line);
            line=buffer.readLine();
        }
        buffer.close();
        reader.close();
        in.close();
        Map<String,Object> map= JSONUtil.parseObj(body.toString());
        Map<String,Object> result=new LinkedHashMap<>();
        for(String key:map.keySet()){
            Object val=map.get(key);
            if(val instanceof String){
                if(!StrUtil.hasEmpty(val.toString())){
                    result.put(key,HtmlUtil.filter(val.toString()));
                }
            }
            else {
                result.put(key,val);
            }
        }
        String json=JSONUtil.toJsonStr(result);
        ByteArrayInputStream bain=new ByteArrayInputStream(json.getBytes());
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bain.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}
