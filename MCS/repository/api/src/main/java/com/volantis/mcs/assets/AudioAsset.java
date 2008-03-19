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
 * $Header: /src/voyager/com/volantis/mcs/assets/AudioAsset.java,v 1.24 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jun-01    Paul            VBM:2001051103 - Added this change history,
 *                              sorted out some comments, changed the
 *                              indentation from the rose style of 3 spaces
 *                              to 2, fixed the constructor, added equals and
 *                              hashCode methods and updated toString value.
 * 09-Jul-01    Paul            VBM:2001070902 - Added encoding property which
 *                              has moved back from Asset.
 * 16-Jul-01    Paul            VBM:2001070508 - Added identityMatches method.
 * 10-Aug-01    Payal           VBM:2001080902 - Added names to the audio types
 * 26-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 24-Oct-01    Paul            VBM:2001092608 - Removed the identityMatches
 *                              method and added the createIdentity method.
 *                              Also made sure that any changes to those
 *                              fields which form part of this objects identity
 *                              causes the cached identity to be discarded.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - add annotations for auto
 *                              generated accessors
 * 22-Mar-02    Adrian          VBM:2002031503 - re-added annotations for auto
 *                              generated accessors.
 * 02-Apr-02    Mat             VBM:2002022009 - Added
 *                              @mariner-generate-imd-accessor and also added
 *                              a constructor that takes an Attributes class.
 * 03-Apr-02    Mat             VBM:2002022009 - Removed constructor that
 *                              took an Attributes class.
 * 26-Apr-02    Mat             VBM:2002040814 - Added 
 *                              @mariner-generate-remote-accessor.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful and
 *                              added annotations to constrain field lengths,
 *                              and flag the required fields.
 * 26-Jul-02    Allan           VBM:2002072508 - Changed to extend
 *                              SubstantiveAsset.
 * 28-Nov-02    Mat             VBM:2002112213 - Added new asset encodings
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * AudioAsset represents an audio asset, such as an MP3 file or a Real Audio
 * file. The combination of name and encoding attributes are unique in the
 * repository.
 *
 * @mariner-hidden-comment The following tags are used by a code generation
 * doclet to automatically generate an Identity class for this class. Each
 * tag specifies the name of a field which constitutes part of the identity.
 * The order of the fields is the order of the parameters to the Identity
 * classes constructor.
 *
 * @mariner-object-identity-field project
 * @mariner-object-identity-field name
 * @mariner-object-identity-field encoding
 *
 * @mariner-object-null-is-empty-string-field value
 * @mariner-object-guardian com.volantis.mcs.components.AudioComponent
 *
 * @mariner-generate-imd-accessor
 *
 * @mariner-generate-property-lookup
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public class AudioAsset
  extends SubstantiveAsset {

    /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as adcmp32
   */
  public static final int ADCPM32 = 1;

  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as basic audio
   */
  public static final int BASIC_AUDIO = 2;

  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as gsm audio
   */
  public static final int GSM_AUDIO = 3;

  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as MIDI
   */
  public static final int MIDI_AUDIO = 4;

  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as mp3
   */
  public static final int MP3_AUDIO = 5;

  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as Windows media audio
   */
  public static final int WINDOWS_MEDIA_AUDIO = 6;

  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as Real Audio
   */
  public static final int REAL_AUDIO = 7;
  
  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as AMR Audio
   */
  public static final int AMR_AUDIO = 8;
  
  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as iMelody Audio
   */
  public static final int IMELODY_AUDIO = 9;
  
  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as SP Midi Audio
   */
  public static final int SP_MIDI_AUDIO = 10;
  
  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as WAV Audio
   */
  public static final int WAV_AUDIO = 11;
  
  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as RMF Audio
   */
  public static final int RMF_AUDIO = 12;
  
  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as NokRing Audio
   */
  public static final int NOKRING_AUDIO = 13;
  
  /**
   * The value assigned to the encoding to indicate that the audio is encoded
   * as SMAF Audio
   */
  public static final int SMAF_AUDIO = 14;

  /**
   * The name of the ADCPM32 audio type
   * @deprecated Internal use only.
   */
  public static final String ADCPM32_NAME = "adcpm32";

  /**
   * The name of the GSM_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String GSM_AUDIO_NAME = "gsm";

  /**
   * The name of BASIC_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String BASIC_AUDIO_NAME = "basic";

  /**
   * The name of the MIDI_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String MIDI_AUDIO_NAME = "midi";

  /**
   * The name of the MP3_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String MP3_AUDIO_NAME = "mp3";

  /**
   * The name of the WINDOWS_MEDIA_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String WINDOWS_MEDIA_AUDIO_NAME = "windowsMedia";

  /**
   * The name of the REAL_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String REAL_AUDIO_NAME = "real";
  
  /**
   * The name of the AMR_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String AMR_AUDIO_NAME = "amr";
  
  /**
   * The name of the IMELODY_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String IMELODY_AUDIO_NAME = "iMelody";
  
  /**
   * The name of the SP_MIDI_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String SP_MIDI_AUDIO_NAME = "spMidi";
  
  /**
   * The name of the WAV_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String WAV_AUDIO_NAME = "wav";
  
  /**
   * The name of the RMF_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String RMF_AUDIO_NAME = "rmf";
  
  /**
   * The name of the NOKRING_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String NOKRING_AUDIO_NAME = "nokring";
  
  /**
   * The name of the SMAF_AUDIO audio type
   * @deprecated Internal use only.
   */
  public static final String SMAF_AUDIO_NAME = "smaf";
  
  /**
   * The encoding of this asset.
   *
   * @mariner-object-field-xml-mapping AudioAsset.ADCPM32
   *                                   "adpcm32"
   * @mariner-object-field-xml-mapping AudioAsset.GSM_AUDIO
   *                                   "gsm"
   * @mariner-object-field-xml-mapping AudioAsset.BASIC_AUDIO
   *                                   "basic"
   * @mariner-object-field-xml-mapping AudioAsset.MIDI_AUDIO
   *                                   "midi"
   * @mariner-object-field-xml-mapping AudioAsset.MP3_AUDIO
   *                                   "mp3"
   * @mariner-object-field-xml-mapping AudioAsset.WINDOWS_MEDIA_AUDIO
   *                                   "windowsAudio"
   * @mariner-object-field-xml-mapping AudioAsset.REAL_AUDIO
   *                                   "realAudio"
   * @mariner-object-field-xml-mapping AudioAsset.AMR_AUDIO
   *                                   "amr"
   * @mariner-object-field-xml-mapping AudioAsset.IMELODY_AUDIO
   *                                   "iMelody"
   * @mariner-object-field-xml-mapping AudioAsset.SP_MIDI_AUDIO
   *                                   "spMidi"
   * @mariner-object-field-xml-mapping AudioAsset.WAV_AUDIO
   *                                   "wav"
   * @mariner-object-field-xml-mapping AudioAsset.RMF_AUDIO
   *                                   "rmf"
   * @mariner-object-field-xml-mapping AudioAsset.NOKRING_AUDIO
   *                                   "nokring"
   * @mariner-object-field-xml-mapping AudioAsset.SMAF_AUDIO
   *                                   "smaf"
   * @mariner-object-field-control-type ReadOnlyComboViewer
   */
  private int encoding;


  /**
   * Creates a new <code>AudioAsset</code> instance.
   *
   */
  public AudioAsset () {
    this ((String) null);
  }

  /**
   * Creates a new <code>AudioAsset</code> instance.
   *
   * @param name the name of this asset
   */
  public AudioAsset (String name) {
    this (name, null, null, BASIC_AUDIO);
  }

  /**
   * Creates a new <code>AudioAsset</code> instance.
   *
   * @param name the name of this asset
   * @param assetGroupName the name of the asset group for this asset
   * @param value the value of this asset
   * @param encoding the encoding of this asset
   */
  public AudioAsset (String name, String assetGroupName,
                     String value, int encoding) {
    setName (name);
    setAssetGroupName (assetGroupName);
    setValue (value);
    setEncoding (encoding);
  }

  /**
   * Create a new <code>AudioAsset</code>.
   * @param identity The identity of the <code>AudioAsset</code>.
   */
  public AudioAsset (AudioAssetIdentity identity) {
    super (identity);

    setEncoding (identity.getEncoding ());
  }

  /**
   * Get the value of encoding.
   * @return value of encoding.
   */
  public int getEncoding() {
    return encoding;
  }

  /**
   * Set the value of encoding.
   * @param encoding Value to assign to encoding.
   */
  public void setEncoding(int encoding) {
    this.encoding = encoding;
    identityChanged ();
  }

  // Javadoc inherited from super class.
  protected RepositoryObjectIdentity createIdentity () {
    return new AudioAssetIdentity(getProject(), getName(), getEncoding());
  }

  public boolean equals (Object object) {
    if (object instanceof AudioAsset) {
      AudioAsset o = (AudioAsset) object;
      return super.equals (o)
        && encoding == o.encoding;
    }

    return false;
  }

  public int hashCode () {
    return super.hashCode ()
      + encoding;
  }

  protected String paramString () {
    return super.paramString ()
      + "," + encoding;
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 06-Jan-04	2362/1	mat	VBM:2004010207 Enable fields to be tagged to output empty string values for attributes

 02-Jan-04	2332/1	richardc	VBM:2003122902 Property name changes and associated knock-ons

 17-Dec-03	2213/3	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 27-Nov-03	2013/2	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
