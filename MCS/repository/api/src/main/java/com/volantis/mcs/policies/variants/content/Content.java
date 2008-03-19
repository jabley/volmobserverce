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
package com.volantis.mcs.policies.variants.content;

import com.volantis.mcs.policies.variants.Variant;

/**
 * The contents of a {@link Variant}.
 * <p/>
 * Variant content is the information actually used by either MCS or the client
 * as the policy content.
 * <p/>
 * The various sub-types of this interface define the actual content types that
 * may be used.
 * <p/>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see Variant
 * @see ContentBuilder
 * @see ContentType
 * @since 3.5.1
 */
public interface Content {

    /**
     * Get a new builder instance for {@link Content}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link ContentBuilder#getContent()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    ContentBuilder getContentBuilder();

    /**
     * Get the type of this content.
     *
     * @return The type of this content.
     */
    ContentType getContentType();

}
