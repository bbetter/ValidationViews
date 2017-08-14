package com.teamvoy.viewvalidation.views.support;

import android.animation.Animator;
import android.view.View;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by mac on 11/8/16.
 */

public class VisibilityAnimator {

    public static void animateVisibility(final View v, final boolean visible) {
        v.animate().scaleY(visible ? 1 : 0).setDuration(400).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(visible ? VISIBLE : GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
