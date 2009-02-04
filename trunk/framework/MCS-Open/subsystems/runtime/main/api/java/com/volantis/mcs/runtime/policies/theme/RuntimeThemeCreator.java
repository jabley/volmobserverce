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

package com.volantis.mcs.runtime.policies.theme;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.asset.OldObjectCreator;
import com.volantis.mcs.runtime.themes.ThemeStyleSheetCompilerFactory;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.sheet.CompiledStyleSheet;

import java.util.List;

public class RuntimeThemeCreator
        implements OldObjectCreator {

    private final StyleSheetFactory styleSheetFactory = StyleSheetFactory.getDefaultInstance();

    private final StyleSheetCompilerFactory styleSheetCompilerFactory;

    public RuntimeThemeCreator() {
        this(ThemeStyleSheetCompilerFactory.getDefaultInstance());
    }

    private RuntimeThemeCreator(StyleSheetCompilerFactory styleSheetCompilerFactory) {
        this.styleSheetCompilerFactory = styleSheetCompilerFactory;
    }

    public RepositoryObject createOldObject(
            ActivatedVariablePolicy policy, Variant variant,
            InternalDevice device) {

        ActivatedThemeContent content =
                (ActivatedThemeContent) variant.getContent();
        StyleSheet styleSheet = content.getStyleSheet();

        // Create a new style sheet.
        StyleSheet compositeStyleSheet;

        // If importing the parent theme then find a theme that is
        // explicitly targeted at the parent device.
        if (content.getImportParent()) {

            // Create a style sheet to contain all the rules.
            compositeStyleSheet = styleSheetFactory.createStyleSheet();
            List rules = compositeStyleSheet.getRules();

            // Add the rules from the parent style sheet first.
            addParentRules(policy, device, rules);

            // Add the style sheet rules for this after the parent's
            // (if any) so that they will override the parent.
            rules.addAll(styleSheet.getRules());
        } else {
            // No rules are imported from the parent so just use the
            // existing style sheet.
            compositeStyleSheet = styleSheet;
        }

        StyleSheetCompiler styleSheetCompiler =
                styleSheetCompilerFactory.createStyleSheetCompiler();

        CompiledStyleSheet compiledStyleSheet =
                styleSheetCompiler.compileStyleSheet(compositeStyleSheet);

        return new RuntimeThemeAdapter(policy.getName(), compiledStyleSheet);
    }

    protected static void addParentRules(
            ActivatedVariablePolicy policy, InternalDevice device,
            List list) {

        device = device.getFallbackDevice();
        while (device != null) {
            String deviceName = device.getName();

            Variant variant = policy.getDeviceTargetedVariant(deviceName);
            if (variant != null) {
                VariantType type = variant.getVariantType();
                if (type != VariantType.NULL) {
                    ActivatedThemeContent content = (ActivatedThemeContent)
                            variant.getContent();
                    if (content.getImportParent()) {
                        addParentRules(policy, device, list);
                    }

                    // Add the style sheet rules for this after the parent's
                    // (if any) so that they will override the parent.
                    StyleSheet styleSheet = content.getStyleSheet();
                    list.addAll(styleSheet.getRules());
                }
                // Stop as soon as we have found a variant.
                return;
            }
            device = device.getFallbackDevice();            
        }
    }
}
