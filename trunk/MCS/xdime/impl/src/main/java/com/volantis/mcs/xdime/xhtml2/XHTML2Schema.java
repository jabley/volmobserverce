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

package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ContentModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;

/**
 * Schema for XHTML 2 namespace.
 */
public final class XHTML2Schema
        extends AbstractSchema {

    // Create elements.
    private final ElementSchema a = createElementSchema(XHTML2Elements.A);
    private final ElementSchema access = createElementSchema(XHTML2Elements.ACCESS);
    private final ElementSchema abbr = createElementSchema(XHTML2Elements.ABBR);
    private final ElementSchema address = createElementSchema(XHTML2Elements.ADDRESS);
    private final ElementSchema blockquote = createElementSchema(XHTML2Elements.BLOCKQUOTE);
    private final ElementSchema body = createElementSchema(XHTML2Elements.BODY);
    public final ElementSchema caption = createElementSchema(XHTML2Elements.CAPTION);
    private final ElementSchema cite = createElementSchema(XHTML2Elements.CITE);
    private final ElementSchema code = createElementSchema(XHTML2Elements.CODE);
    private final ElementSchema dd = createElementSchema(XHTML2Elements.DD);
    private final ElementSchema dfn = createElementSchema(XHTML2Elements.DFN);
    private final ElementSchema div = createElementSchema(XHTML2Elements.DIV);
    private final ElementSchema dl = createElementSchema(XHTML2Elements.DL);
    private final ElementSchema dt = createElementSchema(XHTML2Elements.DT);
    private final ElementSchema em = createElementSchema(XHTML2Elements.EM);
    private final ElementSchema h1 = createElementSchema(XHTML2Elements.H1);
    private final ElementSchema h2 = createElementSchema(XHTML2Elements.H2);
    private final ElementSchema h3 = createElementSchema(XHTML2Elements.H3);
    private final ElementSchema h4 = createElementSchema(XHTML2Elements.H4);
    private final ElementSchema h5 = createElementSchema(XHTML2Elements.H5);
    private final ElementSchema h6 = createElementSchema(XHTML2Elements.H6);
    private final ElementSchema head = createElementSchema(XHTML2Elements.HEAD);
    private final ElementSchema html = createElementSchema(XHTML2Elements.HTML);
    private final ElementSchema kbd = createElementSchema(XHTML2Elements.KBD);
    private final ElementSchema label = createElementSchema(XHTML2Elements.LABEL);
    public final ElementSchema li = createElementSchema(XHTML2Elements.LI);
    private final ElementSchema link = createElementSchema(XHTML2Elements.LINK);
    private final ElementSchema meta = createElementSchema(XHTML2Elements.META);
    private final ElementSchema nl = createElementSchema(XHTML2Elements.NL);
    private final ElementSchema object = createElementSchema(XHTML2Elements.OBJECT);
    private final ElementSchema ol = createElementSchema(XHTML2Elements.OL);
    private final ElementSchema p = createElementSchema(XHTML2Elements.P);
    private final ElementSchema param = createElementSchema(XHTML2Elements.PARAM);
    private final ElementSchema pre = createElementSchema(XHTML2Elements.PRE);
    private final ElementSchema quote = createElementSchema(XHTML2Elements.QUOTE);
    private final ElementSchema samp = createElementSchema(XHTML2Elements.SAMP);
    private final ElementSchema span = createElementSchema(XHTML2Elements.SPAN);
    private final ElementSchema sub = createElementSchema(XHTML2Elements.SUB);
    private final ElementSchema sup = createElementSchema(XHTML2Elements.SUP);
    private final ElementSchema strong = createElementSchema(XHTML2Elements.STRONG);
    private final ElementSchema style = createElementSchema(XHTML2Elements.STYLE);
    private final ElementSchema table = createElementSchema(XHTML2Elements.TABLE);
    public final ElementSchema tbody = createElementSchema(XHTML2Elements.TBODY);
    private final ElementSchema td = createElementSchema(XHTML2Elements.TD);
    public final ElementSchema tfoot = createElementSchema(XHTML2Elements.TFOOT);
    private final ElementSchema th = createElementSchema(XHTML2Elements.TH);
    public final ElementSchema thead = createElementSchema(XHTML2Elements.THEAD);
    private final ElementSchema title = createElementSchema(XHTML2Elements.TITLE);
    public final ElementSchema tr = createElementSchema(XHTML2Elements.TR);
    private final ElementSchema ul = createElementSchema(XHTML2Elements.UL);
    private final ElementSchema var = createElementSchema(XHTML2Elements.VAR);

    public final CompositeModel HEAD_CONTENT = choice();
    private final CompositeModel HEADING = choice();
    private final CompositeModel LIST = choice();
    public final CompositeModel STRUCTURAL = choice();
    public final CompositeModel TEXT = choice();
    public final CompositeModel FLOW = choice();
    public final ContentModel MIXED_TEXT;
    public final ContentModel MIXED_FLOW;

    public XHTML2Schema() {
        // Initialise the heading content set.

        HEADING.add(h1)
            .add(h2)
            .add(h3)
            .add(h4)
            .add(h5)
            .add(h6);

        // Initialise the list content set.

        LIST.add(dl)
            .add(ol)
            .add(ul)
            .add(nl);

        // Initialise the structural content set.

        STRUCTURAL.add(address)
                .add(blockquote)
                .add(div)
                .add(LIST)
                .add(p)
                .add(pre)
                .add(table);

        // Initialise the text content set.

        TEXT.add(a)
            .add(abbr)
            .add(cite)
            .add(code)
            .add(dfn)
            .add(em)
            .add(kbd)
            .add(object)
            .add(quote)
            .add(samp)
            .add(span)
            .add(strong)
            .add(sub)
            .add(sup)
            .add(var);

        FLOW.add(HEADING)
            .add(STRUCTURAL)
            .add(TEXT)
            .add(meta);

        // Initialise the content models for the elements.
        MIXED_TEXT = bounded(choice()
                .add(PCDATA)
                .add(TEXT));

        ContentModel MIXED_FLOW_LIST = bounded(choice()
                .add(PCDATA)
                .add(FLOW)
                .add(LIST));

        MIXED_FLOW = bounded(choice()
                .add(PCDATA)
                .add(FLOW));

        ContentModel LIST_CONTENT = sequence()
                .add(bounded(label).optional())
                .add(bounded(li).atLeastOne());

        // Set the content models for the elements.
        a.setContentModel(MIXED_TEXT);
        abbr.setContentModel(MIXED_TEXT);
        access.setContentModel(EMPTY);
        address.setContentModel(MIXED_TEXT);
        blockquote.setContentModel(MIXED_FLOW_LIST);
        body.setContentModel(bounded(choice()
                .add(HEADING)
                .add(STRUCTURAL)
                .add(LIST)
                .add(meta)));
        caption.setContentModel(MIXED_TEXT);
        cite.setContentModel(MIXED_TEXT);
        code.setContentModel(MIXED_TEXT);
        dd.setContentModel(MIXED_FLOW);
        dfn.setContentModel(MIXED_TEXT);
        div.setContentModel(MIXED_FLOW);
        dl.setContentModel(sequence()
                .add(bounded(label).optional())
                .add(bounded(choice().add(dt).add(dd)).atLeastOne()));
        dt.setContentModel(MIXED_TEXT);
        em.setContentModel(MIXED_TEXT);
        h1.setContentModel(MIXED_TEXT);
        h2.setContentModel(MIXED_TEXT);
        h3.setContentModel(MIXED_TEXT);
        h4.setContentModel(MIXED_TEXT);
        h5.setContentModel(MIXED_TEXT);
        h6.setContentModel(MIXED_TEXT);

        HEAD_CONTENT.add(access)
                        .add(link)
                        .add(meta)
                        .add(style);

        head.setContentModel(sequence()
                .add(title)
                .add(bounded(HEAD_CONTENT)));

        html.setContentModel(sequence().add(head).add(body));
        kbd.setContentModel(MIXED_TEXT);
        label.setContentModel(MIXED_TEXT);
        li.setContentModel(MIXED_FLOW);
        link.setContentModel(EMPTY);
        meta.setContentModel(bounded(choice()
                .add(HEADING)
                .add(STRUCTURAL)
                .add(LIST)
                .add(TEXT)
                .add(PCDATA)));
        nl.setContentModel(sequence()
                .add(label)
                .add(bounded(li).atLeastOne()));
        object.setContentModel(sequence()
                               .add(bounded(caption).optional())
                               .add(bounded(param))
                               .add(MIXED_FLOW));
        ol.setContentModel(LIST_CONTENT);
        p.setContentModel(bounded(choice()
                .add(PCDATA)
                .add(TEXT)
                .add(LIST)
                .add(blockquote)
                .add(pre)
                .add(meta)));
        param.setContentModel(EMPTY);
        pre.setContentModel(MIXED_TEXT);
        quote.setContentModel(MIXED_TEXT);
        samp.setContentModel(MIXED_TEXT);
        span.setContentModel(MIXED_TEXT);
        strong.setContentModel(MIXED_TEXT);
        sub.setContentModel(MIXED_TEXT);
        sup.setContentModel(MIXED_TEXT);
        table.setContentModel(sequence()
                              .add(bounded(caption).optional())
                              .add(choice()
                                      .add(sequence()
                                              .add(bounded(thead).optional())
                                              .add(bounded(tfoot).optional())
                                              .add(bounded(tbody).atLeastOne()))
                                      .add(bounded(tr).atLeastOne())));
        td.setContentModel(MIXED_FLOW);
        th.setContentModel(MIXED_FLOW);
        tfoot.setContentModel(bounded(tr).atLeastOne());
        thead.setContentModel(bounded(tr).atLeastOne());
        tbody.setContentModel(bounded(tr).atLeastOne());
        title.setContentModel(PCDATA);
        style.setContentModel(PCDATA);

        tr.setContentModel(bounded(choice()
                .add(td).add(th)).atLeastOne());
        ul.setContentModel(LIST_CONTENT);
        var.setContentModel(MIXED_TEXT);
    }
}
