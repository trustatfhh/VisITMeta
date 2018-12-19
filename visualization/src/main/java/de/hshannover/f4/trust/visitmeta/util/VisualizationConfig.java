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
package de.hshannover.f4.trust.visitmeta.util;

import de.hshannover.f4.trust.visitmeta.graphCalculator.LayoutType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer.Piccolo2dEdgeRendererType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.noderenderer.Piccolo2dNodeRendererType;
import de.hshannover.f4.trust.visitmeta.gui.historynavigation.HistoryNavigationStrategyType;
import de.hshannover.f4.trust.visitmeta.gui.nodeinformation.NodeInformationStrategyType;
import de.hshannover.f4.trust.visitmeta.gui.search.SearchAndFilterStrategyType;

/**
 * Class that holds keys and default values for the visualization configuration.
 * Used to load from the configuration file <i>visualization_config.yaml</i>
 *
 * @author Bastian Hellmann
 *
 */
public class VisualizationConfig {
	
	public static final String KEY_CONNECTION_DATASERVICESSL_VERIFY_HOSTNAME = "visualization.connection.dataservicessl.verifyhostname";
	public static final boolean DEFAULT_VALUE_CONNECTION_DATASERVICESSL_VERIFY_HOSTNAME = false; 

	public static final String KEY_SHOW_EXTENDED_IDENTIFIER_PREFIX =
			"visualization.identifier.text.showextendedidentifierprefix";
	public static final boolean DEFAULT_VALUE_SHOW_EXTENDED_IDENTIFIER_PREFIX = true;

	public static final String KEY_GUICONTROLLER_Z_FACTOR = "guicontroller.zfactor";
	public static final double DEFAULT_VALUE_GUICONTROLLER_Z_FACTOR = 0.0;

	public static final String KEY_GUICONTROLLER_XY_FACTOR = "guicontroller.xyfactor";
	public static final double DEFAULT_VALUE_GUICONTROLLER_XY_FACTOR = 0.0;

	public static final String KEY_NODE_INFORMATION_STYLE = "visualization.node.information.style";
	public static final String DEFAULT_VALUE_NODE_INFORMATION_STYLE = NodeInformationStrategyType.XML_BREAKDOWN.name();

	public static final String KEY_WINDOW_POSITION_X = "window.position.x";
	public static final int DEFAULT_VALUE_WINDOW_POSITION_X = 0;

	public static final String KEY_WINDOW_POSITION_Y = "window.position.y";
	public static final int DEFAULT_VALUE_WINDOW_POSITION_Y = 0;

	public static final String KEY_WINDOW_WIDTH = "window.width";
	public static final int DEFAULT_VALUE_WINDOW_WIDTH = 1280;

	public static final String KEY_WINDOW_HEIGHT = "window.height";
	public static final int DEFAULT_VALUE_WINDOW_HEIGHT = 720;

	public static final String KEY_WINDOW_DIVIDER = "window.divider";
	public static final int DEFAULT_VALUE_WINDOW_DIVIDER = -1;

	public static final String KEY_HISTORY_NAVIGATION_STYLE = "visualization.history.navigation.style";
	public static final String DEFAULT_VALUE_HISTORY_NAVIGATION_STYLE =
			HistoryNavigationStrategyType.TAB_BASED_NAVIGATION.name();

	public static final String KEY_SEARCH_AND_FILTER_STYLE = "visualization.searchandfilter.style";
	public static final String DEFAULT_VALUE_SEARCH_AND_FILTER_STYLE =
			SearchAndFilterStrategyType.SIMPLE_SEARCH_AND_NO_FILTER
					.name();

	public static final String KEY_CALCULATION_ITERATIONS = "visualization.calculation.iterations";
	public static final int DEFAULT_VALUE_CALCULATION_ITERATIONS = 100;

	public static final String KEY_CALCULATION_DEFAULT_LAYOUTTYPE = "visualization.calculation.layouttype";
	public static final String DEFAULT_VALUE_CALCULATION_DEFAULT_LAYOUTTYPE = LayoutType.FORCE_DIRECTED.name();
	
	public static final String KEY_NETWORK_INTERVAL = "visualization.network.interval";
	public static final int DEFAULT_VALUE_NETWORK_INTERVAL = 10000;

	public static final String KEY_CALCULATION_INTERVAL = "visualization.calculation.interval";
	public static final int DEFAULT_VALUE_CALCULATION_INTERVAL = 3000;

