/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class PageTreeTraversal {

    public static final void traversePageTree(
            final Composite root,
            final Predicate<Control> predicate,
            final Consumer<Control> f) {

        if (predicate.test(root)) {
            f.accept(root);
        }

        final Control[] children = root.getChildren();
        if (children != null) {
            for (final Control control : children) {
                if (!(control instanceof Composite)) {
                    if (predicate.test(control)) {
                        f.accept(control);
                    }
                } else {
                    traversePageTree((Composite) control, predicate, f);
                }
            }
        }
    }

}
