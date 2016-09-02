/*
 * Copyright (c) 2016 Uber Technologies, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.uber.sdk.android.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.res.Attribute;
import org.robolectric.shadows.CoreShadowsAdapter;
import org.robolectric.shadows.RoboAttributeSet;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link UberButton}
 */
public class UberButtonTest extends RobolectricTestBase {

    private static final String ANDROID_ATTR_BACKGROUND = "android:attr/background";
    private static final String ANDROID_ATTR_DRAWABLE_LEFT = "android:attr/drawableLeft";
    private static final String ANDROID_ATTR_DRAWABLE_TOP = "android:attr/drawableTop";
    private static final String ANDROID_ATTR_DRAWABLE_RIGHT = "android:attr/drawableRight";
    private static final String ANDROID_ATTR_DRAWABLE_BOTTOM = "android:attr/drawableBottom";
    private static final String ANDROID_ATTR_DRAWABLE_PADDING = "android:attr/drawablePadding";
    private static final String ANDROID_ATTR_GRAVITY = "android:attr/gravity";
    private static final String ANDROID_ATTR_PADDING = "android:attr/padding";
    private static final String ANDROID_ATTR_PADDING_LEFT = "android:attr/paddingLeft";
    private static final String ANDROID_ATTR_PADDING_TOP = "android:attr/paddingTop";
    private static final String ANDROID_ATTR_PADDING_RIGHT = "android:attr/paddingRight";
    private static final String ANDROID_ATTR_PADDING_BOTTOM = "android:attr/paddingBottom";
    private static final String ANDROID_ATTR_TEXT_COLOR = "android:attr/textColor";
    private static final String ANDROID_ATTR_TEXT_SIZE = "android:attr/textSize";
    private static final String ANDROID_ATTR_TEXT_STYLE = "android:attr/textStyle";
    private static final String ANDROID_ATTR_TEXT = "android:attr/text";

    private static final String ANDROID_COLOR_BLACK = "@android:color/black";
    private static final String ANDROID_COLOR_WHITE = "@android:color/white";
    private static final String DRAWABLE_UBER_BADGE = "@drawable/uber_badge";
    private static final String GRAVITY_END = "end";
    private static final String STYLE_ITALIC = "italic";
    private static final String ONE_SP = "1sp";
    private static final String TWO_SP = "2sp";
    private static final String THREE_SP = "3sp";
    private static final String FOUR_SP = "4sp";
    private static final String TEXT = "test";

    private static final String UBER_PACKAGE_NAME = "com.uber.sdk.android.core";
    private static final String UBER_ATTR_UBER_STYE = UBER_PACKAGE_NAME + ":attr/ub__style";

    private Context context;

    @Before
    public void setup() {
        context = Robolectric.buildActivity(Activity.class).create().get();
    }

    @Test
    public void onCreate_whenBackgroundAttributeSet_shouldSetBackground() {
        AttributeSet attributeSet = makeAttributeSet(
                makeAttribute(ANDROID_ATTR_BACKGROUND, ANDROID_COLOR_WHITE)
        );
        UberButton uberButton = new UberButton(context, attributeSet, 0, 0) { };
        assertEquals(Color.WHITE, ((ColorDrawable) uberButton.getBackground()).getColor());
    }

    @Test
    public void onCreate_whenCompoundDrawablesAndPaddingSet_shouldSetCompoundDrawableAttributes() {
        AttributeSet attributeSet = makeAttributeSet(
                makeAttribute(ANDROID_ATTR_DRAWABLE_LEFT, DRAWABLE_UBER_BADGE),
                makeAttribute(ANDROID_ATTR_DRAWABLE_TOP, DRAWABLE_UBER_BADGE),
                makeAttribute(ANDROID_ATTR_DRAWABLE_RIGHT, DRAWABLE_UBER_BADGE),
                makeAttribute(ANDROID_ATTR_DRAWABLE_BOTTOM, DRAWABLE_UBER_BADGE),
                makeAttribute(ANDROID_ATTR_DRAWABLE_PADDING, ONE_SP)
        );

        UberButton uberButton = new UberButton(context, attributeSet, 0, 0) { };
        Drawable[] drawables = uberButton.getCompoundDrawables();
        assertNotNull(drawables[0]);
        assertNotNull(drawables[1]);
        assertNotNull(drawables[2]);
        assertNotNull(drawables[3]);
        assertEquals(1, uberButton.getCompoundDrawablePadding());
    }