	public static final String KEY_HIGHTLIGHT_TIMEOUT = "visualization.highlights.timeout";
	public static final int DEFAULT_VALUE_HIGHTLIGHT_TIMEOUT = 5000;

	public static final String KEY_NODE_TRANSLATION_DURATION = "visualization.node.translation.duration";
	public static final int DEFAULT_VALUE_NODE_TRANSLATION_DURATION = 1000;

	public static final String KEY_COLOR_BACKGROUND = "color.background";
	public static final String DEFAULT_VALUE_COLOR_BACKGROUND = "0xFFFFFF";

	public static final String KEY_COLOR_EDGE = "color.edge";
	public static final String DEFAULT_VALUE_COLOR_EDGE = "0x000000";

	public static final String KEY_COLOR_NODE_NEW = "color.node.new";
	public static final String DEFAULT_VALUE_COLOR_NODE_NEW = "0x266E62";

	public static final String KEY_COLOR_NODE_DELETE = "color.node.delete";
	public static final String DEFAULT_VALUE_COLOR_NODE_DELETE = "0xA73946";

	public static final String KEY_COLOR_NODE_SELECTED = "color.node.selected";
	public static final String DEFAULT_VALUE_COLOR_NODE_SELECTED = "0xCEB100";

	public static final String KEY_COLOR_NODE_SEARCH = "color.node.search";
	public static final String DEFAULT_VALUE_COLOR_NODE_SEARCH = "0x88A538";

	public static final String KEY_COLOR_NODE_MOUSE_OVER = "color.node.mouseover";
	public static final String DEFAULT_VALUE_COLOR_NODE_MOUSE_OVER = "0x708090";
	
	public static final String KEY_COLOR_NODE_BLANK = "color.node.blank";
	public static final String DEFAULT_VALUE_COLOR_NODE_BLANK = "0xFFFFFF";

	public static final String KEY_COLOR_NODE_HIGHLIGHT = "color.node.highlight.value";
	public static final String DEFAULT_VALUE_COLOR_NODE_HIGHLIGHT = "0xFF0000";
	public static final String KEY_COLOR_NODE_HIGHLIGHT_TYPENAMES = "color.node.highlight.typenames";
	public static final String DEFAULT_VALUE_COLOR_NODE_HIGHLIGHT_TYPENAMES = "event,unexpected-behavior";
	
	public static final String KEY_SEARCH_AND_FILTER_TRANSPARENCY = "visualization.searchandfilter.transparency";
	public static final double DEFAULT_VALUE_SEARCH_AND_FILTER_TRANSPARENCY = 0.2;

	public static final String KEY_IDENTIFIER_TEXT_STYLE = "visualization.identifier.text.style";
	public static final String DEFAULT_VALUE_IDENTIFIER_TEXT_STYLE = "SINGLE_LINE";

	public static final String KEY_METADATA_TEXT_STYLE = "visualization.metadata.text.style";
	public static final String DEFAULT_VALUE_METADATA_TEXT_STYLE = "SINGLE_LINE";

	public static final String KEY_COLOR_PREFIX = "color.";
	public static final String KEY_COLOR_IDENTIFIER_PREFIX = "color.identifier.";
	public static final String KEY_COLOR_METADATA_PREFIX = "color.metadata.";
	public static final String KEY_COLOR_INSIDE_POSTFIX = ".inside";
	public static final String KEY_COLOR_OUTSIDE_POSTFIX = ".outside";
	public static final String KEY_COLOR_BORDER_POSTFIX = ".border";
	public static final String KEY_COLOR_TEXT_POSTFIX = ".text";

	public static final String DEFAULT_VALUE_COLOR_METADATA = "0xFFFFFF";
	public static final String DEFAULT_VALUE_COLOR_IDENTIFIER_INSIDE = "0x9999FF";
	public static final String DEFAULT_VALUE_COLOR_IDENTIFIER_OUTSIDE = "0x9999FF";
	public static final String DEFAULT_VALUE_COLOR_IDENTIFIER_BORDER = "0x000000";
	public static final String DEFAULT_VALUE_COLOR_IDENTIFIER_TEXT = "0x000000";

	public static final String KEY_COLOR_METADATA_INSIDE_DEFAULT = "color.metadata.inside";
	public static final String DEFAULT_VALUE_COLOR_METADATA_INSIDE_DEFAULT = "0xFF9966";

