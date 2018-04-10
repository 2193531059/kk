package com.administrator.seawindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.administrator.seawindow.utils.DialogUtil;
import com.administrator.seawindow.utils.FileUtils;
import com.administrator.seawindow.utils.FilterUtils;
import com.administrator.seawindow.utils.PhotoUtils;
import com.administrator.seawindow.utils.ToastUtil;
import com.administrator.seawindow.view.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class EditPictureActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView pictureShow;
    private String tempPhotoPath;
    private String photoPath;//相册图片路径
    private String camera_path;//图片编辑过后的存储路径
    /* 照相机拍照得到的图片 */
//    private File mCurrentPhotoFile;
    private LinearLayout content_layout;
    //    private LinearLayout bottom_linear;
    private RelativeLayout rl_edit_picture;//编辑图片的整个界面
    private RelativeLayout rl_crop;//剪切图片的整个界面
    private LinearLayout ll_filter;//滤镜的整个界面

    private int getHeadMode;
    private Uri photoUri;

    private Bitmap resizeBmp;//压缩过后的图
    private LinearLayout bottom_edit_linear;//图片编辑底部控件
    private RelativeLayout rl_croprelativelayout;//剪切布局
    private CropImageView mCropImageView;//剪切控件
    private ImageButton ib_cancel;//编辑取消按钮
    private ImageButton ib_crop;//剪切按钮
    private ImageButton ib_filter;//滤镜按钮
    private ImageButton finish_ok;//编辑完成按钮
    private LinearLayout ll_cropedit;//剪切面板的取消和完成控件
    private ImageButton cropCancel;//剪切取消按钮
    private ImageButton cropOk;//剪切完成按钮
    private ImageButton filter_cancel;//滤镜取消按钮
    private ImageButton filter_ok;//滤镜完成按钮
    private ImageView iv_filter;//滤镜显示图片控件
    private HorizontalScrollView filtersSV;//滤镜底部控件
    private RadioButton nofilter, filtermaoboli, filterfudiao, filternihong, filterLOMO, filterzise, filterfanhuang,
            filterbaoshilan, filterbaolilai, filterfanhong, filteryingguanglv;
    private Bitmap pictureBitmap = null;//显示在过滤界面的图片
    private Bitmap resultImg = null;//过滤完成时的图片
    private DialogUtil mDialogUtil;
    private Bitmap cropBitmap;//剪切控件现实的 图片
    private Bitmap cropfinishbit;//剪切完成时的图片
    private Bitmap hh;//剪切控件四周的四个小圆点

    public static final String filePath = Environment.getExternalStorageDirectory() + "/PictureTest/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getBundleExtra("photoHead");
        if (bundle != null) {
            getHeadMode = bundle.getInt("photoMode");
            photoUri = Uri.parse(bundle.getString("photoUri",""));
            tempPhotoPath = bundle.getString("photoPath","");
        }

        setContentView(R.layout.activity_edit_picture);
        pictureShow = findViewById(R.id.pictureShow);
        content_layout = findViewById(R.id.mainLayout);
        rl_croprelativelayout = findViewById(R.id.rl_croprelativelayout);
        mCropImageView = findViewById(R.id.cropmageView);
