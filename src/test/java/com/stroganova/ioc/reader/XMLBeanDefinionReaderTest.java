package com.stroganova.ioc.reader;

import com.stroganova.ioc.entity.BeanDefinition;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XMLBeanDefinionReaderTest {

    @Before
    public void before() {
        //prepare
//<beans>
//    <bean id="spamService1" class="com.stroganova.ioc.SpamService">
//        <property name="spamCount" value="5"/>
//        <property name="mailService" ref="mailServiceForSpam"/>
//    </bean>
// <bean id="spamService2" class="com.stroganova.ioc.SpamService">
//        <property name="spamCount" value="5"/>
//        <property name="mailService" ref="mailServiceForSpam"/>
//    </bean>
// </beans>

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element beansRoot = doc.createElement("beans");
            doc.appendChild(beansRoot);

            Element bean = doc.createElement("bean");
            bean.setAttribute("id", "spamService1");
            bean.setAttribute("class", "com.stroganova.ioc.SpamService");

            Element propertyDef = doc.createElement("property");
            propertyDef.setAttribute("name", "spamCount");
            propertyDef.setAttribute("value", "5");

            Element propertyRef = doc.createElement("property");
            propertyRef.setAttribute("name", "mailService");
            propertyRef.setAttribute("ref", "mailServiceForSpam");

            bean.appendChild(propertyDef);
            bean.appendChild(propertyRef);
            beansRoot.appendChild(bean);

            Element second = doc.createElement("bean");
            second.setAttribute("id", "spamService2");
            second.setAttribute("class", "com.stroganova.ioc.SpamService");

            Element secondPropertyDef = doc.createElement("property");
            secondPropertyDef.setAttribute("name", "spamCount");
            secondPropertyDef.setAttribute("value", "5");

            Element secondPropertyRef = doc.createElement("property");
            secondPropertyRef.setAttribute("name", "mailService");
            secondPropertyRef.setAttribute("ref", "mailServiceForSpam");

            second.appendChild(secondPropertyDef);
            second.appendChild(secondPropertyRef);
            beansRoot.appendChild(second);


            // output DOM XML to file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("testContext.xml"));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        try {
            Files.deleteIfExists(Paths.get("testContext.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBeanDefinitions() throws Exception {

        BeanDefinition second = new BeanDefinition();
        second.setId("spamService2");
        second.setBeanClassName("com.stroganova.ioc.SpamService");
        Map<String, String> secondDependencies = new HashMap<>();
        secondDependencies.put("spamCount", "5");
        second.setDependencies(secondDependencies);
        Map<String, String> secondRefDependencies2 = new HashMap<>();
        secondRefDependencies2.put("mailService", "mailServiceForSpam");
        second.setRefDendencies(secondRefDependencies2);


        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setId("spamService1");
        beanDefinition.setBeanClassName("com.stroganova.ioc.SpamService");
        Map<String, String> dependencies = new HashMap<>();
        dependencies.put("spamCount", "5");
        beanDefinition.setDependencies(dependencies);
        Map<String, String> refDependencies = new HashMap<>();
        refDependencies.put("mailService", "mailServiceForSpam");
        beanDefinition.setRefDendencies(refDependencies);


        List<BeanDefinition> expectedBeanDefinitions = new ArrayList<>();
        expectedBeanDefinitions.add(beanDefinition);
        expectedBeanDefinitions.add(second);
        //when
        XMLBeanDefinionReader reader = new XMLBeanDefinionReader();
        reader.setPath("testContext.xml");
        List<BeanDefinition> actualBeanDefinitions = reader.readBeanDefinitions();
        //then
        Assert.assertEquals(expectedBeanDefinitions, actualBeanDefinitions);
    }

}