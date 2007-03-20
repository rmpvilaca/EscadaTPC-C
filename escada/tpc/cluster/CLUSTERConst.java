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


package escada.tpc.cluster;

/**
 * It defines important constants used in the simulation of the TPC-C.
 */
public class CLUSTERConst {
	public final static int CustomerA = 1023;

	public final static int numINICustomer = 1;

	public final static int numENDCustomer = 3000;

	public final static int qtdINIItem = 5;

	public final static int qtdENDItem = 15;

	public final static int qtdItem = 10;

	public final static int iidA = 8191;

	public final static int numINIItem = 1;

	public final static int numENDItem = 100000;

	public final static int rngCarrier = 10;

	public final static int rngDistrict = 10;

	public final static int probABORTNewOrder = 1;

	public final static int rngABORTNewOrder = 100;

	public final static int probNewOrderLOCALWarehouse = 99;

	public final static int rngNewOrderLOCALWarehouse = 100;

	public final static int probPaymentLOCALWarehouse = 85;

	public final static int rngPaymentLOCALWarehouse = 100;

	public final static int probLASTNAME = 60;

	public final static int rngLASTNAME = 100;

	public final static int LastNameA = 255;

	public final static int numINILastName = 0;

	public final static int numENDLastName = 999;

	public final static int numINIAmount = 100;

	public final static int numENDAmount = 500000;

	public final static int numDIVAmount = 100;

	public final static int numMinClients = 10;

	public final static int numINIThreshHold = 10;

	public final static int numENDThreshHold = 20;

	public final static int numState = 5;
}
// arch-tag: af7f4762-49ef-4e6f-9a08-20e1d8878ed9
