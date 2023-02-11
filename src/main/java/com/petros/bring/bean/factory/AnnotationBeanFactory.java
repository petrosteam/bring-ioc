package com.petros.bring.bean.factory;

import com.petros.bring.annotations.Primary;
import com.petros.bring.exception.BeanCreationException;
import com.petros.bring.exception.CircularDependencyException;
import com.petros.bring.exception.NoSuchBeanException;
import com.petros.bring.exception.NoUniqueBeanException;
import com.petros.bring.postprocessor.BeanPostProcessor;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.petros.bring.Utils.getClassByName;

@Primary
@Slf4j
public class AnnotationBeanFactory implements BeanFactory {

    protected BeanDefinitionRegistry registry;
    protected static final Map<String, Object> rootContextMap = new ConcurrentHashMap<>();
    private final Set<String> circularDependencies = new LinkedHashSet<>();

    public AnnotationBeanFactory(BeanDefinitionRegistry registry) {
        this.registry = registry;
        rootContextMap.clear();
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        return getOptionalBean(beanType)
                .or(() -> this.getPrototypeBeanByType(beanType))
                .orElseThrow(() -> new NoSuchBeanException("Bean with type %s not found".formatted(beanType.getName()))
                );
    }

    /**
     * Getting optional bean with prototype scope.
     * If bean was defined with a prototype scope, the new object will be created on every call to the context
     * @param beanType bean type
     * @return Optional of bean object
     * @param <T> bean type
     */
    public <T> Optional<T> getPrototypeBeanByType(Class<T> beanType) {
        return this.registry.getBeanDefinitionsByType(beanType)
                .stream()
                .filter(beanDefinition -> beanDefinition.getScope().equals(Scope.PROTOTYPE))
                .map(this::createBean)
                .map(beanType::cast)
                .findFirst();
    }

    /**
     * Getting optional bean from the context by the bean type
     * If more than one bean registered with this type, the primary bean is retrieved
     * @param beanType bean type
     * @return optional of bean object
     * @param <T> object type
     */
    private <T> Optional<T> getOptionalBean(Class<T> beanType) {
        var matchingBeans = getAllBeans(beanType);
        if (matchingBeans.size() > 1) {
            return registry.getPrimaryBeanDefinition(beanType)
                    .map(BeanDefinition::getName)
                    .map(rootContextMap::get)
                    .map(beanType::cast);
        }
        return matchingBeans.values().stream()
                .findFirst();
    }

    /**
     * Creating bean from the bean definition
     * @param beanDefinition bean definition
     * @return bean object
     * @param <T> type of the bean
     */
    public <T> T create(BeanDefinition beanDefinition) {
        log.trace("[{}] creating bean", beanDefinition.getName());
        var bean = this.<T>createBean(beanDefinition);
        if (!beanDefinition.getScope().equals(Scope.PROTOTYPE)) {
            log.trace("[{}] has SINGLETON scope. Adding to context", beanDefinition.getName());
            rootContextMap.put(beanDefinition.getName(), bean);
        }
        log.trace("[{}] has been created", beanDefinition.getName());
        return bean;
    }

    /**
     * Post process bean object with all registered post processors in the context
     * @param beanType bean type
     * @param bean bean object
     */
    public void postProcessBean(Class<?> beanType, Object bean) {
        for (BeanPostProcessor postProcessor : getAllBeans(BeanPostProcessor.class).values()) {
            log.trace("[{}] post processing started", beanType.getSimpleName());
            postProcessor.postProcessBeforeInitialization(beanType, bean);
            postProcessor.postProcessAfterInitialization(beanType, bean);
            log.trace("[{}] post processing finished", beanType.getSimpleName());
        }
    }

    /**
     * Creating bean from bean definition
     * This method does not make any validation and used only for bean instantiation
     * @param beanDefinition bean definition
     * @return bean object
     * @param <T> bean type
     */
    @SuppressWarnings("unchecked")
    private <T> T createBean(BeanDefinition beanDefinition) {
        log.trace("[{}] instantiation", beanDefinition.getName());
        try {
            var clazz = getClassByName(beanDefinition.getBeanClassName());
            if (beanDefinition.getDependsOn() != null && beanDefinition.getDependsOn().length != 0) {
                log.trace("[{}] contains dependant beans", beanDefinition.getName());
                var beansByType = new LinkedHashMap<>();
                for (Class<?> dependsOnClass : beanDefinition.getDependsOn()) {
                    var beanToInject = this.getBeanToInject(dependsOnClass);
                    beansByType.put(dependsOnClass, beanToInject);
                    log.trace("[{}] adding bean to inject {}", beanDefinition.getName(), clazz.getName());
                }
                if (beanDefinition.getFactoryMethod() != null) {
                    log.trace("[{}] creating bean by method with parameters", beanDefinition.getName());
                    return createBeanByMethod(beanDefinition, beansByType);
                }
                log.trace("[{}] creating bean with non-default constructor", beanDefinition.getName());
                return (T) clazz.getConstructor(beansByType.keySet().toArray(new Class<?>[]{}))
                        .newInstance(beansByType.values().toArray());
            } else {
                if (beanDefinition.getFactoryMethod() != null) {
                    log.trace("[{}] creating bean by method without parameters", beanDefinition.getName());
                    return createBeanByMethod(beanDefinition, null);
                }
                log.trace("[{}] creating bean with default constructor", beanDefinition.getName());
                return (T) clazz.getConstructor().newInstance();
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            log.error("[{}] Could not instantiate bean: {}", beanDefinition.getName(), e);
            throw new BeanCreationException(beanDefinition, e);
        }
    }

    private <T> T createBeanByMethod(BeanDefinition beanDefinition, LinkedHashMap<Object, Object> beansByType) throws IllegalAccessException, InvocationTargetException {
        Object bean = getBean(beanDefinition.getFactoryBeanClass());
        Method factoryMethod = beanDefinition.getFactoryMethod();
        factoryMethod.setAccessible(true);
        return beansByType != null
                ? (T) factoryMethod.invoke(bean, beansByType.values().toArray())
                : (T) factoryMethod.invoke(bean);
    }

    /**
     * Getting bean for injection. If bean is not found, we create another one
     * @param dependsOnClass dependant bean class
     * @return bean object
     * @param <T> bean type
     */
    private <T> Object getBeanToInject(Class<T> dependsOnClass) {
        return getOptionalBean(dependsOnClass).orElseGet(() -> createDependentBean(dependsOnClass));
    }

    private <T> T createDependentBean(Class<T> dependsOnClass) {
        checkCircularDependency(dependsOnClass.getSimpleName());
        Object o = this.registry.getBeanDefinitionsByType(dependsOnClass).stream()
                .findFirst()
                .map(this::create)
                .orElseThrow(
                        () -> new BeanCreationException("Could not create bean with type: " + dependsOnClass.getName())
                );
        circularDependencies.remove(dependsOnClass.getSimpleName());
        return dependsOnClass.cast(o);
    }

    private void checkCircularDependency(String name) {
        if (circularDependencies.contains(name)) {
            circularDependencies.add(name + "!");
            throw new CircularDependencyException(String.format("Could not initialize application. " +
                    "Circular dependencies are found: " + String.join("->", circularDependencies)));
        }
        circularDependencies.add(name);
    }


    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        return rootContextMap.entrySet().stream()
                .filter(beanEntry -> name.equals(beanEntry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .map(beanType::cast)
                .orElseThrow(() -> new NoSuchBeanException("No bean named " + name + " available"));
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return rootContextMap.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }
}