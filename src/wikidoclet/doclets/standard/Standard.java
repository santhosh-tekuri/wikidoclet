/*
 * @(#)Standard.java	1.5 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.standard;

import com.sun.javadoc.*;
import wikidoclet.doclets.formats.html.*;


public class Standard {

    public static final HtmlDoclet htmlDoclet = new HtmlDoclet();

    public static int optionLength(String option) {
        return htmlDoclet.optionLength(option);
    }

    public static boolean start(RootDoc root) {
        return htmlDoclet.start(root);
    }

    public static boolean validOptions(String[][] options,
                                   DocErrorReporter reporter) {
        return htmlDoclet.validOptions(options, reporter);
    }

    public static LanguageVersion languageVersion() {
	return htmlDoclet.languageVersion();
    }

}

