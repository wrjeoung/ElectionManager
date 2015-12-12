package memo.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.activity.R;
import com.jsloves.election.application.ElectionManagerApp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import memo.multipart.MultipartResponse;
import memo.net.BoardListBody;
import memo.net.MemoAddApi;
import memo.net.MemoUpdateApi;
import memo.utils.Utility;
import support.BaseActivity;
import support.util.Builder;
import support.util.NToast;
import support.widget.NImageView;

/*
import com.base.toolcom.nam.mybaseproject.multipart.MultipartResponse;
import com.base.toolcom.nam.mybaseproject.net.MemoAddApi;
import com.base.toolcom.nam.mybaseproject.net.MemoUpdateApi;
*/

/**
 * Created by Nam on 15. 7. 22..
 */
public class NDialogActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    public static String INPUT_TYPE_MEMO = "input_meno";
    public static String INPUT_TYPE_MEMO_UPDATE = "input_memo_update";

    public static String PARAM_INPUT_TYPE = "param_input_type";
    public static String PARAM_PHONE_NUMBER = "param_phone_number";
    public static String PARAM_ADM_CD = "param_adm_cd";

    public final static int REQ_CODE_PICK_GALLERY = 0;
    public final static int REQ_CODE_TAKE_PHOTO = 300;

    private LinearLayout mWrapperSp;
    private Spinner mSp0;
    private Spinner mSp1;
    private Spinner mSp2;
    private boolean ignoreUpdate;

    private Context mContext;
    private String mInputType;
    private String mAdmCd;

    private EditText mInputMsg;
    private EditText mInputTag;
    private TextView mInputDay;
    private TextView mInputTime;
    private TextView mShareName;

    private NImageView mImagePhoto;
    private ImageView mDeleteBtn;

    private String mMemoSeq;
    private BoardListBody.BoardDTO mMemoInfo;
    private String imageUrl;
    private String oldImageUrl;

