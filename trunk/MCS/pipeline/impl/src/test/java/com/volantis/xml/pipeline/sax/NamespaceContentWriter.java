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
package com.volantis.xml.pipeline.sax;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * ContentHandler that writes out content events, including namespace 
 * declarations, to a Writer    
 */ 
public class NamespaceContentWriter extends ContentWriter {

    /**
     * List to store all the currently declared namespaces that need
     * to be written out
     */ 
    private List namespaces;
    
    /**
     * List to store all the currently declared prefixes that need
     * to be written out
     */
    private List prefixes;

    /**
     * Flag that indicates whether attributes should have namespace prefixes
     * written out
     */
    private boolean namespaceAttributes;

    /**
     * Creates a new NamespaceContentWriter
     * @param writer the Writer
     */ 
    public NamespaceContentWriter(Writer writer) {
        this(writer, false);
    }

    /**
     * Initializes a NamespaceContentWriter instance
     * @param writer the writer
     * @param namespaceAttributes true if attributes should be prefixed
     */
    public NamespaceContentWriter(Writer writer, boolean namespaceAttributes) {
        super(writer);
        this.namespaceAttributes = namespaceAttributes;
        namespaces = new ArrayList();
        prefixes = new ArrayList();
    }

    // javadoc inherited.
    public void startPrefixMapping(String prefix, 
                                   String namespace) throws SAXException {        
        prefixes.add(prefix);
        namespaces.add(namespace);
    }

    // javadoc inherited
    protected void writeAttributes(Attributes attrs) throws SAXException {
        if (!namespaceAttributes) {
            super.writeAttributes(attrs);
        } else {
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String aName = attrs.getLocalName(i);
                    if ("".equals(aName) || (attrs.getQName(i) != null)) {
                        aName = attrs.getQName(i);
                    }
                    write(" " + aName + "=\"" + attrs.getValue(i) + "\"");
                }
            }
        }
    }

    // javadoc inherited.
    protected void writeNamespaceDeclaration() throws SAXException {
        if (!namespaces.isEmpty() && !prefixes.isEmpty()) {

            String namespace, prefix;
            StringBuffer sb = new StringBuffer();

            for(int i=0; i<namespaces.size(); i++) {
                namespace = (String) namespaces.get(i);
                prefix = (String) prefixes.get(i);

                // build up the declaration
                sb.append(" xmlns");
                if (prefix != null) {
                    sb.append(":")
                    .append(prefix);
                }
                sb.append("=\"")
                  .append(namespace)
                  .append("\"");
            }

            // write out the declaration
            write(sb.toString());

            // clear out the lists so that we don't write out these mappings
            // again
            namespaces.clear();
            prefixes.clear();
        }
    }

    // javadoc inherited
    protected void writeElementName(String localName, String qName)
            throws SAXException {
        
        String element = qName;
        if(null == qName || "".equals(qName)) {
            element = localName;
        }
        write(element);
    }
}
             
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 05-Nov-03	438/3	doug	VBM:2003091803 Addressed some rework issues

 04-Nov-03	438/1	doug	VBM:2003091803 Added parameter value processes

 27-Jun-03	127/1	doug	VBM:2003062306 Column Conditioner Modifications

 ===========================================================================
*/
