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

package com.volantis.mcs.protocols.widgets.attributes;

/**
 * Validate element attributes
 */
public class ValidateAttributes extends WidgetAttributes {
    
    // These attributes are "real", in the sense that they may be legally passed
    // through the validate attributes.
    private String messageArea;
    private String messagePopup;
    private boolean auto;
    private String src;

    // These attribute are "virtual", in that they don't formally come from the
    // the external interface, rather they can be set internally and are used in 
    // creation of JS widget and arguments.
    private boolean isMultiple;
    private String inputElementId;
    private String invalidStyle;
    private boolean containsMessageValidationErrorAction;
    private boolean containsFocusValidationErrorAction;
    private String inputFormat;
    private String emptyMessageElementId;
    private String invalidMessageElementId;

    /**
     * @return Returns the auto.
     */
    public boolean isAuto() {
        return auto;
    }

    /**
     * @param auto The auto to set.
     */
    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    /**
     * @return Returns the inputFormat.
     */
    public String getInputFormat() {
        return inputFormat;
    }

    /**
     * @param inputFormat The inputFormat to set.
     */
    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    /**
     * @return Returns the invalidStyle.
     */
    public String getInvalidStyle() {
        return invalidStyle;
    }

    /**
     * @param invalidStyle The invalidStyle to set.
     */
    public void setInvalidStyle(String invalidStyle) {
        this.invalidStyle = invalidStyle;
    }

    /**
     * @return Returns the messageArea.
     */
    public String getMessageArea() {
        return messageArea;
    }

    /**
     * @param messageArea The messageArea to set.
     */
    public void setMessageArea(String messageArea) {
        this.messageArea = messageArea;
    }

    /**
     * @return Returns the containsFocusValidationErrorAction.
     */
    public boolean isContainsFocusValidationErrorAction() {
        return containsFocusValidationErrorAction;
    }

    /**
     * @return Returns the containsMessageValidationErrorAction.
     */
    public boolean isContainsMessageValidationErrorAction() {
        return containsMessageValidationErrorAction;
    }

    /**
     * @return Returns the src.
     */
    public String getSrc() {
        return src;
    }

    /**
     * @param src The src to set.
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * @return Returns the inputElementId.
     */
    public String getInputElementId() {
        return inputElementId;
    }

    /**
     * @param inputElementId The inputElementId to set.
     */
    public void setInputElementId(String inputElementId) {
        this.inputElementId = inputElementId;
    }

    /**
     * @return Returns the isMultiple.
     */
    public boolean isMultiple() {
        return isMultiple;
    }

    /**
     * @param isMultiple The isMultiple to set.
     */
    public void setMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    /**
     * @return Returns the emptyMessageElementId.
     */
    public String getEmptyMessageElementId() {
        return emptyMessageElementId;
    }

    /**
     * @param emptyMessageElementId The emptyMessageElementId to set.
     */
    public void setEmptyMessageElementId(String emptyMessageElementId) {
        this.emptyMessageElementId = emptyMessageElementId;
    }

    /**
     * @return Returns the invalidMessageElementId.
     */
    public String getInvalidMessageElementId() {
        return invalidMessageElementId;
    }

    /**
     * @param invalidMessageElementId The invalidMessageElementId to set.
     */
    public void setInvalidMessageElementId(String invalidMessageElementId) {
        this.invalidMessageElementId = invalidMessageElementId;
    }

    /**
     * @return Returns the containsFocusValidationErrorAction.
     */
    public boolean containsFocusValidationErrorAction() {
        return containsFocusValidationErrorAction;
    }

    /**
     * @param containsFocusValidationErrorAction The containsFocusValidationErrorAction to set.
     */
    public void setContainsFocusValidationErrorAction(
            boolean containsFocusValidationErrorAction) {
        this.containsFocusValidationErrorAction = containsFocusValidationErrorAction;
    }

    /**
     * @return Returns the containsMessageValidationErrorAction.
     */
    public boolean containsMessageValidationErrorAction() {
        return containsMessageValidationErrorAction;
    }

    /**
     * @param containsMessageValidationErrorAction The containsMessageValidationErrorAction to set.
     */
    public void setContainsMessageValidationErrorAction(
            boolean containsMessageValidationErrorAction) {
        this.containsMessageValidationErrorAction = containsMessageValidationErrorAction;
    }

    /**
     * @return Returns the messagePopup.
     */
    public String getMessagePopup() {
        return messagePopup;
    }

    /**
     * @param messagePopup The messagePopup to set.
     */
    public void setMessagePopup(String messagePopup) {
        this.messagePopup = messagePopup;
    }
    
}
