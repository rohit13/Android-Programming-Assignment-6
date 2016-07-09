package com.example.sharm_000.homework8;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by sharm_000 on 2/15/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    OnItemClickListener mItemClickListener;
    private List<Map<String, ?>> mData;
    private Context context;
    int lType;

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
        //public void onItemCheckBoxSelect(int position, boolean isChecked);
        public void onOverFlowButtonClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public RecyclerViewAdapter(Context myContext, List<Map<String, ?>> moviesData, int layoutType){
        mData=moviesData;
        context=myContext;
        lType = layoutType;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView;
        switch (viewType) {
            case 1:
                newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view, parent, false);
                ViewHolder vh = new ViewHolder(newView);
                return vh;
            case 2:
                newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view_1, parent, false);
                ViewHolderGrid vhg = new ViewHolderGrid(newView);
                return vhg;
            default:
                newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view, parent, false);
                ViewHolder vhDefault = new ViewHolder(newView);
                return vhDefault;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Map<String,?> movie = mData.get(position);
        if(holder instanceof ViewHolder)
            ((ViewHolder) holder).bindMovieData(movie);
        else if (holder instanceof ViewHolderGrid)
            ((ViewHolderGrid) holder).bindMovieData(movie);
    }

   /* @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }
*/
    @Override
    public int getItemViewType(int position) {
       /* Map<String,?> movie = mData.get(position);
        Double rating = (Double) movie.get("rating");
        Double threshold=8.0;
        if(rating>=threshold)
            return 1;
        else
            return 2;*/
        if(lType==1)
            return 1;
        else
            return 2;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView movieImage;
        public TextView movieName;
        public TextView movieDesc;
        //public CheckBox isSelected;
        public TextView rating;
        public ImageView overflow;
        //public RelativeLayout myRow;

        public ViewHolder(View itemView) {
            super(itemView);
            movieImage=(ImageView)itemView.findViewById(R.id.movieImage);
            movieName=(TextView)itemView.findViewById(R.id.movieName);
            movieDesc= (TextView) itemView.findViewById(R.id.movieDesc);
            //isSelected = (CheckBox) itemView.findViewById(R.id.chechBox);
            rating = (TextView) itemView.findViewById(R.id.rating);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);
            //myRow = (RelativeLayout) itemView.findViewById(R.id.myRow);

            /*isSelected.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(mItemClickListener!=null)
                        mItemClickListener.onItemCheckBoxSelect(getPosition(),isChecked);
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mItemClickListener!=null)
                        mItemClickListener.onItemClick(v,getPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    if(mItemClickListener!=null)
                        mItemClickListener.onItemLongClick(v,getPosition());
                    return true;
                }
            });

            if(overflow!=null){
                overflow.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(mItemClickListener!=null)
                            mItemClickListener.onOverFlowButtonClick(v,getPosition());
                    }
                });
            }
        }

        public void bindMovieData(Map<String,?> movie){
            if(movieName!=null)
                movieName.setText((String) movie.get("name"));
            if(movieImage!=null)
                movieImage.setImageBitmap((Bitmap) movie.get("image"));
            if(movieDesc!=null)
                movieDesc.setText((String) movie.get("description"));
            if(rating!=null)
                rating.setText( movie.get("rating")+"/10");
        }
    }

    public class ViewHolderGrid extends RecyclerView.ViewHolder{
        public ImageView movieImage;
        public TextView movieName;
        //public CheckBox isSelected;
        public TextView rating;
        public ImageView overflow;
        //public RelativeLayout myRow;

        public ViewHolderGrid(View itemView) {
            super(itemView);
            movieImage=(ImageView)itemView.findViewById(R.id.movieImage);
            movieName=(TextView)itemView.findViewById(R.id.movieName);
            //movieDesc= (TextView) itemView.findViewById(R.id.movieDesc);
            //isSelected = (CheckBox) itemView.findViewById(R.id.chechBox);
            rating = (TextView) itemView.findViewById(R.id.rating);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);
            //myRow = (RelativeLayout) itemView.findViewById(R.id.myRow);

            /*isSelected.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(mItemClickListener!=null)
                        mItemClickListener.onItemCheckBoxSelect(getPosition(),isChecked);
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mItemClickListener!=null)
                        mItemClickListener.onItemClick(v,getPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    if(mItemClickListener!=null)
                        mItemClickListener.onItemLongClick(v,getPosition());
                    return true;
                }
            });

            if(overflow!=null){
                overflow.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(mItemClickListener!=null)
                            mItemClickListener.onOverFlowButtonClick(v,getPosition());
                    }
                });
            }
        }

        public void bindMovieData(Map<String,?> movie){
            if(movieName!=null)
                movieName.setText((String) movie.get("name"));
            if(movieImage!=null)
                movieImage.setImageBitmap((Bitmap) movie.get("image"));
            //    movieImage.setImageResource((Integer) movie.get("image"));
            if(rating!=null)
                rating.setText( movie.get("rating")+"/10");
        }
    }
}

