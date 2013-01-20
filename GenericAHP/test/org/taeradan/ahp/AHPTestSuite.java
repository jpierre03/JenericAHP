package org.taeradan.ahp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.taeradan.ahp.consistency.ConsistencyTest;
import org.taeradan.ahp.controversial.ControversialTest;

/** @author Jean-Pierre PRUNARET */
@RunWith(Suite.class)
@Suite.SuiteClasses({ControversialTest.class, ConsistencyTest.class})
public class AHPTestSuite {
}
