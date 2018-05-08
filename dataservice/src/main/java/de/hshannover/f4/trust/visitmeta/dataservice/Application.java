/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta-dataservice, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.dataservice;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.constructor.ConstructorException;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.dataservice.rest.RestService;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.ConnectionManagerImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.DataserviceModule;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.util.yaml.ConnectionsProperties;

/**
 * Application main class, also provides access to main interfaces. <i>Note:
 * Must only contain static methods and therefore is abstract</i>
 *
 * @author rosso
 *
 */
public abstract class Application {

	public static final String DATASERVICE_VERSION = "${project.version}";

	private static final Logger log = Logger.getLogger(Application.class);

	/**
	 * Configuration class for the application.
	 */
	private static Properties mConfig;

	/**
	 * Configuration class for persistent IF-MAPS Connections.
	 */
	private static ConnectionsProperties mConnections;

	private static RestService restService;

	private static Thread restServiceThread;

	private static ConnectionManager mManager;

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws PropertyException 
	 */
	public static void main(String[] args) throws InterruptedException, PropertyException {
		log.info("VisITMeta dataservice application v" + DATASERVICE_VERSION
				+ " started.");

		// Creates and initializes the ConnectionManager(Impl) instance
		getConnectionManager();

		initComponents();

		List<DataserviceModule> modules = DataserviceModuleConnector.initializeModules(mManager);
		log.info(modules.size() + " dataservice modules were loaded.");

		try {
			loadPersistentConnections();
		} catch (IOException e) {
			log.error("error while load persistent connections", e);
		} catch (ConstructorException e) {
			log.error("error while load persistent connections", e);
		}

		try {
			startupConnect();
		} catch (ConnectionException e) {
			log.error(e.toString());
		}
		
		startRestService(modules);

		log.info("dataservice started successfully");
	}

	/**
	 * Returns an instance of a {@link ConnectionManager}. Creates a new
	 * {@link ConnectionManagerImpl} when instance is empty.
	 *
	 * @return a {@link ConnectionManager} instance
	 */
	public synchronized static ConnectionManager getConnectionManager() {
		if (mManager == null) {
			mManager = new ConnectionManagerImpl();
		}

		return mManager;
	}

	private static void startupConnect() throws ConnectionException, InterruptedException {
		log.debug("startupConnect...");
		mManager.executeStartupConnections();
	}

	private static void loadPersistentConnections() throws IOException {
		log.info("load persistent connections");

		Set<String> keySet = null;
		try {
			keySet = mConnections.getKeySet();
		} catch (PropertyException e) {
			log.error("error while getKeySet from ConnectionsProperties -> " + e.toString(), e);
		}

		for (String s : keySet) {
			MapServerConnection tmp = null;
			try {
				tmp = mConnections.buildConnection(s);
			} catch (ConnectionException | PropertyException e) {
				log.error("error while build new connection from Properties -> " + e.toString(), e);
			}
			try {
				mManager.addConnection(tmp);
			} catch (ConnectionException e) {
				log.error("error while adding connection to the connection pool", e);
			}
		}
	}

	private static void startRestService(List<DataserviceModule> modules) throws PropertyException {
		log.info("Start RestService");
		
		String urlVariable = "";
		// run Service with SSL?
		if (Application.getConfig().getBoolean("ssl")) {
			urlVariable = "restServiceSSLUrl";
		} else {
			urlVariable = "restServiceUrl";
		}
		
		String url = "";
		try {
			url = Application.getConfig().getString(urlVariable);
		} catch (PropertyException e) {
			log.fatal(e.toString(), e);
			throw new RuntimeException("could not load requested properties", e);
		}

		Set<Class<?>> classes = new HashSet<>();
		for (DataserviceModule module : modules) {
			classes.addAll(module.getRestClasses());
		}

		restService = new RestService(url, classes);

		restServiceThread = new Thread(restService, "RestService-Thread");
		restServiceThread.start();
	}

	public static void initComponents() {
		try {
			String config = Application.class.getClassLoader()
					.getResource("dataservice_config.yml").getPath();
			String connections = Application.class.getClassLoader()
					.getResource("dataservice_connections.yml").getPath();
			mConfig = new Properties(config);
			mConnections = new ConnectionsProperties(mManager, connections);

			log.info("components initialized");
		} catch (PropertyException e) {
			String msg = "Error while reading the config files";
			log.fatal(msg);
			throw new RuntimeException(msg, e);
		}
	}

	public static MessageDigest loadHashAlgorithm() {
		try {
			String algoname = mConfig.getString("database.hashAlgorithm");
			log.trace("try to load MessageDigest for '" + algoname + "'");
			return MessageDigest.getInstance(algoname);
		} catch (NoSuchAlgorithmException | PropertyException e) {
			throw new RuntimeException(
					"could not load requested hash algorithm", e);
		}
	}

	/**
	 * @return
	 */
	public static Properties getConfig() {
		if (mConfig == null) {
			throw new RuntimeException(
					"Application property has not been initialized. This is not good!");
		}
		return mConfig;
	}

	/**
	 * @return
	 */
	public static ConnectionsProperties getConnections() {
		if (mConnections == null) {
			throw new RuntimeException(
					"ConnectionsConfig has not been initialized. This is not good!");
		}
		return mConnections;
	}

}
