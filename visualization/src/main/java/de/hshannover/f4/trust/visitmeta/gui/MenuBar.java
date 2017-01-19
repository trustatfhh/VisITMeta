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
package de.hshannover.f4.trust.visitmeta.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.graphCalculator.JungCalculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.LayoutType;
import de.hshannover.f4.trust.visitmeta.gui.MainWindow.SupportedLaF;
import de.hshannover.f4.trust.visitmeta.gui.dialog.ConnectionDialog;
import de.hshannover.f4.trust.visitmeta.gui.util.Dataservices;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

/**
 *
 */
public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MenuBar.class);

	private GuiController mController = null;
	/* Actions */
	private JMenu mMenuActions = null;
	private JMenu mMenuTheme = null;
	private JMenu mMenuLayout = null;
	private JMenuItem mItemStopMotion = null;
	private JMenuItem mItemRedrawGraph = null;
	private JMenuItem mItemSetColors = null;
	private JMenuItem mItemTimings = null;
	private JCheckBoxMenuItem mMenuItemDualViewGraph;

	/**
	 *
	 * @param guiController
	 */
	public MenuBar(final GuiController guiController) {
		super();
		mController = guiController;
		/* Connections */
		JMenu mnConnections = new JMenu("Connections");
		add(mnConnections);

		JMenuItem mntmAddConnection = new JMenuItem("Manage Connections");
		mntmAddConnection.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainWindow mainWindow = mController.getMainWindow();
				List<Data> dataserviceList = ((Dataservices) mainWindow.getConnectionTree().getModel().getRoot())
						.getSubData();

				ConnectionDialog cD = new ConnectionDialog(mainWindow,
						(List<DataserviceConnection>) (List<?>) dataserviceList);
				cD.setVisible(true);
			}
		});

		mnConnections.add(mntmAddConnection);
		/* Actions */
		mMenuActions = new JMenu("Actions");
		add(mMenuActions);

		// mMenuLevelOfDetail = new JMenu("Level of Detail");
		// mMenuActions.add(mMenuLevelOfDetail);
		//
		// mItemLevel0 = new JMenuItem("High Detail");
		// mMenuLevelOfDetail.add(mItemLevel0);
		//
		// mItemLevel1 = new JMenuItem("Middle Detail");
		// mMenuLevelOfDetail.add(mItemLevel1);
		//
		// mItemLevel2 = new JMenuItem("Low Detail");
		// mMenuLevelOfDetail.add(mItemLevel2);

		mItemStopMotion = new JMenuItem("Stop Motion");
		mItemStopMotion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				String oldState = mController.switchGraphMotion();

				if (oldState != null) {
					LOGGER.debug("Changed motion of the graph, old state: "
							+ oldState);
					mItemStopMotion.setText(oldState
							+ " motion");
				}
			}
		});
		mMenuActions.add(mItemStopMotion);

		mItemRedrawGraph = new JMenuItem("Redraw");
		mItemRedrawGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				LOGGER.debug("Redraw the graph.");
				mController.redrawGraph();
			}
		});
		mMenuActions.add(mItemRedrawGraph);

		/* Filter */
		// JMenu mnFilter = new JMenu("Filter");
		// add(mnFilter);
		//
		// JMenu mnActivate = new JMenu("Activate");
		// mnFilter.add(mnActivate);
		//
		// JCheckBoxMenuItem chckbxmntmFilter = new
		// JCheckBoxMenuItem("Filter 1");
		// mnActivate.add(chckbxmntmFilter);
		//
		// JCheckBoxMenuItem chckbxmntmFilter_1 = new
		// JCheckBoxMenuItem("Filter 2");
		// mnActivate.add(chckbxmntmFilter_1);
		//
		// JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem("...");
		// mnActivate.add(checkBoxMenuItem);
		//
		// JMenuItem mntmManageFilter = new JMenuItem("Manage Filter");
		// mnFilter.add(mntmManageFilter);
		/* Settings */
		JMenu mnSettings = new JMenu("Settings");
		add(mnSettings);

		mItemSetColors = new JMenuItem("Colors");
		mItemSetColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				mController.showColorSettings();
			}
		});
		mnSettings.add(mItemSetColors);

		mItemTimings = new JMenuItem("Timings");
		mItemTimings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				mController.showSettings();
			}
		});
		mnSettings.add(mItemTimings);

		mMenuTheme = new JMenu("Theme");
		mnSettings.add(mMenuTheme);

		final List<SupportedLaF> supportedLaFs = guiController.getMainWindow()
				.getSupportedLaFs();
		final MainWindow mainWindow = guiController.getMainWindow();

		for (final SupportedLaF lAf : supportedLaFs) {
			mMenuTheme.add(lAf.menuItem);
			if (lAf.laf.getID() == UIManager.getLookAndFeel().getID()) {
				lAf.menuItem.setSelected(true);
			}
			lAf.menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					LookAndFeel laf = lAf.laf;
					try {
						UIManager.setLookAndFeel(laf);
						SwingUtilities.updateComponentTreeUI(mainWindow);
						mainWindow.pack();
					} catch (UnsupportedLookAndFeelException e) {
						LOGGER.error(e.getMessage());
					}

					for (SupportedLaF lAf2 : supportedLaFs) {
						if (lAf != lAf2) {
							lAf2.menuItem.setSelected(false);
						}
					}
				}
			});
		}

		// JMenu mnGraphDrawer = new JMenu("Graph Drawer");
		// mnSettings.add(mnGraphDrawer);
		//
		// JMenuItem mntmPiccolod = new JMenuItem("Piccolo2D");
		// mnGraphDrawer.add(mntmPiccolod);
		//
		// JMenu mnGraphCalculator = new JMenu("Graph Calculator");
		// mnSettings.add(mnGraphCalculator);
		//
		// JMenuItem mntmJung = new JMenuItem("Jung2");
		// mnGraphCalculator.add(mntmJung);
		//
		// JMenuItem mntmColot = new JMenuItem("Color");
		// mnSettings.add(mntmColot);
		/* Help */
		// JMenu mnHelp = new JMenu("Help");
		// add(mnHelp);
		//
		// JMenuItem mntmHelp = new JMenuItem("Help");
		// mnHelp.add(mntmHelp);
		//
		// JMenuItem mntmAbout = new JMenuItem("About");
		// mnHelp.add(mntmAbout);

		mMenuLayout = new JMenu("Layout");
		mnSettings.add(mMenuLayout);

		@SuppressWarnings("serial")
		final Map<LayoutType, JCheckBoxMenuItem> layoutMap = new EnumMap<LayoutType, JCheckBoxMenuItem>(
				LayoutType.class) {
			{
				put(LayoutType.FORCE_DIRECTED, new JCheckBoxMenuItem(
						"Force-directed (JUNG2)"));
				put(LayoutType.SPRING, new JCheckBoxMenuItem("Spring (JUNG2)"));
				put(LayoutType.BIPARTITE, new JCheckBoxMenuItem("Bipartite"));
				put(LayoutType.CIRCULAR, new JCheckBoxMenuItem("Circular"));
				put(LayoutType.HIERARCHICAL_h1, new JCheckBoxMenuItem("Hierarchical_h1"));
				put(LayoutType.HIERARCHICAL_h2, new JCheckBoxMenuItem("Hierarchical_h2"));
				put(LayoutType.HIERARCHICAL_v, new JCheckBoxMenuItem("Hierarchical_v"));
			}
		};

		for (final Entry<LayoutType, JCheckBoxMenuItem> layout : layoutMap
				.entrySet()) {
			mMenuLayout.add(layout.getValue());
			layout.getValue().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					mController.setLayoutType(layout.getKey());
					for (Entry<LayoutType, JCheckBoxMenuItem> otherLayout : layoutMap
							.entrySet()) {
						if (otherLayout.getKey() != layout.getKey()) {
							otherLayout.getValue().setSelected(false);
						}
					}
				}
			});
		}

		// TODO: Initialize layout type from user settings/preferences, remove
		// dependency on JungCalculator. <VA> 2014-08-05
		layoutMap.get(JungCalculator.DEFAULT_LAYOUT_TYPE).setSelected(true);

		mMenuItemDualViewGraph = new JCheckBoxMenuItem("Dual View");
		mnSettings.add(mMenuItemDualViewGraph);

		mMenuItemDualViewGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mMenuItemDualViewGraph.isSelected()) {
					mController.getMainWindow().showDualViewGraph();
				} else {
					mController.getMainWindow().showSingleViewGraph();
				}
			}
		});
	}

}
