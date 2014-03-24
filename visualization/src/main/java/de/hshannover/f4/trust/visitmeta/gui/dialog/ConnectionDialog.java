package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.datawrapper.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;

public class ConnectionDialog extends JDialog{

	private static final long serialVersionUID = 3274298974215759835L;

	private static final Logger log = Logger.getLogger(ConnectionDialog.class);

	private JTextAreaAppander mJTextAreaAppander;

	private Insets mXinsets;
	private Insets mLblInsets;
	private Insets mNullInsets;

	private JTabbedPane mJtpMain;

	private JPanel mJpSouth;

	private JButton mJbClose;
	private JButton mJbSave;

	private MapServerPanel mConnectionPanelMapServer;
	private DataServicePanel mConnectionPanelDataService;

	private GuiController mGuiController;

	public static void main(String[] args) {
		ConnectionDialog temp = new ConnectionDialog();
		temp.setVisible(true);
	}

	public ConnectionDialog() {
		mJTextAreaAppander = new JTextAreaAppander();
		log.addAppender(mJTextAreaAppander);

		// Hilfsobjekte, damit alle Abstaende harmonisch sind.
		mNullInsets= new Insets(0,0,0,0); // keine Abstaende
		mLblInsets= new Insets(2,5,2,2); // Label-Abstaende
		mXinsets= new Insets(0,10,0,10); // Eingabkomponenten-Abstaende

		createDialog();
		createPanels();

		pack();

		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
	}

	public ConnectionDialog(GuiController guiController) {
		this();
		mGuiController = guiController;
	}

	public void createDialog() {
		setTitle("Manage REST connections");
		setModal(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowListener() {
			@Override public void windowClosing(WindowEvent arg0) {
				dispose();
			}
			@Override public void windowClosed(WindowEvent arg0) {}
			@Override public void windowActivated(WindowEvent arg0) {}
			@Override public void windowDeactivated(WindowEvent arg0) {}
			@Override public void windowDeiconified(WindowEvent arg0) {}
			@Override public void windowIconified(WindowEvent arg0) {}
			@Override public void windowOpened(WindowEvent arg0) {}
		});
	}

