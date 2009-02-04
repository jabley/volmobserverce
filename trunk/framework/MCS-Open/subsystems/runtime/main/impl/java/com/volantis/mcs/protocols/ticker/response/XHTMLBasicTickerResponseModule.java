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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.ticker.response;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;

import com.volantis.mcs.context.PageURIRewriter;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.html.XHTMLBasic;
import com.volantis.mcs.protocols.ticker.response.attributes.AddItemAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.DescriptionAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.FeedPollerAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.IconAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.PlainDescriptionAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.RemoveItemAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SetPollingIntervalAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SetSkipTimesAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SetURLAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SkipTimeAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.TitleAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;

/**
 * Implementation of TickerResponseModule for XHTMLBasic protocol.
 */
public class XHTMLBasicTickerResponseModule extends AbstractTickerResponseModule {
    /**
     * The protocol used.
     */
    private XHTMLBasic protocol;
    
    /**
     * The writer for the JavaScript content. 
     */
    private Writer scriptWriter;
    
    /**
     * The writer for the JavaScript add-item scripts.
     */
    private Writer addItemScriptWriter;
    
    /**
     * ArrayList with all add-item scripts.
     */
    private ArrayList addItemScripts;
    
    /**
     * The DOMOutputBuffer used to record content.
     */
    private DOMOutputBuffer recordedBuffer;
    
    /**
     * An ID of the item currently added;
     */
    private String addedItemId;
    
    /**
     * Indicates, that the SetSkipTimes element has just been opened,
     * ready to open the first skip time. 
     */
    private boolean firstSkipTime = false;
    
    /**
     * The attributes of currently opened placeholder Div element.
     */
    private DivAttributes placeholderAttributes;
    
    /**
     * The URIRewriter for rewriting link inside items.
     */
    private PageURIRewriter itemLinkURIRewriter; 
    
    /**
     * Creates new instance of this module based on given protocol.
     * 
     * @param protocol The protocol used.
     */
    public XHTMLBasicTickerResponseModule(XHTMLBasic protocol) {
        this.protocol = protocol;
        this.itemLinkURIRewriter = new ItemLinkURIRewriter();
    }
    
    /**
     * Returns protocol used by this module.
     * 
     * @return The protocol used.
     */
    public XHTMLBasic getProtocol() {
        return protocol;
    }
    
    // Javadoc inherited
    public void openFeedPoller(FeedPollerAttributes attributes) throws ProtocolException {
        // Create a writer, which will be used to write JavaScript content 
        // by the FeedPoller element and its children.
        scriptWriter = new StringWriter();
        addItemScripts = new ArrayList();
    }
    
    // Javadoc inherited
    public void closeFeedPoller(FeedPollerAttributes attributes) throws ProtocolException {
        // Create script element with the content written
        // using scriptContentWriter
        ScriptAttributes scriptAttributes = new ScriptAttributes();

        protocol.writeOpenScript(scriptAttributes);

        try {
            // Write content of all scripts, excepts for item addition
            protocol.getContentWriter().write(scriptWriter.toString());
            
            // Write script with item additions, in reverse order.
            for(int i = addItemScripts.size() - 1; i >= 0; i--) {
                protocol.getContentWriter().write((String) addItemScripts.get(i));
            }
        } catch (IOException e) {
            throw new ProtocolException(e);
        }

        protocol.writeCloseScript(scriptAttributes);
        
        scriptWriter = null;
        addItemScripts = null;
    }
    
    // Javadoc inherited
    public void openAddItem(AddItemAttributes attributes) throws ProtocolException {
        addItemScriptWriter = new StringWriter();
        
        writeAddItemScript("Ticker.Response.addItem({");
        writeAddItemScript("id:" + createJavaScriptString(attributes.getItemId()));
        writeAddItemScript(",channel:" + createJavaScriptString(attributes.getChannel()));
        
        addedItemId = attributes.getItemId();
    }

