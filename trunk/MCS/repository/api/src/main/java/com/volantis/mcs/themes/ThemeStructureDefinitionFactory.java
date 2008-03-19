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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.themes;

import com.volantis.shared.content.BinaryContentInput;
import com.volantis.shared.jibx.ContentUnmarshaller;
import org.jibx.runtime.JiBXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 *
 */

public abstract class ThemeStructureDefinitionFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
            new MetaDefaultFactory(
                "com.volantis.mcs.themes.impl." +
                    "DefaultThemeStructureDefinitionFactory",
                ThemeStructureDefinitionFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ThemeStructureDefinitionFactory getDefaultInstance() {
        return (ThemeStructureDefinitionFactory)
            metaDefaultFactory.getDefaultFactoryInstance();
    }


    public abstract Map getThemeStructureDefinitionMap();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10779/1	geoff	VBM:2005121202 MCS35: WML vertical whitespace fix does not handle mode settings (take 2)

 09-Dec-05	10756/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 16-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 01-Nov-05	9965/4	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 27-Oct-05	9965/3	ianw	VBM:2005101811 interim commit

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 ===========================================================================
*/
