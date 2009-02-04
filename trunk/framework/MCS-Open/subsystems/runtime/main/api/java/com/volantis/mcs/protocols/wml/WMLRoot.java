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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLRoot.java,v 1.87 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Added this header, fixed
 *                              the code so that it only gets values out of
 *                              attributes once and uses append for each
 *                              separate part of the output string instead of
 *                              inline string concatenation. Also made use of
 *                              the new getStyle method in VolantisProtocol to
 *                              simplify the code, implemented the extended
 *                              function form methods and sorted out the
 *                              nested paragraph problem.
 * 16-Jul-01    Paul            VBM:2001070508 - Fixed javadoc comments.
 * 23-Jul-01    Paul            VBM:2001070507 - Simplified paragraph, added
 *                              support for the <template> tag, simplified
 *                              by renaming all *Attributes parameters to
 *                              attributes, stopped creating StringBuffers
 *                              when the return value was a fixed string,
 *                              reimplemented menus and removed the amount
 *                              of duplicated code needed in the subclasses
 *                              by using flags or methods which the subclasses
 *                              can set or override. Also, reimplemented
 *                              menus and added a helper method
 *                              appendCoreAttributes which appends the core
 *                              WML attributes to a buffer.
 * 24-Jul-01    Paul            VBM:2001071103 - Changed the doActionInput
 *                              method to get the field names from the list
 *                              of fields rather than from the list of field
 *                              names.
 * 26-Jul-01    Paul            VBM:2001071705 - Terminated the <input> tag
 *                              used for textarea.
 * 27-Jul-01    Paul            VBM:2001072603 - Added addBodyAttributes to
 *                              allow subclasses to add extra attributes to
 *                              the body tag.
 * 27-Jul-01    Paul            VBM:2001072704 - Added an inline title if the
 *                              device does not show the card title.
 * 01-Aug-01    Paul            VBM:2001072506 - Fixed minor problems with
 *                              doBooleanInput and doSelectInput.
 * 03-Aug-01    Allan           VBM:2001073008 - Added title support for
 *                              anchor. Added supporsTitleOnTR and
 *                              supportsTitleOnTD properties for WML
 *                              protocols that do support these features.
 *                              Modified doBooleanInput and doSelectInput
 *                              to also use the title attributes on option
 *                              and select tags.
 * 03-Aug-01    Doug            VBM:2001072504 Implemented Client-Side
 *                              Validation for extended forms.
 * 20-Aug-01    Payal           VBM:2001081617 Added br tag in openListItem
 *                              method to have br tags after each list item.
 * 21-Aug-01    Allan           VBM:2001082107 Removed class and id from
 *                              core attributes generated in
 *                              appendCoreAttributes().
 * 28-Aug-01    Allan           VBM:2001082810 - Take out the line in
 *                              doTextInput() that set the title to the
 *                              caption.
 * 04-Sep-01    Kula            VBM:2001083120 - overriding doMeta method is
 *                              implemented, which has an additional attribute
 *                              forua.
 * 04-Sep-01    Paul            VBM:2001081707 - Use getTextFromReference to get
 *                              the text in the correct encoding for those
 *                              attributes whose value could be a
 *                              TextComponentName.
 * 05-Sep-01    Kula            VBM:2001083120 - javadoc changed for doMeta
 *                              method
 * 07-Sep-01    Kula            VBM:2001083120 - javadoc updated for doMeta
 *                              method.
 * 13-09-01     Payal           VBM:2001091014 - Modified method openTable.
 *                              text alignment specified in the style for
 *                              a table will cause l,r,c values to be added
 *                              to the align attribute if values to be
 *                              added to the align attribute are left right
 *                              or center
 * 14-Sep-01    Paul            VBM:2001083114 - Added emulation of emphasis
 *                              tags and link highlighting.
 * 14-Sep-01    Paul            VBM:2001091302 - Made the select option's
 *                              caption default to the option's value if the
 *                              caption is null.
 * 14-Sep-01    Paul            VBM:2001091405 - Added support for name
 *                              attribute on xfaction.
 * 17-Sep-01    Allan           VBM:2001091103 - Override grid open
 *                              and close methods to ensure that all and
 *                              only one dimensional (ie. un-nested) tables
 *                              are generated.
 * 17-Sep-01    Allan           VBM:2001091103 - Modify rule that determines
 *                              whether or not to generate a table tag
 *                              for a layout - affects all openGrid and
 *                              closeGrid type methods.
 * 18-Sep-01    Allan           VBM:2001091014 - Add support for alignment
 *                              on columns in grids and tables. Methods
 *                              affected are openGridPreamble(), openTable(),
 *                              closeTable() and openTableDataCell().
 * 18-Sep-01    Allan           VBM:2001091014 - Ensure l alignment is set
 *                              when there is a style but text-align is not
 *                              set.
 * 19-Sep-01    Paul            VBM:2001091013 - Generate paragraphs naively
 *                              and leave it up to the WMLContentTree to
 *                              transform it into valid WML.
 * 20-Sep-01    Paul            VBM:2001091202 - Added support for implicit
 *                              values.
 * 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 * 21-Sep-01    Doug            VBM:2001090302 - Use getLinkFromReference to get
 *                              links for those attributes whose value could
 *                              be a LinkComponentName.
 * 28-Sep-01    Allan           VBM:2001070507 - addTableDataCellAttributes()
 *                              added. Modified openTableDataCellAttributes()
 *                              to call addTableDataCellAttributes().
 * 01-Oct-01    Allan           VBM:2001083120 - remove doMeta() and replace
 *                              with addMetaAttributes(). Add openHead()
 *                              and closeHead() to write out the head only if
 *                              there is content within the head.
 * 02-Oct-01    Allan           VBM:2001100205 - Check for null childPane
 *                              in openGridPreamble() before using it.
 * 02-Oct-01    Doug            VBM:2001100203 - output a name attrribute with
 *                              a unique intger as the value for the do element
 *                              in doActionInput. The value of 'name' must be
 *                              unique when he have multiple xfaction tags on
 *                              a page.
 * 04-Oct-01    Doug            VBM:2001100201 removed the protected
 *                              supportsAccessKeys property. This
 *                              property is now declared and initialised in
 *                              the super class VolantisProtocol under the
 *                              new name supportsAccessKeyAttribute.
 * 11-Oct-01    Allan           VBM:2001090401 - TableDataCell and
 *                              TableHeaderCell type methods changed to use
 *                              TableCellAttributes.
 * 12-Oct-01    Paul            VBM:2001101205 - Simplified generation of do
 *                              tag.
 * 15-Oct-01    Paul            VBM:2001101204 - Ignore fields which have no
 *                              entry pane when processing action fields.
 * 15-Oct-01    Paul            VBM:2001101207 - Add enteredAnchorBody and
 *                              exitedAnchorBody methods which set and clear
 *                              the insideAnchorBody flag respectively and
 *                              modify all the emphasis methods so that they
 *                              do not generate any output while inside an
 *                              anchor body.
 * 22-Oct-01    Pether          VBM:2001040901 Updated doImage to output
 *                              correct attributes from styles.
 * 23-Oct-01    Pether          VBM:2001101603 Added an override to the method
 *                              closeRowIteratorPaneElementPostamble.
 *                              Also added javadoc for that method.
 * 29-Oct-01    Paul            VBM:2001102901 - Modified to use new methods
 *                              in VolantisProtocol to retrieve context
 *                              information from Formats.
 * 12-Nov-01    Pether          VBM:2001102303 - Changed doHorizontalRuler to
 *                              approximate number of _ to use.
 * 15-Nov-01    Pether          VBM:2001102303 - In doHorizontalRuler now get
 *                              pixelsX from context insted of attributes.
 * 19-Nov-01    Pether          VBM:2001103001 - In openTableDataCell()
 *                              get the align attribute from table-align
 *                              instead of text-align.
 * 20-Nov-01    Pether          VBM:2001111602 - Changed the doSelectInput()
 *                              method to add the accesskey attribute to the
 *                              select tag.
 * 20-Nov-01    Pether          VBM:2001102303 - Added emulateHorizontalTag
 *                              variable and changed doHorizontalRuler to
 *                              use emulated tags if specified.
 * 22-Nov-01    Steve           VBM:2001112208 - Do not output 'null' string
 *                              if alignment is not set for tables.
 * 22-Nov-01    Paul            VBM:2001110202 - Called getUniqueId instead
 *                              of getCount to allow us to create more
 *                              complicated ids rather than use a single
 *                              number.
 * 26-Nov-01    Doug            VBM:2001112004 - Added call to quoteValue in
 *                              openAnchor() so that & are escaped in the href
 *                              value that is being written out.
 * 28-Nov-01    Steve           VBM:2001112706 - Do not output '' if alignment
 *                              is not set for tables.
 * 29-Nov-01    Steve           VBM:2001112902 - If there are no attributes
 *                              on the img tag, dont output it at all rather
 *                              than an empty img with a space alt text.
 * 03-Dec-01    Pether          VBM:2001102303 -  Changed emulateHorizontalTag
 *                              variable to boolean and changed
 *                              doHorizontalRuler to use charachterx and only
 *                              emulate if emulate flag is set to true.
 * 07-Dec-01    Paul            VBM:2001120702 - Prevented the implicit values
 *                              from being ignored when generating the actions.
 *                              They were being ignored because they had no
 *                              entry pane set but they should always be
 *                              generated.
 * 07-Dec-01    Paul            VBM:2001120703 - Provide default false and
 *                              true values if values were not supplied, or
 *                              could not be retrieved.
 * 11-Dec-01    Allan           VBM:2001112914 - Added a <br/> to the fragment
 *                              link mark up.
 * 19-Dec-01    Paul            VBM:2001120506 - Removed tags in file.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 10-Jan-02    Adrian          VBM:2002010901 - Changed doSelectInput() to
 *                              use default ivalue in select tag.  Code was in
 *                              place but wrong variable was referenced and
 *                              index was 0 based, now 1 based.
 * 18-Jan-02    Steve           VBM:2002011102 - Added doDivideHint for
 *                              dissection hints.
 * 21-Jan-02    Mat             VBM:2002011801 - Changed call to getUniqueID()
 *                              to generateWMLActionID() to generate a
 *                              unique ID.
 * 24-Jan-02    Payal           VBM:2001102303 - Modified  doHorizontalRule()
 *                              to change the hr character to a hypen instead
 *                              of an underline and corrected the number of
 *                              chars to be calculated.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 05-Feb-02    Adrian          VBM:2002012901 -  Modified appendOpenGridRow
 *                              to open a <p> tag if the number of columns is
 *                              less than 2.  Likewise appendCloseGridRow
 *                              closes the </p> tag where the number of cols is
 *                              less than 2.  This is to ensure the content of
 *                              each row is started on a new line in the output
 * 12-Feb-02    Adrian          VBM:2001112204 - modified methods...
 *                              appendCloseDefinitionData
 *                              appendCloseDefinitionTerm
 *                              ... to output </br> tags to align the output.
 * 12-Feb-02    Paul            VBM:2002021201 - Removed calls to getPageHead
 *                              method in MarinerPageContext as it is now
 *                              accessible to subclasses of StringProtocol
 *                              through a protected field.
 * 13-Feb-02    Paul            VBM:2002021203 - Added getContentExtension
 *                              method.
 * 14-Feb-02    Paul            VBM:2002021203 - Added implementation of
 *                              appendServerSideInclude.
 * 15-Feb-02    Paul            VBM:2002021203 - Rename server side include to
 *                              portlet include.
 * 15-Feb-02    Mat             VBM:2002021203 - Removed '%' from the
 *                              quoteTable, as is shouldn't have been there.
 * 19-Feb-02    Paul            VBM:2001100102 - Added the form specifier
 *                              parameter to the parameters to be sent when
 *                              submitting a form and also make use of the new
 *                              value attribute on xfaction.
 * 20-Feb-02    Steve           VBM:2001101803 - re-routes form destination to
 *                              fragmentation servlet if the form is
 *                              fragmented.
 * 26-Feb-02    Steve           VBM:2001101803 - fragmentation renamed to
 *                              MarinerFFP and the context path is preprended
 *                              to ensure the correct servlet is executed.
 * 28-Feb-02    Allan           VBM:2002022701 - Reverted back to look for
 *                              text-align in appendOpenTableDataCell(). Also
 *                              backed out the 2002012901 change to
 *                              appendOpenGridRow and changed
 *                              appendCloseGridRow to append a br tag. This
 *                              badly formed wml where a p can be the child
 *                              of another p.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 01-Mar-02    Mat             VBM:2002021203 - Added appendSSIInclude() and
 *                              appendSSIConfig()
 * 04-Mar-02    Paul            VBM:2001101803 - Replaced a lot of duplicated
 *                              code which retrieved the initial value of a
 *                              field element with calls to getInitialValue.
 *                              Properly supported multiple select values
 *                              by encoding any values which can contain ;
 *                              before they are added to the page and splitting
 *                              the ; separated list of values from the request
 *                              into multiple strings and then decoding them.
 *                              Modified doActionInput due to changes to
 *                              StringProtocol.
 * 08-Mar-02    Paul            VBM:2002030607 - Only generate extra
 *                              <wml><card> in portlets which are generated
 *                              out of context. Stopped calling toString on
 *                              StringOutputBuffers.
 * 13-Mar-02    Paul            VBM:2002031301 - Renamed get/endContentBuffer
 *                              to get/endCurrentBuffer respectively.
 * 13-Mar-02    Paul            VBM:2002030104 - Removed classic form methods.
 * 22-Feb-02    Paul            VBM:2002021802 - Moved from protocols package
 *                              and modified to make it generate a DOM.
 * 27-Mar-02    Steve           VBM:2002021103 - Handle the DIV element
 *                              This is not actually valid in WML but MAML has
 *                              extended <div> with a keeptogether attribute
 *                              which denotes that enclosed elements should be
 *                              kept together if possible.
 * 28-Mar-02    Allan           VBM:2002022007 - Updated constructor for
 *                              change of quoteTable to an array.
 * 03-Apr-02    Paul            VBM:2002021802 - Added the text associated with
 *                              the menu item to the anchor element.
 * 04-Apr-02    Paul            VBM:2002021802 - Renamed closeRowIteratorPane
 *                              to closeRowIteratorPaneElement so that line
 *                              breaks are added after cell elements rather
 *                              than at the end of the pane.
 * 09-Apr-02    Allan           VBM:2002040912 - Added a ; to the encoding
 *                              for an & in quoteTable in the constructor.
 * 10-Apr-02    Adrian          VBM:2002031907 - Removed System.println calls.
 * 22-Apr-02    Allan           VBM:2002042203 - Ensure all href and src
 *                              attributes are quoted. Methods doImage,
 *                              doRolloverImage, doMenuItem and doActionInput
 *                              affected.
 * 25-Apr-02    Paul            VBM:2002042202 - Removed some unused code,
 *                              stopped the % being replaced with a " " when
 *                              quoting and prevented the initial header from
 *                              being written.
 * 28-Apr-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 03-May-02    Paul            VBM:2002042203 - Updated dissection code and
 *                              added support for maxPageSize.
 * 07-May-02    Adrian          VBM:2002042302 - Removed method getContent..
 *                              ..Extension() as the cached file extension is
 *                              defined in the device policies.
 * 09-May-02    Ian             VBM:2002031203 - Changed log4j to use string.
 * 10-May-02    Adrian          VBM:2002040808 - Corrected bugs in calls to
 *                              style in openParagraph.  Also default to
 *                              mode="wrap" in openParagraph.
 * 21-May-02    Adrian          VBM:2002021111 - Updated openAnchor and close..
 *                              ..Anchor to write go and postfields tags in
 *                              place of URL parameters where the new theme
 *                              style MarinerHttpMethodHint is set to "post"
 * 23-May-02    Paul            VBM:2002042202 - Stopped quoting attributes
 *                              as outputter does that correctly now.
 * 05-Jun-02    Adrian          VBM:2002021103 - Removed methods openDiv and
 *                              closeDiv
 * 05-Jun-02    Byron           VBM:2002053002 - Added support for tabindex by
 *                              adding tabindex attribute to protocol output.
 *                              Added supportsTabindex attribute and modified
 *                              doTextInput(), doBooleanInput() and
 *                              doSelectInput() methods
 * 14-Jun-02    Byron           VBM:2002052707 - Removed $$ and % mapping
 *                              from the quote table in WMLRoot constructor
 * 14-Jun-02    Byron           VBM:2002042502 - Added processLinkAction(),
 *                              processButtonAction() and processPostFields().
 *                              Modified method doActionInput() to call either
 *                              processButtonAction or processLinkAction based
 *                              on the link style.
 * 17-Jun-02    Byron           VBM:2002061001 - Changed capitalisation for
 *                              xxxxtabindex
 * 20-Jun-02    Adrian          VBM:2002053102 - Do not write paragraph tag if
 *                              insideAnchorBody as this results in nested
 *                              paragraphs the are later promoted and split the
 *                              anchor into two.
 * 24-Jun-02    Adrian          VBM:2001112204 - Open/Close p tag on methods
 *                              openDefinitionList and closeDefinitionList.
 * 25-Jun-02    Mat             VBM:2002040202 - Changed tables to get
 *                              alignment working correctly.
 * 03-Jul-02    Chris W         VBM:2002061901 - Moved the code that adds the
 *                              vform postfield (processPostFields method)
 *                              to the beginning of the method. (The last
 *                              postfield is corrupted by the Openwave SDK wap
 *                              browser.)
 * 09-Jul-02    Adrian          VBM:2002070404 - Updated method initialise to
 *                              set styleSheetRenderer as this is required by
 *                              css emulation.
 * 24-Jul-02    Steve           VBM:2002072301 - Added requiresEmptyOK flag, if
 *                              this is set to true then the emptyok=true
 *                              attribute is added to the <input> element.
 * 25-Jul-02    Byron           VBM:2002071505 - Added overloaded methods
 *                              setAttribute(..) and quoteLiteralDollar() that
 *                              quote an attribute before setting its value.
 *                              Modified numerous calls to setAttribute()
 * 26-Jul-02    Steve           VBM:2002072301 - Removed requiresEmptyOK flag
 *                              as there is now a WMLEmptyOK1_3 protocol
 *                              which overrides the doTextInput() method the
 *                              emptyok is now passed in the text input
 *                              attributes.
 * 30-Jul-02    Sumit           VBM:2002073007 - doMeta implemented to produce
 *                              meta tag according to arch doc.
 * 31-Jul-02    Paul            VBM:2002073008 - Implemented package methods
 *                              so that each canvas in a package becomes a card
 *                              in the deck. Also implemented body methods
 *                              in terms of card methods.
 * 01-Aug-02    Sumit           VBM:2002073109 - optgroup support added. Moved
 *                              option manupilation into recursive functions.
 *                              Changed doSelect to use these functions.
 * 01-Aug-02    Steve           VBM:2002040202 - Changed case of 'lcr' table
 *                              alignment to 'LCR'
 * 02-Aug-02    Sumit           VBM:2002080213 - Changed recursion to support
 *                              keep track of index
 * 06-Aug-02    Paul            VBM:2002073008 - Added support for generating
 *                              overlay canvasses into separate cards.
 * 06-Aug-02    Sumit           VBM:2002080509 - Added support for onevent
 *                              handler for <card> and for the <timer> element
 * 06-Aug-02    Ian             VBM:2002080603 - Support new WapTV stylistic
 *                              properties.
 * 06-Aug-02    Paul            VBM:2002080509 - Rewrote the event code to
 *                              get the scripts from the EventAttributes.
 * 07-Aug-02    Paul            VBM:2002080509 - Convert timer duration from
 *                              ms to 1/10s.
 * 08-Aug-02    Ian             VBM:2002080603 - Updated Javadoc.
 * 13-Aug-02    Paul            VBM:2002080603 - Prevented duplicate ontimer
 *                              events being written and added width and height
 *                              to the img element if the protocol supports it.
 * 14-Aug-02    Allan           VBM:2002081302 - Removed obselete call to
 *                              getStyle() in openTable().
 * 15-Aug-02    Paul            VBM:2002081421 - Added support for xfactions
 *                              to be rendered outside of a form. This involved
 *                              restructuring the code to maximise the use of
 *                              common code and to allow it to be easily
 *                              extended by derived classes. Also made the
 *                              reset action work as it did not work at all.
 *                              The main methods affected were doActionInput
 *                              and the methods that it calls.
 * 20-Aug-02    Adrian          VBM:2002081316 - updated doImage to write only
 *                              altText to outputbuffer if image source is null
 * 21-Aug-02	Mat             VBM:2002081508 - Changed openBody() to take
 *                              account of the uaContext attribute.
 * 27-Aug-02    Steve           VBM:2002082304 - lower case conversion for
 *                              the type attribute in xftextinput, the method
 *                              attribute in xfform and the type attribute in
 *                              xfaction
 * 16-Sep-02    Ian             VBM:2002091006 - Added support for
 *                              marinerFocus by splitting out new method
 *                              addActionDoAttributes.
 * 01-Oct-02    Allan           VBM:2002093002 - Modified writeOptGroups() to
 *                              set the title attribute for an option from
 *                              the prompt attribute provided by papi.
 * 07-Oct-02    Allan           VBM:2002100202 - Modified writeOptGroups() to
 *                              use getTextFromReference() when setting the title
 *                              for an optgroup tag.
 * 07-Oct-02    Allan           VBM:2002100708 - Modified writeOptGroups() to
 *                              set the title attibute of an optgroup to the
 *                              getTextFromReference() on the prompt if the
 *                              result of getTextFromReference() on the caption is
 *                              null.
 * 10-Oct-02    Adrian          VBM:2002100404 - update writePageHead to call
 *                              throw IOException.
 * 14-Oct-02    Sumit           VBM:2002083002 - Changed doBooleanInput to
 *                              increment initial if it is present upto max 2
 * 18-Nov-02    Geoff           VBM:2002111504 - Avoid deprecated methods of
 *                              page context, removed unused imports and
 *                              locals.
 * 04-Dec-02    Phil W-S        VBM:2002111208 - Added handling of the new
 *                              previous and next shard link style class
 *                              attributes in openDissectingPane.
 * 06-Jan-03    Steve           VBM:2002082304 - removed StringConvertor method
 *                              calls in favour of methods already present in
 *                              the String class.
 * 13-Jan-02    Allan           VBM:2002120209 - Modified doSelectInput() to
 *                              not handle accesskey. Fixed doAnchor() javadoc
 * 14-Jan-03    Sumit           VBM:2003011413 - Added open/closeFontWeight
 *                              and made open/closePara and ope/closeSpan call
 *                              these methods is a style exist
 * 21-Jan-03    Byron           VBM:2003011617 - Modified openActionAnchor to
 *                              set the attribute 'accesskey' if a shortcut
 *                              value exists.
 * 22-Jan-03    Doug            VBM:2002120213 - Rewrote the doSelectInput()
 *                              method. This involved removing numerous
 *                              supporting methods.
 *                              Added the inner SelectionRender classes.
 * 23-Jan-03    Doug            VBM:2003012304 - Fully qualified all access
 *                              to the VolantisProtocol mehtods from the
 *                              SelectionRenderer inner classes.
 * 23-Jan-03    Doug            VBM:2002120213 - I Accidentally removed fix
 *                              2003011617. Reinstated lost code.
 * 29-Jan-03    Byron           VBM:2003012803 - Modified constructor to set
 *                              protocolConfiguration value and any static
 *                              variables dependent on it.
 * 13-Feb-03    Byron           VBM:2003021309 - Added implementation for
 *                              getPackagingType.
 * 17-Feb-03    Sumit           VBM:2003021301 - Added align attributes in
 *                              openGridRow if grid cols is < 2.
 * 21-Feb-03    Sumit           VBM:2003022101 - currentColumn is now a stack
 *                              to allow for portals and nested grids
 * 25-Feb-03    Sumit           VBM:2003021301 - In openPara added a line
 *                              to add para alignment if the pane has it set
 * 27-Feb-03    Byron           VBM:2003022105 - Added overloaded
 *                              addOnEventElement method and called it from the
 *                              original method (of the same name).
 * 03-Mar-03    Byron           VBM:2003022813 - Modified addActionReset to
 *                              call getTextFromReference.
 * 14-Mar-03    Doug            VBM:2003030409 - Modified openDisectingPane()
 *                              to write out the
 *                              GENERATE_NEXT_LINK_FIRST_ATTRIBUTE attribte.
 * 19-Mar-03    Mat             VBM:2003031712 - Changed mapping of @ in the
 *                              quote table.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 21-Mar-03    Byron           VBM:2003031907 - Modified openDissectingPane to
 *                              use updated text for shards.
 * 24-Mar-03    Sumit           VBM:2003032006 - Added field that maintains
 *                              current p element and adds alignment on a
 *                              single column grid. Removed currentColumnStack
 *                              and addGridAlignment
 * 25-Mar-03    Steve           VBM:2003031907 - If the previous/next shard
 *                              link text has not been overridden, read it from
 *                              the pane format in the layout.
 * 02-Apr-03    Geoff           VBM:2003032609 - Extend single column grid
 *                              rendering to include both grid and pane format
 *                              alignment; modified openGridRow(),
 *                              closeGridRow() and openPane().
 * 08-Apr-03    Sumit           VBM:2003032713 - Added render support for menu
 *                              item groups
 * 09-Apr-03    Steve           VBM:2003040702  Use WMLOutputter and
 *                              WMLCharacterEncoder to write output.
 *                              Removed quoteLiteralDollar method as this is
 *                              now handled by WMLOutputter. Added
 *                              setVariableStyle() method to set which
 *                              variable types should be generated.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * 17-Apr-03    Chris W         VBM:2003031909 - getXFImplicitAttributesValue
 *                              added to determine whether a postfield's value
 *                              should come from the value or clientVariableName
 *                              attribute of a xfimplicit element. This new
 *                              method is called from addPostField. Added to do
 *                              comment to getXFImplicitAttributesValue
 * 17-Apr-03    Geoff           VBM:2003040305 - Modified two overloads of
 *                              addOnEventElement, getTaskForEvent and
 *                              doInlineAction to use the new DOMScript to
 *                              render WMLTasks as DOM elements.
 * 24-Apr-03    Chris W         VBM:2003030404 - Added supportsNativeMarkup to
 *                              indicate if the protocol supports the maml
 *                              nativemarkup tag. writeOpenNativeMarkup,
 *                              writeCloseNativeMarkup and
 *                              getNativeMarkupOutputBuffer added to support
 *                              nativemarkup maml element. openCard and
 *                              closeCard modified to push/pop an outputbuffer
 *                              containing the card's contents.
 * 25-Apr-03    Chris W         VBM:2003031905 - Changed openCard(). Instead
 *                              of pushing a DOMOutputBuffer onto the stack to
 *                              store the card element's body we use the
 *                              DOMOutputBuffer called wml.card.beforebody.
 *                              Addition of wml.card.onevent, wml.card.timer
 *                              and wml.card.beforebody moved from closeCard()
 *                              to openLayout().
 * 09-May-03    Phil W-S        VBM:2002111502 - Override openPhoneNumber and
 *                              closePhoneNumber to do the required "a" element
 *                              generation, add empty new method
 *                              addPhoneNumberAttributes and augment the
 *                              writeOpenPhoneNumber and writeClosePhoneNumber
 *                              methods to ensure that the insideAnchorBody
 *                              member is managed correctly.
 * 21-May-03    Chris W         VBM:2003040403 - Calls to DOMOutputBuffer.app..
 *                              endLiteral() changed to appendEncoded()
 * 23-May-03    Mat             VBM:2003042907 - Renamed getXMLOutputter() to
 *                              getDocumentOutputter() and removed static
 *                              XMLOutputter static variable.
 * 30-May-03    Chris W         VBM:2003052702 - openDissectingPane() and
 *                              closeDissectingPane() changed to write out new
 *                              special shard link elements after the dissecting
 *                              pane is closed.
 * 01-May-03    Mat             VBM:2003042912 - Changed
 *                              addHorizontalMenuItemSeparator() to return
 *                              new NBSP literal.
 * 23-May-03    Mat             VBM:2003042907 - Renamed getXMLOutputter() to
 *                              getDocumentOutputter() and removed static
 *                              XMLOutputter static variable.
 * 21-May-03    Chris W         VBM:2003040403 - Calls to DOMOutputBuffer.app..
 *                              endLiteral() changed to appendEncoded()
 * 30-May-03    Mat             VBM:2003042911 - Added getWBSAXOutputter() to
 *                              return a relevant outputter according to the
 *                              accept headers.
 * 30-May-03    Chris W         VBM:2003052702 - openDissectingPane() and
 *                              closeDissectingPane() changed to write out new
 *                              special shard link elements after the dissecting
 *                              pane is closed.
 * 30-May-03    Mat             VBM:2003042906 - Added generateDocument()
 * 31-May-03    Geoff           VBM:2003042906 - Hack about with
 *                              encodingWriter to try and fix encoding probs.
 * 02-Jun-03    Steve           VBM:2003042906 - When content is written through
 *                              content attributes it is not checked for dollar
 *                              encoding.
 * 02-Jun-03    Steve           VBM:2003042906 - Removed the above fix. Any $
 *                              not in nativemarkup is treated as a literal.
 * 23-Jun-03    Steve           VBM:2003061807 - 2003042906 is re-enabled
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.charset.Encoding;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.dissection.DissectableAreaAttributes;
import com.volantis.mcs.dissection.DissectableAreaIdentity;
import com.volantis.mcs.dissection.DissectedDocument;
import com.volantis.mcs.dissection.DissectionCharacteristicsImpl;
import com.volantis.mcs.dissection.DissectionContext;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.DissectionURLManager;
import com.volantis.mcs.dissection.Dissector;
import com.volantis.mcs.dissection.DocumentInformationImpl;
import com.volantis.mcs.dissection.RequestedShards;
import com.volantis.mcs.dissection.ShardIterator;
import com.volantis.mcs.dissection.dom.DebugContentHandler;
import com.volantis.mcs.dissection.dom.DissectionElementTypes;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.dom.output.SerialisationURLListener;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter;
import com.volantis.mcs.dom2wbsax.WBSAXIgnoreElementProcessor;
import com.volantis.mcs.dom2wbsax.WBSAXProcessorContext;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BigAttributes;
import com.volantis.mcs.protocols.BlockQuoteAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.BoldAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.CaptionAttributes;
import com.volantis.mcs.protocols.CiteAttributes;
import com.volantis.mcs.protocols.CodeAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMScript;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.DefaultStyleSheetHandler;
import com.volantis.mcs.protocols.DefinitionDataAttributes;
import com.volantis.mcs.protocols.DefinitionListAttributes;
import com.volantis.mcs.protocols.DefinitionTermAttributes;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.EmphasisAttributes;
import com.volantis.mcs.protocols.EncodingWriter;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.FragmentLinkRendererContext;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.ItalicAttributes;
import com.volantis.mcs.protocols.KeyboardAttributes;
import com.volantis.mcs.protocols.LayoutAttributes;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MetaAttributes;
import com.volantis.mcs.protocols.MonospaceFontAttributes;
import com.volantis.mcs.protocols.MontageAttributes;
import com.volantis.mcs.protocols.NativeMarkupAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.ProtocolWriter;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.SampleAttributes;
import com.volantis.mcs.protocols.Script;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.SelectionRenderer;
import com.volantis.mcs.protocols.SmallAttributes;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.StrongAttributes;
import com.volantis.mcs.protocols.SubscriptAttributes;
import com.volantis.mcs.protocols.SuperscriptAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableBodyAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableFooterAttributes;
import com.volantis.mcs.protocols.TableHeaderAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.TimedRefreshInfo;
import com.volantis.mcs.protocols.TimerAttributes;
import com.volantis.mcs.protocols.UnderlineAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFBooleanAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.ValidationHelper;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.css.renderer.RuntimeCSSStyleSheetRenderer;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.dissection.ShardLinkMenu;
import com.volantis.mcs.protocols.dissection.ShardLinkMenuModelBuilder;
import com.volantis.mcs.protocols.forms.ImplicitFieldType;
import com.volantis.mcs.protocols.forms.ActionFieldType;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FieldHandler;
import com.volantis.mcs.protocols.forms.MultipleSelectFieldType;
import com.volantis.mcs.protocols.forms.validation.TextInputFormatParser;
import com.volantis.mcs.protocols.forms.validation.TextInputFormat;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactoryFilter;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.shared.DefaultMenuModule;
import com.volantis.mcs.protocols.menu.shared.DefaultMenuModuleRendererFactory;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.styles.PositivePixelLengthHandler;
import com.volantis.mcs.protocols.styles.PositivePixelLengthOrPercentageHandler;
import com.volantis.mcs.protocols.styles.PropertyHandler;
import com.volantis.mcs.protocols.styles.PropertyRenderer;
import com.volantis.mcs.protocols.styles.ValueHandlerToPropertyAdapter;
import com.volantis.mcs.protocols.trans.CompoundTransformer;
import com.volantis.mcs.protocols.trans.WhiteSpaceFixTransformer;
import com.volantis.mcs.protocols.wml.menu.WMLShardLinkMenuModuleRendererFactoryFilter;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.runtime.configuration.WMLOutputPreference;
import com.volantis.mcs.runtime.dissection.DissectionUtilities;
import com.volantis.mcs.runtime.dissection.RuntimeDissectionContext;
import com.volantis.mcs.runtime.dissection.RuntimeDissectionURLManager;
import com.volantis.mcs.runtime.dissection.URLOptimiser;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.CaptionSideKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.MCSFormActionStyleKeywords;
import com.volantis.mcs.themes.properties.MCSHttpMethodHintKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.wbdom.dissection.DissectableWBDOMFactory;
import com.volantis.mcs.wbdom.dissection.WBDOMDissectableDocument;
import com.volantis.mcs.wbdom.dissection.io.DissectionWBSAXParser;
import com.volantis.mcs.wbdom.dissection.io.WBDOMDissectedContentHandler;
import com.volantis.mcs.wbdom.io.WBSAXParser;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXDisassembler;
import com.volantis.mcs.wbsax.io.WBXMLProducer;
import com.volantis.mcs.wbsax.io.WMLProducer;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.shared.content.ContentStyle;
import com.volantis.styling.Styles;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;
import com.volantis.synergetics.cornerstone.utilities.WhitespaceUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Class: WMLRoot, the base WML sub-class<p>
 * <p/>
 * This sub-class of the root protocol class is itself a root for different
 * versions of WML. The methods here are generalised WML Version 1.1 tags. For
 * each version that sub-classes this, only a few methods will need to be
 * overridden to customise support. Most will work "as is".<p>
 * <p/>
 * WML does not allow paragraphs to be nested but does require that just about
 * everything else belongs inside a paragraph tag. Previously everything which
 * needed a paragraph around it would generate one, unless it was already
 * inside a paragraph. Unfortunately this method did not work properly because
 * the order in which the paragraphs were generated did not match the order in
 * which they appeared in the final WML. This caused a problem with the
 * extended forms.<p>
 * <p/>
 * A better solution was for Panes to generate paragraphs rather than the tags,
 * this is because the order in which panes are output defines the order in
 * which the output appears in the final page and also everything ends up in a
 * pane.<p>
 * <p/>
 * Unfortunately that did not work properly either because there is some markup
 * better solution is to wrap the whole layout in a single paragraph.
 */

