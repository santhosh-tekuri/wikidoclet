/*
 * @(#)IndexBuilder.java	1.33 06/04/07
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.internal.toolkit.util;

import wikidoclet.doclets.internal.toolkit.*;
import com.sun.javadoc.*;
import java.util.*;

/**
 * Build the mapping of each Unicode character with it's member lists
 * containing members names starting with it. Also build a list for all the
 * Unicode characters which start a member name. Member name is
 * classkind or field or method or constructor name.
 *
 * This code is not part of an API.
 * It is implementation that is subject to change.
 * Do not use it as an API
 *
 * @since 1.2
 * @see java.lang.Character
 * @author Atul M Dambalkar
 */
public class IndexBuilder {

    /**
     * Mapping of each Unicode Character with the member list containing
     * members with names starting with it.
     */
    private Map indexmap = new HashMap();

    /**
     * Don't generate deprecated information if true.
     */
    private boolean noDeprecated;

    /**
     * Build this Index only for classes?
     */
    private boolean classesOnly;

    // make ProgramElementDoc[] when new toArray is available
    protected final Object[] elements;

    /**
     * A comparator used to sort classes and members.
     * Note:  Maybe this compare code belongs in the tool?
     */
    private class DocComparator implements Comparator {
        public int compare(Object d1, Object d2) {
            String doc1 = (((Doc) d1).name());
            String doc2 = (((Doc) d2).name());
            int compareResult;
            if ((compareResult = doc1.compareToIgnoreCase(doc2)) != 0) {
                return compareResult;
            } else if (d1 instanceof ProgramElementDoc && d2 instanceof ProgramElementDoc) {
                 doc1 = (((ProgramElementDoc) d1).qualifiedName());
                 doc2 = (((ProgramElementDoc) d2).qualifiedName());
                 return doc1.compareToIgnoreCase(doc2);
            } else {
                return 0;
            }
        }
    }

    /**
     * Constructor. Build the index map.
     *
     * @param configuration the current configuration of the doclet.
     * @param noDeprecated  true if -nodeprecated option is used,
     *                      false otherwise.
     */
    public IndexBuilder(Configuration configuration, boolean noDeprecated) {
        this(configuration, noDeprecated, false);
    }

    /**
     * Constructor. Build the index map.
     *
     * @param configuration the current configuration of the doclet.
     * @param noDeprecated  true if -nodeprecated option is used,
     *                      false otherwise.
     * @param classesOnly   Include only classes in index.
     */
    public IndexBuilder(Configuration configuration, boolean noDeprecated,
                        boolean classesOnly) {
        if (classesOnly) {
            configuration.message.notice("doclet.Building_Index_For_All_Classes");
        } else {
            configuration.message.notice("doclet.Building_Index");
        }
        this.noDeprecated = noDeprecated;
        this.classesOnly = classesOnly;
        buildIndexMap(configuration.root);
        Set set = indexmap.keySet();
        elements =  set.toArray();
        Arrays.sort(elements);
    }

    /**
     * Sort the index map. Traverse the index map for all it's elements and
     * sort each element which is a list.
     */
    protected void sortIndexMap() {
        for (Iterator it = indexmap.values().iterator(); it.hasNext(); ) {
            Collections.sort((List)it.next(), new DocComparator());
        }
    }

    /**
     * Get all the members in all the Packages and all the Classes
     * given on the command line. Form separate list of those members depending
     * upon their names.
     *
     * @param root Root of the documemt.
     */
    protected void buildIndexMap(RootDoc root)  {
        PackageDoc[] packages = root.specifiedPackages();
        ClassDoc[] classes = root.classes();
        if (!classesOnly) {
            if (packages.length == 0) {
                Set set = new HashSet();
                PackageDoc pd;
                for (int i = 0; i < classes.length; i++) {
                    pd = classes[i].containingPackage();
                    if (pd != null && pd.name().length() > 0) {
                        set.add(pd);
                    }
                }
                adjustIndexMap((PackageDoc[]) set.toArray(packages));
            } else {
                adjustIndexMap(packages);
            }
        }
        adjustIndexMap(classes);
        if (!classesOnly) {
            for (int i = 0; i < classes.length; i++) {
                if (shouldAddToIndexMap(classes[i])) {
                    putMembersInIndexMap(classes[i]);
                }
            }
        }
        sortIndexMap();
    }

    /**
     * Put all the members(fields, methods and constructors) in the classdoc
     * to the indexmap.
     *
     * @param classdoc ClassDoc whose members will be added to the indexmap.
     */
    protected void putMembersInIndexMap(ClassDoc classdoc) {
        adjustIndexMap(classdoc.fields());
        adjustIndexMap(classdoc.methods());
        adjustIndexMap(classdoc.constructors());
    }


    /**
     * Adjust list of members according to their names. Check the first
     * character in a member name, and then add the member to a list of members
     * for that particular unicode character.
     *
     * @param elements Array of members.
     */
    protected void adjustIndexMap(Doc[] elements) {
        for (int i = 0; i < elements.length; i++) {
            if (shouldAddToIndexMap(elements[i])) {
                String name = elements[i].name();
                char ch = (name.length()==0)?
                    '*' :
                    Character.toUpperCase(name.charAt(0));
                Character unicode = new Character(ch);
                List list = (List)indexmap.get(unicode);
                if (list == null) {
                    list = new ArrayList();
                    indexmap.put(unicode, list);
                }
                list.add(elements[i]);
            }
        }
    }

    /**
     * Should this doc element be added to the index map?
     */
    protected boolean shouldAddToIndexMap(Doc element) {
        return !(noDeprecated && element.tags("deprecated").length > 0);
    }

    /**
     * Return a map of all the individual member lists with Unicode character.
     *
     * @return Map index map.
     */
    public Map getIndexMap() {
        return indexmap;
    }

    /**
     * Return the sorted list of members, for passed Unicode Character.
     *
     * @param index index Unicode character.
     * @return List member list for specific Unicode character.
     */
    public List getMemberList(Character index) {
        return (List)indexmap.get(index);
    }

    /**
     * Array of IndexMap keys, Unicode characters.
     */
    public Object[] elements() {
        return elements;
    }
}
