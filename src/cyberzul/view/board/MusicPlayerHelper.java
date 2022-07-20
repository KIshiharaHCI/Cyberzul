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

/** Class for controlling of playing game music. */
public class MusicPlayerHelper {

  private final Clip backgroundClip;
  private final Clip tilePlacedClip;
  private final Clip illegalTurnClip;

  private final AudioInputStream audioInputStreamBackgroundMusic;
  private final AudioInputStream audioInputStreamTilePlacedMusic;
  private final AudioInputStream audioInputStreamIllegalTurnMusic;

  private boolean backgroundMusicOn;
  private boolean tilePlacedMusicOn;
  private boolean illegalTurnMusicOn;
  private boolean playMusicOn;

  private SwingWorker<Void, Void> worker;

  /** Controller for playing music in the game. */
  public MusicPlayerHelper() {
    String backgroundMusicPath = "audio/play-music.wav";
    audioInputStreamBackgroundMusic = createAudioInputStream(backgroundMusicPath);
    String tilePlacedMusicPath = "audio/placement-sound.wav";
    audioInputStreamTilePlacedMusic = createAudioInputStream(tilePlacedMusicPath);
    String illegalTurnMusicPath = "audio/illegal-turn-sound.wav";
    audioInputStreamIllegalTurnMusic = createAudioInputStream(illegalTurnMusicPath);
    backgroundClip = createClip(Objects.requireNonNull(audioInputStreamBackgroundMusic));
    tilePlacedClip = createClip(Objects.requireNonNull(audioInputStreamTilePlacedMusic));
    illegalTurnClip = createClip(Objects.requireNonNull(audioInputStreamIllegalTurnMusic));
    playMusicOn = true;
    backgroundMusicOn = false;
    tilePlacedMusicOn = false;
    illegalTurnMusicOn = false;
    initializeWorker();
  }

  private void initializeWorker() {
    worker =
        new SwingWorker<>() {
          @Override
          protected Void doInBackground() throws Exception {
            SwingUtilities.invokeAndWait(
                () -> {
                  if (!backgroundMusicOn) {
                    stopBackgroundMusic();
                  } else {
                    playBackgroundMusic();
                  }
                });
            Thread.sleep(5000);
            return null;
          }
        };
  }

  /**
   * Swich for music button to play or stop the music.
   *
   * @param stop the boolean that indicates whether the music should be stopped.
   */
  public void turnMusicOnOff(boolean stop) {
    if (stop) {
      playMusicOn = false;
      backgroundMusicOn = false;
      stopMusic(backgroundClip);
      stopMusic(tilePlacedClip);
      stopMusic(illegalTurnClip);
    } else {
      playMusicOn = true;
      backgroundMusicOn = true;
      startMusic(backgroundClip, Clip.LOOP_CONTINUOUSLY);
      startMusic(tilePlacedClip, 1);
      startMusic(illegalTurnClip, 1);
    }
  }

  /** Initiates the playing of the background music. */
  public void init() {
    backgroundMusicOn = true;
    handleMusicClip(
        backgroundMusicOn, backgroundClip, audioInputStreamBackgroundMusic, Clip.LOOP_CONTINUOUSLY);
  }

  public void playBackgroundMusic() {
    startMusic(backgroundClip, Clip.LOOP_CONTINUOUSLY);
  }

  /**
   * Plays the music for placing the {@link SourceTile} on the {@link PatternLines} or {@link
   * FloorLinePanel}.
   */
  public void playTilePlacedMusic() {
    if (!tilePlacedMusicOn) {
      tilePlacedMusicOn = true;
      handleMusicClip(tilePlacedMusicOn, tilePlacedClip, audioInputStreamTilePlacedMusic, 0);
      return;
    }
    stopBackgroundWithWorker();
    startMusic(tilePlacedClip, 1);
    startBackgroundWithWorker();
  }

  /**
   * Plays the music for illegal placing the {@link SourceTile} on the {@link PatternLines} or
   * {@link FloorLinePanel}.
   */
  public void playIllegalTurnMusic() {
    if (!illegalTurnMusicOn) {
      illegalTurnMusicOn = true;
      handleMusicClip(illegalTurnMusicOn, illegalTurnClip, audioInputStreamIllegalTurnMusic, 0);
      return;
    }
    stopBackgroundWithWorker();
    startMusic(illegalTurnClip, 1);
    startBackgroundWithWorker();
  }

  private void startBackgroundWithWorker() {
    backgroundMusicOn = true;
    worker.execute();
    initializeWorker();
  }

  private void stopBackgroundWithWorker() {
    stopBackgroundMusic();
    backgroundMusicOn = false;
    worker.execute();
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

  /** Close everything if the Frame closes on dispose. */
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

  public boolean isPlayMusicOn() {
    return playMusicOn;
  }
}
