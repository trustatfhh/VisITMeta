package de.fhhannover.inform.trust.visitmeta.graphDrawer;

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

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.gui.GuiController;

/* Imports ********************************************************************/
/* Class **********************************************************************/
/**
 * Factory for GraphPanel
 */
public class GraphPanelFactory {
/* Attributes *****************************************************************/
	private static final Logger LOGGER = Logger.getLogger(GraphPanelFactory.class);
/* Constructors ***************************************************************/
/* Methods ********************************************************************/
	/**
	 * Return a Panel that shows the graph.
	 * @param type define witch Panel to return.
	 *         "Piccolo2D" a Panel that use Piccolo2D to draw the graph.
	 *         TODO "OpenGL" a Panel that use OpenGL to draw the graph.
	 * @return a Panel that shows the graph.
	 */
	public static GraphPanel getGraphPanel(String type, GuiController pController) {
		LOGGER.trace("Method getGraphPanel(" + type + ") called.");
		switch(type) {
			case "Piccolo2D": return new Piccolo2DPanel(pController);
//			case "OpenGL"   : return new OpenGLPanel(pController);
			default         : return new Piccolo2DPanel(pController);
		}
	}
}
