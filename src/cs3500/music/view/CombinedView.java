package cs3500.music.view;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.Sequencer;
import javax.swing.JButton;

import cs3500.music.controller.KeyboardListener;
import cs3500.music.controller.MouseInputListener;
import cs3500.music.model.Duration;
import cs3500.music.model.ModelOperations;
import cs3500.music.model.Note;
import cs3500.music.model.Octave;
import cs3500.music.model.Pitch;

// CREATED IN ASSIGNMENT 7:
/**
 * A combined view that holds in it a midi and visual view, using elements from both on command.
 * The user can toggle midi play by pressing the spacebar. While midi is not playing, users can
 * add notes by clicking the keyboard buttons.
 */
public class CombinedView implements ViewOperations, VisualOperations, MidiOperations {
  private MidiOperations midiView;
  private VisualOperations visualView;
  private ModelOperations model;
  private int currentBeat;
  private boolean pause;
  private boolean testing;

  // sequencer from midiview
  protected Sequencer sequencer;
  protected float sequencerTempo;

  // button to add a note
  protected JButton addNoteButton;

  CombinedView(ModelOperations model) {
    this.model = model;
    this.visualView = new VisualView(model);
    this.midiView = new MidiView(model);
    this.testing = false;
    this.currentBeat = 0;
    this.pause = false;
  }

  public CombinedView(ModelOperations model, boolean testing) {
    this.model = model;
    if (testing) {
      this.midiView = new MidiView(model, true);
    } else {
      this.midiView = new MidiView(model);
    }
    this.visualView = new VisualView(model);
    this.testing = testing;
    this.currentBeat = 0;
    this.pause = false;
  }

  @Override
  public void render() {
    visualView.render();
    this.beginPlaying();
  }

  private void beginPlaying() {
    Timer timer = new Timer();
    timer.schedule(new MyTimerTask(), 0, model.getTempo() / 1000);
  }

  @Override
  public void printStuff(String stuff) {
    System.out.println(stuff);
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
  public void toggleScroll() {

  }

  @Override
  public List<Note> getNotesAtCurrentBeat() {
    return visualView.getNotesAtCurrentBeat();
  }

  // UNNEEDED, NOT USING SEQUENCER
  @Override
  public void setSequencer(Sequencer sequencer) {
    Objects.requireNonNull(sequencer);
    midiView.setSequencer(sequencer);
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
}
