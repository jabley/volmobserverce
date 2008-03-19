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
 * $Header: /src/voyager/com/volantis/mcs/components/FallsBackToText.java,v 1.3 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Nov-02    Geoff           VBM:2002111504 - Created. 
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.components;

/**
 * Components which are not themselves text, but may fall back to text
 * should implement this interface.
 * <p>
 * Note: {@link TextComponent} does not implement this, even though it has 
 * {@link TextComponent#getFallbackTextComponentName} because fallbacks to
 * components of the same type are handled by selection policies.  
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated See {@link com.volantis.mcs.policies}.
 */ 
public interface FallsBackToText {

    /**
     * Returns the name of the text component which we may fall back to.
     *
     * @return   name of the text component we may fall back to; may be null.
     */
    String getFallbackTextComponentName();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
