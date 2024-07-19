package ir.hourglassdev.muiscplayer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.hourglassdev.muiscplayer.R;

public class Music {
    private int id;
    private String name;
    private String artist;
    private int coverResId;
    private int artistResId;
    private int musicFileResId;

    public Music(String name, String artist, int coverResId, int artistResId, int musicFileResId) {
        this.name = name;
        this.artist = artist;
        this.coverResId = coverResId;
        this.artistResId = artistResId;
        this.musicFileResId = musicFileResId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getCoverResId() {
        return coverResId;
    }

    public void setCoverResId(int coverResId) {
        this.coverResId = coverResId;
    }

    public int getArtistResId() {
        return artistResId;
    }

    public void setArtistResId(int artistResId) {
        this.artistResId = artistResId;
    }

    public int getMusicFileResId() {
        return musicFileResId;
    }

    public void setMusicFileResId(int musicFileResId) {
        this.musicFileResId = musicFileResId;
    }

    public static List<Music> getList() {
        List<Music> musicList = new ArrayList<>();

        Music music1 = new Music("Chehel Gis", "Evan Band",
                R.drawable.music_1_cover, R.drawable.music_1_artist, R.raw.music_1);
        Music music2 = new Music("Tanha Tarin", "Reza Sadeghi",
                R.drawable.music_2_cover, R.drawable.music_2_artist, R.raw.music_2);
        Music music3 = new Music("Hich", "Reza Bahram",
                R.drawable.music_3_cover, R.drawable.music_3_artist, R.raw.music_3);

        musicList.add(music1);
        musicList.add(music2);
        musicList.add(music3);

        return musicList;
    }

    public static String convertMilToString(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;

        return String.format(Locale.US, "%02d:%02d", minute, second);
    }
}
