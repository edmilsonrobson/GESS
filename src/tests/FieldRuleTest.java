package tests;


import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import exceptions.NoMatchException;
import gess.FieldRule;
import gess.Rule;

public class FieldRuleTest {

	@Test
	public void testFieldRuleExamples() {
		String body = "Name: John\n\nOccupation: Scientist\n\nCháracter: Tést";
		Rule rule1 = new FieldRule(body, "Name:");
		Rule rule2 = new FieldRule(body, "Occupation:");
		Rule rule3 = new FieldRule(body, "Cháracter:");
		String extractedText = "";
		try {			
			Assert.assertEquals("John", rule1.apply());
			Assert.assertEquals("Scientist", rule2.apply());
			Assert.assertEquals("Tést", rule3.apply());
		} catch (NoMatchException e) {		
			fail("Failed");
		}
	}

}
