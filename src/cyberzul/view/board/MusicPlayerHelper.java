package cyberzul.view.board;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class MusicPlayerHelper {
  private final String BACKGROUND_MUSIC_PATH = "audio/play-music.wav";
  private final String TILE_PLACED_MUSIC_PATH = "audio/placement-sound.wav";
  private final String TILE_ILLEGAL_PLACED_MUSIC_PATH = "audio/illegal-turn-sound.wav";
  private Clip backgroundClip;
  private Clip tilePlacedClip;
  private Clip illegalTurnClip;

  private AudioInputStream audioInputStreamBackgroundMusic;
  private AudioInputStream audioInputStreamTilePlacedMusic;
  private AudioInputStream audioInputStreamIllegalTurnMusic;

  private boolean backgroundMusicOn;
  private boolean tilePlacedMusicOn;
  private boolean illegalTurnMusicOn;
  private boolean playMusicOn;

  private SwingWorker<Void, Void> worker;

  public MusicPlayerHelper() {
    audioInputStreamBackgroundMusic = createAudioInputStream(BACKGROUND_MUSIC_PATH);
    audioInputStreamTilePlacedMusic = createAudioInputStream(TILE_PLACED_MUSIC_PATH);
    audioInputStreamIllegalTurnMusic = createAudioInputStream(TILE_ILLEGAL_PLACED_MUSIC_PATH);
    backgroundClip = createClip(audioInputStreamBackgroundMusic);
    tilePlacedClip = createClip(audioInputStreamTilePlacedMusic);
    illegalTurnClip = createClip(audioInputStreamIllegalTurnMusic);
    playMusicOn = false;
    backgroundMusicOn = false;
    tilePlacedMusicOn = false;
    illegalTurnMusicOn = false;
    initializeWorker();
  }

  private void initializeWorker() {
    worker =
        new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            SwingUtilities.invokeAndWait(
                new Runnable() {
                  @Override
                  public void run() {
                    if (!backgroundMusicOn) {
                      stopBackgroundMusic();
                    } else {
                      playBackgroundMusic();
                    }
                  }
                });
            Thread.sleep(5000);
            return null;
          }
        };
  }

  public void init() {
    backgroundMusicOn = true;
    handleMusicClip(
        backgroundMusicOn, backgroundClip, audioInputStreamBackgroundMusic, Clip.LOOP_CONTINUOUSLY);
  }

  public void playBackgroundMusic() {
    startMusic(backgroundClip, Clip.LOOP_CONTINUOUSLY);
  }

  public void playTilePlacedMusic() {
    if (!tilePlacedMusicOn) {
      tilePlacedMusicOn = true;
      handleMusicClip(tilePlacedMusicOn, tilePlacedClip, audioInputStreamTilePlacedMusic, 0);
      return;
    }
    stopBackgroundMusic();
    backgroundMusicOn = false;
    worker.execute();
    startMusic(tilePlacedClip, 1);
    backgroundMusicOn = true;
    worker.execute();
    initializeWorker();
  }

  public void playIllegalTurnMusic() {
    if (!illegalTurnMusicOn) {
      illegalTurnMusicOn = true;
      handleMusicClip(illegalTurnMusicOn, illegalTurnClip, audioInputStreamIllegalTurnMusic, 0);
      return;
    }
    stopBackgroundMusic();
    backgroundMusicOn = false;
    worker.execute();
    startMusic(illegalTurnClip, 1);
    backgroundMusicOn = true;
    worker.execute();
    initializeWorker();
  }

  public void stopBackgroundMusic() {
    stopMusic(backgroundClip);
  }

  private void handleMusicClip(
      boolean musicOn, Clip clip, AudioInputStream audioInputStream, int repeatLoop) {
    if (musicOn) {
      setTileMusicProps(clip, audioInputStream, repeatLoop);
    }
  }

  private AudioInputStream createAudioInputStream(String soundPath) {
    URL soundUrl = getClass().getClassLoader().getResource(soundPath);
    try {
      return AudioSystem.getAudioInputStream(Objects.requireNonNull(soundUrl));
    } catch (UnsupportedAudioFileException | IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Clip createClip(AudioInputStream audioInputStream) {
    try {
      AudioFormat format = audioInputStream.getFormat();
      DataLine.Info info = new DataLine.Info(Clip.class, format);
      return (Clip) AudioSystem.getLine(info);
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void setTileMusicProps(Clip clip, AudioInputStream audioInputStream, int repeatLoop) {
    try {
      clip.open(audioInputStream);
      clip.loop(repeatLoop);
      clip.start();
    } catch (LineUnavailableException | IOException e) {
      e.printStackTrace();
    }
  }

  public void closeAllOfMusicPlayer() {
    worker.cancel(true);
    backgroundClip.close();
    tilePlacedClip.close();
    illegalTurnClip.close();
    try {
      audioInputStreamBackgroundMusic.close();
      audioInputStreamTilePlacedMusic.close();
      audioInputStreamIllegalTurnMusic.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void stopMusic(Clip clip) {
    clip.stop();
  }

  private void startMusic(Clip clip, int loop) {
    clip.loop(loop);
    clip.start();
  }
}
