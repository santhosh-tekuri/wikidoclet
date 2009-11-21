/**
 * Copyright (C) 2003  Jason Horman (jason@jhorman.org)
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 */

package wikidoclet;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * All of the work is done here. Performs the text substitutions to turn WIKI
 * strings into HTML. Most of the substitutions are straight regex replacements.
 * The list and table support are more complicated.
 *
 * @author  Jason Horman (jason@jhorman.org)
 */

public class WikiFormatter {

    /***************************************************
     *  Class variables
     ***************************************************/

    private static final Pattern boldPtn = Pattern.compile("\\*(.+?)\\*");
    private static final Pattern italicPtn = Pattern.compile("\\_(.+?)\\_");
    private static final Pattern boldItalicPtn = Pattern.compile("\\_\\_(.+?)\\_\\_");
    private static final Pattern boldFixedFontPtn = Pattern.compile("==(.+?)==");
    private static final Pattern fixedFontPtn = Pattern.compile("=(?![><\"])(.+?)=");
    private static final Pattern horizLinePtn = Pattern.compile("----+");

    private static final Pattern l4HeadPtn = Pattern.compile("---\\+\\+\\+\\+ ?([^\\n]+)\\r?\\n");
    private static final Pattern l3HeadPtn = Pattern.compile("---\\+\\+\\+ ?([^\\n]+)\\r?\\n");
    private static final Pattern l2HeadPtn = Pattern.compile("---\\+\\+ ?([^\\n]+)\\r?\\n");
    private static final Pattern l1HeadPtn = Pattern.compile("---\\+ ?([^\\n]+)\\r?\\n");
    private static final Pattern hrefPtn = Pattern.compile("\\[\\[(.+?)\\]\\[(.+?)\\]\\]\\r?\\n?");
    private static final Pattern verbatimPtn = Pattern.compile("<verbatim>");
    private static final Pattern verbatimEndPtn = Pattern.compile("</verbatim>");

    private static final Pattern bulletPtn = Pattern.compile("^(\\s+)([\\*\\d+])\\.?\\s([^\\n]+)");
    private static final Pattern indentedPtn = Pattern.compile("^(\\s+)([^\\n]+)");

    private static final Pattern tablePtn = Pattern.compile("^(\\s*)\\|");
    private static final Pattern tableCellPtn = Pattern.compile("(\\s*)(.+?)(\\s*)");

    /***************************************************
     *  Instance methods
     ***************************************************/

    /**
     * Override this function to execute wiki formatting.
     */
    public static String format(String commentStr) {
        // perform the simple substitutions.
        commentStr = boldItalicPtn.matcher(commentStr).replaceAll("<i><b>$1</b></i>");
        commentStr = italicPtn.matcher(commentStr).replaceAll("<i>$1</i>");
        commentStr = l4HeadPtn.matcher(commentStr).replaceAll("<h4>$1</h4>");
        commentStr = l3HeadPtn.matcher(commentStr).replaceAll("<h3>$1</h3>");
        commentStr = l2HeadPtn.matcher(commentStr).replaceAll("<h2>$1</h2>");
        commentStr = l1HeadPtn.matcher(commentStr).replaceAll("<h1>$1</h1>");
        commentStr = boldFixedFontPtn.matcher(commentStr).replaceAll("<code><b>$1</b></code>");
        commentStr = fixedFontPtn.matcher(commentStr).replaceAll("<code>$1</code>");
        commentStr = horizLinePtn.matcher(commentStr).replaceAll("<hr size=\"1\"/>");
        commentStr = hrefPtn.matcher(commentStr).replaceAll("<a href=\"$1\">$2</a>");
        commentStr = verbatimPtn.matcher(commentStr).replaceAll("<pre>");
        commentStr = verbatimEndPtn.matcher(commentStr).replaceAll("</pre>");

        // create bulleted lists
        commentStr = replaceBulletsAndNewlines(commentStr); // this replaces the \n's with <br/>\n's also

        // now replace bolds, after bullets are applied.
        commentStr = boldPtn.matcher(commentStr).replaceAll("<b>$1</b>");

        // create tables
        commentStr = replaceTables(commentStr);

        return commentStr;
    }

