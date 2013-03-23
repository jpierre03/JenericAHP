/*
   Sun Public License Notice
 *
 The contents of this file are subject to the Sun Public License
 Version 1.0 (the "License"). You may not use this file except in
 compliance with the License. A copy of the License is available at
http://www.sun.com/
The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2000 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.lib.awtextra;

import java.awt.*;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * AbsoluteLayout is a LayoutManager that works as a replacement for "null" layout to allow placement of components in
 * absolute positions.
 *
 * @version 1.01, Aug 19, 1998
 * @see AbsoluteConstraints
 */
public class AbsoluteLayout
		implements LayoutManager2, Serializable {
	/** generated Serialized Version UID */
	static final long serialVersionUID = -1919857869177070440L;

	/**
	 * Adds the specified component with the specified name to the layout.
	 *
	 * @param name the component name
	 * @param comp the component to be added
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new IllegalArgumentException();
	}

	/**
	 * Removes the specified component from the layout.
	 *
	 * @param comp the component to be removed
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		constraints.remove(comp);
	}

	/**
	 * Calculates the preferred dimension for the specified panel given the components in the specified parent container.
	 *
	 * @param parent the component to be laid out
	 * @see #minimumLayoutSize
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int maxWidth = 0;
		int maxHeight = 0;
		for (Enumeration e = constraints.keys(); e.hasMoreElements(); ) {
			Component comp = (Component) e.nextElement();
			AbsoluteConstraints ac = (AbsoluteConstraints) constraints.get(comp);
			Dimension size = comp.getPreferredSize();

			int width = ac.getWidth();
			if (width == -1) {
				width = size.width;
			}
			int height = ac.getHeight();
			if (height == -1) {
				height = size.height;
			}

			if (ac.x + width > maxWidth) {
				maxWidth = ac.x + width;
			}
			if (ac.y + height > maxHeight) {
				maxHeight = ac.y + height;
			}
		}
		return new Dimension(maxWidth, maxHeight);
	}

	/**
	 * Calculates the minimum dimension for the specified panel given the components in the specified parent container.
	 *
	 * @param parent the component to be laid out
	 * @see #preferredLayoutSize
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int maxWidth = 0;
		int maxHeight = 0;
		for (Enumeration e = constraints.keys(); e.hasMoreElements(); ) {
			Component comp = (Component) e.nextElement();
			AbsoluteConstraints ac = (AbsoluteConstraints) constraints.get(comp);

			Dimension size = comp.getMinimumSize();

			int width = ac.getWidth();
			if (width == -1) {
				width = size.width;
			}
			int height = ac.getHeight();
			if (height == -1) {
				height = size.height;
			}

			if (ac.x + width > maxWidth) {
				maxWidth = ac.x + width;
			}
			if (ac.y + height > maxHeight) {
				maxHeight = ac.y + height;
			}
		}
		return new Dimension(maxWidth, maxHeight);
	}

	/**
	 * Lays out the container in the specified panel.
	 *
	 * @param parent the component which needs to be laid out
	 */
	@Override
	public void layoutContainer(Container parent) {
		for (Enumeration e = constraints.keys(); e.hasMoreElements(); ) {
			Component comp = (Component) e.nextElement();
			AbsoluteConstraints ac = (AbsoluteConstraints) constraints.get(comp);
			Dimension size = comp.getPreferredSize();
			int width = ac.getWidth();
			if (width == -1) {
				width = size.width;
			}
			int height = ac.getHeight();
			if (height == -1) {
				height = size.height;
			}

			comp.setBounds(ac.x, ac.y, width, height);
		}
	}

	/**
	 * Adds the specified component to the layout, using the specified constraint object.
	 *
	 * @param comp   the component to be added
	 * @param constr where/how the component is added to the layout.
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constr) {
		if (!(constr instanceof AbsoluteConstraints)) {
			throw new IllegalArgumentException();
		}
		constraints.put(comp, constr);
	}

	/**
	 * Returns the maximum size of this component.
	 *
	 * @see Component#getMinimumSize()
	 * @see Component#getPreferredSize()
	 * @see LayoutManager
	 */
	@Override
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Returns the alignment along the x axis.  This specifies how the component would like to be aligned relative to other
	 * components.  The value should be a number between 0 and 1 where 0 represents alignment along the origin, 1 is
	 * aligned the furthest away from the origin, 0.5 is centered, etc.
	 */
	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	/**
	 * Returns the alignment along the y axis.  This specifies how the component would like to be aligned relative to other
	 * components.  The value should be a number between 0 and 1 where 0 represents alignment along the origin, 1 is
	 * aligned the furthest away from the origin, 0.5 is centered, etc.
	 */
	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	/** Invalidates the layout, indicating that if the layout manager has cached information it should be discarded. */
	@Override
	public void invalidateLayout(Container target) {
	}


	/** A mapping <Component, AbsoluteConstraints> */
	private final Hashtable constraints = new Hashtable();
}
