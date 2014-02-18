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
 * This file is part of visitmeta visualization, version 0.0.3,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.gui;

import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import org.apache.commons.collections15.map.HashedMap;
import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.ExpandedLink;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.Position;
import de.hshannover.f4.trust.visitmeta.datawrapper.SettingManager;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeManagerCreation;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeManagerDeletion;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeSelector;
import de.hshannover.f4.trust.visitmeta.datawrapper.UpdateContainer;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FacadeLogic;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanelFactory;

public class GraphConnection implements Observer {
	private static final Logger LOGGER = Logger.getLogger(GraphConnection.class);
	private FacadeLogic mFacadeLogic = null;
	private GraphPanel mGraphPanel = null;
	private SettingManager mSettingManager = null;
	private TimeSelector mTimeSelector = null;
	private TimeManagerCreation mTimerCreation = null;
	private TimeManagerDeletion mTimerDeletion = null;
	private boolean mAddHighlights = true;
	private ConnectionTab mParentTab = null;
	private HashedMap<Observable, Observable> mObservables = new HashedMap<>();

	public GraphConnection(FacadeLogic pLogic) {
		mFacadeLogic = pLogic;
		mTimeSelector = TimeSelector.getInstance();
		mSettingManager = SettingManager.getInstance();
		mGraphPanel = GraphPanelFactory.getGraphPanel("Piccolo2D", this);

		mTimerCreation = TimeManagerCreation.getInstance();
		mTimerDeletion = TimeManagerDeletion.getInstance();
		mTimerCreation.setController(this);
		mTimerDeletion.setController(this);

		mSettingManager.addObserver(this);
		mFacadeLogic.addObserver(this);
		mTimeSelector.addObserver(this);
	}

	public void setParentTab(ConnectionTab parentTab) {
		this.mParentTab = parentTab;
	}
	
	public ConnectionTab getParentTab() {
		return mParentTab;
	}
	
	public JComponent getGraphPanel() {
		return mGraphPanel.getPanel();
	}

	/**
	 * Add the identifier in the list to the graph.
	 * 
	 * @param pIdentifier
	 *            the list with identifier.
	 */
	private synchronized void addIdentifier(List<NodeIdentifier> pIdentifier) {
		LOGGER.trace("Method addIdentifier(" + pIdentifier + ") called.");
		for (NodeIdentifier vIdentifier : pIdentifier) {
			mGraphPanel.addIdentifier(vIdentifier);
			if (mAddHighlights) {
				mGraphPanel.markAsNew(vIdentifier);
				mTimerCreation.addNode(vIdentifier);
			}
			vIdentifier.addObserver(this);
			mObservables.put(vIdentifier, vIdentifier);
		}
	}

	/**
	 * Add the metadata of the list and create the edges to the identifier in
	 * the graph.
	 * 
	 * @param pIdentifier
	 *            the identifier.
	 * @param pMetadata
	 *            the list with metadata.
	 */
	private synchronized void addMetadata(NodeIdentifier pIdentifier, List<NodeMetadata> pMetadata) {
		LOGGER.trace("Method addMetadata(" + pIdentifier + ", " + pMetadata + ") called.");
		for (NodeMetadata vMetadata : pMetadata) {
			mGraphPanel.addMetadata(pIdentifier, vMetadata);
			if (mAddHighlights) {
				mGraphPanel.markAsNew(vMetadata);
				mTimerCreation.addNode(vMetadata);
			}
			vMetadata.addObserver(this);
			mObservables.put(vMetadata, vMetadata);
		}
	}

	/**
	 * Add the metadata of the list and create the edges of the link in the
	 * graph.
	 * 
	 * @param pLink
	 *            the link.
	 * @param pMetadata
	 *            the list with metadata.
	 */
	private synchronized void addMetadata(ExpandedLink pLink, List<NodeMetadata> pMetadata) {
		LOGGER.trace("Method addMetadata(" + pLink + ", " + pMetadata + ") called.");
		for (NodeMetadata vMetadata : pMetadata) {
			mGraphPanel.addMetadata(pLink, vMetadata);
			if (mAddHighlights) {
				mGraphPanel.markAsNew(vMetadata);
				mTimerCreation.addNode(vMetadata);
			}
			vMetadata.addObserver(this);
			mObservables.put(vMetadata, vMetadata);
		}
	}

