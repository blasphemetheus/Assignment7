package cs3500.music.view;

import java.util.List;

import cs3500.music.controller.KeyboardListener;
import cs3500.music.controller.MouseInputListener;
import cs3500.music.model.ModelOperations;
import cs3500.music.model.Note;

/**
 * The operations that all of the views need to be able to handle.
 */
public interface ViewOperations {

  /**
   * Renders the view, whatever the specifics of that rendering may be. (For midi, starts playback,
   * for text, outputs String to console, for combined, renders gui and starts playback
   */
  void render();

  /**
   * The void method that can be called for all views. Does not have any effect for
   * midi and textual views. Otherwise moves the visible red arrow one to the right (if room).
   */
  void moveRight();

  /**
   * The void method that can be called for all views. Does not have any effect for
   * midi and textual views. Otherwise moves the visible red arrow one to the left (if possible).
   */
  void moveLeft();

  /**
   * Toggles playback for the view. (Has no effect on visual and textual views)
   */
  void togglePlayback();

  /**
   * Jumps to beginning of the representation stored in the view. Does not work during playback.
   */
  void jumpToBeginning();

  /**
   * Jumps to end of the representation stored in the view. Does not function during playback.
   */
  void jumpToEnd();

  /**
   * Attempts to add the specified note at the current beat.
   */
  void addNoteAtBeat(Note note);

  /**
   * Attempts to add a note at the current beat. Default note is 1 beat middle c.
   * A method for convenience rather than usefulness.
   */
  void addNoteAtBeat();

  /**
   * Updates the view to reflect current changes. (should only do anything in views with a gui).
   */
  void update();

  /**
   * Updates the view to reflect the current location of the red line.
   * Should only do anything in views with a gui.
   */
  void updateRedLine();

  /**
   * A method to add a Mouse Listener to the view.
   * Same method signature to add a MouseListener to Swing stuff.
   *
   * @param mil the listener to be added
   */
  void addMouseListener(MouseInputListener mil);

  /**
   * A method to add a Mouse Listener to the view.
   * Same method signature to add a MouseListener to Swing stuff.
   *
   * @param mil the listener to be added
   */
  void removeMouseListener(MouseInputListener mil);

  /**
   * A method to add a Keyboard Listener to the view.
   * Same method signature to add a KeyListener to Swing stuff.
   *
   * @param kbd the listener to be added
   */
  void addKeyListener(KeyboardListener kbd);

  /**
   * A method to add a Keyboard Listener to the view.
   * Same method signature to add a KeyListener to Swing stuff.
   *
   * @param kbd the listener to be added
   */
  void removeKeyListener(KeyboardListener kbd);

  /**
   * Returns a List of Note at the current beat.
   * If textual view, returns an empty list (because it doesn't store a current beat).
   * @return the notes at the current beat
   */
  List<Note> getNotesAtCurrentBeat();

  /**
   * Sets the viewModel to the given model.
   * @param model the given model
   */
  void setModel(ModelOperations model);

  /**
   * Plays all the notes that start at the specified beat. Only works for combined and midi.
   *
   * @param beat the specified beat
   */
  void playAllStartingAtBeat(int beat);
}