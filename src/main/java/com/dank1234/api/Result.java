package com.dank1234.api;

/**
 * {@link Result} is an enum that states whether the execution of the code was completed
 * or failed for a specific reason, such as an Exception being thrown would return a
 * Result.EXCEPTION.
 *
 * @author dank1234
 */
public enum Result {
    /**
     * Returns if everything is okay and code executed correctly.
     */
    SUCCESSFUL,
    /**
     * Returns if there was an undefined logic error in the code.
     */
    FAILURE,
    /**
     * Returns if a value was too small or too large.
     */
    BOUND_FAILURE,
    /**
     * Returns if an exception was thrown during execution.
     */
    EXCEPTION
}
