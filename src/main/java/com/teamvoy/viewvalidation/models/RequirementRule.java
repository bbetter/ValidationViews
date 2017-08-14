package com.teamvoy.viewvalidation.models;

import com.teamvoy.viewvalidation.IRule;

/**
 * Created by mac on 12/22/16.
 * default is requrired:  true
 */
public class RequirementRule implements IRule<String> {

    private boolean required = false;

    public RequirementRule() {

    }

    public RequirementRule(boolean req) {
        this.required = req;
    }

    @Override
    public boolean isValid(String val) {
        return !required || !val.isEmpty();
    }

    @Override
    public String getMessage() {
        return "Must not be empty";
    }

    public boolean isRequired() {
        return required;
    }
}
