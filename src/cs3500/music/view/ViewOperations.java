package cs3500.music.view;

import java.awt.event.MouseListener;

import cs3500.music.controller.ButtonListener;
import cs3500.music.controller.KeyboardListener;
import cs3500.music.controller.MouseInputListener;
import cs3500.music.model.Note;

/**
 * The operations that all of my views need to be able to do.
 */
public interface ViewOperations {

  /**
   * Renders the view, whatever the specifics of that rendering may be.
   */
  void render();


  /**
   * Prints the stuff onto the console.
   *
   * @param stuff whatever String is given
   */
  void printStuff(String stuff);


  /**
   * Forces the view to have a method to set up the (typing) keyboard.
   * Same method signature to add a KeyListener to Swing.
   *
   * @param kbd the listener to be added
   */
  void addKeyListener(KeyboardListener kbd);

  void removeKeyListener(KeyboardListener kbd);

//  /**
//   * Forces the view to have a method to set up actions for buttons.
//   * All buttons must be given this action listener.
//   *
//   * @param buttonListener the listener to be added
//   */
//  void addActionListener(ButtonListener buttonListener);
//
//  void removeActionListener(ButtonListener buttonListener);

  void moveRight();

  void moveLeft();

  void addMouseListener(MouseInputListener mil);

  void removeMouseListener(MouseInputListener mil);

  /**
   * Toggles playback for the view.
   */
  void togglePlayback();

  void jumpToBeginning();

  void jumpToEnd();

  /**
   * Attempts to add a note at the current beat.
   */
  void addNoteAtBeat(Note note);

  /**
   * Attempts to add a note at the current beat.
   */
  void addNoteAtBeat();

  /**
   * Updates the view to reflect current changes.
   */
  void update();
}