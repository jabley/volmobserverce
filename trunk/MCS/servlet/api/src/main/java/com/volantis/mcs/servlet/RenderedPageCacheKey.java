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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

/**
 * The key for the rendered page cache. This contains the following values:
 * <dl>
 * <dt>The hash of the input XDIME document</dt>
 * <dd>This is required to provide a reasonable likelihood that the input
 * document is the same as the document used to create the cached value. Note,
 * of course, that it does not provide a guarantee - two distinct documents
 * may provide the same hash. However, it is deemed that for the applications
 * where page caching is enabled, the combination of this value with the other
 * contents of the key will make it sufficiently unlikely to be an acceptable
 * risk. Note, of course, that this does <strong>not</strong> apply to any
 * page expected to contain confidential data of any sort, which should
 * always have page caching disabled.</dd>
 * <dt>The device name</dt>
 * <dd>Different devices produce different markup from the same input XDIME,
 * so this is required as part of the key.</dd>
 * <dt>The character set</dt>
 * <dd>Different output character sets may also produce different markup,
 * and should therefore be treated as distinct cache entries.</dd>
 * <dt>The request URI</dt>
 * <dd>It is likely that requests to different URIs will have different
 * expected output. We make the assumption that this will always be the case,
 * which reduces the risk of false matches (but may also result in some
 * 'real' matches being missed, particularly if the application encodes
 * information in the request URI).</dd>
 * <dt>The page fragmentation state</dt>
 * <dd>Different fragmentation states will lead to different output, so this
 * needs to be included.</dd>
 */
public final class RenderedPageCacheKey {
    /**
     * The String hash of the input XDIME.
     */
    private final long inputHash;

    /**
     * The device name.
     */
    private final String deviceName;

    /**
     * The character set.
     */
    private final String characterSet;

    /**
     * The request URI (excluding any parameters)
     */
    private final String requestURI;

    /**
     * The fragmentation state.
     */
    private final String fragmentationState;

    /**
     * A cached copy of this instance's hash code.
     */
    private final int hashCode;

    /**
     * Creates a new instance of RenderedPageCacheKey with the specified
     * values. Note that because the class is immutable, all values must
     * be specified at instance creation time.
     *
     * @param xdime the xdime document whose hash code will be used to form part
     * of this composite key.
     * @param deviceName The device name
     * @param characterSet The device character set
     * @param requestURI The request URI
     * @param fragmentationState The current fragmentation state
     */
    public RenderedPageCacheKey(String xdime, String deviceName,
                                String characterSet, String requestURI,
                                String fragmentationState) {
        this.inputHash = computeXDIMEHash(xdime);
        this.deviceName = (deviceName == null) ? "" : deviceName;
        this.characterSet = (characterSet == null) ? "" : characterSet;
        this.requestURI = (requestURI == null) ? "" : requestURI;
        this.fragmentationState =
                (fragmentationState == null) ? "" : fragmentationState;
        // Create a pre-cached version of the hash code for this instance -
        // we can do this because the key is immutable. This is calculated
        // using the recommendations in Josh Bloch's 'Effective Java'. It is
        // possible that this will increase the likelihood of a false cache
        // hit over a simple XOR of the values (which <STRONG>should</STRONG>
        // already be well distributed), but avoids the possibility of
        // pathological cases such as two strings being equal.
        int hashTemp = 17;
        hashTemp = 37 * hashTemp + this.requestURI.hashCode();
        hashTemp = 37 * hashTemp + this.characterSet.hashCode();
        hashTemp = 37 * hashTemp + this.fragmentationState.hashCode() ;
        hashTemp = 37 * hashTemp + this.deviceName.hashCode();
        Long l = new Long(this.inputHash);
        hashTemp = 37 * hashTemp + l.intValue();


        hashCode = hashTemp;
    }

    /**
     * Compute the hascode for an XDIME page. The algorithm is the same as the
     * {@link String#hashCode()}  method but with a long.
     * @param xdime the XDIME page
     * @return the hash code as a long
     */
    private long computeXDIMEHash(String xdime) {
        long hash = 0;
        if (xdime != null) {
            for (int i = 0; i < xdime.length(); i++) {
                hash = 31 * hash + xdime.charAt(i);
            }
        }
        return hash;
    }

    // Javadoc inherited
    public int hashCode() {
        return hashCode;
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        boolean equal = false;
        if (o != null && o.getClass() == this.getClass()) {
            RenderedPageCacheKey other = (RenderedPageCacheKey) o;
            // Note that we can assume the strings are not null - it is not
            // possible to create an instance of RenderedPageCacheKey
            // with null arguments
            if (other.inputHash == inputHash &&
                other.characterSet.equals(characterSet) &&
                other.deviceName.equals(deviceName) &&
                other.fragmentationState.equals(fragmentationState) &&
                other.requestURI.equals(requestURI)) {
                equal = true;
            }
        }
        return equal;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-May-05	7762/4	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 20-May-05	7762/2	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 14-Feb-05	6786/3	adrianj	VBM:2005012506 Rendered page cache rework

 11-Feb-05	6786/1	adrianj	VBM:2005012506 Added rendered page cache

 ===========================================================================
*/
