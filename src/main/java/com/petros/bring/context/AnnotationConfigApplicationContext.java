package com.petros.bring.context;

import com.petros.bring.bean.factory.AnnotationBeanFactory;
import com.petros.bring.exception.BeanDefinitionOverrideException;
import com.petros.bring.postprocessor.BeanPostProcessor;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class AnnotationConfigApplicationContext extends AnnotationBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors;

    public AnnotationConfigApplicationContext(BeanDefinitionRegistry registry, List<BeanPostProcessor> beanPostProcessors) {
        super(registry);
        this.beanPostProcessors = beanPostProcessors;
        Arrays.stream(registry.getBeanDefinitionNames())
                .map(registry::getBeanDefinition)
                .forEach(this::registerBean);
    }

    private void registerBean(BeanDefinition beanDefinition) {
        validate(beanDefinition);
        Object bean = createBean(beanDefinition);
        rootContextMap.put(beanDefinition.getName(), bean);
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

    private Object createBean(BeanDefinition beanDefinition) {
        try {
            Object obj = null;
            Class<?> aClass = Class.forName(beanDefinition.getBeanClassName());
            if (beanDefinition.getDependsOn() != null) {
//                Map<String, Object> beansByName = new HashMap<>();
//                for (String dependsOnName : beanDefinition.getDependsOn()) {
//                    Class<?> bClass = Class.forName(beanDefinition.getBeanClassName());
//                    beansByName.put(dependsOnName, getBean(bClass));
//                }
                // check for circular dependencies
//                obj = aClass.getConstructor().newInstance() with non default constructor
            } else {
                obj = aClass.getConstructor().newInstance();
            }
            for (BeanPostProcessor postProcessor : beanPostProcessors) {
                postProcessor.postProcessBeforeInitialization(aClass, obj);
                postProcessor.postProcessAfterInitialization(aClass, obj);
            }
            return obj;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
