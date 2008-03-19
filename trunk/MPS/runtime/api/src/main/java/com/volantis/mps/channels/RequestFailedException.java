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
package com.volantis.mps.channels;

import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageException;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.Iterator;
import java.util.List;

/**
 * {@link MessageException} to indicate that sending messages to one or more
 * recipients failed.
 */
public class RequestFailedException extends MessageException {

    /**
     * The exception message localiser for this class.
     */
    private static ExceptionLocalizer LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            RequestFailedException.class);

    /**
     * The list of failed recipients.
     */
    private final List failedRecipients;

    /**
     * Creates an instance with a list of failed recipients. Failed recipients
     * should already have their failure reasons set.
     *
     * @param message the error message
     * @param failedRecipients the non-empty list of
     * {@link com.volantis.mps.recipient.MessageRecipient failed recipients} who
     * didn't recieve the message properly.
     * @throws IllegalArgumentException if the specified list is null or empty
     */
    public RequestFailedException(final String message,
                                  final List failedRecipients) {
        super(LOCALIZER.format(message));
        if (failedRecipients == null || failedRecipients.isEmpty()) {
            throw new IllegalArgumentException(
                "Failed recipient list must not be empty");
        }
        this.failedRecipients = failedRecipients;
    }

    /**
     * Returns an iterator over the recipients who failed to get a message. The
     * objects returned by the iterator are
     * {@link com.volantis.mps.recipient.MessageRecipient} instances.
     *
     * <p>Never returns null, never returns an empty iterator.</p>
     *
     * <p>The individual reasons of failures can be get from the recipients.</p>
     *
     * @return the list of the recipients
     */
    public Iterator getFailedRecipients() {
        return failedRecipients.iterator();
    }
}
