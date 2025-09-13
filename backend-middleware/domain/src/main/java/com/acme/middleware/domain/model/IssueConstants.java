package com.acme.middleware.domain.model;

/**
 * Domain constants for Issue aggregate to avoid hardcoded values throughout the codebase.
 */
public final class IssueConstants {
    
    // Priority constants
    public static final String DEFAULT_PRIORITY = "MEDIUM";
    public static final String HIGH_PRIORITY = "HIGH";
    public static final String LOW_PRIORITY = "LOW";
    
    // Sync status constants
    public static final String SYNC_PENDING = "PENDING";
    public static final String SYNC_SUCCESS = "SYNCED";
    public static final String SYNC_FAILED = "FAILED";
    
    // Title validation constants
    public static final int MIN_TITLE_LENGTH = 1;
    public static final int MAX_TITLE_LENGTH = 255;
    
    // Description validation constants  
    public static final int MAX_DESCRIPTION_LENGTH = 1000;
    
    private IssueConstants() {
        // Utility class - prevent instantiation
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}