package com.teamvoy.viewvalidation;

/**
 * Created by mac on 11/9/16.
 */

public interface IRule<T> {
    boolean isValid(T val);

    String getMessage();

}
