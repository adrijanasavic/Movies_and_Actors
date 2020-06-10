package com.example.movies_and_actors.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.movies_and_actors.R;
import com.example.movies_and_actors.model_movie.Result;

import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    Context context;
    List<Result> youtubeVideoList;

    public VideoAdapter() {
    }

    public VideoAdapter(Context context,List<Result> youtubeVideoList) {
        this.context = context;
        this.youtubeVideoList = youtubeVideoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.video_view, parent, false);

        return new VideoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {

        holder.videoWeb.loadData(  "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" +  youtubeVideoList.get(position).getKey() + "\" frameborder=\"0\" allowfullscreen= width=\"100%\" height=\"100%\"></iframe>", "text/html" , "utf-8" );
        holder.videoWeb.setBackgroundColor(255);

//        holder.nazivVidea.setText(youtubeVideoList.get(position).getName());
//        holder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, VideoPlayer.class);
//                intent.putExtra("key",youtubeVideoList.get(position).getKey());
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return youtubeVideoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        WebView videoWeb;
//        TextView nazivVidea;
//        Button button;

        public VideoViewHolder(View itemView) {
            super(itemView);

            videoWeb = itemView.findViewById(R.id.videoWebView);
//            nazivVidea = itemView.findViewById(R.id.naziv_videa);
//            button = itemView.findViewById(R.id.button);

            videoWeb.getSettings().setJavaScriptEnabled(true);
//            videoWeb.setWebChromeClient(new WebChromeClient() {
//
//            } );
        }
    }
}