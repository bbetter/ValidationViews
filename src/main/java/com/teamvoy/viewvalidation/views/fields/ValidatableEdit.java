package com.teamvoy.viewvalidation.views.fields;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamvoy.viewvalidation.IRule;
import com.teamvoy.viewvalidation.R;
import com.teamvoy.viewvalidation.models.behaviour.DefaultEditTextValidationBehaviour;
import com.teamvoy.viewvalidation.views.support.HintView;

import static com.teamvoy.viewvalidation.views.support.VisibilityAnimator.animateVisibility;

/**
 * Created by mac on 11/10/16.
 */
public class ValidatableEdit extends ValidatableInputField<EditText, String> implements TextWatcher, View.OnFocusChangeListener {

    /**
     * somehow use parent class
     */
    private DefaultEditTextValidationBehaviour defaultBehaviour;

    public enum Mode {
        TEXT,
        PHONE,
        PASSWORD,
        EMAIl
    }

    private TextView singleErrorView;
    private ViewGroup hintsErrorContainer;

    private int singleErrorViewBackgroundColor;
    private int singleErrorViewTextColor;

    public ValidatableEdit(Context context) {
        super(context);
    }

    public ValidatableEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
    }

    public ValidatableEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);
    }

    private void initAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ValidatableEdit,
                0, 0);
        final int index = a.getInteger(R.styleable.ValidatableEdit_inputType, 0);
        Mode mode = Mode.values()[index];
        String hint = a.getString(R.styleable.ValidatableEdit_hint);
        switch (mode) {
            case EMAIl:
                getField().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case TEXT:
                getField().setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case PHONE:
                getField().setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case PASSWORD:
                getField().setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
        }
        setHint(hint);
        singleErrorViewBackgroundColor = a.getColor(R.styleable.ValidatableEdit_singleErrorBackgroundColor, getContext().getResources().getColor(R.color.red));
        singleErrorViewTextColor = a.getColor(R.styleable.ValidatableEdit_singleErrorTextColor, getContext().getResources().getColor(android.R.color.white));
        updateAttributes();
        a.recycle();
    }

    @Override
    protected void init() {
        super.init();
        updateAttributes();
        updateListeners();
        defaultBehaviour = new DefaultEditTextValidationBehaviour(this);
    }

    private void updateListeners() {
        getField().setOnFocusChangeListener(this);
        getField().addTextChangedListener(this);
    }

    private void updateAttributes() {
        getField().setMaxLines(1);
        getField().setSingleLine(true);
        getField().setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }

    @Override
    public String getValue() {
        return getField().getText().toString();
    }

    @Override
    public void setValue(String text) {
        getField().setText(text);
    }

    //region basic views
    @Override
    public View getView() {
        return getField();
    }

    @Override
    public EditText getField() {
        if (field == null) {
            field = new EditText(getContext());
            field.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return field;
    }

    @Override
    public View getSingleErrorView() {
        if (singleErrorView == null) {
            singleErrorView = getDefaultErrorView(getContext(), "");
        }
        if (rules != null && !rules.isEmpty()) {
            singleErrorView.setTextSize(14);
            singleErrorView.setText(rules.get(0).getMessage());
        }
        return singleErrorView;
    }

    @Override
    public ViewGroup getHintsViewContainer() {
        if (hintsErrorContainer == null) {
            hintsErrorContainer = getDefaultHintsContainer(getContext());
        }
        return hintsErrorContainer;
    }

    @Override
    protected Drawable getErrorDrawable() {
        return getDrawable(R.drawable.ic_error);
    }

    @Override
    protected Drawable getSuccessDrawable() {
        return getDrawable(R.drawable.ic_tick);
    }
    //endregion

    //region callbacks
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (isValidating()) {
            if (hasFocus) {
                defaultBehaviour.onFocusObtained();
            } else {
                defaultBehaviour.onFocusLost();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (rules == null || rules.isEmpty()) return;
        if (isValidating()) {
            defaultBehaviour.onValueChanged(s.toString());
        }
        requestLayout();
    }

    //endregion

    //region actions
    @Override
    public void clearDrawable() {
        getField().setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    @Override
    public void triggerDrawableValidation() {
        if (isEmpty()) {
            clearDrawable();
        } else {
            Drawable drawable;
            if (isValid()) {
                drawable = getSuccessDrawable();
            } else {
                drawable = getErrorDrawable();
            }
            getField().setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    public void setHint(String str) {
        getField().setHint(str);
    }

    //endregion

    //region not needed
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    //endregion
}
