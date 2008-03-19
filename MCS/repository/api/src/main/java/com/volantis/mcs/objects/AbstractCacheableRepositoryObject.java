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
 * $Header: /src/voyager/com/volantis/mcs/objects/AbstractCacheableRepositoryObject.java,v 1.3 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Dec-02    Steve           VBM:2002090210 - An Abstract Repository object
 *                              with caching information for remote
 *                              policy objects.
 * 06-Feb-03    Allan           VBM:2003012905 - Removed incorrect 
 *                              mariner-object-field-length doclet javadoc 
 *                              statements on true/false values type fields - 
 *                              length is not significant and breaks 
 *                              validation. 
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.objects;

import com.volantis.mcs.objects.AbstractRepositoryObject;
import com.volantis.mcs.objects.RepositoryObjectIdentity;


/**
 * An Abstract Repository object with caching information for remote
 * policy objects.
 * @mariner-ignore-xml-attribute cacheThisPolicy
 * @mariner-ignore-xml-attribute retainDuringRetry
 * @mariner-ignore-xml-attribute retryFailedRetrieval
 * @mariner-ignore-xml-attribute timeToLive
 * @mariner-ignore-xml-attribute retryInterval
 * @mariner-ignore-xml-attribute retryMaxCount
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public abstract class AbstractCacheableRepositoryObject 
    extends AbstractRepositoryObject
    implements CacheableRepositoryObject {

  /**
   * Flag to determine if the component is to be cached
   *
   * @mariner-object-field-value "false"
   * @mariner-object-field-value "true"
   */  
  private boolean cacheThisPolicy;

  /**
   * Flag to determine if the component is to be retained during retries
   *
   * @mariner-object-field-value "false"
   * @mariner-object-field-value "true"
   */  
  private boolean retainDuringRetry;

  /**
   * Flag to determine if retries are attempted if retrieval fails
   *
   * @mariner-object-field-value "false"
   * @mariner-object-field-value "true"
   */  
  private boolean retryFailedRetrieval;
  
  /**
   * Number of seconds to hold this component in the cache
   *
   * @mariner-object-field-length 5
   */    
  private int timeToLive;

  /**
   * Number of seconds between retreival attempts
   *
   * @mariner-object-field-length 5
   */    
  private int retryInterval;

  /**
   * Number of retry attempts before failing
   *
   * @mariner-object-field-length 5
   */    
  private int retryMaxCount;
    

  /**
   * Create an unnamed component.
   */
  public AbstractCacheableRepositoryObject() {
      super();
  }

  /**
   * Create a component with the specified name.
   * @param name The name of the component.
   */
  public AbstractCacheableRepositoryObject(String name) {
    super( name );
  }

  /**
   * Create a new <code>AbstractComponent</code>.
   * @param identity The identity to use.
   */
  public AbstractCacheableRepositoryObject(RepositoryObjectIdentity identity) {
    super (identity);
  }

  /**
   * Set the policy caching attribute
   * @param flag boolean flag enabling or disabling caching 
   */
  public void setCacheThisPolicy( boolean flag ) {
      cacheThisPolicy = flag;
  }
  
  /**
   * Get the policy caching attribute
   * @return boolean flag denoting whether this policy should be cached
   */
  public boolean getCacheThisPolicy() {
      return cacheThisPolicy;
  }


  /**
   * Set whether or not this component is to be retained when a retry is
   * in progress.
   * @param flag boolean flag enabling or disabling retry caching
   */
  public void setRetainDuringRetry( boolean flag ) {
      retainDuringRetry = flag;
  }
  
  /** 
   * Get the retained during retry attribute
   * @return boolean flag sepcifying whether or not the component is retained.
   */
  public boolean getRetainDuringRetry() {
      return retainDuringRetry;
  }

  /**
   * Set whether or not component retrieval is retried on failure.
   * @param flag boolean flag specifying whether retries should be performed.
   */
  public void setRetryFailedRetrieval( boolean flag ) {
      retryFailedRetrieval = flag;
  }
  
  /** 
   * Get the retained during retry attribute
   * @return boolean flag sepcifying whether or not the component is retained.
   */
  public boolean getRetryFailedRetrieval() {
      return retryFailedRetrieval;
  }

  /**
   * Set the length of time for this component to live in a cache.
   * @param secs int amount of time in seconds
   */
  public void setTimeToLive( int secs ) {
      timeToLive = secs;
  }
  
  /**
   * Return the number of seconds that this component has to live in the cache
   * @return int the number of seconds.
   */
  public int getTimeToLive() {
      return timeToLive;
  }

  /**
   * Set the number of seconds between retrieval retry attempts
   * @param secs int the number of seconds between attempts
   */
  public void setRetryInterval( int secs ) {
      retryInterval = secs;
  }
  
  /**
   * Get the number of seconds between retry attempts
   * @return int number of seconds
   */
  public int getRetryInterval() {
      return retryInterval;
  }
  
  /** 
   * Set the maximum number of retries before the component is abandoned.
   * @param count int the maximum number of retries
   */
  public void setRetryMaxCount( int count ) {
      retryMaxCount = count;
  }

  /** 
   * Get the maximum number of retries before the component is abandoned.
   * @return int the maximum number of retries
   */
  public int getRetryMaxCount() {
      return retryMaxCount;
  }  
  
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 12-Jan-04	2532/1	andy	VBM:2004010903 implemented mariner-ignore-xml-attribute doclet tag

 ===========================================================================
*/
