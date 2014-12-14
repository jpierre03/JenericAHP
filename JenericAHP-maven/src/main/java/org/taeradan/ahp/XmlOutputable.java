package org.taeradan.ahp;

import org.jdom.Element;

/**
 * @author Jean-Pierre PRUNARET
 */
public interface XmlOutputable {

	/**
	 * @return jdom element representing the object
	 * @see org.jdom.Element
	 */
	Element toXml();
}
