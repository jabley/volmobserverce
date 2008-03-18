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
package com.volantis.devrep.repository.impl.accessors.xml;

import com.volantis.devrep.device.api.xml.definitions.Category;
import com.volantis.devrep.device.api.xml.definitions.TypeDeclaration;
import com.volantis.devrep.device.api.xml.definitions.Policy;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class Definitions {

    private List types = new ArrayList();

    private List categories = new ArrayList();

    private Map nameToTypeMap = new HashMap();

    private Map nameToCategoryMap = new HashMap();

    private Map nameToPolicyMap;

    public void addCategory(Category category) {
        Category old = (Category)
                nameToCategoryMap.put(category.getName(), category);
        if (old != null) {
            throw new IllegalStateException("cannot merge categories");
        }

        categories.add(category);
    }

    public void addType(TypeDeclaration type) {
        TypeDeclaration old = (TypeDeclaration)
                nameToTypeMap.put(type.getName(), type);
        if (old != null) {
            throw new IllegalStateException("cannot add duplicate type");
        }
        types.add(type);
    }

    public Iterator categories() {
        return categories.iterator();
    }

    public Category getCategory(String categoryName) {
        return (Category) nameToCategoryMap.get(categoryName);
    }

    public Policy getPolicy(String policyName) {
        if (nameToPolicyMap == null) {
            nameToPolicyMap = new HashMap();
            Iterator categories = categories();
            while (categories.hasNext()) {
                Category category = (Category) categories.next();
                Iterator policies = category.policies();
                while (policies.hasNext()) {
                    Policy policy = (Policy) policies.next();
                    nameToPolicyMap.put(policy.getName(), policy);
                }
            }
        }
        return (Policy) nameToPolicyMap.get(policyName);
    }

    public TypeDeclaration getType(String typeName) {
        return (TypeDeclaration) nameToTypeMap.get(typeName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 ===========================================================================
*/
