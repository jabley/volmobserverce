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
package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.MemberName;
import com.volantis.mcs.protocols.widgets.MemberReference;
import com.volantis.mcs.protocols.widgets.MemberType;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MemberReferenceImpl implements MemberReference {
    /**
     * A member separator character.
     */
    private final static char MEMBER_SEPARATOR = '#';
    
    /**
     * Referenced widget ID.
     */
    private final String widgetId;
    
    /**
     * Referenced member names.
     */
    private final List memberNames;
    
    /**
     * Initializes this MemberReference.
     * 
     * @param referenceString
     * @throws XDIMEException
     */
    public MemberReferenceImpl(String referenceString) throws XDIMEException {
        this(null, referenceString);
    }
    
    /**
     * Initializes this MemberReference.
     * 
     * @param context The XDIME context.
     * @param referenceString A reference string, as specified in the XDIME attributes.
     * @throws XDIMEException if reference string is invalid, or member is not found. 
     */
    public MemberReferenceImpl(XDIMEContextInternal context, String referenceString) throws XDIMEException {
        String widgetId = null;
        
        List memberNames = null; 

        int lastMinusIndex = -1;
        
        MemberName lastMemberName = null;
        
        do {
            int minusIndex = referenceString.indexOf(MEMBER_SEPARATOR, lastMinusIndex + 1);

            // Extract next name component
            String component;
            
            if (minusIndex != -1) {
                // If minus sign was found, strip the name component from
                // previous up to the next minus sign.
                component = referenceString.substring(lastMinusIndex + 1, minusIndex);
            } else {
                // If minus sign was not found, strip the name component from
                // previous minus to the end of the string.
                component = referenceString.substring(lastMinusIndex + 1);
            }
            
            if (widgetId == null) {
                widgetId = component;
            } else {
                if (memberNames == null) {
                    memberNames = new ArrayList();
                }
                
                MemberName memberName;
                
                try {
                    memberName = MemberName.getMemberNameFor(component);
                } catch (IllegalArgumentException e) {
                    throw new XDIMEException("Undefined " + getMemberType().getName() + " reference: " + referenceString);
                }
                
                memberNames.add(memberName);
                
                lastMemberName = memberName;
            }
            
            // Store the position of last minus sign for next loop iteration.
            lastMinusIndex = minusIndex;
        } while (lastMinusIndex != -1);
        
        // Throw an exception, if reference is invalid.
        if (widgetId == null || memberNames == null) {
            throw new XDIMEException("Illegal " + getMemberType().getName() + " reference: " + referenceString);
        }
        
        // Check, that last member name is of correct type.
        if (lastMemberName.getMemberType() != getMemberType()) {
            throw new XDIMEException("Expected " + getMemberType().getName() + 
                    ", found " + lastMemberName.getMemberType().getName() + 
                    ": " + referenceString);
        }
        
        // After widget ID and member names are parsed, initalize
        // this reference with it.
        this.widgetId = widgetId;
        
        this.memberNames = Collections.unmodifiableList(memberNames);
    }

    // Javadoc inherited
    public String getWidgetId() {
        return widgetId;
    }

    // Javadoc inherited.
    public List getMemberNames() {
        return memberNames;
    }
    
    /**
     * Returns the type of the member for this reference.
     * 
     * @return  The type of the member for this reference.
     */
    protected abstract MemberType getMemberType();
}
