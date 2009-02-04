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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import java.io.InputStream;

/**
 * A document provided for XML documents.
 *
 * DISCLAIMER: This class and its associated classes are a quick fix built to
 * provide the ability to edit themes and layouts without the Design
 * parts. As such there are more likely to be bugs, bits missing and
 * bits that could be better designed.
 */
public class XMLDocumentProvider extends FileDocumentProvider {

    // javadoc inherited
    protected IDocument createDocument(Object element) throws CoreException {
        IDocument document = super.createDocument(element);
        if (document != null) {
            establishPartitioner(document);
        }
        return document;
    }

    /**
     * Establish document partitioner for this document provider.
     * @param document The document for the partitioner. Must not be null.
     * @throws IllegalArgumentException If document is null.
     */
    private void establishPartitioner(IDocument document) {
        if (document == null) {
            throw new IllegalArgumentException("Cannot be null: document"); //$NON-NLS-1$
        }

        IDocumentPartitioner partitioner =
                new DefaultPartitioner(
                        new XMLPartitionScanner(),
                        new String[]{
                            XMLPartitionScanner.XML_TAG,
                            XMLPartitionScanner.XML_COMMENT});
        partitioner.connect(document);
        document.setDocumentPartitioner(partitioner);
    }

    /**
     * Set the document content.
     * @param element The element (e.g. IEditorInput) associciated with the
     * document.
     * @param contentStream The InputStream from which to derive the
     * content. The content is expected to be well-formed xml.
     * @throws CoreException If the contentStream cannot be read.
     */
    public void setDocumentContent(Object element, InputStream contentStream,
                                   boolean isDirty)
            throws CoreException {

        setDocumentContent(getDocument(element), contentStream,
                getEncoding(element));


        FileInfo info = (FileInfo) getElementInfo(element);
        if (info != null) {

            if (isDirty != info.fCanBeSaved && info.fCanBeSaved) {
                // This means that the document from which this content
                // has been set has been saved making it non dirty.
                // Because it has been saved the resource is now out
                // of sync with this document provided and an attempt
                // to save will therefore bring up a dialog asking the
                // user if they wish to override the changes. To avoid
                // this dialog we synchronize here.
                synchronize(element);
            } else {
                fireElementContentAboutToBeReplaced(element);
                info.fDocument.set(getDocument(element).get());
                if (info.fCanBeSaved && !isDirty) {
                    addUnchangedElementListeners(element, info);
                }
                fireElementContentReplaced(element);
                if (isDirty != info.fCanBeSaved) {
                    info.fCanBeSaved = isDirty;
                    fireElementDirtyStateChanged(element, isDirty);
                }
            }
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

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 06-Jan-04	2412/2	allan	VBM:2004010407 Fixed dirty status handling when switching editor page.

 04-Jan-04	2309/2	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
