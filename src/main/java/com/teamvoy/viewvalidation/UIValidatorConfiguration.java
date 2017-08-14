package com.teamvoy.viewvalidation;

import com.teamvoy.viewvalidation.models.MultipleFieldsValidationListener;
import com.teamvoy.viewvalidation.models.ValidationTriggeredListener;
import com.teamvoy.viewvalidation.models.ValidationGroupStructure;
import com.teamvoy.viewvalidation.views.fields.InputField;
import com.teamvoy.viewvalidation.views.fields.ValidatableInputField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 11/16/16.
 */

public class UIValidatorConfiguration {
    private static int groupStartIndex = -1;
    private static int groupEndIndex = -1;

    private List<ValidatableInputField> fields = new ArrayList<>();

    private UIValidatorConfiguration(List<ValidatableInputField> fields) {
        this.fields = fields;
        for (ValidatableInputField field : fields) {
            field.hideErrors();
        }
    }

    private static boolean allOfFieldsValid(List<ValidatableInputField> fields) {
        boolean valid = true;
        for (ValidatableInputField f : fields) {
            valid = valid && f.isValid();
        }
        return valid;
    }

    public static class Builder {
        private List<ValidatableInputField> fields = new ArrayList<>();


        public UIValidatorConfiguration.Builder with(ValidatableInputField field) {
            fields.add(field);
            if (groupStartIndex == groupEndIndex) {
                groupStartIndex++;
            }
            return this;
        }

        public UIValidatorConfiguration.Builder rule(IRule<String> rule) {
            fields.get(fields.size() - 1).addRule(rule);
            return this;
        }

        public UIValidatorConfiguration.Builder onValidationTriggered(ValidationTriggeredListener listener) {
            fields.get(fields.size() - 1).addValidationTriggeredListener(listener);
            return this;
        }

        public UIValidatorConfiguration.Builder onFieldsValidationTriggered(final MultipleFieldsValidationListener listener) {
            groupEndIndex = fields.size() - 1;

            for (final InputField f : fields) {
                ValidatableInputField vField = (ValidatableInputField) f;
                vField.addValidationTriggeredListener(new ValidationTriggeredListener() {
                    @Override
                    public void onValidationTriggered(ValidatableInputField field) {
                        final List<ValidatableInputField> sublistOfFields = Builder.this.fields.subList(groupStartIndex, groupEndIndex);
                        listener.onFieldsValidated(new ValidationGroupStructure(f, sublistOfFields, allOfFieldsValid(sublistOfFields)));
                    }
                });
            }
            return this;
        }


        public UIValidatorConfiguration.Builder onAllFieldsValidationTriggered(final MultipleFieldsValidationListener listener) {
            for (final InputField f : fields) {
                ValidatableInputField vf = (ValidatableInputField) f;
                vf.addValidationTriggeredListener(new ValidationTriggeredListener() {
                    @Override
                    public void onValidationTriggered(ValidatableInputField field) {
                        listener.onFieldsValidated(new ValidationGroupStructure(f, fields, allOfFieldsValid(fields)));
                    }
                });
            }
            return this;
        }

        public UIValidatorConfiguration.Builder rules(List<IRule<String>> rules) {
            fields.get(fields.size() - 1).addRules(rules);
            return this;
        }

        public UIValidatorConfiguration build() {
            return new UIValidatorConfiguration(fields);
        }
    }

    public boolean isValid() {
        return allOfFieldsValid(fields);
    }

    public void hideErrors() {
        for (ValidatableInputField field : fields) {
            field.hideErrors();
        }
    }

    public boolean validate() {
        boolean valid = true;
        for (ValidatableInputField field : fields) {
            valid = field.validate() && valid;
        }
        return valid;
    }
}
