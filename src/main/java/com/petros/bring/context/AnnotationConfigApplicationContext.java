package com.petros.bring.context;

import com.petros.bring.bean.factory.AnnotationBeanFactory;
import com.petros.bring.exception.BeanDefinitionOverrideException;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;

import java.util.Arrays;

public class AnnotationConfigApplicationContext extends AnnotationBeanFactory {

    private final BeanDefinitionRegistry registry;

    public AnnotationConfigApplicationContext(BeanDefinitionRegistry registry) {
        this.registry = registry;
        Arrays.stream(registry.getBeanDefinitionNames())
                .map(registry::getBeanDefinition)
                .forEach(this::registerBean);
    }

    private void registerBean(BeanDefinition beanDefinition) {
        var beanName = beanDefinition.getName();
        var existingBeanDefinition = rootContextMap.get(beanName);
        if (existingBeanDefinition != null) {
            throw new BeanDefinitionOverrideException(String.format(
                    "Cannot register bean definition [%s] for bean '%s': There is already [%s] bound.",
                    beanDefinition, beanName, existingBeanDefinition
            ));
        }
        rootContextMap.put(beanName, beanDefinition);
    }

}
