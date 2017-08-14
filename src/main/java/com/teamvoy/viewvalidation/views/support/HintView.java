package com.teamvoy.viewvalidation.views.support;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamvoy.viewvalidation.IRule;
import com.teamvoy.viewvalidation.R;


/**
 * Created by mac on 11/3/16.
 */

public class HintView<K extends Object> extends LinearLayout {

    private String text = "";

    private State state = State.EMPTY;

    public void updateContent() {
        final TextView textView = (TextView) findViewById(R.id.hintTextView);
        final ImageView imageView = (ImageView) findViewById(R.id.hintImageView);
        textView.setText(text);
        switch (state) {
            case VALID:
                imageView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ic_tick));
                break;
            case INVALID:
                textView.setTextColor(Color.RED);
                imageView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ic_error));
                break;
            case EMPTY:
                final Drawable drawable = getContext().getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel);
                drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                imageView.setBackgroundDrawable(drawable);
                break;
        }
    }

    public State getState() {
        return state;
    }

    public enum State {
        EMPTY,
        VALID,
        INVALID
    }

    public void setState(State state) {
        this.state = state;
        updateContent();
        requestLayout();
    }

    public HintView(Context context) {
        super(context);
        init(context, State.VALID);
    }

    public HintView(Context context, IRule<K> rule, K value) {
        super(context);
        State state = State.EMPTY;
        if (!value.toString().isEmpty()) {
            state = rule.isValid(value) ? State.VALID : State.INVALID;
        }
        this.text = rule.getMessage();
        init(context, state);
    }

    private void init(Context context, State state) {
        inflate(context, R.layout.simple_hint_item, this);
        setState(state);
    }

}
