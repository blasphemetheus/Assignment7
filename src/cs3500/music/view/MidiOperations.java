package cs3500.music.view;

import javax.sound.midi.Sequencer;

import cs3500.music.model.ModelOperations;

/**
 * Public facing interface for midiviews.
 */
public interface MidiOperations extends ViewOperations {

  /**
   * Sets the sequencer to the given sequencer.
   * @param sequencer the given sequencer
   */
  void setSequencer(Sequencer sequencer);

  /**
   * Sets the viewModel to the given model.
   * @param model the given model
   */
  void setModel(ModelOperations model);

  /**
   * Plays all the notes that start at the specified beat.
   *
   * @param beat the specified beat
   */
  void playAllStartingAtBeat(int beat);

}
