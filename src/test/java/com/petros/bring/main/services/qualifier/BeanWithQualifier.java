package com.petros.bring.main.services.qualifier;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Qualifier;

@Component
public class BeanWithQualifier {

    @Autowired
    @Qualifier("qualifierDependency")
    private QualifierDependency dependency;

    public QualifierDependency getDependency() {
        return dependency;
    }
}
