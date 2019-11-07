package com.pactera.route;

import com.pactera.proccess.CamelProccess;
import com.pactera.proccess.CamelResponseBodyRoute;
import com.pactera.proccess.GlobalExceptionProccess;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author simonMeng
 * @version 1.0
 * @date 2019/10/16
 **/
@Component
public class XML2JSONRoute extends RouteBuilder {
    public Logger logger= LoggerFactory.getLogger(XML2JSONRoute.class);
    @Autowired
    private XmlJsonDataFormat xmlJsonFormat;
    @Autowired
    private GlobalExceptionProccess globalExceptionProccess;

    @Override
    public void configure() throws Exception {
        onException(java.lang.Exception.class)
                .handled(true)
                .redeliveryDelay(2)
                .process(globalExceptionProccess);

        from("direct:responseGzipConvert").unmarshal().gzip().to("direct:marshalEmployeexml2json");
        try {
            // xml to json数据格式的转换
            from("direct:marshalEmployeexml2json")
                    .to("bean:camelResponseBodyRoute?method=process")
                    .marshal().xmljson()
                    .to("bean:myDataformat?method=process")

                    .to("log:?level=INFO&showBody=true");
        }catch (Exception e){
            logger.error("this is xml convert to json data found error:{}",e.getMessage());
        }
        // json to xml数据格式转换
        from("direct:unMarshalEmployeejson2xml")
                .unmarshal(xmlJsonFormat)
                .to("log:?level=INFO&showBody=true");
    }
}
