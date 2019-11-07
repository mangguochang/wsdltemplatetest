package com.pactera.proccess;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Map;

/**
 * @author simonMeng
 * @version 1.0
 * @date 2019/11/6
 **/
@Component("camelResponseBodyRoute")
public class CamelResponseBodyRoute {
    private Logger logger = LoggerFactory.getLogger(CamelResponseProccess.class);

    public void process(Exchange exchange, @Body String body){
        Map<String,Object> objectMap=exchange.getIn().getHeaders();
        boolean isXMLData=false;
        try {
            Document document= DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(body)));
            isXMLData=true;
            exchange.getIn().setBody(exchange.getIn().getBody());
            logger.info("this response content is xml data!");
            logger.info("Content:");
            logger.info("===================================================");
            logger.info(body);
            logger.info("===================================================");
        }catch (Exception e){
            exchange.getIn().setBody(body);
            logger.error("this response content not is xml data:\n{}",body);
        }
        objectMap.put("isXML",isXMLData);
        exchange.getIn().setHeaders(objectMap);

    }


}
