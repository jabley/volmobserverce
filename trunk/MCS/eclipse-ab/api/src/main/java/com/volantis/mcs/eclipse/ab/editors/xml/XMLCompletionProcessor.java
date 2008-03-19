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
package com.volantis.mcs.eclipse.ab.editors.xml;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import java.util.ArrayList;
import java.util.List;

import com.volantis.mcs.eclipse.ab.editors.xml.schema.AttributeDefinition;
import com.volantis.mcs.eclipse.ab.editors.xml.schema.DocumentInfo;
import com.volantis.mcs.eclipse.ab.editors.xml.schema.ElementDefinition;
import com.volantis.mcs.eclipse.ab.editors.xml.schema.InputContext;
import com.volantis.mcs.eclipse.ab.editors.xml.schema.SchemaDefinition;

/**
 * This completion processor utilizes a schema definition "lookup table" of
 * permitted element and attribute relationships and a document info object to
 * discover the current completion options.
 *
 * <p><strong>DISCLAIMER</strong>: This class and its associated classes are a
 * quick fix built to provide the ability to edit themes and layouts without
 * the Design parts. As such there are more likely to be bugs, bits missing and
 * bits that could be better designed.</p>
 */
public class XMLCompletionProcessor implements IContentAssistProcessor {
    /**
     * Completes the base class to allow document information to be obtained
     * from a given viewer's document.
     */
    private static class ViewerDocumentInfo extends DocumentInfo {
        /**
         * The viewer owning the document from which document information is to
         * be obtained
         */
        ITextViewer viewer;
        
        /**
         * Permits the viewer to be defined/re-defined.
         * 
         * @param viewer the viewer to be queried
         */
        public void setViewer(ITextViewer viewer) {
            this.viewer = viewer;
        }
        
