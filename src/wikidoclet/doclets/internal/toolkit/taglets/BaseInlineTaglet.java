/*
 * @(#)BaseInlineTaglet.java	1.8 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.internal.toolkit.taglets;

/**
 * An abstract inline taglet that outputs HTML.
 *
 * This code is not part of an API.
 * It is implementation that is subject to change.
 * Do not use it as an API
 *
 * @author Jamie Ho
 * @since 1.4
 */

public abstract class BaseInlineTaglet extends BaseTaglet {

    /**
     * Will return true since this is an inline tag.
     * @return true since this is an inline tag.
     */
    public boolean isInlineTag() {
        return true;
    }
}