//        bottom_linear = findViewById(R.id.bottom_linear);
        rl_crop = findViewById(R.id.rl_crop);
        rl_edit_picture = findViewById(R.id.rl_edit_picture);
        bottom_edit_linear = findViewById(R.id.bottom_edit_linear);
        ib_cancel = findViewById(R.id.ib_cancel);
        ib_crop = findViewById(R.id.ib_jianqie);
        ib_filter = findViewById(R.id.bt_guolv);
        finish_ok = findViewById(R.id.bt_edit_finish);
        ll_cropedit = findViewById(R.id.ll_cropedit);
        cropCancel = findViewById(R.id.crop_cancel);
        cropOk = findViewById(R.id.crop_ok);
        ll_filter = findViewById(R.id.ll_filter);
        filter_cancel = findViewById(R.id.filter_cancel);
        filter_ok = findViewById(R.id.filter_ok);
        filtersSV = findViewById(R.id.filtersSV);
        nofilter = findViewById(R.id.nofilter);
        filtermaoboli = findViewById(R.id.filtermaoboli);
        filterfudiao = findViewById(R.id.filterfudiao);
        filternihong = findViewById(R.id.filternihong);
        filterLOMO = findViewById(R.id.filterLOMO);
        filterzise = findViewById(R.id.filterzise);
        filterfanhuang = findViewById(R.id.filterfanhuang);
        filterbaoshilan = findViewById(R.id.filterbaoshilan);
        filterbaolilai = findViewById(R.id.filterbaolilai);
        filterfanhong = findViewById(R.id.filterfanhong);
        filteryingguanglv = findViewById(R.id.filteryingguanglv);
        iv_filter = findViewById(R.id.iv_filter);
        mDialogUtil = new DialogUtil(this,R.string.picture_handling,true);

        setListener();
    }

    @SuppressLint("HandlerLeak")
    final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (content_layout.getWidth() != 0) {
                    // 取消定时器
                    timer.cancel();
                    compressed();
                }
            }
        }
    };

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };

    private void setListener(){
        ib_cancel.setOnClickListener(this);
        ib_filter.setOnClickListener(this);
        finish_ok.setOnClickListener(this);
        cropCancel.setOnClickListener(this);
        ib_crop.setOnClickListener(this);
        cropOk.setOnClickListener(this);
        filter_cancel.setOnClickListener(this);
        filter_ok.setOnClickListener(this);
        iv_filter.setOnClickListener(this);
        filtersSV.setOnClickListener(this);
        nofilter.setOnClickListener(this);
        filtermaoboli.setOnClickListener(this);
        filterfudiao.setOnClickListener(this);
        filternihong.setOnClickListener(this);
        filterLOMO.setOnClickListener(this);
        filterzise.setOnClickListener(this);
        filterfanhuang.setOnClickListener(this);
        filterbaoshilan.setOnClickListener(this);
        filterbaolilai.setOnClickListener(this);
        filterfanhong.setOnClickListener(this);
        filteryingguanglv.setOnClickListener(this);

        if(getHeadMode == 0){
            photoPath = tempPhotoPath;
            if (content_layout.getWidth() == 0) {
                timer.schedule(task, 10, 1000);
            } else {
                compressed();
            }
//                bottom_linear.setVisibility(View.GONE);
            rl_edit_picture.setVisibility(View.VISIBLE);
        }else if(getHeadMode == 1){
            Uri selectedImage = photoUri;
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            if(c != null){
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                photoPath = c.getString(columnIndex);
                c.close();
            }
            // 延迟每次延迟10 毫秒 隔1秒执行一次
            if (content_layout.getWidth() == 0) {
                timer.schedule(task, 10, 1000);
            } else {
                compressed();
            }
//                bottom_linear.setVisibility(View.GONE);
            rl_edit_picture.setVisibility(View.VISIBLE);
        }
    }

    private void compressed() {
        resizeBmp = PhotoUtils.getInstance(this).compressionFiller(photoPath,content_layout);
        if(resizeBmp == null){
            return;
        }
        pictureShow.setImageBitmap(resizeBmp);
        camera_path = SaveBitmap(resizeBmp, "saveTemp");
    }

    // 将生成的图片保存到内存中
    public String SaveBitmap(Bitmap bitmap, String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(filePath);
            if (!dir.exists())
                dir.mkdir();
            File file = new File(filePath + name + ".jpg");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                    out.flush();
                    out.close();
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_cancel:
                this.finish();
                break;
            //剪切
            case R.id.ib_jianqie:
                rl_edit_picture.setVisibility(View.GONE);
                rl_crop.setVisibility(View.VISIBLE);
                hh = BitmapFactory.decodeResource(this.getResources(), R.drawable.small_point);
                mCropImageView.setCropOverlayCornerBitmap(hh);
                cropBitmap = BitmapFactory.decodeFile(camera_path);
                mCropImageView.setImageBitmap(cropBitmap);
                break;

            case R.id.crop_cancel://剪切的取消按钮

                rl_edit_picture.setVisibility(View.VISIBLE);
                rl_crop.setVisibility(View.GONE);
                break;
            case R.id.crop_ok://剪切完成按钮
                cropfinishbit = mCropImageView.getCroppedImage();
                FileUtils.writeImage(cropfinishbit, camera_path, 100);//保存剪切后的图片

                rl_edit_picture.setVisibility(View.VISIBLE);
                rl_crop.setVisibility(View.GONE);
                pictureShow.setImageBitmap(cropfinishbit);
                break;
            //滤镜
            case R.id.bt_guolv:
                rl_edit_picture.setVisibility(View.GONE);
                ll_filter.setVisibility(View.VISIBLE);
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = 1;
                pictureBitmap = BitmapFactory.decodeFile(camera_path, option);
                iv_filter.setImageBitmap(pictureBitmap);
                break;

            case R.id.filter_cancel://取消滤镜按钮

                rl_edit_picture.setVisibility(View.VISIBLE);
                ll_filter.setVisibility(View.GONE);
                break;
            case R.id.filter_ok://滤镜完成按钮
                FileUtils.writeImage(resultImg, camera_path, 100);//保存过滤后的图片
                rl_edit_picture.setVisibility(View.VISIBLE);
                ll_filter.setVisibility(View.GONE);
                pictureShow.setImageBitmap(resultImg);
                break;

            //完成
            case R.id.bt_edit_finish:
                if (TextUtils.isEmpty(photoPath)) {
                    ToastUtil.show(EditPictureActivity.this, getString(R.string.editpicturefragment_chose_pic));
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("photoPath", camera_path);
                this.setResult(Activity.RESULT_OK, intent);
                this.finish();
                break;

            case R.id.nofilter:
                iv_filter.setImageBitmap(pictureBitmap);
                break;
            case R.id.filtermaoboli://毛玻璃
                mDialogUtil.setText(R.string.picture_handling);//显示加载对话框
                mDialogUtil.showDialog();
                resultImg = FilterUtils.Blur(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);

                mDialogUtil.dismissDialog();//隐藏对话框
                break;
            case R.id.filterfudiao://浮雕
                resultImg = FilterUtils.Carving(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);
                break;
            case R.id.filternihong://霓虹
                resultImg = FilterUtils.Neon(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);
                break;
            case R.id.filterLOMO://LOMO
                resultImg = FilterUtils.Lomo1(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);
                break;
            case R.id.filterzise://紫色
                resultImg = FilterUtils.Purple(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);
                break;
            case R.id.filterfanhuang://泛黄
                resultImg = FilterUtils.Yellow(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);
                break;
            case R.id.filterbaoshilan://宝石蓝
                resultImg = FilterUtils.Blue(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);
                break;
            case R.id.filterbaolilai://宝来利
                resultImg = FilterUtils.Polaroid(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);
                break;
            case R.id.filterfanhong://泛红
                resultImg = FilterUtils.Red(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);
                break;
            case R.id.filteryingguanglv://荧光绿
                resultImg = FilterUtils.Green(pictureBitmap);
                iv_filter.setImageBitmap(resultImg);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pictureBitmap != null) {
            pictureBitmap.recycle();
            pictureBitmap = null;
        }
        if (resultImg != null) {
            resultImg.recycle();
            resultImg = null;
        }
        if (resizeBmp != null) {
            resizeBmp.recycle();
            resizeBmp = null;
        }
        if (cropBitmap != null) {
            cropBitmap.recycle();
            cropBitmap = null;
        }
        if (cropfinishbit != null) {
            cropfinishbit.recycle();
            cropfinishbit = null;
        }
        if (hh != null) {
            hh.recycle();
            hh = null;
        }
    }
}
