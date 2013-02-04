package org.taeradan.ahp.controversial;

import org.junit.*;
import org.junit.rules.MethodRule;
import org.taeradan.ahp.AHPRoot;
import org.taeradan.ahp.TimeConsumeRule;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/** @author Jean-Pierre PRUNARET */
public class ControversialTest {

	private static final double                         TEMOIN_MAX    = 1000;
	private static final double                         VARIABLE_MAX  = 1000000000;
	private static final double                         DECALAGE      = 0;
	private static final String                         ALTERNATIVE_1 = "Alternative 1";
	private static final String                         ALTERNATIVE_2 = "Alternative 2";
	private static final String                         ALTERNATIVE_3 = "Alternative 3";
	private static final String                         ALTERNATIVE_4 = "Alternative 4";
	private static final String                         ALTERNATIVE_5 = "Alternative 5";
	private static final List<ControversialAlternative> ALTERNATIVES  = makeAlternatives();
	private static AHPRoot ahpRoot;

	private static List<ControversialAlternative> makeAlternatives() {
		final List<ControversialAlternative> alts = new ArrayList<>(5);


		boolean add = alts.add(new ControversialAlternative(ALTERNATIVE_1,
															(1 / 5.) * TEMOIN_MAX,
															((1 / 5.) * VARIABLE_MAX) + DECALAGE));

		alts.add(new ControversialAlternative(ALTERNATIVE_2,
											  (2 / 5.) * TEMOIN_MAX,
											  ((2 / 5.) * VARIABLE_MAX) + DECALAGE));

		alts.add(new ControversialAlternative(ALTERNATIVE_3,
											  (3 / 5.) * TEMOIN_MAX,
											  ((3 / 5.) * VARIABLE_MAX) + DECALAGE));

		alts.add(new ControversialAlternative(ALTERNATIVE_4,
											  (4 / 5.) * TEMOIN_MAX,
											  ((4 / 5.) * VARIABLE_MAX) + DECALAGE));

		alts.add(new ControversialAlternative(ALTERNATIVE_5,
											  (5 / 5.) * TEMOIN_MAX,
											  ((5 / 5.) * VARIABLE_MAX) + DECALAGE));
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

	public ControversialTest() {
	}

	@Rule
	public MethodRule rule = new TimeConsumeRule();

	@BeforeClass
	public static void setUpClass()
			throws
			Exception {
	}

	@AfterClass
	public static void tearDownClass()
			throws
			Exception {
	}

	@Before
	public void setUp() {

		final URL resource = ControversialTest.class.getResource("/org/taeradan/ahp/conf/controversial_test.xml");
		System.out.println(resource);
		final File aFile = new File(resource.getFile());

		ahpRoot = new AHPRoot(aFile, "org.taeradan.ahp.controversial.");
	}

	@After
	public void tearDown() {
	}


	@Test
	public void test100_initialState() {
		final ArrayList<ControversialAlternative> alternatives = new ArrayList<>(ALTERNATIVES);

		assertTrue(alternatives.size() == 5);

		assertTrue(alternatives.get(1 - 1).name.equalsIgnoreCase(ALTERNATIVE_1));
		assertTrue(alternatives.get(2 - 1).name.equalsIgnoreCase(ALTERNATIVE_2));
		assertTrue(alternatives.get(3 - 1).name.equalsIgnoreCase(ALTERNATIVE_3));
		assertTrue(alternatives.get(4 - 1).name.equalsIgnoreCase(ALTERNATIVE_4));
		assertTrue(alternatives.get(5 - 1).name.equalsIgnoreCase(ALTERNATIVE_5));

		assertTrue(alternatives.get(1 - 1).getRank() == Integer.MAX_VALUE);
		assertTrue(alternatives.get(2 - 1).getRank() == Integer.MAX_VALUE);
		assertTrue(alternatives.get(3 - 1).getRank() == Integer.MAX_VALUE);
		assertTrue(alternatives.get(4 - 1).getRank() == Integer.MAX_VALUE);
		assertTrue(alternatives.get(5 - 1).getRank() == Integer.MAX_VALUE);
	}

	@Test
	public void test200_ranking() {
		final ArrayList<ControversialAlternative> alternatives = new ArrayList<>(ALTERNATIVES);

		ahpRoot.calculateRanking(alternatives);

//		System.out.println("======================================================");
//		System.out.println(ahpRoot.resultToString());
//		System.out.println("Valeurs de \"rank\" pour chaque alternative:");

		printRanking(ALTERNATIVES);

		assertTrue(alternatives.size() == 5);

		assertTrue(alternatives.get(1 - 1).name.equalsIgnoreCase(ALTERNATIVE_1));
		assertTrue(alternatives.get(2 - 1).name.equalsIgnoreCase(ALTERNATIVE_2));
		assertTrue(alternatives.get(3 - 1).name.equalsIgnoreCase(ALTERNATIVE_3));
		assertTrue(alternatives.get(4 - 1).name.equalsIgnoreCase(ALTERNATIVE_4));
		assertTrue(alternatives.get(5 - 1).name.equalsIgnoreCase(ALTERNATIVE_5));

		assertTrue(alternatives.get(1 - 1).getRank() == 5);
		assertTrue(alternatives.get(2 - 1).getRank() == 4);
		assertTrue(alternatives.get(3 - 1).getRank() == 3);
		assertTrue(alternatives.get(4 - 1).getRank() == 2);
		assertTrue(alternatives.get(5 - 1).getRank() == 1);
	}
}