        // javadoc inherited
        protected char getChar(int index) {
            try {
                return viewer.getDocument().getChar(index);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        // javadoc inherited
        protected int getLength() {
            return viewer.getDocument().getLength();
        }

        // javadoc inherited
        protected int indexOf(int start, String search) {
            try {
                return viewer.getDocument().search(start,
                                                   search,
                                                   true,
                                                   true,
                                                   false);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * Characters that, when typed, automatically invoke content assist.
     */    
    private static char[] autoActivationChars = { '<', ' ', ':', '/' };
    
    /**
     * Determines possible contextual information with respect to current viewer
     * document content and the document offset.
     */
    private ViewerDocumentInfo docInfo;
    
    /**
     * Provides the schema information used to identify the completion options.
     */
    private SchemaDefinition schema;
    
    /**
     * Initializes the new instance using the given parameters.
     * 
     * @param schema the schema definition used to obtain completion proposals
     */
    public XMLCompletionProcessor(SchemaDefinition schema) {
        this.schema = schema;
        this.docInfo = new ViewerDocumentInfo();
    }

    // javadoc inherited
    public ICompletionProposal[] computeCompletionProposals(
        ITextViewer viewer,
        int documentOffset) {
        ICompletionProposal[] proposals = null;
        String prefix;
        
        docInfo.setViewer(viewer);
        prefix = docInfo.getNamespacePrefix(schema.getNamespaceURI());

        // If the document doesn't define a namespace prefix for the schema's
        // namespace URI, we can't do completions
        if (prefix != null) {
            InputContext ic = docInfo.getInputContext(documentOffset - 1);
            
            // If the document info could not determine an input context at
            // the specified location in the document, we can't do completions
            if (ic != null) {
                String elementName = localName(
                    ic.getContainingElementName(),
                    prefix);

                // If the local name derived from the input context containing
                // element is null, we can't do completions
                if (elementName != null) {
                    String matchName = ic.getMatchName();

                    if (!ic.isAttribute() &&
                        (matchName != null) &&
                        matchName.startsWith("/")) { //$NON-NLS-1$
                        // If the match name starts with "/" we are trying to
                        // complete the currently open element (so we must not
                        // be in attribute match mode)
                        matchName = localMatchName(matchName.substring(1),
                                                   ic.isAttribute(),
                                                   prefix);

                        if ("".equals(matchName) || //$NON-NLS-1$
                            elementName.startsWith(matchName)) {
                            // Propose closing the currently open element
                            proposals =
                                new ICompletionProposal[1];
                            String replace;
                            StringBuffer sb =
                                new StringBuffer(2 +
                                                 ic.getContainingElementName().
                                                 length());
                            sb.append('/').
                                append(ic.getContainingElementName()).
                                append('>');

                            replace = sb.toString();

                            proposals[0] =
                                new CompletionProposal(
                                    replace,
                                    documentOffset - ic.getMatchName().length(),
                                    ic.getMatchName().length(),
                                    replace.length(),
                                    null,
                                    elementName,
                                    null,
                                    null);
                        }
                    } else {
                        // We are trying to complete something other than the
                        // close element markup
                        ElementDefinition element =
                            schema.getElementDefinition(elementName);
                        matchName = localMatchName(matchName,
                                                   ic.isAttribute(),
                                                   prefix);

                        // If an element definition cannot be found for the
                        // containing element, or we can't derive a local match
                        // name from the input context we can't do completions
                        if ((element != null) && (matchName != null)) {
                            List options = new ArrayList();

                            if (ic.isAttribute()) {
                                List attributes =
                                    element.getAttributeDefinitions(matchName);
                                AttributeDefinition attribute;

                                for (int i = 0;
                                     i < attributes.size();
                                     i++) {
                                    attribute =
                                        (AttributeDefinition)attributes.get(i);

                                    options.add(attribute.getName());
                                }
                            } else {
                                List subElements =
                                    element.getSubElementNames(matchName);

                                options.addAll(subElements);
                            }

                            if (options.size() != 0) {
                                proposals =
                                    stringListToCompletionProposalArray(
                                        options,
                                        documentOffset -
                                        ic.getMatchName().length(),
                                        ic.getMatchName().length(),
                                        ic.isAttribute(),
                                        prefix);
                            }
                        }
                    }
                }
            }
        }
        
        return proposals;
    }

    /**
     * This supporting method is used to actually generate the completion
     * proposal array based on the list of element or attribute names given.
     *
     * @param strings       the element or attribute names
     * @param replaceOffset the start point of the text to be replaced by the
     *                      completions
     * @param replaceLength the length of text to be replaced by the
     *                      completions
     * @param attributes    indicates if the strings are attribute or element
     *                      names
     * @param prefix        the namespace prefix for use with elements
     * @return the completion proposal array
     */
    protected ICompletionProposal[] stringListToCompletionProposalArray(
        List strings,
        int replaceOffset,
        int replaceLength,
        boolean attributes,
        String prefix) {
        ICompletionProposal[] proposals =
            new ICompletionProposal[strings.size()];
        int cursorPos;
        String replace;
        
        for (int i = 0; i < strings.size(); i++) {
            String option = (String)strings.get(i);
            
            if (attributes) {
                replace = option + "=\"\""; //$NON-NLS-1$
                cursorPos = option.length() + 2;
            } else {
                // Firstly, find out if there are any (mandatory) attributes
                ElementDefinition element =
                    schema.getElementDefinition(option);
                List attrs = element.getAttributeDefinitions(null);
                String prefixedName = qName(prefix, option);
                StringBuffer sb = new StringBuffer(prefixedName.length() * 4);
                boolean hasMandatoryAttributes = false;

                // Determine if the element may have content.
                // NOTE: This assumes content is sub-elements only and doesn't
                // account for textual content.
                boolean hasContent = element.hasSubElements();

                // The replacement will always start with the prefixed name
                sb.append(prefixedName);

                for (int j = 0;
                     !hasMandatoryAttributes && (j < attrs.size());
                     j++) {
                    hasMandatoryAttributes =
                        ((AttributeDefinition)attrs.get(j)).isRequired();
                }

                if (!hasMandatoryAttributes) {
                    // Since there are no mandatory attributes, simply complete
                    // the element open and close syntax
                    if (hasContent) {
                        sb.append("></").append(prefixedName).append('>'); //$NON-NLS-1$
                    } else {
                        sb.append("/>"); //$NON-NLS-1$
                    }

                    replace = sb.toString();

                    if (attrs.size() == 0) {
                        // Since there are no attributes, simply place the
                        // cursor between the "><" characters (if content) or
                        // after the "/>" (if no content)
                        if (hasContent) {
                            cursorPos = prefixedName.length() + 1;
                        } else {
                            cursorPos = prefixedName.length() + 2;
                        }
                    } else {
                        // Since there could be attributes defined, place the
                        // cursor immediately before the open tag markup's ">"
                        // (if content) or "/" (if no content) character
                        cursorPos = prefixedName.length();
                    }
                } else {
                    // There is at least one mandatory attribute. In this case
                    // we add empty definitions for each mandatory attribute
                    // and leave the cursor positioned in the first mandatory
                    // attribute's (empty) value definition
                    cursorPos = 0;

                    for (int j = 0;
                         j < attrs.size();
                         j++) {
                        AttributeDefinition attr =
                            (AttributeDefinition)attrs.get(j);

                        if (attr.isRequired()) {
                            sb.append(' ').append(attr.getName()).append("=\""); //$NON-NLS-1$

                            if (cursorPos == 0) {
                                // Record the position for this (first)
                                // mandatory attribute's empty value definition
                                cursorPos = sb.length();
                            }

                            sb.append('"');
                        }
                    }

                    if (hasContent) {
                        sb.append("></").append(prefixedName).append('>'); //$NON-NLS-1$
                    } else {
                        sb.append("/>"); //$NON-NLS-1$
                    }

                    replace = sb.toString();
                }
            }

            // Create the proposal using the replacement string and cursor position
            // calculated above. The list always shows the "unqualified" names for
            // attributes and elements.
            proposals[i] =
                new CompletionProposal(
                    replace,
                    replaceOffset,
                    replaceLength,
                    cursorPos,
                    null,
                    option,
                    null,
                    null);
        }

        return proposals;
    }
    
    /**
     * Returns a non-null localName if the qName is non-null and the prefix is
     * empty or the given qName starts with the given prefix.
     *
     * @param qName  the qualified name
     * @param prefix the prefix expected to be found in the qName
     * @return the local name derived from the given qName
     */
    protected String localName(String qName, String prefix) {
        String result = null;
        
        if (qName != null) {
            if ("".equals(prefix)) { //$NON-NLS-1$
                result = qName;
            } else if (qName.startsWith(prefix + ":")) { //$NON-NLS-1$
                result = qName.substring(prefix.length() + 1);
            }
        }
        
        return result;
    }

    /**
     * Returns a non-null localName if the qName is non-null and it represents
     * an attribute or the prefix is empty or the given qName starts with the
     * given prefix.
     *
     * @param qName       the qualified name
     * @param isAttribute whether the qualified name is for an attribute or
     *                    element
     * @param prefix      the prefix expected to be found in the qName if its
     *                    for an element
     * @return the local name derived from the given qName
     */
    protected String localMatchName(String qName,
                                    boolean isAttribute,
                                    String prefix) {
        String result = null;

        if (isAttribute) {
            result = qName;
        } else if (qName != null) {
            if ("".equals(prefix)) { //$NON-NLS-1$
                result = qName;
            } else if (qName.length() <= prefix.length()) {
                if (prefix.startsWith(qName)) {
                    result = ""; //$NON-NLS-1$
                }
            } else if (qName.startsWith(prefix + ":")) { //$NON-NLS-1$
                result = qName.substring(prefix.length() + 1);
            }
        }
        
        return result;
    }

    /**
     * Generates a qualified name from a prefix and given local name. The
     * prefix may be null.
     *
     * @param prefix    the namespace prefix
     * @param localName the local name
     * @return the qName derived from the namespace prefix and local name
     */
    protected String qName(String prefix, String localName) {
        String result = null;
    
        if (localName != null) {
            if ((prefix == null) || ("".equals(prefix))) { //$NON-NLS-1$
                result = localName;    
            } else {
                StringBuffer sb = new StringBuffer(
                    prefix.length() + 1 + localName.length());
                sb.append(prefix).append(':').append(localName);
                
                result = sb.toString(); 
            }
        }
        
        return result;
    }
    
    // javadoc inherited
    public IContextInformation[] computeContextInformation(
        ITextViewer viewer,
        int documentOffset) {
        return null;
    }

    // javadoc inherited
    public char[] getCompletionProposalAutoActivationCharacters() {
        return autoActivationChars;
    }

    // javadoc inherited
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    // javadoc inherited
    public String getErrorMessage() {
        return null;
    }

    // javadoc inherited
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 08-Jan-04	2498/1	philws	VBM:2004010804 Provide end element markup completion and improve attribute markup completion handling

 07-Jan-04	2433/1	philws	VBM:2004010702 Fix content assist choice refinement and optimize generated markup

 04-Jan-04	2309/2	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
