package com.pertos.bring.postprocessor;

/**
 * BeanPostProcessor makes it possible to apply custom modification for created beans.
 * Usually post processors that populate beans via marker interfaces implement postProcessBeforeInitialization,
 * and post processors that wrap beans with proxies - should implement postProcessAfterInitialization.
 */
public interface BeanPostProcessor {

    /**
     * Method is processed before @PostConstruct. Usually uses for dependency injection, filling properties
     * @param bean - bean, that should be modified
     * @param beanName - bean name
     * @return - original bean, not a proxy
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * Method is processed after @PostConstruct. Used when custom logic around bean methods is needed.
     * For example with @Cacheable, @Async, @Transactional...
     * @param bean - bean, that should be modified
     * @param beanName - bean name
     * @return - bean wrapped with proxy
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
