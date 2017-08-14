package com.teamvoy.viewvalidation.models.behaviour;

import com.teamvoy.viewvalidation.views.fields.ValidatableInputField;

/**
 * Created by mac on 11/18/16.
 */

public abstract class EditTextValidationBehaviour extends ValidationBehaviour {

    public EditTextValidationBehaviour(ValidatableInputField field) {
        super(field);
    }

    abstract void onFocusLost();

    abstract void onFocusObtained();
}
