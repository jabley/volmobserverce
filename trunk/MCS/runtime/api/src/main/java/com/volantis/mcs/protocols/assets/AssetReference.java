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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.assets;

/**
 * An AssetReference provides an abstraction from an Asset and allows for
 * an Asset to be represented lazily.  The actual asset is only retrieved
 * from a repository once it is requested.
 *
 * <p>
 * Asset references are not threadsafe and therefore are not cacheable in the
 * current design.  Any changes to this should also involve updating all
 * code implementing this interface.
 * </p>
 */
public interface AssetReference {
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Nov-05	10410/1	pabbott	VBM:2005101703 Recover if rollover image asset not found

 23-Nov-05	10408/1	pabbott	VBM:2005101703 Recover if rollover image asset not found

 23-Nov-05	10400/1	pabbott	VBM:2005101703 Recover if rollover image asset not found

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/1	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 ===========================================================================
*/
