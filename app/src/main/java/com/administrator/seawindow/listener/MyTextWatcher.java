package com.administrator.seawindow.listener;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.administrator.seawindow.R;
import com.administrator.seawindow.utils.ToastUtil;

import java.util.Set;

/**
 * 检测用户输入的内容是否符合规则
 */
public class MyTextWatcher implements TextWatcher {

    private EditText textTarget;
    private Context mContext;
    private String text_null;
    private String text_too_long;
    private int text_max;

    public static final int TYPE_COMMENT = 0;
    public static final int TYPE_USERNAME = 1;

    public MyTextWatcher(EditText textTarget, int targetType) {
        if (textTarget == null) {
            return;
        }
        this.textTarget = textTarget;
        mContext = textTarget.getContext();
        textTarget.addTextChangedListener(this);

        switch (targetType) {
            case TYPE_COMMENT:
                text_max = 210;
                text_null = mContext.getString(R.string.null_comment);
                text_too_long = mContext.getString(R.string.comment_too_long);
                break;
            case TYPE_USERNAME:
                text_max = 21;
                text_null = mContext.getString(R.string.re_enter_username);
                text_too_long = mContext.getString(R.string.username_is_too_long);
                break;
        }
    }

    public int judgeText(){
        String text = textTarget.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ToastUtil.show(mContext, text_null);
            return -1;
        }
        return 0;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (text_max != 0) {
            int pos = editable.length() - 1;
            if (editable.toString().getBytes().length > text_max) {
                editable.delete(pos, pos + 1);
                ToastUtil.show(mContext, text_too_long);
            }
        }
    }
}
