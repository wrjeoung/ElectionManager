package memo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jsloves.election.activity.R;
import com.jsloves.election.application.ElectionManagerApp;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import memo.activity.BoardActivity;
import memo.activity.PhotoViewDialogActivity;
import memo.net.BoardListBody;
import memo.recyclerview.RecyclerArrayAdapter;
import support.util.Builder;
import support.util.ResolutionUtils;
import support.widget.NImageView;

/**
 * Created by Nam on 15. 7. 19..
 */
public class TimeLineAdapter extends RecyclerArrayAdapter<BoardListBody.BoardDTO, RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public interface OnClickListener{
        void onClickListener(int pos);
    }

    private Context mContext;
    private ResolutionUtils mResolutionUrils;
    private OnClickListener mClickListener;
    private BoardActivity.MemoModify mModifyListener;

    public void setOnClickListener(OnClickListener listener){
        mClickListener = listener;
    }

    public TimeLineAdapter(Context context, BoardActivity.MemoModify modifyListener) {
        mContext = context;
        mResolutionUrils = new ResolutionUtils(context);
        mModifyListener = modifyListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_line, parent, false);

//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.height = mResolutionUrils.convertPixelHeight(400);
//        view.setLayoutParams(layoutParams);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//            TextView textView = (TextView) holder.itemView;
//            textView.setText(getItem(position));
        final ItemViewHolder viewHolder = (ItemViewHolder) holder;

        if(getItemCount() == 0)
            return ;

       final BoardListBody.BoardDTO memoData = getItem(position);

        if(!TextUtils.isEmpty(memoData.contents)) {
            viewHolder.mTvMemo.setVisibility(View.VISIBLE);
            viewHolder.mTvMemo.setText(memoData.contents);

            viewHolder.mImgTime.setVisibility(View.VISIBLE);
            viewHolder.mTvTime.setVisibility(View.VISIBLE);

            if(memoData.longDate != 0)
                viewHolder.mTvTime.setText(convertTimeHour(memoData.longDate));
            else{
                viewHolder.mImgTime.setVisibility(View.GONE);
                viewHolder.mTvTime.setVisibility(View.GONE);
            }
        }
        else {
            viewHolder.mImgTime.setVisibility(View.GONE);
            viewHolder.mTvTime.setVisibility(View.GONE);
            viewHolder.mTvMemo.setVisibility(View.GONE);
        }

        viewHolder.mTvTupyogu.setText(getAdmText(memoData.admCd));
        if(!TextUtils.isEmpty(memoData.imgShow)) {
            viewHolder.mImgPhoto.setVisibility(View.VISIBLE);
            Log.e("nam", memoData.imgShow);
            viewHolder.mImgPhoto.setImageUrl(memoData.imgShow);
            viewHolder.mImgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoViewDialogActivity.callActivity(mContext, memoData.imgShow);
                }
            });
        } else {
            viewHolder.mImgPhoto.setVisibility(View.GONE);
//            viewHolder.mImgPhoto.setImageResource(R.drawable.sample1);
        }

        viewHolder.mLayoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClickListener != null)
                    mClickListener.onClickListener(position);
            }
        });

        viewHolder.mMemoLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Builder(mContext)
                        .setNegativeButton("편집", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mModifyListener.modifyMemoItem(position);
                            }
                        })
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mModifyListener.deleteMemoItem(position);
                            }
                        }).show();
                return false;
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTime;
        TextView mTvMemo;
        TextView mTvTupyogu;
        NImageView mImgPhoto;
        View mImgTime;
        FrameLayout mLayoutTime;
        LinearLayout mMemoLayout;

        public ItemViewHolder(View view) {
            super(view);

            mImgTime = view.findViewById(R.id.img_time);
            mTvTime = (TextView) view.findViewById(R.id.tv_time);
            mTvTupyogu = (TextView) view.findViewById(R.id.tv_tupyogu);
            mTvMemo = (TextView) view.findViewById(R.id.tv_memo);
            mImgPhoto = (NImageView) view.findViewById(R.id.img_photo);
            mLayoutTime = (FrameLayout) view.findViewById(R.id.layout_time);
            mMemoLayout = (LinearLayout) view.findViewById(R.id.layout_memo);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public long getHeaderId(int position) {
        BoardListBody.BoardDTO memoData = getItem(position);
        if (memoData != null && memoData.longDate != 0) {
            String strDate = convertTimeDay(memoData.longDate);

            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
                Date date = sdf.parse(strDate);
                return date.getTime();
            } catch (java.text.ParseException e){

            }
        }

        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(convertTimeDay(getItem(position).longDate));
//        holder.itemView.setBackgroundColor(getRandomColor());
    }

    private String convertTimeDay(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        return sdf.format(time);
    }

    private String convertTimeHour(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH시 mm분");
        return sdf.format(time);
    }


    private String getAdmText(String admCd) {
        String haengCode = admCd.split("-")[0];
        String sigunguCode = admCd.substring(0,5);

        StringBuffer buffer = new StringBuffer();

        JSONObject joCode1 = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsCodeObject();
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
        String tupyoguText = (String)((JSONArray)(joText3.get(haengText))).get(tupyoguIndex);

        buffer.append(haengText);
        buffer.append("\n");
        buffer.append(tupyoguText);

        return buffer.toString();
    }
}