	/**
	 * Remove the IdentifierNode.
	 * 
	 * @param pIdentifier
	 *            the NodeIdentifier to identify the node in the graph.
	 */
	private synchronized void deleteIdentifier(List<NodeIdentifier> pIdentifier) {
		LOGGER.trace("Method deleteIdentifier(" + pIdentifier + ") called.");
		if (mAddHighlights) {
			for (NodeIdentifier vIdentifier : pIdentifier) {
				mGraphPanel.markAsDelete(vIdentifier);
				mTimerDeletion.addNode(vIdentifier);
			}
		}
	}

	/**
	 * Delete the MetadataNode and the edges to the node from the graph.
	 * 
	 * @param pMetadata
	 *            the NodeMetadata to identify the node and edges in the graph.
	 */
	private synchronized void deleteMetadata(List<NodeMetadata> pMetadata) {
		LOGGER.trace("Method deleteMetadata(" + pMetadata + ") called.");
		if (mAddHighlights) {
			for (NodeMetadata vMetadata : pMetadata) {
				mGraphPanel.markAsDelete(vMetadata);
				mTimerDeletion.addNode(vMetadata);
			}
		}
	}

	/**
	 * 
	 * @param pNode
	 */
	public synchronized void deleteNode(Position pNode) {
		LOGGER.trace("Method deleteNode(" + pNode + ") called.");
		mGraphPanel.deleteNode(pNode);
		pNode.deleteObserver(this);
		mObservables.remove(pNode);
	}

