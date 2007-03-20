/*
 * TPCC Client
 * Copyright (C) 2006 University of Minho
 * See http://gorda.di.uminho.pt/ for more information.
 *
 * Partially funded by the European Union Framework Programme for
 * Research and Technological Development, thematic priority
 * Information Society and Media, project GORDA (004758).
 * 
 * Contributors:
 *  - Rui Oliveira <rco@di.uminho.pt>
 *  - Jose Orlando Pereira <jop@di.uminho.pt>
 *  - Antonio Luis Sousa <als@di.uminho.pt>
 *  - Alfranio Tavares Correia Junior <alfranio@lsd.di.uminho.pt> 
 *  - Luis Soares <los@di.uminho.pt>
 *  - Ricardo Manuel Pereira Vilaca <rmvilaca@di.uminho.pt>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */


/*
 * Created on 24-May-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package escada.tpc.logger;

import org.apache.log4j.Logger;

/**
 * @author alfranio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PerformanceLogger {
	private static Logger logger = Logger.getLogger(PerformanceLogger.class);

	public static boolean isPerformanceLoggerEnabled() {
		return (logger.isInfoEnabled());
	}

	public static void info(String pStr) {
		logger.info(pStr);
	}
}