    @Test
    public void onCreate_whenOverallPaddingSet_shouldAddOverallPadding() {
        AttributeSet attributeSet = makeAttributeSet(
                makeAttribute(ANDROID_ATTR_PADDING, ONE_SP)
        );
        UberButton uberButton = new UberButton(context, attributeSet, 0, 0) { };
        assertEquals(1, uberButton.getPaddingLeft());
        assertEquals(1, uberButton.getPaddingTop());
        assertEquals(1, uberButton.getPaddingRight());
        assertEquals(1, uberButton.getPaddingBottom());
    }

    @Test
    public void onCreate_whenIndividualPaddingsSet_shouldHaveSeparatePaddings() {
        AttributeSet attributeSet = makeAttributeSet(
                makeAttribute(ANDROID_ATTR_PADDING_LEFT, ONE_SP),
                makeAttribute(ANDROID_ATTR_PADDING_TOP, TWO_SP),
                makeAttribute(ANDROID_ATTR_PADDING_RIGHT, THREE_SP),
                makeAttribute(ANDROID_ATTR_PADDING_BOTTOM, FOUR_SP)
        );
        UberButton uberButton = new UberButton(context, attributeSet, 0, 0) { };
        assertEquals(1, uberButton.getPaddingLeft());
        assertEquals(2, uberButton.getPaddingTop());
        assertEquals(3, uberButton.getPaddingRight());
        assertEquals(4, uberButton.getPaddingBottom());
    }

    @Test
    public void onCreate_whenIndividualAndOverallPaddingsSet_shouldHaveIndividualPaddingsTrumpOverall() {
        AttributeSet attributeSet = makeAttributeSet(
                makeAttribute(ANDROID_ATTR_PADDING, ONE_SP),
                makeAttribute(ANDROID_ATTR_PADDING_TOP, TWO_SP),
                makeAttribute(ANDROID_ATTR_PADDING_BOTTOM, FOUR_SP)
        );
        UberButton uberButton = new UberButton(context, attributeSet, 0, 0) { };
        assertEquals(1, uberButton.getPaddingLeft());
        assertEquals(2, uberButton.getPaddingTop());
        assertEquals(1, uberButton.getPaddingRight());
        assertEquals(4, uberButton.getPaddingBottom());
    }

    @Test
    public void onCreate_whenTextAttributesSet_shouldAddAllAttributes() {
        AttributeSet attributeSet = makeAttributeSet(
                makeAttribute(ANDROID_ATTR_TEXT_COLOR, ANDROID_COLOR_BLACK),
                makeAttribute(ANDROID_ATTR_GRAVITY, GRAVITY_END),
                makeAttribute(ANDROID_ATTR_TEXT_SIZE, FOUR_SP),
                makeAttribute(ANDROID_ATTR_TEXT_STYLE, STYLE_ITALIC),
                makeAttribute(ANDROID_ATTR_TEXT, TEXT)
        );
        UberButton uberButton = new UberButton(context, attributeSet, 0, 0) { };
        assertEquals(Color.BLACK, uberButton.getCurrentTextColor());
        assertEquals(Typeface.ITALIC, uberButton.getTypeface().getStyle());
        assertEquals(4, uberButton.getTextSize(), 0);
        assertEquals(TEXT, uberButton.getText().toString());
        assertTrue(uberButton.getGravity() != 0);
    }

