/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.operations.diselect;

import com.volantis.synergetics.factory.MetaDefaultFactory;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;

/**
 * An api class used to add the diselect rules to a DynamicProcessConfiguration
 */
public abstract class DIRulesRegisterer {
    /**
     * The factory from which to get the default rules register instance.
     */
    private static MetaDefaultFactory FACTORY =
            new MetaDefaultFactory(
                    "com.volantis.xml.pipeline.sax.impl.operations.diselect." +
                    "DefaultDIRulesRegisterer",
                    DIRulesRegisterer.class.getClassLoader());

    /**
     * Returns the default instance of this factory.
     *
     * @return the default instance of this factory
     */
    public static DIRulesRegisterer getDefaultInstance() {
        return (DIRulesRegisterer) FACTORY.getDefaultFactoryInstance();
    }

    /**
     * Registers the the rules against the given dynamic configuration
     *
     * @param dynamicConfiguration the dynamic configuration to
     *   register the rules on
     */
    public abstract void register(DynamicProcessConfiguration dynamicConfiguration);
}