package cs3500.music.controller;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import cs3500.music.model.Duration;
import cs3500.music.model.ModelOperations;
import cs3500.music.model.Note;
import cs3500.music.model.Octave;
import cs3500.music.model.Pitch;
import cs3500.music.util.MouseRunnable;
import cs3500.music.view.ViewOperations;
import cs3500.music.view.VisualView;

/**
 * Our Controller for the Music Model, stores model and view and uses keyboard and button listeners.
 */
public class MusicController implements ControllerOperations {
  private ViewOperations view;
  private ModelOperations model;


  /**
   * The public constructor, where we pass in the model and view and configure our listeners.
   *
   * @param m the ModelOperations
   * @param v the ViewOperations
   */
  public MusicController(ModelOperations m, ViewOperations v) {
    this.view = v;
    this.model = m;
    configureKeyBoardListener();
    //    configureButtonListener();
    configureMouseListener();
  }

  @Override
  public void start() {
    this.view.render();
  }

  private void configureMouseListener() {
    Map<Integer, MouseRunnable> mouseEvents = new HashMap<>();

    // my stuff
    mouseEvents.put(MouseEvent.MOUSE_CLICKED, new OnClick());
    mouseEvents.put(MouseEvent.MOUSE_WHEEL, new OnWheel());

    MouseInputListener mil = new MouseInputListener(mouseEvents);
    view.addMouseListener(mil);
  }

  private void configureKeyBoardListener() {
    Map<Character, Runnable> keyTypes = new HashMap<>();
    Map<Integer, Runnable> keyPresses = new HashMap<>();
    Map<Integer, Runnable> keyReleases = new HashMap<>();

    // My Stuff
    keyTypes.put('c', new MoveBeatOneBackward());
    // keyTypes.put('d', () -> this.view.printStuff("WOWZA"));
    keyTypes.put('a', () -> this.view.addNoteAtBeat());

    keyPresses.put(KeyEvent.VK_LEFT, new MoveBeatOneBackward());
    keyPresses.put(KeyEvent.VK_RIGHT, new MoveBeatOneForward());
    keyPresses.put(KeyEvent.VK_SPACE, () -> this.view.togglePlayback());
    keyPresses.put(KeyEvent.VK_HOME, () -> this.view.jumpToBeginning());
    keyPresses.put(KeyEvent.VK_END, () -> this.view.jumpToEnd());

    KeyboardListener kbd = new KeyboardListener();
    kbd.setKeyTypedMap(keyTypes);
    kbd.setKeyPressedMap(keyPresses);
    kbd.setKeyReleasedMap(keyReleases);

    view.addKeyListener(kbd);
  }

  //  private void configureButtonListener() {
  //    Map<String, Runnable> buttonClickedMap = new HashMap<String, Runnable>();
  //    ButtonListener buttonListener = new ButtonListener();
  //
  //    //    buttonClickedMap.put("Echo Button", new EchoButtonAction());
  //    // buttonClickedMap.put("Exit Button", new ExitButtonAction());
  //
  //    buttonListener.setButtonClickedActionMap(buttonClickedMap);
  //    this.view.addActionListener(buttonListener);
  //  }

  //-----------------------------------------------------------------

  // CLASSES NESTED INSIDE CONTROLLER SO THEY HAVE ACCESS TO THE VIEW
  //  class MakeOriginalCase implements Runnable {
  //    public void run() {
  //      String text = model.toString();
  //      view.setEchoOutput(text);
  //    }
  //  }

  class MoveBeatOneForward implements Runnable {
    public void run() {
      view.moveRight();
      // moves beat of red line one forward
      // if in last beat moves to very end
      System.out.println("right");

    }
  }

  class MoveBeatOneBackward implements Runnable {
    public void run() {
      view.moveLeft();
      // moves beat of red line one backward
      // if at first beat, doesn't do anything (or restarts the beat)
      System.out.println("left");
    }
  }

  /**
   * Performs logic for a click.
   */
  class OnClick implements Runnable, MouseRunnable {

    //TODO Might want to make these static fields in VisualView
    int X_FRAME = VisualView.X_FRAME;
    int Y_FRAME = VisualView.Y_FRAME;
    int X_SCALE = VisualView.X_SCALE;
    int Y_SCALE = VisualView.Y_SCALE;

    public void run(MouseEvent e) {
      Note note = null;
      try {
        note = this.getNote(e);
      } catch (IllegalArgumentException j) {
        System.out.println("caught illegalargexception: " + j);
        System.out.println("failed to find a note at that clickspot");
      }

      view.addNoteAtBeat(note);
      //      view.update();
    }

    private Note getNote(MouseEvent e) throws IllegalArgumentException {
      Note note = null;
      int beat = 0;
      int x = e.getX();
      int y = e.getY();
      y = y - Y_FRAME;
      x = x - X_FRAME;

      int griddedX = x / X_SCALE;
      int griddedY = y / Y_SCALE;
      //TODO this logic doesn't make any sense yet, just randomly selects pitches and octaves
      if (griddedX > 12) {
        griddedX = (int) Math.floor(Math.random() * 12);
      }
      if (griddedY > 12) {
        griddedY = (int) Math.floor(Math.random() * 12);
      }
      Pitch pitch = Pitch.fromInt(griddedX);
      Octave octave = Octave.fromInt(griddedY);
      Duration duration = new Duration(1, 2);
      return new Note(pitch, octave, duration);
    }

    public void run() {
      //TODO what to do on click (where did click, what to do logic)
      System.out.println("click detected");
    }

  }

  class OnWheel implements Runnable, MouseRunnable {
    public void run() {
      //TODO does stuff on wheel
      System.out.println("wheel detected");
    }

    @Override
    public void run(MouseEvent e) {
      //DOESn't do anything currently
      //TODO does stuff on wheel
      System.out.println("wheel detected");
    }
  }

  //  class MakeCaps implements Runnable {
  //    public void run() {
  //      String text = model.toString();
  //      text = text.toUpperCase();
  //      view.setEchoOutput(text);
  //    }
  //  }

  //  class EchoButtonAction implements Runnable {
  //    public void run() {
  //      String text = view.getInputString();
  //      // send text to the model
  //      model.setString(text);
  //
  //      //clear input text field
  //      view.clearInputString();
  //      // echo the string in view
  //      text = model.getString();
  //      view.setEchoOutput(text);
  //
  //      //set focus bck to main frame (for keyboard listerner stuff
  //      view.resetFocus();
  //    }
  //  }

  //  class ExitButtonAction implements Runnable {
  //    public void run() {
  //      System.exit(0);
  //    }
  //  }
}