    // Javadoc inherited
    public void closeAddItem(AddItemAttributes attributes) throws ProtocolException {
        addedItemId = null;
        
        writeAddItemScript("});");
        
        addItemScripts.add(addItemScriptWriter.toString());
        
        addItemScriptWriter = null;
    }
    
    // Javadoc inherited
    public void closeRemoveItem(RemoveItemAttributes attributes) throws ProtocolException {
        writeScript("Ticker.Response.removeItem(");
        writeScript(createJavaScriptString(attributes.getItemId()));
        writeScript(");");
    }
    
    // Javadoc inherited
    public void openTitle(TitleAttributes attributes) throws ProtocolException {
        String id = openPlaceholder(attributes);

        protocol.getMarinerPageContext().pushURIRewriter(itemLinkURIRewriter);
        
        writeAddItemScript(",title:" + createJavaScriptString(id));
    }
    
    // Javadoc inherited
    public void closeTitle(TitleAttributes attributes) throws ProtocolException {
        protocol.getMarinerPageContext().popURIRewriter(null);

        closePlaceholder();
    }
    
    // Javadoc inherited
    public void openIcon(IconAttributes attributes) throws ProtocolException {
        String id = openPlaceholder(attributes);

        protocol.getMarinerPageContext().pushURIRewriter(itemLinkURIRewriter);

        writeAddItemScript(",icon:" + createJavaScriptString(id));
    }
    
    // Javadoc inherited
    public void closeIcon(IconAttributes attributes) throws ProtocolException {
        protocol.getMarinerPageContext().popURIRewriter(null);

        closePlaceholder();
    }
    
    // Javadoc inherited
    public void openDescription(DescriptionAttributes attributes) throws ProtocolException {
        String id = openPlaceholder(attributes);
        
        protocol.getMarinerPageContext().pushURIRewriter(itemLinkURIRewriter);
        
        writeAddItemScript(",description:" + createJavaScriptString(id));
    }
    
    // Javadoc inherited
    public void closeDescription(DescriptionAttributes attributes) throws ProtocolException {
        protocol.getMarinerPageContext().popURIRewriter(null);
        
        closePlaceholder();
    }

    // Javadoc inherited
    public void openPlainDescription(PlainDescriptionAttributes attributes) throws ProtocolException {
        String id = openPlaceholder(attributes);
        
        protocol.getMarinerPageContext().pushURIRewriter(itemLinkURIRewriter);
        
        writeAddItemScript(",plainDescription:" + createJavaScriptString(id));
    }
    
    // Javadoc inherited
    public void closePlainDescription(PlainDescriptionAttributes attributes) throws ProtocolException {
        protocol.getMarinerPageContext().popURIRewriter(null);
        
        closePlaceholder();
    }

    // Javadoc inherited
    public void openSetURL(SetURLAttributes attributes) throws ProtocolException {
        startContentRecording();
    }
    
    // Javadoc inherited
    public void closeSetURL(SetURLAttributes attributes) throws ProtocolException {
        String url = stopContentRecording();
        
        // TODO: I'm not 100% sure, but the URL needs to be escaped 
        // before rendering into JavaScript. I must check the URL
        // alphabet, if it includes characters which needs to be escaped.
        writeScript("Ticker.Response.setURL(" + createJavaScriptString(url) + ");");
    }
    
    // Javadoc inherited
    public void openSetPollingInterval(SetPollingIntervalAttributes attributes) throws ProtocolException {
        startContentRecording();
    }
    
    // Javadoc inherited
    public void closeSetPollingInterval(SetPollingIntervalAttributes attributes) throws ProtocolException {
        String interval = stopContentRecording();
        
        writeScript("Ticker.Response.setPollingInterval(" + interval + ");");
    }
    
    // Javadoc inherited
    public void openSetSkipTimes(SetSkipTimesAttributes attributes) throws ProtocolException {
        writeScript("Ticker.Response.setSkipTimes([");
        
        firstSkipTime = true;
    }
    
    // Javadoc inherited
    public void closeSetSkipTimes(SetSkipTimesAttributes attributes) throws ProtocolException {
        writeScript("]);");                
    }
    
