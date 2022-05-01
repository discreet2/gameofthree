package com.takeaway.gameofthree.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UtilityTest {
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 10, 100, 1000})
    void testRandomNumberGeneratingWithinLimit(int value) {
        Assertions.assertTrue(Utility.getRandomNumber(value) < value);
    }
}
