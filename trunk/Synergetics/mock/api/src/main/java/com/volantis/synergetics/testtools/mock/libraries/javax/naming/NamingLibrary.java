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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools.mock.libraries.javax.naming;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.Referenceable;

/**
 *Triggers auto generation of classes within<code>javax.naming</code>and
 *contained packages for which the source is not available.
 *
 *@mock.generate library="true"
 */
public class NamingLibrary {

    /**
     * @mock.generate interface="true"
     */
    public Context context;

    /**
     * @mock.generate interface="true"
     */
    public Name name;

    /**
     * @mock.generate interface="true"
     */
    public NameParser nameParser;

    /**
     * @mock.generate interface="true"
     */
    public NamingEnumeration namingEnumeration;

    /**
     * @mock.generate interface="true"
     */
    public Referenceable referenceable;

}
