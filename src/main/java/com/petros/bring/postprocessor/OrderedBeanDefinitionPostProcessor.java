package com.petros.bring.postprocessor;

public abstract class OrderedBeanDefinitionPostProcessor implements BeanPostProcessor {
    protected int order;

    public OrderedBeanDefinitionPostProcessor(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
