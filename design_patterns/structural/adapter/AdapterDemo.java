package week_3;

public class AdapterDemo {

    // Target Interface
    interface MediaPlayer {
        void play(String fileName);
    }

    // Existing MP3 Player (works fine)
    static class Mp3Player implements MediaPlayer {
        public void play(String fileName) {
            System.out.println("Playing MP3 file: " + fileName);
        }
    }

    // Incompatible VLC Player (third-party / legacy)
    static class VlcPlayer {
        void playVlcFile(String file) {
            System.out.println("Playing VLC file: " + file);
        }
    }

    // Adapter (THE HERO)
    static class VlcAdapter implements MediaPlayer {

        private VlcPlayer vlcPlayer;

        public VlcAdapter(VlcPlayer vlcPlayer) {
            this.vlcPlayer = vlcPlayer;
        }

        @Override
        public void play(String fileName) {
            vlcPlayer.playVlcFile(fileName);
        }
    }

    //  Client
    public static void main(String[] args) {

        MediaPlayer mp3Player = new Mp3Player();
        mp3Player.play("song.mp3");

        MediaPlayer vlcPlayer =
                new VlcAdapter(new VlcPlayer());
        vlcPlayer.play("movie.vlc");
    }
}