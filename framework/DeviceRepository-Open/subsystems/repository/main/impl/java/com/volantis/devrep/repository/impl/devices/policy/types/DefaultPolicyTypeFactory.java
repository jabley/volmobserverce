package com.volantis.devrep.repository.impl.devices.policy.types;

import com.volantis.devrep.repository.api.devices.policy.types.PolicyTypeFactory;
import com.volantis.mcs.devices.policy.types.TextPolicyType;

public class DefaultPolicyTypeFactory
        extends PolicyTypeFactory {

    public TextPolicyType createTextPolicyType() {
        return new DefaultTextPolicyType();
    }
}
