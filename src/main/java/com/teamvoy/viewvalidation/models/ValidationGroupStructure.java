package com.teamvoy.viewvalidation.models;

import com.teamvoy.viewvalidation.views.fields.InputField;
import com.teamvoy.viewvalidation.views.fields.ValidatableInputField;

import java.util.List;

/**
 * Created by mac on 11/18/16.
 */

public class ValidationGroupStructure {
    private InputField initiator;
    private List<ValidatableInputField> fields;
    private boolean isValid;

    public ValidationGroupStructure(InputField initiator, List<ValidatableInputField> fields, boolean isValid) {
        this.initiator = initiator;
        this.fields = fields;
        this.isValid = isValid;
    }

    public InputField getInitiator() {
        return initiator;
    }

    public List<ValidatableInputField> getFields() {
        return fields;
    }

    public boolean isValid() {
        return isValid;
    }
}