	public void createPanels() {
		getContentPane().setLayout(new GridBagLayout());

		mJpSouth = new JPanel();
		mJpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

		mJbClose = new JButton("Close");
		mJbClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});

		mJbSave = new JButton("Save");

		mJpSouth.add(mJbClose);
		mJpSouth.add(mJbSave);

		mConnectionPanelMapServer = new MapServerPanel(this);
		mConnectionPanelDataService = new DataServicePanel(this);

		mJtpMain = new JTabbedPane();
		mJtpMain.add("Map Server Connections", mConnectionPanelMapServer);
		mJtpMain.add("Dataservice Connections", mConnectionPanelDataService);

		//			 x  y  w  h  wx   wy
		addComponent(0, 0, 1, 1, 1.0, 1.0, getContentPane(), mJtpMain, mLblInsets);
		addComponent(0, 1, 1, 1, 0.0, 0.0, getContentPane(), mJpSouth, mLblInsets);
	}

	/**
	 * Hilfsroutine beim Hinzufuegen einer Komponente zu einem
	 * Container im GridBagLayout.
	 * Die Parameter sind Constraints beim Hinzufuegen.
	 * @param x x-Position
	 * @param y y-Position
	 * @param width Breite in Zellen
	 * @param height Hoehe in Zellen
	 * @param weightx Gewicht
	 * @param weighty Gewicht
	 * @param cont Container
	 * @param comp Hinzuzufuegende Komponente
	 * @param insets Abstaende rund um die Komponente
	 */
	private static void addComponent(int x, int y, int width, int height, double weightx, double weighty, Container cont, Component comp, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x; gbc.gridy = y;
		gbc.gridwidth = width; gbc.gridheight = height;
		gbc.weightx = weightx; gbc.weighty = weighty;
		gbc.insets= insets;
		cont.add(comp, gbc);
	}

	private static void switchJPanel(int x, int y, int width, int height, double weightx, double weighty, JPanel cont, Component compToAdd, Component compToRemove, Insets insets) {
		cont.remove(compToRemove);
		addComponent(x, y, width, height, weightx, weighty, cont, compToAdd, insets);
		cont.updateUI();
	}

	public static List<DataserviceConnection> getDataserviceConnectionsFromProperties(){
		ArrayList<DataserviceConnection> dataserviceList = new ArrayList<DataserviceConnection>();
		int connectionCount = Integer.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT, "0"));

		for(int i=0; i<connectionCount; i++){
			String name = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(i), "localhost");
			String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(i), "http://localhost:8000");
			boolean rawXml = Boolean.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(i), "true").toLowerCase());

			dataserviceList.add(new DataserviceConnection(name, url, rawXml));
		}

		return dataserviceList;
	}

	private abstract class TabPanel extends JPanel{

		private static final long serialVersionUID = -1419963248354632788L;


		protected ConnectionDialog mContext;

		protected JSplitPane mJspContent;
		protected JPanel mJpLeftSplitPane;
		protected JPanel mJpRightSplitPane;
		protected JPanel mJpConnectionParameter;
		protected JPanel mJpLog;

		private JPanel mJpAddDeleteCopy;
		//		private JPanel mJpSouth;

		private JTextArea mJtaLogWindows;
		private JScrollPane mJspLogWindows;

		protected JButton mJbAdd;
		protected JButton mJbDelete;
		protected JButton mJbCopy;

		protected JLabel mJlNoConnectionsYet;

		private DefaultListModel<DataserviceConnection> mListModelDataService;

		private TabPanel(ConnectionDialog context){
			mContext = context;
			createPanels();
			addConnectionList();

			if (!((DefaultListModel<?>)getConnectionList().getModel()).isEmpty()) {
				getConnectionList().setSelectedIndex(0);
			}
		}

		private void createPanels() {
			setLayout(new GridBagLayout());

			mJpLeftSplitPane= new JPanel();
			mJpLeftSplitPane.setLayout(new GridBagLayout());
			mJpLeftSplitPane.setBorder(BorderFactory.createTitledBorder("Connection List"));

			mJpConnectionParameter= new JPanel();
			mJpConnectionParameter.setLayout(new GridBagLayout());
			mJpConnectionParameter.setBorder(BorderFactory.createTitledBorder("Connection Parameter"));

			mJpLog= new JPanel();
			mJpLog.setLayout(new GridBagLayout());
			mJpLog.setBorder(BorderFactory.createTitledBorder("Connection Log"));

			mJpRightSplitPane= new JPanel();
			mJpRightSplitPane.setLayout(new GridBagLayout());

			mJbAdd = new JButton("New");
			mJbDelete = new JButton("Delete");
			mJbCopy = new JButton("Duplicate");

			mJpAddDeleteCopy = new JPanel();
			mJpAddDeleteCopy.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			mJpAddDeleteCopy.add(mJbAdd);
			mJpAddDeleteCopy.add(mJbDelete);
			mJpAddDeleteCopy.add(mJbCopy);

			//			mJpSouth = new JPanel();
			//			mJpSouth.setLayout(new FlowLayout(FlowLayout.LEFT));
			//


			mJlNoConnectionsYet = new JLabel("No connections yet.");
			mJlNoConnectionsYet.setHorizontalAlignment(SwingConstants.CENTER);

			mJspContent = new JSplitPane();
			mJspContent.setLeftComponent(mJpLeftSplitPane);
			mJspContent.setRightComponent(mJpRightSplitPane);

			mJtaLogWindows = new JTextArea(5, 40);
			mJtaLogWindows.setEditable(false);
			// for append logging messages
			mJTextAreaAppander.addJTextArea(mJtaLogWindows);

			mJspLogWindows = new JScrollPane(mJtaLogWindows);

			//			 x  y  w  h  wx   wy
			addComponent(0, 2, 1, 1, 0.0, 0.0, mJpLeftSplitPane, mJpAddDeleteCopy, mNullInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJspContent, mLblInsets);
			addComponent(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, mJlNoConnectionsYet, mLblInsets);
			addComponent(0, 0, 1, 1, 1.0, 1.0, mJpRightSplitPane, mJpConnectionParameter, mLblInsets);
			addComponent(0, 0, 1, 1, 0.0, 0.0, mJpLog, mJspLogWindows, mLblInsets);
			addComponent(0, 1, 1, 1, 0.0, 0.0, mJpRightSplitPane, mJpLog, mLblInsets);
			//			addComponent(0, 3, 1, 1, 0.0, 0.0, mJpLeftSplitPane, mJpSouth, mNullInsets);
		}

		private void addConnectionList() {
			//			 x  y  w  h  wx   wy
			addComponent(0, 1, 1, 1, 1.0, 1.0, mJpLeftSplitPane, getConnectionList(), mLblInsets);
		}

		protected abstract JPanel getParameterPanel();
		protected abstract JList<?> getConnectionList();

	}

	private class MapServerPanel extends TabPanel{

		private static final long serialVersionUID = -7620433767757449461L;

		private JList<RESTConnection> mJlMapServerConnections;
		private DefaultListModel<RESTConnection> mListModelMapServer;

		private MapServerParameterPanel mParameterPanel;

		private RESTConnection mPreviousConnection;

		private JComboBox<DataserviceConnection> mJcbDataServiceConnection;
		private JLabel mJlDataservice;
		private JPanel mJpDataserviceComboBox;

		public void updateDataserviceComboBox(){
			mJcbDataServiceConnection.removeAllItems();

			for(DataserviceConnection dc: getDataserviceConnectionsFromProperties()){
				mJcbDataServiceConnection.addItem(dc);
			}
		}

		public MapServerPanel(ConnectionDialog context) {
			super(context);

			mParameterPanel = new MapServerParameterPanel();

			mJcbDataServiceConnection = new JComboBox<DataserviceConnection>();

			updateDataserviceComboBox();

			mJpDataserviceComboBox = new JPanel();
			mJpDataserviceComboBox.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			mJlDataservice = new JLabel("Dataservice:");

			mJpDataserviceComboBox.add(mJlDataservice);
			mJpDataserviceComboBox.add(mJcbDataServiceConnection);

			addComponent(0, 0, 1, 1, 0.0, 0.0, mJpLeftSplitPane, mJpDataserviceComboBox, mNullInsets);

			addListeners();

			DataserviceConnection dConnection = (DataserviceConnection) mJcbDataServiceConnection.getSelectedItem();
			updateRestConnectionsList(dConnection);

			if (!mListModelMapServer.isEmpty()) {
				mJlMapServerConnections.setSelectedIndex(0);
			}
		}

		private void addListeners() {
			mJlMapServerConnections.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					RESTConnection param = mJlMapServerConnections.getSelectedValue();
					if (param == null) {
						return;
					}

					if(mPreviousConnection != null){
						mPreviousConnection.update(mParameterPanel.mJtfName.getText().trim(), mParameterPanel.mJtfUrl.getText().trim(), mParameterPanel.mJcbDump.isSelected());
						mPreviousConnection.setUsername(mParameterPanel.mJtfUsername.getText().trim());
						mPreviousConnection.setPassword(new String(mParameterPanel.mJtfPassword.getPassword()).trim());
						mPreviousConnection.setConnectAtStartUp(mParameterPanel.mJcbConnectingAtStartUp.isSelected());
					}

					mParameterPanel.mJtfName.setText(param.getName());
					mParameterPanel.mJtfUrl.setText(param.getUrl());
					mParameterPanel.mJcbDump.setSelected(param.isDumping());
					mParameterPanel.mJtfUsername.setText(param.getUsername());
					mParameterPanel.mJtfPassword.setText(param.getPassword());
					mParameterPanel.mJcbBasicAuthentication.setSelected(param.isBasicAuthentication());
					mParameterPanel.mJcbConnectingAtStartUp.setSelected(param.isConnectAtStartUp());

					switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(), mJlNoConnectionsYet, mLblInsets);

					mPreviousConnection = param;
				}
			});

			mJbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String name = "NewConnection" + (mListModelMapServer.getSize() + 1);
					String url = "https://localhost:8443";
					RESTConnection param = new RESTConnection((DataserviceConnection) mJcbDataServiceConnection.getSelectedItem(), name, url, false);
					param.setBasicAuthentication(true);

					mListModelMapServer.add(mListModelMapServer.getSize(), param);

					switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(), mJlNoConnectionsYet, mLblInsets);

					mJlMapServerConnections.setSelectedIndex(mListModelMapServer.getSize() - 1);

				}
			});

			mJbDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = mJlMapServerConnections.getSelectedIndex();
					if (index >= 0) {
						mListModelMapServer.remove(index);
						if (!mListModelMapServer.isEmpty()) {
							index = (index == 0) ? 0 : index - 1;
							mJlMapServerConnections.setSelectedIndex(index);
						} else {
							switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, mJlNoConnectionsYet, getParameterPanel(), mLblInsets);
						}
					}
				}
			});

			mJbCopy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					RESTConnection param =  mJlMapServerConnections.getSelectedValue().clone();
					mListModelMapServer.add(mListModelMapServer.getSize(), param);
					mJlMapServerConnections.setSelectedIndex(mListModelMapServer.getSize() - 1);
				}
			});


			mJbSave.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(mJtpMain.getSelectedComponent() == mConnectionPanelMapServer){
						DataserviceConnection tmpCon = mJlMapServerConnections.getSelectedValue().getDataserviceConnection();

						ClientConfig config = new DefaultClientConfig();
						Client client = Client.create(config);

						URI uri_connect = UriBuilder.fromUri(tmpCon.getUrl()).build();
						WebResource temp1 = client.resource(uri_connect);

						JSONObject jObj = new JSONObject();
						try {

							jObj.put("url", mParameterPanel.mJtfUrl.getText().trim());
							jObj.put("user", mParameterPanel.mJtfUsername.getText().trim());
							jObj.put("userPass", new String(mParameterPanel.mJtfPassword.getPassword()).trim());
							jObj.put("connectAtStartUp", mParameterPanel.mJcbConnectingAtStartUp.isSelected());
							jObj.put("basicAuthentication", mParameterPanel.mJcbBasicAuthentication.isSelected());

						} catch (JSONException e1) {
							e1.printStackTrace();
						}

						String response = temp1.path(mJlMapServerConnections.getSelectedValue().getName()).type(MediaType.APPLICATION_JSON).put(String.class, jObj);
						System.out.println(response);

						GraphContainer connection = new GraphContainer(mJlMapServerConnections.getSelectedValue().getName(), mJlMapServerConnections.getSelectedValue());
						mGuiController.addConnection(connection);

						log.info("new Map-Server connection is stored in " + tmpCon.getName());

						//										Connection vConnection = FactoryConnection.getConnection(ConnectionType.REST, mJlMapServerConnections.getSelectedValue());
						//										Calculator vCalculator = FactoryCalculator.getCalculator(CalculatorType.JUNG);
						//
						//										FacadeNetwork vNetwork = new FacadeNetwork(vConnection);
						//										FacadeLogic vLogic = new FacadeLogic(vNetwork, vCalculator);
						//										GraphConnection connController = new GraphConnection(vLogic);
						//
						//										mGuiController.addConnection(mJlMapServerConnections.getSelectedValue().getName(), connController, mJlMapServerConnections.getSelectedValue());
						//
						//										Thread vThreadNetwork = new Thread(vNetwork);
						//										Thread vThreadLogic = new Thread(vLogic);
						//
						//										vThreadNetwork.start();
						//										vThreadLogic.start();
					}
				}

			});

			//			mJcbDataServiceConnection.addPropertyChangeListener(new PropertyChangeListener() {
			//
			//				@Override
			//				public void propertyChange(PropertyChangeEvent arg0) {
			//					System.out.println("propertyChange " + arg0);
			//					if(arg0.getOldValue() == null && arg0.getNewValue() == ){
			//						System.out.println("propertyChange != null " + arg0);
			//						DataserviceConnection dConnection = (DataserviceConnection) mJcbDataServiceConnection.getSelectedItem();
			//						updateRestConnectionsList(dConnection);
			//
			//						if (!mListModelMapServer.isEmpty()) {
			//							mJlMapServerConnections.setSelectedIndex(0);
			//						}
			//					}
			//				}
			//			});

			mJcbDataServiceConnection.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent event) {
					System.out.println("itemStateChanged " + event);
					if(event.getStateChange() == ItemEvent.SELECTED){
						System.out.println("itemStateChanged = SELECTED" + event);
						DataserviceConnection dConnection = (DataserviceConnection) event.getItem();

						updateRestConnectionsList(dConnection);

						if (!mListModelMapServer.isEmpty()) {
							mJlMapServerConnections.setSelectedIndex(0);
						}
					}
				}});
		}

		private void updateRestConnectionsList(DataserviceConnection dConnection){
			log.info("Update connection list from Dataservice(" + dConnection.getName() + ")");
			mListModelMapServer.removeAllElements();

			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);

			URI uri_connect = UriBuilder.fromUri(dConnection.getUrl()).build();
			WebResource temp1 = client.resource(uri_connect);
			JSONObject jsonResponse = temp1.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
			System.out.println(jsonResponse);

			Iterator<String> ii = jsonResponse.keys();
			while(ii.hasNext()){
				String jKey = ii.next();
				JSONObject jsonConnection;
				try {
					jsonConnection = jsonResponse.getJSONObject(jKey);
					RESTConnection restConn = new RESTConnection(dConnection, jKey, jsonConnection.getString("URL"), false);
					restConn.setBasicAuthentication(jsonConnection.optBoolean("BasicAuthentication", true));
					restConn.setUsername(jsonConnection.getString("Username"));
					restConn.setPassword(jsonConnection.getString("Password"));
					restConn.setConnectAtStartUp(jsonConnection.optBoolean("connectAtStartUp", false));
					restConn.setDumping(jsonConnection.optBoolean("Dumping", false));
					restConn.setMaxPollResultSize(jsonConnection.optString("MaxPollResultSize"));
					mListModelMapServer.addElement(restConn);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected JPanel getParameterPanel() {
			if(mParameterPanel == null){
				mParameterPanel = new MapServerParameterPanel();
			}
			return mParameterPanel;
		}

		@Override
		protected JList<?> getConnectionList() {
			if(mJlMapServerConnections == null){
				mListModelMapServer = new DefaultListModel<RESTConnection>();

				mJlMapServerConnections = new JList<RESTConnection>();
				mJlMapServerConnections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				mJlMapServerConnections.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
				mJlMapServerConnections.setModel(mListModelMapServer);
			}
			return mJlMapServerConnections;
		}

	}

	private class DataServicePanel extends TabPanel{

		private static final long serialVersionUID = 6943047877185872808L;

		private JList<DataserviceConnection> mJlDataServiceConnections;
		private DefaultListModel<DataserviceConnection> mListModelDataService;

		private DataServiceParameterPanel mParameterPanel;

		private DataserviceConnection mPreviousConnection;

		private DataServicePanel(ConnectionDialog context){
			super(context);

			mParameterPanel = new DataServiceParameterPanel();

			readProperties();
			addListeners();

			if (!mListModelDataService.isEmpty()) {
				mJlDataServiceConnections.setSelectedIndex(0);
			}
		}

		private void addListeners() {
			mJlDataServiceConnections.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					DataserviceConnection param = mJlDataServiceConnections.getSelectedValue();
					if (param == null) {
						return;
					}

					if(mPreviousConnection != null){
						mPreviousConnection.update(mParameterPanel.mJtfName.getText().trim(), mParameterPanel.mJtfUrl.getText().trim(), mParameterPanel.mJcbRawXML.isSelected());
					}

					mParameterPanel.mJtfName.setText(param.getName());
					mParameterPanel.mJtfUrl.setText(param.getUrl());
					mParameterPanel.mJcbRawXML.setSelected(param.isRawXml());

					switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(), mJlNoConnectionsYet, mLblInsets);

					mPreviousConnection = param;
				}
			});

			mJbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String name = "New Connection (" + (mListModelDataService.getSize() + 1) + ")";
					String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_DEFAULT_URL, "http://localhost:8000");
					DataserviceConnection param = new DataserviceConnection(name, url, true);

					mListModelDataService.add(mListModelDataService.getSize(), param);

					switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(), mJlNoConnectionsYet, mLblInsets);

					mJlDataServiceConnections.setSelectedIndex(mListModelDataService.getSize() - 1);

				}
			});

			mJbDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = mJlDataServiceConnections.getSelectedIndex();
					if (index >= 0) {
						mListModelDataService.remove(index);
						if (!mListModelDataService.isEmpty()) {
							index = (index == 0) ? 0 : index - 1;
							mJlDataServiceConnections.setSelectedIndex(index);
						} else {
							switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, mJlNoConnectionsYet, getParameterPanel(), mLblInsets);
						}
					}
				}
			});

			mJbCopy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					DataserviceConnection param =  mJlDataServiceConnections.getSelectedValue().clone();
					mListModelDataService.add(mListModelDataService.getSize(), param);
					mJlDataServiceConnections.setSelectedIndex(mListModelDataService.getSize() - 1);
				}
			});

			mJbSave.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(mJtpMain.getSelectedComponent() == mConnectionPanelDataService){
						persistDataServiceConnections();
						mContext.mConnectionPanelMapServer.updateDataserviceComboBox();
						log.info("DataService Connections was persist");
					}

				}

			});

		}

		private void persistDataServiceConnections(){
			int propertyCount = Integer.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT, "0"));
			for(int i=0; i<propertyCount; i++){
				removeDataServiceConnection(i);
			}

			if(mPreviousConnection != null){
				mPreviousConnection.update(mParameterPanel.mJtfName.getText().trim(), mParameterPanel.mJtfUrl.getText().trim(), mParameterPanel.mJcbRawXML.isSelected());
			}

			int count = mListModelDataService.size();
			PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT, String.valueOf(count));

			for(int i=0; i<count; i++){
				PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(i), mListModelDataService.get(i).getName());
				PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(i), mListModelDataService.get(i).getUrl());
				PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(i), String.valueOf(mListModelDataService.get(i).isRawXml()));
			}
		}

		private void removeDataServiceConnection(int index){
			PropertiesManager.removeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(index));
			PropertiesManager.removeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(index));
			PropertiesManager.removeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(index));

		}

		private void readProperties(){
			for(DataserviceConnection dc: getDataserviceConnectionsFromProperties()){
				addDataserviceConnection(dc);
			}
		}

		private void addDataserviceConnection(DataserviceConnection con){
			mListModelDataService.add(mListModelDataService.getSize(), con);
		}

		@Override
		protected JPanel getParameterPanel() {
			if(mParameterPanel == null){
				mParameterPanel = new DataServiceParameterPanel();
			}
			return mParameterPanel;
		}

		@Override
		protected JList<?> getConnectionList() {
			if(mJlDataServiceConnections == null){
				mListModelDataService = new DefaultListModel<DataserviceConnection>();

				mJlDataServiceConnections = new JList<DataserviceConnection>();
				mJlDataServiceConnections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				mJlDataServiceConnections.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
				mJlDataServiceConnections.setModel(mListModelDataService);
			}
			return mJlDataServiceConnections;
		}

	}

	private class MapServerParameterPanel extends JPanel{

		private static final long serialVersionUID = -3686612903315798696L;

		private JLabel mJlName;
		private JLabel mJlUrl ;
		private JLabel mJlDump;
		private JLabel mJlDumpDescription;
		private JLabel mJlDumpDescription2;
		private JLabel mJlDumpDescription3;
		private JLabel mJlBasicAuthentication;
		private JLabel mJlUsername ;
		private JLabel mJlPassword;
		private JLabel mJlMaxPollResultSize;
		private JLabel mJlConnectingAtStartUp;

		private JTextField mJtfUrl;
		private JTextField mJtfName;
		private JTextField mJtfUsername;
		private JTextField mJtfMaxPollResultSize;

		private JPasswordField mJtfPassword;

		private JCheckBox mJcbDump;
		private JCheckBox mJcbBasicAuthentication;
		private JCheckBox mJcbConnectingAtStartUp;


		private MapServerParameterPanel(){
			createPanels();
			addListeners();
		}

		private void addListeners(){
			final Component test = this;
			mJcbDump.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (mJcbDump.isSelected()) {
						JOptionPane.showMessageDialog(test,
								"Dumping is NOT IF-MAP 2.0 compliant and can only be used with irond.",
								"Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
		}

		private void createPanels() {
			setLayout(new GridBagLayout());

			mJlName = new JLabel("Name");
			mJlUrl = new JLabel("Map-Server Url");
			mJlBasicAuthentication = new JLabel("Basic Authentication");
			mJlBasicAuthentication.setEnabled(false);
			mJlUsername = new JLabel("Username");
			mJlPassword = new JLabel("Password");
			mJlMaxPollResultSize = new JLabel("max-poll-result-size");
			mJlConnectingAtStartUp = new JLabel("Connecting at start-up");
			mJlDump = new JLabel("Dump");
			mJlDumpDescription = new JLabel("Dumping is NOT IF-MAP 2.0 compliant!");
			mJlDumpDescription.setHorizontalAlignment(SwingConstants.CENTER);
			mJlDumpDescription.setEnabled(false);
			mJlDumpDescription2 = new JLabel("That is currently only supported by irond.");
			mJlDumpDescription2.setHorizontalAlignment(SwingConstants.CENTER);
			mJlDumpDescription2.setEnabled(false);
			mJlDumpDescription3 = new JLabel("The dump is used to bootstrap the visualization process.");
			mJlDumpDescription3.setHorizontalAlignment(SwingConstants.CENTER);
			mJlDumpDescription3.setEnabled(false);

			mJtfName = new JTextField();
			mJtfUrl = new JTextField();
			mJcbBasicAuthentication = new JCheckBox();
			mJcbBasicAuthentication.setEnabled(false);
			mJcbConnectingAtStartUp = new JCheckBox();
			mJtfUsername = new JTextField();
			mJtfPassword = new JPasswordField();
			mJtfMaxPollResultSize = new JTextField();
			mJcbDump = new JCheckBox();


			//			 x  y  w  h  wx   wy
			addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, mLblInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, mLblInsets);
			addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlBasicAuthentication, mLblInsets);
			addComponent(0, 3, 1, 1, 1.0, 1.0, this, mJlUsername, mLblInsets);
			addComponent(0, 4, 1, 1, 1.0, 1.0, this, mJlPassword, mLblInsets);
			addComponent(0, 5, 1, 1, 1.0, 1.0, this, mJlMaxPollResultSize, mLblInsets);
			addComponent(0, 6, 1, 1, 1.0, 1.0, this, mJlConnectingAtStartUp, mLblInsets);
			addComponent(0, 7, 1, 1, 1.0, 1.0, this, mJlDump, mLblInsets);
			addComponent(0, 8, 2, 1, 1.0, 1.0, this, mJlDumpDescription, mNullInsets);
			addComponent(0, 9, 2, 1, 1.0, 1.0, this, mJlDumpDescription2, mNullInsets);
			addComponent(0, 10, 2, 1, 1.0, 1.0, this, mJlDumpDescription3, mNullInsets);

			addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, mLblInsets);
			addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, mLblInsets);
			addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbBasicAuthentication, mLblInsets);
			addComponent(1, 3, 1, 1, 1.0, 1.0, this, mJtfUsername, mLblInsets);
			addComponent(1, 4, 1, 1, 1.0, 1.0, this, mJtfPassword, mLblInsets);
			addComponent(1, 5, 1, 1, 1.0, 1.0, this, mJtfMaxPollResultSize, mLblInsets);
			addComponent(1, 6, 1, 1, 1.0, 1.0, this, mJcbConnectingAtStartUp, mLblInsets);
			addComponent(1, 7, 1, 1, 1.0, 1.0, this, mJcbDump, mLblInsets);
		}
	}

	private class DataServiceParameterPanel extends JPanel{

		private static final long serialVersionUID = -4830135051242549298L;

		private JLabel mJlName;
		private JLabel mJlUrl ;
		private JLabel mJlRawXml;

		private JTextField mJtfUrl;
		private JTextField mJtfName;

		public JCheckBox mJcbRawXML;


		private DataServiceParameterPanel(){
			createPanels();
		}

		private void createPanels() {
			setLayout(new GridBagLayout());

			mJlUrl = new JLabel("Url");
			mJlName = new JLabel("Name");
			mJlRawXml = new JLabel("RAW-XML");

			mJtfUrl = new JTextField();
			mJtfName = new JTextField();

			mJcbRawXML = new JCheckBox();

			//			 x  y  w  h  wx   wy
			addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, mLblInsets);
			addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, mLblInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, mLblInsets);
			addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, mLblInsets);
			addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlRawXml, mLblInsets);
			addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbRawXML, mLblInsets);
		}
	}
}
