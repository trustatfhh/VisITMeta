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
package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import org.piccolo2d.nodes.PPath;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d.Piccolo2DGraphicWrapper;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d.Piccolo2DGraphicWrapperFactory;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

public class PolicyMetadataReferenceEdgeRenderer extends StraightLinePiccolo2dEdgeRenderer {

	private static final List<String> VALID_METADATA_TYPENAMES = Arrays.asList(new String[] { "policy-feature",
			"policy-action", "feature", "pattern-matched" });

	@Override
	public void drawEdge(PPath pEdge, Point2D vStart, Point2D vEnd, Metadata metadata, Identifier identifier) {
		super.drawEdge(pEdge, vStart, vEnd, metadata, identifier);

		// only for edges between two metadata
		if (identifier != null) {
			return;
		}

		// only for policy-feature and policy-action metadata
		if ("policy-feature".equals(metadata.getTypeName())) {
			drawPolicyFeatureMetadataReferenceDashedLine(pEdge, vStart, vEnd);
			
		} else if ("policy-action".equals(metadata.getTypeName())) {
			drawPolicyActionMetadataReferenceDashedLine(pEdge, vStart, vEnd);
			
		} else if ("pattern-matched".equals(metadata.getTypeName())) {
			drawPublishVertexToMatchedIdentifierDashedLine(pEdge, vStart, vEnd);
			
		} else {
			return;
		}
	}

	private void drawPublishVertexToMatchedIdentifierDashedLine(PPath pEdge, Point2D vStart, Point2D vEnd) {
		Piccolo2DGraphicWrapper edge = Piccolo2DGraphicWrapperFactory.create(pEdge, null);
		
		if (!checkEdgeNodes(edge)) {
			return;
		}
		
		StraightDashedLinePiccolo2dEdgeRenderer.drawStraightDashedLine(pEdge, vStart, vEnd);
	}

	private void drawPolicyFeatureMetadataReferenceDashedLine(PPath pEdge, Point2D vStart, Point2D vEnd) {
		Piccolo2DGraphicWrapper edge = Piccolo2DGraphicWrapperFactory.create(pEdge, null);

		if (!checkEdgeNodes(edge)) {
			return;
		}

		StraightDashedLinePiccolo2dEdgeRenderer.drawStraightDashedLine(pEdge, vStart, vEnd);
	}

	private void drawPolicyActionMetadataReferenceDashedLine(PPath pEdge, Point2D vStart, Point2D vEnd) {
		Piccolo2DGraphicWrapper edge = Piccolo2DGraphicWrapperFactory.create(pEdge, null);

		if (!checkEdgeNodes(edge)) {
			return;
		}

		StraightDashedLinePiccolo2dEdgeRenderer.drawStraightDashedLine(pEdge, vStart, vEnd);
	}

	private boolean checkEdgeNodes(GraphicWrapper edge) {
		List<GraphicWrapper> nodes = edge.getNodes();

		if (nodes == null || nodes.size() != 2) {
			return false;
		}

		for (GraphicWrapper node : nodes) {
			Object data = node.getData();
			if (data instanceof Propable) {
				Propable propable = (Propable) data;
				String typName = propable.getTypeName();
				// only policy-feature, policy-action, feature or pattern-matched metadata are valid
				if (!VALID_METADATA_TYPENAMES.contains(typName)) {
					return false;
				}
			}
		}

		return true;
	}
}
