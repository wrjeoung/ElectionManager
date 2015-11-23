package com.jsloves.election.adapter;

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

import com.jsloves.election.DTO.BoardListBody;
import com.jsloves.election.activity.BoardActivity.MemoModify;
import com.jsloves.election.activity.PhotoViewDialogActivity;
import com.jsloves.election.activity.R;
import com.jsloves.election.recyclerview.RecyclerArrayAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private MemoModify mModifyListener;

    public void setOnClickListener(OnClickListener listener){
        mClickListener = listener;
    }

    public TimeLineAdapter(Context context, MemoModify modifyListener) {
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

        if(!TextUtils.isEmpty(memoData.userName))
            viewHolder.mTvWriter.setText(memoData.userName);

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
        TextView mTvWriter;
        NImageView mImgPhoto;
        View mImgTime;
        FrameLayout mLayoutTime;
        LinearLayout mMemoLayout;

        public ItemViewHolder(View view) {
            super(view);

            mImgTime = view.findViewById(R.id.img_time);
            mTvTime = (TextView) view.findViewById(R.id.tv_time);
            mTvWriter = (TextView) view.findViewById(R.id.tv_writer);
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
}
