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

import com.volantis.mcs.eclipse.ab.editors.xml.schema.SchemaDefinition;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.IAnnotationHover;

/**
 * Configuration for XML editors.
 *
 * @todo later javadoc the workings of this class here
 *
 * DISCLAIMER: This class and its associated classes are a quick fix built to
 * provide the ability to edit themes and layouts without the Design
 * parts. As such there are more likely to be bugs, bits missing and
 * bits that could be better designed.
 */
public class XMLConfiguration extends SourceViewerConfiguration {
	private XMLDoubleClickStrategy doubleClickStrategy;
	private XMLTagScanner tagScanner;
	private XMLScanner scanner;
	private ColorManager colorManager;
    private SchemaDefinition schema;

    /**
     * The IAnnotationHover association with this XMLConfiguration.
     */
    private final IAnnotationHover annotationHover;
    /**
     * Construct a new XMLConfiguration.
     * @param colorManager The ColorManager for this XMLConfiguration.
     * @param schema The SchemaDefinition for this XMLConfiguration.
     * @param annotationHover The IAnnotationHover for this
     * XMLConfiguration.
     */
	public XMLConfiguration(
        ColorManager colorManager,
        SchemaDefinition schema, IAnnotationHover annotationHover) {
		this.colorManager = colorManager;
        this.schema = schema;
        this.annotationHover = annotationHover;
	}

    // javadoc inherited
    public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
        return annotationHover;
    }
    
    /**
     *
     * @param sourceViewer
     * @return a String array of the configured content types
     * @todo later complete this javadoc
     */
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			XMLPartitionScanner.XML_COMMENT,
			XMLPartitionScanner.XML_TAG };
	}

    /**
     * Get the ITextDoubleClickStrategy for this XMLConfiguration.
     * @param sourceViewer Unused.
     * @param contentType Unused.
     * @return An XMLDoubleClickStrategy.
     */
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null) {
			doubleClickStrategy = new XMLDoubleClickStrategy();
        }
		return doubleClickStrategy;
	}

    /**
     * Get the XMLScanner for this XMLConfiguration.
     * @return An XMLScanner.
     */
	protected XMLScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new XMLScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(XMLColorConstants.DEFAULT))));
		}
		return scanner;
	}

    /**
     * Get the XMLTagScanner for this XMLConfiguration.
     * @return An XMLTagScanner.
     */
    protected XMLTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new XMLTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(XMLColorConstants.TAG))));
		}
		return tagScanner;
	}

    /**
     * Get the IPresentationReconciler for this XMLConfiguration.
     * @param sourceViewer Unused.
     * @return A PresenationReconciler based on a NonRuleBasedDamagerRepaier.
     */
	public IPresentationReconciler
            getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_TAG);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_TAG);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(XMLColorConstants.XML_COMMENT)));
		reconciler.setDamager(ndr, XMLPartitionScanner.XML_COMMENT);
		reconciler.setRepairer(ndr, XMLPartitionScanner.XML_COMMENT);

		return reconciler;
	}

    // javadoc inherited
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant assistant = new ContentAssistant();
        XMLCompletionProcessor processor =
            new XMLCompletionProcessor(schema);

        assistant.setContentAssistProcessor(
            processor,
            XMLPartitionScanner.XML_TAG);

        assistant.setContentAssistProcessor(
            processor,
            XMLPartitionScanner.XML_DEFAULT);

        assistant.setContentAssistProcessor(
            processor,
            IDocument.DEFAULT_CONTENT_TYPE);

        assistant.enableAutoActivation(true);
        assistant.setAutoActivationDelay(500); // half a second?
        assistant.setProposalPopupOrientation(
            ContentAssistant.CONTEXT_INFO_BELOW);
        assistant.enableAutoInsert(true);

        return assistant;
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 09-Jan-04	2515/1	allan	VBM:2004010513 Add hover annotation and mouse click handling to rulers.

 04-Jan-04	2309/2	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