    /**
     * Searches for the bullet pattern and replaces it with ul|ol's and li's.
     * The bullet pattern is 3 spaces and a *.
     *
     *   * bullet 1
     *      * bullet 2
     *   * bullet 3
     *
     * Also does the replacement of newlines with =br= tags, and enables users
     * to use html tags inside of =pre/verbatim= blocks.
     *
     * NOTE: This has become a hack...
     */
    private static String replaceBulletsAndNewlines(String commentStr) {
        int lastBulletDepth = 0;
        String lastUolTag = null;
        int tabSize = 3;    // default tab size of 3

        StringBuffer newCommentStrBuf = new StringBuffer(commentStr.length()+100);
        String[] split = commentStr.split("\n");

        // keeps track of whether <pre> is on or not
        boolean preOn = false;

        for (int i = 0; i < split.length; i++) {
            String line = split[i];

            // check if pre mode is on. we won't write a "<br/> at the end if it is.
            if (line.indexOf("<pre>") != -1) {
                preOn = true;
            } else if (line.indexOf("</pre>") != -1) {
                preOn = false;
            }

            // check for the bullet pattern
            //   * bullet 1
            //      * bullet 2
            //   * bullet 3
            Matcher matcher = bulletPtn.matcher(line);
            if (line.length() > 0 && matcher.matches()) {
                lastUolTag = matcher.group(2).equals("*") ? "ul" : "ol";

                // the first bullet determines the tab size
                int depth = 0;
                if (lastBulletDepth == 0) {
                    tabSize = matcher.group(1).length();
                    depth = 1;
                } else {
                    depth = getIndentDepth(matcher.group(1), tabSize);
                }

                if (depth > lastBulletDepth) {
                    newCommentStrBuf.append("<" + lastUolTag + ">\n");
                } else if (depth < lastBulletDepth) {
                    newCommentStrBuf.append("</" + lastUolTag + ">\n");
                }

                newCommentStrBuf.append("\t<li>" + matcher.group(3) + "\n");
                lastBulletDepth = depth;

            } else {

                // bullet can span multiple lines so check and see if we are
                // still inside of a bullet tag.
                boolean stillInBullet = false;
                if (lastBulletDepth > 0) {
                    // if the trimed line is 0 we are probably still in the bullet since
                    // it probably is just a newline between bullets.
                    if (line.trim().length() > 0) {
                        Matcher indentedMatcher = indentedPtn.matcher(line);
                        if (indentedMatcher.matches()) {
                            int depth = getIndentDepth(indentedMatcher.group(1), tabSize);
                            if (depth >= lastBulletDepth) {
                                line = indentedMatcher.group(2);
                                stillInBullet = true;
                            }
                        }
                    } else {
                        stillInBullet = true;
                    }
                }

                if (!stillInBullet) {
                    // close each opened ul/ol
                    while(lastBulletDepth-- >= 0) {
                        newCommentStrBuf.append("</" + lastUolTag + ">\n");
                    }
                }

                if (!preOn) {
                    if (line.trim().length() > 0) {
                        newCommentStrBuf.append(line);
                    } else {
                        newCommentStrBuf.append("<p>");
                    }
                } else {
                    // allow users to type tags in pre blocks
                    line = line.replaceAll("&", "&amp;");
                    line = line.replaceAll("<(?!pre)", "&lt;");
                    line = line.replaceAll("(?<!pre)>", "&gt;");
                    newCommentStrBuf.append(line);
                }

                newCommentStrBuf.append("\n");
            }
        }

        // i may need to write out the last set of ol/ul
        while(lastBulletDepth-- >= 0) {
            newCommentStrBuf.append("</" + lastUolTag + ">\n");
        }

        return newCommentStrBuf.toString();
    }

