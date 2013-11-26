package de.fhhannover.inform.trust.visitmeta.dataservice.rest;

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

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.dataservice.Application;
import de.fhhannover.inform.trust.visitmeta.ifmap.UpdateService;


@Path("/dump")
public class DumpingResource {

	private static final Logger log = Logger.getLogger(DumpingResource.class);

	private UpdateService mUpdateService;

	public DumpingResource() {
		mUpdateService = initUpdateService();
	}

	/**
	 * Example-URL: <tt>http://example.com:8000/dump/start</tt>
	 */
	@PUT
	@Path("start")
	public String startDump() {
		mUpdateService.subscribeDeleteAll();
		mUpdateService.startDumpingService();
		return "INFO: dumping starts successfully";
	}
	
	/**
	 * Example-URL: <tt>http://example.com:8000/dump/stop</tt>
	 */
	@PUT
	@Path("stop")
	public String stopDump() {
		mUpdateService.stropDumpingService();
		return "INFO: dumping stop successfully";
	}

	private UpdateService initUpdateService() {
		return Application.getUpdateService();
	}
}