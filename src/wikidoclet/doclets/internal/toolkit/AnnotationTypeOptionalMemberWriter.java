/*
 * @(#)AnnotationTypeOptionalMemberWriter.java	1.3 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.internal.toolkit;

import com.sun.javadoc.*;

/**
 * The interface for writing annotation type optional member output.
 *
 * This code is not part of an API.
 * It is implementation that is subject to change.
 * Do not use it as an API
 *
 * @author Jamie Ho
 * @since 1.5
 */

public interface AnnotationTypeOptionalMemberWriter extends
    AnnotationTypeRequiredMemberWriter {

    /**
     * Write the default value documentation.
     */
    public void writeDefaultValueInfo(MemberDoc member);
}

