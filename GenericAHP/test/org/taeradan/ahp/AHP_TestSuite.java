package org.taeradan.ahp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.taeradan.ahp.consistency.ConsistencyTest;
import org.taeradan.ahp.controversial.ControversialTest;
import org.taeradan.ahp.matrix.MatrixValueTest;

/** @author Jean-Pierre PRUNARET */
@RunWith(Suite.class)
@Suite.SuiteClasses({ConsistencyTest.class,
					 ControversialTest.class,
					 MatrixValueTest.class})
public class AHP_TestSuite {
}
