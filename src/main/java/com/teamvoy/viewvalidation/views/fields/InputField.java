package com.teamvoy.viewvalidation.views.fields;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


/**
 * Created by mac on 11/4/16.
 */

public abstract class InputField<T extends View,K extends Object> extends LinearLayout {

    protected static final int DEFAULT_PADDING = 20;
    protected static final int DEFAULT_FONT_SIZE = 14;

    protected T field;

    public InputField(Context context) {
        super(context);
        init();
    }

    public InputField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        setLayoutTransition(new LayoutTransition());
        setOrientation(VERTICAL);
        addView(getView());
    }

    public abstract T getField();

    /**
     * use in cases where getView() and getField are different views for example
     * view is textInputLayout with field in edittext
     * @return
     */
    public abstract View getView();

    public abstract K getValue();

    public abstract void setValue(K text);

}
