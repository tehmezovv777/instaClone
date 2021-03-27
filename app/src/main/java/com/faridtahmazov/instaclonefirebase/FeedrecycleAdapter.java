package com.faridtahmazov.instaclonefirebase;

import android.media.session.MediaController;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FeedrecycleAdapter extends RecyclerView.Adapter<FeedrecycleAdapter.PostHolder> {

    private ArrayList<String> userEmailList;
    private ArrayList<String> userCommentList;
    private ArrayList<String> userImageList;

    public FeedrecycleAdapter(ArrayList<String> userEmailList, ArrayList<String> userCommentList, ArrayList<String> userImageList) {
        this.userEmailList = userEmailList;
        this.userCommentList = userCommentList;
        this.userImageList = userImageList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycle_raw, parent,false);
        
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.emailText.setText(userEmailList.get(position));
        holder.commentText.setText(userCommentList.get(position));
        Picasso.get().load(userImageList.get(position)).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return userEmailList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView emailText, commentText;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.userImage);
            emailText = itemView.findViewById(R.id.userEmailText);
            commentText = itemView.findViewById(R.id.userCommentText);
        }
    }
}
