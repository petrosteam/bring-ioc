package com.petros.bring.validator;

import com.petros.bring.annotations.Component;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.exception.BeanDefinitionOverrideException;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.Scope;

import static com.petros.bring.Utils.getClassByName;

@Component
public class UniqueBeanDefinitionValidator implements BeanDefinitionValidator {
    private final BeanFactory beanFactory;

    public UniqueBeanDefinitionValidator(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void validate(BeanDefinition beanDefinition) {
        if (Scope.PROTOTYPE.equals(beanDefinition.getScope())) {
            return;
        }
        var existingBean = beanFactory.getBean(beanDefinition.getName(), getClassByName(beanDefinition.getBeanClassName()));
        if (existingBean != null) {
            throw new BeanDefinitionOverrideException(String.format(
                    "Cannot register bean definition [%s] for bean '%s': There is already [%s] bound.",
                    beanDefinition, beanDefinition.getName(), existingBean
            ));
        }
    }
}
