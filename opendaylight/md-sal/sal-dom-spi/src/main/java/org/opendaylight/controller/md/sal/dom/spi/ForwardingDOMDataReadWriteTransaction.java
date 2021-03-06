/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.md.sal.dom.spi;

import com.google.common.base.Optional;
import com.google.common.collect.ForwardingObject;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.FluentFuture;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.dom.api.DOMDataReadWriteTransaction;
import org.opendaylight.mdsal.common.api.CommitInfo;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier;
import org.opendaylight.yangtools.yang.data.api.schema.NormalizedNode;

/**
 * Utility {@link DOMDataReadWriteTransaction} implementation which forwards all interface
 * method invocation to a delegate instance.
 *
 * @deprecated Use {@link org.opendaylight.mdsal.dom.spi.ForwardingDOMDataReadWriteTransaction} instead.
 */
@Deprecated
public abstract class ForwardingDOMDataReadWriteTransaction extends ForwardingObject
        implements DOMDataReadWriteTransaction {
    @Override
    protected abstract @NonNull DOMDataReadWriteTransaction delegate();

    @Override
    public CheckedFuture<Optional<NormalizedNode<?, ?>>, ReadFailedException> read(final LogicalDatastoreType store,
            final YangInstanceIdentifier path) {
        return delegate().read(store, path);
    }

    @Override
    public CheckedFuture<Boolean, ReadFailedException> exists(final LogicalDatastoreType store,
            final YangInstanceIdentifier path) {
        return delegate().exists(store, path);
    }

    @Override
    public Object getIdentifier() {
        return delegate().getIdentifier();
    }

    @Override
    public void put(final LogicalDatastoreType store, final YangInstanceIdentifier path,
            final NormalizedNode<?, ?> data) {
        delegate().put(store, path, data);
    }

    @Override
    public void merge(final LogicalDatastoreType store, final YangInstanceIdentifier path,
            final NormalizedNode<?, ?> data) {
        delegate().merge(store, path, data);
    }

    @Override
    public boolean cancel() {
        return delegate().cancel();
    }

    @Override
    public void delete(final LogicalDatastoreType store, final YangInstanceIdentifier path) {
        delegate().delete(store, path);
    }

    @Override
    public FluentFuture<? extends CommitInfo> commit() {
        return delegate().commit();
    }
}
