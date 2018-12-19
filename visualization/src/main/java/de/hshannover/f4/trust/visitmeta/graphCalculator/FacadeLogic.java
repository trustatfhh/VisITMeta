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
 * This file is part of visitmeta-visualization, version 0.6.0,
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
package de.hshannover.f4.trust.visitmeta.graphCalculator;

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.Position;
import de.hshannover.f4.trust.visitmeta.datawrapper.SettingManager;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeManagerDeletion;
import de.hshannover.f4.trust.visitmeta.datawrapper.UpdateContainer;
import de.hshannover.f4.trust.visitmeta.network.FacadeNetwork;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

/**
 * Calculates the layout and provides a graph structure to the gui.
 */
public class FacadeLogic extends Observable implements Observer, Runnable {

	private static final Logger LOGGER = Logger.getLogger(FacadeLogic.class);

	private GraphContainer mConnection = null;
	private FacadeNetwork mFacadeNetwork = null;
	private Calculator mCalculator = null;
	private UpdateContainer mUpdateContainer = null;
	private SettingManager mSettingManager = null;
	private boolean mIsDone = false;
	private boolean mDoCalculation = true;
	private int mInterval = 0;
	private int mIterations = 0;
	private TimeManagerDeletion mTimerDeletion = null;

	public FacadeLogic(GraphContainer connection) {
		mConnection = connection;
		mUpdateContainer = new UpdateContainer();
		mFacadeNetwork = mConnection.getFacadeNetwork();
		mCalculator = mConnection.getCalculator();
		mSettingManager = mConnection.getSettingManager();
		mInterval = mSettingManager.getCalculationInterval();
		mIterations = mSettingManager.getCalculationIterations();
		mCalculator.setIterations(mIterations);
		mTimerDeletion = mConnection.getTimeManagerDeletion();
		mTimerDeletion.setLogic(this);
		mSettingManager.addObserver(this);
		mFacadeNetwork.addObserver(this);
	}

	public synchronized UpdateContainer getUpdate() {
		LOGGER.trace("Method getUpdate() called.");
		return mUpdateContainer.getCopyFlat();
	}

	/**
	 * End the thread.
	 */
	public synchronized void finish() {
		LOGGER.trace("Method finish() called.");
		mIsDone = true;
	}

	/**
	 * Set the new position for a Position-Object.
	 *
	 * @param pNode
	 *            the Object.
	 * @param pNewX
	 *            the new x coordinate.
	 * @param pNewY
	 *            the new y coordinate.
	 * @param pNewZ
	 *            the new z coordinate.
	 */
	public synchronized void updateNode(Position pNode, double pNewX, double pNewY, double pNewZ, boolean pinNode) {
		LOGGER.trace("Method updateNode("
				+ pNode + ", " + pNewX + ", " + pNewY + ", " + pNewZ + ") called.");
		mCalculator.updateNode(pNode, pNewX, pNewY, pNewZ, pinNode);
	}

	/**
	 * Start the calculation of position in the thread.
	 */
	public synchronized void startCalculation() {
		LOGGER.trace("Method startCalculation() called.");
		mDoCalculation = true;
	}

	/**
	 * Stop the calculation of position in the thread.
	 */
	public synchronized void stopCalculation() {
		LOGGER.trace("Method stopCalculation() called.");
		mDoCalculation = false;
	}

	/**
	 * Return if the thread is calculation positions.
	 *
	 * @return False = Motion of graph is Off. True = Motion of graph is On.
	 */
	public boolean isCalculationRunning() {
		LOGGER.trace("Method isCalculationRunning() called.");
		return mDoCalculation;
	}

	/**
	 * @see FacadeNetwork#loadGraphAtDeltaStart()
	 */
	public synchronized void loadGraphAtDeltaStart() {
		clearGraph();
		mFacadeNetwork.loadGraphAtDeltaStart();
	}

	/**
	 * Loads the current graph
	 *
	 * @see FacadeNetwork#loadCurrentGraph()
	 */
	public synchronized void loadCurrentGraph() {
		clearGraph();
		mFacadeNetwork.loadCurrentGraph();
	}

	/**
	 * Load the delta to the timestamps in TimeSelector. This methode notify the
	 * observers.
	 *
	 * @see FacadeNetwork#loadDelta()
	 */
	public synchronized void loadDelta() {
		LOGGER.trace("Method loadDelta() called.");
		/* Call for delta */
		mFacadeNetwork.loadDelta();
	}

	/**
	 * Remove all nodes and edges from the calcualtor.
	 */
	public synchronized void clearGraph() {
		LOGGER.trace("Method clearGraph() called.");
		mCalculator.clearGraph();
	}

	/**
	 * Remove a node from the graph.
	 *
	 * @param pNode
	 *            the node.
	 */
	public synchronized void deleteNode(Position pNode) {
		LOGGER.trace("Method deleteNode("
				+ pNode + ") called.");
		mCalculator.deleteNode(pNode);
	}

	/**
	 * Set the layout type (e.g., force-directed).
	 */
	public void setLayoutType(LayoutType layoutType) {
		mCalculator.setLayoutType(layoutType);
	}

	/**
	 * Get the active layout type (e.g., force-directed).
	 */
	public LayoutType getLayoutType() {
		return mCalculator.getLayoutType();
	}

	/**
	 * Recalculate the position of all nodes.
	 */
	public synchronized void recalculateGraph() {
		LOGGER.trace("Method recalculateGraph() called.");
		mCalculator.adjustGraphAnew(Main.getConfig().getInt(VisualizationConfig.KEY_CALCULATION_ITERATIONS,
				VisualizationConfig.DEFAULT_VALUE_CALCULATION_ITERATIONS));
	}

	@Override
	public void update(Observable o, Object arg) {
		LOGGER.trace("Method update("
				+ o + ", " + arg + ") called.");
		if (o instanceof FacadeNetwork) {
			synchronized (this) {
				/* Add nodes to calculator. */
				mUpdateContainer = mFacadeNetwork.getUpdate();
				mCalculator.addRemoveNodesLinksMetadatas(mUpdateContainer);
			}
			setChanged();
			notifyObservers();
		} else if (o instanceof SettingManager) {
			mInterval = mSettingManager.getCalculationInterval();
			mIterations = mSettingManager.getCalculationIterations();
			mCalculator.setIterations(mIterations);
		}
	}

	@Override
	public void run() {
		LOGGER.trace("Method run() called.");
		try {
			synchronized (this) {
				while (!mIsDone) {
					if (mDoCalculation) {
						mCalculator.adjustAllNodes(mIterations, true, true);
					}
					wait(mInterval);
				}
			}
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		}
	}

}
