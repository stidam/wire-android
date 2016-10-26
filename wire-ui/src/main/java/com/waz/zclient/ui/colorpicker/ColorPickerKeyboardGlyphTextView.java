/**
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waz.zclient.ui.colorpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import com.waz.zclient.ui.R;
import com.waz.zclient.ui.text.GlyphTextView;


public class ColorPickerKeyboardGlyphTextView extends GlyphTextView implements ColorPickerView {

    public ColorPickerKeyboardGlyphTextView(Context context) {
        this(context, null);
    }

    public ColorPickerKeyboardGlyphTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerKeyboardGlyphTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setText(R.string.glyph__keyboard);
        setGravity(Gravity.CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.wire__text_size__regular));
    }

    @Override
    public void setSelected(int size) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.wire__text_size__big));
        invalidate();
    }

    @Override
    public void setUnselected() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.wire__text_size__regular));
        invalidate();
    }

    @Override
    public int getSize() {
        return 0;
    }
}