public abstract class WMLRoot extends DOMProtocol
    implements WMLConstants, WMLVariable, DissectionConstants,
    MenuRendererContext {

    private Element gridRowElement;

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(WMLRoot.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(WMLRoot.class);

    /**
     * Set of inline stylistic elements that certain devices have issues
     * honoring whitespace with
     */
    private static final Set inlineStyleWhitespaceElements = new HashSet();

    /**
     * Set of inline link based elements that certain devices have issues
     * honoring whitespace with
     */
    private static final Set inlineLinkWhitespaceElements = new HashSet();

    static {
        // add the various elements that some devices have trouble honouring
        // whitespace with the the sets.
        inlineStyleWhitespaceElements.add("b");
        inlineStyleWhitespaceElements.add("big");
        inlineStyleWhitespaceElements.add("em");
        inlineStyleWhitespaceElements.add("i");
        inlineStyleWhitespaceElements.add("small");
        inlineStyleWhitespaceElements.add("strong");
        inlineStyleWhitespaceElements.add("u");

        inlineLinkWhitespaceElements.add("a");
    }

    /**
     * Name of buffer that will hold template tag output.
     */
    protected static final String PAGE_TEMPLATE_BUFFER_NAME = "template";

    /**
     * The default false value to use for xfboolean field.
     */
    private static final String XFBOOLEAN_DEFAULT_FALSE_VALUE = "0";

    /**
     * The default true value to use for xfboolean field.
     */
    private static final String XFBOOLEAN_DEFAULT_TRUE_VALUE = "1";

    private static final String CARDS_IN_REGIONS = "CARDS_IN_REGIONS";
    private static final String CARDS_NOT_IN_REGIONS
        = "CARDS_NOT_IN_REGIONS";

    /**
     * Default style properties for the stylistic XDIME/PAPI elements.
     */
    private static CompiledStyleSheet defaultStyleSheetCache;

    // Initialise the default styles.
    static {
        initialiseDefaultStyles();
    }

    /**
     * Initialises the default style sheet cache that will be used to populate
     * the <code>compiledDefaultStyleSheet</code> property on object
     * construction.
     */
    private static void initialiseDefaultStyles() {
        InputStream cssInputStream = 
                WMLRoot.class.getResourceAsStream("WMLRoot.css");

        // only try and compile it if the resource was found - for example it
        // may not be found during tests.
        if (cssInputStream != null) {
            defaultStyleSheetCache = DefaultStyleSheetHandler
                    .compileStyleSheet(cssInputStream);
        }
    }

    /**
     * Selection Renderer to use when rendering menu style selections
     */
    protected SelectionRenderer defaultSelectionRenderer;

    /**
     * Object used to calculate the ivalue for select elements
     */
    protected final InitialValueHandler initialOptionHandler;

    /**
     * Specifies if this version of WML supports a title attrtibute on the TR
     * tag.
     */
    protected boolean supportsTitleOnTR = false;

    /**
     * Specifies if this version of WML supports a title attrtibute on the TD
     * tag.
     */
    protected boolean supportsTitleOnTD = false;

    /**
     * Specifies if this version of WML supports width and height on the img
     * tag.
     */
    protected boolean supportsImgDimensions = false;

    /**
     * Flag which indicates whether do should always be used to support
     * xfaction. If it is false then anchor should be used if it needs to be
     * inline.
     */
    protected boolean alwaysUseDoForAction = false;

    /**
     * The information needed to emulate the horizontal tag,  if false
     * emulation is not required.
     */
    protected boolean emulateHorizontalTag;

    /**
     * If this flag is true then we are processing the body of an anchor,
     * otherwise we are not.
     */
    protected boolean insideAnchorBody;

    /**
     * Specifies if this protocol may support tabindex attribute
     */
    protected boolean supportsTabindex = true;

    /**
     * This buffer contains extra cards which appeared in regions.
     */
    private DOMOutputBuffer cardsInRegions;

    /**
     * This buffer contains extra cards which did not appear in regions.
     */
    private DOMOutputBuffer cardsNotInRegions;

    /**
     * True if this protocol is about to generate WMLC rather than WML.
     */
    private Boolean generateWMLC;

    /**
     * The WBXML version of the charset encoding to use.
     */
    private CharsetCode charsetCode;

    /**
     * The style of variable to generate
     */
    protected char variableStyle = WMLVariable.WMLV_NOBRACKETS;

    private final WMLDollarEncoder dollarEncoder;

    /**
     * A menu module for "special" shard link menus.
     */
    private MenuModule shardLinkMenuModule;

    /**
     * The main transformer for this protocol. This could be a compound
     * transformer (StyleInversionTransformer and UnabridgedDOMTransformer).
     */
    private DOMTransformer transformer;

    protected PropertyHandler widthHandler;
    protected PropertyRenderer heightHandler;

    private PropertyRenderer lineHeightRenderer;

    private PropertyRenderer wordSpacingRenderer;

    /**
     * The optional timed refresh information object. This may be null.
     */
    private TimedRefreshInfo timedRefreshInfo;

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     *                       objects.
     * @param configuration  The protocol specific configuration.
     */
    public WMLRoot(ProtocolSupportFactory supportFactory,
                   ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        styleSheetRenderer = RuntimeCSSStyleSheetRenderer.getSingleton();

        quoteTable[172] = " ";
        quoteTable['@'] = "@";
        quoteTable['\''] = "&apos;";
        initialOptionHandler = new InitialValueHandler();

        dollarEncoder = new WMLDollarEncoder();

        // WML protocols support fragmentation.
        supportsFragmentation = true;

        compiledDefaultStyleSheet = defaultStyleSheetCache;
    }

    /**
     * Initialise the protocol. This is called after the context has been
     * initialised so it can be queried for information.
     */
    public void initialise() {
        super.initialise();

        if(logger.isDebugEnabled()) {
            logger.debug ("Initialising (WMLRoot)" + this);
        }

        transformer = createDOMTransformer();

        emulateHorizontalTag
            = context.getBooleanDevicePolicyValue(EMULATE_WML_HORIZONTAL_TAG);

        // Get the maximum page size.
        String value = context.getDevicePolicyValue(MAX_WML_DECK_SIZE);
        maxPageSize = -1;
        if (value != null) {
            try {
                maxPageSize = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                logger.error("page-size-invalid-maximum", new Object[]{value},
                             nfe);
            }
        }

        // Some protocol variants support automatic shortcut prefix display
        supportsAutomaticShortcutPrefixDisplay =
            supportsAccessKeyAttribute &&
            context.getDevice().getBooleanPolicyValue(
                    DevicePolicyConstants.SUPPORTS_WML_ACCESSKEY_AUTOMAGIC_NUMBER_DISPLAY);
    }

    // Javadoc inherited.
    protected ContentStyle calculateOutputStyle() {
        ContentStyle outputStyle;

        // Calculate the output style for WML.
        //
        // In this case the output style depends on whether we are generating
        // WMLC so we must calculate that first.
        //
        // NOTE: this *must* currently be calculated lazily rather than during
        // initialisation as the charset is only calculated after the protocol
        // is initialised, due to the "limitations" of the MSRC / MPC /
        // Protocol.initialise chain of methods.

        // Calculate whether the page will generate WMLC. This also calculates
        // the WBSAX charset code as a side effect.
        if (generateWMLC == null) {
            calculateGenerateWMLC();
        }

        // Output style is derived from whether we will generate WMLC or not.
        if (Boolean.TRUE.equals(generateWMLC)) {
            outputStyle = ContentStyle.BINARY;
        } else {
            outputStyle = ContentStyle.TEXT;
        }

        return outputStyle;
    }

    /**
     * Calculate if this page will {@link #generateWMLC}.
     * <p>
     * This calculates {@link #charsetCode} as a side effect as the two values
     * are co-dependent.
     */
    private void calculateGenerateWMLC() {

        // Assume we want to generate WMLC.
        boolean willGenerateWMLC = true;

        // Calculate if the device repository thinks that the device supports
        // WMLC.
        WMLCDeviceSupport wmlcSupported =
            WMLCDeviceSupport.getMode(context.getRequestContext());

        // Calculate if this device request (including headers) can result in
        // WMLC generation.
        MarinerRequestContext requestContext = context.getRequestContext();
        ApplicationContext appContext =
            ContextInternals.getApplicationContext(requestContext);
        if (wmlcSupported == WMLCDeviceSupport.WML) {
            // Device says no WMLC
            willGenerateWMLC = false;
        } else if (wmlcSupported == WMLCDeviceSupport.WMLC) {
            // Device says support WMLC
            willGenerateWMLC = true;
        } else {
            // Decide if we will generate binary WMLC from the headers
            willGenerateWMLC = appContext.isWMLCSupported();
            // If we want to do WMLC but the config says do WML, then do WML
            if (willGenerateWMLC) {
                ProtocolsConfiguration protocols =
                        context.getVolantisBean().getProtocolsConfiguration();

                if (protocols.getPreferredOutputFormat() ==
                        WMLOutputPreference.WML) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Device supports WMLC but disabling " +
                                "due to wml configuration setting.");
                    }
                    willGenerateWMLC = false;
                }
            }
        }

        // Calculate the MIBEnum to use. This depends on and also affects
        // whether we are going to generate WMLC.
        int mibEnum;
        final String charsetName = context.getCharsetName();
        if (charsetName == null) {
            throw new RuntimeException(
                exceptionLocalizer.format("charset-not-found"));
        }
        // If we are still trying to generate WMLC,
        if (willGenerateWMLC) {
            // Then carefully check that the charset encoding's MIBenum value
            // is valid in a WMLC context.
            Encoding charsetEncoding = context.getCharsetEncoding();
            if (charsetEncoding == null) {
                throw new RuntimeException(
                    exceptionLocalizer.format("charset-not-found"));
            }
            int requestedMibEnum = charsetEncoding.getMIBEnum();
            switch (requestedMibEnum) {
                case Encoding.MIBENUM_NOT_CONFIGURED:
                    // Fall back to WML if there is no MIBEnum value configured.
                    // Seems nicer than just throwing an exception, although it
                    // may cause the problem to go unnoticed.
                    logger.warn("charset-mibenum-missing",
                                new Object[]{charsetName});
                    willGenerateWMLC = false;
                    // We need a valid charset code even for text rendering,
                    // even though it is ignored, so just use unknown for now.
                    mibEnum = CharsetCode.UNKNOWN;
                    break;
                case Encoding.MIBENUM_NOT_REGISTERED:
                    // If this charset was not registered with IANA, there is
                    // no valid MIBEnum value, so we send WBXML "unknown" value.
                    if (logger.isDebugEnabled()) {
                        logger.debug("Using charset '" +
                                charsetName + "' with " +
                                "configured MIBEnum '0' (not registered " +
                                "at IANA), sending WBXML 'unknown' value");
                    }
                    mibEnum = CharsetCode.UNKNOWN;
                    break;
                default:
                    // A normal MIBEnum, just use it as is.
                    if (logger.isDebugEnabled()) {
                        logger.debug("Using normal charset " +
                                charsetName);
                    }
                    mibEnum = requestedMibEnum;
                    break;
            }
        } else {
            // We are not generating WMLC.
            // In this case the mibEnum is required but will not actually be
            // used so just use unknown for now.
            mibEnum = CharsetCode.UNKNOWN;
        }

        // Create a charset code with the requested charset name and the
        // interpreted MIBEnum from the encoding.
        // The mibEnum value will be be used if we are generating WMLC, and the
        // charset name will be used if we are generating WML.
        charsetCode = new CharsetCode(mibEnum, charsetName);

        generateWMLC = Boolean.valueOf(willGenerateWMLC);
    }

    // Javadoc inherited.
    protected void createStyleEmulationRenderer() {
        super.createStyleEmulationRenderer();
        // Finally tell the renderer that these elements should not
        // have style emulation applied to them.
        styleEmulationRenderer.exclude("a");        
    }

    protected DOMTransformer createDOMTransformer() {
        final boolean deviceHonoursAlign =
            context.getBooleanDevicePolicyValue(
                DEVICE_HONOURS_ALIGN_WHEN_MODE_NOWRAP);

        DOMTransformer transformer = new WMLDOMTransformer(this,
                deviceHonoursAlign);

        if (!deviceHonoursSpacingForInlineStylingOpenElements()) {
            transformer = new CompoundTransformer(transformer,
                    new WhiteSpaceFixTransformer(inlineStyleWhitespaceElements,
                                                 inlineLinkWhitespaceElements));
        }

        return transformer;
    }

    // Javadoc inherited.
    protected void initialiseStyleHandlers() {
        super.initialiseStyleHandlers();

        widthHandler = new ValueHandlerToPropertyAdapter(
            StylePropertyDetails.WIDTH,
            new PositivePixelLengthHandler());
        heightHandler = new ValueHandlerToPropertyAdapter(
            StylePropertyDetails.HEIGHT,
            new PositivePixelLengthHandler());

        lineHeightRenderer = new ValueHandlerToPropertyAdapter(
                StylePropertyDetails.LINE_HEIGHT,
                new PositivePixelLengthOrPercentageHandler());

        wordSpacingRenderer = new ValueHandlerToPropertyAdapter(
                StylePropertyDetails.WORD_SPACING,
                new PositivePixelLengthOrPercentageHandler());
    }

    // Javadoc inherited
    public void createWriters() {
        // WMLDollarEncoderWritter should be applied only for
        // "native markup" therefore content writer is simple ProtocolWriter
        // from now. We must do that to avoid dollar encoding for "non-native markups"
        // protocolWriter is common for both content and native Writers as it was before
        ProtocolWriter protocolWriter = new ProtocolWriter (this);
        contentWriter = protocolWriter;
        nativeWriter = new WMLDollarEncoderWriter(protocolWriter);
    }


    protected boolean supportsTabindex() {
        return supportsTabindex;
    }

    // ========================================================================
    //   General helper methods.
    // ========================================================================

    // Javadoc inherited.
    protected FragmentLinkRendererContext createFragmentLinkRendererContext() {

        return new WMLFragmentLinkRendererContext(this);
    }

    /**
     * Write the montage page.
     */
    protected void writeMontageContent(PackageBodyOutput output,
                                       MontageAttributes attributes)
        throws IOException, ProtocolException {

        // Create the document, only add the doc type if writing the head.
        createDocument(writeHead);

        // If we need to log the page output now before we do anything else
        // with it.
        logPageOutput("PAGE OUTPUT");

        // Write out the page.
        writeDocument(output);
    }

    // --------------------------------------------------------------------------
    //   Page Generation
    // --------------------------------------------------------------------------

    protected void writeDocument(PackageBodyOutput output)
        throws IOException, ProtocolException {

        MarinerRequestContext requestContext = context.getRequestContext();
        ApplicationContext appContext =
            ContextInternals.getApplicationContext(requestContext);

        // If the accept headers indicate that WMLC is supported, create a
        // WBXML producer. If not, default to WMLProducer.
        WBSAXContentHandler handler = null;
        WBSAXDocumentOutputter wbsaxOutputter;
        WBSAXContentHandler producer;

        // If the user has not asked to know the output style of this protocol,
        // then we will not have calculated whether we will be generating WMLC.
        // In this case we better generate it now as we are about to need it.
        if (generateWMLC == null) {
            calculateGenerateWMLC();
        }

        // Check that the outputs of calculateGenerateWMLC are available.
        if (charsetCode == null || generateWMLC == null) {
            throw new IllegalStateException(
                    "charsetCode and generateWMLC must be available");
        }

        EnvironmentContext envContext = context.getEnvironmentContext();
        if (Boolean.TRUE.equals(generateWMLC)) {
            // Generate binary WMLC.
            envContext.setContentType("application/vnd.wap.wmlc");
            producer = new WBXMLProducer(output.getOutputStream());
        } else {
            // For WML, the entity encoding is done now. The encoding writer
            // is passed to the producer to do the encoding.
            envContext.setContentType("text/vnd.wap.wml");
            Writer out = output.getWriter();
            Writer enc = new EncodingWriter(out, getCharacterEncoder());
            producer = new WMLProducer(out, enc);
        }

        SerialisationURLListener urlListener = null;
        final PackageResources pr = appContext.getPackageResources();
        if (pr != null) {
            // Force the packager to understand that only a subset of
            // assets are likely to be included in the response
            pr.initializeEncodedURLs();

            urlListener = new SerialisationURLListener() {
                public void foundURL(String url) {
                    pr.addEncodedURLCandidate(url);
                }
            };
        }

        WMLRootConfiguration wmlConfiguration = (WMLRootConfiguration)
            protocolConfiguration;

        Encoding charsetEncoding = context.getCharsetEncoding();
        if (charsetEncoding == null) {
            throw new RuntimeException(
                exceptionLocalizer.format("charset-not-found"));
        }

        // Create the shared context that all the objects which participate
        // in serialising the MCSDOM use.
        Codec codec = new Codec(charsetCode);
        StringTable stringTable = new StringTable();
        StringFactory strings = new StringFactory(codec);
        StringReferenceFactory references =
            new StringReferenceFactory(stringTable, strings);
        WBSAXProcessorContext processorContext =
            new WBSAXProcessorContext(
                wmlConfiguration.getElementNameFactory(),
                wmlConfiguration.getAttributeStartFactory(), strings,
                references, charsetEncoding, stringTable,
                wmlConfiguration);

        // Create the WBSAXDocumentOutputter - this serialises the MCSDOM into
        // WBSAX events. This will then be configured for any special
        // processing that it needs to do.
        wbsaxOutputter = new WBSAXDocumentOutputter(processorContext);

        // Set up the null name element processor.
        // This will ignore any null name elements introduced into the MCSDOM
        // during the DOM optimisation process.
        wbsaxOutputter.addSpecialElementProcessor(null,
                                                  new WBSAXIgnoreElementProcessor(
                                                      processorContext));

        // todo: move "final" disassembler here to show accesskeys
        // Otherwise it is too late to capture accesskey output properly and
        // you will see null data in AccesskeyOpaqueValues rather than the
        // appropriate accesskey values which appear in the output (hopefully).
        // This will require refactoring since the "final" disassember is
        // currently cut and pasted below twice (dissection/none).
        // It might be a good idea to add a description to the disassember
        // constructor as well since dissection uses multiple disassemblers in
        // one pipeline.

        // If this protocol supports access key attributes...
        if (supportsAccessKeyAttribute) {
            // ... then set up the special processing for access keys.

            // This is required to calculate access key values when we are
            // using numeric shortcut style menus against WML which, unlike
            // Openwave, does not support this automagically.
            // NOTE: we could slightly improve performance by only doing this
            // when actually required, but whether it is worth calculating this
            // is not clear at this stage.
            //
            // This consists of two parts as outlined below.

            // First, we add a special element processor to the outputter which
            // picks up any special ACCESSKEY-ANNOTATION elements that have
            // been inserted into the MCSDOM by the protocol for each element
            // which uses an accesskey which requires a calculated value and
            // translates them into special opaque WBSAX events during the
            // MCSDOM output phase.
            boolean doesDeviceDisplayAccesskeyNums =
                context.getDevice().getBooleanPolicyValue(DevicePolicyConstants.
                                                          SUPPORTS_WML_ACCESSKEY_AUTOMAGIC_NUMBER_DISPLAY);
            wbsaxOutputter.addSpecialElementProcessor(
                AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT,
                new WBSAXAccesskeyAnnotationElementProcessor(processorContext,
                                                             !doesDeviceDisplayAccesskeyNums));

            // Second, we add a special filter to the producer which picks up
            // the special opaque WBSAX events created by the element processor
            // above and calculates the appropriate accesskey value as required
            // during the WBDOM output phase. This needs to be done here so
            // that dissection doesn't end up throwing accesskey values away.
            producer = new AccesskeyWBSAXFilter(codec, producer,
                                                wmlConfiguration.getAttributeStartFactory());
        }

        if (isDissectionNeeded()) {
            prepareAndWriteViaDissection(wbsaxOutputter,
                                         producer,
                                         urlListener,
                                         wmlConfiguration,
                                         processorContext,
                                         handler);
        }
        else {
            // We are writing out directly rather than via WBDOM and
            // dissection.

            StringWriter out = null;
            try {
                if (logger.isDebugEnabled()) {
                    out = new StringWriter();
                    handler = new WBSAXDisassembler(producer, out);
                } else {
                    handler = producer;
                }

                processorContext.setContentHandler(handler);

                // Since we are writing out directly, we must configure the
                // WBSAX Outputter to do the things that the dissector would
                // normally do.
                processorContext.setFinalOutput(true);
                processorContext.setUrlListener(urlListener);

                wbsaxOutputter.output(document);
            } finally {
                if (logger.isDebugEnabled()) {
                    out.flush();
                    logger.debug(out);
                }
            }
        }

    }

    private void prepareAndWriteViaDissection(WBSAXDocumentOutputter wbsaxOutputter,
                                    WBSAXContentHandler producer,
                                    SerialisationURLListener urlListener,
                                    WMLRootConfiguration wmlConfiguration,
                                    WBSAXProcessorContext processorContext,
                                    WBSAXContentHandler handler
                                    ) throws IOException, ProtocolException {

        URLOptimiser optimiser;
        WBSAXContentHandler filter;

        if (logger.isDebugEnabled()) {
            logger.debug("Using dissection");
        }

        // Set up the dissection element processors.
        wbsaxOutputter.addSpecialElementProcessor(
                DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT,
                new WBSAXDissectionElementProcessor(processorContext,
                                                DissectionElementTypes.getDissectableAreaType()));
        wbsaxOutputter.addSpecialElementProcessor(
                DissectionConstants.KEEPTOGETHER_ELEMENT,
                new WBSAXDissectionElementProcessor(processorContext,
                                                DissectionElementTypes.getKeepTogetherType()));
        wbsaxOutputter.addSpecialElementProcessor(
                DissectionConstants.SHARD_LINK_ELEMENT,
                new WBSAXShardLinkElementProcessor(processorContext,
                                               DissectionElementTypes.getShardLinkType()));
        wbsaxOutputter.addSpecialElementProcessor(
                DissectionConstants.SHARD_LINK_GROUP_ELEMENT,
                new WBSAXDissectionElementProcessor(processorContext,
                                                DissectionElementTypes.getShardLinkGroupType()));
        wbsaxOutputter.addSpecialElementProcessor(
                DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT,
                new WBSAXDissectionElementProcessor(processorContext,
                                                DissectionElementTypes.getShardLinkConditionalType()));

        // Factory to create dissection WBDOM nodes.
        DissectableWBDOMFactory dissectionFactory =
            new DissectableWBDOMFactory(wmlConfiguration);
        // Handler to parse WBSAX events into WBDOM nodes.
        WBSAXParser parser = new DissectionWBSAXParser(dissectionFactory,
                                                       wmlConfiguration);
        StringWriter out = null;
        try {
            if (logger.isDebugEnabled()) {
                out = new StringWriter();
                handler = new WBSAXDisassembler(parser, out);
            } else {
                handler = parser;
            }

            // Plugin optimiser
            optimiser = new URLOptimiser(handler);
            optimiser.setPageContext(getMarinerPageContext());
            if (logger.isDebugEnabled()) {
                out = new StringWriter();
                filter = new WBSAXDisassembler(optimiser, out);
            } else {
                filter = optimiser;
            }

            processorContext.setContentHandler(filter);
            wbsaxOutputter.output(document);
        } finally {
            if (logger.isDebugEnabled()) {
                out.flush();
                logger.debug(out);
            }
        }

        try {
            if (logger.isDebugEnabled()) {
                out = new StringWriter();
                handler = new WBSAXDisassembler(producer, out);
            } else {
                handler = producer;
            }
            
            super.writeViaDissection(
                    new WBDOMDissectableDocument(parser.getDocument()),
                    new WBDOMDissectedContentHandler(handler, wmlConfiguration, urlListener));
            
        } finally {
            if (logger.isDebugEnabled()) {
                out.flush();
                logger.debug(out);
            }
        }
    }
    
    protected void generateCSS() throws IOException {
        // do nothing
    }

    /**
     * Optionally add the title attribute to the element provided.
     */
    protected void addTitleAttribute(Element element,
                                     MCSAttributes attributes,
                                     boolean title) {

        if (title) {
            setAttribute(element, "title", attributes.getTitle());
        }
    }

    // ========================================================================
    //   Page element methods
    // ========================================================================

    // Javadoc inherited from super class.
    public String defaultMimeType() {
        return "text/vnd.wap.wml";
    }

    // javadoc unnecessary
    public String getPackagingType() {
        return "multipart/mixed";
    }

    public void writePageHead() throws IOException {

        super.writePageHead();

        // Get the buffer which contains the whole page.
        DOMOutputBuffer pageBuffer = getPageBuffer();

        // Get the buffer which contains the contents of the template element.
        DOMOutputBuffer template
            = getExtraBuffer(PAGE_TEMPLATE_BUFFER_NAME, false);
        if (template != null) {
            Element element = pageBuffer.openElement("template");
            pageBuffer.addOutputBuffer(template);
            pageBuffer.closeElement(element);
        }

    }

    // ------------------------------------------------------------------------
    //   Card Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    protected void addCardAttributes(Element element,
                                     CanvasAttributes attributes) {
    }

    // Javadoc inherited from super class.
    private void addCardEvents(DOMOutputBuffer dom,
                                 CanvasAttributes attributes)
        throws ProtocolException {

        addOnEventElement(dom, attributes, "ontimer",
                          EventConstants.ON_TIMER);
        addEnterEvents(dom, attributes);
    }

    /**
     * Adds onEnter events to the dom.
     *
     * @param dom        The output Dom.
     * @param attributes The canvas attributes.
     */

    protected void addEnterEvents(DOMOutputBuffer dom,
                                  CanvasAttributes attributes)
        throws ProtocolException {
        // The onenter event is a WapTV extension which is activated when a
        // card is entered from either direction. On WML if this is set we
        // should set both the onenterforward and onenterbackward event. When
        // we add support for them then they will override the value entered
        // for onenter. We will not attempt to merge them in.
        addOnEventElement(dom, attributes, "onenterforward",
                          EventConstants.ON_ENTER_FORWARD);
        addOnEventElement(dom, attributes, "onenterbackward",
                          EventConstants.ON_ENTER_BACKWARD);

    }

    // Javadoc inherited from super class.
    protected void openCard(DOMOutputBuffer dom,
                            CanvasAttributes attributes)
        throws ProtocolException {

        String value;

        Element element = dom.openStyledElement("card", attributes);

        // Add the id if specified.
        if ((value = attributes.getId()) != null) {
            element.setAttribute("id", value);
        }
        value = attributes.getUaContext();
        if ("new".equals(value)) {
            setAttribute(element, "newcontext", "true");
        }

        // Add a title if specified.
        addTitleAttribute(element, attributes, true);

        // Allow subclasses to add extra attributes to card.
        addCardAttributes(element, attributes);

        // Add card events.
        addCardEvents(dom, attributes);

        // Store emulated emphasis tag markup in wml.card.beforebody
        // This ensure that native markup for onevent and timer elements
        // appears first.
        DeviceLayoutContext dlc = context.getDeviceLayoutContext();
        dom = (DOMOutputBuffer)dlc.getOutputBuffer(
            NativeMarkupAttributes.WML_CARD_BEFOREBODY, true);

        // NOTE: stylistic markup emulation handled in openLayout instead.
    }

    // Javadoc inherited from super class.
    protected void closeCard(DOMOutputBuffer dom,
                             CanvasAttributes attributes) {

        // NOTE: stylistic markup emulation handled in closeLayout instead.

        dom.closeElement("card");
    }

    /**
     * Add the open layout markup to the specified DOMOutputBuffer. All sub
     * classes must call this version of the method. Failure to do so means
     * that the native markup written to wml.card.timer, wml.card.onevent and
     * wml.card.beforebody will not be written out. See AN009 Marlin Markup
     * Language p19 for more details.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openLayout(DOMOutputBuffer dom,
                              LayoutAttributes attributes) {
        /* Add on onevent, timer and any other elements before the card's
         * body. These are specified by the maml nativemarkup element.
         *
         * Some parts of the page output are written straight into the dom.
         * However other parts are written out later when writeLayout() is called
         * e.g.
         *
         * <canvas>   ---> written to dom
         *    <p>     ---> written when writeLayout called
         * </canvas>  ---> written to dom
         *
         * In order to get the markup produced by writeLayout(), written into the
         * correct place, we need to save an insertion point here before we close
         * the canvas element.
         *
         * Then, when writeLayout is called we restore the insertion point so that
         * the markup produced by this method is written into the correct place.
         *
         * This is especially important, when a page has been included inside
         * another page.
         *
         * writeLayout() calls the DeviceLayoutRenderer which calls this method.
         *
         */
        DeviceLayoutContext dlc = context.getDeviceLayoutContext();
        dom.addOutputBuffer((DOMOutputBuffer)dlc.getOutputBuffer(
            NativeMarkupAttributes.WML_CARD_ONEVENT, false));
        dom.addOutputBuffer((DOMOutputBuffer)dlc.getOutputBuffer(
            NativeMarkupAttributes.WML_CARD_TIMER, false));
        dom.addOutputBuffer((DOMOutputBuffer)dlc.getOutputBuffer(
            NativeMarkupAttributes.WML_CARD_BEFOREBODY, false));

        // NOTE: This handles opening the stylistic emulation markup for
        // both normal and inclusion canvases. We need to do it here to
        // respect the semantics for output defined by the native markup
        // support above, where the "normal" body appears after the special
        // buffers.
        openStyleMarker(dom, canvasAttributes);
    }

    // Javadoc inherited.
    protected void closeLayout(DOMOutputBuffer dom,
                               LayoutAttributes attributes) {

        // See note in open method for why this is here.
        closeStyleMarker(dom);

        super.closeLayout(dom, attributes);
    }

    /**
     * Writes a script element out to the dom buffer. Does no parsing and
     * assumes the <onevent> is correct markup
     */
    protected void addOnEventElement(DOMOutputBuffer dom,
                                     MCSAttributes attributes,
                                     String eventName, int eventIndex)
        throws ProtocolException {

        Script task = getTaskForEvent(attributes, eventName, eventIndex);
        addOnEventElement(dom, attributes, eventName, task);
    }

    /**
     * Writes a script element out to the dom buffer. Does no parsing and
     * assumes the <onevent> is correct markup
     */
    protected void addOnEventElement(DOMOutputBuffer dom,
                                     MCSAttributes attributes,
                                     String eventName, Script task)
        throws ProtocolException {
        // Only generate the onevent if we got the contents of the script.
        if (task != null) {
            Element element = dom.openStyledElement("onevent", attributes);
            element.setAttribute("type", eventName);

            // Append the script into the output buffer.
            task.appendTo(dom);

            dom.closeElement(element);
        }
    }

    /**
     * Get the task script from an event attribute.
     */
    protected Script getTaskForEvent(MCSAttributes attributes,
                                     String eventName, int eventIndex)
        throws ProtocolException {
        Script script = null;

        // Get the event attributes which have been specified if any.
        EventAttributes events = attributes.getEventAttributes(false);
        if (events == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No events found for " + eventName
                             + " at " + eventIndex);
            }
        } else {
            // Get the specific event which has been asked for.
            ScriptAssetReference scriptObject = events.getEvent(eventIndex);
            if (scriptObject == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("No event found for " + eventName
                                 + " at " + eventIndex);
                }
            } else {
                // Get the contents of the object.
                script = DOMScript.createScript(scriptObject);
                if (logger.isDebugEnabled()) {
                    logger.debug("Script object " + scriptObject
                                 + " is " + script);
                }
            }
        }

        // Return the script.
        return script;
    }

    // ------------------------------------------------------------------------
    //   Body Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    protected void openBody(DOMOutputBuffer dom,
                            BodyAttributes attributes)
        throws ProtocolException {

        openCard(dom, attributes.getCanvasAttributes());
    }

    // Javadoc inherited from super class.
    protected void closeBody(DOMOutputBuffer dom,
                             BodyAttributes attributes) {

        closeCard(dom, attributes.getCanvasAttributes());
    }

    // Javadoc inherited from super class.
    protected void openCanvas(DOMOutputBuffer dom,
                              CanvasAttributes attributes) {
        dom.openElement("wml");
    }

    // Javadoc inherited from super class.
    protected void closeCanvas(DOMOutputBuffer dom,
                               CanvasAttributes attributes) {
        dom.closeElement("wml");
    }

    /**
     * Open the head element. In WML a head element must contain other elements
     * to be valid.
     */
    protected void openHead(DOMOutputBuffer dom,
                            boolean empty) {
        if (!empty) {
            dom.openElement("head");
        }
    }

    protected void closeHead(DOMOutputBuffer dom,
                             boolean empty) {
        if (!empty) {
            dom.closeElement("head");
        }
    }

    /**
     * Override this method to prevent the initial header being added to the
     * page.
     */
    public void writeInitialHeader() {
    }

    protected void openInclusion(DOMOutputBuffer dom,
                                 CanvasAttributes attributes)
        throws ProtocolException {

        // If this inclusion is an overlay then it is a separate card.
        if (attributes.isOverlay ()) {
            if(logger.isDebugEnabled()){
                logger.debug ("Generating overlay inclusion in its own card");
            }
            // Get the current device layout context.
            DeviceLayoutContext deviceLayoutContext =
                    context.getDeviceLayoutContext();
            deviceLayoutContext.setOverlay(true);

            openCard(dom, attributes);
        }

        // NOTE: stylistic markup emulation handled in openLayout instead.
    }

    /**
     * Override this method to generate a card element if the nesting depth is
     * 1.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeInclusion(DOMOutputBuffer dom,
                                  CanvasAttributes attributes) {

        // NOTE: stylistic markup emulation handled in closeLayout instead.

        // If this inclusion is an overlay then it is a separate card.
        if (attributes.isOverlay()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Generating overlay inclusion in its own card");
            }

            closeCard(dom, attributes);
        }
    }

    /**
     * Override this to control where the markup for an inclusion will be
     * written.
     */
    public void beginNestedInclusion() {

        // Get the current device layout context.
        DeviceLayoutContext deviceLayoutContext
            = context.getDeviceLayoutContext();

        // If this inclusion is an overlay then it is a separate card.
        if (deviceLayoutContext.isOverlay()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Generating overlay inclusion in its own card");
            }


            DOMOutputBuffer dom;
            if (deviceLayoutContext.isInRegion()) {
                if (cardsInRegions == null) {
                    cardsInRegions = getExtraBuffer(CARDS_IN_REGIONS,
                                                    true);
                }
                dom = cardsInRegions;
            } else {
                if (cardsNotInRegions == null) {
                    cardsNotInRegions = getExtraBuffer(CARDS_NOT_IN_REGIONS,
                                                       true);
                }
                dom = cardsNotInRegions;
            }
            context.pushOutputBuffer(dom);
        }
    }

    /**
     * End a nested inclusion. This is called for each inclusion within a
     * region in the including page.
     */
    public void endNestedInclusion() {

        // Get the current device layout context.
        DeviceLayoutContext deviceLayoutContext
            = context.getDeviceLayoutContext();

        // If this inclusion is an overlay then it is a separate card.
        if (deviceLayoutContext.isOverlay()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Generating overlay inclusion in its own card");


                // If the card is not in a region then we have to add it into
                // a separate buffer

            }
            DOMOutputBuffer dom;
            if (deviceLayoutContext.isInRegion()) {
                dom = cardsInRegions;
            } else {
                dom = cardsNotInRegions;
            }
            context.popOutputBuffer(dom);
        }
    }

    /**
     * Override this method to add the extra cards to the end of the body.
     */
    protected void processBodyBuffer(DOMOutputBuffer buffer) {

        //debug ("Body buffer before processing", buffer);

        boolean removeFirstCard = false;
        cardsInRegions = getExtraBuffer(CARDS_IN_REGIONS, false);
        cardsNotInRegions = getExtraBuffer(CARDS_NOT_IN_REGIONS, false);

        if (cardsInRegions != null) {
            // Add the extra cards after the rest of the page, do this before
            // we try and remove the first card.
            buffer.addOutputBuffer(cardsInRegions);
            removeFirstCard = true;
        }

        if (cardsNotInRegions != null) {
            // Add the extra cards after the rest of the page, do this before
            // we try and remove the first card.
            buffer.addOutputBuffer(cardsNotInRegions);
            removeFirstCard = true;
        }

        if (removeFirstCard) {
            // The first card is essentially empty so remove it.
            Element root = buffer.getRoot();
            Node node = root.getHead();
            if (node != null && node instanceof Element) {
                Element card = (Element)node;
                if (logger.isDebugEnabled()) {
                    logger.debug("Removing card " + card);
                }
                if ("card".equals(card.getName())) {
                    card.remove();
                }
            }
        }

        //debug ("Body buffer after processing", buffer);
    }

    // ========================================================================
    //   Dissection methods
    // ========================================================================

    protected DOMTransformer getDOMTransformer() {
        return transformer;
    }

    // ========================================================================
    //   Layout / format methods
    // ========================================================================

    /**
     * Write a string which is used to mark up the start of a section of the
     * generated output which can be split over multiple pages. This marker is
     * recognised and removed from the output by the generateContentTree
     * method.
     *
     * @param dom        The DOMOuptutBuffer to write to.
     * @param attributes The attributes of the dissecting pane being opened.
     * @todo check to see if this should generate stylistic markup ala {@link
     * #openPane}.
     */
    protected
    void openDissectingPane(DOMOutputBuffer dom,
                            DissectingPaneAttributes attributes) {

        // Only generate dissection mark up if we are dissecting.
        if (!isDissecting()) {
            return;
        }

        // Dissection is needed because we are generating mark up for a
        // dissecting pane.
        setDissectionNeeded(true);

        Element element = dom.openElement(DISSECTABLE_CONTENTS_ELEMENT);
        // Set the attributes associated with the dissectable contents.
        DissectableAreaIdentity identity
            = new DissectableAreaIdentity(attributes.getInclusionPath(),
                                          attributes.getDissectingPane()
                                          .getName());
        DissectableAreaAttributes dissectableAttributes
            = new DissectableAreaAttributes();
        dissectableAttributes.setIdentity(identity);
        element.setAnnotation(dissectableAttributes);
    }

    /**
     * Set the attribute if it is not null and quote it if necessary.
     *
     * @param element the element to operate on
     * @param id      the identifier for the setAttribute method
     * @param value   the attribute value
     */
    protected void setAttribute(Element element, String id, String value) {
        if (value != null) {
            element.setAttribute(id, value);
        }
    }

    /**
     * Generate a string which is used to mark the end of the section of the
     * generated output which can be split over multiple pages. After the
     * dissecting pane is closed, a special block is output that contains the
     * shard links.
     */
    protected
    void closeDissectingPane(DOMOutputBuffer dom,
                             DissectingPaneAttributes attributes)
        throws ProtocolException {

        // Only generate dissection mark up if we are dissecting.
        if (!isDissecting()) {
            return;
        }

        dom.closeElement(DISSECTABLE_CONTENTS_ELEMENT);

        // Push the output buffer onto the context as the current.
        // This is required since the menu rendering will add stuff to the
        // current output buffer by default.
        context.pushOutputBuffer(dom);

        try {
            // Create a menu model for the shard link attributes.
            ShardLinkMenuModelBuilder builder =
                    new ShardLinkMenuModelBuilder(
                            getOutputBufferFactory());
            ShardLinkMenu menu = builder.buildShardLinkMenuModel(attributes);

            // Render the shard link menu.
            MenuRenderer menuRenderer = getShardLinkMenuModule().
                getMenuRendererSelector().selectMenuRenderer(menu);
            menuRenderer.render(menu);
        } catch (RendererException e) {
            throw new ProtocolException(e.getMessage(), e);
        }

        context.popOutputBuffer(dom);
/*
        todo: delete when we clean up all the old menu code.

        // Create menu attributes based on the shard links.
        MenuAttributes menuAttributes = createMenuAttributes(attributes);
        MenuOrientation orientation = menuOrientation(menuAttributes);

        // Write out the special shard link group element.
        openShardLinkGroup(dom, attributes);

        // Surround the shard links with a paragraph
        Element p = dom.openElement("p");
        p.setAttribute("mode", "wrap");

        // Write out the first shard link
        boolean isNextLinkFirst = attributes.isNextLinkFirst();
        openShardLinkSpecial(dom, isNextLinkFirst);
        Iterator menuItems = menuAttributes.getItems().iterator();
        boolean horizontalSeparatorRequired
                = doMenuItem(dom, menuAttributes, (MenuItem)menuItems.next());
        closeShardLinkSpecial(dom);

        // Add the shard link conditional special element that goes around the
        // markup separating the shard links
        openShardLinkConditional(dom, StandardContentRules.getSeparatorRule());
        if (orientation.equals(MenuOrientation.VERTICAL)) {
            addVerticalMenuItemSeparator(dom);
        } else if ( horizontalSeparatorRequired ) {
            Style st = getStyle( null, menuAttributes );
            if( st == null ) {
                addHorizontalMenuItemSeparator(dom);
            } else {
                String sep = st.getMarinerMenuHorizontalSeparator();
                if( sep == null ) {
                    addHorizontalMenuItemSeparator(dom);
                } else {
                    addHorizontalMenuItemSeparator(dom, sep);
                }
            }
        }
        closeShardLinkConditional(dom);

        // Write out the second shard link
        openShardLinkSpecial(dom, !isNextLinkFirst);
        doMenuItem(dom, menuAttributes, (MenuItem)menuItems.next());
        closeShardLinkSpecial(dom);

        // Close the remaining elements
        dom.closeElement("p");
        closeShardLinkGroup(dom);
*/
    }

    /**
     * Open the special shard link element
     *
     * @param dom        The DOMOutputBuffer
     * @param isNextLink This boolean is true, if the shard link to be written
     *                   out is the next link and false if we want the previous
     *                   link.
     * todo: delete when we clean up all the old menu code.
     */
