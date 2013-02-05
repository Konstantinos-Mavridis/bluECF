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
 *   			  org.eclipse.ecf.examples.internal.remoteservices.hello.ds.host
 *******************************************************************************/
package org.eclipse.ecf.examples.internal.remoteservices.hello.bp.host;

import org.eclipse.ecf.examples.remoteservices.hello.HelloMessage;
import org.eclipse.ecf.examples.remoteservices.hello.IHello;

public class HelloBean implements IHello {
	// Implementation of the IHello service

	/*
	 * This method can be executed via remote proxies.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.examples.remoteservices.hello.IHello#hello(java.lang.
	 * String)
	 */
	public String hello(String callerMessage) {
		System.out.println("Received hello from caller " + callerMessage + "\n");
		return "Acknowledgement from service host - " + callerMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.examples.remoteservices.hello.IHello#helloMessage(org
	 * .eclipse.ecf.examples.remoteservices.hello.HelloMessage)
	 */
	public String helloMessage(HelloMessage message) {
		System.out.println("Received hello message " + message + "\n");
		return "Acknowledgement from service host - " + message.getFrom();
	}
}