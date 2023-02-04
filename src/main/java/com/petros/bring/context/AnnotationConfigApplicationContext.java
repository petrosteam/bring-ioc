package com.petros.bring.context;

import com.petros.bring.annotations.Component;
import com.petros.bring.bean.factory.AnnotationBeanFactory;
import com.petros.bring.reader.BeanDefinitionRegistry;

import static com.petros.bring.Utils.getClassByName;

@Component(name = "annotationConfigApplicationContext")
public class AnnotationConfigApplicationContext extends AnnotationBeanFactory {

    public AnnotationConfigApplicationContext(BeanDefinitionRegistry registry) {
        super(registry);
        register(registry);
    }

    public void register(BeanDefinitionRegistry registry) {
        registry.getBeanDefinitions().forEach(this::create);
//        var beans = Arrays.stream(registry.getBeanDefinitionNames())
//                .map(registry::getBeanDefinition)
//                .map(this::create)
//                .collect(Collectors.toSet());
        rootContextMap.values().forEach(bean -> postProcessBean(getClassByName(bean.getClass().getName()), bean));
    }
}
