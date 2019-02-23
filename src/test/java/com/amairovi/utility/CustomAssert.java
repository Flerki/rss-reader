package com.amairovi.utility;

import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomAssert {

    public static void assertThrowsNpe(Executable executable){
        assertThrows(NullPointerException.class, executable);
    }

    public static void assertThrowsIae(Executable executable){
        assertThrows(IllegalArgumentException.class, executable);
    }
}
