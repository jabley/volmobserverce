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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Label provider for providing textual labels for proxies representing
 * variants.
 */
public class VariantProxyLabelProvider extends LabelProvider {
    /**
     * This is an ultimate fallback icon to use if no other icon (including the
     * per-instance default) is available. Under normal circumstances it should
     * never be used, but it prevents errors if an unrecognised variant is
     * ever encountered.
     */
    private static final Image DEFAULT_ICON =
            EclipseCommonMessages.getPolicyIcon("theme.deviceTheme");

    /**
     * A map associating variant types with their corresponding variant icons.
     */
    private static final Map VARIANT_TYPE_ICONS = new HashMap();
    static {
        VARIANT_TYPE_ICONS.put(VariantType.AUDIO, EclipseCommonMessages.
                getPolicyIcon("audioComponent.audioAsset"));
        VARIANT_TYPE_ICONS.put(VariantType.CHART, EclipseCommonMessages.
                getPolicyIcon("chartComponent.chartAsset"));
        VARIANT_TYPE_ICONS.put(VariantType.IMAGE, EclipseCommonMessages.
                getPolicyIcon("imageComponent.deviceImageAsset"));
        VARIANT_TYPE_ICONS.put(VariantType.LAYOUT, EclipseCommonMessages.
                getPolicyIcon("layout.deviceLayoutCanvasFormat"));
        VARIANT_TYPE_ICONS.put(VariantType.LINK, EclipseCommonMessages.
                getPolicyIcon("linkComponent.linkAsset"));
        VARIANT_TYPE_ICONS.put(VariantType.SCRIPT, EclipseCommonMessages.
                getPolicyIcon("scriptComponent.scriptAsset"));
        VARIANT_TYPE_ICONS.put(VariantType.TEXT, EclipseCommonMessages.
                getPolicyIcon("textComponent.textAsset"));
        VARIANT_TYPE_ICONS.put(VariantType.THEME, EclipseCommonMessages.
                getPolicyIcon("theme.deviceTheme"));
    }

    /**
     * A map associating layout types with their corresponding variant icons.
     */
    private static final Map LAYOUT_ICONS = new HashMap();
    static {
        LAYOUT_ICONS.put(LayoutType.CANVAS, EclipseCommonMessages.
                getPolicyIcon("layout.deviceLayoutCanvasFormat"));
        LAYOUT_ICONS.put(LayoutType.MONTAGE, EclipseCommonMessages.
                getPolicyIcon("layout.deviceLayoutMontageFormat"));
    }

    /**
     * The default variant type to assume for this label provider - generally
     * used in the case of null variants.
     */
    private VariantType defaultVariantType;

    /**
     * Create a variant proxy label provider with a specified default variant
     * type.
     *
     * @param defaultType The default variant type to assume if a proxy does
     *                    not contain a variant with a recognised type
     */
    public VariantProxyLabelProvider(VariantType defaultType) {
        defaultVariantType = defaultType;
    }

    // Javadoc inherited
    public String getText(Object o) {
        String label = "";

        if (o != null) {
            Proxy proxy = (Proxy) o;

            label = render(proxy.getModelObject());
        }

        return label;
    }

    /**
     * Renders an object as text.
     *
     * <p>This method should be overridden to create label providers that can
     * render more complex objects that can not render themselves using
     * {@link Object#toString}.</p>
     *
     * @param o The object to render
     * @return The string value of that object
     */
    protected String render(Object o) {
        String rendered = "";
        if (o != null && o instanceof VariantBuilder) {
            VariantBuilder variant = (VariantBuilder) o;
            rendered = new VariantLabelRenderer().render(variant);
        }
        return rendered;
    }

    // Javadoc inherited
    public Image getImage(Object o) {
        Image image = null;
        if (o != null) {
            Proxy proxy = (Proxy) o;
            Object model = proxy.getModelObject();
            if (model != null && model instanceof VariantBuilder) {
                VariantBuilder variant = (VariantBuilder) model;
                VariantType type = variant.getVariantType();

                if (type == VariantType.LAYOUT) {
                    InternalLayoutContentBuilder content =
                            (InternalLayoutContentBuilder)
                            variant.getContentBuilder();
                    if (content != null) {
                        Layout layout = content.getLayout();
                        if (layout != null) {
                            image = (Image) LAYOUT_ICONS.get(layout.getType());
                        }
                    }
                } else {
                    image = (Image) VARIANT_TYPE_ICONS.get(type);
                }
            }
        }

        // If no image has been found, use the default variant type.
        if (image == null) {
            image = (Image) VARIANT_TYPE_ICONS.get(defaultVariantType);
        }

        return image == null ? DEFAULT_ICON : image;
    }
}
