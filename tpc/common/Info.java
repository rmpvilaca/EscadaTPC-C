package Escada.tpc.common;

import java.util.*;

/**
* This class implements a map (repository) using a string as index to
* an object. Internally, it uses a Hashtable in order to build this map.
* Basically, it aims at providing an object to transfer multiple information
* among entities involved in the emulation process.
**/
public class Info {
  private Hashtable hs;

  /**
  * It instantiates the the class building an empty repository.
  **/
  public Info() {
    hs = new Hashtable();
  }

  /**
  * It defines a map between an id (String) and an object.
  * 
  * @param String index used to retrieve the object
  * @param Object object stored
  * @see getInfo
  */
  public void putInfo(String id, Object info) {
    hs.put(id, info);
  }


  /**
  * It returns an object using the id (String) as an index to
  * retrieve it.
  *
  * @param String index used to retrieve the object
  * @return object stored
  * @see getInfo
  **/
  public Object getInfo(String id) {
    return (hs.get(id));
  }

  /**
  *  It removes the map between the id (String) and the object. 
  *
  *  @param String index used to retrieve the object
  **/
  public void resetInfo(String id) {
    hs.remove(id);
  }

  /**
  * It removes all the maps from the repository.
  **/
  public void resetInfo() {
    hs.clear();
  }

  /**
  * It prints the a list of pairs (index,value) for all the maps
  * stored in the repository.
  **/
  public String toString() {
    Enumeration e = hs.keys();
    StringBuffer str = new StringBuffer();

    System.out.println();
    while (e.hasMoreElements()) {
      Object o = e.nextElement();
      str.append("key=" + o + "  value=" + hs.get(o) + "    ");
    }
    return (str.toString());
  }
}// arch-tag: dfc5609b-83d1-465d-95de-de3cf9c99597
// arch-tag: 086049d0-e20a-47f2-937a-25ca589c7a83
// arch-tag: ec37801d-57f4-45e9-84ce-4b8623b2db11