	/**
	 * 
	 * @param pNode
	 */
	public synchronized void removeHighlight(Position pNode) {
		LOGGER.trace("Method removeHighlight(" + pNode + ") called.");
		mGraphPanel.clearHighlight(pNode);
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
	public void updateNode(Position pNode, double pNewX, double pNewY, double pNewZ) {
		LOGGER.trace("Method updateNode(" + pNode + ", " + pNewX + ", " + pNewY + ", " + pNewZ + ") called.");
		mFacadeLogic.updateNode(pNode, pNewX, pNewY, pNewZ);
	}

	/**
	 * Get a list of all known publisher.
	 * 
	 * @return stuff
	 */
	public List<String> getPublisher() {
		LOGGER.trace("Method getPublisher() called.");
		return mGraphPanel.getPublisher();
	}

	/**
	 * Repaint nodes of a specific type and publisher.
	 * 
	 * @param pPublisher
	 *            the id of the current publisher, empty if the default color is
	 *            changed.
	 * @param pType
	 *            the type of the node,
	 */
	public void repaintNodes(String pType, String pPublisher) {
		LOGGER.trace("Method repaintMetadata(" + pPublisher + ") called.");
		mGraphPanel.repaintNodes(pType, pPublisher);
	}

	/**
	 * Stop the calculation of node positions.
	 */
	public void stopGraphMotion() {
		LOGGER.trace("Method stopGraphMotion() called.");
		mFacadeLogic.stopCalculation();
	}

	/**
	 * Resume the calculation of node positions.
	 */
	public void startGraphMotion() {
		LOGGER.trace("Method startGraphMotion() called.");
		mFacadeLogic.startCalculation();
	}

	/**
	 * Checks whether the positions of nodes are currently calculated.
	 * 
	 * @return False = Motion of graph is Off. True = Motion of graph is On.
	 */
	public boolean isGraphMotion() {
		LOGGER.trace("Method isGraphMotion() called.");
		return mFacadeLogic.isCalculationRunning();
	}

	/**
	 * Reposition all nodes.
	 */
	public void redrawGraph() {
		LOGGER.trace("Method redrawGraph() called.");
		mFacadeLogic.recalculateGraph();
	}

	/**
	 * Remove all nodes and edges from the graph.
	 */
	public void clearGraph() {
		LOGGER.trace("Method clearGraph() called.");
		/* Delete observables */
		for (Observable vObserbale : mObservables.keySet()) {
			vObserbale.deleteObserver(this);
		}
		mObservables.clear();
		mGraphPanel.clearGraph();
	}

	/**
	 * Load the initial graph to the timestamp in TimeSelector.
	 * 
	 * @see FacadeLogic#loadInitialGraph()
	 */
	private void loadInitialGraph() {
		LOGGER.trace("Method loadInitialGraph() called.");
		mTimerCreation.removeAll();
		mTimerDeletion.removeAll();
		clearGraph();
		mFacadeLogic.loadInitialGraph();
	}

	/**
	 * Load the delta to the timestamps in TimeSelector.
	 * 
	 * @see FacadeLogic#loadDelta()
	 */
	private void loadDelta() {
		LOGGER.trace("Method loadDelta() called.");
		mFacadeLogic.loadDelta();
	}

	/**
	 * Load the initial graph and delta to the timestamp in TimeSelector.
	 * 
	 * @see GraphConnection#loadInitialGraph()
	 * @see GraphConnection#loadDelta()
	 */
	private void delta() {
		LOGGER.trace("Method delta() called.");
		mGraphPanel.setNodeTranslationDuration(0);
		mAddHighlights = false;
		loadInitialGraph();
		mTimerCreation.removeAll();
		mTimerDeletion.removeAll();
		mAddHighlights = true;
		loadDelta();
		mGraphPanel.setNodeTranslationDuration(mSettingManager.getNodeTranslationDuration());
	}

	/**
	 * Fill the GraphPanel with the data.
	 * 
	 * @param pData
	 *            the data.
	 */
	public void fillGraph(UpdateContainer pData) {
		/* Add nodes to graph. */
		addIdentifier(pData.getListAddIdentifier());
		for (NodeIdentifier vIdentifier : pData.getListAddIdentifier()) {
			addMetadata(vIdentifier, vIdentifier.getMetadata());
		}
		for (ExpandedLink vLink : pData.getListAddLinks()) {
			addMetadata(vLink, vLink.getMetadata());
		}
		for (Entry<NodeIdentifier, List<NodeMetadata>> vSet : pData.getListAddMetadataIdentifier().entrySet()) {
			addMetadata(vSet.getKey(), vSet.getValue());
		}
		for (Entry<ExpandedLink, List<NodeMetadata>> vSet : pData.getListAddMetadataLinks().entrySet()) {
			addMetadata(vSet.getKey(), vSet.getValue());
		}
		for (List<NodeMetadata> vMetadata : pData.getListDeleteMetadataLinks().values()) {
			deleteMetadata(vMetadata);
		}
		for (List<NodeMetadata> vMetadata : pData.getListDeleteMetadataIdentifier().values()) {
			deleteMetadata(vMetadata);
		}
		deleteIdentifier(pData.getListDeleteIdentifier());

		mGraphPanel.adjustPanelSize();
	}

	@Override
	public void update(Observable o, Object arg) {
		LOGGER.trace("Method update(" + o + ", " + arg + ") called.");
		if (o instanceof FacadeLogic) {
			fillGraph(mFacadeLogic.getUpdate());
			mGraphPanel.repaint();
		} else if (o instanceof NodeIdentifier) {
			mGraphPanel.updateIdentifier((NodeIdentifier) o);
			mGraphPanel.repaint();
		} else if (o instanceof NodeMetadata) {
			mGraphPanel.updateMetadata((NodeMetadata) o);
			mGraphPanel.repaint();
		} else if (o instanceof TimeSelector) {
			if (!mTimeSelector.isLiveView()) {
				delta();
			}
			mGraphPanel.repaint();
		} else if (o instanceof SettingManager) {
			mGraphPanel.setNodeTranslationDuration(mSettingManager.getNodeTranslationDuration());
		}
	}
}
