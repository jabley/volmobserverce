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
 * $Header: /src/voyager/com/volantis/mcs/protocols/dissection/DissectionConstants.java,v 1.6 2003/03/14 16:37:28 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 03-May-02    Paul            VBM:2002042203 - Wrapped all constant values
 *                              with ' to make it clearer in the debug where
 *                              one starts and one ends.
 * 05-Jun-02    Adrian          VBM:2002021103 - Added DISSECTION_HINT constant
 *                              for mariner-disection-hint style property on
 *                              div and tablebody tags.
 * 03-Dec-02    Phil W-S        VBM:2002111208 - Added shard link previous and
 *                              next style class attributes.
 * 14-Mar-03    Doug            VBM:2003030409 - Added the
 *                              GENERATE_NEXT_LINK_FIRST_ATTRIBUTE constant.
 * 29-May-03    Chris W         VBM:2003052702 - Added constants used so that
 *                              the protocol can output shard link markup before
 *                              the dissector is called.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.dissection;

/**
 * This interface contains dissection related constants.
 */
public interface DissectionConstants {

    /* As a result of the new accurate dissection the following constants are  
     * in use.
     */
    /**
     * Name of special element that surrounds a dissecting pane
     */
    public static final String DISSECTABLE_CONTENTS_ELEMENT
            = "DISSECTABLE-CONTENTS";

    /**
     * Special magic url character that the new dissection algorithm will
     * replace with the correct url
     */
    public static final String URL_MAGIC_CHAR = "[url]";

    /**
     * The name of the special element used to denote a dividehint element
     */
    public static final String DIVIDE_HINT_ELEMENT = "DIVIDE-HINT";

    /**
     * The name of the special element used to denote a keeptogether element
     */
    public static final String KEEPTOGETHER_ELEMENT = "KEEP-TOGETHER";

    /**
     * The name of the special element that wraps the shard links.
     */
    public static final String SHARD_LINK_GROUP_ELEMENT = "SHARD-LINK-GROUP";

    /**
     * The name of the special element denoting possibly optional shard link
     * markup.
     */
    public static final String SHARD_LINK_CONDITIONAL_ELEMENT
            = "SHARD-LINK-CONDITIONAL";

    /**
     * The name of the special element denoting a shard link.
     */
    public static final String SHARD_LINK_ELEMENT = "SHARD-LINK";

    /* As a result of the new accurate dissection these constants are deprecated.
    * They cannot be deleted just yet as they are used for xhtml basic
    * dissection which, at the time of writing, still uses the old algorithm.
    */
    public static final String DISSECTING_PANE_NAME_ATTRIBUTE
            = "'DISSECTABLE PANE NAME'";

    public static final String INCLUSION_PATH_ATTRIBUTE
            = "'INCLUSION PATH'";

    public static final String NEXT_SHARD_SHORTCUT_ATTRIBUTE
            = "'NEXT SHARD SHORTCUT'";

    public static final String NEXT_SHARD_LINK_TEXT_ATTRIBUTE
            = "'NEXT SHARD LINK TEXT'";

    public static final String PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE
            = "'PREVIOUS SHARD SHORTCUT'";

    public static final String PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE
            = "'PREVIOUS SHARD LINK TEXT'";

    public static final String MAXIMUM_CONTENT_SIZE_ATTRIBUTE
            = "'MAXIMUM CONTENT SIZE'";

    /**
     * Constant that indicates the ordering of the previous/next shard links.
     */
    public static final String GENERATE_NEXT_LINK_FIRST_ATTRIBUTE
            = "GENERATE_NEXT_LINK_FIRST_ATTRIBUTE";


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 03-Mar-05	7277/1	philws	VBM:2005011906 Port pane styling fix from MCS 3.3

 03-Mar-05	7273/1	philws	VBM:2005011906 Ensure panes are thematically styled as per the requesting XDIME style class specifications

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Jun-03	277/2	chrisw	VBM:2003052702 Merged changes from Metis to Mimas

 05-Jun-03	285/1	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
