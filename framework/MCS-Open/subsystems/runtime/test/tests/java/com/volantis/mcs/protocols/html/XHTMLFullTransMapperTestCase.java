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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.TransMapperTestAbstract;
import com.volantis.mcs.protocols.trans.TransMapper;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 * Tests the XHTMLFullTransMapper.
 */
public class XHTMLFullTransMapperTestCase extends TransMapperTestAbstract {

    // Javadoc inherited.
    public TransMapper getTransMapper(TransformationConfiguration configuration) {
        return new XHTMLFullTransMapper(configuration);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9223/4	emma	VBM:2005080403 Remove style class from within protocols and transformers

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 12-Jul-05	8990/3	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 ===========================================================================
*/
