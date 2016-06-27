package com.yahoo.squidb.sql;

/**
 * Case-insensitive criterion that checks for if the given expression contains the provided
 * text.
 */
public class CaseInsensitiveLikeCriterion extends CaseInsensitiveEqualsCriterion {

    public static CaseInsensitiveLikeCriterion insensitiveLike(final Field<String> expression, final String value) {
        return new CaseInsensitiveLikeCriterion(expression, value);
    }

    public CaseInsensitiveLikeCriterion(final Field<String> expression, final String value) {
        super(expression, Operator.like, getWildCardQuery(value));
    }

    private static String getWildCardQuery(final String text) {
        return "%" + text + "%";
    }
}
