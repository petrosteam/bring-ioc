package com.petros.bring.main.services.autowired.fields;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Value;

import java.math.BigDecimal;
import java.math.BigInteger;

@Component(name = "serviceWithInjectedFieldValues")
public class ServiceWithInjectedFieldValues {

    @Value("stringValue1")
    private String defaultStringValue;
    @Value("1")
    private int defaultIntValue;
    @Value("2.0")
    private double defaultDoubleValue;
    @Value("3")
    private long defaultLongValue;
    @Value("4")
    private short defaultShortValue;
    @Value("5")
    private byte defaultByteValue;
    @Value("6")
    private BigInteger bigIntegerValue;
    @Value("7.0")
    private Float floatValue;
    @Value("8.0")
    private BigDecimal bigDecimalValue;
    @Value("${absentStringValFromProperties:defaultValueInsteadOfProperties}")
    private String absentValueFromApplicationProperties;

    public String getDefaultStringValue() {
        return defaultStringValue;
    }

    public int getDefaultIntValue() {
        return defaultIntValue;
    }

    public double getDefaultDoubleValue() {
        return defaultDoubleValue;
    }

    public long getDefaultLongValue() {
        return defaultLongValue;
    }

    public short getDefaultShortValue() {
        return defaultShortValue;
    }

    public byte getDefaultByteValue() {
        return defaultByteValue;
    }

    public String getAbsentValueFromApplicationProperties() {
        return absentValueFromApplicationProperties;
    }

    public BigInteger getBigIntegerValue() {
        return bigIntegerValue;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }
}
