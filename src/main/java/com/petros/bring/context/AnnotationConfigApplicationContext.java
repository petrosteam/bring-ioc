package com.petros.bring.context;

import com.petros.bring.bean.factory.AnnotationBeanFactory;
import com.petros.bring.exception.BeanDefinitionOverrideException;
import com.petros.bring.postprocessor.BeanPostProcessor;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;

import java.util.Arrays;
import java.util.List;

public class AnnotationConfigApplicationContext extends AnnotationBeanFactory {

    public AnnotationConfigApplicationContext(BeanDefinitionRegistry registry, List<BeanPostProcessor> beanPostProcessors) {
        super(registry, beanPostProcessors);
        Arrays.stream(registry.getBeanDefinitionNames())
                .map(registry::getBeanDefinition)
                .forEach(this::registerBean);
    }

    private void registerBean(BeanDefinition beanDefinition) {
        validate(beanDefinition);
        getBean(registry.getBeanTypeByName(beanDefinition.getName()));
    }

    private static void validate(BeanDefinition beanDefinition) {
        if (Scope.PROTOTYPE.equals(beanDefinition.getScope())) {
            return;
        }
        var existingBean = rootContextMap.get(beanDefinition.getName());
        if (existingBean != null) {
            throw new BeanDefinitionOverrideException(String.format(
                    "Cannot register bean definition [%s] for bean '%s': There is already [%s] bound.",
                    beanDefinition, beanDefinition.getName(), existingBean
            ));
        }
    }
}
