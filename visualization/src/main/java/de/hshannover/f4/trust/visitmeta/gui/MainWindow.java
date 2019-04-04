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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.gui.dialog.DataServiceParameterPanel;
import de.hshannover.f4.trust.visitmeta.gui.dialog.DialogHelper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.LayoutHelper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.MapServerParameterPanel;
import de.hshannover.f4.trust.visitmeta.gui.dialog.SubscriptionParameterPanel;
import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreeCellRenderer;
import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreePopupMenu;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceRestConnectionImpl;
import de.hshannover.f4.trust.visitmeta.gui.util.Dataservices;
import de.hshannover.f4.trust.visitmeta.gui.util.MapServerRestConnectionImpl;
import de.hshannover.f4.trust.visitmeta.gui.util.ParameterListener;
import de.hshannover.f4.trust.visitmeta.gui.util.ParameterPanel;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnectionTree;
import de.hshannover.f4.trust.visitmeta.gui.util.RestHelper;
import de.hshannover.f4.trust.visitmeta.gui.util.RestSubscriptionImpl;
import de.hshannover.f4.trust.visitmeta.input.gui.MotionControllerHandler;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.DataserviceData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MainWindow.class);

	private static final Properties mConfig = Main.getConfig();

	private static final String VISITMETA_ICON_16PX = "visitmeta-icon-16px.png";
	private static final String VISITMETA_ICON_32PX = "visitmeta-icon-32px.png";
	private static final String VISITMETA_ICON_64PX = "visitmeta-icon-64px.png";
	private static final String VISITMETA_ICON_128PX = "visitmeta-icon-128px.png";
	private boolean mDualView = false;

	private JSplitPane mMainSplitPane = null;
	private JPanel mLeftMainPanel = null;
	private JSplitPane mRightMainPanelSplitPane = null;
	private JPanel mRightMainPanel = null;
	private NavigationPanel mProjectionView;
	private JPanel mGraphPanel;
	private JPanel mJpParameter;
	private ParameterPanel mJpParameterValues;
	private JPanel mJpParameterSouth;
	private JButton mJbParameterSave;
	private JButton mJbParameterReset;
	private JTabbedPane mTabbedConnectionPaneLeft = null;
	private JTabbedPane mTabbedConnectionPaneRight = null;
	private JScrollPane mConnectionScrollPane = null;
	private RESTConnectionTree mConnectionTree = null;
	private ConnectionTreeCellRenderer mTreeRenderer = null;
	private static List<SupportedLaF> supportedLaFs = new ArrayList<SupportedLaF>();
	private ImageIcon[] mCancelIcon = null;
	private MotionControllerHandler mMotionControllerHandler = null;

	/**
	 *
	 * @param guiController
	 */
	public MainWindow(MotionControllerHandler motionControllerHandler) {
		super("VisITMeta GUI v" + Main.VISUALIZATION_VERSION);

		mMotionControllerHandler = motionControllerHandler;

		init();
	}

	/**
	 * Initializes the main panel
	 */
	private void init() {
		this.setLookAndFeel();
		this.setMinimumSize(new Dimension(800, 600));

		Image visitMetaIcon16px = new ImageIcon(
				MainWindow.class.getClassLoader().getResource(VISITMETA_ICON_16PX).getPath()).getImage();
		Image visitMetaIcon32px = new ImageIcon(
				MainWindow.class.getClassLoader().getResource(VISITMETA_ICON_32PX).getPath()).getImage();
		Image visitMetaIcon64px = new ImageIcon(
				MainWindow.class.getClassLoader().getResource(VISITMETA_ICON_64PX).getPath()).getImage();
		Image visitMetaIcon128px = new ImageIcon(
				MainWindow.class.getClassLoader().getResource(VISITMETA_ICON_128PX).getPath()).getImage();

		List<? extends Image> visitMetaIcons = Arrays.asList(visitMetaIcon16px, visitMetaIcon32px, visitMetaIcon64px,
				visitMetaIcon128px);
		this.setIconImages(visitMetaIcons);

		initLeftHandSide();
		initRightHandSide();

		mMainSplitPane = new JSplitPane();
		mMainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mMainSplitPane.setResizeWeight(0.125);		
		mMainSplitPane.setRightComponent(mRightMainPanelSplitPane);
		mMainSplitPane.setRightComponent(mRightMainPanel);
		mMainSplitPane.setLeftComponent(mLeftMainPanel);
		mMainSplitPane.setOneTouchExpandable(true);

		this.getContentPane().add(mMainSplitPane);

		loadProperties();
		setCloseOperation();
	}

	/**
	 * Initializes the left hand side of the main panel
	 */
	private void initLeftHandSide() {
		try {
			mConnectionTree = new RESTConnectionTree(Main.getDataservicePersister().loadDataserviceConnections(this));
		} catch (PropertyException e) {
			LOGGER.error(e.toString(), e);
			DialogHelper.showErrorDialog(e.toString(), e.getClass().getSimpleName());
		}

		mConnectionTree.expandAllNodes();
		mConnectionTree.addMouseListener(new ConnectionTreeMainWindowListener(this));

		mTreeRenderer = new ConnectionTreeCellRenderer();
		mConnectionTree.setCellRenderer(mTreeRenderer);

		mConnectionScrollPane = new JScrollPane(mConnectionTree);

		mLeftMainPanel = new JPanel();
		mLeftMainPanel.setLayout(new GridBagLayout());

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, mLeftMainPanel, mConnectionScrollPane,
				LayoutHelper.LABEL_INSETS);
	}

	public void changeParameterPanel() {
		if (mLeftMainPanel.getComponents().length > 1) {
			mLeftMainPanel.remove(mLeftMainPanel.getComponents().length - 1);
		}

		mJpParameter = new JPanel();
		mJpParameter.setLayout(new GridBagLayout());
		mJpParameter.setBorder(BorderFactory.createTitledBorder("Parameter"));

		mJbParameterSave = new JButton("Save");
		mJbParameterSave.setEnabled(false);
		mJbParameterSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				savePropertyChanges();
			}
		});

		mJbParameterReset = new JButton("Reset");
		mJbParameterReset.setEnabled(false);
		mJbParameterReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				resetPropertyChanges();
			}
		});

		Object selectedComponent = mConnectionTree.getLastSelectedPathComponent();

		if (selectedComponent instanceof DataserviceRestConnectionImpl) {
			mJpParameterValues = new DataServiceParameterPanel(((DataserviceData) selectedComponent).copy());
			mJbParameterSave.setEnabled(((DataserviceRestConnectionImpl) selectedComponent).isNotPersised());
			mJbParameterReset.setEnabled(((DataserviceRestConnectionImpl) selectedComponent).isNotPersised());
		} else if (selectedComponent instanceof MapServerRestConnectionImpl) {
			mJpParameterValues = new MapServerParameterPanel(((MapServerData) selectedComponent).copy());
			mJbParameterSave.setEnabled(((MapServerRestConnectionImpl) selectedComponent).isNotPersised());
			mJbParameterReset.setEnabled(((MapServerRestConnectionImpl) selectedComponent).isNotPersised());
		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			mJpParameterValues = new SubscriptionParameterPanel(((SubscriptionData) selectedComponent).copy());
			mJbParameterSave.setEnabled(((RestSubscriptionImpl) selectedComponent).isNotPersised());
			mJbParameterReset.setEnabled(((RestSubscriptionImpl) selectedComponent).isNotPersised());
		}

		if (mJpParameterValues != null) {
			mJpParameterValues.addParameterListener(new ParameterListener() {
				@Override
				public void parameterChanged() {
					Data changedData = mJpParameterValues.getData();
					propertiesDataChanged(changedData);
				}
			});

			mJpParameterSouth = new JPanel();
			mJpParameterSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

			mJpParameterSouth.add(mJbParameterReset);
			mJpParameterSouth.add(mJbParameterSave);

			LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 0.0, mJpParameter, mJpParameterValues,
					LayoutHelper.LABEL_INSETS);
			LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 0.0, mJpParameter, mJpParameterSouth, LayoutHelper.LABEL_INSETS);
			LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 0.0, mLeftMainPanel, mJpParameter, LayoutHelper.LABEL_INSETS);
			mLeftMainPanel.updateUI();
		}
	}

	/**
	 * Adds a component to a JTabbedPane with a little "close tab" button on the
	 * right side of the tab.
	 *
	 * @param cTab
	 *            the ConnectionTab that should be added
	 */
	private void addClosableTab(JTabbedPane tabPane, final ConnectionTab cTab) {
		if (mCancelIcon == null) {
			mCancelIcon = new ImageIcon[2];
			mCancelIcon[0] = new ImageIcon(MainWindow.class.getClassLoader().getResource("close.png").getPath());
			mCancelIcon[1] = new ImageIcon(MainWindow.class.getClassLoader().getResource("closeHover.png").getPath());
		}
		tabPane.addTab(cTab.getConnName(), cTab);
		int pos = tabPane.indexOfComponent(cTab);

		FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);

		JPanel pnlTab = new JPanel(f);
		pnlTab.setOpaque(false);

		JButton btnClose = new JButton();
		btnClose.setOpaque(false);

		btnClose.setRolloverIcon(mCancelIcon[1]);
		btnClose.setRolloverEnabled(true);
		btnClose.setIcon(mCancelIcon[0]);

		btnClose.setBorder(null);
		btnClose.setFocusable(false);

		pnlTab.add(new JLabel(cTab.getConnName()));
		pnlTab.add(btnClose);

		tabPane.setTabComponentAt(pos, pnlTab);

		/**
		 * Remove the current tab from the tab pane and from the MotionController if it
		 * is closed in the GUI.
		 */
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeConnectionTab(cTab);
			}
		};
		btnClose.addActionListener(listener);

		tabPane.setSelectedComponent(cTab);
	}

	/**
	 * Initializes the right hand side of the main panel
	 */
	private void initRightHandSide() {
		mTabbedConnectionPaneLeft = new JTabbedPane();

		/**
		 * Whenever the current tab inside the GUI changes, the MotionControllerHandler
		 * instance is informed.
		 */
		mTabbedConnectionPaneLeft.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
				ConnectionTab currentTab = (ConnectionTab) sourceTabbedPane.getSelectedComponent();
				if (currentTab != null) {
					mMotionControllerHandler.setCurrentConnectionTab(currentTab);
					LOGGER.debug("Tab changed to " + currentTab.getConnName());
				}
			}
		});

		mTabbedConnectionPaneRight = new JTabbedPane();

		/**
		 * Whenever the current tab inside the GUI changes, the MotionControllerHandler
		 * instance is informed.
		 */
		mTabbedConnectionPaneRight.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
				ConnectionTab currentTab = (ConnectionTab) sourceTabbedPane.getSelectedComponent();
				if (currentTab != null) {
					mMotionControllerHandler.setCurrentConnectionTab(currentTab);
					LOGGER.debug("Tab changed to " + currentTab.getConnName());
				}
			}
		});

		mGraphPanel = new JPanel();
		mGraphPanel.setLayout(new GridLayout());
		mGraphPanel.add(mTabbedConnectionPaneLeft);
		if (mDualView) {
			mGraphPanel.add(mTabbedConnectionPaneRight);
		}

		boolean useNavigationPanel = mConfig.getBoolean(VisualizationConfig.KEY_GUI_NAVIGATION_PANEL,
				VisualizationConfig.DEFAULT_VALUE_GUI_NAVIGATION_PANEL);
		if (useNavigationPanel) {
			mProjectionView = new NavigationPanel();

			mRightMainPanelSplitPane = new JSplitPane();
			mRightMainPanelSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			mRightMainPanelSplitPane.setResizeWeight(0.125);
			mRightMainPanelSplitPane.setRightComponent(mGraphPanel);
			mRightMainPanelSplitPane.setLeftComponent(mProjectionView);
			mRightMainPanelSplitPane.setOneTouchExpandable(true);
		} else {
			mRightMainPanel = mGraphPanel;
		}
	}

	public void openConnectedMapServerConnections() {
		Dataservices dataservices = (Dataservices) mConnectionTree.getModel().getRoot();

		int index = 0;
		for (Data dataservice : dataservices.getSubData()) {
			for (Data data : dataservice.getSubData()) {
				if (data instanceof MapServerRestConnectionImpl) {
					MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) data;
					if (mapServerConnection.isConnected()) {
						showConnectedGraph(mapServerConnection);
						
						if (mapServerConnection.getConnectionName().equals("default") || (dataservice.getSubData().size() == 1) || (index == 0)) {	// TODO maybe change to first entry in YAML file for dataservice connection; MAP server connection is not known on GUI side
							LOGGER.debug("Found MAP server connection named \"default\" OR only 1 connection is present OR first entry of many; therefore selecting it in the connection tree");
							TreePath pathToMapServerConnection = new TreePath(new Object[] {dataservices, dataservice, data});
							mConnectionTree.setSelectionPath(pathToMapServerConnection);
						}
					}
					index++;
				}
			}
		}
	}

	public void reopenConnectionTabs() {
		for (Component c : getOpenedTabs()) {
			if (c instanceof ConnectionTab) {
				ConnectionTab connectionTab = (ConnectionTab) c;
				closeConnectionTab(connectionTab);
			}
		}
		openConnectedMapServerConnections();
	}

	private void showConnectedGraph(MapServerRestConnectionImpl mapServerConnection) {
		if (!mapServerConnection.isGraphStarted()) {
			mapServerConnection.initGraph();
		}

		openConnectionTab(mapServerConnection.getConnectionTab());
	}

	/**
	 * Loads Properties
	 */
	private void loadProperties() {
		setLocation(
				mConfig.getInt(VisualizationConfig.KEY_WINDOW_POSITION_X,
						VisualizationConfig.DEFAULT_VALUE_WINDOW_POSITION_X),
				mConfig.getInt(VisualizationConfig.KEY_WINDOW_POSITION_Y,
						VisualizationConfig.DEFAULT_VALUE_WINDOW_POSITION_Y));
		setPreferredSize(new Dimension(
				mConfig.getInt(VisualizationConfig.KEY_WINDOW_WIDTH, VisualizationConfig.DEFAULT_VALUE_WINDOW_WIDTH),
				mConfig.getInt(VisualizationConfig.KEY_WINDOW_HEIGHT,
						VisualizationConfig.DEFAULT_VALUE_WINDOW_HEIGHT)));
		mMainSplitPane.setDividerLocation(mConfig.getInt(VisualizationConfig.KEY_WINDOW_DIVIDER,
				VisualizationConfig.DEFAULT_VALUE_WINDOW_DIVIDER));
	}

	/**
	 * Adds a listener to the window in order to save the windows' position and size
	 * and sets the close operation
	 */
	private void setCloseOperation() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				try {
					mConfig.set(VisualizationConfig.KEY_WINDOW_POSITION_X, (int) getLocationOnScreen().getX());
					mConfig.set(VisualizationConfig.KEY_WINDOW_POSITION_Y, (int) getLocationOnScreen().getY());
					mConfig.set(VisualizationConfig.KEY_WINDOW_WIDTH, getWidth());
					mConfig.set(VisualizationConfig.KEY_WINDOW_HEIGHT, getHeight());
					mConfig.set(VisualizationConfig.KEY_WINDOW_DIVIDER, mMainSplitPane.getDividerLocation());
				} catch (PropertyException e) {
					LOGGER.fatal(e.toString(), e);
					throw new RuntimeException("could not save properties");
				}
				System.exit(0);
			}
		});
	}

	static class SupportedLaF {
		JCheckBoxMenuItem menuItem;
		String name;
		LookAndFeel laf;

		SupportedLaF(String name, LookAndFeel laf) {
			this.name = name;
			this.laf = laf;
			this.menuItem = new JCheckBoxMenuItem(name);
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Set the look and feel of java depending on the operating system.
	 */
	private void setLookAndFeel() {
		LOGGER.trace("Method setLookAndFeel() called.");

		UIManager.LookAndFeelInfo[] installedLafs = UIManager.getInstalledLookAndFeels();
		for (UIManager.LookAndFeelInfo lafInfo : installedLafs) {
			try {
				@SuppressWarnings("rawtypes")
				Class lnfClass = Class.forName(lafInfo.getClassName());
				LookAndFeel laf = (LookAndFeel) (lnfClass.newInstance());
				if (laf.isSupportedLookAndFeel()) {
					String name = lafInfo.getName();
					supportedLaFs.add(new SupportedLaF(name, laf));

				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				DialogHelper.showErrorDialog(e.toString(), e.getClass().getSimpleName());
				continue;
			}
		}

		for (SupportedLaF lAf : supportedLaFs) {
			if (lAf.name.equals("Windows")) {
				LookAndFeel laf = lAf.laf;
				try {
					UIManager.setLookAndFeel(laf);
					lAf.menuItem.setSelected(true);
				} catch (UnsupportedLookAndFeelException e) {
					LOGGER.error(e.getMessage(), e);
					DialogHelper.showErrorDialog(e.toString(), e.getClass().getSimpleName());
				}
			}
		}
	}

	public List<SupportedLaF> getSupportedLaFs() {
		return supportedLaFs;
	}

	public RESTConnectionTree getConnectionTree() {
		return mConnectionTree;
	}

	public Component[] getOpenedTabs() {
		Component[] leftTabs = mTabbedConnectionPaneLeft.getComponents();
		Component[] rightTabs = mTabbedConnectionPaneRight.getComponents();

		return concatArrays(leftTabs, rightTabs);
	}

	private Component[] concatArrays(Component[] one, Component[] two) {
		Component[] result = Arrays.copyOf(one, one.length + two.length);
		System.arraycopy(two, 0, result, one.length, two.length);
		return result;
	}

	public void showAllMapServerConnections(boolean b) {
		mConnectionTree.showAllMapServerConnections(b);
	}

	public void showAllSubscriptions(boolean b) {
		mConnectionTree.showAllSubscriptions(b);
	}

	public void propertiesDataChanged(Data changedData) {
		mJbParameterSave.setEnabled(true);
		mJbParameterReset.setEnabled(true);

		Object selectedComponent = mConnectionTree.getLastSelectedPathComponent();
		if (selectedComponent instanceof DataserviceRestConnectionImpl) {
			DataserviceRestConnectionImpl dataserviceConnection = (DataserviceRestConnectionImpl) selectedComponent;
			if (!dataserviceConnection.isNotPersised()) {
				dataserviceConnection.setOldData(((DataserviceData) dataserviceConnection).copy());
			}
			dataserviceConnection.changeData((DataserviceData) changedData);
			dataserviceConnection.setNotPersised(true);

		} else if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;
			if (!mapServerConnection.isNotPersised()) {
				mapServerConnection.setOldData(((MapServerData) mapServerConnection).copy());
			}
			mapServerConnection.changeData((MapServerData) changedData);
			mapServerConnection.setNotPersised(true);

		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;
			if (!subscription.isNotPersised()) {
				subscription.setOldData(((SubscriptionData) subscription).copy());
			}
			subscription.changeData((SubscriptionData) changedData);
			subscription.setNotPersised(true);
		}

		mConnectionTree.updateUI();
	}

	public void resetPropertyChanges() {
		Object selectedComponent = mConnectionTree.getLastSelectedPathComponent();

		if (selectedComponent instanceof DataserviceRestConnectionImpl) {
			DataserviceRestConnectionImpl dataserviceConnection = (DataserviceRestConnectionImpl) selectedComponent;
			dataserviceConnection.resetData();
			dataserviceConnection.setNotPersised(false);

		} else if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;
			mapServerConnection.resetData();
			mapServerConnection.setNotPersised(false);

		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;
			subscription.resetData();
			subscription.setNotPersised(false);

		}
		mConnectionTree.updateUI();
		changeParameterPanel();
	}

	public void savePropertyChanges() {
		Object selectedComponent = mConnectionTree.getLastSelectedPathComponent();

		if (selectedComponent instanceof DataserviceRestConnectionImpl) {
			DataserviceRestConnectionImpl dataserviceConnection = (DataserviceRestConnectionImpl) selectedComponent;
			try {
				Main.getDataservicePersister().persist(dataserviceConnection);
				dataserviceConnection.setNotPersised(false);
			} catch (PropertyException e) {
				LOGGER.error(e.toString());
				DialogHelper.showErrorDialog(e.toString(), e.getClass().getSimpleName());
			}

		} else if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;

			try {
				RestHelper.saveMapServerConnection(mapServerConnection.getDataserviceConnection(), mapServerConnection);
				mapServerConnection.setNotPersised(false);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONException e) {
				LOGGER.error(e.toString());
				DialogHelper.showErrorDialog(e.toString(), e.getClass().getSimpleName());
			}

		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;
			Data parentData = mConnectionTree.getSelectedParentData();
			if (parentData instanceof MapServerRestConnectionImpl) {
				MapServerRestConnectionImpl connectionData = (MapServerRestConnectionImpl) parentData;
				try {
					RestHelper.saveSubscription(connectionData.getDataserviceConnection(), connectionData.getName(),
							subscription);
					subscription.setNotPersised(false);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONException
						| ConnectionException e) {
					LOGGER.error(e.toString());
					DialogHelper.showErrorDialog(e.toString(), e.getClass().getSimpleName());
				}
			}
		}
		mConnectionTree.updateUI();
		mJbParameterSave.setEnabled(false);
		mJbParameterReset.setEnabled(false);
	}

	public void showConnectionTreePopupMenu(int x, int y) {
		TreePath mouseTreePath = mConnectionTree.getClosestPathForLocation(x, y);

		selectPath(mouseTreePath);

		Object selectedComponent = mouseTreePath.getLastPathComponent();

		ConnectionTreePopupMenu popUp = new ConnectionTreePopupMenu(mConnectionTree, this, (Data) selectedComponent);
		popUp.show(mConnectionTree, x, y);
	}

	private boolean isTabOpen(ConnectionTab tab) {
		for (Component openTab : mTabbedConnectionPaneLeft.getComponents()) {
			if (openTab.equals(tab)) {
				return true;
			}
		}

		for (Component openTab : mTabbedConnectionPaneRight.getComponents()) {
			if (openTab.equals(tab)) {
				return true;
			}
		}

		return false;
	}

	public void openMapServerConnectionTab() {
		Object selectedComponent = mConnectionTree.getLastSelectedPathComponent();
		if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;
			ConnectionTab toOpenTab = mapServerConnection.getConnectionTab();

			if (!mapServerConnection.isGraphStarted()) {
				mapServerConnection.initGraph();
			}

			if (isTabOpen(toOpenTab)) {
				closeConnectionTab(toOpenTab.getConnName());
			} else {
				openConnectionTab(toOpenTab);
			}
		}
	}

	private void openConnectionTab(ConnectionTab tab) {
		int openTabSizeLeft = mTabbedConnectionPaneLeft.getComponents().length;
		int openTabSizeRight = mTabbedConnectionPaneRight.getComponents().length;

		if (mDualView && openTabSizeLeft > openTabSizeRight) {
			addClosableTab(mTabbedConnectionPaneRight, tab);
		} else {
			addClosableTab(mTabbedConnectionPaneLeft, tab);
		}
	}

	private ConnectionTab getConntectionTab(String connectionTabName) {
		for (Component openTab : mTabbedConnectionPaneLeft.getComponents()) {
			if (openTab instanceof ConnectionTab) {
				ConnectionTab openConnectionTab = (ConnectionTab) openTab;

				if (openConnectionTab.getConnName().equals(connectionTabName)) {
					return openConnectionTab;
				}
			}
		}

		for (Component openTab : mTabbedConnectionPaneRight.getComponents()) {
			if (openTab instanceof ConnectionTab) {
				ConnectionTab openConnectionTab = (ConnectionTab) openTab;

				if (openConnectionTab.getConnName().equals(connectionTabName)) {
					return openConnectionTab;
				}
			}
		}

		return null;
	}

	public void closeConnectionTab(String connectionTabName) {
		ConnectionTab toCloseTab = getConntectionTab(connectionTabName);
		if (toCloseTab != null) {
			closeConnectionTab(toCloseTab);
		}
	}

	private void closeConnectionTab(ConnectionTab connectionTab) {
		connectionTab.finishGraphContainer();
		mTabbedConnectionPaneLeft.remove(connectionTab);
		mTabbedConnectionPaneRight.remove(connectionTab);
		mMotionControllerHandler.removeConnectionTab(connectionTab);
	}

	public void mouseDoubleClicked() {
		Object selectedComponent = mConnectionTree.getLastSelectedPathComponent();
		if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;

			if (subscription.isActive()) {
				subscription.stopSubscription();
			} else {
				subscription.startSubscription();
			}

			mConnectionTree.updateUI();
		}
	}

	public void selectPath(TreePath newPath) {
		mConnectionTree.setSelectionPath(newPath);
		changeParameterPanel();
	}

	public void showDualViewGraph() {
		if (!mDualView) {
			mDualView = true;
			mGraphPanel.add(mTabbedConnectionPaneRight);
			reopenConnectionTabs();
		}
	}

	public void showSingleViewGraph() {
		if (mDualView) {
			mDualView = false;
			mGraphPanel.remove(mTabbedConnectionPaneRight);
			reopenConnectionTabs();
		}
	}
}
