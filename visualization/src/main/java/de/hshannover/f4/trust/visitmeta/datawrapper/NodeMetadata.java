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
 * This file is part of visitmeta-visualization, version 0.2.0,
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
package de.hshannover.f4.trust.visitmeta.datawrapper;





import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Wrapper class that contains a metadata and its current position in the layout.
 */
public class NodeMetadata extends Position {

	private static final Logger LOGGER = Logger.getLogger(NodeMetadata.class);

	private RichMetadata mMetadata;

	public NodeMetadata(RichMetadata metadata) {
		super();
		mMetadata = metadata;
	}

	public Metadata getMetadata() {
		LOGGER.trace("Method getMetadata() called.");
		return mMetadata.getMetadata();
	}

	/**
	 * Deprecated, use setRichMetadata instead.
	 */
	@Deprecated
	public void setMetadata(Metadata metadata) {
		LOGGER.trace("Method setMetadata(" + metadata + ") called.");
		mMetadata = new RichMetadata(metadata);
		setChanged();
		notifyObservers();
	}

	public RichMetadata getRichMetadata() {
		LOGGER.trace("Method getRichMetadata() called.");
		return mMetadata;
	}

	/**
	 * Set metadata as RichMetadata, i.e., metadata with corresponding identifier or link
	 * (depending on metadata type)
	 */
	public void setRichMetadata(RichMetadata metadata) {
		LOGGER.trace("Method setRichMetadata(" + metadata + ") called.");
		mMetadata = metadata;
		setChanged();
		notifyObservers();
	}

}
