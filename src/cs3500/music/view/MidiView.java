package cs3500.music.view;

import java.util.List;
import java.util.Objects;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import cs3500.music.controller.KeyboardListener;
import cs3500.music.controller.MouseInputListener;
import cs3500.music.mocks.MockReciever;
import cs3500.music.mocks.MockSynthesizer;
import cs3500.music.model.ModelOperations;
import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Octave;
import cs3500.music.model.Pitch;

// ASSIGNMENT 7: changed how midi works (added a testing boolean so we can skip the delay,
// in a separate constructor)
/**
 * The view that renders as audio (via MIDI) playback.
 */
public class MidiView implements ViewOperations {
  ModelOperations model;
  private Synthesizer synth;
  private Sequencer sequencer;
  private Receiver receiver;
  private boolean testing;
  private int currentBeat;
  private boolean pause;


  //  /**
  //   * The constructor for the MidiView that does not use a model.
  //   */
  //  public MidiView() {
  //    try {
  //      this.synth = MidiSystem.getSynthesizer();
  //      this.receiver = synth.getReceiver();
  //      this.synth.open();
  //      this.sequencer = MidiSystem.getSequencer();
  //    } catch (MidiUnavailableException e) {
  //      e.printStackTrace();
  //    }
  //    this.model = null;
  //  }

