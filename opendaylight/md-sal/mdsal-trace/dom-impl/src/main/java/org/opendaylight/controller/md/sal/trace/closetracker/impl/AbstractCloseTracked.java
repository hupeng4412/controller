/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.md.sal.trace.closetracker.impl;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Convenience abstract base class for {@link CloseTracked} implementors.
 *
 * @author Michael Vorburger.ch
 */
@Deprecated
public abstract class AbstractCloseTracked<T extends AbstractCloseTracked<T>> implements CloseTracked<T> {

    private final CloseTrackedTrait<T> closeTracker;

    protected AbstractCloseTracked(CloseTrackedRegistry<T> transactionChainRegistry) {
        this.closeTracker = new CloseTrackedTrait<>(transactionChainRegistry, this);
    }

    protected void removeFromTrackedRegistry() {
        closeTracker.removeFromTrackedRegistry();
    }

    @Override
    public @Nullable StackTraceElement[] getAllocationContextStackTrace() {
        return closeTracker.getAllocationContextStackTrace();
    }

    @Override
    public final CloseTracked<T> getRealCloseTracked() {
        return this;
    }
}
