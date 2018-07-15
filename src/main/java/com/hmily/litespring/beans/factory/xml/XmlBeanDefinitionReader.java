package com.hmily.litespring.beans.factory.xml;

import com.hmily.litespring.beans.BeanDefinition;
import com.hmily.litespring.beans.factory.BeanDefinitionStoreException;
import com.hmily.litespring.beans.factory.support.BeanDefinitionRegistry;
import com.hmily.litespring.beans.factory.support.GenericBeanDefinition;
import com.hmily.litespring.core.io.Resource;
import com.hmily.litespring.util.ClassUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by zyzhmily on 2018/7/14.
 */
public class XmlBeanDefinitionReader {

    public static final String ID_ATTRIBUTE="id";

    public static final String CLASS_ATTRIBUTE="class";

    public static final String SCOPE_ATTRIBUTE="scope";

    BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry){
        this.registry=registry;
    }

    public void loadBeanDefinitions(Resource resource){
        InputStream is=null;
        try {
            ClassLoader cl= ClassUtils.getDefaultClassLoader();
            is=resource.getInputStream();
            SAXReader reader=new SAXReader();
            Document doc=reader.read(is);

            Element root=doc.getRootElement();
            Iterator<Element> iter =root.elementIterator();
            while (iter.hasNext()){
                Element ele=(Element) iter.next();
                String  id=ele.attributeValue(ID_ATTRIBUTE);
                String beanClassName=ele.attributeValue(CLASS_ATTRIBUTE);
                BeanDefinition bd=new GenericBeanDefinition(id,beanClassName);
                if (ele.attribute(SCOPE_ATTRIBUTE)!=null){
                     bd.setScope(ele.attributeValue(SCOPE_ATTRIBUTE));
                }
                this.registry.registerBeanDefinition(id,bd);
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(),e);
        }finally {
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}