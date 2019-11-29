package com.hmily.litespring.test.v4;

import com.hmily.litespring.beans.factory.support.DefaultBeanFactory;
import com.hmily.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.hmily.litespring.context.annotation.AutowiredFieldElement;
import com.hmily.litespring.context.annotation.InjectionElement;
import com.hmily.litespring.context.annotation.InjectionMetadata;
import com.hmily.litespring.core.io.ClassPathResource;
import com.hmily.litespring.core.io.Resource;
import com.hmily.litespring.dao.v4.AccountDao;
import com.hmily.litespring.dao.v4.ItemDao;
import com.hmily.litespring.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class InjectionMetadataTest {

    @Test
    public void testInjection() throws Exception{

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        Class<?> clz = PetStoreService.class;
        LinkedList<InjectionElement> elements = new LinkedList<InjectionElement>();

        {
            Field f = PetStoreService.class.getDeclaredField("accountDao");
            InjectionElement injectionElem = new AutowiredFieldElement(f,true,factory);
            elements.add(injectionElem);
        }
        {
            Field f = PetStoreService.class.getDeclaredField("itemDao");
            InjectionElement injectionElem = new AutowiredFieldElement(f,true,factory);
            elements.add(injectionElem);
        }

        InjectionMetadata metadata = new InjectionMetadata(clz,elements);

        PetStoreService petStore = new PetStoreService();

        metadata.inject(petStore);

        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);

        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);

    }


}
