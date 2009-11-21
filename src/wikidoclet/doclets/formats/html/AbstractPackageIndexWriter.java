/*
 * @(#)AbstractPackageIndexWriter.java	1.36 05/12/02
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.formats.html;

import com.sun.javadoc.*;
import java.io.*;
import java.util.*;

/**
 * Abstract class to generate the overview files in
 * Frame and Non-Frame format. This will be sub-classed by to
 * generate overview-frame.html as well as overview-summary.html.
 *
 * @author Atul M Dambalkar
 */
public abstract class AbstractPackageIndexWriter extends HtmlDocletWriter {

    /**
     * Array of Packages to be documented.
     */
    protected PackageDoc[] packages;

    /**
     * Constructor. Also initialises the packages variable.
     *
     * @param filename Name of the package index file to be generated.
     */
    public AbstractPackageIndexWriter(ConfigurationImpl configuration,
                                      String filename) throws IOException {
        super(configuration, filename);
        this.relativepathNoSlash = ".";
        packages = configuration.packages;
    }

    protected abstract void printNavigationBarHeader();

    protected abstract void printNavigationBarFooter();

    protected abstract void printOverviewHeader();

    protected abstract void printIndexHeader(String text);

    protected abstract void printIndexRow(PackageDoc pkg);

    protected abstract void printIndexFooter();

    /**
     * Generate the contants in the package index file. Call appropriate
     * methods from the sub-class in order to generate Frame or Non
     * Frame format.
     * @param title the title of the window.
     * @param includeScript boolean set true if windowtitle script is to be included
     */
    protected void generatePackageIndexFile(String title, boolean includeScript) throws IOException {
        String windowOverview = configuration.getText(title);
        printHtmlHeader(windowOverview,
            configuration.metakeywords.getOverviewMetaKeywords(title,
                configuration.doctitle),
            includeScript);
        printNavigationBarHeader();
        printOverviewHeader();

        generateIndex();

        printOverview();

        printNavigationBarFooter();
        printBodyHtmlEnd();
    }

    /**
     * Default to no overview, overwrite to add overview.
     */
    protected void printOverview() throws IOException {
    }

    /**
     * Generate the frame or non-frame package index.
     */
    protected void generateIndex() {
        printIndexContents(packages, "doclet.Package_Summary");
    }

    /**
     * Generate code for package index contents. Call appropriate methods from
     * the sub-classes.
     *
     * @param packages Array of packages to be documented.
     * @param text     String which will be used as the heading.
     */
    protected void printIndexContents(PackageDoc[] packages, String text) {
        if (packages.length > 0) {
            Arrays.sort(packages);
            printIndexHeader(text);
            printAllClassesPackagesLink();
            for(int i = 0; i < packages.length; i++) {
                if (packages[i] != null) {
                    printIndexRow(packages[i]);
                }
            }
            printIndexFooter();
        }
    }

    /**
     * Print the doctitle, if it is specified on the command line.
     */
    protected void printConfigurationTitle() {
        if (configuration.doctitle.length() > 0) {
            center();
            h1(configuration.doctitle);
            centerEnd();
        }
    }

    /**
     * Highlight "Overview" in the bold format, in the navigation bar as this
     * is the overview page.
     */
    protected void navLinkContents() {
        navCellRevStart();
        fontStyle("NavBarFont1Rev");
        boldText("doclet.Overview");
        fontEnd();
        navCellEnd();
    }

    /**
     * Do nothing. This will be overridden in PackageIndexFrameWriter.
     */
    protected void printAllClassesPackagesLink() {
    }
}



