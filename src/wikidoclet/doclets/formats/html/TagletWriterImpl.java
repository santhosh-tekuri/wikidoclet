/*
 * @(#)TagletWriterImpl.java	1.14 05/11/30
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.formats.html;

import wikidoclet.doclets.internal.toolkit.*;
import wikidoclet.doclets.internal.toolkit.builders.SerializedFormBuilder;
import wikidoclet.doclets.internal.toolkit.taglets.*;
import wikidoclet.doclets.internal.toolkit.util.*;
import com.sun.javadoc.*;

/**
 * The taglet writer that writes HTML.
 *
 * @since 1.5
 * @author Jamie Ho
 */

public class TagletWriterImpl extends TagletWriter {

    private HtmlDocletWriter htmlWriter;

    public TagletWriterImpl(HtmlDocletWriter htmlWriter, boolean isFirstSentence) {
        this.htmlWriter = htmlWriter;
        this.isFirstSentence = isFirstSentence;
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput getOutputInstance() {
        return new TagletOutputImpl("");
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput getDocRootOutput() {
        return new TagletOutputImpl(htmlWriter.relativepathNoSlash);
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput deprecatedTagOutput(Doc doc) {
        StringBuffer output = new StringBuffer();
        Tag[] deprs = doc.tags("deprecated");
        if (doc instanceof ClassDoc) {
            if (Util.isDeprecated((ProgramElementDoc) doc)) {
                output.append("<B>" +
                    ConfigurationImpl.getInstance().
                        getText("doclet.Deprecated") + "</B>&nbsp;");
                if (deprs.length > 0) {
                    Tag[] commentTags = deprs[0].inlineTags();
                    if (commentTags.length > 0) {

                        output.append(commentTagsToOutput(null, doc,
                            deprs[0].inlineTags(), false).toString()
                        );
                    }
                }
                output.append("<p>");
            }
        } else {
            MemberDoc member = (MemberDoc) doc;
            if (Util.isDeprecated((ProgramElementDoc) doc)) {
                output.append("<DD><B>" +
                    ConfigurationImpl.getInstance().
                            getText("doclet.Deprecated") + "</B>&nbsp;");
                if (deprs.length > 0) {
                    output.append("<I>");
                    output.append(commentTagsToOutput(null, doc,
                        deprs[0].inlineTags(), false).toString());
                    output.append("</I>");
                }
                if (member instanceof ExecutableMemberDoc) {
                    output.append(DocletConstants.NL + "<P>" +
                        DocletConstants.NL);
                }
            } else {
                if (Util.isDeprecated(member.containingClass())) {
                    output.append("<DD><B>" +
                    ConfigurationImpl.getInstance().
                            getText("doclet.Deprecated") + "</B>&nbsp;");
                }
            }
        }
        return new TagletOutputImpl(output.toString());
    }

    /**
     * {@inheritDoc}
     */
    public MessageRetriever getMsgRetriever() {
        return htmlWriter.configuration.message;
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput getParamHeader(String header) {
        StringBuffer result = new StringBuffer();
        result.append("<DT>");
        result.append("<B>" +  header + "</B>");
        return new TagletOutputImpl(result.toString());
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput paramTagOutput(ParamTag paramTag, String paramName) {
        TagletOutput result = new TagletOutputImpl("<DD><CODE>" + paramName + "</CODE>"
         + " - " + htmlWriter.commentTagsToString(paramTag, null, paramTag.inlineTags(), false));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput returnTagOutput(Tag returnTag) {
        TagletOutput result = new TagletOutputImpl(DocletConstants.NL + "<DT>" +
            "<B>" + htmlWriter.configuration.getText("doclet.Returns") +
            "</B>" + "<DD>" +
            htmlWriter.commentTagsToString(returnTag, null, returnTag.inlineTags(),
            false));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput seeTagOutput(Doc holder, SeeTag[] seeTags) {
        String result = "";
        if (seeTags.length > 0) {
            result = addSeeHeader(result);
            for (int i = 0; i < seeTags.length; ++i) {
                if (i > 0) {
                    result += ", " + DocletConstants.NL;
                }
                result += htmlWriter.seeTagToString(seeTags[i]);
            }
        }
        if (holder.isField() && ((FieldDoc)holder).constantValue() != null &&
                htmlWriter instanceof ClassWriterImpl) {
            //Automatically add link to constant values page for constant fields.
            result = addSeeHeader(result);
            result += htmlWriter.getHyperLink(htmlWriter.relativePath +
                ConfigurationImpl.CONSTANTS_FILE_NAME
                + "#" + ((ClassWriterImpl) htmlWriter).getClassDoc().qualifiedName()
                + "." + ((FieldDoc) holder).name(),
                htmlWriter.configuration.getText("doclet.Constants_Summary"));
        }
        if (holder.isClass() && ((ClassDoc)holder).isSerializable()) {
            //Automatically add link to serialized form page for serializable classes.
            if (!(SerializedFormBuilder.serialInclude(holder) &&
                      SerializedFormBuilder.serialInclude(((ClassDoc)holder).containingPackage()))) {
                return result.equals("") ? null : new TagletOutputImpl(result);
            }
            result = addSeeHeader(result);
            result += htmlWriter.getHyperLink(htmlWriter.relativePath + "serialized-form.html",
                ((ClassDoc)holder).qualifiedName(), htmlWriter.configuration.getText("doclet.Serialized_Form"), false);
        }
        return result.equals("") ? null : new TagletOutputImpl(result);
    }

    private String addSeeHeader(String result) {
        if (result != null && result.length() > 0) {
            return result + ", " + DocletConstants.NL;
        } else {
            return "<DT><B>" + htmlWriter.configuration().getText("doclet.See_Also") + "</B><DD>";
        }
     }

    /**
     * {@inheritDoc}
     */
    public TagletOutput simpleTagOutput(Tag[] simpleTags, String header) {
        String result = "<DT><B>" + header + "</B></DT>" + DocletConstants.NL +
            "  <DD>";
        for (int i = 0; i < simpleTags.length; i++) {
            if (i > 0) {
                result += ", ";
            }
            result += htmlWriter.commentTagsToString(simpleTags[i], null, simpleTags[i].inlineTags(), false);
        }
         return new TagletOutputImpl(result + "</DD>" + DocletConstants.NL);
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput simpleTagOutput(Tag simpleTag, String header) {
        return new TagletOutputImpl("<DT><B>" + header + "</B></DT>" + "  <DD>"
            + htmlWriter.commentTagsToString(simpleTag, null, simpleTag.inlineTags(), false)
            + "</DD>" + DocletConstants.NL);
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput getThrowsHeader() {
        return new TagletOutputImpl(DocletConstants.NL + "<DT>" + "<B>" +
            htmlWriter.configuration().getText("doclet.Throws") + "</B>");
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput throwsTagOutput(ThrowsTag throwsTag) {
        String result = DocletConstants.NL + "<DD>";
        result += throwsTag.exceptionType() == null ?
            htmlWriter.codeText(throwsTag.exceptionName()) :
            htmlWriter.codeText(
                htmlWriter.getLink(new LinkInfoImpl(LinkInfoImpl.CONTEXT_MEMBER,
                throwsTag.exceptionType())));
        TagletOutput text = new TagletOutputImpl(
            htmlWriter.commentTagsToString(throwsTag, null,
            throwsTag.inlineTags(), false));
        if (text != null && text.toString().length() > 0) {
            result += " - " + text;
        }
        return new TagletOutputImpl(result);
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput throwsTagOutput(Type throwsType) {
        return new TagletOutputImpl(DocletConstants.NL + "<DD>" +
            htmlWriter.codeText(htmlWriter.getLink(
                new LinkInfoImpl(LinkInfoImpl.CONTEXT_MEMBER, throwsType))));
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput valueTagOutput(FieldDoc field, String constantVal,
            boolean includeLink) {
        return new TagletOutputImpl(includeLink ?
            htmlWriter.getDocLink(LinkInfoImpl.CONTEXT_VALUE_TAG, field,
                constantVal, false) : constantVal);
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput commentTagsToOutput(Tag holderTag, Tag[] tags) {
        return commentTagsToOutput(holderTag, null, tags, false);
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput commentTagsToOutput(Doc holderDoc, Tag[] tags) {
        return commentTagsToOutput(null, holderDoc, tags, false);
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput commentTagsToOutput(Tag holderTag,
        Doc holderDoc, Tag[] tags, boolean isFirstSentence) {
        return new TagletOutputImpl(htmlWriter.commentTagsToString(
            holderTag, holderDoc, tags, isFirstSentence));
    }

    /**
     * {@inheritDoc}
     */
    public Configuration configuration() {
        return htmlWriter.configuration();
    }

    /**
     * Return an instance of a TagletWriter that knows how to write HTML.
     *
     * @return an instance of a TagletWriter that knows how to write HTML.
     */
    public TagletOutput getTagletOutputInstance() {
        return new TagletOutputImpl("");
    }
}

