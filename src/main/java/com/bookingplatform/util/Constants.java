package com.bookingplatform.util;

public final class Constants {

    private Constants(){}

    /*
     * JWT
     */

    public static final String TOKEN_PREFIX =
            "Bearer ";

    public static final String AUTH_HEADER =
            "Authorization";

    /*
     * ROLES
     */

    public static final String ROLE_ADMIN =
            "ROLE_ADMIN";

    public static final String ROLE_CUSTOMER =
            "ROLE_CUSTOMER";

    public static final String ROLE_PROVIDER =
            "ROLE_PROVIDER";

    /*
     * FILES
     */

    public static final long MAX_FILE_SIZE =
            20 * 1024 * 1024;

    /*
     * PAGINATION
     */

    public static final int DEFAULT_PAGE =
            0;

    public static final int DEFAULT_SIZE =
            10;

}