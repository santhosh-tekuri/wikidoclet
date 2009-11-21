/*
 * @(#)TaggedMethodFinder.java	1.11 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.internal.toolkit.util;

import com.sun.javadoc.*;

/**
 * Find a tagged method.
 *
 * This code is not part of an API.
 * It is implementation that is subject to change.
 * Do not use it as an API
 *
 * @author Atul M Dambalkar
 */
public class TaggedMethodFinder extends MethodFinder {
    public boolean isCorrectMethod(MethodDoc method) {
        return method.paramTags().length + method.tags("return").length +
               method.throwsTags().length + method.seeTags().length > 0;
    }
}
