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
 *   		  org.eclipse.ecf.examples.internal.remoteservices.hello.ds.consumer
 *******************************************************************************/
package org.eclipse.ecf.examples.internal.remoteservices.hello.bp.consumer;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ecf.examples.remoteservices.hello.IHello;
import org.eclipse.ecf.examples.remoteservices.hello.IHelloAsync;
import org.eclipse.ecf.remoteservice.IAsyncCallback;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceProxy;
import org.eclipse.ecf.remoteservice.RemoteServiceHelper;
import org.eclipse.equinox.concurrent.future.IFuture;

public class HelloClientBean {
	private static final String CLASSNAME = "org.eclipse.ecf.examples.internal.remoteservices.hello.bp.consumer.HelloClientBean";

	/**
	 * Binding method to be used with a referenced-interface implementation
	 * proxy injected by the activation framework (e.g. Gemini Blueprint).
	 * 
	 * @param svcproxy
	 */
	public void bindHello(IHello svcproxy) {
		// Print out a string representation of the injected proxy instance
		System.out.println("\n" + "Injected IHello implementation proxy is " + svcproxy);

		syncInvokeSvcProxy(svcproxy);
		sleep();
		asyncInvokeRemoteSvc(svcproxy);
		sleep();
		asyncInvokeSvcCallback(svcproxy);
		sleep();
		asyncInvokeSvcFuture(svcproxy);
	}

	/**
	 * Perform synchronous remote call via proxy.
	 * 
	 * Blocking method due to synchronous communication with remote service.
	 * 
	 * @param proxy
	 */
	private void syncInvokeSvcProxy(IHello proxy) {
		System.out.println("\n" + "ENTERED SYNCHRONOUS REMOTE CALL VIA PROXY");

		System.out.println("Initiating invocation of synchronous remote call via proxy instance...");
		proxy.hello(CLASSNAME + " using a synchronous remote call via proxy");
		System.out.println("Completed invocation of synchronous remote call via proxy instance. Check service host for details.");

		System.out.println("EXITING SYNCHRONOUS REMOTE CALL VIA PROXY" + "\n");
	}

	/**
	 * Perform asynchronous remote call via Future derived from proxy.
	 * 
	 * Non-blocking remote service invocation storing the result inside a Future
	 * placeholder.
	 * 
	 * @param remotesvc
	 */
	private void asyncInvokeRemoteSvc(IHello svcproxy) {
		System.out.println("ENTERED REMOTE CALL VIA FUTURE");
		IRemoteService remotesvc;
		try {
			// Retrieve the remote service instance from the proxy
			remotesvc = ((IRemoteServiceProxy) svcproxy).getRemoteService();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("EXITING REMOTE CALL VIA FUTURE DUE TO EXCEPTION UPON CREATION OF IRemoteService" + "\n");
			return;
		}

		System.out.println("Initiating invocation of remote call via future...");
		IFuture remotesvcFuture = RemoteServiceHelper.futureExec(remotesvc, "hello", new Object[] { CLASSNAME
				+ " using an asynchronous remote call via future." });
		System.out.println("Completed invocation of remote call via future. Check service host for details." + "\n");
		try {
			while (!remotesvcFuture.isDone()) {
				// Dummy blocking to demonstrate client executing background
				// logic while waiting for service response
				System.out.println("Waiting for remote service response via future...");
				sleep();
			}
			// Once the remote service has replied the future is filled and the
			// response shall be processed
			Object response = remotesvcFuture.get();
			System.out.println("Successfully received remote service response via future: " + response + "\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("EXITING REMOTE CALL VIA FUTURE DUE TO EXCEPTION" + "\n");
		}
		System.out.println("EXITING REMOTE CALL VIA FUTURE" + "\n");
	}

	/**
	 * Perform asynchronous remote call via callback.
	 * 
	 * With ECF >=3.3 remote services, when a proxy is created, if an interface
	 * class with the name [fq service interface name]Async can be loaded and it
	 * extends IAsyncRemoteServiceProxy then the proxy will implement that
	 * interface.
	 * 
	 * @param svcproxy
	 */
	private void asyncInvokeSvcCallback(IHello svcproxy) {
		// Provided that the IHelloAsync interface was available during proxy
		// creation, the created service proxy shall implement it.
		if (svcproxy instanceof IHelloAsync) {
			System.out.println("ENTERED ASYNCHRONOUS REMOTE CALL VIA CALLBACK");

			IHelloAsync helloAsyncImpl = (IHelloAsync) svcproxy;

			// Create callback to be used by the IHelloAsync instance
			IAsyncCallback<String> helloAsyncCallbackImpl = new IAsyncCallback<String>() {
				public void onSuccess(String response) {
					System.out.println("Successfully received remote service response via callback: " + response + "\n");
				}

				public void onFailure(Throwable t) {
					System.out.println("Received exception in remote service response via callback:" + t + "\n");
				}
			};

			System.out.println("Initiating invocation of remote call via callback...");
			helloAsyncImpl.helloAsync(CLASSNAME + " using an asynchronous remote call via callback.", helloAsyncCallbackImpl);
			System.out.println("Completed invocation of remote call via callback." + "\n");
			System.out.println("EXITING ASYNCHRONOUS REMOTE CALL VIA CALLBACK" + "\n");
		} else {
			System.err.println("ASYNCHRONOUS REMOTE CALL VIA CALLBACK NOT EXECUTED" + "\n");
		}
	}

	/**
	 * Perform asynchronous remote call via future.
	 * 
	 * With ECF >=3.3 remote services, when a proxy is created, if an interface
	 * class with the name [fq service interface name]Async can be loaded and it
	 * extends IAsyncRemoteServiceProxy then the proxy will implement that
	 * interface.
	 * 
	 * @param svcproxy
	 */
	private void asyncInvokeSvcFuture(IHello svcproxy) {
		// Provided that the IHelloAsync interface was available during proxy
		// creation, the created service proxy shall implement it.
		if (svcproxy instanceof IHelloAsync) {
			System.out.println("ENTERED ASYNCHRONOUS REMOTE CALL VIA FUTURE");

			IHelloAsync helloAsync = (IHelloAsync) svcproxy;

			// Call asynchronously with future
			System.out.println("Initiating invocation of remote call via future...");
			IFuture helloAsyncFuture = helloAsync.helloAsync(CLASSNAME + " using an asynchronous remote call via future.");
			System.out.println("Completed invocation of remote call via future." + "\n");
			try {
				while (!helloAsyncFuture.isDone()) {
					// Dummy blocking to demonstrate client executing background
					// logic while waiting for service response
					System.out.println("Waiting for remote service response via future...");
					sleep();
				}
				// Once the remote service has replied the future is filled and
				// the
				// response shall be processed
				Object response = helloAsyncFuture.get();
				System.out.println("Successfully received remote service response via future: " + response + "\n");
			} catch (OperationCanceledException e) {
				System.out.println("Received operation cancel in remote service response via future: " + e + "\n");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("Interrupted operation in remote service response via future: " + e + "\n");
				e.printStackTrace();
			}
			System.out.println("EXITING ASYNCHRONOUS REMOTE CALL VIA FUTURE" + "\n");
		} else {
			System.err.println("ASYNCHRONOUS REMOTE CALL VIA FUTURE NOT EXECUTED" + "\n");
		}
	}

	/**
	 * Default sleep for 100ms.
	 */
	private void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}