    // Javadoc inherited
    public void openSkipTime(SkipTimeAttributes attributes) throws ProtocolException {
        if (firstSkipTime) {
            firstSkipTime = false;
        } else {
            writeScript(",");
        }
        
        writeScript("[" + attributes.getFrom() + "," + attributes.getTo() + "]");
        
    }
    
    /**
     * Writes given string into current FeedPoller script content.
     * 
     * @param content A string to write.
     * @throws ProtocolException
     */
    private void writeScript(String content) throws ProtocolException {
        try {
            scriptWriter.write(content);
        } catch (IOException e) {
            throw new ProtocolException(e);
        }        
    }
    
    /**
     * Writes given string into current FeedPoller item script content.
     * 
     * @param content A string to write.
     * @throws ProtocolException
     */
    private void writeAddItemScript(String content) throws ProtocolException {
        try {
            addItemScriptWriter.write(content);
        } catch (IOException e) {
            throw new ProtocolException(e);
        }        
    }
    
    /**
     * Starts content recording.
     * Note, that only PCDATA is recorded.
     */
    private void startContentRecording() {
        // Create new output buffer to capture the content of the URL.
        recordedBuffer = (DOMOutputBuffer) protocol.getOutputBufferFactory().createOutputBuffer();
        
        protocol.getMarinerPageContext().pushOutputBuffer(recordedBuffer);        
    }
    
    /**
     * Stops content recording and return recorded content as String.
     * 
     * @return The recorded content
     */
    private String stopContentRecording() {
        // Pop output buffer
        protocol.getMarinerPageContext().popOutputBuffer(recordedBuffer);
        
        // The buffer should contain PCDATA.
        return recordedBuffer.getPCDATAValue();        
    }
    
    /**
     * Opens a placeholder element and returns its ID.
     * 
     * @return The ID of the placeholder element.
     */
    private String openPlaceholder(MCSAttributes attributes) throws ProtocolException {
        placeholderAttributes = new DivAttributes();

        placeholderAttributes.copy(attributes);
        
        placeholderAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        placeholderAttributes.getStyles().getPropertyValues().
            setSpecifiedValue(StylePropertyDetails.DISPLAY, DisplayKeywords.BLOCK);
        
        protocol.writeOpenDiv(placeholderAttributes);
        
        return placeholderAttributes.getId();
    }
    
    /**
     * Closes previously opened placeholder element.
     */
    private void closePlaceholder() {
        protocol.writeCloseDiv(placeholderAttributes);        
    }

    /**
     * The URI rewriter used to rewrite links inside items.
     * It rewrites only the URIs of PageURLType.ANCHOR type
     */
    private final class ItemLinkURIRewriter implements PageURIRewriter {
        // Javadoc inherited
        public URI rewrite(URI uri, PageURLType type) {
            if (type != PageURLType.ANCHOR) {
                // If URI is of the ANCHOR type, do not rewrite it.
                return uri;
                
            } else {
                // If URI is of the ANCHOR type, rewrite it by wrapping
                // the link within the JavaScript call.
                String rewrittenURIString = "javascript:Ticker.Response.followLink("
                        + createJavaScriptString(addedItemId)
                        + "," + createJavaScriptString(uri.toString()) + ")";

                try {
                    return new URI(rewrittenURIString);
                } catch (URISyntaxException e) {
                    // In case URI can not be rewritten, throw a runtime
                    // exception, since there's nothing else we can do here.
                    throw new RuntimeException("Can not rewrite URI " + uri + " into "
                            + rewrittenURIString, e);
                }
            }
        }

        // Javadoc inherited
        public boolean willPossiblyRewrite(PageURLType type) {
            // Only URIs of ANCHOR type will be rewritten.
            return (type == PageURLType.ANCHOR);
        }
    }
    
    private String createJavaScriptString(String string) {
        return '\'' + StringEscapeUtils.escapeJavaScript(string) + '\'';
    }
}
