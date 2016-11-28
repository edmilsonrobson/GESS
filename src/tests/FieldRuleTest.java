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
		String body = "Name: John\n\nOccupation: Scientist\n\nCh�racter: T�st";
		Rule rule1 = new FieldRule("Name:");
		Rule rule2 = new FieldRule("Occupation:");
		Rule rule3 = new FieldRule("Ch�racter:");
		String extractedText = "";
		try {			
			Assert.assertEquals("John", rule1.apply(body));
			Assert.assertEquals("Scientist", rule2.apply(body));
			Assert.assertEquals("T�st", rule3.apply(body));
		} catch (NoMatchException e) {		
			fail("Failed");
		}
	}

}
