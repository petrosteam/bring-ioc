package com.petros.bring;

import com.petros.bring.annotations.Autowire;
import com.petros.bring.annotations.Service;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.TypesAnnotated;


/**
 * The type Application context.
 */
public class ApplicationContext {

    /**
     * The Context.
     */
    Map<Class<?>, Object> context;

    /**
     * Instantiates a new Application context.
     *
     * @param rootPackage the root package
     */
    public ApplicationContext(String rootPackage) {
        Set<Class<?>> annotatedClasses = getAnnotatedClasses(rootPackage);
        setContext(annotatedClasses);
        InjectFields(context.values());
    }

    private void setContext(Set<Class<?>> annotatedClasses) {
        context = annotatedClasses.stream()
                .collect(Collectors.toMap(Function.identity(), ApplicationContext::createClassInstance));
    }

    private static Set<Class<?>> getAnnotatedClasses(String rootPackage) {
        Reflections reflections = new Reflections(rootPackage);
        Set<Class<?>> annotatedClasses =
                reflections.get(TypesAnnotated.with(Service.class).asClass());
        return annotatedClasses;
    }

    private void InjectFields(Collection<Object> contextObjects) {
        for (Object value : contextObjects) {
            for (Field declaredField : value.getClass().getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowire.class)) {
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(value, getBean(declaredField.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private static Object createClassInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets bean.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the bean
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) context.get(clazz);
    }

}
