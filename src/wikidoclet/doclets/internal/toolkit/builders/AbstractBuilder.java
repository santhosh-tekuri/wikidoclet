/*
 * @(#)AbstractBuilder.java	1.7 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.internal.toolkit.builders;

import wikidoclet.doclets.internal.toolkit.*;
import wikidoclet.doclets.internal.toolkit.util.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * The superclass for all builders.  A builder is a class that provides
 * the structure and content of API documentation.  A builder is completely
 * doclet independent which means that any doclet can use builders to
 * construct documentation, as long as it impelements the appropriate
 * writer interfaces.  For example, if a doclet wanted to use
 * {@link ConstantsSummaryBuilder} to build a constant summary, all it has to
 * do is implement the ConstantsSummaryWriter interface and pass it to the
 * builder using a WriterFactory.
 *
 * This code is not part of an API.
 * It is implementation that is subject to change.
 * Do not use it as an API
 *
 * @author Jamie Ho
 * @since 1.5
 */

public abstract class AbstractBuilder {

    /**
     * The configuration used in this run of the doclet.
     */
    protected Configuration configuration;

    /**
     * Keep track of which packages we have seen for
     * efficiency purposes.  We don't want to copy the
     * doc files multiple times for a single package.
     */
    protected static Set containingPackagesSeen;

    /**
     * True if we want to print debug output.
     */
    protected static final boolean DEBUG = false;

    /**
     * Construct a Builder.
     * @param configuration the configuration used in this run
     *        of the doclet.
     */
    public AbstractBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Return the name of this builder.
     *
     * @return the name of the builder.
     */
    public abstract String getName();

    /**
     * Build the documentation.
     *
     * @throws IOException there was a problem writing the output.
     */
    public abstract void build() throws IOException;

    /**
     * Build the documentation, as specified by the given XML elements.
     *
     * @param elements the XML elements that specify which components to
     *                 document.
     */
    protected void build(List elements) {
        for (int i = 0; i < elements.size(); i++ ) {
            Object element = elements.get(i);
            String component = (String)
                ((element instanceof String) ?
                     element :
                    ((List) element).get(0));
            try {
                invokeMethod("build" + component,
                    element instanceof String ?
                        new Class[] {} :
                        new Class[] {List.class},
                    element instanceof String ?
                        new Object[] {} :
                        new Object[] {((List) element).subList(1,
                            ((List) element).size())});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                configuration.root.printError("Unknown element: " + component);
                throw new DocletAbortException();
            } catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                configuration.root.printError("Exception " +
                    e.getClass().getName() +
                    " thrown while processing element: " + component);
                throw new DocletAbortException();
            }
        }
    }

    /**
     * Given the name and parameters, invoke the method in the builder.  This
     * method is required to invoke the appropriate build method as instructed
     * by the builder XML file.
     *
     * @param methodName   the name of the method that we would like to invoke.
     * @param paramClasses the types for each parameter.
     * @param params       the parameters of the method.
     */
    protected abstract void invokeMethod(String methodName, Class[] paramClasses,
            Object[] params)
    throws Exception;
}
