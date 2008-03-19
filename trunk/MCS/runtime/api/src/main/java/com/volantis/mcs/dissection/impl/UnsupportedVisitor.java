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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.AbstractDocumentVisitor;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * This class causes all the visitor methods to throw an exception.
 */
public class UnsupportedVisitor
    extends AbstractDocumentVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            UnsupportedVisitor.class);

    public UnsupportedVisitor(DissectableDocument document) {
        super(document);
    }

    public UnsupportedVisitor() {
    }

    public void visitDocumentImpl(DissectableDocument document)
        throws DissectionException {

        throw new DissectionException(
                    exceptionLocalizer.format("unsupported-operation"));
    }

    public void visitElement(DissectableElement element)
        throws DissectionException {

        throw new DissectionException(
                    exceptionLocalizer.format("unsupported-operation"));
    }

    public void visitText(DissectableText text)
        throws DissectionException {

        throw new DissectionException(
                    exceptionLocalizer.format("unsupported-operation"));
    }

    public void visitShardLink(DissectableElement element)
        throws DissectionException {

        throw new DissectionException(
                    exceptionLocalizer.format("unsupported-operation"));
    }

    public void visitShardLinkGroup(DissectableElement element)
        throws DissectionException {

        throw new DissectionException(
                    exceptionLocalizer.format("unsupported-operation"));
    }

    public void visitShardLinkConditional(DissectableElement element)
        throws DissectionException {

        throw new DissectionException(
                    exceptionLocalizer.format("unsupported-operation"));
    }

    public void visitDissectableArea(DissectableElement element)
        throws DissectionException {

        throw new DissectionException(
                    exceptionLocalizer.format("unsupported-operation"));
    }

    public void visitKeepTogether(DissectableElement element)
        throws DissectionException {

        throw new DissectionException(
                    exceptionLocalizer.format("unsupported-operation"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