//    private InputImageAdapter mImageAdapter;
//    private int mEditPos;
    private String mTakePhotoPath;

    private Map<Integer, String> mFilePathMap;

    @Override
    public int onGetContentViewResource() {
        mActivityType = ACTIVITY_DIALOG;
        return R.layout.activity_input_dialog;
    }

    @Override
    public void onInit() {

        mContext = this;
        ignoreUpdate = false;

        getWindow().getAttributes().width = mResolutionutils.convertWidth(680);
        getWindow().getAttributes().height = mResolutionutils.convertHeight(720);

        mFilePathMap = new HashMap<>();

        handleIntent(getIntent());

        if (!TextUtils.isEmpty(mInputType)) {
            setTitle(getString(R.string.input_title_memo));
        }

        buildComponents();

        if (mInputType.equals(INPUT_TYPE_MEMO_UPDATE)) {
            initMemoInfo();
        }
        initSpinner(mAdmCd);
    }

    private void initSpinner(String admCd) {
        ignoreUpdate = true;

        String haengCode = admCd.split("-")[0];
        String sigunguCode = admCd.substring(0,5);

        JSONObject joCode1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsCodeObject();
        JSONObject joCode2 = (JSONObject)joCode1.get("HAENGJOUNGDONG");
        JSONObject joCode3 = (JSONObject)joCode1.get("TUPYOGU");

        JSONObject joText1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject();
        JSONObject joText2 = (JSONObject)joText1.get("HAENGJOUNGDONG");
        JSONObject joText3 = (JSONObject)joText1.get("TUPYOGU");

        int sigunguIndex = ElectionManagerApp.getIndex((JSONArray) joCode1.get("SIGUNGU"), sigunguCode);
        int haengIndex = ElectionManagerApp.getIndex((JSONArray) joCode2.get(sigunguCode), haengCode);
        int tupyoguIndex = ElectionManagerApp.getIndex((JSONArray) joCode3.get(haengCode), admCd);

        String sigunguText = (String)((JSONArray)joText1.get("SIGUNGU")).get(sigunguIndex);
        String haengText = (String)((JSONArray)(joText2.get(sigunguText))).get(haengIndex);

        setUpSpinner(mSp0, joText1.get("SIGUNGU").toString());
        mSp0.setSelection(sigunguIndex);
        setUpSpinner(mSp1, joText2.get(sigunguText).toString());
        mSp1.setSelection(haengIndex);
        setUpSpinner(mSp2, joText3.get(haengText).toString());
        mSp2.setSelection(tupyoguIndex);
    }

    private void setUpSpinner(Spinner spinner, String items) {
        Type type = new TypeToken<List<String>>() {
        }.getType();
        Gson converter = new Gson();
        List<String> list = converter.fromJson(items, type);
        ArrayAdapter sp_Adapter = new ArrayAdapter(getApplicationContext(), R.layout.row_spinner_item, list);
        spinner.setAdapter(sp_Adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void handleIntent(Intent getIntent) {
        if (getIntent != null) {
            mInputType = getIntent.getStringExtra(PARAM_INPUT_TYPE);
            mAdmCd = getIntent.getStringExtra(PARAM_ADM_CD);
            mMemoInfo = (BoardListBody.BoardDTO) getIntent.getSerializableExtra("MEMOINFO");

            if (TextUtils.isEmpty(mInputType) || TextUtils.isEmpty(mAdmCd))
                ;//errorNoti(this, getString(R.string.error_param), true);
        }
    }

    private void initMemoInfo() {
        mWrapperSp.setVisibility(View.GONE);
        mInputMsg.setText(mMemoInfo.contents);
        mInputTag.setText(mMemoInfo.tag);

        mInputMsg.setSelection(mMemoInfo.contents.length());
        mInputTag.setSelection(mMemoInfo.tag.length());

        boolean imageYn = (mMemoInfo.imgYn.equals("Y")) ? true : false;

        if(imageYn) {
           imageUrl = mMemoInfo.imgShow;
            oldImageUrl = imageUrl;
            mImagePhoto.setImageUrl(imageUrl);
            mDeleteBtn.setVisibility(View.VISIBLE);
            Log.e("nam", "imageUrl : " + imageUrl);

        } else {
            imageUrl = "";
        }
    }

    private void buildComponents() {
        mWrapperSp = (LinearLayout) findViewById(R.id.wrapper_sp);
        mSp0 = (Spinner) findViewById(R.id.sp0);
        mSp1 = (Spinner) findViewById(R.id.sp1);
        mSp2 = (Spinner) findViewById(R.id.sp2);

        mInputMsg = (EditText) findViewById(R.id.edt_msg);
        mInputTag = (EditText) findViewById(R.id.edt_tag);
        mImagePhoto = (NImageView) findViewById(R.id.img_photo);
        mDeleteBtn = (ImageView) findViewById(R.id.delete_icon);

        mImagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NToast.show(mContext, "사진추가", NToast.SHORT);

                new Builder(mContext)
                        .setNegativeButton("사진찍기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getTakeImage();
                            }
                        })
                        .setPositiveButton("가져오기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getPhotoImage();
                            }
                        }).show();

            }
        });

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Builder(mContext)
                        .setIcon(Builder.ICON_INFO)
                        .setMessage(getString(R.string.remove_photo))
                        .setNegativeButton(getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mFilePathMap.remove(0);
                                oldImageUrl = "";
                                mDeleteBtn.setVisibility(View.GONE);
                                mImagePhoto.setImageResource(R.drawable.btn_add_nor);
                            }
                        }).show();
            }
        });

        Button btnOK = (Button) findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(mInputType.equals(INPUT_TYPE_MEMO) && (mSp1.getSelectedItemPosition() == 0 || mSp2.getSelectedItemPosition() == 0)) {
                    errorNoti(mContext, getString(R.string.not_selected_tupyogu), false);
                    return;
                }
                */
                String msg = mInputMsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    errorNoti(mContext, getString(R.string.input_msg_empty), false);
                    return;
                }

                if (mInputType.equals(INPUT_TYPE_MEMO)){
//                    NToast.show(mContext, new SimpleDateFormat("yyyy/MM/dd HH:mm").format(getInputTime()), NToast.SHORT);

                    String contents = mInputMsg.getText().toString();
                    String tag = mInputTag.getText().toString();
                    ArrayList<String> attachFileList = null;
                    String attachmentPath = "";
                    if(mFilePathMap.size() != 0) {
                        attachFileList = new ArrayList<String>(mFilePathMap.values());
                        attachmentPath = mFilePathMap.get(0);
                    }

                    showLoadingDlg(false);

                    String[] array = {(String)mSp0.getSelectedItem(), (String)mSp1.getSelectedItem(), (String)mSp2.getSelectedItem()};
                    final String adm_cd = ElectionManagerApp.getInstance().getTupyoguCode(array);
                    mAdmCd = adm_cd;

                    MemoAddApi api = new MemoAddApi(mContext, contents, adm_cd ,tag, attachmentPath,  new MemoAddApi.InsertMemoListener() {
                        @Override
                        public void onReceiver(MultipartResponse response) {
                            hideLoadingDlg();
                            finish();
                        }
                    });

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        api.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        api.execute();
                    }

                } else if (mInputType.equals(INPUT_TYPE_MEMO_UPDATE)) {
                    //메모 수정
                    String contents = mInputMsg.getText().toString();
                    String tag = mInputTag.getText().toString();
                    ArrayList<String> attachFileList = null;
                    String attachmentPath = "";
                    if(mFilePathMap.size() != 0) {
                        attachFileList = new ArrayList<String>(mFilePathMap.values());
                        attachmentPath = mFilePathMap.get(0);
                    }

                    String memoSeq = mMemoInfo.memoSeq;
                    String admCd = mMemoInfo.admCd;


                    if(!imageUrl.isEmpty() && oldImageUrl.isEmpty() && attachmentPath.isEmpty()) {
                        imageUrl = "";
                    } else if(!imageUrl.isEmpty()){
                        imageUrl = imageUrl.split("/memo_upload/")[1];
                    } else {
                        imageUrl = "";
                    }


                    MemoUpdateApi api = new MemoUpdateApi(mContext, memoSeq, admCd, contents, tag, attachmentPath, imageUrl, new MemoUpdateApi.InsertMemoListener() {
                        @Override
                        public void onReceiver(MultipartResponse response) {
                            finish();
                        }
                    });

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        api.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        api.execute();
                    }


                }
            }
        });

        LinearLayout layoutTime = (LinearLayout) findViewById(R.id.layout_time);

        // 현재시간 표시
        if (!TextUtils.isEmpty(mInputType)) {
            layoutTime.setVisibility(View.VISIBLE);

            final long currentTime = System.currentTimeMillis();
            final Date currentDate = new Date(currentTime);

            String toDay = new SimpleDateFormat("yyyy년 MM월 dd일").format(currentDate);
            String toTime = new SimpleDateFormat("HH시 mm분").format(currentDate);

            mInputDay = (TextView) findViewById(R.id.txt_day);
            mInputDay.setText(toDay);

            mInputTime = (TextView) findViewById(R.id.txt_time);
            mInputTime.setText(toTime);

        }

        buildRecyclerView();
    }

    private void buildRecyclerView() {
        if(mInputType.equals(INPUT_TYPE_MEMO_UPDATE)) {
            findViewById(R.id.layout_share).setVisibility(View.GONE);
        }

        mShareName = (TextView) findViewById(R.id.tv_name);
    }

    @Override
    public void finish() {

        Intent intent = new Intent();
//        intent.putExtra("key", value);
        setResult(RESULT_OK, intent);

        super.finish();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String toDay = String.format("%d년 %d월 %d일", year,monthOfYear+1, dayOfMonth);
            mInputDay.setText(toDay);
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String toTime = String.format("%d시 %d분", hourOfDay, minute);
            mInputTime.setText(toTime);
        }
    };

    private long getInputTime(){
        if(mInputDay == null || mInputTime == null)
            return 0;

        if(TextUtils.isEmpty(mInputDay.getText()) || TextUtils.isEmpty(mInputTime.getText()))
            return 0;

        String inputStrDate = mInputDay.getText().toString() + mInputTime.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일HH시 mm분");

        try {
            Date inputDate = sdf.parse(inputStrDate);

            return inputDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void getPhotoImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_PICK_GALLERY);
    }

    private void getTakeImage(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(Utility.isIntentAvailable(mContext, MediaStore.ACTION_IMAGE_CAPTURE)){
            String dir = Utility.getDCIMDir(mContext);
            String fileName = Utility.getCurrentTimeFileName() + ".jpg";
            mTakePhotoPath = dir + "/" + fileName;

            File file = new File(dir, fileName);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

            startActivityForResult(takePictureIntent, REQ_CODE_TAKE_PHOTO);
        } else {
            errorNoti(mContext, getString(R.string.error_no_camera), false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQ_CODE_PICK_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri selectedImage = intent.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        mImagePhoto.setImagePath(picturePath);
                        mDeleteBtn.setVisibility(View.VISIBLE);

                        mFilePathMap.clear();
                        oldImageUrl = "";
                        mFilePathMap.put(0, picturePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQ_CODE_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
//                    Bitmap profileBitmap = (Bitmap)intent.getExtras().get(MediaStore.EXTRA_OUTPUT);

                    File takeFile = new File(mTakePhotoPath);
                    if(takeFile.exists()){
                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(takeFile)));

                        mImagePhoto.setImagePath(mTakePhotoPath);
                        mDeleteBtn.setVisibility(View.VISIBLE);

                        oldImageUrl = "";
                        mFilePathMap.clear();
                        mFilePathMap.put(0, mTakePhotoPath);
                    }
                }
                break;

        }
    }

    public static void callActivityForResult(Activity context, String inputType, String admCd, BoardListBody.BoardDTO memoInfo, int requestCode) {
        Intent intent = new Intent(context, NDialogActivity.class);
        intent.putExtra(PARAM_INPUT_TYPE, inputType);
        intent.putExtra(PARAM_ADM_CD, admCd);
        intent.putExtra("MEMOINFO", memoInfo);

        context.startActivityForResult(intent, requestCode);

        Log.e("nam", "admCd : " + admCd);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        final int viewId = parent.getId();
        // TODO Auto-generated method stub

        if (ignoreUpdate) {
            if(viewId != R.id.sp2) {
                return;
            }
            ignoreUpdate = false;
        }

        switch (viewId) {
            case R.id.sp0:
                String sigungu = (String) parent.getSelectedItem();
                JSONObject jo1 = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
                setUpSpinner(mSp1, jo1.get(sigungu).toString());
                break;
            case R.id.sp1:
                String haengjoungdong = (String) parent.getSelectedItem();
                JSONObject jo2 = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsObject().get("TUPYOGU");
                setUpSpinner(mSp2, jo2.get(haengjoungdong).toString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
