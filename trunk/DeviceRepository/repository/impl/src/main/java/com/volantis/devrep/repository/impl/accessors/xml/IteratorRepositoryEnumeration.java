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
package com.volantis.devrep.repository.impl.accessors.xml;

import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;

import java.util.Iterator;

public class IteratorRepositoryEnumeration implements RepositoryEnumeration {

    private Iterator iterator;

    public IteratorRepositoryEnumeration(Iterator iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() throws RepositoryException {
        if (iterator != null) {
            return iterator.hasNext();
        } else {
            return false;
        }
    }

    public Object next() throws RepositoryException {
        if (iterator != null) {
            return iterator.next();
        } else {
            throw new IllegalStateException("Iterator is empty");
        }
    }

    public void close() throws RepositoryException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
