package com.feicuiedu.eshop.base.widgets;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.feicuiedu.eshop.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SimpleNumberPicker extends RelativeLayout {


    @BindView(R.id.edit_number) EditText etNumber;
    @BindView(R.id.image_minus) ImageView ivMinus;

    private OnNumberChangedListener mOnNumberChangedListener;

    public SimpleNumberPicker(Context context) {
        super(context);
        init(context);
    }

    public SimpleNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @OnClick({R.id.image_plus, R.id.image_minus})
    public void onClick(View view) {
        if (view.getId() == R.id.image_minus) {

            if (getNumber() == 0) {
                return;
            }

            setNumber(getNumber() - 1);
        } else {
            setNumber(getNumber() + 1);
        }
    }

    @OnTextChanged(R.id.edit_number)
    public void onTextChanged() {
        if (getNumber() == 0) {
            ivMinus.setImageResource(R.drawable.minus_gray);
        } else {
            ivMinus.setImageResource(R.drawable.minus);
        }

        if (mOnNumberChangedListener != null) {
            mOnNumberChangedListener.onNumberChanged(getNumber());
        }
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_simple_number_picker, this, true);
        ButterKnife.bind(this);
    }

    public int getNumber() {
        String str = etNumber.getText().toString();

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }


    }

    public void setNumber(int number) {
        etNumber.setText(String.valueOf(number));
    }

    public void setOnNumberChangedListener(OnNumberChangedListener onNumberChangedListener) {
        mOnNumberChangedListener = onNumberChangedListener;
    }

    public interface OnNumberChangedListener {
        void onNumberChanged(int number);
    }
}
