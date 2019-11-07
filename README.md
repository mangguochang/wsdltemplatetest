# WSDLTemplate

#### 介绍
{**以下是码云平台说明，您可以替换此简介**
码云是 OSCHINA 推出的基于 Git 的代码托管平台（同时支持 SVN）。专为开发者提供稳定、高效、安全的云端软件开发协作平台
无论是个人、团队、或是企业，都能够用码云实现代码托管、项目管理、协作开发。企业项目请看 [https://gitee.com/enterprises](https://gitee.com/enterprises)}

#### 软件架构
软件架构说明


#### 安装教程

1.  更新代码下来后，使用idea打开进行运行测试
2.  在spring/camel-context.xml配置文件的格式转换代码，可以根据需要进行修改。
3.  在spring/camel-context.xml配置文件中代码：
            <choice>
                <when>
                    <simple>${in.header.accept} == 'application/json'</simple>
                    <to uri="direct:marshalEmployeexml2json"/>
                </when>
            </choice>
      这段配置代码就 是可以根据用户的请求数据格式不一样返回json/xml。
4.  在spring/camel-context.xml配置中文件的以下代码：
     <bean id="xmlJsonDataFormat" class="org.apache.camel.dataformat.xmljson.XmlJsonDataFormat">
            <property name="encoding" value="UTF-8"/>
            <property name="elementName" value="Envelope"/>
            <property name="removeNamespacePrefixes" value="true"/>
            <property name="forceTopLevelObject" value="true"/>
            <property name="trimSpaces" value="true"/>
            <property name="skipNamespaces" value="true"/>
        </bean>
      主要是针对json数据格式转换xml的需要设置的，主要需要设置elementName的值，指定根节点的配置。

#### 使用说明

1.  目前使用的端口是8089。
2.  可调用的接口URL: http://localhost:8089/rest/service/matchwsdl
    Header参数：
    请求内容类型：ContentType =application/xml 或者ContentType =application/saop-xml 
    接收的数据格式：Accept: application/xml  或者 application/json
    body参数为：
     <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:web="http://WebXml.com.cn/">
       <soap:Header/>
           <soap:Body>
              <web:getSupportProvince/>
           </soap:Body>
     </soap:Envelope>
3.swagger ui url: http://localhost:8089/swagger-ui.html

#### 参与贡献

1.  Fork 本仓库
2.  新建 Master 分支
3.  提交代码
4.  新建 Pull Request


#### 码云特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5.  码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
