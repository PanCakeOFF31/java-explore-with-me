package ru.practicum.common.handler;

public class ApiConstants {
    // 400
    public final static String BAD_REQUEST_STATUS = "BAD_REQUEST";
    public final static String BAD_REQUEST_REASON = "Incorrectly made request.";
    // 403
//    public final static String FORBIDDEN_STATUS = "FORBIDDEN";
//    public final static String FORBIDDEN_REASON = "For the requested operation the conditions are not met.";
    // 404
    public final static String NOT_FOUND_STATUS = "NOT_FOUND";
    public final static String NOT_FOUND_REASON = "The required object was not found.";
    // 405
    public final static String METHOD_NOT_ALLOWED_STATUS = "METHOD_NOT_ALLOWED";
    public final static String METHOD_NOT_ALLOWED_REASON = "The server does not support the request method for the targeted resource.";
    // 409
    public final static String CONFLICT_STATUS = "CONFLICT";
    public final static String CONFLICT_REASON = "Integrity constraint has been violated.";
    // 501
    public final static String METHOD_NOT_IMPLEMENTED_STATUS = "METHOD_NOT_IMPLEMENTED";
    public final static String METHOD_NOT_IMPLEMENTED_REASON = "The server does not support the functionality required to fulfill the request.";
}
