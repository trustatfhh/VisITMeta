package de.fhhannover.inform.trust.visitmeta.gui;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

/**
 *
 */
public class MenuBar extends JMenuBar {
	private static final long   serialVersionUID = 1L;
	private static final Logger LOGGER           = Logger.getLogger(MenuBar.class);

	private GuiController mContoller         = null;
	/* Actions */
	private JMenu         mMenuActions       = null;
	private JMenu         mMenuLevelOfDetail = null;
	private JMenuItem     mItemLevel0        = null;
	private JMenuItem     mItemLevel1        = null;
	private JMenuItem     mItemLevel2        = null;
	private JMenuItem     mItemStopMotion    = null;
	private JMenuItem     mItemRedrawGraph   = null;
	private JMenuItem     mItemSetColors     = null;
	private JMenuItem     mItemTimings      = null;

	public MenuBar(GuiController pController) {
		super();
		mContoller = pController;
		/* Connections */
//		JMenu mnConnections = new JMenu("Connections");
//		add(mnConnections);
//
//		JMenu mnConnectTo = new JMenu("Connect To");
//		mnConnections.add(mnConnectTo);
//
//		JMenuItem mntmServer = new JMenuItem("Server 1");
//		mnConnectTo.add(mntmServer);
//
//		JMenuItem mntmServer_1 = new JMenuItem("Server 2");
//		mnConnectTo.add(mntmServer_1);
//
//		JMenuItem menuItem = new JMenuItem("...");
//		mnConnectTo.add(menuItem);
//
//		JMenuItem mntmAddConnection = new JMenuItem("Manage Connections");
//		mnConnections.add(mntmAddConnection);
		/* Actions */
		mMenuActions = new JMenu("Actions");
		add(mMenuActions);

//		mMenuLevelOfDetail = new JMenu("Level of Detail");
//		mMenuActions.add(mMenuLevelOfDetail);
//
//		mItemLevel0 = new JMenuItem("High Detail");
//		mMenuLevelOfDetail.add(mItemLevel0);
//
//		mItemLevel1 = new JMenuItem("Middle Detail");
//		mMenuLevelOfDetail.add(mItemLevel1);
//
//		mItemLevel2 = new JMenuItem("Low Detail");
//		mMenuLevelOfDetail.add(mItemLevel2);

		mItemStopMotion = new JMenuItem("Stop Motion");
		mItemStopMotion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				if(mContoller.isGraphMotion()) {
					LOGGER.debug("Stop motion of the graph.");
					mContoller.stopGraphMotion();
					mItemStopMotion.setText("Start Motion");
				} else {
					LOGGER.debug("Start motion of the graph.");
					mContoller.startGraphMotion();
					mItemStopMotion.setText("Stop Motion");
				}
			}
		});
		mMenuActions.add(mItemStopMotion);

		mItemRedrawGraph = new JMenuItem("Redraw");
		mItemRedrawGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				LOGGER.debug("Redraw the graph.");
				mContoller.redrawGraph();
			}
		});
		mMenuActions.add(mItemRedrawGraph);

		/* Filter */
//		JMenu mnFilter = new JMenu("Filter");
//		add(mnFilter);
//
//		JMenu mnActivate = new JMenu("Activate");
//		mnFilter.add(mnActivate);
//
//		JCheckBoxMenuItem chckbxmntmFilter = new JCheckBoxMenuItem("Filter 1");
//		mnActivate.add(chckbxmntmFilter);
//
//		JCheckBoxMenuItem chckbxmntmFilter_1 = new JCheckBoxMenuItem("Filter 2");
//		mnActivate.add(chckbxmntmFilter_1);
//
//		JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem("...");
//		mnActivate.add(checkBoxMenuItem);
//
//		JMenuItem mntmManageFilter = new JMenuItem("Manage Filter");
//		mnFilter.add(mntmManageFilter);
		/* Settings */
		JMenu mnSettings = new JMenu("Settings");
		add(mnSettings);

		mItemSetColors = new JMenuItem("Colors");
		mItemSetColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				mContoller.showColorSettings();
			}
		});
		mnSettings.add(mItemSetColors);

		mItemTimings = new JMenuItem("Timings");
		mItemTimings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				mContoller.showSettings();
			}
		});
		mnSettings.add(mItemTimings);

//		JMenu mnGraphDrawer = new JMenu("Graph Drawer");
//		mnSettings.add(mnGraphDrawer);
//
//		JMenuItem mntmPiccolod = new JMenuItem("Piccolo2D");
//		mnGraphDrawer.add(mntmPiccolod);
//
//		JMenu mnGraphCalculator = new JMenu("Graph Calculator");
//		mnSettings.add(mnGraphCalculator);
//
//		JMenuItem mntmJung = new JMenuItem("Jung2");
//		mnGraphCalculator.add(mntmJung);
//
//		JMenuItem mntmColot = new JMenuItem("Color");
//		mnSettings.add(mntmColot);
		/* Help */
//		JMenu mnHelp = new JMenu("Help");
//		add(mnHelp);
//
//		JMenuItem mntmHelp = new JMenuItem("Help");
//		mnHelp.add(mntmHelp);
//
//		JMenuItem mntmAbout = new JMenuItem("About");
//		mnHelp.add(mntmAbout);
	}
}
