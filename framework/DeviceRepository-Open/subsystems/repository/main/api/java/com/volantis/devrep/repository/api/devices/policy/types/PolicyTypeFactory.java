package com.volantis.devrep.repository.api.devices.policy.types;

import com.volantis.synergetics.factory.MetaDefaultFactory;
import com.volantis.mcs.devices.policy.types.TextPolicyType;

public abstract class PolicyTypeFactory {

    /**
     * Set up the meta default factory instance
     */
    private static MetaDefaultFactory metaDefaultFactory =
        new MetaDefaultFactory(
            "com.volantis.devrep.repository.impl.devices.policy.types.DefaultPolicyTypeFactory",
            PolicyTypeFactory.class.getClassLoader());

    /**
     * @return the default instance of the factory.
     */
    public static PolicyTypeFactory getDefaultInstance() {
        return (PolicyTypeFactory)
            metaDefaultFactory.getDefaultFactoryInstance();
    }

    public abstract TextPolicyType createTextPolicyType();
}
