package memo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jsloves.election.DTO.ImageInfoDTO;
import com.jsloves.election.activity.R;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;

import memo.adapter.TimeLineAdapter;
import memo.net.BoardListBody;
import memo.net.DefaultBody;
import memo.net.MemoDeleteApi;
import memo.net.MemoListApi;
import memo.recyclerview.RecyclerViewLoadMore;
import support.BaseActivity;
import support.GpsManager;
import support.io.BaseRequest;
import support.io.NetworkError;
import support.io.model.Response;
import support.util.Builder;
import support.util.NToast;

/**
 * Created by juhyukkim on 2015. 11. 22..
 */
public class BoardActivity extends BaseActivity {
    public static int DEFAULT_LIST_COUNT = 20;
    public static int REQUEST_CODE_MEMO = 1010;
    public static int REQUEST_CODE_SMS = 1020;
    public static String PARAM_ADDRESS_SEQ = "address_seq";

    private Context mContext;
    private int mOffset = 0;
    private int mPage = 0;
    private boolean mIsLoadMore = true;

    private LinearLayout mLayoutTop;
    private RecyclerView mUserTimeLine;
    private TimeLineAdapter mTimeListAdapter;
    private LinearLayout mLayoutUserInfo;
    private Button mBtnExpand;
    private String mAdmCd;

    //private UserDefaultInfoFragment mUserDefaultInfoFragment;

    //private UserInfo mUserInfo;

    private boolean mIsExpandUserInfo = false;
    private GpsManager mGpsManager;

    public interface MemoModify {
        public void deleteMemoItem(int position);
        public void modifyMemoItem(int position);
    }

    @Override
    public int onGetContentViewResource() {
        return R.layout.activity_board;
    }

    @Override
    public void onInit() {
        mContext = this;

        buildComponents();

        //setFragment();

        //getUserInfo();
    }

    private void getMemoList(final String admCd) {
        showLoadingDlg(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MemoListApi api = new MemoListApi(mAdmCd, mOffset);
                requestApi(api, mMemoListCallback);
            }
        }, 100);
    }

    private void buildComponents() {
        mLayoutTop = (LinearLayout) findViewById(R.id.layout_top);
        mLayoutUserInfo = (LinearLayout) findViewById(R.id.layout_userinfo);
        mLayoutUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Builder(mContext).setIcon(R.drawable.icon_alert)
                        .setMessage("편집화면으로 이동하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                Intent modifyIntent = new Intent(mContext, AddCustomerActivity.class);
                                modifyIntent.putExtra(Configuration.INTENTPARAMS_MODIFY, true);
                                modifyIntent.putExtra(Configuration.INTENTPARAMS_ADDRESSINFO, mAddressSeq);
                                startActivity(modifyIntent);
                                */
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        ImageView imgWrite = (ImageView) findViewById(R.id.img_write);
        imgWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new Builder(mContext)
                        .setPositiveButton("메모작성", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NDialogActivity.callActivityForResult((Activity) mContext, NDialogActivity.INPUT_TYPE_MEMO, mAdmCd, null, REQUEST_CODE_MEMO);
                            }
                        }).show();
                 */
                NDialogActivity.callActivityForResult((Activity) mContext, NDialogActivity.INPUT_TYPE_MEMO, mAdmCd, null, REQUEST_CODE_MEMO);
            }
        });

        mBtnExpand = (Button) findViewById(R.id.btn_expand);
        mBtnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (mIsExpandUserInfo) {
                    showDefaultUserInfo();
                    mIsExpandUserInfo = false;
                } else {
                    showDetailUserInfo();
                    mIsExpandUserInfo = true;
                }
                */
            }
        });

        buildRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_MEMO) {
                mPage = 0;
                mOffset = 0;

                if (mTimeListAdapter != null)
                    mTimeListAdapter.clear();

                getMemoList(mAdmCd);
                mIsLoadMore = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    BaseRequest.OnRequestCallback<BoardListBody> mMemoListCallback = new BaseRequest.OnRequestCallback<BoardListBody>() {
        @Override
        public void onResult(Response response, BoardListBody responseBody) {
            hideLoadingDlg();
            Log.e("nam", "memo list request result");

            if (responseBody == null || responseBody.custMemoList == null) {
                mIsLoadMore = false;
                return;
            }

            mPage++;
            mOffset += responseBody.custMemoList.size();
            mTimeListAdapter.addAll(changeMemoData(responseBody.custMemoList));
        }

        @Override
        public void onError(NetworkError error) {
            hideLoadingDlg();
            Log.e("nam", "memo list request error");
            mIsLoadMore = false;
        }
    };

    BaseRequest.OnRequestCallback<DefaultBody> mMemoDeleteCallback = new BaseRequest.OnRequestCallback<DefaultBody>() {
        @Override
        public void onResult(Response response, DefaultBody responseBody) {
            hideLoadingDlg();
            Log.e("nam", "memo delete request result");

            if (responseBody == null) {
                return;
            }

            mPage = 0;
            mOffset = 0;

            if (mTimeListAdapter != null)
                mTimeListAdapter.clear();

            getMemoList(mAdmCd);
            mIsLoadMore = true;
        }

        @Override
        public void onError(NetworkError error) {
            hideLoadingDlg();
            Log.e("nam", "memo list request error");
            mIsLoadMore = false;
        }
    };

    private ArrayList<BoardListBody.BoardDTO> changeMemoData(ArrayList<BoardListBody.BoardDTO> oldData) {
        ArrayList<BoardListBody.BoardDTO> newData = new ArrayList<BoardListBody.BoardDTO>();

        for (BoardListBody.BoardDTO memo : oldData) {
            if (memo.imgFileList == null) {
                newData.add(memo);
            } else {
                if (memo.imgFileList.size() == 0) {
                    newData.add(memo);
                    continue;
                }

                int nImgCount = 0;
//                for (ImageInfo info : memo.imgFileList) {
                ImageInfoDTO info = memo.imgFileList.get(0);

                BoardListBody.BoardDTO newMemo = new BoardListBody.BoardDTO(memo);

                if (nImgCount != 0) {
                    newMemo.contents = "";
                }

                String imgUrl = "http://222.122.149.161:7070/img/memo/" + memo.imgFileList.get(nImgCount).imgUrl;
                newMemo.imgShow = imgUrl;
                newData.add(newMemo);
                nImgCount++;
//                }
            }
        }

        return newData;
    }

    private int getLayoutManagerOrientation(int activityOrientation) {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return LinearLayoutManager.VERTICAL;
        } else {
            return LinearLayoutManager.HORIZONTAL;
        }
    }

    private void buildRecyclerView() {
        mUserTimeLine = (RecyclerView) findViewById(R.id.recyclerview);

        mTimeListAdapter = new TimeLineAdapter(mContext, new MemoModify(){

            @Override
            public void deleteMemoItem(int position) {
                deleteItem(position);
            }

            @Override
            public void modifyMemoItem(int position) {
                editItem(position);
            }
        });

        mUserTimeLine.setAdapter(mTimeListAdapter);

        // Set layout manager
        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, orientation, false);
        mUserTimeLine.setLayoutManager(layoutManager);

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mTimeListAdapter);
        mUserTimeLine.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
