package org.taeradan.ahp;

import java.io.File;

public class EmptyAHPRoot
	extends AHPRoot {

	public EmptyAHPRoot() {
		super(
			new File(EmptyAHPRoot.class.getResource("/org/taeradan/ahp/conf/empty_ahp.xml").getFile()),
			AHPRoot.DEFAULT_INDICATOR_PATH
		);
	}
}
