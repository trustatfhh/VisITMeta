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
 * This file is part of visitmeta-dataservice, version 0.3.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.ifmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.MessageDigest;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;

import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryIdentifierFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InternalIdentifierFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InternalMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.Neo4JTestDatabaseFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.DummyGraphCache;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.dataservice.rest.JsonMarshaller;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.persistence.Executor;
import de.hshannover.f4.trust.visitmeta.persistence.Writer;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JConnection;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JExecutor;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JReader;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JRepository;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JTimestampManager;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JWriter;

public abstract class AbstractMultiSubscriptionTestCase {

	private static final Logger mLog = Logger.getLogger(AbstractMultiSubscriptionTestCase.class);

	protected Writer mWriter;

	protected Connection mConnection;

	protected Executor mExecutor;

	protected JsonMarshaller mJsonMarshaller;

	protected Neo4JConnection mDbConnection;

	protected Neo4JReader mNeo4jDb;

	protected GraphDatabaseService mGraphDb;

	protected Neo4JTimestampManager mTimestampManager;

	protected GraphService mService;

	protected Neo4JRepository mNeo4jRepo;

	protected IfmapJHelper mIfmapJHelper;

	protected InternalIdentifierFactory mIdentifierFactory;

	protected InternalMetadataFactory mMetadataFactory;

	protected AbstractMultiSubscriptionTestCase() {
	}

	@Before
	public void setUp() throws Exception {
		mGraphDb = Neo4JTestDatabaseFactory.createGraphDB();
		assumeNotNull(mGraphDb);

		mDbConnection = mock(Neo4JConnection.class);
		when(mDbConnection.getConnection()).thenReturn(mGraphDb);

		mTimestampManager = new Neo4JTimestampManager(mDbConnection);
		when(mDbConnection.getTimestampManager()).thenReturn(mTimestampManager);

		mNeo4jRepo = new Neo4JRepository(mDbConnection, MessageDigest.getInstance("MD5"));
		mNeo4jDb = new Neo4JReader(mNeo4jRepo, mDbConnection);
		mExecutor = new Neo4JExecutor(mDbConnection);
		mWriter = new Neo4JWriter(mNeo4jRepo, mDbConnection);

		mService = new SimpleGraphService(mNeo4jDb, mExecutor, new DummyGraphCache());
		mJsonMarshaller = new JsonMarshaller();

		mIdentifierFactory = new InMemoryIdentifierFactory();
		mMetadataFactory = new InMemoryMetadataFactory();
		mIfmapJHelper = new IfmapJHelper(mIdentifierFactory);

		mConnection = mock(Connection.class);

	}

	@After
	public void tearDown() {
		if (mGraphDb != null) {
			mGraphDb.shutdown();
		}
	}

	protected void startPollTask(PollResult ifmapPollResult) {
		mLog.debug("PollTask started....");

		try {
			when(mConnection.poll()).thenReturn(ifmapPollResult);
		} catch (ConnectionException e) {
			mLog.error(e.getMessage(), e);
		}

		PollTask task = new PollTask(mConnection, mMetadataFactory, mIfmapJHelper);

		try {

			de.hshannover.f4.trust.visitmeta.ifmap.PollResult pollResult = task.call();
			mWriter.submitPollResult(pollResult);

		} catch (ConnectionException e) {
			mLog.error(e.toString(), e);
		}

		mLog.debug("... PollTask finished");
	}

	/**
	 * Check the change-map-size.
	 * 
	 * @param changesMap
	 * @param expected
	 */
	protected void assertEqualsMapSize(SortedMap<Long, Long> changesMap, int expected) {
		if (changesMap == null) {
			throw new IllegalArgumentException();
		}
		assertEquals("Because the changes map size is wrong.", expected, changesMap.size());
	}

	/**
	 * Equals the map keys and values.
	 * All keys and values from changesMap1 may be contains in changesMap2.
	 * 
	 * @param changesMap1
	 * @param changesMap2
	 */
	public void assertEqualsMapValues(SortedMap<Long, Long> changesMap1, SortedMap<Long, Long> changesMap2) {
		if (changesMap1 == null || changesMap2 == null) {
			throw new IllegalArgumentException();
		}

		for (Entry<Long, Long> entry : changesMap1.entrySet()) {

			Long changeMap1Value = entry.getValue();
			Long chnageMap2Value = changesMap2.get(entry.getKey());

			if (chnageMap2Value == null) {
				fail("The second changes map does not contain the key(" + entry.getKey()
						+ ") from the first changes map.");
			}

			assertEquals("Because the changes map values are not same.", chnageMap2Value, changeMap1Value);
		}
	}

	/**
	 * Equals new values from the second changes map with the expected value.
	 * New values from the second changes map does not contains in the first changes map.
	 * 
	 * @param changesMap1
	 * @param changesMap2
	 */
	public void assertEqualsNewValues(SortedMap<Long, Long> changesMap1, SortedMap<Long, Long> changesMap2, long expectedValue) {
		if (changesMap1 == null || changesMap2 == null) {
			throw new IllegalArgumentException();
		}

		for (Entry<Long, Long> entry : changesMap2.entrySet()) {
			if (!changesMap1.containsKey(entry.getKey())) {
				// only for new keys in the second changes map
				assertEquals("Because the value from the key(" + entry.getKey() + ") is not right.", expectedValue,
						entry.getValue().longValue());
				break;
			}
		}
	}

	protected void printNeo4jDB() {
		Neo4JTestDatabaseFactory.printDB(mGraphDb);
	}

	protected void printCurrentGraph() {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();
		JSONArray currentGraphJSON = mJsonMarshaller.toJson(currentGraph);

		System.out.println(currentGraphJSON);
	}

}
