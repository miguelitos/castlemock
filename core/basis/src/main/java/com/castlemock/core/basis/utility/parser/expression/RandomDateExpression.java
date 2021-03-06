/*
 * Copyright 2017 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.core.basis.utility.parser.expression;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link RandomDateExpression} is an {@link Expression} and will
 * transform an matching input string into a random date.
 * @author Karl Dahlgren
 * @since 1.13
 */
public class RandomDateExpression extends AbstractExpression {

    private static final long RANGE = 1000000000000L;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String IDENTIFIER = "RANDOM_DATE";

    /**
     * The transform method provides the functionality to transform a provided <code>input</code>.
     * The transformation and the end result will be determine by how it is implemented by each {@link Expression}.
     * Each {@link Expression} provides it's own functionality and will transform the text differently.
     * @param input The input string that will be transformed.
     * @return A transformed <code>input</code>.
     */
    @Override
    public String transform(final ExpressionInput input) {
        final long now = System.currentTimeMillis();
        final long leftLimit = now - RANGE;
        final long rightLimit = now + RANGE;
        final long randomValue = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        final Date date = new Date(Math.abs(randomValue));
        return DATE_FORMAT.format(date);
    }

    /**
     * The match method is used to determine if an <code>input</code> string matches
     * the criteria to be transformed.
     * @param input The input that will be determine if it matches the criteria to be transformed.
     * @return True if the input string matches the criteria. False otherwise.
     */
    @Override
    public boolean match(final String input) {
        return IDENTIFIER.equalsIgnoreCase(input);
    }
}
