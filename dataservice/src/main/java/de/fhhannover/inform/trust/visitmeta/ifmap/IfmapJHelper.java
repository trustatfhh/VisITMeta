package de.fhhannover.inform.trust.visitmeta.ifmap;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhhannover.inform.trust.ifmapj.identifier.Identifiers;
import de.fhhannover.inform.trust.ifmapj.messages.ResultItem;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InternalIdentifierFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;

class IfmapJHelper {

	private final DocumentBuilder mDocumentBuilder;
	private InternalIdentifierFactory mIdentifierFactory;

	public IfmapJHelper(InternalIdentifierFactory idFactory) {
		mIdentifierFactory = idFactory;
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		f.setNamespaceAware(true);
		try {
			mDocumentBuilder = f.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("could not create document builder");
		}
	}

	public InternalIdentifier ifmapjIdentifierToInternalIdentifier(
			de.fhhannover.inform.trust.ifmapj.identifier.Identifier id) {
		Document document = mDocumentBuilder.newDocument();
		Element element = Identifiers.tryToElement(id, document);
		document.appendChild(element);
		return mIdentifierFactory.createIdentifier(document);
	}

	public InternalIdentifier extractSingleIdentifier(ResultItem item) {
		if (item.getIdentifier1() != null) {
			return ifmapjIdentifierToInternalIdentifier(item.getIdentifier1());
		} else if (item.getIdentifier2() != null) {
			return ifmapjIdentifierToInternalIdentifier(item.getIdentifier2());
		} else {
			throw new RuntimeException("ResultItem '"+item+"' contains no identifier");
		}
	}

}
