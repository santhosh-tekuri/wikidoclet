/*
 * @(#)PackageIndexWriter.java	1.38 05/11/30
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.formats.html;

import wikidoclet.doclets.internal.toolkit.util.*;
import com.sun.javadoc.*;
import java.io.*;
import java.util.*;

/**
 * Generate the package index page "overview-summary.html" for the right-hand
 * frame. A click on the package name on this page will update the same frame
 * with the "pacakge-summary.html" file for the clicked package.
 *
 * @author Atul M Dambalkar
 */
public class PackageIndexWriter extends AbstractPackageIndexWriter {

    /**
     * Root of the program structure. Used for "overview" documentation.
     */
    private RootDoc root;

    /**
     * Map representing the group of packages as specified on the command line.
     *
     * @see Group
     */
    private Map groupPackageMap;

    /**
     * List to store the order groups as specified on the command line.
     */
    private List groupList;

    /**
     * Construct the PackageIndexWriter. Also constructs the grouping
     * information as provided on the command line by "-group" option. Stores
     * the order of groups specified by the user.
     *
     * @see Group
     */
    public PackageIndexWriter(ConfigurationImpl configuration,
                              String filename)
                       throws IOException {
        super(configuration, filename);
        this.root = configuration.root;
        groupPackageMap = configuration.group.groupPackages(packages);
        groupList = configuration.group.getGroupList();
    }

    /**
     * Generate the package index page for the right-hand frame.
     *
     * @param configuration the current configuration of the doclet.
     */
    public static void generate(ConfigurationImpl configuration) {
        PackageIndexWriter packgen;
        String filename = "overview-summary.html";
        try {
            packgen = new PackageIndexWriter(configuration, filename);
            packgen.generatePackageIndexFile("doclet.Window_Overview_Summary", true);
            packgen.close();
        } catch (IOException exc) {
            configuration.standardmessage.error(
                        "doclet.exception_encountered",
                        exc.toString(), filename);
            throw new DocletAbortException();
        }
    }

    /**
     * Print each package in separate rows in the index table. Generate link
     * to each package.
     *
     * @param pkg Package to which link is to be generated.
     */
    protected void printIndexRow(PackageDoc pkg) {
        if(pkg != null && pkg.name().length() > 0) {
            trBgcolorStyle("white", "TableRowColor");
            summaryRow(20);
            bold();
            printPackageLink(pkg, Util.getPackageName(pkg), false);
            boldEnd();
            summaryRowEnd();
            summaryRow(0);
            printSummaryComment(pkg);
            summaryRowEnd();
            trEnd();
       }
    }

    /**
     * Depending upon the grouping information and their titles, generate
     * separate table indices for each package group.
     */
    protected void generateIndex() {
        for (int i = 0; i < groupList.size(); i++) {
        String groupname = (String)groupList.get(i);
        List list = (List)groupPackageMap.get(groupname);
            if (list != null && list.size() > 0) {
                printIndexContents((PackageDoc[])list.
                                       toArray(new PackageDoc[list.size()]),
                                    groupname);
            }
        }
    }

    /**
     * Print the overview summary comment for this documentation. Print one line
     * summary at the top of the page and generate a link to the description,
     * which is generated at the end of this page.
     */
    protected void printOverviewHeader() {
        if (root.inlineTags().length > 0) {
            printSummaryComment(root);
            p();
            bold(configuration.getText("doclet.See"));
            br();
            printNbsps();
            printHyperLink("", "overview_description",
                configuration.getText("doclet.Description"), true);
            p();
        }
    }

    /**
     * Print Html tags for the table for this package index.
     */
    protected void printIndexHeader(String text) {
        tableIndexSummary();
        tableHeaderStart("#CCCCFF");
        bold(text);
        tableHeaderEnd();
    }

    /**
     * Print Html closing tags for the table for this package index.
     */
    protected void printIndexFooter() {
        tableEnd();
        p();
        space();
    }

    /**
     * Print the overview comment as provided in the file specified by the
     * "-overview" option on the command line.
     */
    protected void printOverviewComment() {
        if (root.inlineTags().length > 0) {
            anchor("overview_description");
            p();
            printInlineComment(root);
            p();
        }
    }

    /**
     * Call {@link #printOverviewComment()} and then genrate the tag information
     * as provided in the file specified by the "-overview" option on the
     * command line.
     */
    protected void printOverview() throws IOException {
        printOverviewComment();
        printTags(root);
    }

    /**
     * Print the top text (from the -top option), the upper
     * navigation bar, and then the title (from the"-title"
     * option), at the top of page.
     */
    protected void printNavigationBarHeader() {
        printTop();
        navLinks(true);
        hr();
        printConfigurationTitle();
    }

    /**
     * Print the lower navigation bar and the bottom text
     * (from the -bottom option) at the bottom of page.
     */
    protected void printNavigationBarFooter() {
        hr();
        navLinks(false);
        printBottom();
    }
}



