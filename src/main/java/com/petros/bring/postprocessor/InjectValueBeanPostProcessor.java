package com.petros.bring.postprocessor;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Value;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;
import com.petros.bring.environment.ApplicationEnvironment;
import com.petros.bring.environment.PropertyData;
import com.petros.bring.environment.PropertyResolver;
import com.petros.bring.environment.convert.TypeConversionService;
import com.petros.bring.exception.UnsatisfiedPropertyValueException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * Implementation of PostProcessor needed for injection values to fields annotated with annotation {@link Value}
 */
@Component
public class InjectValueBeanPostProcessor implements BeanPostProcessor {

    private final BeanFactory factory;

    private final PropertyResolver propertyResolver;

    private final TypeConversionService typeConversionService;

    public InjectValueBeanPostProcessor(AnnotationConfigApplicationContext beanFactory,
                                        PropertyResolver propertyResolver,
                                        TypeConversionService typeConversionService) {
        this.factory = beanFactory;
        this.propertyResolver = propertyResolver;
        this.typeConversionService = typeConversionService;
    }

    @Override
    public Object postProcessBeforeInitialization(Class<?> beanType, Object obj) {

        var annotatedFields = Arrays.stream(beanType.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Value.class));

        annotatedFields.forEach(field -> injectValue(field, obj));
        return obj;
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

        var resultedValue = propertyResolver.canHandle(annotationValue)
                ? propertyResolver.handle(annotationValue, type)
                : new PropertyData(annotationValue);


        field.setAccessible(true);

        Object convertedValue = typeConversionService.convertValueIfPossible(resultedValue.getValue(), String.class, type);
        try {
            field.set(bean, convertedValue);
        } catch (IllegalAccessException e) {
            throw new UnsatisfiedPropertyValueException(e.getLocalizedMessage());
        }


    }
}
