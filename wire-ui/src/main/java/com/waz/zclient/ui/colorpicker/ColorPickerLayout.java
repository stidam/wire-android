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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.waz.zclient.ui.R;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerLayout extends LinearLayout {

    public static final String TAG = ColorPickerLayout.class.getName();
    private static final String DEFAULT_EMOJI = "\uD83D\uDE03";
    private String currentEmoji;

    // the list of colors offered by the app
    private int[] accentColors;

    private List<ColorPickerView> colorDotViews = new ArrayList<>();
    private ColorPickerEmojiView emojiView;
    private ColorPickerKeyboardGlyphTextView keyboardView;

    private OnColorSelectedListener onColorSelectedListener;
    private OnWidthChangedListener onWidthChangedListener;

    private int currentDotRadius = getResources().getDimensionPixelSize(R.dimen.color_picker_small_dot_radius);
    private EmojiSize currentEmojiSize = EmojiSize.SMALL;

    public ColorPickerLayout(Context context) {
        this(context, null);
    }

    public ColorPickerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        currentEmoji = DEFAULT_EMOJI;
        keyboardView = new ColorPickerKeyboardGlyphTextView(getContext());
        keyboardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorSelectedListener.onTextClick();
                setCurrentColor(keyboardView);
            }
        });
        emojiView = (ColorPickerEmojiView) LayoutInflater.from(getContext()).inflate(R.layout.color_picker_emoji_view, this, false);
        emojiView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorSelectedListener.onMoreClick();
            }
        });
        int[] colors = getResources().getIntArray(R.array.accents_color);
        setAccentColors(colors, colors[0]);

        invalidate();
    }


    private void populateDotsView(int selectedColor) {
        if (accentColors == null) {
            return;
        }
        if (getChildCount() != 0) {
            removeAllViews();
        }

        emojiView.setEmoji(currentEmoji);
        colorDotViews.add(emojiView);
        addView(emojiView);

        View divider = new View(getContext());
        divider.setBackgroundColor(getResources().getColor(R.color.light_graphite_40));
        int width = getResources().getDimensionPixelSize(R.dimen.wire__divider__height__thin);
        int height = getResources().getDimensionPixelSize(R.dimen.color_picker_divider_height);
        LayoutParams params = new LayoutParams(width, height);
        addView(divider, params);

        LayoutParams keyboardLayoutParams = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.color_picker_emoji_dot_width), getResources().getDimensionPixelSize(R.dimen.color_picker_emoji_dot_height));
        addView(keyboardView, keyboardLayoutParams);
        colorDotViews.add(keyboardView);

        divider = new View(getContext());
        divider.setBackgroundColor(getResources().getColor(R.color.light_graphite_40));
        addView(divider, params);

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for (int accentColor: accentColors) {
            final ColorPickerDotView dot = (ColorPickerDotView) layoutInflater.inflate(R.layout.color_picker_dot_view, this, false);
            dot.setColor(accentColor);
            dot.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrentColor(dot);
                }
            });
            if (selectedColor == accentColor) {
                dot.setSelected(currentDotRadius);
            }
            colorDotViews.add(dot);
            addView(dot);
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                //to notify of width change
                if (onWidthChangedListener == null) {
                    return;
                }
                onWidthChangedListener.onScrollWidthChanged(getWidth());
            }
        }, 0);
    }

    public void setAccentColors(int[] colors, int selectedColor) {
        accentColors = colors;
        populateDotsView(selectedColor);
        invalidate();
    }

    public void setCurrentColor(ColorPickerView view) {
        for (ColorPickerView colorPickerView: colorDotViews) {
            if (colorPickerView != view) {
                colorPickerView.setUnselected();
            } else {
                if (view instanceof ColorPickerDotView) {
                    colorPickerView.setSelected(currentDotRadius);
                    currentDotRadius = colorPickerView.getSize();
                    ColorPickerDotView colorPickerDotView = (ColorPickerDotView) colorPickerView;
                    onColorSelectedListener.onColorSelected(colorPickerDotView.getCircleColor(), colorPickerDotView.getStrokeSize());
                } else if (view instanceof ColorPickerEmojiView) {
                    colorPickerView.setSelected(currentEmojiSize.ordinal());
                    ColorPickerEmojiView colorPickerEmojiView = (ColorPickerEmojiView) colorPickerView;
                    onColorSelectedListener.onEmojiSelected(colorPickerEmojiView.getEmoji(), colorPickerEmojiView.getEmojiSize());
                } else if (view instanceof ColorPickerKeyboardGlyphTextView) {
                    colorPickerView.setSelected(0);
                } else {
                    Timber.e("asdf feck");
                }
            }
        }
        invalidate();
    }

    public void setCurrentEmoji(String emoji, EmojiSize size) {
        emojiView.setEmoji(emoji);
        currentEmojiSize = size;
        setCurrentColor(emojiView);
    }

    /**
     * Sets the callback of the parent
     */
    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        this.onColorSelectedListener = onColorSelectedListener;
    }

    public void setOnWidthChangedListener(OnWidthChangedListener onWidthChangedListener) {
        this.onWidthChangedListener = onWidthChangedListener;
    }

    /**
     * Callback to parent
     */
    public interface OnColorSelectedListener {
        void onColorSelected(int color, int strokeSize);
        void onEmojiSelected(String emoji, int size);
        void onMoreClick();
        void onTextClick();
    }

    public interface OnWidthChangedListener {
        void onScrollWidthChanged(int width);
    }
}