    /**
     * Gets the indent depth from the leading spaces in the bulleted list
     * pattern and number list pattern. Bullets should be nested in increments
     * of 3 spaces or with tabs. If using spaces the length of the spaces
     * string is divided by 3 for the depth.
     */
    private static int getIndentDepth(String str, int tabSize) {
        return str.replaceAll(" {" + tabSize + "}", " ").length();
    }

    /**
     * Searches for the table pattern and replaces it with table tags. The table
     * pattern looks like this:
     *
     *   | h1 | h2 | h3 |
     *   | 1 |  2  |  3 |
     *
     * Aligns the <td> tags based on the whitespace in each column. More space
     * on the left means align left, more on the right, align right, equal align
     * center. If the 1st row has a bold element in it, it will become a header
     * row.
     *
     * The table has a html css "class" of wikitable to allow the user to override
     * the table styling.
     */
    private static String replaceTables(String commentStr) {
        StringBuffer newCommentStrBuf = new StringBuffer(commentStr.length()+100);
        String[] split = commentStr.split("\n");
        boolean inTable = false;
        for (int i = 0; i < split.length; i++) {
            String line = split[i];
            Matcher matcher = tablePtn.matcher(line);
            if (matcher.find()) {
                if (!inTable) {
                    boolean headerRow = line.indexOf("<b>") != -1; // bold in the 1st row means header
                    newCommentStrBuf.append("<table class=\"wikitable\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">\n");
                    if (headerRow) newCommentStrBuf.append("\t<thead bgcolor=\"#dddddd\">\n");
                    newCommentStrBuf.append("\t\t<tr>\n");

                    String[] tdata = getTableData(line);
                    for (int j = 0; j < tdata.length; j++) {
                        newCommentStrBuf.append(tdata[j]);
                    }

                    newCommentStrBuf.append("\t\t</tr>");
                    if (headerRow) newCommentStrBuf.append("\t</thead>\n");
                    newCommentStrBuf.append("\t<tbody>\n");
                    inTable = true;

                } else {
                    newCommentStrBuf.append("\t\t<tr>\n");

                    String[] tdata = getTableData(line);
                    for (int j = 0; j < tdata.length; j++) {
                        newCommentStrBuf.append(tdata[j]);
                    }

                    newCommentStrBuf.append("\t\t<tr>\n");
                }
            } else {
                if (inTable) {
                    newCommentStrBuf.append("\t</tbody>\n</table>\n");
                    inTable = false;
                }

                // don't need to re-append br, since bullet replacement did it
                newCommentStrBuf.append(line + "\n");
            }
        }

        if (inTable) {
            newCommentStrBuf.append("\t</tbody>\n</table>\n");
        }

        return newCommentStrBuf.toString();
    }

    /**
     * Formats and gets the contents of a row. Aligns the <td> tags based on the
     * whitespace in each column. More space on the left means align left, more
     * on the right, align right, equal align center.
     */
    private static String[] getTableData(String line) {
        line = line.substring(line.indexOf("|"), line.lastIndexOf("|"));
        String[] tdata = line.split("\\|");

        for (int i = 0; i < tdata.length; i++) {
            String tdataItem = tdata[i];
            Matcher matcher = tableCellPtn.matcher(tdataItem);
            if (matcher.matches()) {
                String beginSpacesStr = matcher.group(1);
                String endSpacesStr = matcher.group(3);
                int beginSpaces = beginSpacesStr != null ? beginSpacesStr.length() : 0;
                int endSpaces = endSpacesStr != null ? endSpacesStr.length() : 0;

                String alignment = beginSpaces == endSpaces ? "center" :
                                   beginSpaces > endSpaces ? "right" : "left";

                tdata[i] = "\t\t\t<td align=\"" + alignment + "\">" + matcher.group(2) + "</td>\n";
            }
        }

        return tdata;
    }
}