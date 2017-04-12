package cs3500.music.view;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;

import cs3500.music.controller.KeyboardListener;
import cs3500.music.controller.MouseInputListener;
import cs3500.music.model.Duration;
import cs3500.music.model.ModelOperations;
import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Octave;
import cs3500.music.model.Pitch;

// CREATED IN ASSIGNMENT 7:
/**
 * A combined view that holds in it a midi and visual view, using elements from both on command.
 * The user can toggle midi play by pressing the spacebar. While midi is not playing, users can
 * add notes by clicking the keyboard buttons.
 */
public class CombinedView implements ViewOperations {
  private ViewOperations midiView;
  private ViewOperations visualView;
  private ModelOperations model;
  private int currentBeat;
  private boolean pause;
  private boolean testing;
  Timer timer;


  //  // sequencer from midiview
  //  protected Sequencer sequencer;
  //  protected float sequencerTempo;

  // button to add a note
  protected JButton addNoteButton;

  CombinedView(ModelOperations model) {
    this.model = model;
    this.visualView = new VisualView(model);
    this.midiView = new MidiView(model);
    this.testing = false;
    this.currentBeat = 0;
    this.pause = true;
    this.timer = new Timer();
  }

  @Override
  public void render() {
    visualView.render();
    this.beginPlaying();
  }

  private void beginPlaying() {
    if (!pause) {
      this.timer.schedule(new MyTimerTask(), 0, model.getTempo() / 1000);
    }
  }

  @Override
  public void addKeyListener(KeyboardListener kbd) {
    visualView.addKeyListener(kbd);
  }

  @Override
  public void removeKeyListener(KeyboardListener kbd) {
    visualView.removeKeyListener(kbd);
  }

  @Override
  public void moveRight() {
    visualView.moveRight();
  }

  @Override
  public void moveLeft() {
    visualView.moveLeft();
  }

  @Override
  public void addMouseListener(MouseInputListener mil) {
    visualView.addMouseListener(mil);
  }

  @Override
  public void removeMouseListener(MouseInputListener mil) {
    visualView.removeMouseListener(mil);
  }

  @Override
  public void togglePlayback() {
    this.pause = !this.pause;
    this.beginPlaying();
  }

  @Override
  public void jumpToBeginning() {
    visualView.jumpToBeginning();
    this.currentBeat = 0;
  }

  @Override
  public void jumpToEnd() {
    visualView.jumpToEnd();
    this.currentBeat = model.numBeats() - 1;
  }

  @Override
  public void addNoteAtBeat(Note note) {
    // TODO these are currently dummy default pitches and octaves
    Octave octave = Octave.FOUR;
    Pitch pitch = Pitch.C$D;
    int startBeat = currentBeat;
    int beatsHeld = 1; //TODO
    Duration duration = new Duration(startBeat, beatsHeld);
    Note toAdd = new Note(pitch, octave, duration);
    if (this.model.getAllStartingAtBeat(currentBeat).contains(toAdd)) {
      System.out.println("ERROR, tried to ADD a duplicate note");
      //throw new IllegalArgumentException("There exists a note of the same type there you dummy");
    } else {
      this.model.addNote(note);
      this.visualView.update();
    }
  }

  @Override
  public void addNoteAtBeat() {
    this.addNoteAtBeat(new Note(Pitch.A, Octave.SEVEN, new Duration(currentBeat, 1)));
  }

  @Override
  public void updateRedLine() {

  }

  @Override
  public void update() {
    visualView.update();
  }

  @Override
  public List<Note> getNotesAtCurrentBeat() {
    return visualView.getNotesAtCurrentBeat();
  }

  @Override
  public void setModel(ModelOperations model) {
    this.model = model;
  }

  @Override
  public void playAllStartingAtBeat(int beat) {
    currentBeat = beat;
    this.beginPlaying();
  }

  public static CombinedView.Builder builder() {
    return new CombinedView.Builder();
  }

  /**
   * My custom TimerTask that specifies what to do when the Timer hits the specified time.
   * (ie play all the notes at the current beat).
   */
  class MyTimerTask extends TimerTask {
    @Override
    public void run() {
      if (!pause && (currentBeat < model.numBeats())) {
        //TODO Need some method that will update the placement of the red line (current beat)
        visualView.updateRedLine();
        midiView.playAllStartingAtBeat(currentBeat);
        currentBeat += 1;
      }
    }
  }


  /**
   * A Builder for my Combined View so that it can use mocks
   * and not have a built in delay for tests, and also pass in custom midi and visual views.
   */
  public static final class Builder {
    private ModelOperations buildModel;
    private Synthesizer buildSynth;
    private Receiver buildRec;
    private MidiView buildMidi;
    private VisualView buildVisual;
    private boolean delay;

    Builder() {
      delay = true;
    }

    /**
     * Builds the CombinedView. Throws an exception if the view is no null (no setSubViews call).
     * @return
     */
    public CombinedView build() {
      if (buildModel == null) {
        throw new IllegalArgumentException("Failed to pass in a model, cannot build CombinedView");
      }

      CombinedView combinedView = new CombinedView(buildModel);

      MidiView utilizedMidi;
      VisualView utilizedVisual;

      if (buildMidi == null) {
        utilizedMidi = (MidiView) combinedView.midiView;
      } else {
        utilizedMidi = buildMidi;
      }

      if (buildSynth != null) {
        utilizedMidi.setSynthesizer(buildSynth);
      }

      if (buildRec != null) {
        utilizedMidi.setReceiver(buildRec);
      }

      if (!delay) {
        utilizedMidi.hasNoDelay();
      }

      if (buildVisual == null) {
        utilizedVisual = (VisualView) combinedView.visualView;
      } else {
        utilizedVisual = buildVisual;
      }

      if (!utilizedMidi.model.equals(utilizedVisual.model)) {
        throw new IllegalArgumentException("model of midi and visual are not synced during build");
      }

      combinedView.midiView = utilizedMidi;
      combinedView.visualView = utilizedVisual;

      return combinedView;
    }

    /**
     * Sets the Receiver for the CombinedView within this Builder.
     * @param rec the given receiver to be set
     * @return the Builder
     */
    public Builder setReceiver(Receiver rec) {
      this.buildRec = rec;
      return this;
    }

    /**
     * Sets the synthesizer for the CombinedView within the Builder.
     * @param synth the given synth to be set as
     * @return the Builder
     */
    public Builder setSynthesizer(Synthesizer synth) {
      this.buildSynth = synth;
      return this;
    }

    /**
     * Sets the midi for the combinedView within the Builder.
     * @param midi the given midiView
     * @return the Builder
     */
    public Builder setMidi(MidiView midi) {
      this.buildMidi = midi;
      return this;
    }

    /**
     * Sets the visual for the combinedView within the Builder.
     * @param visual the given visualView
     * @return the Builder
     */
    public Builder setVisual(VisualView visual) {
      this.buildVisual = visual;
      return this;
    }

    /**
     * Build using noDelay (for testing purposes).
     * @return the Builder
     */
    public Builder noDelay() {
      this.delay = false;
      return this;
    }

    /**
     * Building using delay during playback specifically (so the music plays in real time).
     * @return the Builder
     */
    public Builder delay() {
      this.delay = true;
      return this;
    }
  }
}
