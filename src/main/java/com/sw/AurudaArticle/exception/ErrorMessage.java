package com.sw.AurudaArticle.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public final class ErrorMessage {
    public static final String NO_USER = "No user found";
    public static final String NO_ARTICLE = "No article found";
    public static final String NO_COMMENT = "No comment found";
    public static final String INVALID_SORTING_TYPE = "Invalid sorting type";
    public static final String NO_SORTING_TYPE = "No found sorting type";
    public static final String COULD_NOT_CREATE_THE_DIRECTORY="Could not create the directory where the uploaded files will be stored.";
    private ErrorMessage() {
    }
}
