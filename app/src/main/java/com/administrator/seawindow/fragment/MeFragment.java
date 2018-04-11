package com.administrator.seawindow.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.administrator.seawindow.EditPictureActivity;
import com.administrator.seawindow.LoginActivity;
import com.administrator.seawindow.R;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.utils.PreferenceUtil;
import com.administrator.seawindow.utils.ToastUtil;
import com.administrator.seawindow.view.ImageCircleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "MeFragment";
    private String nickName;
    private String phoneNum;
    private String email;
    private TextView et_name_info, et_phone_info, et_email_info;
    private Button loginout;
    private ImageCircleView imageCircleView;
    private View popView;
    private View mRootView;
    private Button addPictureFromPhotoBtn, addPictureFromCameraBtn, cancle;
    private PopupWindow mPopupWindow;
    private File mCurrentPhotoFile;
    private String tempPhotoPath;
    private String photoAdr;
    private int getHeadMode;

    private final int NEED_PIC_PHOTO = 100;
    private final int NEED_CAMERA = 200;
    private final int PHOTO_PICKED_WITH_DATA = 3021;
    /* 用来标识请求照相功能的activity */
    private final int CAMERA_WITH_DATA = 1000;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_me_layout, null);
        getData();
        initView(mRootView);
        return mRootView;
    }

    private void getData(){
        HashMap<String, String> userInfo = PreferenceUtil.getUserInfo(getActivity());
        nickName = userInfo.get("nickName");
        phoneNum = userInfo.get("phoneNum");
        email = userInfo.get("email");
    }

    private void initView(View view){
        et_name_info = view.findViewById(R.id.et_name_info);
        et_phone_info = view.findViewById(R.id.et_phone_info);
        et_email_info = view.findViewById(R.id.et_email_info);
        imageCircleView = view.findViewById(R.id.civ_info);
        et_name_info.setText(nickName);
        et_phone_info.setText(phoneNum);
        et_email_info.setText(email);
        loginout = view.findViewById(R.id.loginout);
        loginout.setOnClickListener(this);
        imageCircleView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginout:
                loginOut();
                break;
            case R.id.civ_info:
                showPop();
                break;
            case R.id.btn_pick_photo://从相册选择图片
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        MeFragment.this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, NEED_PIC_PHOTO);
                    } else {
                        getPictureFromPhoto();
                    }
                } else {
                    getPictureFromPhoto();
                }
                break;
            case R.id.btn_take_photo://拍照
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        MeFragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA}, NEED_CAMERA);
                    } else {
                        getPictureFormCamera();
                    }
                } else {
                    getPictureFormCamera();
                }
                break;
            case R.id.btn_cancel:
                mPopupWindow.dismiss();
                return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            mPopupWindow.dismiss();
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == 111 && data != null) {
            String photoPath = data.getStringExtra("photoPath");
            photoAdr = photoPath;

            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            Log.e(TAG, "onActivityResult: photoAdr = " + photoAdr);
            //上传头像

            int userID = PreferenceUtil.getLOGINSTATE(getActivity());

            if (userID == -1) {
                return;
            }

            HashMap<String, Integer> params = new HashMap<>();
            params.put("uid", userID);

            File file = new File(photoAdr);

            HttpUtils.getInstance().doPostHeadPhoto(ConstantPool.UPLOAD_HEAD_PHOTO, params, file);

        } else {
            Bundle bundle = new Bundle();
            if (requestCode == CAMERA_WITH_DATA) {
                getHeadMode = 0;
                bundle.putInt("photoMode", getHeadMode);
                bundle.putString("photoPath", tempPhotoPath);
            } else if (requestCode == PHOTO_PICKED_WITH_DATA) {
                getHeadMode = 1;
                bundle.putInt("photoMode", getHeadMode);
                bundle.putString("photoUri", data.getData().toString());
            }
            Intent intent = new Intent(getActivity(), EditPictureActivity.class);
            intent.putExtra("photoHead", bundle);
            startActivityForResult(intent, 111);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case NEED_CAMERA:
                // 如果权限被拒绝，grantResults 为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPictureFormCamera();
                } else {
                    ToastUtil.show(MeFragment.this.getActivity(), R.string.permission_dined);
                }
                break;
            case NEED_PIC_PHOTO:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPictureFromPhoto();
                } else {
                    ToastUtil.show(MeFragment.this.getActivity(), R.string.permission_dined);
                }
                break;
        }
    }

    /* 从相册中获取照片 */
    private void getPictureFromPhoto() {
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openphotoIntent, PHOTO_PICKED_WITH_DATA);
    }

    /* 从相机中获取照片 */
    private void getPictureFormCamera() {
        Uri imageCaptureUri;

        File imagePath = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera");
        if (!imagePath.exists()) imagePath.mkdirs();
        mCurrentPhotoFile = new File(imagePath, getNewFileName() + ".jpg");
        tempPhotoPath = mCurrentPhotoFile.getPath();
        //第二参数是在manifest.xml定义 provider的authorities属性
        imageCaptureUri = FileProvider.getUriForFile(getActivity(), "com.administrator.seawindow.fileprovider", mCurrentPhotoFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip =
                    ClipData.newUri(getActivity().getContentResolver(), "A photo", imageCaptureUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    getActivity().getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getActivity().grantUriPermission(packageName, imageCaptureUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        startActivityForResult(intent, 1000);
    }

    private String getNewFileName(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());

        return formatter.format(curDate);
    }

    private void showPop() {
        if (popView == null) {
            popView = LayoutInflater.from(getActivity()).inflate(R.layout.changephoto_up_menu, null);
            addPictureFromPhotoBtn = popView.findViewById(R.id.btn_pick_photo);
            addPictureFromCameraBtn = popView.findViewById(R.id.btn_take_photo);
            cancle = popView.findViewById(R.id.btn_cancel);
            addPictureFromPhotoBtn.setOnClickListener(this);
            addPictureFromCameraBtn.setOnClickListener(this);
            cancle.setOnClickListener(this);
        }
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popup_window_anim);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }

    private void loginOut(){
        PreferenceUtil.loginOut(getActivity());
        OpenActivityUtil.openActivity(getActivity(), LoginActivity.class);
        getActivity().finish();
    }
}
