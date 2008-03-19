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
/** (c) Volantis Systems Ltd 2004.  */
package com.volantis.mcs.project.impl;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.project.LocalPolicySource;
import com.volantis.mcs.repository.LocalRepository;


/**
 * Abstract class which is extended by local policy source classes.
 */
public abstract class LocalPolicySourceImpl
        extends AbstractPolicySource
        implements LocalPolicySource {

    /**
     * The underlying repository.
     */
    protected final LocalRepository repository;

    /**
     * The {@link PolicyBuilderAccessor} for this source.
     */
    private final PolicyBuilderAccessor accessor;

    /**
     * Initialise.
     *
     * @param repository The {@link LocalRepository} for this source.
     * @param accessor   The {@link PolicyBuilderAccessor} for this source.
     * @param reader     The {@link PolicyBuilderReader} for this source.
     */
    protected LocalPolicySourceImpl(
            LocalRepository repository,
            PolicyBuilderAccessor accessor,
            PolicyBuilderReader reader) {
        super(reader);
        this.repository = repository;
        this.accessor = accessor;
    }

    // Javadoc inherited.
    public LocalRepository getRepository() {
        return repository;
    }

    // Javadoc inherited.
    public PolicyBuilderAccessor getPolicyBuilderAccessor() {
        return accessor;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Jan-04	2769/1	mat	VBM:2004012702 Add PolicySource

 ===========================================================================
*/
