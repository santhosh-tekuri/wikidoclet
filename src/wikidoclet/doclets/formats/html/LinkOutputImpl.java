/*
 * @(#)LinkOutputImpl.java	1.3 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.formats.html;

import wikidoclet.doclets.internal.toolkit.util.links.*;

/**
 * Stores output of a link.
 *
 * @author Jamie Ho
 * @since 1.5
 */
public class LinkOutputImpl implements LinkOutput {

    /**
     * The output of the link.
     */
    public StringBuffer output;

    /**
     * Construct a new LinkOutputImpl.
     */
    public LinkOutputImpl() {
        output = new StringBuffer();
    }

    /**
     * {@inheritDoc}
     */
    public void append(Object o) {
        output.append(o instanceof String ?
            (String) o : ((LinkOutputImpl)o).toString());
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return output.toString();
    }

}

