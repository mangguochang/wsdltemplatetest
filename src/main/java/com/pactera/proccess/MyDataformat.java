package com.pactera.proccess;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;


/**
 * 数据格式转换的类
 * @author simonMeng
 * @version 1.0
 * @date 2019/10/23
 **/
@Component("myDataformat")
public class MyDataformat {
    @Value("${camel.json.format.split.str}")
    public String splitStr;
    @Value("${camel.json.format.delete.node}")
    public String deleteNodeNames;
    @Value("${camel.wsdl.return.jsonNode}")
    public String returnJsonNode;

    public JSON process(@Body String body) {
        String jsonStr=null!=body?body:null;
        String[] nodeNames=null;
        JSON jsonObject=null;
        if(null!=jsonStr){
            String[] strings=splitStr.split(";");
            for (int i=0;i<strings.length;i++){
                String oldStr=strings[i];
                if(null!=oldStr&&oldStr.length()>0){
                    jsonStr=jsonStr.replace(oldStr,"");
                }
            }
            nodeNames=deleteNodeNames.split(";");
            if(isJSONString(jsonStr)){
                JSONObject jsonObj=JSONObject.fromObject(jsonStr);
                jsonObject=resultJsonObj(jsonObj);
                if(null==jsonObject){
                    jsonObject=jsonObj;
                }
               /* if(null!=nodeNames&&nodeNames.length>0){
                    deleteNodeName(nodeNames,jsonObject);
                }*/
            }
        }else{
            jsonObject=JSONObject.fromObject(jsonStr);
        }
        return jsonObject;
    }

    /***
     * 处理掉一些不需要的节点，直接拿到response节点内容信息
     * @param jsonObject
     * @return
     */
    public JSON resultJsonObj(JSONObject jsonObject){
        Iterator keys=jsonObject.keys();
        JSON returnObj=null;
        while (keys.hasNext()){
            String key=keys.next().toString();
            if(key.toLowerCase().indexOf(returnJsonNode.toLowerCase())>-1){
                if(isJSONArray(jsonObject.getString(key))){
                    return jsonObject.getJSONArray(key);
                }
                return jsonObject.getJSONObject(key);
            }else{
                if(isJSONString(jsonObject.getString(key))){
                    if(isJSONArray(jsonObject.getString(key))){
                        JSONArray jsonArray=jsonObject.getJSONArray(key);
                        for (int k=0;k<jsonArray.size();k++){
                            JSONObject jsonobj=jsonArray.getJSONObject(k);
                            if(jsonobj.size()>1){//是否有子集
                                if(isJSONString(jsonobj.toString())){
                                    returnObj=resultJsonObj(jsonobj);
                                }
                            }
                        }
                    }else{
                        returnObj=resultJsonObj(jsonObject.getJSONObject(key));
                    }
                }
            }
        }
        return returnObj;
    }

    public JSONObject deleteNodeName(String[] nodeNames,JSONObject jsonObject){
        Iterator keys=jsonObject.keys();
        JSONObject returnObj=null;
        while (keys.hasNext()){
           String key=keys.next().toString();
           boolean isDel=false;
           for (int i=0;i<nodeNames.length;i++){
               String nodeName=nodeNames[i];
               if(key.equalsIgnoreCase(nodeName)){
                  isDel=true;
                  break;
               }
           }
           if(isDel){
               jsonObject.remove(key);
               returnObj=jsonObject;
               deleteNodeName(nodeNames,jsonObject);
           }
           if(isJSONString(jsonObject.getString(key))){
                System.out.println("jsonObject.values().size()"+jsonObject.values().size()+",size:"+jsonObject.size());
                if(isJSONArray(jsonObject.getString(key))){
                    JSONArray jsonArray=jsonObject.getJSONArray(key);
                    for (int k=0;k<jsonArray.size();k++){
                        JSONObject jsonobj=jsonArray.getJSONObject(k);
                        if(jsonobj.size()>1){//是否有子集
                            if(isJSONString(jsonobj.toString())){
                                returnObj=deleteNodeName(nodeNames,jsonobj);

                            }
                        }
                    }
                }else{
                    JSONObject jsonobj=jsonObject.getJSONObject(key);
                    if(jsonobj.size()>1){//是否有子集
                        if(isJSONString(jsonobj.toString())){
                            returnObj=deleteNodeName(nodeNames,jsonobj);
                        }
                    }
                }

            }
        }
        return returnObj;
    }

    /***
     * 判断是否json字符的方法
     * @param jsonString
     * @return
     */
    private boolean isJSONString(String jsonString){
        try {
            JSONObject jsonObject=JSONObject.fromObject(jsonString);
            return true;
        }catch (Exception e){
            try {
                JSONArray jsonArray=JSONArray.fromObject(jsonString);
                return true;
            }catch (Exception ex){
                return false;
            }
        }
    }
    /***
     * 判断是否json字符的方法
     * @param jsonString
     * @return
     */
    private boolean isJSONArray(String jsonString){
        try {
            JSONArray jsonArray=JSONArray.fromObject(jsonString);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
