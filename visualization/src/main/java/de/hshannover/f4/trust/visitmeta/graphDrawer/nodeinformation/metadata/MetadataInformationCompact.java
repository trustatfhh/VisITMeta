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
package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.metadata;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.util.DocumentUtils;

/**
 * Implementation of {@link MetadataInformationStrategy} that creates textual
 * information for {@link Metadata} containing the typename and additional
 * information in further lines, but tries to generate a compact apperance.
 *
 * Example:<br>
 * request-for-investigation
 *
 * @author Bastian Hellmann
 *
 */
public class MetadataInformationCompact implements MetadataInformationStrategy {

	private static final List<String> TYPENAMES_FROM_METADATA_FOR_NETWORK_SECURITY = Arrays.asList("capability",
			"device-attribute", "device-characteristic", "enforcement-report", "event", "ip-mac", "layer2-information",
			"location", "request-for-investigation", "role", "unexpected-behavior", "wlan-information");

	private static final String NAMESPACE_URI_METADATA_FOR_NETWORK_SECURITY = "meta";

	@Override
	public String getText(Metadata metadata) {
		String typename = metadata.getTypeName();
		StringBuilder sb = new StringBuilder();

		sb.append(typename);

		if (TYPENAMES_FROM_METADATA_FOR_NETWORK_SECURITY.contains(typename)) {
			handleIFMAPMetadataForNetworkSecurity(typename, metadata, sb);
		} else {
			handleUnknownMetadata(typename, metadata, sb);
		}

		return sb.toString();
	}

	private void handleIFMAPMetadataForNetworkSecurity(String typename, Metadata metadata, StringBuilder sb) {
		String xpathPrefixElement = "/" + NAMESPACE_URI_METADATA_FOR_NETWORK_SECURITY + ":" + typename + "/";
		String xpathPrefixAttribute = "/" + NAMESPACE_URI_METADATA_FOR_NETWORK_SECURITY + ":" + typename + "[@";
		String xpathPostfixAttrubute = "]";

		switch (typename) {
		case "capability":
			// 1-1 name
			// 0-1 administrative-domain
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "name", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "administrative-domain", " (", ")");
			break;
		case "device-attribute":
			// 1-1 name
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "name", "\n", "");
			break;
		case "device-characteristic":
			// 0-1 manufacturer
			// 0-1 model
			// 0-1 os
			// 0-1 os-version
			// 0 device-type
			// 1-1 discovered-time
			// 1-1 discoverer-id
			// 1 discoverer-method
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "manufacturer", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "model", "/", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "os", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "os-version", ", ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "device-type", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "discovered-time", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "discoverer-id", ", ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "discoverer-method", ", ", "");
			break;
		case "enforcement-report":
			// 1-1 enforcement-action
			// 0-1 other-type-definition
			// 0-1 enforcement-reason
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "enforcement-action", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "other-type-definition", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "enforcement-reason", "\n", "");
			break;
		case "event":
			// 1-1 discovered-time
			// 1-1 discoverer-id
			// 1-1 magnitude
			// 1-1 confidence
			// 1-1 significance
			// 0-1 type
			// 0-1 other-type-definition
			// 0-1 information
			// 0-1 vulnerability-uri
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "discovered-time", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "discoverer-id", ", ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "magnitude", "\nmagnitude: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "confidence", "\nconfidence: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "significance", "\nsignificance: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "type", "\ntpye: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "other-type-definition", ", ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "vulnerability-uri", ", ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "information", "\n", "");
			break;
		// case "ip-mac":
		// /meta:ip-mac/end-time
		// 0-1 start-time
		// 0-1 end-time
		// 0-1 dhcp-server
		// break;
		case "layer2-information":
			// 0-1 vlan
			// 0-1 vlan-name
			// 0-1 port
			// 0-1 administrative-domain
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "vlan", "\nvlan #", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "vlan-name", ", ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "port", ", port: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "administrative-domain", "\n", "");
			break;
		case "location":
			// 1-x location-information
			// attr type
			// attr value
			// 1-1 discovered-time
			// 1-1 discoverer-id
			appendIfNotEmpty(sb, metadata, xpathPrefixAttribute + "type" + xpathPostfixAttrubute, "", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixAttribute + "value" + xpathPostfixAttrubute, "", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "discovered-time", "", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "discoverer-id", "", "");
			break;
		case "request-for-investigation":
			// 0-1 attr multi qualifier
			appendIfNotEmpty(sb, metadata, xpathPrefixAttribute + "qualifier" + xpathPostfixAttrubute, "\nqualifier: ",
					"");
			break;
		case "role":
			// 0-1 admnistrative-domain
			// 1-1 name
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "name", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "administrative-domain", "\n", "");
			break;
		case "unexpected-behavior":
			// 1-1 discovered-time
			// 1-1 discoverer-id
			// 0-1 information
			// 1-1 magnitude
			// 0-1 confidence
			// 1-1 significance
			// 0-1 type
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "discovered-time", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "discoverer-id", ", ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "information", "\n", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "magnitude", "\nmagnitude: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "confidence", "\nconfidence: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "significance", "\nsignificance: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "type", "\n", "");
			break;
		case "wlan-information":
			// 0-1 ssid
			// 1-x ssid-unicast-security
			// 1-1 ssid-group-security
			// 1-x ssid-management-security
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "ssid", "\nssid: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "ssid-unicast-security", "\nsecurity-unicast: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "ssid-group-security", ", security-group: ", "");
			appendIfNotEmpty(sb, metadata, xpathPrefixElement + "ssid-management-security", ", security-management: ",
					"");
			break;
		default:
			break;
		}
	}

	private void appendIfNotEmpty(StringBuilder sb, Metadata metadata, String xpath, String prefix, String postfix) {
		if (metadata.hasProperty(xpath)) {
			sb.append(prefix + metadata.valueFor(xpath) + postfix);
		}
	}

	private void handleUnknownMetadata(String typename, Metadata metadata, StringBuilder sb) {
		// TODO: elements and/or attribute???
		Document document = DocumentUtils.parseXmlString(metadata.getRawData());
		Map<String, String> map = DocumentUtils.extractInformation(document, DocumentUtils.NAME_TYPE_VALUE);
		String name = map.get("name");
		String type = map.get("value");
		String value = map.get("type");

		boolean nameExists = map.containsKey("name");
		boolean typeExists = map.containsKey("type");
		boolean valueExists = map.containsKey("value");

		if (nameExists) {
			sb.append("\n");
			sb.append(name);

			if (typeExists) {
				sb.append(" (");
				sb.append(type);

				if (valueExists) {
					sb.append(", ");
					sb.append(value);
				}
				sb.append(")");
			} else if (valueExists) {
				sb.append(" (");
				sb.append(value);
				sb.append(")");
			}
		} else if (typeExists) {
			sb.append("\n");
			sb.append(type);

			if (valueExists) {
				sb.append(", ");
				sb.append(value);
			}
			sb.append(")");
		} else if (valueExists) {
			sb.append("\n");
			sb.append(value);
		}
	}

}
