package com.petros.bring.context;

import com.petros.bring.bean.factory.AnnotationBeanFactory;
import com.petros.bring.postprocessor.OrderedBeanDefinitionPostProcessor;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;

import java.util.Arrays;
import java.util.Queue;

public class AnnotationConfigApplicationContext extends AnnotationBeanFactory {

    public AnnotationConfigApplicationContext(BeanDefinitionRegistry registry, Queue<OrderedBeanDefinitionPostProcessor> beanPostProcessors) {
        super(registry, beanPostProcessors);
        registerConfigurationBeans();
        registerBeans();
    }

    private void registerConfigurationBeans() {
        Arrays.stream(registry.getBeanDefinitionNames())
                .map(registry::getBeanDefinition)
                .filter(BeanDefinition::isConfiguration)
                .forEach(this::registerBean);
    }

    private void registerBeans() {
        Arrays.stream(registry.getBeanDefinitionNames())
                .map(registry::getBeanDefinition)
                .forEach(this::registerBean);
    }

    private void registerBean(BeanDefinition beanDefinition) {
        if (needsRegistration(beanDefinition)) {
            getBean(registry.getBeanTypeByName(beanDefinition.getName()));
        }
    }

    private boolean needsRegistration(BeanDefinition beanDefinition) {
        if (Scope.PROTOTYPE.equals(beanDefinition.getScope())) {
            return false;
        }
        return rootContextMap.get(beanDefinition.getName()) == null;
    }
}
