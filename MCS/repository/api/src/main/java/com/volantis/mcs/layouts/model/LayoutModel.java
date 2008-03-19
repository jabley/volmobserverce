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
package com.volantis.mcs.layouts.model;

import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.integrity.DefinitionTypeBuilder;
import com.volantis.mcs.model.validation.integrity.DefinitionType;
import com.volantis.mcs.model.validation.integrity.DefinitionTypeHandler;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContent;

public class LayoutModel {

    public static final DefinitionType DISSECTING_PANE_SCOPE_TYPE;
    static {
        DefinitionTypeBuilder builder = new DefinitionTypeBuilder();
        builder.setDefinitionTypeHandler(new DefinitionTypeHandler() {
            public void reportDuplicate(ValidationContext context,
                    SourceLocation source, Path path, Object identifier) {
                // NOTE: identifier is a dummy object in this case.
                context.addDiagnostic(source, path, DiagnosticLevel.ERROR,
                        context.createMessage("dissecting-pane-duplicate"));
            }
        });
        DISSECTING_PANE_SCOPE_TYPE = builder.getDefinitionType();
    }


    /**
     * Actually there is no layout interaction model at the moment. However, we
     * do have validation errors reported from the underlying layout classes.
     * The paths that they generate are of the form:
     *
     * .../layout/rootFormat/formats/n[/formats/n]...[/name]
     *
     * This form is mostly based on the old underlying model so may need to
     * be updated when we rewrite that model to be more sensible.
     *
     * The commented code following might form the basis of the future
     * interation model.
     */

    public static final PropertyIdentifier LAYOUT =
            new PropertyIdentifier(InternalLayoutContent.class, "layout");

    public static final PropertyIdentifier ROOT_FORMAT =
            new PropertyIdentifier(Layout.class, "rootFormat");

//    public static final PropertyIdentifier FORMATS =
//            new PropertyIdentifier(Format.class, "formats");
//
//    public static final PropertyIdentifier FORMAT_NAME =
//            new PropertyIdentifier(Format.class, "formatName");
//
//
//    public static void buildLayoutModelDescriptor(
//            ModelDescriptorBuilder builder) {
//
//        // NOTE: here we create only the structure of the interation model for
//        // validation purposes. The interaction model cannot be created as none
//        // of the required factory classes for creating the underlying model
//        // have been provided. This is intentional since the old crufty
//        // underlying model classes need to be thrown away and rewritten before
//        // it's worth trying to map the interaction model to them.
//
//        BaseClassDescriptor formatDescriptor = builder.addBaseClassDescriptor(
//                Format.class);
//
//        // Create the bean descriptors for InternalLayoutContent.
//        BeanDescriptorBuilder layoutContentBuilder = builder.getBeanBuilder(
//                InternalLayoutContentBuilder.class, null);
//
//        // Create the bean descriptors for Layout.
//        BeanDescriptorBuilder layoutBuilder = builder.getBeanBuilder(
//                Layout.class, null);
//
//        // Create the class descriptor for Pane.
//        BeanDescriptorBuilder paneBuilder = builder.getBeanBuilder(
//                Pane.class, null);
//
//        // Create the class descriptor for Pane.
//        BeanDescriptorBuilder gridBuilder = builder.getBeanBuilder(
//                Grid.class, null);
//
//        // LayoutContentBuilder has a Layout.
//        layoutContentBuilder.addPropertyDescriptor(
//                LAYOUT,
//                builder.getTypeDescriptor(Layout.class),
//                null,
//                false);
//
//        // Layout has a root Format.
//        layoutBuilder.addPropertyDescriptor(
//                ROOT_FORMAT,
//                builder.getTypeDescriptor(Format.class),
//                null,
//                true);
//
//        // A Pane has common Format properties.
//        addFormatProperties(builder, paneBuilder, formatDescriptor);
//        // A Pane has a mandatory name.
//        paneBuilder.addPropertyDescriptor(
//                FORMAT_NAME,
//                builder.getTypeDescriptor(String.class),
//                null,
//                true);
//
//        // A Grid has common Format properties.
//        addFormatProperties(builder, gridBuilder, formatDescriptor);
//        // A Grid has an optional name.
//        gridBuilder.addPropertyDescriptor(
//                FORMAT_NAME,
//                builder.getTypeDescriptor(String.class),
//                null,
//                false);
//
//        // TODO: add other format types.
//    }
//
//    private static void addFormatProperties(ModelDescriptorBuilder builder,
//            BeanDescriptorBuilder formatBuilder,
//            BaseClassDescriptor formatDescriptor) {
//
//        // A Format has a list of child Formats.
//        formatBuilder.addPropertyDescriptor(
//                FORMATS,
//                builder.getStandardListDescriptor(
//                        ArrayList.class,
//                        formatDescriptor),
//                null,
//                false);
//    }

}
