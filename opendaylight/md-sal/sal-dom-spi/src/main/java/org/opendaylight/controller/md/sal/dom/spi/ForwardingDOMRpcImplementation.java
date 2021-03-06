/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.md.sal.dom.spi;

import com.google.common.collect.ForwardingObject;
import com.google.common.util.concurrent.CheckedFuture;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.controller.md.sal.dom.api.DOMRpcException;
import org.opendaylight.controller.md.sal.dom.api.DOMRpcIdentifier;
import org.opendaylight.controller.md.sal.dom.api.DOMRpcImplementation;
import org.opendaylight.controller.md.sal.dom.api.DOMRpcResult;
import org.opendaylight.yangtools.yang.data.api.schema.NormalizedNode;

/**
 * Utility implementation which implements {@link DOMRpcImplementation} by forwarding it to
 * a backing delegate.
 *
 * @deprecated Use {@link org.opendaylight.mdsal.dom.spi.ForwardingDOMRpcImplementation} instead.
 */
@Deprecated
public abstract class ForwardingDOMRpcImplementation extends ForwardingObject implements DOMRpcImplementation {
    @Override
    protected abstract @NonNull DOMRpcImplementation delegate();

    @Override
    public CheckedFuture<DOMRpcResult, DOMRpcException> invokeRpc(final DOMRpcIdentifier type,
            final NormalizedNode<?, ?> input) {
        return delegate().invokeRpc(type, input);
    }
}
