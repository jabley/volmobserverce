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

package com.volantis.mcs.model.impl.validation;

import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.impl.path.PathBuilderImpl;
import com.volantis.mcs.model.impl.validation.integrity.DefinitionScopeImpl;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.PathBuilder;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.I18NMessage;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.integrity.DefinitionScope;
import com.volantis.mcs.model.validation.integrity.DefinitionType;
import com.volantis.shared.stack.Stack;
import com.volantis.shared.stack.ArrayListStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 */
public class ValidationContextImpl
        implements ValidationContext {

    /**
     * The factory to use to create objects.
     */
    private final ModelFactory factory;

    /**
     * The list of diagnostics.
     */
    private final List diagnostics;

    /**
     * A map of DiagnosticTypes to a Stack of DiagnosticScopes.
     */
    private final Map type2ScopeStack;

    private final PathBuilder pathBuilder;

    /**
     * Initialise.
     */
    public ValidationContextImpl(ModelFactory factory) {
        this.factory = factory;
        diagnostics = new ArrayList();
        type2ScopeStack = new HashMap();
        pathBuilder = new PathBuilderImpl();
    }

    public void addDiagnostic(
            SourceLocation source, DiagnosticLevel level, I18NMessage message) {
        addDiagnostic(source, getCurrentPath(), level, message);
    }

    public void addDiagnostic(
            SourceLocation source, Path path, DiagnosticLevel level,
            I18NMessage message) {

        final DiagnosticImpl diagnostic =
                new DiagnosticImpl(source, path, level, message);
        diagnostics.add(diagnostic);
    }

    public I18NMessage createMessage(String messageKey) {
        return factory.createMessage(messageKey);
    }

    public I18NMessage createMessage(
            String messageKey, Object arg1, Object arg2) {
        return factory.createMessage(messageKey, arg1, arg2);
    }

    public I18NMessage createMessage(String messageKey, Object arg) {
        return factory.createMessage(messageKey, arg);
    }

    public I18NMessage createMessage(String messageKey, Object[] arguments) {
        return factory.createMessage(messageKey, arguments);
    }

    // Javadoc inherited.
    public DefinitionScope beginDefinitionScope(DefinitionType definitionType) {

        Stack scopeStack = (Stack) type2ScopeStack.get(definitionType);
        if (scopeStack == null) {
            scopeStack = new ArrayListStack();
            type2ScopeStack.put(definitionType, scopeStack);
        }
        DefinitionScope scope = new DefinitionScopeImpl(definitionType);
        scopeStack.push(scope);
        return scope;
    }

    // Javadoc inherited.

    public DefinitionScope getDefinitionScope(DefinitionType definitionType) {
        DefinitionScope scope = null;
        Stack scopeStack = (Stack) type2ScopeStack.get(definitionType);
        if (scopeStack != null) {
            scope = (DefinitionScope) scopeStack.peek();
        }
        return scope;
    }

    // Javadoc inherited.
    public void endDefinitionScope(DefinitionType definitionType) {
        Stack scopeStack = (Stack) type2ScopeStack.get(definitionType);
        if (scopeStack == null || scopeStack.isEmpty()) {
            throw new IllegalArgumentException(
                    "Scope for " + definitionType + " not found");
        }
        scopeStack.pop();
        if (scopeStack.isEmpty()) {
            type2ScopeStack.remove(definitionType);
        }
    }

    public Step pushPropertyStep(PropertyIdentifier property) {
        return pathBuilder.addPropertyStep(property);
    }

    public Step pushPropertyStep(String property) {
        return pathBuilder.addPropertyStep(property);
    }

    public Step pushIndexedStep(int index) {
        return pathBuilder.addIndexedStep(index);
    }

    public Path getCurrentPath() {
        return pathBuilder.getPath();
    }

    public void popStep(Step step) {
        Step removed = pathBuilder.removeStep();
        if (removed != step) {
            throw new IllegalStateException(
                    "Expected to remove " + step + " but removed " + removed);
        }
    }

    public List getDiagnostics() {
        return diagnostics;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/7	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/5	pduffin	VBM:2005101811 Added stack of objects in validation context

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
