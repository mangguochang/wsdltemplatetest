package com.pactera.proccess;

import com.pactera.common.TemplateUitls;
import com.pactera.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 请求参数进来需要转换的操作。
 * @author simonMeng
 * @version 1.0
 * @date 2019/10/18
 **/
@Component("camelRequestProccess")
public class CamelRequestProccess implements Processor {
    private Logger logger = LoggerFactory.getLogger(CamelRequestProccess.class);

    @Autowired
    public UserService userService;
    @Value("${spring.application.name}")
    public String appId;

    @Value("${camel.wsdl.elementName}")
    public String camelWSDLElementName;
    @Value("${camel.wsdl.xmlnsSoap}")
    public String camelWSDLXMLSoap;
    @Value("${camel.wsdl.targetNamespace}")
    public String camelWSDLTargetNamespace;


    @Override
    public void process(Exchange exchange) throws Exception {
        long startTime=new Date().getTime();
        StringBuffer stringBuffer=new StringBuffer();
        Object object=exchange.getIn().getBody();
        Map<String,Object> headers=exchange.getIn().getHeaders();
        String token=null!=headers.get(TemplateUitls.Token_Key)?headers.get(TemplateUitls.Token_Key).toString():
                null;
        int tokenStatus=0;
        if(null!=token&&token.length()>0){
            boolean isOk=userService.checkUserAuthority(token,appId);
            if(isOk){
                JSONObject jsonObject=null;
                if(null!=headers.get("Content-Type")
                        &&headers.get("Content-Type").toString().equalsIgnoreCase("application/json")){//
                    if(object instanceof Map){
                        jsonObject=JSONObject.fromObject(object);
                        stringBuffer.append("<soap:"+camelWSDLElementName+"  xmlns:soap=\""+camelWSDLXMLSoap+"\" xmlns:web=\""+camelWSDLTargetNamespace+"\">");
                        stringBuffer.append("<soap:Header/>");
                        stringBuffer.append("<soap:Body>");
                        if(null!=jsonObject){
                            stringBuffer.append(resultJSONObject(jsonObject));
                        }
                        stringBuffer.append("</soap:Body>");
                        stringBuffer.append("</soap:"+camelWSDLElementName+">");
                        exchange.getIn().setBody(stringBuffer.toString());
                    }
                }
                logger.info("stringBuffer:{}",stringBuffer.toString());
                tokenStatus= TemplateUitls.Token_Success_Status_Ok;
            }else{
                logger.info("App-{}:Token is no authority or no valid!",appId);
                tokenStatus= TemplateUitls.Token_Fail_Status_Power_OR_Invalid;//无权访问或者
            }
        }else{
            logger.info("App-{}:Token is null!",appId);
            tokenStatus= TemplateUitls.Token_Fail_Status_Null;//无效Token
        }
        headers.put("tokenStatus",tokenStatus);
        headers.put("startTime",startTime);
        exchange.getIn().setHeaders(headers);
    }
    private String resultJSONObject(JSONObject jsonObject){
        Iterator iterator=jsonObject.keySet().iterator();
        StringBuffer stringBuffer=new StringBuffer();
        while (iterator.hasNext()){
            String key=iterator.next().toString();
            stringBuffer.append("<web:").append(key).append(">");
            JSONObject tempobj= jsonObject.getJSONObject(key);
            if(!tempobj.isEmpty()){
                Iterator it=tempobj.keys();
                while (it.hasNext()){
                    String child_Key=it.next().toString();
                    stringBuffer.append("<web:").append(child_Key).append(">");
                    String value=tempobj.getString(child_Key);
                    stringBuffer.append(value);
                    stringBuffer.append("</web:").append(child_Key).append(">");
                }
            }
            stringBuffer.append("</web:").append(key).append(">");
        }
        return stringBuffer.toString();
    }
}
