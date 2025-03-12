package com.example.oauth.common;


public interface ResponseMessage {

    String SUCCESS = "Success";

    String VALIDATION_FAIL = "Validation failed";

    String DUPLICATE_ID = "Duplicate Id";

    String SIGN_IN_FAIL = "Sign in fail";
    String CERTIFICATION_FAIL = "Certification failed";

    String MAIL_FAIL = "Mail send fail";
    String DATABASE_ERROR = "Database error";
}