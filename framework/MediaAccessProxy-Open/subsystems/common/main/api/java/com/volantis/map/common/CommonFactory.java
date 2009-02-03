package com.volantis.map.common;

import com.volantis.synergetics.factory.MetaDefaultFactory;
import com.volantis.map.common.param.MutableParameters;

/**
 * Factory for producing instances of common classes.
 */
public abstract class CommonFactory {

    private static final MetaDefaultFactory instance =
        new MetaDefaultFactory("com.volantis.map.common.impl.DefaultCommonFactory",
                               CommonFactory.class.getClassLoader());

    /**
     * Gets an instance of CommonFactory.
     *
     * @return CommonFactory gets an instance of the factory.
     */
    public static CommonFactory getInstance() {
        return (CommonFactory) instance.getDefaultFactoryInstance();
    }

    /**
     * Create an mutable collection of parameters.
     *
     * @return A new instance of parameters.
     */
    public abstract MutableParameters createMutableParameters();
}