	public static final String KEY_COLOR_METADATA_OUTSIDE_DEFAULT = "color.metadata.outside";
	public static final String DEFAULT_VALUE_COLOR_METADATA_OUTSIDE_DEFAULT = "0xFF9966";

	public static final String KEY_COLOR_METADATA_BORDER_DEFAULT = "color.metadata.border";
	public static final String DEFAULT_VALUE_COLOR_METADATA_BORDER_DEFAULT = "0x000000";

	public static final String KEY_COLOR_METADATA_TEXT_DEFAULT = "color.metadata.text";
	public static final String DEFAULT_VALUE_COLOR_METADATA_TEXT_DEFAULT = "0x000000";

	public static final String KEY_COLOR_IDENTIFIER_EXTENDED_INSIDE = "color.identifier.extended.inside";
	public static final String DEFAULT_VALUE_COLOR_IDENTIFIER_EXTENDED_INSIDE = "0x9999FF";

	public static final String KEY_COLOR_IDENTIFIER_EXTENDED_OUTSIDE = "color.identifier.extended.outside";
	public static final String DEFAULT_VALUE_COLOR_IDENTIFIER_EXTENDED_OUTSIDE = "0x9999FF";

	public static final String KEY_COLOR_IDENTIFIER_EXTENDED_BORDER = "color.identifier.extended.border";
	public static final String DEFAULT_VALUE_COLOR_IDENTIFIER_EXTENDED_BORDER = "0x000000";

	public static final String KEY_COLOR_IDENTIFIER_EXTENDED_TEXT = "color.identifier.extended.text";
	public static final String DEFAULT_VALUE_COLOR_IDENTIFIER_EXTENDED_TEXT = "0x000000";

	public static final String KEY_PICCOLO2D_METADATA_NODE_STYLE = "visualization.metadata.node.style";
	public static final String DEFAULT_VALUE_PICCOLO2D_METADATA_NODE_STYLE =
			Piccolo2dNodeRendererType.RECTANGLES_WITH_RECTANGULAR_CORNERS.name();

	public static final String KEY_PICCOLO2D_IDENTIFIER_NODE_STYLE = "visualization.identifier.node.style";
	public static final String DEFAULT_VALUE_PICCOLO2D_IDENTIFIER_NODE_STYLE =
			Piccolo2dNodeRendererType.RECTANGLES_WITH_ROUNDED_CORNERS.name();

	public static final String KEY_PICCOLO2D_EDGE_STYLE = "visualization.edge.style";
	public static final String DEFAULT_VALUE_PICCOLO2D_EDGE_STYLE =
			Piccolo2dEdgeRendererType.STRAIGHT_LINE.name();

	public static final String KEY_CONNECTION_IRONDETECT_URL = "visualization.connection.irondetect.url";
	public static final String DEFAULT_VALUE_CONNECTION_IRONDETECT_URL = "http://localhost:8001";
	
	public static final String KEY_CONNECTION_DATASERVICE_URL = "visualization.connection.dataservice.url";
	public static final String DEFAULT_VALUE_CONNECTION_DATASERVICE_URL = "http://localhost:8000";
	public static final String KEY_CONNECTION_DATASERVICE_CONNECTION_NAME = "visualization.connection.dataservice.connectionname";
	public static final String DEFAULT_VALUE_CONNECTION_DATASERVICE_CONNECTION_NAME = "localMAPServer";
	
	public static final String KEY_CONNECTION_IFMAP_URL = "visualization.connection.ifmap.url";
	public static final String DEFAULT_VALUE_CONNECTION_IFMAP_URL = "http://localhost:8443";

	public static final String KEY_CONNECTION_IFMAP_USERNAME = "visualization.connection.ifmap.username";
	public static final String DEFAULT_VALUE_CONNECTION_IFMAP_USERNAME = "visitmeta-publisher";

	public static final String KEY_CONNECTION_IFMAP_PASSWORD = "visualization.connection.ifmap.password";
	public static final String DEFAULT_VALUE_CONNECTION_IFMAP_PASSWORD = "visitmeta-publisher";

	public static final String KEY_CONNECTION_IFMAP_TRUSTSTORE_PATH = "visualization.connection.ifmap.truststore.path";
	public static final String DEFAULT_VALUE_CONNECTION_IFMAP_TRUSTSTORE_PATH = "config/visitmeta.jks";

