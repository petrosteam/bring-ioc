package com.petros.bring.postprocessor;

import com.petros.bring.annotations.Value;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.environment.ApplicationEnvironment;
import com.petros.bring.environment.PropertyData;
import com.petros.bring.environment.PropertyResolver;
import com.petros.bring.environment.convert.TypeConversionService;
import com.petros.bring.exception.UnsatisfiedPropertyValueException;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Implementation of PostProcessor needed for injection values to fields annotated with annotation {@link Value}
 */
public class InjectValueBeanPostProcessor implements BeanPostProcessor {

    /**
     * @param beanType              - a class of a bean
     * @param obj                   - an object of a bean
     * @param annotationBeanFactory - factory to get/create a bean
     */
    @Override
    public <T> void postProcessBeforeInitialization(Class<T> beanType, T obj, BeanFactory annotationBeanFactory) {

        var annotatedFields = Arrays.stream(beanType.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Value.class));

        annotatedFields.forEach(field -> injectValue(field, obj));
    }

    /**
     * Value for injection can be obtained in several ways. There may be several strategies described by
     * {@link PropertyResolver} in common {@link ApplicationEnvironment} class.
     *
     * @param field - field of a Class.
     * @param bean  - an object for field value injection.
     */
    private void injectValue(Field field, Object bean) {
        var type = field.getType();
        var annotationValue = field.getAnnotation(Value.class).value();

        var resultedValue = ApplicationEnvironment.getPropertyResolvers().stream()
                .filter(resolver -> resolver.canHandle(annotationValue))
                .findFirst()
                .map(pr -> pr.handle(annotationValue, type))
                .orElse(new PropertyData(annotationValue));


        field.setAccessible(true);

        Object convertedValue = TypeConversionService.convertValueIfPossible(resultedValue.getValue(), String.class, type);
        try {
            field.set(bean, convertedValue);
        } catch (IllegalAccessException e) {
            throw new UnsatisfiedPropertyValueException(e.getLocalizedMessage());
        }


    }
}
