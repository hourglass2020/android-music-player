package ir.hourglassdev.muiscplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import ir.hourglassdev.muiscplayer.models.Music;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<Music> musicList;
    private int playingMusicPosition = -1;

    private onMusicClickListener listener;

    public MusicAdapter(List<Music> musicList, onMusicClickListener listener) {
        this.musicList = musicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bind(musicList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        private TextView tvArtist, tvName;
        private SimpleDraweeView ivCover;
        private LottieAnimationView animationView;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);

            tvArtist = itemView.findViewById(R.id.tv_music_artist);
            tvName = itemView.findViewById(R.id.tv_music_name);
            ivCover = itemView.findViewById(R.id.iv_music);
            animationView = itemView.findViewById(R.id.animationView);
        }

        public void bind(Music music) {
            ivCover.setActualImageResource(music.getCoverResId());
            tvArtist.setText(music.getArtist());
            tvName.setText(music.getName());

            if(getAdapterPosition() == playingMusicPosition){
                animationView.setVisibility(View.VISIBLE);
            }else{
                animationView.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(view -> {
                listener.onClick(music, getAdapterPosition());
            });
        }
    }

    public void notifyMusicChange(Music music) {
        int index = musicList.indexOf(music);
        if (index != -1) {
            if(index != playingMusicPosition){
                notifyItemChanged(playingMusicPosition);
                playingMusicPosition = index;
                notifyItemChanged(playingMusicPosition);
            }
        }
    }

    public interface onMusicClickListener{
        void onClick(Music music, int position);
    }
}