    @Test
    public void onCreate_whenNoAttributesSet_shouldUseUberButtonDefaults() {
        UberButton uberButton = new UberButton(context, null, 0, 0) { };
        Resources resources = context.getResources();

        assertEquals(R.drawable.uber_button_background_selector_black,
                uberButton.getBackgroundResource());

        assertNull(uberButton.getCompoundDrawables()[0]);
        assertNull(uberButton.getCompoundDrawables()[1]);
        assertNull(uberButton.getCompoundDrawables()[2]);
        assertNull(uberButton.getCompoundDrawables()[3]);

        assertEquals(0, uberButton.getPaddingLeft(), 0);
        assertEquals(0, uberButton.getPaddingTop(), 0);
        assertEquals(0, uberButton.getPaddingRight(), 0);
        assertEquals(0, uberButton.getPaddingBottom(), 0);

        assertEquals(resources.getColor(R.color.uber_white), uberButton.getCurrentTextColor());
        assertEquals(Typeface.NORMAL, uberButton.getTypeface().getStyle());
        assertTrue(StringUtils.isEmpty(uberButton.getText().toString()));
    }

    @Test
    public void onCreate_whenUberStyleSet_shouldUseUberStyle() {
        AttributeSet attributeSet = makeAttributeSet(
                makeAttribute(UBER_ATTR_UBER_STYE, "white")
        );

        UberButton uberButton = new UberButton(context, attributeSet, 0, R.style.UberButton_White) { };
        Resources resources = context.getResources();

        assertEquals(R.drawable.uber_button_background_selector_white, uberButton.getBackgroundResource());

        assertNull(uberButton.getCompoundDrawables()[0]);
        assertNull(uberButton.getCompoundDrawables()[1]);
        assertNull(uberButton.getCompoundDrawables()[2]);
        assertNull(uberButton.getCompoundDrawables()[3]);

        assertEquals(0, uberButton.getPaddingLeft(), 0);
        assertEquals(0, uberButton.getPaddingTop(), 0);
        assertEquals(0, uberButton.getPaddingRight(), 0);
        assertEquals(0, uberButton.getPaddingBottom(), 0);

        assertEquals(resources.getColor(R.color.uber_black), uberButton.getCurrentTextColor());
        assertEquals(Typeface.NORMAL, uberButton.getTypeface().getStyle());
        assertTrue(uberButton.getGravity() != 0);
        assertTrue(StringUtils.isEmpty(uberButton.getText().toString()));
    }

    @Test
    public void getActivity_whenAttachedToActivity_shouldReturnActivity() {
        final Activity testActivity = Robolectric.setupActivity(Activity.class);

        final UberButton uberButton = new UberButton(testActivity, null, 0, 0) { };

        assertThat(uberButton.getActivity()).isSameAs(testActivity);
    }

    @Test
    public void getActivity_whenContextWrapper_shouldReturnActivity() {
        final Activity testActivity = Robolectric.setupActivity(Activity.class);
        final ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(testActivity, 0);

        UberButton uberButton = new UberButton(contextThemeWrapper, null, 0, 0) { };

        assertThat(uberButton.getActivity()).isSameAs(testActivity);
    }

    @Test
    public void getActivity_whenContextWrapperNested_shouldReturnActivity() {
        final Activity testActivity = Robolectric.setupActivity(Activity.class);
        final ContextThemeWrapper nestedWrapper = new ContextThemeWrapper(testActivity, 0);
        final ContextThemeWrapper wrapper = new ContextThemeWrapper(nestedWrapper, 0);

        UberButton uberButton = new UberButton(wrapper, null, 0, 0) { };

        assertThat(uberButton.getActivity()).isSameAs(testActivity);
    }

    @Test(expected = IllegalStateException.class)
    public void getActivity_whenContextIsNotActivity_shouldThrowException() {
        final ContextThemeWrapper wrapper = new ContextThemeWrapper(RuntimeEnvironment.application, 0);

        UberButton uberButton = new UberButton(wrapper, null, 0, 0) { };

        uberButton.getActivity();
    }

    private static AttributeSet makeAttributeSet(Attribute... attributes) {
        return new RoboAttributeSet(Arrays.asList(attributes), new CoreShadowsAdapter().getResourceLoader());
    }

    private static Attribute makeAttribute(String fullyQualifiedAttributeName, Object value) {
        return new Attribute(fullyQualifiedAttributeName, String.valueOf(value), UBER_PACKAGE_NAME);
    }
}
