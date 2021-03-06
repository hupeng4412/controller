/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.md.sal.common.api;

import java.util.EventListener;
import org.opendaylight.yangtools.concepts.Registration;

@Deprecated
public interface RegistrationListener<T extends Registration> extends EventListener {

    void onRegister(T registration);

    void onUnregister(T registration);
}
