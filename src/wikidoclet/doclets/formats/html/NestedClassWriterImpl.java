/*
 * @(#)NestedClassWriterImpl.java	1.33 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.formats.html;

import wikidoclet.doclets.internal.toolkit.*;
import wikidoclet.doclets.internal.toolkit.util.*;
import com.sun.javadoc.*;

import java.io.*;

/**
 * Writes nested class documentation in HTML format.
 *
 * @author Robert Field
 * @author Atul M Dambalkar
 * @author Jamie Ho (rewrite)
 */
public class NestedClassWriterImpl extends AbstractMemberWriter
    implements MemberSummaryWriter {

    private boolean printedSummaryHeader = false;

    public NestedClassWriterImpl(SubWriterHolderWriter writer,
            ClassDoc classdoc) {
        super(writer, classdoc);
    }

    public NestedClassWriterImpl(SubWriterHolderWriter writer) {
        super(writer);
    }

    /**
     * Write the classes summary header for the given class.
     *
     * @param classDoc the class the summary belongs to.
     */
    public void writeMemberSummaryHeader(ClassDoc classDoc) {
        printedSummaryHeader = true;
        writer.println("<!-- ======== NESTED CLASS SUMMARY ======== -->");
        writer.println();
        writer.printSummaryHeader(this, classDoc);
    }

    /**
     * Write the classes summary footer for the given class.
     *
     * @param classDoc the class the summary belongs to.
     */
    public void writeMemberSummaryFooter(ClassDoc classDoc) {
        writer.printSummaryFooter(this, classDoc);
    }

    /**
     * Write the inherited classes summary header for the given class.
     *
     * @param classDoc the class the summary belongs to.
     */
    public void writeInheritedMemberSummaryHeader(ClassDoc classDoc) {
        if(! printedSummaryHeader){
            //We don't want inherited summary to not be under heading.
            writeMemberSummaryHeader(classDoc);
            writeMemberSummaryFooter(classDoc);
            printedSummaryHeader = true;
        }
        writer.printInheritedSummaryHeader(this, classDoc);
    }

    /**
     * {@inheritDoc}
     */
    public void writeInheritedMemberSummary(ClassDoc classDoc,
            ProgramElementDoc nestedClass, boolean isFirst, boolean isLast) {
        writer.printInheritedSummaryMember(this, classDoc, nestedClass, isFirst);
    }

    /**
     * Write the inherited classes summary footer for the given class.
     *
     * @param classDoc the class the summary belongs to.
     */
    public void writeInheritedMemberSummaryFooter(ClassDoc classDoc) {
        writer.printInheritedSummaryFooter(this, classDoc);
        writer.println();
    }

    /**
     * Write the header for the nested class documentation.
     *
     * @param classDoc the class that the classes belong to.
     */
    public void writeHeader(ClassDoc classDoc, String header) {
        writer.anchor("nested class_detail");
        writer.printTableHeadingBackground(header);
    }

    /**
     * Write the nested class header for the given nested class.
     *
     * @param nestedClass the nested class being documented.
     * @param isFirst the flag to indicate whether or not the nested class is the
     *        first to be documented.
     */
    public void writeClassHeader(ClassDoc nestedClass, boolean isFirst) {
        if (! isFirst) {
            writer.printMemberHeader();
            writer.println("");
        }
        writer.anchor(nestedClass.name());
        writer.dl();
        writer.h3();
        writer.print(nestedClass.name());
        writer.h3End();
    }



    /**
     * Close the writer.
     */
    public void close() throws IOException {
        writer.close();
    }

    public int getMemberKind() {
        return VisibleMemberMap.INNERCLASSES;
    }

    public void printSummaryLabel(ClassDoc cd) {
        writer.boldText("doclet.Nested_Class_Summary");
    }

    public void printSummaryAnchor(ClassDoc cd) {
        writer.anchor("nested_class_summary");
    }

    public void printInheritedSummaryAnchor(ClassDoc cd) {
        writer.anchor("nested_classes_inherited_from_class_" +
                       cd.qualifiedName());
    }

    public void printInheritedSummaryLabel(ClassDoc cd) {
        String clslink = writer.getPreQualifiedClassLink(
            LinkInfoImpl.CONTEXT_MEMBER, cd, false);
        writer.bold();
        writer.printText(cd.isInterface() ?
            "doclet.Nested_Classes_Interface_Inherited_From_Interface" :
            "doclet.Nested_Classes_Interfaces_Inherited_From_Class",
            clslink);
        writer.boldEnd();
    }

    protected void writeSummaryLink(int context, ClassDoc cd, ProgramElementDoc member) {
        writer.bold();
        writer.printLink(new LinkInfoImpl(context, (ClassDoc)member, false));
        writer.boldEnd();
    }

    protected void writeInheritedSummaryLink(ClassDoc cd,
            ProgramElementDoc member) {
        writer.printLink(new LinkInfoImpl(LinkInfoImpl.CONTEXT_MEMBER,
            (ClassDoc)member, false));
    }

    protected void printSummaryType(ProgramElementDoc member) {
        ClassDoc cd = (ClassDoc)member;
        printModifierAndType(cd, null);
    }

    protected void printHeader(ClassDoc cd) {
        // N.A.
    }

    protected void printBodyHtmlEnd(ClassDoc cd) {
        // N.A.
    }

    protected void printMember(ProgramElementDoc member) {
        // N.A.
    }

    protected void writeDeprecatedLink(ProgramElementDoc member) {
        writer.printQualifiedClassLink(LinkInfoImpl.CONTEXT_MEMBER,
            (ClassDoc)member);
    }

    protected void printNavSummaryLink(ClassDoc cd, boolean link) {
        if (link) {
            writer.printHyperLink("", (cd == null) ? "nested_class_summary":
                    "nested_classes_inherited_from_class_" +
                cd.qualifiedName(),
                ConfigurationImpl.getInstance().getText("doclet.navNested"));
        } else {
            writer.printText("doclet.navNested");
        }
    }

    protected void printNavDetailLink(boolean link) {
    }

    protected void printMemberLink(ProgramElementDoc member) {
    }

    protected void printMembersSummaryLink(ClassDoc cd, ClassDoc icd,
                                           boolean link) {
        if (link) {
            writer.printHyperLink(cd.name() + ".html",
                (cd == icd)?
                    "nested_class_summary":
                    "nested_classes_inherited_from_class_" +
                    icd.qualifiedName(),
                    ConfigurationImpl.getInstance().getText(
                        "doclet.Nested_Class_Summary"));
        } else {
            writer.printText("doclet.Nested_Class_Summary");
        }
    }
}


