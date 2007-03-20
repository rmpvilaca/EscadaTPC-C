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


package escada.tpc.tpcc;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;
import escada.tpc.tpcc.database.transaction.dbTPCCDatabase;

/**
 * It implements the states according to the definition of the TPC-C. Basically,
 * it sets up import information used in the execution of the new order
 * transaction. Additionally, it defines the trace flag, which is a boolean
 * value used to log traces or not and the trace file.
 */
public class NewOrderTrans extends StateObject {
	public void initProcess(Emulation em, String hid) {
		int wid = (em.getEmulationId() / 10) + 1;
		int did = 0;
		int cid = 0;
		int qtd = 0;
		boolean error = false;
		boolean localWarehouse = false;

		outInfo.put("resubmit", Boolean.toString(Emulation
				.getStatusReSubmit()));
		outInfo.put("trace", Emulation.getTraceInformation());
		outInfo.put("abort", Integer.toString(0));
		outInfo.put("hid", hid);

		outInfo.put("wid", Integer.toString(wid));

		did = RandGen.nextInt(em.getRandom(), 1, TPCCConst.rngDistrict + 1);
		outInfo.put("did", Integer.toString(did));

		cid = RandGen.NURand(em.getRandom(), TPCCConst.CustomerA,
				TPCCConst.numINICustomer, TPCCConst.numENDCustomer);
		outInfo.put("cid", Integer.toString(cid));

		qtd = RandGen.nextInt(em.getRandom(), TPCCConst.qtdINIItem,
				TPCCConst.qtdENDItem + 1);

		outInfo.put("qtd", Integer.toString(qtd));

		if (RandGen.nextInt(em.getRandom(), TPCCConst.rngABORTNewOrder + 1) == TPCCConst.probABORTNewOrder) {
			error = true;
		}

		int i = 0;
		int iid = 0;
		int qtdi = 0;
		int supwid = 0;
		while (i < qtd) {
			iid = RandGen.NURand(em.getRandom(), TPCCConst.iidA,
					TPCCConst.numINIItem, TPCCConst.numENDItem);
			qtdi = RandGen.nextInt(em.getRandom(), 1, TPCCConst.qtdItem + 1);
			if ((error) && ((i + 1) >= qtd)) {
				iid = 0;
				outInfo.put("abort", Integer.toString(1));
			}
			outInfo.put("iid" + i, Integer.toString(iid));
			outInfo.put("qtdi" + i, Integer.toString(qtdi));
			if ((RandGen.nextInt(em.getRandom(),
					TPCCConst.rngNewOrderLOCALWarehouse + 1) <= TPCCConst.probNewOrderLOCALWarehouse)
					|| (Emulation.getNumberConcurrentEmulators() <= TPCCConst.numMinClients)) {
				outInfo.put("supwid" + i, Integer.toString(wid));
			} else {
				supwid = RandGen.nextInt(em.getRandom(), 1, (Emulation
						.getNumberConcurrentEmulators() / 10) + 1);
				outInfo.put("supwid" + i, Integer.toString(supwid));
				localWarehouse = false;
			}
			i++;
		}

		if (localWarehouse) {
			outInfo.put("localwid", Integer.toString(1));
		} else {
			outInfo.put("localwid", Integer.toString(0));
		}

		outInfo.put("thinktime", Long.toString(em.getThinkTime()));
		outInfo.put("file", em.getEmulationName());
	}

	public void prepareProcess(Emulation em, String hid) {

	}

	public Object requestProcess(Emulation em, String hid) {
		Object requestProcess = null;
		dbTPCCDatabase db = (dbTPCCDatabase) em.getDatabase();
		try {
			initProcess(em, hid);
			requestProcess = db.TraceNewOrderDB(outInfo, hid);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return (requestProcess);
	}

	public void postProcess(Emulation em, String hid) {
		inInfo.clear();
		outInfo.clear();
	}

	public void setProb() {
		prob = 45;
	}

	public void setKeyingTime() {
		keyingtime = 18;
	}

	public void setThinkTime() {
		thinktime = 12;
	}

	public String toString() {
		return ("NewOrderTrans");
	}
}// arch-tag: 74f56eb7-28a1-454b-8d3e-cb80a85557e3
