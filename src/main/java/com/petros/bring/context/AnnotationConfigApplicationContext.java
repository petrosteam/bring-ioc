package com.petros.bring.context;

import com.petros.bring.annotations.Component;
import com.petros.bring.bean.factory.AnnotationBeanFactory;
import com.petros.bring.reader.BeanDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;

import static com.petros.bring.Utils.getClassByName;

@Component(name = "annotationConfigApplicationContext")
@Slf4j
public class AnnotationConfigApplicationContext extends AnnotationBeanFactory {
    private final String REGISTRY = "registry";

    public AnnotationConfigApplicationContext(BeanDefinitionRegistry registry) {
        super(registry);
        register();
    }

    /**
     * Register beans from bean definition (creating new beans)
     * This method replaces a default registry bean if it was set
     *
     * We need to override registry when we add client package to scan.
     * Our bring implementation scans
     */
    public void register() {
        log.trace("Setting registry...");
        rootContextMap.put(REGISTRY, registry);
        log.debug("Bean registration started");
        for (var beanDefinition : registry.getBeanDefinitions()) {
            log.trace("[{}] start processing bean", beanDefinition.getName());
            if (rootContextMap.containsKey(beanDefinition.getName())) {
                log.trace("[{}] has been already created. Skipping.", beanDefinition.getName());
                continue;
            }
            this.create(beanDefinition);
        }
        rootContextMap.values().forEach(bean -> postProcessBean(getClassByName(bean.getClass().getName()), bean));
        log.debug("Bean registration finished");
    }
}
