/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.variants.Variant;

/**
 */
public class SelectedVariantImpl
        implements SelectedVariant {

    /**
     * The policy that containes
     */
    private final ActivatedVariablePolicy policy;

    private final Variant variant;

    /**
     * The device on which the variant was selected.
     *
     * <p>This may not be the same as the current device, i.e. it may be a
     * fallback device.</p>
     */
    private final InternalDevice device;

    private final RepositoryObject oldObject;

    public SelectedVariantImpl(
            ActivatedVariablePolicy policy, Variant variant,
            InternalDevice device,
            RepositoryObject oldObject) {
        
        if (policy == null) {
            throw new IllegalArgumentException("policy cannot be null");
        }
        this.policy = policy;
        this.variant = variant;
        this.device = device;
        this.oldObject = oldObject;
    }

    public ActivatedVariablePolicy getPolicy() {
        return policy;
    }

    public Variant getVariant() {
        return variant;
    }

    public InternalDevice getDevice() {
        return device;
    }

    public RepositoryObject getOldObject() {
        return oldObject;
    }
}
