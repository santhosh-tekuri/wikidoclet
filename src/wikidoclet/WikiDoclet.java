/**
 * Copyright (C) 2003  Santhosh Kumar T (santhosh.tekuri@gmail.com)
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

import com.sun.javadoc.RootDoc;
import wikidoclet.doclets.internal.toolkit.util.DocletAbortException;
import wikidoclet.doclets.standard.Standard;
import wikidoclet.doclets.formats.html.HtmlDoclet;

import java.io.IOException;

/**
 * @author Santhosh Kumar T (santhosh.tekuri@gmail.com)
 */
public class WikiDoclet extends HtmlDoclet{
    public static boolean start(RootDoc root) {
        try{
            WikiDoclet doclet = new WikiDoclet();
            return doclet.start(doclet, root);
        }catch(Exception ex){
            ex.printStackTrace();
            throw (RuntimeException)ex;
        }
    }
}