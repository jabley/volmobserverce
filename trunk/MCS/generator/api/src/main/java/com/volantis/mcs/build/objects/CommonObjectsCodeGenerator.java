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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.build.objects;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

/**
 * This class automatically generates identity objects for the classes
 * which have been processed.
 */
public class CommonObjectsCodeGenerator extends ObjectsCodeGenerator {

    // JavaDoc inherited from super class.
    public static boolean start(RootDoc rootDoc) {
        CommonObjectsCodeGenerator doclet =
                new CommonObjectsCodeGenerator(rootDoc);
        System.err.println();

        return doclet.processRootDoc();
    }

    /**
     * Create a new <code>ObjectsCodeGenerator</code>.
     *
     * @param rootDoc The JavaDoc object which represents the result of the
     *                javadoc parsing.
     */
    public CommonObjectsCodeGenerator(RootDoc rootDoc) {

        this.rootDoc = rootDoc;
    }

    /**
     * Override to allow some preparation to be done first.
     */
    protected boolean processRootDoc() {

        // Prepare any necessary files.
        PropertyValueLookUpUtilities.createPropertyValueLookUpFile(
                generatedDir);

        return super.processRootDoc();
    }

    /**
     * Process the class information.
     *
     * @param objectClassDoc The JavaDoc object which represents a class which
     *                       was parsed.
     */
    protected void processClassDoc(ClassDoc objectClassDoc) {
        boolean generatedUtilities = false;
        // Ignore abstract classes.
        //if (objectClassDoc.isAbstract()) {
        //    return;
        //}

        // Ignore classes which do not have any identity tags.
        Tag[] tags = objectClassDoc.tags("mariner-object-identity-field");
        if (tags.length == 0) {
            return;
        }

        RepositoryObjectInfo info = RepositoryObjectInfo.getInstance(rootDoc,
                objectClassDoc);

        generateIdentityClass(info);

        tags = objectClassDoc.tags("mariner-generate-property-lookup");
        if (tags.length != 0) {
            if (!generatedUtilities) {
                PropertyValueLookUpUtilities.generateUtilities(info,
                        generatedDir);
                generatedUtilities = true;
            }
            PropertyValueLookUpUtilities.updatePropertyValueLookUp(info,
                    generatedDir);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 17-Jan-05	6670/1	adrianj	VBM:2005010506 Implementation of resource asset continued

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Nov-04	5882/3	ianw	VBM:2004102008 Rework to make ObjectsCodeGenerator abstract
 ===========================================================================
*/
