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
 * This file is part of visitmeta dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
package de.hshannover.f4.trust.metalyzer.api.finder.util;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;

import org.junit.Test;

import de.hshannover.f4.trust.metalyzer.api.finder.util.NoIdentifierFilter;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

public class NoIdentifierFilterTest {
	
	private NoIdentifierFilter filter;
	private ArrayList<Identifier> expected = new ArrayList<>();
	private ArrayList<Identifier> actual = new ArrayList<>();
	
	@Test
	public void testIfNoIdentifierAreFiltered() {
		givenNewNoIdentifierFilter();
		
		whenFilterIdentifierIsCalled();
		
		thenFilterShouldNotFilterAnyIdentifier();
	}

	private void givenNewNoIdentifierFilter() {
		filter = new NoIdentifierFilter();
		
	}

	private void whenFilterIdentifierIsCalled() {
		expected.add(new IdentifierImpl("device"));
		expected.add(new IdentifierImpl("identity"));
		expected.add(new IdentifierImpl("device"));
		expected.add(new IdentifierImpl("ip-address"));
		
		filter.filter(expected);
		
		actual = (ArrayList<Identifier>) filter.getList();
	}

	private void thenFilterShouldNotFilterAnyIdentifier() {
		assertThat(expected, is(actual));
		
	}
}