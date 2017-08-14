package com.teamvoy.viewvalidation.models.behaviour;

import android.widget.EditText;

import com.teamvoy.viewvalidation.views.fields.ValidatableInputField;

/**
 * Created by mac on 11/18/16.
 */

public class DefaultEditTextValidationBehaviour extends EditTextValidationBehaviour {

    public DefaultEditTextValidationBehaviour(ValidatableInputField<EditText, String> field) {
        super(field);
    }

    @Override
    public void onFocusLost() {
        field.triggerDrawableValidation();
        if (field.isValid()) {
            field.hideErrors();
        } else {
            if (field.areMultipleRulesAvailable()) {
                if (field.isEmpty()) {
                    field.hideErrors();
                } else {
                    field.triggerHintsValidation();
                }
            } else {
                field.triggerSingleErrorValidation();
            }
        }
        field.triggerValidationListeners();
    }

    @Override
    public void onFocusObtained() {
        field.triggerDrawableValidation();
        if (field.areMultipleRulesAvailable()) {
            if (field.isEmpty()) {
                field.triggerHintsValidation();
            }
        }
        field.triggerValidationListeners();
    }

    @Override
    public void onValueChanged(String str) {
        if (field.isValid()) {
            field.triggerDrawableValidation();
            field.hideErrors();
        } else {
            if (field.areMultipleRulesAvailable()) {
                field.triggerHintsValidation();
            }
        }

        field.triggerValidationListeners();
    }
}
