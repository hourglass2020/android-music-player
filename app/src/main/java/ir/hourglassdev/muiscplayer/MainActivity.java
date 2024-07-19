package ir.hourglassdev.muiscplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.slider.Slider;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.hourglassdev.muiscplayer.databinding.ActivityMainBinding;
import ir.hourglassdev.muiscplayer.models.Music;

public class MainActivity extends AppCompatActivity implements MusicAdapter.onMusicClickListener {

    private ActivityMainBinding binding;
    private List<Music> musicList = Music.getList();

    private MediaPlayer mediaPlayer;
    private MusicState musicState = MusicState.STOPPED;

    private Timer timer;
    private boolean isDragging = false;
    private int cursor = 0;

    private MusicAdapter adapter;

    enum MusicState {
        PLAYING, PAUSED, STOPPED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new MusicAdapter(musicList, this);

        RecyclerView recyclerView = findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        onMusicChange(musicList.get(cursor));
    }

    private void onMusicChange(Music music) {
        adapter.notifyMusicChange(music);
        binding.musicSlider.setValue(0);
        mediaPlayer = MediaPlayer.create(this, music.getMusicFileResId());
        mediaPlayer.setOnPreparedListener(mediaPlayer1 -> {
            mediaPlayer.start();
            timer = new Timer();
            binding.durationTv.setText(Music.convertMilToString(mediaPlayer.getDuration()));
            musicState = MusicState.PLAYING;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        binding.positionTv.setText(Music.convertMilToString(mediaPlayer.getCurrentPosition()));
                        if (!isDragging) {
                            binding.musicSlider.setValue(mediaPlayer.getCurrentPosition());
                        }
                    });
                }
            }, 1000, 1000);

            binding.musicSlider.setValueTo(mediaPlayer.getDuration());
            binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.setOnCompletionListener(mediaPlayer2 -> {
                goNext();
            });
        });

        binding.ivMainCover.setActualImageResource(music.getCoverResId());
        binding.ivMainArtist.setActualImageResource(music.getArtistResId());
        binding.musicNameTv.setText(music.getName());
        binding.tvMainArtist.setText(music.getArtist());

        binding.playBtn.setOnClickListener(view -> {
            switch (musicState) {
                case PLAYING:
                    mediaPlayer.pause();
                    musicState = MusicState.PAUSED;
                    binding.playBtn.setImageResource(R.drawable.ic_play_32dp);
                    break;
                case PAUSED:
                case STOPPED:
                    mediaPlayer.start();
                    musicState = MusicState.PLAYING;
                    binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_24);
                    break;
            }
        });

        binding.musicSlider.addOnChangeListener((slider, value, fromUser) -> {
            binding.positionTv.setText(Music.convertMilToString((long) value));
        });

        binding.musicSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                isDragging = true;
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                isDragging = false;
                mediaPlayer.seekTo((int) slider.getValue());
            }
        });

        binding.nextBtn.setOnClickListener(view -> {
            goNext();
        });

        binding.previousBtn.setOnClickListener(view -> {
            goPrevious();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void goNext() {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();

        if (cursor < musicList.size() - 1) {
            cursor++;
        } else {
            cursor = 0;
        }

        onMusicChange(musicList.get(cursor));
    }

    private void goPrevious() {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();

        if (cursor == 0) {
            cursor = musicList.size() - 1;
        } else {
            cursor--;
        }

        onMusicChange(musicList.get(cursor));
    }

    @Override
    public void onClick(Music music, int position) {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
        cursor = position;
        onMusicChange(musicList.get(cursor));
    }
}