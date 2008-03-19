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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.ui;

import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.sample.ui.ProxyRenderer;
import com.volantis.mcs.interaction.sample.ui.ProxyRenderers;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.impl.RuleImpl;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.model.ThemeModel;
import com.volantis.styling.properties.StyleProperty;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThemeProxyRenderers implements ProxyRenderers {

    private static final Map RENDERERS;
    private static final ProxyRenderer DEFAULT_RENDERER = new ProxyRenderer() {
        public String render(Proxy proxy) {
            return proxy.toString();
        }
    };

    static {
        RENDERERS = new HashMap();

        RENDERERS.put(
                RuleImpl.class, new ProxyRenderer() {
                    public String render(Proxy proxy) {
                        Rule rule = (Rule) proxy.getModelObject();

                        StringWriter writer = new StringWriter();
                        RendererContext context = new RendererContext(
                                writer, CSSStyleSheetRenderer.getSingleton());
                        StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton();
                        try {
                            renderer.renderStyleRule(rule, context);
                            context.flushStyleSheet();
                        } catch (IOException e) {
                        }

                        return writer.getBuffer().toString();
                    }
                });

        RENDERERS.put(
                Selector.class, new ProxyRenderer() {
                    public String render(Proxy proxy) {
                        Selector selector = (Selector) proxy.getModelObject();

                        StringWriter writer = new StringWriter();
                        RendererContext context = new RendererContext(
                                writer, CSSStyleSheetRenderer.getSingleton());
                        StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton();
                        try {
                            renderer.renderStyleSelectors(
                                    Collections.singletonList(selector),
                                    context);
                            context.flushStyleSheet();
                        } catch (IOException e) {
                        }

                        return writer.getBuffer().toString();
                    }
                });

        RENDERERS.put(
                StyleProperties.class, new ProxyRenderer() {
                    public String render(Proxy proxy) {
                        StyleProperties properties = (StyleProperties) proxy.getModelObject();

                        StringWriter writer = new StringWriter();
                        RendererContext context = new RendererContext(
                                writer, CSSStyleSheetRenderer.getSingleton());
                        StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton();
                        try {
                            renderer.renderStyleProperties(
                                    properties, context);
                            context.flushStyleSheet();
                        } catch (IOException e) {
                        }

                        return writer.getBuffer().toString();
                    }
                });

        RENDERERS.put(
                StyleValue.class, new ProxyRenderer() {
                    public String render(Proxy proxy) {
                        StyleValue value = (StyleValue) proxy.getModelObject();
                        BeanProxy propertiesProxy = (BeanProxy) proxy.getParentProxy();
                        PropertyIdentifier identifier = propertiesProxy.getPropertyForProxy(
                                proxy);
                        StyleProperty property = ThemeModel.
                                getStylePropertyForPropertyIdentifier(
                                        identifier);
                        StringWriter writer = new StringWriter();
                        RendererContext context = new RendererContext(
                                writer, CSSStyleSheetRenderer.getSingleton());
                        StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton();
                        try {
                            MutableStyleProperties properties =
                                ThemeFactory.getDefaultInstance().
                                    createMutableStyleProperties();
                            properties.setStyleValue(property, value);
                            renderer.renderStyleProperties(properties, context);
                            context.flushStyleSheet();
                        } catch (IOException e) {
                        }

                        String string = writer.getBuffer().toString();
                        int index = string.indexOf(':');
                        if (index != -1) {
                            string = string.substring(index + 1).trim();
                        }

                        return string;
                    }
                });
    }

    public ProxyRenderer getRenderer(Proxy proxy) {
        Class typeClass = proxy.getTypeDescriptor().getTypeClass();
        ProxyRenderer renderer = (ProxyRenderer) RENDERERS.get(typeClass);
        if (renderer == null) {
            renderer = DEFAULT_RENDERER;
        }
        return renderer;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 ===========================================================================
*/
