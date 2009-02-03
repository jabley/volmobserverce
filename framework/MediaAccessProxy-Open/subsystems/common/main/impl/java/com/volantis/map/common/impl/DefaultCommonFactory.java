package com.volantis.map.common.impl;

import com.volantis.map.common.CommonFactory;
import com.volantis.map.common.impl.param.DefaultMutableParameters;
import com.volantis.map.common.param.MutableParameters;

/**
 * Default implementation of {@link CommonFactory}.
 */
public class DefaultCommonFactory
        extends CommonFactory {

    // Javadoc inherited.
    public MutableParameters createMutableParameters() {
        return new DefaultMutableParameters();
    }
}
