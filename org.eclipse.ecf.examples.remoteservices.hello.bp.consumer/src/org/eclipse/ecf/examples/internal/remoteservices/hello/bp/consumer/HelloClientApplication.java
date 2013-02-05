/*******************************************************************************
 * Copyright (c) 2013 Konstantinos Mavridis - All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 1)	Konstantinos Mavridis - implementation of Blueprint-based activation 
 * 2)	Composent, Inc. - initial API and implementation of 
 *   		   org.eclipse.ecf.examples.internal.remoteservices.hello.ds.consumer
 *******************************************************************************/
package org.eclipse.ecf.examples.internal.remoteservices.hello.bp.consumer;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class HelloClientApplication implements IApplication {

	Object syncObj = new Object();
	boolean completed = false;

	public Object start(IApplicationContext context) {
		synchronized (syncObj) {
			while (!completed) {
				try {
					syncObj.wait();
				} catch (InterruptedException e) {
					// Catch the interrupt and do nothing.
				}
			}
		}
		return IApplication.EXIT_OK;
	}

	public void stop() {
		synchronized (syncObj) {
			completed = true;
			syncObj.notifyAll();
		}
	}
}