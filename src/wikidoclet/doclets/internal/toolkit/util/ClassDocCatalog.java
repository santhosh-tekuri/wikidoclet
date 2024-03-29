/*
 * @(#)ClassDocCatalog.java	1.14 06/04/07
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.internal.toolkit.util;

import com.sun.javadoc.*;
import java.util.*;

/**
 * This class acts as an artificial PackageDoc for classes specified
 * on the command line when running Javadoc.  For example, if you
 * specify several classes from package java.lang, this class will catalog
 * those classes so that we can retrieve all of the classes from a particular
 * package later.
 *
 * This code is not part of an API.
 * It is implementation that is subject to change.
 * Do not use it as an API
 *
 * @author Jamie Ho
 * @since 1.4
 */

 public class ClassDocCatalog {

     /**
      * Stores the set of packages that the classes specified on the command line
      * belong to.  Note that the default package is "".
      */
     private Set packageSet;


     /**
      * Stores all classes for each package
      */
     private Map allClasses;

     /**
      * Stores ordinary classes (excluding Exceptions and Errors) for each
      * package
      */
     private Map ordinaryClasses;

     /**
      * Stores exceptions for each package
      */
     private Map exceptions;

    /**
     * Stores enums for each package.
     */
    private Map enums;

    /**
     * Stores annotation types for each package.
     */
    private Map annotationTypes;

     /**
      * Stores errors for each package
      */
     private Map errors;

     /**
      * Stores interfaces for each package
      */
     private Map interfaces;

     /**
      * Construct a new ClassDocCatalog.
      *
      * @param classdocs the array of ClassDocs to catalog
      */
     public ClassDocCatalog (ClassDoc[] classdocs) {
         init();
         for (int i = 0; i < classdocs.length; i++) {
             addClassDoc(classdocs[i]);
         }
     }

     /**
      * Construct a new ClassDocCatalog.
      *
      */
     public ClassDocCatalog () {
         init();
     }

     private void init() {
         allClasses = new HashMap();
         ordinaryClasses = new HashMap();
         exceptions = new HashMap();
         enums = new HashMap();
         annotationTypes = new HashMap();
         errors = new HashMap();
         interfaces = new HashMap();
         packageSet = new HashSet();
     }

     /**
      * Add the given class to the catalog.
      * @param classdoc the ClassDoc to add to the catelog.
      */
      public void addClassDoc(ClassDoc classdoc) {
        if (classdoc == null) {
            return;
        }
        addClass(classdoc, allClasses);
        if (classdoc.isOrdinaryClass()) {
            addClass(classdoc, ordinaryClasses);
        } else if (classdoc.isException()) {
            addClass(classdoc, exceptions);
        } else if (classdoc.isEnum()) {
            addClass(classdoc, enums);
        } else if (classdoc.isAnnotationType()) {
            addClass(classdoc, annotationTypes);
        } else if (classdoc.isError()) {
            addClass(classdoc, errors);
        } else if (classdoc.isInterface()) {
            addClass(classdoc, interfaces);
        }
      }

      /**
       * Add the given class to the given map.
       * @param classdoc the ClassDoc to add to the catelog.
       * @param map the Map to add the ClassDoc to.
       */
      private void addClass(ClassDoc classdoc, Map map) {

          PackageDoc pkg = classdoc.containingPackage();
          if (pkg.isIncluded()) {
              //No need to catalog this class since it's package is
              //included on the command line
              return;
          }
          String key = Util.getPackageName(pkg);
          Set s = (Set) map.get(key);
          if (s == null) {
              packageSet.add(key);
              s = new HashSet();
          }
          s.add(classdoc);
          map.put(key, s);

      }

      private ClassDoc[] getArray(Map m, String key) {
          Set s = (Set) m.get(key);
          if (s == null) {
              return new ClassDoc[] {};
          } else {
              return (ClassDoc[]) s.toArray(new ClassDoc[] {});
          }
      }

      /**
       * Return all of the classes specified on the command-line that
       * belong to the given package.
       * @param packageDoc the package to return the classes for.
       */
      public ClassDoc[] allClasses(PackageDoc pkgDoc) {
          return pkgDoc.isIncluded() ?
                pkgDoc.allClasses() :
                getArray(allClasses, Util.getPackageName(pkgDoc));
      }

      /**
       * Return all of the classes specified on the command-line that
       * belong to the given package.
       * @param packageName the name of the package specified on the
       * command-line.
       */
      public ClassDoc[] allClasses(String packageName) {
          return getArray(allClasses, packageName);
      }

     /**
      * Return the array of package names that this catalog stores
      * ClassDocs for.
      */
     public String[] packageNames() {
         return (String[]) packageSet.toArray(new String[] {});
     }

     /**
      * Return true if the given package is known to this catalog.
      * @param packageName the name to check.
      * @return true if this catalog has any information about
      * classes in the given package.
      */
     public boolean isKnownPackage(String packageName) {
         return packageSet.contains(packageName);
     }


      /**
       * Return all of the errors specified on the command-line
       * that belong to the given package.
       * @param packageName the name of the package specified on the
       * command-line.
       */
      public ClassDoc[] errors(String packageName) {
          return getArray(errors, packageName);
      }

      /**
       * Return all of the exceptions specified on the command-line
       * that belong to the given package.
       * @param packageName the name of the package specified on the
       * command-line.
       */
      public ClassDoc[] exceptions(String packageName) {
          return getArray(exceptions, packageName);
      }

      /**
       * Return all of the enums specified on the command-line
       * that belong to the given package.
       * @param packageName the name of the package specified on the
       * command-line.
       */
      public ClassDoc[] enums(String packageName) {
          return getArray(enums, packageName);
      }

      /**
       * Return all of the annotation types specified on the command-line
       * that belong to the given package.
       * @param packageName the name of the package specified on the
       * command-line.
       */
      public ClassDoc[] annotationTypes(String packageName) {
          return getArray(annotationTypes, packageName);
      }

      /**
       * Return all of the interfaces specified on the command-line
       * that belong to the given package.
       * @param packageName the name of the package specified on the
       * command-line.
       */
      public ClassDoc[] interfaces(String packageName) {
          return getArray(interfaces, packageName);
      }

      /**
       * Return all of the ordinary classes specified on the command-line
       * that belong to the given package.
       * @param packageName the name of the package specified on the
       * command-line.
       */
      public ClassDoc[] ordinaryClasses(String packageName) {
          return getArray(ordinaryClasses, packageName);
      }
}
