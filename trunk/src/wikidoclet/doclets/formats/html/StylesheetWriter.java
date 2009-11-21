/*
 * @(#)StylesheetWriter.java	1.26 05/11/30
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.formats.html;

import wikidoclet.doclets.internal.toolkit.util.*;

import java.io.*;

/**
 * Writes the style sheet for the doclet output.
 *
 * @author Atul M Dambalkar
 */
public class StylesheetWriter extends HtmlDocletWriter {

    /**
     * Constructor.
     */
    public StylesheetWriter(ConfigurationImpl configuration,
                            String filename) throws IOException {
        super(configuration, filename);
    }

    /**
     * Generate the style file contents.
     * @throws DocletAbortException
     */
    public static void generate(ConfigurationImpl configuration) {
        StylesheetWriter stylegen;
        String filename = "";
        try {
            filename = "stylesheet.css";
            stylegen = new StylesheetWriter(configuration, filename);
            stylegen.generateStyleFile();
            stylegen.close();
        } catch (IOException exc) {
            configuration.standardmessage.error(
                        "doclet.exception_encountered",
                        exc.toString(), filename);
            throw new DocletAbortException();
        }
    }

    /**
     * Generate the style file contents.
     */
    protected void generateStyleFile() {
        print("/* "); printText("doclet.Style_line_1"); println(" */");
        println("");

        print("/* "); printText("doclet.Style_line_2"); println(" */");
        println("");

        print("/* "); printText("doclet.Style_line_3"); println(" */");
        println("body { background-color: #FFFFFF; color:#000000 }");
        println("");

        print("/* "); printText("doclet.Style_Headings"); println(" */");
        println("h1 { font-size: 145% }");
        println("");

        print("/* "); printText("doclet.Style_line_4"); println(" */");
        print(".TableHeadingColor     { background: #CCCCFF; color:#000000 }");
        print(" /* "); printText("doclet.Style_line_5"); println(" */");
        print(".TableSubHeadingColor  { background: #EEEEFF; color:#000000 }");
        print(" /* "); printText("doclet.Style_line_6"); println(" */");
        print(".TableRowColor         { background: #FFFFFF; color:#000000 }");
        print(" /* "); printText("doclet.Style_line_7"); println(" */");
        println("");

        print("/* "); printText("doclet.Style_line_8"); println(" */");
        println(".FrameTitleFont   { font-size: 100%; font-family: Helvetica, Arial, sans-serif; color:#000000 }");
        println(".FrameHeadingFont { font-size:  90%; font-family: Helvetica, Arial, sans-serif; color:#000000 }");
        println(".FrameItemFont    { font-size:  90%; font-family: Helvetica, Arial, sans-serif; color:#000000 }");
        println("");

       // Removed doclet.Style_line_9 as no longer needed

        print("/* "); printText("doclet.Style_line_10"); println(" */");
        print(".NavBarCell1    { background-color:#EEEEFF; color:#000000}");
        print(" /* "); printText("doclet.Style_line_6"); println(" */");
        print(".NavBarCell1Rev { background-color:#00008B; color:#FFFFFF}");
        print(" /* "); printText("doclet.Style_line_11"); println(" */");

        print(".NavBarFont1    { font-family: Arial, Helvetica, sans-serif; color:#000000;");
        println("color:#000000;}");
        print(".NavBarFont1Rev { font-family: Arial, Helvetica, sans-serif; color:#FFFFFF;");
        println("color:#FFFFFF;}");
        println("");

        print(".NavBarCell2    { font-family: Arial, Helvetica, sans-serif; ");
        println("background-color:#FFFFFF; color:#000000}");
        print(".NavBarCell3    { font-family: Arial, Helvetica, sans-serif; ");
        println("background-color:#FFFFFF; color:#000000}");
        println("");

    }

}



