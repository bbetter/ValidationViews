package com.teamvoy.viewvalidation;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class IRuleTest {

    private IRule<String> uaPhoneRule;
    private IRule<String> capitalLetterRule;
    private IRule<String> noLetterPRule;

    private IRule<Integer> ageRule;

    @Before
    public void initRules() {

        ageRule = new IRule<Integer>() {
            @Override
            public boolean isValid(Integer val) {
                return val.compareTo(18) == 1 || val.compareTo(18) == 0;
            }

            @Override
            public String getMessage() {
                return null;
            }
        };

        uaPhoneRule = new IRule<String>() {
            @Override
            public boolean isValid(String val) {
                return val.startsWith("+380") && val.length() == 13;
            }

            @Override
            public String getMessage() {
                return "email";
            }
        };

        capitalLetterRule = new IRule<String>() {
            @Override
            public boolean isValid(String val) {
                return val.toUpperCase().charAt(0) == val.charAt(0);
            }

            @Override
            public String getMessage() {
                return "capital";
            }
        };

        noLetterPRule = new IRule<String>() {
            @Override
            public boolean isValid(String val) {
                return !(val.contains("P") || val.contains("p"));
            }

            @Override
            public String getMessage() {
                return "no letter p";
            }
        };
    }


    @Test
    public void test_basicRulesCorrect() throws Exception {
        assertTrue(uaPhoneRule.isValid("+380634345367"));
        assertFalse(uaPhoneRule.isValid("+522634345367"));

        assertTrue(capitalLetterRule.isValid("Test"));
        assertFalse(capitalLetterRule.isValid("test"));

        assertTrue(noLetterPRule.isValid("klmno_qrstu"));
        assertFalse(noLetterPRule.isValid("klmnopqrstu"));
    }

    @Test
    public void testGenericRulesCorrect() throws Exception {
        assertEquals(ageRule.isValid(5), false);
        assertEquals(ageRule.isValid(18), true);
        assertEquals(ageRule.isValid(19), true);
    }
}