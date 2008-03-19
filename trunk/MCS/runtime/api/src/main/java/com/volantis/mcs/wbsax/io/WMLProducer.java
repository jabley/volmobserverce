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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-May-03    steve           VBM:2003042908   Created.
 * 23-May-03    Steve           VBM:2003042908   Writes to an OutputSource
 *                              Updated startDocument. The StringReference 
 *                              version passes the document type.
 * 29-May-03    Geoff           VBM:2003042905 - Fix bugs, remove OutputSource.
 * 31-May-03    Geoff           VBM:2003042906 - Add encoding support. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax.io;

import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.StringFactory;

import java.io.Writer;
import java.io.IOException;

/**
 * WMLProducer. Turn WBSAX events into WML
 */
public class WMLProducer extends XMLProducer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Create a XMLProducer
     *
     * @param out the Writer to write to
     */
    public WMLProducer(Writer out, Writer encoded) {
        super( out, encoded );
    }

    /**
     * Output a string to the content of the attribute
     * 
     * @param part a reference to the string holding the attribute
     * 
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValue(StringReference part) throws WBSAXException {
        output( part.resolveString().getString() );
    }
    
    /**
     * Output a string to the content of the attribute
     * 
     * @param part a string holding the attribute
     * 
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValue(WBSAXString part)throws WBSAXException {
        output( part.getString() );
    }    
    
    /**
     * Write an extended attribute value
     *
     * @param code the extension code
     * @param part the value of the attribute
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValueExtension(Extension code, WBSAXString part)
        throws WBSAXException {
            
        addContentValueExtension( code, part );
    }

    /**
     * Write an extended attribute value
     *
     * @param code the extension code
     * @param part a reference to the value of the attribute
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValueExtension(Extension code, StringReference part)
        throws WBSAXException {

        addContentValueExtension( code, part );
    }

    /**
     * Output an extended string to the content of the element
     *
     * @param code the extension code
     * @param part a string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValueExtension(Extension code, WBSAXString part)
        throws WBSAXException {
        try {
            if (code == Extension.ZERO) {
                out.write("$(");
                output(part.getString());
                out.write(":e)");
            } else if (code == Extension.ONE) {
                out.write("$(");
                output(part.getString());
                out.write(":u)");
            } else if (code == Extension.TWO) {
                out.write("$(");
                output(part.getString());
                out.write(")");
            } else {
                throw new UnsupportedOperationException(
                        "Document Extension (" +
                        String.valueOf(code.intValue()) + ") not supported");
            }
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Output an extended string to the content of the element
     *
     * @param code the extension code
     * @param part a reference to the string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValueExtension(Extension code, StringReference part)
        throws WBSAXException {
        try {
            if (code == Extension.ZERO) {
                out.write("$(");
                output(part.resolveString().getString());
                out.write(":e)");
            } else if (code == Extension.ONE) {
                out.write("$(");
                output(part.resolveString().getString());
                out.write(":u)");
            } else if (code == Extension.TWO) {
                out.write("$(");
                output(part.resolveString().getString());
                out.write(")");
            } else {
                throw new UnsupportedOperationException(
                        "Document Extension (" +
                        String.valueOf(code.intValue()) + ") not supported");
            }
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }


    /**
     * Start a new document.
     *
     * @param version the WBXML version code
     * @param publicId holds the code of the document type
     * @param charset which character set is being used
     * @param stringTable the document string table
     *
     * @throws WBSAXException if there is an error writing the header
     */
    public void startDocument(VersionCode version, PublicIdCode publicId,
            Codec charset, StringTable stringTable, StringFactory strings)
        throws WBSAXException {
        try {
            super.startDocument(version, publicId, charset, stringTable, 
                    strings);
            if (publicId.getName() != null) {
                out.write("<!DOCTYPE wml PUBLIC \"");
                output(publicId.getName());
                out.write("\" \"");
                output(publicId.getDtd());
                out.write("\">");
            }
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Start a new document.
     *
     * @param version the WBXML version code
     * @param publicId a reference to a string holding the public id
     * @param charset which character set is being used
     * @param stringTable the document string table
     *
     * @throws WBSAXException if there is an error writing the header
     * 
     * @todo The document is only passed a PUBLIC ID, there is no DTD
     * specified. The DTD needs to be passed and handled.
     */
    public void startDocument(VersionCode version, StringReference publicId,
            Codec charset, StringTable stringTable, StringFactory strings)
        throws WBSAXException {
        try {
            super.startDocument( version, publicId, charset, stringTable, 
                    strings);
            out.write( "<!DOCTYPE wml PUBLIC \"" );
            output( publicId.resolveString().getString() );
            out.write( "\" >" );
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Output a string to the content of the element
     *
     * @param part a reference to the string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValue(StringReference part)
        throws WBSAXException {
        
        output( part.resolveString().getString() );
    }

    /**
     * Output a string to the content of the element
     *
     * @param part a string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValue(WBSAXString part)
        throws WBSAXException {
        output( part.getString() );
    }
   
    /**
     * Output a string but double any '$' characters.
     * '$' characters that get through to here are in the content of a
     * WML element and are not variables. ( If they were variables they
     * would be written via the addContentValueExtension() methods )
     * We therefore double any '$' characters so the WML device displays
     * them as single '$' characters.
     * @param str the string to write.
     */
    private void output( String str ) throws WBSAXException {
        try {
            char chr;
            for (int i = 0; i < str.length(); i++) {
                chr = str.charAt(i);
                if (chr == '$') {
                    enc.write("$$");
                } else {
                    enc.write(chr);
                }
            } 
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

}




/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 30-Jun-03	644/1	geoff	VBM:2003061001 port from mimas to metis

 ===========================================================================
*/
