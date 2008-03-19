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

package com.volantis.xml.namespace;

import com.volantis.shared.recovery.RecoverableTransaction;

/**
 * This extension of the NamespacePrefixTracker interface provides a common set
 * of methods for those NamespacePrefixTracker which must be able to:
 * <ul>
 * <li> Capture their state at a known instant.
 * <li> Restore the captured state under error conditions.
 * </ul>
 *
 * @see NamespacePrefixTracker
 * @see RecoverableTransaction
 */
public interface RecoverableNamespacePrefixTracker
        extends NamespacePrefixTracker, RecoverableTransaction {
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
