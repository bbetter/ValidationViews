package com.teamvoy.viewvalidation.models.behaviour;

import com.teamvoy.viewvalidation.views.fields.ValidatableInputField;

/**
 * Created by mac on 11/18/16.
 */

public abstract class ValidationBehaviour {

    protected ValidatableInputField field;

    public ValidationBehaviour(ValidatableInputField field) {
        this.field = field;
    }

    abstract void onValueChanged(String str);

}