	public static final String KEY_CONNECTION_IFMAP_TRUSTSTORE_PASSWORD = "visualization.connection.ifmap.truststore.password";
	public static final String DEFAULT_VALUE_CONNECTION_IFMAP_TRUSTSTORE_PASSWORD = "visitmeta";

	public static final String KEY_CONNECTION_IFMAP_THREADSAFE = "visualization.connection.ifmap.threadsafe";
	public static final boolean DEFAULT_VALUE_CONNECTION_IFMAP_THREADSAFE = true;

	public static final String KEY_CONNECTION_IFMAP_INITIALCONNECTIONTIMEOUT = "visualization.connection.ifmap.initialconnectiontimeout";
	public static final int DEFAULT_VALUE_CONNECTION_IFMAP_INITIALCONNECTIONTIMEOUT = (120 * 1000);
	
	public static final String KEY_GUI_NAVIGATION_PANEL = "visualization.gui.navigationpanel";
	public static final boolean DEFAULT_VALUE_GUI_NAVIGATION_PANEL = false;
	
	public static final String KEY_CALCULATION_JUNG_FORCEDIRECTED_ATTRACTION_MULTIPLIER = "visualization.calculation.jung.forcedirected.attractionmultiplier";
	public static final double DEFAULT_VALUE_CALCULATION_JUNG_FORCEDIRECTED_ATTRACTION_MULTIPLIER = 0.25;
	
	public static final String KEY_CALCULATION_JUNG_FORCEDIRECTED_REPULSION_MULTIPLIER = "visualization.calculation.jung.forcedirected.repulsionmultiplier";
	public static final double DEFAULT_VALUE_CALCULATION_JUNG_FORCEDIRECTED_REPULSION_MULTIPLIER = 0.75;
	
	public static final String KEY_CALCULATION_JUNG_FORCEDIRECTED_MAX_ITERATIONS = "visualization.calculation.jung.forcedirected.maxiterations";
	public static final int DEFAULT_VALUE_CALCULATION_JUNG_FORCEDIRECTED_MAX_ITERATIONS = 500;

	public static final String KEY_CALCULATION_JUNG_KAMADAKAWAI_ADJUSTFORGRAVITY = "visualization.calculation.jung.kamadakawai.adjustforgravity";
	public static final boolean DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_ADJUSTFORGRAVITY = true;
	
	public static final String KEY_CALCULATION_JUNG_KAMADAKAWAI_DISCONNECTEDDISTANCEMULTIPLIER = "visualization.calculation.jung.kamadakawai.disconnecteddistancemultiplier";
	public static final double DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_DISCONNECTEDDISTANCEMULTIPLIER = 1.0;
	
	public static final String KEY_CALCULATION_JUNG_KAMADAKAWAI_EXCHANGEVERTICES = "visualization.calculation.jung.kamadakawai.exchangevertices";
	public static final boolean DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_EXCHANGEVERTICES = true;
	
	public static final String KEY_CALCULATION_JUNG_KAMADAKAWAI_LENGTHFACTOR = "visualization.calculation.jung.kamadakawai.lengthfactor";
	public static final double DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_LENGTHFACTOR = 1.0;
	
	public static final String KEY_CALCULATION_JUNG_KAMADAKAWAI_MAX_ITERATIONS = "visualization.calculation.jung.kamadakawai.maxiterations";
	public static final int DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_MAX_ITERATIONS = 100;
	
	public static final String KEY_CALCULATION_JUNG_SPRING_STRETCH = "visualization.calculation.jung.spring.stretch";
	public static final double DEFAULT_VALUE_CALCULATION_JUNG__SPRING_STRETCH = 0.7;
	
	public static final String KEY_CALCULATION_JUNG_SPRING_REPULSIONRANGE = "visualization.calculation.jung.spring.repulsionrange";
	public static final int DEFAULT_VALUE_CALCULATION_JUNG_SPRING_REPULSIONRANGE = 100;
	
	public static final String KEY_CALCULATION_JUNG_SPRING_FORCEMULTIPLIER = "visualization.calculation.jung.spring.forcemultiplier";
	public static final double DEFAULT_VALUE_CALCULATION_JUNG_SPRING_FORCEMULTIPLIER = 1.0 / 3.0;
}