//    private void openShardLinkSpecial(DOMOutputBuffer dom, boolean isNextLink) {
//        Element shardLinkFirst = dom.openElement(SHARD_LINK_ELEMENT);
//        ShardLinkAttributes attrs = new ShardLinkAttributes();
//        if (isNextLink) {
//            attrs.setAction(ShardLinkAction.NEXT);
//        } else {
//            attrs.setAction(ShardLinkAction.PREVIOUS);
//        }
//        shardLinkFirst.setAnnotation(attrs);
//    }

    /**
     * Closes the special shard link element
     *
     * @param dom The DOMOutputBuffer
     * todo: delete when we clean up all the old menu code.
     */
//    private void closeShardLinkSpecial(DOMOutputBuffer dom) {
//        dom.closeElement(SHARD_LINK_ELEMENT);
//    }

    /**
     * Open the special element shard link conditional
     *
     * @param dom  The DOMOutputBuffer
     * @param rule The ShardLinkContentRule
     * todo: delete when we clean up all the old menu code.
     */
//    private void openShardLinkConditional(
//            DOMOutputBuffer dom,
//            ShardLinkContentRule rule) {
//
//        Element shardLinkConditional
//                = dom.openElement(SHARD_LINK_CONDITIONAL_ELEMENT);
//
//        // Create a ShardLinkConditionalAttributes object containing the rule
//        // and set it on the element
//        ShardLinkConditionalAttributes slcAttributes
//                = new ShardLinkConditionalAttributes();
//        slcAttributes.setContentRule(rule);
//        shardLinkConditional.setAnnotation(slcAttributes);
//    }

    /**
     * Closes the special shard link conditional element
     *
     * @param dom DOMOutputBuffer
     * todo: delete when we clean up all the old menu code.
     */
