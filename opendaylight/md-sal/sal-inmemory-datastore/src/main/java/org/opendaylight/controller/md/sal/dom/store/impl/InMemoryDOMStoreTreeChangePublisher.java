/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.md.sal.dom.store.impl;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.controller.md.sal.dom.api.DOMDataTreeChangeListener;
import org.opendaylight.controller.md.sal.dom.spi.AbstractDOMDataTreeChangeListenerRegistration;
import org.opendaylight.controller.sal.core.spi.data.AbstractDOMStoreTreeChangePublisher;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.util.concurrent.QueuedNotificationManager;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier;
import org.opendaylight.yangtools.yang.data.api.schema.NormalizedNode;
import org.opendaylight.yangtools.yang.data.api.schema.tree.DataTreeCandidate;
import org.opendaylight.yangtools.yang.data.api.schema.tree.DataTreeCandidateNode;
import org.opendaylight.yangtools.yang.data.api.schema.tree.DataTreeCandidates;
import org.opendaylight.yangtools.yang.data.api.schema.tree.DataTreeSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class InMemoryDOMStoreTreeChangePublisher extends AbstractDOMStoreTreeChangePublisher {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryDOMStoreTreeChangePublisher.class);

    private final QueuedNotificationManager<AbstractDOMDataTreeChangeListenerRegistration<?>, DataTreeCandidate>
            notificationManager;

    InMemoryDOMStoreTreeChangePublisher(final ExecutorService listenerExecutor, final int maxQueueSize) {
        notificationManager = QueuedNotificationManager.create(listenerExecutor, (listener, notifications) -> {
            // FIXME: we are not checking for listener being closed
            listener.getInstance().onDataTreeChanged(notifications);
        }, maxQueueSize,
                "DataTreeChangeListenerQueueMgr");
    }

    private InMemoryDOMStoreTreeChangePublisher(final QueuedNotificationManager<
            AbstractDOMDataTreeChangeListenerRegistration<?>, DataTreeCandidate> notificationManager) {
        this.notificationManager = notificationManager;
    }

    QueuedNotificationManager<AbstractDOMDataTreeChangeListenerRegistration<?>, DataTreeCandidate>
            getNotificationManager() {
        return notificationManager;
    }

    @Override
    protected void notifyListeners(final Collection<AbstractDOMDataTreeChangeListenerRegistration<?>> registrations,
            final YangInstanceIdentifier path, final DataTreeCandidateNode node) {
        final DataTreeCandidate candidate = DataTreeCandidates.newDataTreeCandidate(path, node);

        for (AbstractDOMDataTreeChangeListenerRegistration<?> reg : registrations) {
            LOG.debug("Enqueueing candidate {} to registration {}", candidate, registrations);
            notificationManager.submitNotification(reg, candidate);
        }
    }

    @Override
    protected synchronized void registrationRemoved(
            final AbstractDOMDataTreeChangeListenerRegistration<?> registration) {
        LOG.debug("Closing registration {}", registration);

        // FIXME: remove the queue for this registration and make sure we clear it
    }

    <L extends DOMDataTreeChangeListener> ListenerRegistration<L> registerTreeChangeListener(
            final YangInstanceIdentifier treeId, final L listener, final DataTreeSnapshot snapshot) {
        final AbstractDOMDataTreeChangeListenerRegistration<L> reg = registerTreeChangeListener(treeId, listener);

        final Optional<NormalizedNode<?, ?>> node = snapshot.readNode(YangInstanceIdentifier.EMPTY);
        if (node.isPresent()) {
            final DataTreeCandidate candidate = DataTreeCandidates.fromNormalizedNode(
                    YangInstanceIdentifier.EMPTY, node.get());

            InMemoryDOMStoreTreeChangePublisher publisher =
                    new InMemoryDOMStoreTreeChangePublisher(notificationManager);
            publisher.registerTreeChangeListener(treeId, listener);
            publisher.publishChange(candidate);
        }

        return reg;
    }

    synchronized void publishChange(final @NonNull DataTreeCandidate candidate) {
        // Runs synchronized with registrationRemoved()
        processCandidateTree(candidate);
    }
}
