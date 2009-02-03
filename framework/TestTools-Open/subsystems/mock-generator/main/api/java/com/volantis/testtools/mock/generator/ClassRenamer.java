/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

public class ClassRenamer {

    public String rename(String fullyQualifiedName) {
        String pkgName;
        int index = fullyQualifiedName.lastIndexOf('.');
        if (index == -1) {
            pkgName = "";
        } else {
            pkgName = fullyQualifiedName.substring(0, index);
        }
        String className = fullyQualifiedName.substring(index + 1);
        String dstPkgName = renamePackage(pkgName);

        String dstClassName;
        if (dstPkgName.equals("")) {
            dstClassName = className + "Mock";
        } else {
            dstClassName = dstPkgName + "." + className + "Mock";
        }

        return dstClassName;
    }

    public String renamePackage(String pkgName) {
        String dstPkgName = pkgName;
        if (pkgName != null && !pkgName.equals("")) {
            if (!pkgName.startsWith("com.volantis" + ".")) {
                dstPkgName = "mock." + pkgName;
            }
        }

        return dstPkgName;
    }
}
