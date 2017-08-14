package com.teamvoy.viewvalidation.views.fields;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamvoy.viewvalidation.IRule;
import com.teamvoy.viewvalidation.models.RequirementRule;
import com.teamvoy.viewvalidation.models.ValidationTriggeredListener;
import com.teamvoy.viewvalidation.views.support.HintView;

import java.util.ArrayList;
import java.util.List;

import static com.teamvoy.viewvalidation.views.support.VisibilityAnimator.animateVisibility;


/**
 * Created by mac on 11/8/16.
 */

public abstract class ValidatableInputField<T extends View, K extends Object> extends InputField<T, K> {

    private boolean validating = true;
    private boolean required = true;

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isValidating() {
        return validating;
    }

    public void setValidating(boolean validating) {
        this.validating = validating;
    }

    private static final int DEFAULT_STATE_IMAGE_SIZE = 50;

    protected List<ValidationTriggeredListener> validationTriggeredListeners = new ArrayList<>();

    public void setValidationTriggeredListener(List<ValidationTriggeredListener> validationTriggeredListeners) {
        this.validationTriggeredListeners = validationTriggeredListeners;
    }

    public void addAllValidationTriggeredListener(List<ValidationTriggeredListener> validationTriggeredListeners) {
        this.validationTriggeredListeners.addAll(validationTriggeredListeners);
    }

    public void addValidationTriggeredListener(ValidationTriggeredListener listener) {
        this.validationTriggeredListeners.add(listener);
    }

    public void triggerValidationListeners() {
        if (validationTriggeredListeners != null) {
            for (ValidationTriggeredListener listener : validationTriggeredListeners) {
                listener.onValidationTriggered(this);
            }
        }
    }

    protected List<IRule<K>> rules = new ArrayList<>();

    public List<IRule<K>> getRules() {
        return rules;
    }

    /**
     * here we are setting rules
     *
     * @param rules
     */
    public void setRules(List<IRule<K>> rules) {
        this.rules.clear();
        for (IRule rule : rules) {
            if ((rule instanceof RequirementRule)) {
                final boolean required = ((RequirementRule) rule).isRequired();
                setRequired(required);
            } else {
                this.rules.add(rule);
            }
        }
    }

    public void addRules(List<IRule<K>> rules) {
        for (IRule rule : rules) {
            if ((rule instanceof RequirementRule)) {
                final boolean required = ((RequirementRule) rule).isRequired();
                setRequired(required);
            } else {
                this.rules.add(rule);
            }
        }
    }

    public void addRule(IRule<K> rule) {
        this.rules.add(rule);
    }

    public ValidatableInputField(Context context) {
        super(context);
    }

    public ValidatableInputField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ValidatableInputField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected abstract View getSingleErrorView();

    protected abstract ViewGroup getHintsViewContainer();

    protected abstract Drawable getErrorDrawable();

    protected abstract Drawable getSuccessDrawable();

    //not sure about this two being abstract(perhapse make some default implementation)
    //issue is that for edit text for example it's right drawable
    public abstract void triggerDrawableValidation();

    public abstract void clearDrawable();

    @Override
    protected void init() {
        super.init();

        if (getSingleErrorView() != null) {
            addView(getSingleErrorView());
        }

        if (getHintsViewContainer() != null) {
            addView(getHintsViewContainer());
        }
    }

    protected BitmapDrawable getDrawable(@DrawableRes int res) {
        Bitmap bitmap = ((BitmapDrawable) getContext().getResources().getDrawable(res)).getBitmap();
        return new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,
                DEFAULT_STATE_IMAGE_SIZE, DEFAULT_STATE_IMAGE_SIZE, true));
    }

    //region checks
    public boolean isValid() {
        if (!isRequired() && isEmpty()) return true;
        for (IRule<K> rule : rules) {
            if (!rule.isValid(getValue())) return false;
        }
        return true;
    }

    public boolean isRequired() {
        return !(rules == null || rules.isEmpty()) && required;
    }

    public boolean areMultipleRulesAvailable() {
        return rules.size() > 1;
    }
    //endregion

    //region actions

    public void hideErrors() {
        if (getHintsViewContainer() != null)
            animateVisibility(getHintsViewContainer(), false);
        if (getSingleErrorView() != null)
            animateVisibility(getSingleErrorView(), false);
    }


    public void triggerSingleErrorValidation() {
        animateVisibility(getSingleErrorView(), true);
    }

    //endregion

    //region defaults
    @NonNull
    public static TextView getDefaultErrorView(Context context, String txt) {
        TextView temp = new TextView(context);
        temp.setVisibility(GONE);
        temp.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
        temp.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        temp.setTextColor(context.getResources().getColor(android.R.color.white));
        temp.setTextSize(DEFAULT_FONT_SIZE);
        temp.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        temp.setText(txt);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        temp.setLayoutParams(params);
        return temp;
    }

    @NonNull
    public static LinearLayout getDefaultHintsContainer(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setVisibility(GONE);
        layout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(VERTICAL);
        return layout;
    }
    //endregion

    public void triggerHintsValidation() {
        if (getValue() instanceof String) {
            K value = getValue();
            ViewGroup container = getHintsViewContainer();
            container.removeAllViews();
            animateVisibility(container, !isValid());
            if (rules != null && !rules.isEmpty()) {
                for (IRule<K> rule : rules) {
                    container.addView(new HintView<>(getContext(), rule, value));
                }
            }
        }
    }

    public boolean isEmpty() {
        return getValue() != null && getValue().toString().isEmpty();
    }

    public boolean validate() {
        boolean valid = true;
        if (isValid()) {
            hideErrors();
        } else {
            if (areMultipleRulesAvailable()) {
                triggerHintsValidation();
            } else {
                triggerSingleErrorValidation();
            }
            valid = false;
        }
//        not needed
//        triggerValidationListeners();
        return valid;
    }
}
