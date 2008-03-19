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
 * $Header: /src/voyager/com/volantis/mcs/protocols/voicexml/VoiceXMLGrammar.java,v 1.1 2003/04/28 16:14:55 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 01-Aug-01    Paul            VBM:2001072506 - Renamed generateSelectGrammar
 *                              to generateSingleSelectGrammar and added
 *                              generateMultipleSelectGrammar.
 * 04-Sep-01    Paul            VBM:2001081707 - Modified methods to allow
 *                              the grammar to resolve TextComponentNames.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 17-Apr-03    Byron           VBM:2003032608 - Modifed all method signatures.
 *                              Renamed getGrammarFromObject to
 *                              generateGrammarFromObject. Added comments.
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.TextAssetReference;

import java.util.Collection;

public interface VoiceXMLGrammar {


    /**
     * Generate a boolean grammar.
     *
     * @param dom         The dom output buffer.
     @param fieldName   The name of the field for which the grammar is being
          *                    generated.
     @param falseReference The object representing the false state.
     @param trueReference  The object representing the true state.
     */
    public void generateBooleanGrammar(
            DOMOutputBuffer dom,
            String fieldName,
            TextAssetReference falseReference,
            TextAssetReference trueReference);

    /**
     * Generate a single select grammar.
     *
     * @param dom       The dom output buffer.
     * @param protocol  The protocol instance which needs the grammar.
     * @param fieldName The name of the field for which the grammar is being
     *                  generated.
     * @param options   The options which must be matched by the grammar.
     */
    public void generateSingleSelectGrammar(DOMOutputBuffer dom,
                                            VoiceXMLRoot protocol,
                                             String fieldName,
                                             Collection options);

    /**
     * Generate a multi select grammar.
     *
     * @param dom       The dom output buffer.
     * @param fieldName The name of the field for which the grammar is being
     *                  generated.
     * @param options   The options which must be matched by the grammar.
     */
    public void generateMultipleSelectGrammar(
            DOMOutputBuffer dom,
            String fieldName,
            Collection options);


    /**
     * Get the grammar from the object.
     *
     * @param dom      The dom output buffer.
     @param reference   The grammar object, this could either be a
     */
    public void generateGrammarFromObject(
            DOMOutputBuffer dom,
            TextAssetReference reference);
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Mar-04	3419/1	pduffin	VBM:2004031203 Fixed build problems with non ascii characters

 ===========================================================================
*/
