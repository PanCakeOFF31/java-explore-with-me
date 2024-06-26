package ru.practicum.common.handler;

public class ApiConstants {
    // 400
    public static final String BAD_REQUEST_STATUS = "BAD_REQUEST";
    public static final String BAD_REQUEST_REASON = "Incorrectly made request.";
    // 403
//    public static final String FORBIDDEN_STATUS = "FORBIDDEN";
//    public static final String FORBIDDEN_REASON = "For the requested operation the conditions are not met.";
    // 404
    public static final String NOT_FOUND_STATUS = "NOT_FOUND";
    public static final String NOT_FOUND_REASON = "The required object was not found.";
    // 405
    public static final String METHOD_NOT_ALLOWED_STATUS = "METHOD_NOT_ALLOWED";
    public static final String METHOD_NOT_ALLOWED_REASON = "The server does not support the request method for the targeted resource.";
    // 409
    public static final String CONFLICT_STATUS = "CONFLICT";
    public static final String CONFLICT_REASON = "Integrity constraint has been violated.";
    // 501
    public static final String METHOD_NOT_IMPLEMENTED_STATUS = "METHOD_NOT_IMPLEMENTED";
    public static final String METHOD_NOT_IMPLEMENTED_REASON = "The server does not support the functionality required to fulfill the request.";
}
