package com.pactera.proccess;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Map;


/**
 * Camel过程的类
 * @author simonMeng
 * @version 1.0
 * @date 2019/10/15
 */
//@Component
public class CamelProccess implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Processor");

    }
}
