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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.cache.body;

/**
 * This typesafe enumeration class provides us with four states that a
 * CacheBodyOperationProcess may be started in.
 */
public class CacheBodyOperationProcessState {

    /**
     * The volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * This represents a state where the {@link CacheBodyOperationProcess} has
     * not been initialized.
     */
    public static final CacheBodyOperationProcessState UNINITIALIZED =
            new CacheBodyOperationProcessState("uninitialized");

    /**
     * This represents a state where the {@link CacheBodyOperationProcess}
     * both records events and forwards them to the next
     * {@link com.volantis.xml.pipeline.sax.XMLProcess}
     */
    public static final CacheBodyOperationProcessState RECORD_AND_FORWARD =
            new CacheBodyOperationProcessState("record_and_forward");

    /**
     * This represents a state where the {@link CacheBodyOperationProcess}
     * should forward previously recorded events and set the pipeline to
     * suppress events coming from the head of the pipeline.
     */
    public static final CacheBodyOperationProcessState PLAYBACK_AND_SUPPRESS =
            new CacheBodyOperationProcessState("playback_and_suppress");

    /**
     * This represents a state where the {@link CacheBodyOperationProcess}
     * simply forwards all events to the next
     * {@link com.volantis.xml.pipeline.sax.XMLProcess}
     */
    public static final CacheBodyOperationProcessState NO_CACHE_FORWARD_ONLY =
            new CacheBodyOperationProcessState("no_cache_forward_only");

    /**
     * The name of this typesafe enumeration instance.
     */
    private String name;

    /**
     * Create a new CacheBodyOperationProcessState with the given value.
     * This constructor is private to prevent instances other than the static
     * ones defined here being created.
     * @param name - The name for this CacheBodyOperationProcessState.
     */
    private CacheBodyOperationProcessState(String name) {
        this.name = name;
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 09-Jun-03	49/4	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 09-Jun-03	49/2	adrian	VBM:2003060505 Updated xml caching process to include cacheBody element

 ===========================================================================
*/
