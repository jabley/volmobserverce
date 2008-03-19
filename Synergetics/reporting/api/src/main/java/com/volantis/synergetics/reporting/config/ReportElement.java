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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.config;

/**
 * An object for jibx to map the report element of the reporting configuration
 * onto.
 */
public class ReportElement {
    /**
     * Exclusions list
     */
    protected ReportExclusion reportExclusion;

    /**
     * The sql handler this report should use (if any)
     */
    protected SqlHandler sqlHandler;

    /**
     * The generic handler this report should use if any.
     */
    protected GenericHandler genericHandler;

    /**
     * Indicates that this report is enabled
     */
    protected boolean enabled;

    /**
     * The name of the binding or interface that this report will be generated
     * for
     */
    protected String binding;

    // javadoc unnecessary
    public ReportExclusion getReportExclusion() {
        return this.reportExclusion;
    }

    // javadoc unnecessary
    public void setReportExclusion(ReportExclusion reportExclusion) {
        this.reportExclusion = reportExclusion;
    }

    // javadoc unnecessary
    public SqlHandler getSqlHandler() {
        return this.sqlHandler;
    }

    // javadoc unnecessary
    public void setSqlHandler(SqlHandler sqlHandler) {
        this.sqlHandler = sqlHandler;
    }

    // javadoc unnecessary
    public GenericHandler getGenericHandler() {
        return this.genericHandler;
    }

    // javadoc unnecessary
    public void setGenericHandler(GenericHandler genericHandler) {
        this.genericHandler = genericHandler;
    }

    // javadoc unnecessary
    public boolean getEnabled() {
        return this.enabled;
    }

    // javadoc unnecessary
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // javadoc unnecessary
    public String getBinding() {
        return this.binding;
    }

    // javadoc unnecessary
    public void setBinding(String binding) {
        this.binding = binding;
    }

}