//        mUserTimeLine.addItemDecoration(new DividerDecoration(this));

        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(mUserTimeLine, headersDecor);

        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        NToast.show(mContext, "header", NToast.SHORT);
                    }
                });

        mUserTimeLine.setOnScrollListener(new RecyclerViewLoadMore(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (mTimeListAdapter != null && mOffset >= 20 && mIsLoadMore)
                    getMemoList(mAdmCd);
            }
        });

        mUserTimeLine.addOnItemTouchListener(touchListener);
//        mUserTimeLine.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, final int position) {
//                if(view.getId() == R.id.img_photo)
//                    return ;
//
//                new Builder(mContext)
//                        .setNegativeButton("편집", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                editItem(position);
//                            }
//                        })
//                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                deleteItem(position);
//                            }
//                        }).show();
//
//            }
//        }));

        mTimeListAdapter.setOnClickListener(new TimeLineAdapter.OnClickListener() {
            @Override
            public void onClickListener(final int pos) {
                new Builder(mContext)
                        .setNegativeButton("편집", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                editItem(pos);
                            }
                        })
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteItem(pos);
                            }
                        }).show();
            }
        });

        mTimeListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
    }

    private void deleteItem(final int pos) {
        new Builder(mContext)
                .setIcon(R.drawable.icon_alert)
                .setMessage("선택한 메모를 삭제합니다.")
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String memoSeq = mTimeListAdapter.getItem(pos).memoSeq;
                        MemoDeleteApi api = new MemoDeleteApi(memoSeq);
                        requestApi(api, mMemoDeleteCallback);
                    }
                }).show();
    }

    private void editItem(int pos) {
        BoardListBody.BoardDTO memoInfo = (BoardListBody.BoardDTO)mTimeListAdapter.getItem(pos);
        NDialogActivity.callActivityForResult((Activity) mContext, NDialogActivity.INPUT_TYPE_MEMO_UPDATE, mAdmCd, memoInfo, REQUEST_CODE_MEMO);
    }

}
