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

package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.protocols.dissection.DissectionConstants;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * This class provides access to an abstraction of the special dissection
 * elements that may be useful when implementing a DissectableDocument.
 */
public class DissectionElementTypes {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private static ElementType DISSECTABLE_AREA_TYPE = new ElementType() {

        public String getDescription() {
            return "DISSECTABLE-AREA";
        }

        public void invoke(DocumentVisitor visitor,
                           DissectableElement element)
            throws DissectionException {
            visitor.visitDissectableArea(element);
        }
    };

    private static ElementType KEEP_TOGETHER_TYPE = new ElementType() {

        public String getDescription() {
            return "KEEP-TOGETHER";
        }

        public void invoke(DocumentVisitor visitor,
                           DissectableElement element)
            throws DissectionException {
            visitor.visitKeepTogether(element);
        }
    };

    private static ElementType SHARD_LINK_TYPE = new ElementType() {

        public String getDescription() {
            return "SHARD-LINK";
        }

        public void invoke(DocumentVisitor visitor,
                           DissectableElement element)
            throws DissectionException {
            visitor.visitShardLink(element);
        }
    };

    private static ElementType SHARD_LINK_GROUP_TYPE = new ElementType() {

        public String getDescription() {
            return "SHARD-LINK-GROUP";
        }

        public void invoke(DocumentVisitor visitor,
                           DissectableElement element)
            throws DissectionException {
            visitor.visitShardLinkGroup(element);
        }
    };

    private static ElementType SHARD_LINK_CONDITIONAL_TYPE = new ElementType() {

        public String getDescription() {
            return "SHARD-LINK-CONDITIONAL";
        }

        public void invoke(DocumentVisitor visitor,
                           DissectableElement element)
            throws DissectionException {
            visitor.visitShardLinkConditional(element);
        }
    };

    private static ElementType PLAIN_ELEMENT_TYPE = new ElementType() {

        public String getDescription() {
            return "PLAIN-ELEMENT";
        }

        public void invoke(
            DocumentVisitor visitor,
            DissectableElement element)
            throws DissectionException {
            visitor.visitElement(element);
        }
    };

    private static Map ALL_TYPES = new HashMap();

    static {
        ALL_TYPES.put(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT, DISSECTABLE_AREA_TYPE);
        ALL_TYPES.put(DissectionConstants.KEEPTOGETHER_ELEMENT, KEEP_TOGETHER_TYPE);
        ALL_TYPES.put(DissectionConstants.SHARD_LINK_ELEMENT, SHARD_LINK_TYPE);
        ALL_TYPES.put(DissectionConstants.SHARD_LINK_GROUP_ELEMENT, SHARD_LINK_GROUP_TYPE);
        ALL_TYPES.put(DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT, SHARD_LINK_CONDITIONAL_TYPE);
        ALL_TYPES.put(null, PLAIN_ELEMENT_TYPE);
    }

    /**
     * Get an ElementType object that will invoke the
     * {@link DocumentVisitor#visitDissectableArea} method.
     * @return An ElementType object.
     */
    public static ElementType getDissectableAreaType() {
        return DISSECTABLE_AREA_TYPE;
    }

    /**
     * Get an ElementType object that will invoke the
     * {@link DocumentVisitor#visitKeepTogether} method.
     * @return An ElementType object.
     */
    public static ElementType getKeepTogetherType() {
        return KEEP_TOGETHER_TYPE;
    }

    /**
     * Get an ElementType object that will invoke the
     * {@link DocumentVisitor#visitShardLink} method.
     * @return An ElementType object.
     */
    public static ElementType getShardLinkType() {
        return SHARD_LINK_TYPE;
    }

    /**
     * Get an ElementType object that will invoke the
     * {@link DocumentVisitor#visitShardLinkGroup} method.
     * @return An ElementType object.
     */
    public static ElementType getShardLinkGroupType() {
        return SHARD_LINK_GROUP_TYPE;
    }

    /**
     * Get an ElementType object that will invoke the
     * {@link DocumentVisitor#visitShardLinkConditional} method.
     * @return An ElementType object.
     */
    public static ElementType getShardLinkConditionalType() {
        return SHARD_LINK_CONDITIONAL_TYPE;
    }

    /**
     * Get an ElementType object that will invoke the
     * {@link DocumentVisitor#visitElement} method.
     * @return An ElementType object.
     */
    public static ElementType getPlainElementType() {
        return PLAIN_ELEMENT_TYPE;
    }

    public static Map getAllTypes() {
        return ALL_TYPES;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
