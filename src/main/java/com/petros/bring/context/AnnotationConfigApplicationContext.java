package com.petros.bring.context;

import com.petros.bring.annotations.Component;
import com.petros.bring.bean.factory.AnnotationBeanFactory;
import com.petros.bring.reader.BeanDefinitionRegistry;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.petros.bring.Utils.*;

@Component(name = "annotationConfigApplicationContext")
public class AnnotationConfigApplicationContext extends AnnotationBeanFactory {

    public AnnotationConfigApplicationContext(BeanDefinitionRegistry registry) {
        super(registry);
        register(registry);
    }

    public void register(BeanDefinitionRegistry registry) {
        var beans = Arrays.stream(registry.getBeanDefinitionNames())
                .map(registry::getBeanDefinition)
                .map(this::create)
                .collect(Collectors.toSet());
        beans.forEach(bean -> postProcessBean(getClassByName(bean.getClass().getName()), bean));
    }
}