  /**
   * The default constructor for the MidiView.
   */
  public MidiView(ModelOperations model) {
    try {
      this.synth = MidiSystem.getSynthesizer();
      this.receiver = synth.getReceiver();
      this.synth.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
    this.model = model;
    this.testing = false;
    this.currentBeat = 0;
    this.pause = true;
  }

  /**
   * Permanently ends playback.
   */
  public void endPlayback() {
    this.receiver.close();
  }

  /**
   * Renders the MidiView by playing through a piece of music (at the tempo specified in the model).
   */
  @Override
  public void render() {
    List<Note> listOfNotes = model.getNotes();

    System.out.println("play song now");

    for (Note note : listOfNotes) {
      try {
        this.playNote(note, model.getTempo());
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
      }
    }

    int lengthInBeats = model.numBeats();
    int tempo = model.getTempo();
    int lengthInMicroSeconds = lengthInBeats * tempo;

    try {
      Thread.sleep(lengthInMicroSeconds);
    } catch (InterruptedException e) {
      System.out.println("Interrupted Yo");
      e.printStackTrace();
    }
  }

  /**
   * Plays a Note as a MIDI thing turned into sound.
   * Relevant classes and methods from the javax.sound.midi library:
   * <ul>
   * <li>{@link MidiSystem#getSynthesizer()}</li>
   * <li>{@link Synthesizer}
   * <ul>
   * <li>{@link Synthesizer#open()}</li>
   * <li>{@link Synthesizer#getReceiver()}</li>
   * <li>{@link Synthesizer#getChannels()}</li>
   * </ul>
   * </li>
   * <li>{@link Receiver}
   * <ul>
   * <li>{@link Receiver#send(MidiMessage, long)}</li>
   * <li>{@link Receiver#close()}</li>
   * </ul>
   * </li>
   * <li>{@link MidiMessage}</li>
   * <li>{@link ShortMessage}</li>
   * <li>{@link MidiChannel}
   * <ul>
   * <li>{@link MidiChannel#getProgram()}</li>
   * <li>{@link MidiChannel#programChange(int)}</li>
   * </ul>
   * </li>
   * </ul>
   *
   * @param note  the given note to be played
   * @param tempo the tempo in microseconds
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI"> d
   * </a>
   */
  public void playNote(Note note, int tempo) throws InvalidMidiDataException {
    //    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 64);
    //    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, 0, 60, 64);
    //    this.receiver.send(start, -1);
    //    this.receiver.send(stop, this.synth.getMicrosecondPosition() + 200000);

    int intPitch = this.computeIntPitch(note.getPitch(), note.getOctave());

    // My shortmessage protocol is (all int) (statusByte, channel, pitch, volume)
    MidiMessage initiateNote = new ShortMessage(ShortMessage.NOTE_ON, note.getInstrument(),
            intPitch, note.getVolume());
    MidiMessage endNote = new ShortMessage(ShortMessage.NOTE_OFF, note.getInstrument(),
            intPitch, note.getVolume());


    // need to change the beat value the note stores to microseconds (uses tempo)
    // TODO change to get rid of errors
    int startTiming = this.convertBeatToMicrosecond(note.getDur().getStartBeat(), tempo);
    // TODO change this to not be an error (logic too?)
    int endTiming = (int) this.synth.getMicrosecondPosition() + tempo
            * (note.getDur().getBeatsHeld() - 1);
    this.receiver.send(initiateNote, startTiming);
    this.receiver.send(endNote, endTiming);
  }

  /**
   * Outputs the int that represents the microsecond of the given beat (very first one on the beat).
   *
   * @param startBeat the int representing the beat to be converted
   * @param tempo     the int representing the tempo (in microseconds per beat)
   * @return the int representing the microsecond where the beat starts
   */
  private int convertBeatToMicrosecond(int startBeat, int tempo) {
    return startBeat * tempo;
  }

  /**
   * Returns the toString of the receiver.
   *
   * @return a string representation of the current Midi view reciever object
   */
  public String receiverString() {
    return this.receiver.toString();
  }

  /**
   * Plays all the notes that start at the specified beat.
   *
   * @param beat the specified beat
   */
  public void playAllStartingAtBeat(int beat) {
    List<Note> startingAtBeat = model.getAllStartingAtBeat(beat);

    for (Note note : startingAtBeat) {
      try {
        playNote(note, model.getTempo());
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
      }
    }
  }

  /*
   * Shouldn't be useless in this view, but currently is.
   */
  @Override
  public void togglePlayback() {
    if (pause) {
      this.startMusic();
    } else {
      this.stopMusic();
    }
  }

  /**
   * Handles changing the music from unpaused to paused.
   * (if at last beat, simply stop playback, if before last, stop playback,
   * if before beginning, throw error, if after end, throw error.
   */
  private void stopMusic() {
  }

  /**
   * Handles when the music is paused and we wish it to be unpaused.
   * (If at last beat, restart, if before last beat, start at that beat.
   * If before beginning, throw error).
   */
  private void startMusic() {
    if (currentBeat == model.numBeats()) {
      this.jumpToBeginning();
      this.playAllStartingAtBeat(currentBeat);
    } else {
      if (currentBeat < 0) {
        throw new IllegalArgumentException("Current Beat Before 0, can't start playback");
      } else {
        this.playAllStartingAtBeat(currentBeat);
      }
    }
  }

  @Override
  public List<Note> getNotesAtCurrentBeat() {
    return null;
  }




  @Override
  public void addKeyListener(KeyboardListener kbd) {
    // useless in this view
  }

  @Override
  public void removeKeyListener(KeyboardListener kbd) {
    //useless in this view
  }

  @Override
  public void addMouseListener(MouseInputListener mil) {
    // useless in this view
  }

  @Override
  public void removeMouseListener(MouseInputListener mil) {
    // useless in this view
  }



  @Override
  public void moveRight() {
    // useless in this view
  }

  @Override
  public void moveLeft() {
    // useless in this view
  }

  @Override
  public void jumpToBeginning() {
    // DO NOTHING?
  }

  @Override
  public void jumpToEnd() {
    // DO NOTHING?
  }

  @Override
  public void addNoteAtBeat(Note note) {
    // DO NOTHING
  }

  @Override
  public void addNoteAtBeat() {
    //DO Nothing
  }

  @Override
  public void update() {
    // DO NOTHING
  }

  @Override
  public void updateRedLine() {
    // DO NOTHING
  }



  /**
   * Outputs the integer representation of a pitch (in the range [0 127]).
   * Where C4 is 60 and each half-step is one int.
   *
   * @param pitch  the Pitch
   * @param octave the Octave
   * @return the int representation to be passed to MIDI
   */
  private int computeIntPitch(Pitch pitch, Octave octave) {
    int pitchVal = pitch.getOrderVal() - 1;
    int octaveVal = octave.toInt() - 1;


    for (int i = 0; i < octaveVal; i++) {
      pitchVal += 12;
    }
    return pitchVal;
  }

  ///////////////////////////////////////////////////////////////////////////////////


  /**
   * Statically makes a builder for a CombinedView.
   * @return a Builder for this class
   */
  public static MidiView.Builder builder() {
    return new MidiView.Builder();
  }

  @Override
  public void setModel(ModelOperations model) {
    this.model = model;
  }

  /**
   * Sets the Synthesizer.
   * @param synthesizer the given synthesizer
   */
  public void setSynthesizer(Synthesizer synthesizer) {
    this.synth = synthesizer;
  }

  /**
   * Sets the Receiver.
   * @param receiver the given receiver
   */
  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  /**
   * Sets the view to have no delay.
   */
  public void hasNoDelay() {
    this.testing = true;
  }

  /*
  You must implement either a builder or convenience constructors for your MIDI view,
  so that by default the view uses the actual MIDI synthesizer, but for testing can be run
  with your mock instead. If you create a StringBuilder, and pass to the mock-synth,
  you can then read out the contents of the StringBuilder to confirm that you’ve
  played all the right notes.

  Hint: Remember that you are not testing whether Java’s Receiver, MidiDevice,
  etc. are working correctly: they do. You are testing whether your program is
  using them correctly to provide the correct inputs to these classes so that
  they may play them.
   */

  /**
   * The Buider for my MIDI view so that the view can use the actual MIDI synth for real but then
   * use my mocks instead.
   */
  public static final class Builder {
    private ModelOperations model;
    private Synthesizer buildSynth;
    private Receiver buildRec;
    private boolean delay;

    Builder() {
      delay = true;
    }

    /**
     * Build the stored midiView. Throws an exception if setModel is not called.
     * @return the built midiView
     */
    public MidiView build() {
      if (model == null) {
        throw new IllegalArgumentException("Failed to pass in a model, cannot build MidiView");
      }

      MidiView midi = new MidiView(model);

      if (buildSynth != null) {
        midi.setSynthesizer(buildSynth);
      }

      if (buildRec != null) {
        midi.setReceiver(buildRec);
      }

      if (!delay) {
        midi.hasNoDelay();
      }

      return midi;
    }

    /**
     * Sets the model for the builder.
     * @param model the given ModelOperation
     * @return the Builder
     */
    public Builder setModel(ModelOperations model) {
      this.model = model;
      return this;
    }

    /**
     * Build using a Synth.
     * @return the Builder
     */
    public Builder setSynth(Synthesizer synth) {
      buildSynth = synth;
      return this;
    }

    /**
     * Build using a Receiver.
     * @param receiver the given receiver
     * @return the Builder
     */
    public Builder setReceiver(Receiver receiver) {
      this.buildRec = receiver;
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
