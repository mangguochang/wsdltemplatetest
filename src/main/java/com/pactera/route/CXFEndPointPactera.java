package com.pactera.route;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.DataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.DefaultValue;
import java.util.HashMap;
import java.util.Map;

/**
 * @author simonMeng
 * @version 1.0
 * @date 2019/11/1
 **/
/*@Component("cXFEndPointPactera")*/
public class CXFEndPointPactera extends CxfEndpoint {

    public CXFEndPointPactera(){
        Map<String,Object> properties=new HashMap<>();
        properties.put("xmlns:ns1","http://WebXml.com.cn/");
        properties.put("id","cXFEndPointPactera");

        this.setProperties(properties);
    }

    @Override
    public void setWsdlURL(@Value("${camel.wsdl.url}")String url) {
        super.setWsdlURL(url+"?wsdl");
    }

    @Override
    public void setDataFormat(@DefaultValue("RAW") DataFormat format) {
        super.setDataFormat(format);
    }

    @Override
    public void setServiceNameString(@Value("${camel.wsdl.serviceName}") String service) {
        super.setServiceNameString(service);
    }

    @Override
    public void setEndpointNameString(@Value("${camel.wsdl.endpointName}") String port) {
        super.setEndpointNameString(port);
    }

    @Override
    public void setBeanId(@DefaultValue("cXFEndPointPactera") String beanId) {
        super.setBeanId(beanId);
    }

}
