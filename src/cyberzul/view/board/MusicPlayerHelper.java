package cyberzul.view.board;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/** Class for controlling of playing game music. */
public class MusicPlayerHelper {

  private final Clip backgroundClip;
  private final Clip tilePlacedClip;
  private final Clip illegalTurnClip;

  private final AudioInputStream audioInputStreamBackgroundMusic;
  private final AudioInputStream audioInputStreamTilePlacedMusic;
  private final AudioInputStream audioInputStreamIllegalTurnMusic;

  private boolean tilePlacedMusicOn;
  private boolean illegalTurnMusicOn;
  private boolean playMusicOn;
  private final FloatControl volumeBackground;
  private final FloatControl volumeTilePlaced;
  private final FloatControl volumeIllegalTurn;

  /** Controller for playing music in the game. */
  public MusicPlayerHelper() {
    String backgroundMusicPath = "audio/play-music.wav";
    audioInputStreamBackgroundMusic = createAudioInputStream(backgroundMusicPath);
    backgroundClip = createClip(Objects.requireNonNull(audioInputStreamBackgroundMusic));
    setTileMusicProps(Objects.requireNonNull(backgroundClip), audioInputStreamBackgroundMusic);
    volumeBackground = createVolume(Objects.requireNonNull(backgroundClip));
    volumeBackground.setValue(-17);


    String tilePlacedMusicPath = "audio/placement-sound.wav";
    audioInputStreamTilePlacedMusic = createAudioInputStream(tilePlacedMusicPath);
    tilePlacedClip = createClip(Objects.requireNonNull(audioInputStreamTilePlacedMusic));
    tilePlacedMusicOn = false;
    setTileMusicProps(Objects.requireNonNull(tilePlacedClip), audioInputStreamTilePlacedMusic);
    volumeTilePlaced = createVolume(Objects.requireNonNull(tilePlacedClip));
    volumeTilePlaced.setValue(-6);


    String illegalTurnMusicPath = "audio/illegal-turn-sound.wav";
    audioInputStreamIllegalTurnMusic = createAudioInputStream(illegalTurnMusicPath);
    illegalTurnClip = createClip(Objects.requireNonNull(audioInputStreamIllegalTurnMusic));
    illegalTurnMusicOn = false;
    setTileMusicProps(Objects.requireNonNull(illegalTurnClip), audioInputStreamIllegalTurnMusic);
    volumeIllegalTurn = createVolume(Objects.requireNonNull(illegalTurnClip));
    volumeIllegalTurn.setValue(-6);

    playMusicOn = true;
  }

  /** Initiates the playing of the background music. */
  public void init() {
    tilePlacedMusicOn = true;
    illegalTurnMusicOn = true;
    startMusic(backgroundClip, true);
    loop(backgroundClip);
  }

  /**
   * Swich for music button to play or stop the music.
   *
   * @param stop the boolean that indicates whether the music should be stopped.
   */
  public void turnMusicOnOff(boolean stop) {
    if (stop) {
      playMusicOn = false;
      tilePlacedMusicOn = false;
      illegalTurnMusicOn = false;
      stopMusic(backgroundClip);
      stopMusic(tilePlacedClip);
      stopMusic(illegalTurnClip);
    } else {
      playMusicOn = true;
      tilePlacedMusicOn = true;
      illegalTurnMusicOn = true;
      startMusic(backgroundClip, true);
      loop(backgroundClip);
    }
  }

  /**
   * Plays the music for placing the {@link SourceTile} on the {@link PatternLines} or {@link
   * FloorLinePanel}.
   */
  public void playTilePlacedMusic() {
    tilePlacedClip.setFramePosition(0);
    startMusic(tilePlacedClip, tilePlacedMusicOn);
  }

  /**
   * Plays the music for illegal placing the {@link SourceTile} on the {@link PatternLines} or
   * {@link FloorLinePanel}.
   */
  public void playIllegalTurnMusic() {
    illegalTurnClip.setFramePosition(0);
    startMusic(illegalTurnClip, illegalTurnMusicOn);
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

  private FloatControl createVolume(Clip clip) {
    return (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
  }

  private void setTileMusicProps(Clip clip, AudioInputStream audioInputStream) {
    try {
      clip.open(audioInputStream);
    } catch (LineUnavailableException | IOException e) {
      e.printStackTrace();
    }
  }

  /** Close everything if the Frame closes on dispose. */
  public void closeAllOfMusicPlayer() {
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

  private void startMusic(Clip clip, boolean musicOn) {
    if (musicOn) {
      clip.start();
    }
  }

  private void loop(Clip clip) {
    clip.loop(Clip.LOOP_CONTINUOUSLY);
  }

  public boolean isPlayMusicOn() {
    return playMusicOn;
  }
  @SuppressFBWarnings("EI_EXPOSE_REP")
  public FloatControl getVolumeBackground() {
    return volumeBackground;
  }
  @SuppressFBWarnings("EI_EXPOSE_REP")
  public FloatControl getVolumeTilePlaced() {
    return volumeTilePlaced;
  }

  @SuppressFBWarnings("EI_EXPOSE_REP")
  public FloatControl getVolumeIllegalTurn() {
    return volumeIllegalTurn;
  }
}
