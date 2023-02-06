package com.petros.bring.postprocessor;

/**
 * BeanPostProcessor makes it possible to apply custom modification for created beans.
 * Usually post processors that populate beans via marker interfaces implement postProcessBeforeInitialization,
 * and post processors that wrap beans with proxies - should implement postProcessAfterInitialization.
 */
public interface BeanPostProcessor {

    /**
     * Method is processed before @PostConstruct. Usually uses for dependency injection, filling properties
     * @param bean - bean, that should be modified
     * @param beanClass - class of the bean
     * @return - original bean, not a proxy
     */
    default Object postProcessBeforeInitialization(Class<?> beanClass, Object bean) {
        return bean;
    }

    /**
     * Method is processed after @PostConstruct. Used when custom logic around bean methods is needed.
     * For example with @Cacheable, @Async, @Transactional...
     * @param bean - bean, that should be modified
     * @param beanClass - class of the bean
     * @return - bean wrapped with proxy
     */
    default Object postProcessAfterInitialization(Class<?> beanClass, Object bean) {
        return bean;
    }
}
