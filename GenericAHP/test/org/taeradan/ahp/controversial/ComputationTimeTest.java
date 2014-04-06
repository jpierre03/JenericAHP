package org.taeradan.ahp.controversial;

import org.junit.*;
import org.junit.rules.MethodRule;
import org.taeradan.ahp.AHPRoot;
import org.taeradan.ahp.TimeConsumeRule;

import java.io.File;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * @author Jean-Pierre PRUNARET
 */
public class ComputationTimeTest {

	private static final double TEMOIN_MAX = new Random().nextInt(100 * 1000);
	private static final double VARIABLE_MAX = new Random().nextInt(100 * 1000);
	private static final double DECALAGE = new Random().nextInt(100 * 1000);

	private final List<ControversialAlternative> alternatives = new ArrayList<>(makeAlternatives());
	private static AHPRoot ahpRoot;

	private static List<ControversialAlternative> makeAlternatives() {
		final int maxAlternatives = 2000;
		final List<ControversialAlternative> alts = new ArrayList<>(maxAlternatives);

		for (int i = 0; i < maxAlternatives; i++) {
			final double value = ((double) i + 1) / (maxAlternatives + 1);

			alts.add(new ControversialAlternative(
				"Alternative " + i,
				value * TEMOIN_MAX,
				(value * VARIABLE_MAX) + DECALAGE));
		}

		return Collections.unmodifiableList(alts);
	}

	private static void printRanking(Collection<ControversialAlternative> alternatives) {
		for (ControversialAlternative currentAlt : alternatives) {
			System.out.println(currentAlt.name + " = " + currentAlt.getRank());
		}
	}

	public static void printAHPStructure() {
		System.out.println(ahpRoot.toStringRecursive());
		assertTrue(true);
	}

	public ComputationTimeTest() {
	}

	@Rule
	public MethodRule rule = new TimeConsumeRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {

		final URL resource = ComputationTimeTest.class.getResource("/org/taeradan/ahp/conf/controversial_test.xml");
		System.out.println(resource);
		final File aFile = new File(resource.getFile());

		ahpRoot = new AHPRoot(aFile, "org.taeradan.ahp.controversial.");

		alternatives.clear();
		alternatives.addAll(makeAlternatives());
	}

	@After
	public void tearDown() {
	}


	@Test
	public void ranking() {
		final ArrayList<ControversialAlternative> alternatives = new ArrayList<>(this.alternatives);

		ahpRoot.calculateRanking(alternatives);

//		System.out.println("======================================================");
//		System.out.println(ahpRoot.resultToString());
//		System.out.println("Valeurs de \"rank\" pour chaque alternative:");

		//printRanking(this.alternatives);
	}
}