//    private void closeShardLinkConditional(DOMOutputBuffer dom) {
//        dom.closeElement(SHARD_LINK_CONDITIONAL_ELEMENT);
//    }

    /**
     * Open the special element shard link group
     *
     * @param dom        DOMOutputBuffer
     * @param attributes DissectingPaneAttributes
     * todo: delete when we clean up all the old menu code.
     */
//    private void openShardLinkGroup(
//            DOMOutputBuffer dom,
//            DissectingPaneAttributes attributes) {
//
//        Element shardLinkGroup =
//                dom.openElement(SHARD_LINK_GROUP_ELEMENT);
//
//        // Create a ShardLinkGroupAttributes object containing the attributes
//        // associated with this shard link group and set it on the element.
//        DissectingPane pane = attributes.getDissectingPane();
//        DissectableAreaIdentity identity
//                = new DissectableAreaIdentity(attributes.getInclusionPath(),
//                                              pane.getName());
//        ShardLinkGroupAttributes slgAttributes = new ShardLinkGroupAttributes();
//        slgAttributes.setDissectableArea(identity);
//
//        shardLinkGroup.setAnnotation(slgAttributes);
//    }

    /**
     * Closes the special shard link group element
     *
     * @param dom DOMOutputBuffer
     * todo: delete when we clean up all the old menu code.
     */
