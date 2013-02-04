package org.taeradan.ahp;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * @author Jean-Pierre PRUNARET
 * @link http://stackoverflow.com/a/5547025
 */
public class TimeConsumeRule
		implements MethodRule {
	@Override
	public Statement apply(final Statement base, final FrameworkMethod method, Object target) {
		return new Statement() {
			@Override
			public void evaluate()
					throws
					Throwable {
				long start = System.currentTimeMillis();
				try {
					base.evaluate();
				} finally {
					System.out.println("");
					System.out.println(method.getName() + " used " + (System.currentTimeMillis() - start) + " ms;");
				}
			}
		};
	}
}


/**
 * Use it with (in classes):
 *	@Rule
 *	public MethodRule rule = new TimeConsumeRule();
 */
