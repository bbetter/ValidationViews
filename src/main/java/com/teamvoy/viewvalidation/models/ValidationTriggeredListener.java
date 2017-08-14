package com.teamvoy.viewvalidation.models;

import com.teamvoy.viewvalidation.views.fields.ValidatableInputField;

/**
 * Created by mac on 11/8/16.
 */

public interface ValidationTriggeredListener {
    void onValidationTriggered(ValidatableInputField field);
}