//    private void closeShardLinkGroup(DOMOutputBuffer dom) {
//        dom.closeElement(SHARD_LINK_GROUP_ELEMENT);
//    }

    /**
     * Creates attributes used to represent the menu of shard links.
     *
     * @param attributes The DissectingPaneAttributes
     * @return MenuAttributes
     * todo: delete when we clean up all the old menu code.
     */
//    private MenuAttributes createMenuAttributes(
//            DissectingPaneAttributes attributes) {
//
//        DissectingPane pane = attributes.getDissectingPane();
//
//        MenuItem nextLink = new MenuItem();
//        nextLink.setHref(DissectionConstants.URL_MAGIC_CHAR);
//        nextLink.setShortcut(pane.getNextShardShortcut());
//        nextLink.setStyleClass(pane.getNextShardLinkClass());
//        nextLink.setText(pane.getNextShardLinkText());
//
//        MenuItem previousLink = new MenuItem();
//        previousLink.setHref(DissectionConstants.URL_MAGIC_CHAR);
//        previousLink.setShortcut(pane.getPreviousShardShortcut());
//        previousLink.setStyleClass(pane.getPreviousShardLinkClass());
//        previousLink.setText(pane.getPreviousShardLinkText());
//
//        MenuAttributes menuAttributes = new MenuAttributes();
//        menuAttributes.setTagName(null);
//        menuAttributes.setStyleClass(attributes.getStyleClass());
//
//        if (attributes.isNextLinkFirst()) {
//            menuAttributes.addItem(nextLink);
//            menuAttributes.addItem(previousLink);
//        } else {
//            menuAttributes.addItem(previousLink);
//            menuAttributes.addItem(nextLink);
//        }
//
//
//        return menuAttributes;
//    }

    /**
     * Normally we don't generate any markup explicitly for WML panes. We rely
     * on {@link WMLDOMTransformer} to add in P's where necessary, or rely on
     * writing into grid TD tags.
     * <p/>
     * However, we define a special case when writing panes inside a single
     * column grid, where we emulate the grid using P tags. In this scenario,
     * {@link #openPane} will override any horizontal alignment added to the
     * generated P by {@link #openGrid}.
     *
     * @param dom        the output buffer to add the markup to.
     * @param attributes PaneAttributes used to create this tag.
     */
    protected void openPane(DOMOutputBuffer dom, PaneAttributes attributes) {

        // If we are inside a single column grid row (emulated using P's),
        if (gridRowElement != null) {
            // then set the alignment on the P element which the grid row and
            // pane "share" to be that of the pane format rather than the grid
            // format.

            // Remove any alignment that the grid row added in.
            gridRowElement.removeAttribute("align");
        }

        // Add a stylistic emulation markup element for the pane.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited.
    protected void closePane(DOMOutputBuffer dom, PaneAttributes attributes) {

        // Finish any stylistic emulation markup for the pane.
        closeStyleMarker(dom);
    }

    /**
     * Generate the markup that will open a grid. In this case a grid is
     * defined using the <code>table</code> tag. However, WML is a special
     * case. Nested tables are not allowed. If the layout contains nested grids
     * then the parent wrappers of such grids are removed leaving only the
     * child grids defined as independent tables.
     *
     * @param attributes GridAttributes used to create this tag.
     */
    protected void openGrid(DOMOutputBuffer dom,
                            GridAttributes attributes) {

        // If we have multiple columns...
        if (attributes.getColumns() >= 2) {
            Element element = dom.openStyledElement("table", attributes,
                    DisplayKeywords.TABLE);

            // The format may be a SpatialFormatIterator.
            Format format = attributes.getFormat();
            if (format instanceof Grid) {

                // NB: We do not need to set the column attribute because it
                // will be resolved and added in the associated transformer

                Grid grid = (Grid) format;

                StringBuffer alignValues = new StringBuffer();
                int numCols = grid.getColumns();
                for (int i = 0; i < numCols; i++) {
                    // All the children must be a Panes since the assoicated
                    // format has no grid children
                    Format childPane = grid.getChildAt(i);

                    // If the layout is incomplete then the childPane could be
                    // null
                    if (childPane != null) {
                        String hAlign = childPane.getHorizontalAlignment();

                        if (FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_CENTER
                                .equals(
                                        hAlign)) {
                            alignValues.append('C');
                        } else {
                            if (FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_RIGHT
                                    .equals(
                                            hAlign)) {
                                alignValues.append('R');
                            } else {
                                alignValues.append('L'); // default to left
                            }
                        }
                    }
                }

                element.setAttribute("align", alignValues.toString());
            }
        }

        openStyleMarker(dom, attributes);

    }

    /**
     * Generate the markup that will close a grid. In this case a grid is
     * defined using the <code>table</code> tag. However, WMLWideScreen is a
     * special case. Nested tables are not allowed. If the layout contains
     * nested grids then the parent wrappers of such grids are removed leaving
     * only the child grids defined as independent tables.
     *
     * @param attributes GridAttributes used to create this tag.
     */
    protected void closeGrid(DOMOutputBuffer dom,
                             GridAttributes attributes) {

        closeStyleMarker(dom);

        // If we have multiple columns...
        if (attributes.getColumns() >= 2) {
            dom.closeElement("table");
        }

    }

    /**
     * Generate the markup that will open a grid row. Normally this is a TR
     * element, but there is also the special case of a single column grid
     * which we emulate with the P element.
     * <p/>
     * In the single column grid scenario, we must save a reference to the P
     * element generated, because {@link #openPane} and {@link #openGridRow}
     * "share" this P tag as a host for horizontal alignment setting, and
     * {@link #openPane} may need to override the setting we put in.
     *
     * @param dom        the output buffer to add the markup to.
     * @param attributes GridRowAttributes used to create this tag.
     */
    protected void openGridRow(DOMOutputBuffer dom,
                               GridRowAttributes attributes) {

        // If we have multiple columns...
        // TODO: is this calculation correct for grids rendered from spatials?
        // particularly ones with non fixed columns?
        // I suppose the fix is to make the columns a settable property of the
        // attributes rather than have it go direct to the format...
        if (attributes.getColumns() >= 2) {
            // then we create the grid as normal using a table.
            dom.openStyledElement("tr", attributes, DisplayKeywords.TABLE_ROW);
        } else {
            // else we must emulate a single column grid using Ps.
            // So, we create a P, with the alignment defined in the grid,
            // and save a reference so we can adjust it later if necessary.
            gridRowElement = dom.openStyledElement(WMLConstants.BLOCH_ELEMENT,
                    attributes);
        }

    }

    /**
     * Generate the markup that will close a grid row. Normally this is a /TR
     * element, but there is also the special case of a single column grid
     * which we emulate with the /P element.
     * <p/>
     * In the single column grid scenario, we must clear the reference to the P
     * element generated, to prevent it being picked up accidentally by
     * subsequent invocations of {@link #openPane}.
     *
     * @param dom        the output buffer to add the markup to.
     * @param attributes GridRowAttributes used to create this tag.
     */
    protected void closeGridRow(DOMOutputBuffer dom,
                                GridRowAttributes attributes) {

        // If we have multiple columns...
        if (attributes.getColumns() >= 2) {
            // then close off the normal table based rendering of grids.
            dom.closeElement("tr");
        } else {
            // else close off the P we used to emulate the single column grid.
            dom.closeElement(WMLConstants.BLOCH_ELEMENT);
            gridRowElement = null;
        }
    }

    /**
     * Generate the markup that will open a grid child. In this case a grid
     * child is defined using the <code>td</code> tag. However, WMLWideScreen
     * is a special case. Nested tables are not allowed. If the layout contains
     * nested grids then the parent wrappers of such grids are removed leaving
     * only the child grids defined as independent tables.
     *
     * @param attributes GridChildAttributes used to create this tag.
     */
    protected void openGridChild(DOMOutputBuffer dom,
                                 GridChildAttributes attributes) {

        // If we have multiple columns...
        if (attributes.getColumns() >= 2) {
            // then we create the grid as normal using a table.
            dom.openStyledElement("td", attributes,
                    DisplayKeywords.TABLE_CELL);
        } else {
            openStyleMarker(dom, attributes);
        }

    }

    /**
     * Generate the markup that will close a grid child in this case a grid
     * child is defined using the <code>td</code> tag. However, WMLWideScreen
     * is a special case. Nested tables are not allowed. If the layout contains
     * nested grids then the parent wrappers of such grids are removed leaving
     * only the child grids defined as independent tables.
     *
     * @param attributes GridChildAttributes used to create this tag.
     */
    protected void closeGridChild(DOMOutputBuffer dom,
                                  GridChildAttributes attributes) {

        // If we have multiple columns...
        if (attributes.getColumns() >= 2) {
            // then close off the normal table based rendering of grids.
            dom.closeElement("td");
        } else {
            closeStyleMarker(dom);
        }
    }

    // Javadoc inherited from super class.
    protected void openColumnIteratorPaneElement(DOMOutputBuffer dom,
                                                 ColumnIteratorPaneAttributes attributes) {

        // Start any stylistic emulation markup for a single instance of a
        // column iterated pane. Note that the styling is bust at the moment,
        // all panes have the last styleClass value.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeColumnIteratorPaneElement(DOMOutputBuffer dom,
                                                  ColumnIteratorPaneAttributes attributes) {

        // Finish any stylistic emulation markup for a single instance of a
        // column iterated pane.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openRowIteratorPane(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {

        // No stylistic information applies to the entire row (I think).
    }

    // Javadoc inherited from super class.
    protected void closeRowIteratorPane(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {

        // No stylistic information applies to the entire row (I think).
    }

    // Javadoc inherited from super class.
    protected void openRowIteratorPaneElement(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {

        // Start any stylistic emulation markup for a single instance of a
        // row iterated pane. Note that the styling is bust at the moment,
        // all panes have the last styleClass value.
        openStyleMarker(dom, attributes);
    }

    /**
     * Generate the markup that separates different elements in a
     * RowIteratorPane.
     *
     * @param attributes RowIteratorPaneAttributes is used to create this tag.
     */
    protected void closeRowIteratorPaneElement(DOMOutputBuffer dom,
                                               RowIteratorPaneAttributes attributes) {

        // Finish any stylistic emulation markup for a single instance of a
        // row iterated pane.
        closeStyleMarker(dom);

        addVerticalMenuItemSeparator(dom);
    }

    // ========================================================================
    //   Dialling methods.
    // ========================================================================

    public void writeOpenPhoneNumber(PhoneNumberAttributes attributes) {
        super.writeOpenPhoneNumber(attributes);

        // Make sure that other types of markup are not output in this
        // body area
        insideAnchorBody = true;
    }

    public void writeClosePhoneNumber(PhoneNumberAttributes attributes)
        throws ProtocolException {
        super.writeClosePhoneNumber(attributes);

        // Re-enable other types of markup
        insideAnchorBody = false;
    }

    /**
     * Unlike anchors, phone numbers are always rendered as anchor "a" tags.
     * This method's processing must be matched by {@link #closePhoneNumber}.
     */
    protected void openPhoneNumber(DOMOutputBuffer dom,
                                   PhoneNumberAttributes attributes)
        throws ProtocolException {

        // Always render using the anchor "a" tag, setting the appropriate
        // attributes
        Element element = dom.openStyledElement("a", attributes);

        setAttribute(element, "href",
                     attributes.getQualifiedFullNumber());

        addTitleAttribute(element, attributes, true);
        addDoAttributes(element, attributes);

        if (supportsAccessKeyAttribute) {
            String value = getPlainText(attributes.getShortcut());
            setAttribute(element, "accesskey", value);
        }

        addPhoneNumberAttributes(element, attributes);

        // NOTE: cannot add stylistic emulation markup inside an anchor.
    }

    protected void closePhoneNumber(DOMOutputBuffer dom,
                                    PhoneNumberAttributes attributes) {

        // NOTE: cannot add stylistic emulation markup inside an anchor.

        // Close the anchor "a" tag opened in {@link #openPhoneNumber}
        dom.closeElement("a");
    }

    /**
     * Provided to allow specializations to add their own extra attributes to
     * the phone number element.
     *
     * @param element    the element representing the phone number link
     * @param attributes the attributes supplied
     * @throws ProtocolException if a problem is encountered
     */
    private void addPhoneNumberAttributes(Element element,
                                            PhoneNumberAttributes attributes)
        throws ProtocolException {
    }

    // ========================================================================
    //   Navigation methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void enteredAnchorBody() {
        insideAnchorBody = true;
    }

    // Javadoc inherited from super class.
    protected void exitedAnchorBody() {
        insideAnchorBody = false;
    }

    // ========================================================================
    // Used by DeprecatedAnchorOutput - caused change from protected to public
    // ========================================================================
    // JavaDoc inherited
    public void openAnchor(DOMOutputBuffer dom, AnchorAttributes attributes)
        throws ProtocolException {

        String href = getLinkFromReference(attributes.getHref());

        boolean post = false;

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue;

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_HTTP_METHOD_HINT);
        if (styleValue == MCSHttpMethodHintKeywords.POST) {
            post = true;

            if (href != null) {
                MarinerURL url = new MarinerURL(href);
                Map params = url.getParameterMap();
                if (params == null || params.isEmpty()) {
                    post = false;
                }
            } else {
                post = false;
            }
        }

        Element element = null;
        if (post) {
            element = dom.openStyledElement("anchor", attributes);
        } else {
            element = dom.openStyledElement("a", attributes);
            setAttribute(element, "href", getLinkFromReference(attributes.getHref()));
        }
        addTitleAttribute(element, attributes, true);
        addDoAttributes(element, attributes);

        String value;
        if (supportsAccessKeyAttribute) {
            value = getPlainText(attributes.getShortcut());
            setAttribute(element, "accesskey", value);
        }

        // NOTE: cannot add stylistic emulation markup inside an anchor.
    }

    // ========================================================================
    // Used by DeprecatedAnchorOutput - caused change from protected to public
    // ========================================================================
    // JavaDoc inherited
    public void closeAnchor(DOMOutputBuffer dom, AnchorAttributes attributes) {

        // NOTE: cannot add stylistic emulation markup inside an anchor.

        String href = getLinkFromReference(attributes.getHref());
        MarinerURL url = null;
        Map params = null;

        boolean post = false;

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue;

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_HTTP_METHOD_HINT);
        if (styleValue == MCSHttpMethodHintKeywords.POST) {
            post = true;

            if (href != null) {
                url = new MarinerURL(href);
                params = url.getParameterMap();
                if (params == null || params.isEmpty()) {
                    post = false;
                }
            } else {
                post = false;
            }
        }

        if (post) {
            // @todo 2005060816 annotate child with style information if it's not inherited from the parent
            Element goElement = dom.openStyledElement("go", attributes);
            int index = href.lastIndexOf('?');
            if (index != -1) {
                href = href.substring(0, index);
            }
            setAttribute(goElement, "href", href);
            goElement.setAttribute("method", "post");

            Set keys = params.keySet();
            Iterator i = keys.iterator();
            while (i.hasNext()) {
                String keyStr = (String)i.next();
                String[] valueStr = (String[])params.get(keyStr);
                if (keyStr != null && valueStr != null) {
                    for (int j = 0; j < valueStr.length; j++) {
                        Element postElement = dom.openElement("postfield");
                        postElement.setAttribute("name", keyStr);
                        postElement.setAttribute("value", valueStr[j]);
                        dom.closeElement("postfield");
                    }
                }
            }
            dom.closeElement("go");

            dom.closeElement("anchor");
        } else {
            dom.closeElement("a");
        }
    }

    /**
     * Generate the markup for the default segment link.
     *
     * @param dom        The DOMOutputBuffer to add the markup to.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doDefaultSegmentLink(DOMOutputBuffer dom,
                                        AnchorAttributes attributes)
        throws ProtocolException {

        attributes.setContent("Back");

        doAnchor(dom, attributes);
    }

    // ========================================================================
    //   Block element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void openAddress(DOMOutputBuffer dom,
                               AddressAttributes attributes) {

        // Start any stylistic emulation markup for the address.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeAddress(DOMOutputBuffer dom,
                                AddressAttributes attributes) {

        // Finish any stylistic emulation markup for the address.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openBlockQuote(DOMOutputBuffer dom,
                                  BlockQuoteAttributes attributes) {

        // Start any stylistic emulation markup for the blockquote.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeBlockQuote(DOMOutputBuffer dom,
                                   BlockQuoteAttributes attributes) {

        // Finish any stylistic emulation markup for the blockquote.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    public void openDiv (DOMOutputBuffer dom,
                            DivAttributes attributes)
            throws ProtocolException {

        if ("true".equals(attributes.getKeepTogether())) {
          dom.openElement(KEEPTOGETHER_ELEMENT);
        }
        // Avoid creating invalid markup - anchor cannot contain paragraphs.
        if (insideAnchorBody) {
            return;
        }

        dom.openStyledElement(WMLConstants.BLOCH_ELEMENT, attributes);

    }

    // Javadoc inherited from super class.
    public void closeDiv (DOMOutputBuffer dom,
                             DivAttributes attributes) {
        // Avoid creating invalid markup - anchor cannot contain paragraphs.
        if (!insideAnchorBody) {
            dom.closeElement(WMLConstants.BLOCH_ELEMENT);
        }
        
        if ("true".equals(attributes.getKeepTogether())) {
          dom.closeElement(KEEPTOGETHER_ELEMENT);
        }
    }

    /**
     * Just store the information object so it can be written out when the page
     * buffers are written.
     *
     * @param tri the timed refresh information or null.
     */
    public void writeTimedRefresh(TimedRefreshInfo tri) {
        if (DevicePolicyConstants.FULL_SUPPORT_POLICY_VALUE.equals(
            context.getDevicePolicyValue(
            DevicePolicyConstants.X_ELEMENT_SUPPORTS_WAP_TIMER))) {
            this.timedRefreshInfo = tri;
        }
    }

    /**
     * Overridden to allow insertion of the onevent and timer elements
     * necessary for timed refresh
     * @param deviceLayoutContext
     * @throws IOException
     * @throws ProtocolException
     */
    public void writeLayout(DeviceLayoutContext deviceLayoutContext)
        throws IOException, ProtocolException {

        if (null != timedRefreshInfo) {
            DOMOutputBuffer oneventBuffer = (DOMOutputBuffer)
                deviceLayoutContext.getOutputBuffer(
                    NativeMarkupAttributes.WML_CARD_ONEVENT, true);
            Element onEventElement = oneventBuffer.openElement("onevent");
            onEventElement.setAttribute("type", "ontimer");
            Element goElement = oneventBuffer.addElement("go");
            
            String url = timedRefreshInfo.getRefreshURL();
            if (null == url) {
                // if no url was given then use the current request url
                url = context.getRelativeRequestURL();
            }

            goElement.setAttribute("href", url);
            oneventBuffer.closeElement("onevent");

            DOMOutputBuffer timerBuffer = (DOMOutputBuffer)
                deviceLayoutContext.getOutputBuffer(
                    NativeMarkupAttributes.WML_CARD_TIMER, true);
            Element timerElement = timerBuffer.addElement("timer");
            timerElement.setAttribute(
                "value", Integer.toString(
                    timedRefreshInfo.getIntervalInTenthsOfSecond()));
        }
        // carry on writing the layout
        super.writeLayout(deviceLayoutContext);
    }

    // Javadoc inherited from super class.
    protected void openHeading1(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {

        // Open a heading with h1 default style.
        openHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeHeading1(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {

        // Close a heading with h1 default style.
        closeHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void openHeading2(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {

        // Open a heading with h2 default style.
        openHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeHeading2(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {

        // Close a heading with h2 default style.
        closeHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void openHeading3(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {

        // Open a heading with h3 default style.
        openHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeHeading3(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {

        // Close a heading with h3 default style.
        closeHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void openHeading4(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {

        // Open a heading with h4 default style.
        openHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeHeading4(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {

        // Close a heading with h4 default style.
        closeHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void openHeading5(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {

        // Open a heading with h5 default style.
        openHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeHeading5(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {

        // Close a heading with h5 default style.
        closeHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void openHeading6(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {

        // Open a heading with h6 default style.
        openHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeHeading6(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {

        // Close a heading with h6 default style.
        closeHeading(dom, attributes);
    }

    /**
     * Open a h1 - h6 heading, using the default style provided.
     *
     * @param dom        the output buffer to write to
     * @param attributes the attributes of the heading to write.
     */
    protected void openHeading(DOMOutputBuffer dom,
                               HeadingAttributes attributes) {

        // Ensure that the heading starts on a new line.
        dom.addStyledElement("br", attributes);

        // Start any stylistic emulation markup for the heading.
        openStyleMarker(dom, attributes);

    }

    /**
     * Close a h1 - h6 heading, using the default style provided.
     *
     * @param dom        the output buffer to write to
     * @param attributes the attributes of the heading to write.
     */
    protected void closeHeading(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {

        // Finish any stylistic emulation markup for the heading.
        closeStyleMarker(dom);

        // Ensure that the text following the heading starts on a new line.
        dom.addStyledElement("br", attributes);
    }

    /**
     * Will generate a horizontal ruler if emulateHorizontalTag is set to be
     * true.
     */
    protected void doHorizontalRule(DOMOutputBuffer dom,
                                    HorizontalRuleAttributes attributes) {

        // Avoid creating invalid markup - anchor cannot contain emulated hr.
        if (insideAnchorBody) {
            return;
        }

        String value;
        int length = -1;

        if (emulateHorizontalTag) {
            try {
                value = context.getDevicePolicyValue("charactersx");
                length = Integer.parseInt(value) - 2;
            } catch (NumberFormatException ie) {
                logger.error("unexpected-exception", ie);
            }

            if (length > 0) {
                Styles styles = attributes.getStyles();
                dom.openStyledElement(WMLConstants.BLOCH_ELEMENT, styles);

                for (int i = 0; i < length; i++) {
                    dom.appendEncoded("-");
                }

                dom.closeElement(WMLConstants.BLOCH_ELEMENT);
            }
        }
    }

    // Javadoc inherited from super class.
    protected void openParagraph(DOMOutputBuffer dom,
                                 ParagraphAttributes attributes) {

        // Avoid creating invalid markup - anchor cannot contain paragraphs.
        if (insideAnchorBody) {
            return;
        }

        // NOTE: to be completely correct we should probably leave explicit
        // <p> in the markup as it is semantically different to <BLOCH> but
        // for now it makes the block -> p | br translation more difficult to
        // I have just made it BLOCH for simplicity.

        dom.openStyledElement(WMLConstants.BLOCH_ELEMENT, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeParagraph(DOMOutputBuffer dom,
                                  ParagraphAttributes attributes) {

        // Avoid creating invalid markup - anchor cannot contain paragraphs.
        if (insideAnchorBody) {
            return;
        }

        dom.closeElement(WMLConstants.BLOCH_ELEMENT);
    }

    // JavaDoc inherited
    public void openSpan(DOMOutputBuffer dom, SpanAttributes attributes)
        throws ProtocolException {

        // Start any stylistic emulation markup for the span.
        openStyleMarker(dom, attributes);
    }

    // JavaDoc inherited
    public void closeSpan(DOMOutputBuffer dom, SpanAttributes attributes) {

        // Finish any stylistic emulation markup for the span.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openPre(DOMOutputBuffer dom, PreAttributes attributes) {

        // Avoid creating invalid markup - anchor cannot contain style tags.
        if (insideAnchorBody) {
            return;
        }

        dom.openStyledElement("em", attributes);
    }

    // Javadoc inherited from super class.
    protected void closePre(DOMOutputBuffer dom, PreAttributes attributes) {

        // Avoid creating invalid markup - anchor cannot contain style tags.
        if (insideAnchorBody) {
            return;
        }

        dom.closeElement("em");
    }

    // ========================================================================
    //   List element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected
    void openDefinitionData(DOMOutputBuffer dom,
                            DefinitionDataAttributes attributes) {

        // Start any stylistic emulation markup for the dd.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected
    void closeDefinitionData(DOMOutputBuffer dom,
                             DefinitionDataAttributes attributes) {

        // Finish any stylistic emulation markup for the dd.
        closeStyleMarker(dom);

        dom.addStyledElement("br", attributes);
    }

    // Javadoc inherited from super class.
    protected
    void openDefinitionList(DOMOutputBuffer dom,
                            DefinitionListAttributes attributes) {

        dom.openStyledElement(WMLConstants.BLOCH_ELEMENT, attributes);

    }

    // Javadoc inherited from super class.
    protected
    void closeDefinitionList(DOMOutputBuffer dom,
                             DefinitionListAttributes attributes) {

        dom.closeElement(WMLConstants.BLOCH_ELEMENT);
    }

    // Javadoc inherited from super class.
    protected
    void openDefinitionTerm(DOMOutputBuffer dom,
                            DefinitionTermAttributes attributes) {

        // Start any stylistic emulation markup for the dt.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected
    void closeDefinitionTerm(DOMOutputBuffer dom,
                             DefinitionTermAttributes attributes) {

        // Finish any stylistic emulation markup for the dt.
        closeStyleMarker(dom);

        dom.addStyledElement("br", attributes);
    }

    // Javadoc inherited from super class.
    protected void openListItem(DOMOutputBuffer dom,
                                ListItemAttributes attributes) {
        dom.addStyledElement("br", attributes);

    }

    // Javadoc inherited from super class.
    protected void closeListItem(DOMOutputBuffer dom,
                                 ListItemAttributes attributes) {

    }

    // Javadoc inherited from super class.
    protected void openOrderedList(DOMOutputBuffer dom,
                                   OrderedListAttributes attributes)
        throws ProtocolException {

        // Start any stylistic emulation markup for the ol.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeOrderedList(DOMOutputBuffer dom,
                                    OrderedListAttributes attributes) {

        // Finish any stylistic emulation markup for the ol.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected
    void openUnorderedList(DOMOutputBuffer dom,
                           UnorderedListAttributes attributes) {

        // Start any stylistic emulation markup for the ul.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected
    void closeUnorderedList(DOMOutputBuffer dom,
                            UnorderedListAttributes attributes) {

        // Finish any stylistic emulation markup for the ul.
        closeStyleMarker(dom);
    }

    // ========================================================================
    //   Table element methods.
    // ========================================================================

    /**
     * Add some extra attributes to the table.
     * <p/>
     * Subclasses should override this if they need to add any extra attributes
     * to the table.
     * <p/>
     */
    protected void addTableAttributes(Element element,
                                      TableAttributes attributes) {
    }


    protected void addTableRowAttributes(Element element,
                                         TableRowAttributes attributes) {
    }


    // Javadoc inherited from super class.
    protected void openTable(DOMOutputBuffer dom,
                             TableAttributes attributes) {

        Element element = dom.openStyledElement("table", attributes,
                DisplayKeywords.TABLE);
        // Initialise a TableState object to hold alignment information.
        element.setObject(new TableState(context));

        // NB: We do not need to set the column attribute because it will
        // be resolved and added in the associated transformer

        // Allow subclasses to add extra attributes to table.
        addTableAttributes(element, attributes);

    }

    // Javadoc inherited from super class.
    protected void closeTable(DOMOutputBuffer dom,
                              TableAttributes attributes) {

        Element tableElement = dom.closeElement("table");

        // Add any alignment values to the table attributes
        TableState ts = (TableState)tableElement.getObject();
        ReusableStringBuffer alignBuffer = ts.getBuffer();
        if (alignBuffer.length() > 0) {
            tableElement.setAttribute("align", alignBuffer.toString());
        }
        ts.release();
        tableElement.setObject(null);

        // If the table contains a caption (which will be a BLOCH element as the
        // first child of the table) move the caption either above or below
        // the table depending on its caption-side style property.
        Node firstNodeChild = tableElement.getHead();
        if (firstNodeChild instanceof Element) {
            Element firstChild = (Element) firstNodeChild;
            if (firstChild != null && WMLConstants.BLOCH_ELEMENT.equals(
                    firstChild.getName())) {
                StyleValue captionSide = firstChild.getStyles().
                        getPropertyValues().getComputedValue(
                                StylePropertyDetails.CAPTION_SIDE);

                // This explicit remove is required to work around a bug in the
                // DOM implementation - if insertBefore/insertAfter are called
                // without first removing the node to be moved, they can leave
                // the DOM in an inconsistent state where the block element
                // exists in two places (leading to an infinite loop when
                // iterating over the DOM).
                firstChild.remove();

                if (captionSide == CaptionSideKeywords.TOP) {
                    // Move the caption before the table element
                    firstChild.insertBefore(tableElement);
                } else {
                    // Move the caption after the table element
                    firstChild.insertAfter(tableElement);
                }
            }
        }
    }

    /**
     * Add protocol specific table data cell attributes.
     *
     * @param element    The <code>Element</code> to add protocol specific
     *                   table cell attributes to
     * @param attributes the <code>TableCellAttributes</code>
     */
    protected void addTableCellAttributes(Element element,
                                          TableCellAttributes attributes) {
    }

    protected void addTableCellEvents(DOMOutputBuffer dom,
                                      TableCellAttributes attributes)
        throws ProtocolException {
    }


    // Javadoc inherited from super class.
    protected void openTableDataCell(DOMOutputBuffer dom,
                                     TableCellAttributes attributes)
        throws ProtocolException {

        Element element = dom.openStyledElement("td", attributes,
                DisplayKeywords.TABLE_CELL);

        // Find the table object so that we can get the
        // TableState from it.
        Element parent = element.getParent();
        while (parent != null && !"table".equals(parent.getName())) {
            parent = parent.getParent();
        }

        if (parent == null) {
            throw new IllegalStateException("td does not have a parent table");
        }

        TableState ts = (TableState)parent.getObject();

        // Only do the align code for the first row.
        // Allow for the row to be zero as there may not have been a tr
        if (ts.getRow() <= 1) {
            Styles styles = attributes.getStyles();
            PropertyValues propertyValues = styles.getPropertyValues();
            StyleValue styleValue = propertyValues.getComputedValue(
                    StylePropertyDetails.TEXT_ALIGN);

            ReusableStringBuffer tableAlign = ts.getBuffer();
            if (styleValue == TextAlignKeywords.RIGHT) {
                tableAlign.append('R');
            } else if (styleValue == TextAlignKeywords.CENTER) {
                tableAlign.append('C');
            } else {
                tableAlign.append('L');
            }
        }

        addTitleAttribute(element, attributes, supportsTitleOnTD);
        addTableCellAttributes(element, attributes);

        addTableCellEvents(dom, attributes);

    }

    // Javadoc inherited from super class.
    protected
    void closeTableDataCell(DOMOutputBuffer dom,
                            TableCellAttributes attributes) {

        dom.closeElement("td");
    }

    // Javadoc inherited from super class.
    protected
    void openTableHeaderCell(DOMOutputBuffer dom,
                             TableCellAttributes attributes) {

        dom.openStyledElement("td", attributes,
                DisplayKeywords.TABLE_CELL);

    }

    // Javadoc inherited from super class.
    protected
    void closeTableHeaderCell(DOMOutputBuffer dom,
                              TableCellAttributes attributes) {

        dom.closeElement("td");
    }

    // Javadoc inherited from super class.
    protected void openTableRow(DOMOutputBuffer dom,
                                TableRowAttributes attributes) {

        Element element = dom.openStyledElement("tr", attributes,
                DisplayKeywords.TABLE_ROW);

        Element parent = element.getParent();
        while (parent != null && !"table".equals(parent.getName())) {
            parent = parent.getParent();
        }

        if (parent == null) {
            throw new IllegalStateException("tr does not have a parent table");
        }

        TableState ts = (TableState)parent.getObject();
        ts.incrementRow();
        addTitleAttribute(element, attributes, supportsTitleOnTR);
        addTableRowAttributes(element, attributes);

    }

    // Javadoc inherited from super class.
    protected void closeTableRow(DOMOutputBuffer dom,
                                 TableRowAttributes attributes) {

        dom.closeElement("tr");
    }

    // Javadoc inherited from super class.
    protected void openTableHeader(DOMOutputBuffer dom,
                                   TableHeaderAttributes attributes)
        throws ProtocolException {

        // Start any stylistic emulation markup for the table header.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeTableHeader(DOMOutputBuffer dom,
                                    TableHeaderAttributes attributes) {

        // Finish any stylistic emulation markup for the table header.
        closeStyleMarker(dom);
    }

    // Javadoc inherited
    protected void closeTableCaption(DOMOutputBuffer dom,
                                     CaptionAttributes attributes) {
        dom.closeElement(WMLConstants.BLOCH_ELEMENT);
    }

    // Javadoc inherited
    protected void openTableCaption(DOMOutputBuffer dom,
                                    CaptionAttributes attributes)
            throws ProtocolException {
        dom.openStyledElement(WMLConstants.BLOCH_ELEMENT, attributes);
    }

    // Javadoc inherited from super class.
    protected void openTableBody(DOMOutputBuffer dom,
                                 TableBodyAttributes attributes)
        throws ProtocolException {

        // Start any stylistic emulation markup for the table body.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeTableBody(DOMOutputBuffer dom,
                                  TableBodyAttributes attributes) {
        // Finish any stylistic emulation markup for the table body.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openTableFooter(DOMOutputBuffer dom,
                                   TableFooterAttributes attributes)
        throws ProtocolException {

        // Start any stylistic emulation markup for the table footer.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeTableFooter(DOMOutputBuffer dom,
                                    TableFooterAttributes attributes) {

        // Finish any stylistic emulation markup for the table footer.
        closeStyleMarker(dom);
    }

    // ========================================================================
    //   Inline element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void openBig(DOMOutputBuffer dom,
                           BigAttributes attributes) {

        // Start any stylistic emulation markup for the big.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeBig(DOMOutputBuffer dom,
                            BigAttributes attributes) {

        // Finish any stylistic emulation markup for the big.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openBold(DOMOutputBuffer dom,
                            BoldAttributes attributes) {

        // Start any stylistic emulation markup for the bold.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeBold(DOMOutputBuffer dom,
                             BoldAttributes attributes) {

        // Finish any stylistic emulation markup for the bold.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openCite(DOMOutputBuffer dom,
                            CiteAttributes attributes) {

        // Start any stylistic emulation markup for the cite.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeCite(DOMOutputBuffer dom,
                             CiteAttributes attributes) {

        // Finish any stylistic emulation markup for the cite.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openCode(DOMOutputBuffer dom,
                            CodeAttributes attributes) {

        // Start any stylistic emulation markup for the code.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeCode(DOMOutputBuffer dom,
                             CodeAttributes attributes) {

        // Finish any stylistic emulation markup for the code.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openEmphasis(DOMOutputBuffer dom,
                                EmphasisAttributes attributes) {

        // Avoid creating invalid markup - anchor cannot contain style tags.
        if (!insideAnchorBody) {
            dom.openStyledElement("em", attributes);
        }

    }

    // Javadoc inherited from super class.
    protected void closeEmphasis(DOMOutputBuffer dom,
                                 EmphasisAttributes attributes) {

        // Avoid creating invalid markup - anchor cannot contain style tags.
        if (!insideAnchorBody) {
            dom.closeElement("em");
        }
    }

    // Javadoc inherited from super class.
    protected void openItalic(DOMOutputBuffer dom,
                              ItalicAttributes attributes) {

        // Start any stylistic emulation markup for the i.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeItalic(DOMOutputBuffer dom,
                               ItalicAttributes attributes) {

        // Finish any stylistic emulation markup for the i.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openKeyboard(DOMOutputBuffer dom,
                                KeyboardAttributes attributes) {

        // Start any stylistic emulation markup for the kbd.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeKeyboard(DOMOutputBuffer dom,
                                 KeyboardAttributes attributes) {

        // Finish any stylistic emulation markup for the kbd.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void doLineBreak(DOMOutputBuffer dom,
                               LineBreakAttributes attributes) {

        dom.addStyledElement("br", attributes);
    }

    // Javadoc inherited from super class.
    protected void openMonospaceFont(DOMOutputBuffer dom,
                                     MonospaceFontAttributes attributes)
        throws ProtocolException {

        // Start any stylistic emulation markup for the monospace font.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeMonospaceFont(DOMOutputBuffer dom,
                                      MonospaceFontAttributes attributes) {

        // Finish any stylistic emulation markup for the monospace font.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openSample(DOMOutputBuffer dom,
                              SampleAttributes attributes) {

        // Start any stylistic emulation markup for the sample.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeSample(DOMOutputBuffer dom,
                               SampleAttributes attributes) {

        // Finish any stylistic emulation markup for the sample.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openSmall(DOMOutputBuffer dom,
                             SmallAttributes attributes) {

        // Start any stylistic emulation markup for the small.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeSmall(DOMOutputBuffer dom,
                              SmallAttributes attributes) {

        // Finish any stylistic emulation markup for the small.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openStrong(DOMOutputBuffer dom,
                              StrongAttributes attributes) {

        // Avoid creating invalid markup - anchor cannot contain style tags.
        if (!insideAnchorBody) {
            dom.openStyledElement("strong", attributes);
        }
    }

    // Javadoc inherited from super class.
    protected void closeStrong(DOMOutputBuffer dom,
                               StrongAttributes attributes) {

        // Avoid creating invalid markup - anchor cannot contain style tags.
        if (!insideAnchorBody) {
            dom.closeElement("strong");
        }
    }

    // Javadoc inherited from super class.
    protected void openSubscript(DOMOutputBuffer dom,
                                 SubscriptAttributes attributes) {

        // Start any stylistic emulation markup for the sub.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeSubscript(DOMOutputBuffer dom,
                                  SubscriptAttributes attributes) {

        // Finish any stylistic emulation markup for the sub.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openSuperscript(DOMOutputBuffer dom,
                                   SuperscriptAttributes attributes) {

        // Start any stylistic emulation markup for the sup.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeSuperscript(DOMOutputBuffer dom,
                                    SuperscriptAttributes attributes) {

        // Finish any stylistic emulation markup for the sup.
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openUnderline(DOMOutputBuffer dom,
                                 UnderlineAttributes attributes) {

        // Start any stylistic emulation markup for the u.
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeUnderline(DOMOutputBuffer dom,
                                  UnderlineAttributes attributes) {

        // Finish any stylistic emulation markup for the u.
        closeStyleMarker(dom);
    }

    // ========================================================================
    //   Special element methods.
    // ========================================================================


    /**
     * SPECIAL NOTE 1: There are potential override cases here, where
     * tag and theme arguments can match. In all cases, tag will
     * override any theme values. Ordering is therefore important
     */


    /**
     * Add any theme stylistic attributes for an img element.
     *
     * @param element    The element to add the attributes to.
     * @param attributes The attributes for this element.
     */
    protected void addImageAttributes(Element element,
                                      ImageAttributes attributes) {

    }

    /**
     * Generate the markaup to add an img element to the dom tree.
     *
     * @param dom        The dom tree.
     * @param attributes The attributes for this element.
     */
    protected void doImage(DOMOutputBuffer dom,
                           ImageAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;
        TextAssetReference reference = attributes.getAltText();
        String altText = getPlainText(reference);

        if ((value = attributes.getSrc()) != null) {
            Element element = dom.allocateStyledElement("img", styles);

            addImageAttributes(element, attributes);

            String attributeName = (attributes.isLocalSrc() ?
                "localsrc" : "src");
            if (attributes.getAssetURLSuffix() != null) {
                value = value + attributes.getAssetURLSuffix();
            }
            setAttribute(element, attributeName, value);

            // Apply stylistic information, if present
            if (null != styles) {
                
                if ((value = wordSpacingRenderer.getAsString(styles)) != null) {
                    element.setAttribute("hspace", value);
                }
    
                if ((value = lineHeightRenderer.getAsString(styles)) != null) {
                    element.setAttribute("vspace", value);
                }
    
                // Set the width and height attribute if they are supported on the
                // current device. First we try the style and then if that did not
                // set it then we need to try the image asset itself.
                if (supportsImgDimensions) {
                    if ((value = widthHandler.getAsString(styles)) != null) {
                        element.setAttribute("width", value);
                    }
                    if ((value = heightHandler.getAsString(styles)) != null) {
                        element.setAttribute("height", value);
                    }
    
    
                    // todo merge the attribute values into the style.
                    if (element.getAttributeValue("width") == null
                        && (value = attributes.getWidth()) != null) {
                        element.setAttribute("width", value);
                    }
                    if (element.getAttributeValue("height") == null
                        && (value = attributes.getHeight()) != null) {
                        element.setAttribute("height", value);
                    }
                }
            }
            
            if (altText != null) {
                element.setAttribute("alt", altText);
            } else if (element.hasAttributes()) {
                // The alt attribute is mandatory on WML
                element.setAttribute("alt", " ");
            }

            // Add the img element
            dom.openElement(element);
            // Nothing needs to be done here for style emulation
            // as the styles will have been added when the element was allocated.
            dom.closeElement(element);

        } else if (altText != null) {
            if (!WhitespaceUtilities.isWhitespace(altText, 0,
                                                  altText.length())) {
                dom.appendEncoded(altText);
            }
        }
    }

    /**
     * Add a timer element to the dom tree.
     *
     * @param dom        The dom tree.
     * @param attributes The attributes for this element.
     */
    public void doTimer(DOMOutputBuffer dom, TimerAttributes attributes) {

        Element element = dom.addStyledElement("timer", attributes);

        String value;

        // Parse the duration string into an int and then convert it from
        // milliseconds to 1/10s of a second rounding to the nearest 1/10.
        value = attributes.getDuration();
        int duration = Integer.parseInt(value);
        duration = (duration + 50) / 100;

        if (logger.isDebugEnabled()) {
            logger.debug("Converted " + value + "ms to "
                         + duration + " 1/10s");
        }

        element.setAttribute("value", String.valueOf(duration));
    }

    // ========================================================================
    //   Menu element methods.
    // ========================================================================

    /**
     * This method appends the output for the menu item to the outStr buffer.
     *
     * @return True if a horizontal separator is required and false otherwise.
     *         Images do not require a horizontal separator but text items do.
     *         <p/>
     *         This code is similar to some that appears in openHref - when
     *         tidying up this code and removing call backs etc., it is
     *         important that both methods are updated. </p>
     */
    protected boolean doMenuItem(DOMOutputBuffer dom,
                                 MenuAttributes attributes,
                                 MenuItem item) {

        String resolvedHref = getRewrittenLinkFromObject(item.getHref(),
                                                         item.getSegment() !=
                                                         null);

        if (resolvedHref == null) {
            // OK we could not resolve the link. See if there is a fallback
            // TextComponent associated with any LinkComponent that might
            // have been provided.
            String fallbackText =
                getTextFallbackFromLink(item.getHref());
            if (fallbackText != null) {
                dom.appendEncoded(fallbackText);
            }
            return true;
        }

        // resolvedHref is valid so continue processing of the menu item
        String type = attributes.getType();
        String value;

        // Handle rollover image style menu.
        if ("rolloverimage".equals(type)) {

            // Try and do the rollover image, if any problems occurred then
            // try simple text.
            if (doRolloverImage(dom, item, resolvedHref)) {
                return false;
            }
        }

        Element element = dom.openStyledElement("a", item);

        if (supportsAccessKeyAttribute) {
            value = getPlainText(item.getShortcut());
            setAttribute(element, "accesskey", value);
        }

        setAttribute(element, "href", resolvedHref);

        // todo fix: style class for menu items are ignored.
        addDoAttributes(element, attributes);

        // Add the text associated with the menu item.
        dom.appendEncoded(item.getText());

        dom.closeElement(element);

        return true;
    }

    /**
     * Add a vertical menu item seperator (br) element to the dom tree.
     *
     * @param dom The dom tree.
     */
    protected void addVerticalMenuItemSeparator(DOMOutputBuffer dom) {
        dom.addElement("br");
    }

    /**
     * Add a horizontal menu item seperator (&nbsp;) element to the dom tree.
     *
     * @param dom The dom tree.
     */
    protected void addHorizontalMenuItemSeparator(DOMOutputBuffer dom) {
        dom.appendEncoded(NBSP);
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.protocols.DOMProtocol#renderMenuItemSeparator(com.volantis.mcs.protocols.DOMOutputBuffer, com.volantis.mcs.protocols.ImageAttributes)
     */
    protected void renderMenuItemSeparator(DOMOutputBuffer dom,
                                           ImageAttributes imageAttributes) {
        doImage(dom, imageAttributes);
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.protocols.DOMProtocol#renderMenuItemSeparator(com.volantis.mcs.protocols.DOMOutputBuffer, java.lang.String, int)
     */
    protected void renderMenuItemSeparator(DOMOutputBuffer dom, String separatorCharacters,
                                           int separatorRepeat) {
        for (int i = 0; i < separatorRepeat; i++) {
            dom.appendEncoded(separatorCharacters);
        }
    }

    /**
     * Generate the markup to display this menu item as a rollover image, or
     * return false if it could not be done.
     *
     * @param dom  The buffer to add the generated markup to.
     * @param item The attributes of the menu item.
     * @return True if the menu item could be rendered as a rollover image and
     *         false otherwise.
     */
    private boolean doRolloverImage(DOMOutputBuffer dom,
                                      MenuItem item,
                                      String resolvedHref) {

        // No longer supported.
        throw new UnsupportedOperationException();
    }

    // ========================================================================
    //   Script element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void openScript(DOMOutputBuffer dom,
                              ScriptAttributes attributes) {

        throw new UnsupportedOperationException
            ("script not supported on wml");
    }

    // Javadoc inherited from super class.
    protected void closeScript(DOMOutputBuffer dom,
                               ScriptAttributes attributes) {

        throw new UnsupportedOperationException
            ("script not supported on wml");
    }

    // ========================================================================
    //   Extended function form element methods.
    // ========================================================================

    protected void openForm(XFFormAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getPreambleBuffer(attributes.getFormData ());
        doTopFragmentLinks(dom, attributes);
    }

    protected void closeForm(XFFormAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getPostambleBuffer(attributes.getFormData ());
        doBottomFragmentLinks(dom, attributes);
        context.clearFormFragmentResetState();
    }

    /**
     * Add any theme stylistic attributes for an input element.
     *
     * @param element    The element to add the attributes to.
     * @param attributes The attributes for this element.
     */
    protected void addTextInputAttributes(Element element,
                                          XFTextInputAttributes attributes) {

    }

    /**
     * Add extra information to the element, or otherwise, which will provide
     * client side validation of the input field.
     *
     * @param element    The Element to add client side validation
     * @param attributes {@link XFTextInputAttributes} for the text input field
     */
    protected void addTextInputValidation(
            Element element,
            XFTextInputAttributes attributes) {

        // Get the validation format and parse it to make sure that it is
        // valid.
        TextInputFormat inputFormat = getTextInputFormat(attributes);

        // If it is valid then add appropriate attributes.
        if (inputFormat != null) {
            String format = inputFormat.getFormat();
            if (format != null) {
                ValidationHelper helper = getValidationHelper();
                element.setAttribute("format",
                        helper.createTextInputFormat(format));
            }
            boolean emptyOk = inputFormat.isEmptyOk();
            if (emptyOk) {
                element.setAttribute("emptyok", "true");
            }
        }
    }

    /**
     * Generate the markaup to add an img element to the dom tree.
     *
     * @param attributes The attributes for this element.
     */
    public void doTextInput(XFTextInputAttributes attributes)
            throws ProtocolException {

        String value;
        int ivalue;
        ContainerInstance entryContainerInstance =
                attributes.getEntryContainerInstance();

        // Write out the caption.
        writeCaption(attributes);

        // If the entry container is not set then return as there is nothing
        // else we can do.
        if (entryContainerInstance == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Direct the markup to the entry container's content buffer.
        DOMOutputBuffer dom = getCurrentBuffer(entryContainerInstance);

        // Generate the markup.
        Element element = dom.addStyledElement("input", attributes);

        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);

        String tabindex = (String)attributes.getTabindex();
        if (supportsTabindex && tabindex != null) {
            element.setAttribute("tabindex", tabindex);
        }

        // Initialise the title attribute from the prompt attribute, as
        // title is ALMOST one of the core attributes.
        attributes.setTitle(getPlainText(attributes.getPrompt()));

        addTitleAttribute(element, attributes, true);
        addTextInputAttributes(element, attributes);

        element.setAttribute("name", attributes.getName());
        element.setAttribute("type", attributes.getType().toLowerCase());

        // Get stylistic information.
        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();

        StyleInteger columnsValue = (StyleInteger)
                propertyValues.getComputedValue(StylePropertyDetails.MCS_COLUMNS);
        element.setAttribute("size", columnsValue.getStandardCSS());

        addTextInputValidation(element, attributes);

        if ((value = getInitialValue(attributes)) != null) {
            element.setAttribute("value", value);
        }

        if ((ivalue = attributes.getMaxLength()) != -1) {
            element.setAttribute("maxlength", String.valueOf(ivalue));
        }

        if (supportsAccessKeyAttribute) {
            setAttribute(element, "accesskey", getPlainText(
                attributes.getShortcut()));
        }
    }

    public void doBooleanInput(XFBooleanAttributes attributes)
            throws ProtocolException {

        String value;
        String[] values;
        ContainerInstance entryContainerInstance =
                attributes.getEntryContainerInstance();        

        // Output the caption.
        writeCaption(attributes);

        // If the entry container is not set then return as there is nothing
        // else we can do.
        if (entryContainerInstance == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Direct the markup to the entry container's content buffer.
        DOMOutputBuffer dom = getCurrentBuffer(entryContainerInstance);

        // Generate the markup.
        Element element = dom.openStyledElement("select", attributes);

        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);

        String tabindex = (String)attributes.getTabindex();
        if (supportsTabindex && tabindex != null) {
            element.setAttribute("tabindex", tabindex);
        }

        // Initialise the title attribute from the prompt attribute, as
        // title is ALMOST one of the core attributes.
        attributes.setTitle(getPlainText(attributes.getPrompt()));

        addTitleAttribute(element, attributes, true);

        element.setAttribute("name", attributes.getName());
        element.setAttribute("multiple", "false");

        if ((value = getInitialValue(attributes)) != null) {
            int iv = Integer.parseInt(value);
            value = Integer.toString((iv + 1 > 2) ? 2 : iv + 1);
            element.setAttribute("ivalue", value);
        }

        // Get the comma separated list of false values and parse it into
        // an array of strings.
        value = getPlainText(attributes.getFalseValues());
        String caption = null;
        if (value == null) {
            caption = XFBOOLEAN_DEFAULT_FALSE_VALUE;
        } else {
            values = parseCommaSeparatedList(value);
            caption = values[0];
        }

        // Add the false option.
        doOption(dom, attributes, "0", caption);

        // Get the comma separated list of true values and parse it into
        // an array of strings.
        value = getPlainText(attributes.getTrueValues());
        if (value == null) {
            caption = XFBOOLEAN_DEFAULT_TRUE_VALUE;
        } else {
            values = parseCommaSeparatedList(value);
            caption = values[0];
        }

        // Add the true option.
        doOption(dom, attributes, "1", caption);

        // Close the select element.
        dom.closeElement(element);
    }

    /**
     * Add any theme stylistic attributes for an option element.
     *
     * @param element    The element to add the attributes to.
     * @param attributes The attributes for this element.
     */
    protected void addOptionAttributes(Element element,
                                       MCSAttributes attributes) {
    }

    /**
     * Generate the markaup to add an option element to the dom tree.
     *
     * @param dom        The dom tree.
     * @param attributes The attributes for this element.
     */
    protected void doOption(DOMOutputBuffer dom,
                            MCSAttributes attributes,
                            String value,
                            String caption) {

        // Add the option.
        Element element = dom.openStyledElement("option", attributes);

        addTitleAttribute(element, attributes, true);

        element.setAttribute("value", value);
        addOptionAttributes(element, attributes);
        dom.appendEncoded(caption);

        dom.closeElement(element);
    }

    /**
     * Encode a multiple select value and add it to the specified buffer.
     * <p/>
     * ; are encoded as ,. and , is encoded as ,, </p>
     */
    protected String encodeMultipleSelectValue(String value) {

        StringBuffer outStr = new StringBuffer();
        int length = value.length();
        for (int i = 0; i < length; i += 1) {
            char c = value.charAt(i);
            if (c == ';') {
                outStr.append(",.");
            } else if (c == ',') {
                outStr.append(",,");
            } else {
                outStr.append(c);
            }
        }
        return outStr.toString();
    }

    /**
     * Parse the ; separated list of encoded values and return an array of
     * decoded values.
     *
     * @param value The ; separated list of encode values.
     * @return The array of decoded values.
     */
    public String[] decodeMultipleSelectValues(String value) {

        // If the value is null then there are no values.
        if (value == null) {
            return null;
        }

        // Get the length of the value.
        int length = value.length();

        // If the value is empty then there are no values.
        if (length == 0) {
            return null;
        }

        // Count how many different values there are which should be one more
        // than the number of semi colons.
        int count = 1;
        for (int i = 0; i < length; i += 1) {
            char c = value.charAt(i);
            if (c == ';') {
                count += 1;
            }
        }

        // Create an array for the values.
        String[] values = new String[count];

        // Iterate through the string decoding characters and adding them to
        // the string buffer until a ; is reached at which point the contents
        // of the buffer are added to the array and the buffer is cleared
        // ready for the next list.
        StringBuffer buffer = new StringBuffer();
        count = 0;
        for (int i = 0; i < length; i += 1) {
            char c = value.charAt(i);
            if (c == ';') {
                values[count] = buffer.toString();
                count += 1;
                buffer.setLength(0);
            } else if (c == ',') {
                i += 1;
                c = value.charAt(i);
                if (c == '.') {
                    // ,. is a ;
                    buffer.append(';');
                } else if (c == ',') {
                    // ,, is a ,
                    buffer.append(',');
                } else {
                    // Something is not right so just append the characters
                    // as we found them.
                    buffer.append(',').append(c);
                }
            } else {
                buffer.append(c);
            }
        }

        // Add the last value to the array.
        values[count] = buffer.toString();

        return values;
    }

    // Javadoc inherited from super class.
    public FieldHandler getFieldHandler(MultipleSelectFieldType type) {
        return WMLMultipleSelectFieldHandler.getSingleton();
    }


    public void doSelectInput(XFSelectAttributes attributes)
        throws ProtocolException {

        ContainerInstance captionContainerInstance;
        ContainerInstance entryContainerInstance;
        String value;

        // Add the caption to the caption container.
        if ((captionContainerInstance =
                attributes.getCaptionContainerInstance()) != null &&
                (value = getPlainText(attributes.getCaption())) != null) {
            addToBuffer(captionContainerInstance, value);
        }

        // If the entry container is not set then return as there is nothing
        // else we can do.
        if ((entryContainerInstance =
                attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }

        updateSelectedOptions(attributes);
        // Direct the markup to the entry container's content buffer.
        DOMOutputBuffer dom = getCurrentBuffer(entryContainerInstance);


        getSelectionRenderer(attributes).renderSelection(attributes,
                dom);
    }

    // javadoc inherited from superclass
    protected SelectionRenderer
        getSelectionRenderer(XFSelectAttributes attributes) {
        if (null == defaultSelectionRenderer) {
            defaultSelectionRenderer = new WMLDefaultSelectionRenderer(this);
        }
        return defaultSelectionRenderer;
    }


    /**
     * Add any theme stylistic attributes for an optGroup element.
     *
     * @param element    The element to add the attributes to.
     * @param attributes The attributes for this element.
     */
    protected void addOptGroupAttributes(Element element,
                                         XFSelectAttributes attributes) {
    }

    /**
     * Add an action to the form. The action is implemented using a do element
     * of type submit which contains a go element which itself contains
     * postfield elements for all the name, value pairs which form part of the
     * form data. These include implicit values and action values.
     *
     * @param dom        The DOMOutputBuffer to which the action should be
     *                   added.
     * @param attributes The attributes to use when generating the mark up.
     */
    protected void doActionInput(DOMOutputBuffer dom,
                                 XFActionAttributes attributes)
        throws ProtocolException {

        // Update the title to use for the field, this may modify the title
        // attribute in the attributes passed in so this must be called before
        // the addCoreAttributes method is called.
        updateFieldTitle(attributes);

        // Check if this action is being rendered inline (no form attributes
        // and not emulating an XForm control) - we'll need to do something
        // different.
        if (attributes.getFormAttributes() == null &&
                attributes.getContainingXFFormName() == null) {

            doInlineAction(dom, attributes);

        } else {
            // Only submit action types are supported.
            String actionType = attributes.getType();

            // Get stylistic information.
            Styles styles = attributes.getStyles();
            StyleValue styleValue = styles.getPropertyValues().getComputedValue(
                    StylePropertyDetails.MCS_FORM_ACTION_STYLE);

            // Actions which have an action style of "link" should be rendered
            // inline, otherwise they should appear in a browser menu.
            boolean inline = (styleValue == MCSFormActionStyleKeywords.LINK);
            doFormAction(dom, attributes, actionType, inline);
        }
    }

    /**
     * This method generates markup which will cause the action to appear
     * inline in the page and which when acticated will cause the onclick event
     * handler to be activated.
     *
     * @param dom        the dom output buffer
     * @param attributes the action attributes
     */
    private void doInlineAction(DOMOutputBuffer dom,
                                  XFActionAttributes attributes)
        throws ProtocolException {

        // Open the element to use for the action.
        Element actionElement = openAction(dom, attributes, true);

        // Add the task from the onclick script.
        Script task = getTaskForEvent(attributes, "onclick",
                                      EventConstants.ON_CLICK);
        if (task != null) {
            // Append the script into the output buffer.
            task.appendTo(dom);
        }

        // Close the action element.
        dom.closeElement(actionElement);
    }

    /**
     * This method generates markup which will cause the action to appear
     * either inline in the page, or in a browser menu depending on the inline
     * flag. When activated the action will submit the form.
     *
     * @param dom        the dom output buffer
     * @param attributes the action attributes
     * @param actionType Either "submit" or "reset"
     * @param inline     True means the action should appear inline in the page
     *                   and false means it should appear in a browser menu.
     */
    protected void doFormAction(DOMOutputBuffer dom,
                                XFActionAttributes attributes,
                                String actionType,
                                boolean inline) {

        // Open the anchor element to use for the action.
        Element actionElement = openAction(dom, attributes, inline);

        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(actionElement, attributes);

        if ("submit".equals(actionType)) {
            // Add the go element to use for the action.
            addActionGo(dom, attributes);
        } else if ("reset".equals(actionType)) {
            addActionReset(dom, attributes);
        } else {
            throw new IllegalArgumentException
                ("Unknown action type " + actionType);
        }

        // Close the do element.
        dom.closeElement(actionElement);
    }

    /**
     * Open the outermost WML element to use for action. This is either going
     * to be a do or an anchor element.
     *
     * @param dom        the dom output buffer
     * @param attributes the action attributes
     * @param inline     True if the action should be inline and false if it
     *                   should be in a browser menu.
     */
    protected Element openAction(DOMOutputBuffer dom,
                                 XFActionAttributes attributes,
                                 boolean inline) {
        if (alwaysUseDoForAction || !inline) {
            return openActionDo(dom, attributes);
        } else {
            return openActionAnchor(dom, attributes);
        }
    }

    /**
     * This method is responsible for opening and initialising an anchor
     * element for an xfaction.
     */
    private Element openActionAnchor(DOMOutputBuffer dom,
                                       XFActionAttributes attributes) {

        Element element = dom.openStyledElement("anchor", attributes);

        // Add title which is supported by anchor.
        addTitleAttribute(element, attributes, true);

        // Get the caption from the text component, if it could not be found
        // then use the title.
        String caption = getPlainText(attributes.getCaption());
        if (caption == null) {
            caption = attributes.getTitle();
        }

        if (supportsAccessKeyAttribute) {
            String value = getPlainText(attributes.getShortcut());
            setAttribute(element, "accesskey", value);
        }

        // If a caption has been specified then add it to the body of anchor,
        // otherwise do not add anything as it may be that is what is wanted.
        if (caption != null) {
            try {
                dollarEncoder.reset();
                dom.appendEncoded( dollarEncoder.encode( caption ) );
            } catch (WMLVariableException e) {
                logger.error("encoding-error", new Object[]{caption});
            }
        }

        return element;
    }

    /**
     * Get the type to use for do element.
     */
    protected String getActionDoType(XFActionAttributes attributes) {
        return "accept";
    }

    /**
     * This method is responsible for opening and initialising an do element
     * for an xfaction.
     */
    private Element openActionDo(DOMOutputBuffer dom,
                                   XFActionAttributes attributes) {

        Element doElement = dom.openStyledElement("do", attributes);
        String value = getActionDoType(attributes);
        doElement.setAttribute("type", value.toLowerCase());

        // Use the action name as the name of the do. If no name has been
        // specified on the action then generate a unique name.
        String name = attributes.getName();
        if (name == null) {
            name = context.generateWMLActionID();
        }
        doElement.setAttribute("name", name);

        String caption = getPlainText(attributes.getCaption());

        // The label attribute is a combination of caption and title, how
        // it is rendered depends on the device which is rendering it.
        // Try the caption first, then the title (prompt).
        if ((value = caption) != null
            || (value = attributes.getTitle()) != null) {
            doElement.setAttribute("label", value);
        }

        // Set the tab index of the do element if one exists
        String tabindex = (String)attributes.getTabindex();
        if (supportsTabindex && tabindex != null) {
            doElement.setAttribute("tabindex", tabindex);
        }
        addActionDoAttributes(doElement, attributes);
        return doElement;
    }

    /**
     * Add any theme stylistic attributes for an xfaction do element.
     *
     * @param element    The element to add the attributes to.
     * @param attributes The attributes for this element.
     */
    protected void addActionDoAttributes(Element element,
                                         XFActionAttributes attributes) {
    }

    /**
     * This method is responsible for adding a go element for an xfaction.
     */
    private Element addActionGo(DOMOutputBuffer dom,
                                  XFActionAttributes attributes) {

        XFFormAttributes formAttributes = attributes.getFormAttributes();

        Element goElement = dom.openStyledElement("go", attributes);

        // the form attributes will only be null if this is an emulated XForm
        // control, and in this case, these elements will be emulated later.
        if (!attributes.isEulatedXFormFieldAttributes()) {
            setAttribute(goElement, "href", resolveFormAction(formAttributes));
            setAttribute(goElement, "method",
                    formAttributes.getMethod().toLowerCase());
        }

        processPostFields(dom, attributes, formAttributes);

        // Close the go element.
        dom.closeElement(goElement);

        return goElement;
    }

    /**
     * Process the post fields. Common to processButtonAction() and
     * processLinkAction().
     *
     * @param dom
     * @param attributes
     * @param formAttributes    Must be non null
     */
    private void processPostFields(DOMOutputBuffer dom,
                                     XFActionAttributes attributes,
                                     XFFormAttributes formAttributes) {

        if (!attributes.isEulatedXFormFieldAttributes()) {
            // Add a postfield element for the form specifier.
            Element postFieldElement = dom.addStyledElement("postfield",
                                                            attributes);
            postFieldElement.setAttribute("name", URLConstants.FORM_PARAMETER);
            postFieldElement.setAttribute("value", getFormSpecifier(formAttributes));
        }
        
        boolean doneThis = false;
        // Iterate over all the fields adding postfield tags to this action.
        // The list of fields may not contain the current action if it is
        // being added by form fragmentation code so make sure that we
        // process this attribute as well.
        List fields = formAttributes.getFields();
        int count = fields.size();
        for (int i = 0; i < count; i += 1) {

            XFFormFieldAttributes field
                = (XFFormFieldAttributes)fields.get(i);

            // Ignore other action fields when generating the field names.
            if (field instanceof XFActionAttributes) {
                if (field == attributes) {
                    doneThis = true;
                } else {
                    continue;
                }
            }

            // Ignore any non implicit fields which do not have an entry
            // pane set.
            if (!(field instanceof XFImplicitAttributes)
                && field.getEntryContainerInstance() == null) {
                continue;
            }

            // Add a post field entry for the field.
            addPostField(dom, field);
        }

        // If we have not added a post field entry for this field then add
        // one now.
        if (!doneThis) {
            addPostField(dom, attributes);
        }
    }

    protected void addPostField(DOMOutputBuffer dom,
                                XFFormFieldAttributes attributes) {

        // Only add a postfield if the field has a name.
        String fieldName = attributes.getName();
        if (fieldName != null) {
            String value;

            if (attributes instanceof XFImplicitAttributes) {
                // The field is an implicit value so get the value out
                // of the field. We need to encode $ characters as
                // literals or variables depending on the configuration
                value =
                    getXFImplicitAttributesValue(
                        (XFImplicitAttributes)attributes);
                dollarEncoder.reset();
            } else if (attributes instanceof XFActionAttributes) {
                // The field is an action field so get the value out of
                // the field. If the value is null then try the caption,
                // else use a single space.
                value = ((XFActionAttributes)attributes).getValue();
                if (value == null) {
                    value = " ";
                }
            } else {
                // The field is a normal input field so the value of the
                // field is stored in a variable with the same name as the
                // field.
                value = variableStyle + fieldName + variableStyle;
            }

            Element element = dom.addStyledElement("postfield", attributes);
            element.setAttribute("name", fieldName);
            element.setAttribute("value", value);
        }
    }

    // Javadoc inherited.
    public void populateEmulatedActionElement(Element element,
            EmulatedXFormDescriptor fd) {

        if ("do".equals(element.getName())) {
            // then it is an action element, so we should populate the contained
            // action url and method, and generate post fields
            Element goElement = (Element) element.getHead();

            String resolvedAction = resolveFormAction(fd.getFormAttributes());
            setAttribute(goElement, "href", resolvedAction);
            setAttribute(goElement, "method", fd.getFormMethod());

            // Add a postfield element for the form specifier.
            final Element postFieldElement = domFactory.createElement();
            postFieldElement.setName("postfield");
            postFieldElement.setAttribute("name", URLConstants.FORM_PARAMETER);
            postFieldElement.setAttribute("value", fd.getFormSpecifier());
            goElement.addHead(postFieldElement);

            // Iterate over all the fields adding postfield tags to this action.
            List fields = fd.getFields();
            int count = fields.size();
            for (int i = 0; i < count; i += 1) {
                FieldDescriptor field = (FieldDescriptor) fields.get(i);

                final String fieldName = field.getName();
                if (fieldName != null) {
                    final String value;
                    // Ignore action fields when generating post fields (this
                    // one will have been added by #processPostFields).
                    if (field.getType() instanceof ActionFieldType) {
                        continue;
                    } else if (field.getType() instanceof ImplicitFieldType) {
                        if(null == field.getInitialValue()){
                            // null value is not-valid
                            value = "";
                        } else {
                            value = field.getInitialValue();                            
                        }
                    } else {
                        // The field is emulating an XForm control, so the
                        // value of the field is stored in a variable with the
                        // same name as the field.
                        value = variableStyle + field.getName() + variableStyle;
                    }

                    // Add a post field entry for the field.
                    final Element postField = domFactory.createElement();
                    postField.setName("postfield");
                    postField.setAttribute("name", fieldName);
                    postField.setAttribute("value", value);
                    goElement.addHead(postField);
                }
            }
        }
    }

    /**
     * Determines the value of an xfimplicit element. This can be specified in
     * two ways: - a value attribute used to specify the value of the field
     * passed back to the server. - a clientVariableName attribute used to
     * specify the name of a client- side variable whose value is passed back
     * to the server. This can be a Mariner attribute expression. When both are
     * present, clientVariableName takes precedence.
     *
     * @param attributes The XFImplicitAttributes
     * @return String The value to be written to the postfield.
     * @todo better reduce garbage creating in the method
     */
    protected String getXFImplicitAttributesValue(
        XFImplicitAttributes attributes) {
        String value = attributes.getClientVariableName();

        if (value != null) {
            /* To make the variable name legal in wml, we prefix it with a
             * $ e.g. <xfimplicit name="fred" clientVariableName="freda"/>
             * renders as <postfield name="fred" value="$freda"/>
             * So <xfimplicit name="fred" clientVariableName="$freda"/>
             * will renders as <postfield name="fred" value="$$freda"/>
             * even though its not what the user intended but at least its
             * predictable behaviour.
             * While $freda will be interpreted as its value.
             * $$freda is interpreted as the literal string $$freda.
             * So as not to clash with $ handling in the rest of WML we add
             * the dollar symbol using WMLVariable constants.
             */
            value = WMLVariable.WMLV_NOBRACKETS + value
                + WMLVariable.WMLV_NOBRACKETS;
        } else {
            value = attributes.getValue();
        }
        return value;
    }

    /**
     * This method is responsible for adding markup to reset the form.
     */
    protected Element addActionReset(DOMOutputBuffer dom,
                                     XFActionAttributes attributes) {

        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element refreshElement = dom.openStyledElement("refresh", attributes);

        XFFormAttributes formAttributes = attributes.getFormAttributes();
        if (formAttributes != null) {
            // Iterate over the fields in the form resetting any variables
            // associated with them to the default value.
            List fields = formAttributes.getFields();
            int count = fields.size();

            for (int i = 0; i < count; i += 1) {

                XFFormFieldAttributes field
                    = (XFFormFieldAttributes)fields.get(i);

                // Ignore fields which do not have variables associated with
                // them, this includes action and implicit fields, fields which
                // have no names or which are not generated for the current page.
                String fieldName;
                if (field instanceof XFActionAttributes
                    || field instanceof XFImplicitAttributes
                    || field.getEntryContainerInstance() == null
                    || (fieldName = field.getName()) == null) {
                    continue;
                }

                // Get the initial value, if it is null then change it to an
                // empty string.
                String initialValue = getPlainText(field.getInitial());
                if (initialValue == null) {
                    initialValue = "";
                }
                Element element = dom.addElement("setvar");
                element.setAttribute("name", fieldName);
                element.setAttribute("value", initialValue);
            }
        }

        // Close the refresh element.
        dom.closeElement(refreshElement);

        return refreshElement;
    }

    protected void addDoAttributes(Element element,
                                   MCSAttributes attributes) {
    }

    /**
     * Update the title to use for a field. This method has the side effect
     * that it sets the title if it has not been set but a prompt has been
     * set.
     */
    private void updateFieldTitle(XFFormFieldAttributes attributes) {
        String title;

        // If an explicit title has been set then use that.
        if (attributes.getTitle() != null) {
            return;
        }

        // Next try the prompt if that has been set.
        if ((title = getPlainText(attributes.getCaption())) != null) {
            attributes.setTitle(title);
            return;
        }

        // No title was found.
        return;
    }


    /**
     * Add Meta elemets to the DOM tree. If the MetaAttributes have both a name
     * and an httpEquiv then create a separate meta element for each one.
     */

    public void doMeta(DOMOutputBuffer dom,
                          MetaAttributes attributes) {

        String value = null;
        if (attributes != null) {
            // @todo 2005060816 annotate child with style information if it's not inherited from the parent
            Element element = dom.addStyledElement("meta", attributes);

            if ((value = attributes.getName()) != null) {
                element.setAttribute("name", value);
                if ((value = attributes.getContent()) != null) {
                    element.setAttribute("content", value);
                }
                addMetaAttributes(element, attributes);
                element = null;
            }

            if ((value = attributes.getHttpEquiv()) != null) {
                if (element == null)
                    element = dom.addElement("meta");
                element.setAttribute("http-equiv", value);
                if ((value = attributes.getContent()) != null) {
                    element.setAttribute("content", value);
                }
            }

            addMetaAttributes(element, attributes);
        }
    }

    // ========================================================================
    //   Custom markup methods
    // ========================================================================

    // javadoc inherited
    public boolean supportsNativeMarkup() {
        return true;
    }

    //javadoc inherited
    public boolean doProcessNativeMarkupWithoutExpression() {
        return true;
    }
    // ========================================================================
    //   Fallback methods
    // ========================================================================

    // Inherit javadoc.
    protected void renderAltText(String altText,
                                 MCSAttributes attributes)
        throws ProtocolException {
        // For WML we just write out the text itself, relying on the WML DOM
        // transformer to add a P in if necessary. See VBM:2004022712.
        // This is as consistent with the HTML way of adding a span as we can
        // get considering WML has no span.
        // todo: later: consider using openSpan here?

        DOMOutputBuffer dom = getCurrentBuffer();

        // Start any stylistic emulation markup for the element who's alt text
        // we are rendering.
        openStyleMarker(dom, attributes);

        Writer writer = getContentWriter();
        try {
            writer.write(altText);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
        }

        // Finish any stylistic emulation markup for the element who's alt text
        // we are rendering.
        closeStyleMarker(dom);

    }

    // javadoc inherited
    protected OutputBuffer getNativeMarkupOutputBuffer(
        NativeMarkupAttributes attributes) throws ProtocolException {
        String target = attributes.getTargetLocation();

        OutputBuffer buffer = null;

        if (NativeMarkupAttributes.WML_DECK_HEAD.equals(target)) {
            if (logger.isDebugEnabled()) {
                logger.debug("native markup goes in wml.deck.head");
            }

            buffer = getHeadBuffer();
        } else if (NativeMarkupAttributes.WML_DECK_TEMPLATE.equals(target)) {
            if (logger.isDebugEnabled()) {
                logger.debug("native markup goes in wml.deck.template");
            }

            buffer = getExtraBuffer(PAGE_TEMPLATE_BUFFER_NAME, true);
        } else if (NativeMarkupAttributes.WML_CARD_TIMER.equals(target)) {
            if (logger.isDebugEnabled()) {
                logger.debug("native markup goes in wml.card.timer");
            }

            DeviceLayoutContext dlc = context.getDeviceLayoutContext();
            buffer = dlc.getOutputBuffer(
                NativeMarkupAttributes.WML_CARD_TIMER,
                true);
        } else if (NativeMarkupAttributes.WML_CARD_ONEVENT.equals(target)) {
            if (logger.isDebugEnabled()) {
                logger.debug("native markup goes in wml.card.onevent");
            }

            DeviceLayoutContext dlc = context.getDeviceLayoutContext();
            buffer = dlc.getOutputBuffer(
                NativeMarkupAttributes.WML_CARD_ONEVENT, true);
        } else if (NativeMarkupAttributes.WML_CARD_BEFOREBODY.equals(target)) {
            if (logger.isDebugEnabled()) {
                logger.debug("native markup goes in wml.card.beforebody");
            }

            DeviceLayoutContext dlc = context.getDeviceLayoutContext();
            buffer = dlc.getOutputBuffer(
                NativeMarkupAttributes.WML_CARD_BEFOREBODY, true);
        } else {
            buffer = super.getNativeMarkupOutputBuffer(attributes);
        }

        return buffer;
    }

    protected InitialValueHandler getInitialOptionHandler() {
        return initialOptionHandler;
    }

    //==========================================================================
    // MenuRenderer interface implementation
    //==========================================================================

    // javadoc inherited
    public void writeTitleAttribute(Element element,
                                    MCSAttributes attributes) {
        // delegate to the addTitleAttribute method
        addTitleAttribute(element, attributes, true);
    }

    //========================================================================
    // MenuModule related implementation.
    //========================================================================

    /**
     * Creates a default menu module for this protocol.
     */
    // Other javadoc inherited.
    protected MenuModule createMenuModule(
        MenuModuleRendererFactoryFilter metaFactory) {

        MenuModuleRendererFactory rendererFactory =
            new DefaultMenuModuleRendererFactory(getRendererContext(),
                                                 getDeprecatedOutputLocator(),
                                                 getMenuModuleCustomisation());

        if (metaFactory != null) {
            rendererFactory = metaFactory.decorate(rendererFactory);
        }

        return new DefaultMenuModule(getRendererContext(), rendererFactory);
    }

    /**
     * Get the "special" shard link menu module for this protocol, creating one
     * if necessary.
     *
     * @return the menu module for shard links.
     */
    private MenuModule getShardLinkMenuModule() {

        if (shardLinkMenuModule == null) {
            shardLinkMenuModule = createMenuModule(
                createShardLinkMenuModuleRendererFactoryFilter());
        }
        return shardLinkMenuModule;
    }

    /**
     * Create a menu module renderer factory filter for use when creating a
     * shard link menu module.
     * <p/>
     * This allows us to customise the way we decorate the factories which
     * create renderers to add the shard link markup.
     *
     * @return the filter created.
     */
    private MenuModuleRendererFactoryFilter
        createShardLinkMenuModuleRendererFactoryFilter() {

        return new WMLShardLinkMenuModuleRendererFactoryFilter();
    }
}