package cs3500.music.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs3500.music.controller.ButtonListener;
import cs3500.music.controller.KeyboardListener;
import cs3500.music.model.Duration;
import cs3500.music.model.ModelOperations;
import cs3500.music.model.Note;
import cs3500.music.model.Octave;
import cs3500.music.model.Pitch;

/**
 * A View that renders by printing the music state to the console and then ends. Does not.
 * Listen for actions or keys.
 */
public class TextualView implements ViewOperations {
  ModelOperations model;
  //  ConcreteGuiViewPanel listener;


  /**
   * Constructs a textual view.
   * @param model the given model
   */
  public TextualView(ModelOperations model) {
    this.model = model;

    // The only reason I'm using this implementation is because it definitely functions
    //    listener = new ConcreteGuiViewPanel(model);
    //    listener.setFocusable(true);
    //    listener.requestFocusInWindow();
  }


  /**
   * Renders the textual view by outputting it directly to the console (System.out) I think.
   */
  @Override
  public void render() {
    String output = "";
    output += this.getMusicState(model);

    System.out.println(output);
  }

  @Override
  public void printStuff(String stuff) {
    System.out.println(stuff);
  }

  @Override
  public void addKeyListener(KeyboardListener kbd) {

    // does nothing, because no commands should do anything as of yet
  }

  @Override
  public void addActionListener(ButtonListener buttonListener) {
    // does nothing, because no command should do anything as of yet
  }


  // MAKE SURE TO USE getNotes() !!!!!!!!!!!!!!

  /**
   * Helper method to configure the number of columns.
   * If you're on the last octave then print the last possible pitch
   * otherwise print all possible pitches till the next octave.
   *
   * @param i   the octave to be printed
   * @param max the max value a note can be according to its pitch
   * @return number of columns
   */
  public int columns(int i, int max) {
    if (i == max) {
      return max;
    } else {
      return 11;
    }
  }

  /**
   * Returns the state of the Model as a String (in columnar form) <p>Technical Specs: - A column of
   * numbers representing the beats, printed right-justified and padded with leading spaces, that is
   * exactly as wide as necessary. (So if your piece is 999 beats long, it uses three columns of
   * characters; if it’s 1000 beats long, it uses four.) - A sequence of columns, each five
   * characters wide, representing each pitch. The first line prints out the names of the pitches,
   * more-or-less centered within the five-character column. I.e., "  F2 " and " G#3 " and " D#10".
   * Because we need to represent at least ten octaves, three-character columns won’t be wide
   * enough.) - Use exactly as many columns as are needed for your piece, from its lowest to its
   * highest note. - Each note-head is rendered as an "  X  ", and each note-sustain is rendered as
   * "  |  ". When a note is not played, five spaces are rendered (as "     "). - As a consequence:
   * every line should be exactly the same length, as shown above. - Every item, including the last
   * one, ends in a newline. - Use the # character (the standard hash or pound sign) to represent
   * sharps, rather than the more correct ♯, to avoid any formatting errors when running your
   * code.</p>
   *
   * @return the state of the game
   */
  public String getMusicState(ModelOperations model) {

    String musicState = "";
    if (model.getNotes().isEmpty()) {
      return musicState;
    }
    ///Find end point of song(last note endAt beat)
    ///to set the required space for beats column
    ///We need this now to know where to start our first line
    List<Integer> beats = new ArrayList<>();
    for (int i = 0; i < model.getNotes().size(); i++) {
      List<Note> lon = model.getNotes();
      Duration dur = lon.get(i).getDur();
      int endBeat = dur.getEndBeat();
      beats.add(endBeat);
    }
    ///Add necessary gaps to first line
    int beatMax = Collections.max(beats);
    // bmlength is the number of digits
    int bmLength = String.valueOf(beatMax).length();

    for (int i = 0; i < bmLength; i++) {
      musicState += " ";
    }
    ///Accumulate pitches and octaves to empty lists to determine min and max values
    List<Integer> firstline1 = new ArrayList<>();
    List<Integer> firstline2 = new ArrayList<>();
    List<Note> firstline = new ArrayList<>();
    for (int i = 0; i < model.getNotes().size(); i++) {
      firstline1.add(model.getNotes().get(i).getPitch().getOrderVal());
    }
    for (int i = 0; i < model.getNotes().size(); i++) {
      firstline2.add(model.getNotes().get(i).getOctave().toInt());
    }
    ///Sort out maximums and minimums
    int pitchMin = Collections.min(firstline1);
    int pitchMax = Collections.max(firstline1);
    int octaveMin = Collections.min(firstline2);
    int octaveMax = Collections.max(firstline2);

    ///make all possible combination between min and max values and add
    ///to an empty list
    for (int i = octaveMin; i <= octaveMax; i++) {
      for (int j = pitchMin; j <= columns(i, pitchMax); j++) {
        // firstline.add(new Note(new Pitch(j, null), new Octave(i), null));
        firstline.add(new Note(Pitch.fromInt(j), Octave.fromInt(i), null));
      }
    }

    ///loop the list with the correct character spaces between them(5)
    for (int k = 0; k < firstline.size(); k++) {
      switch (firstline.get(k).toString().length()) {
        case 2:
          musicState += "  " + firstline.get(k).toString() + " ";
          break;
        case 3:
          musicState += " " + firstline.get(k).toString() + " ";
          break;
        case 4:
          musicState += " " + firstline.get(k).toString() + "";
          break;
        default:
          musicState += "" + firstline.get(k).toString() + "";
      }
    }
    musicState += "\n";

    ///Now each corresponding line:
    ///Print each line vertically
    for (int i = 0; i < beatMax; i++) {
      musicState += i;
      ///horizontally
      ///loop through 5 character spaces
      for (int j = 0; j < firstline.size(); j++) {
        ///loop through list for possible X or | or empty
        for (int k = 0; k < model.getNotes().size(); k++) {
          if (model.getNotes().get(k).getDur().getStartBeat() == i
                  && model.getNotes().get(k).getPitch() == firstline.get(j).getPitch()
                  && model.getNotes().get(k).getOctave() == firstline.get(j).getOctave()) {
            musicState += "  X  ";
          } else {
            if (model.getNotes().get(k).getDur().getEndBeat() == i
                    && model.getNotes().get(k).getPitch() == firstline.get(j).getPitch()
                    && model.getNotes().get(k).getOctave() == firstline.get(j).getOctave()) {
              musicState += "  |  ";
            } else {
              if (model.getNotes().get(k).getDur().getStartBeat() < i
                      && model.getNotes().get(k).getDur().getEndBeat() >= i
                      && model.getNotes().get(k).getPitch() == firstline.get(j).getPitch()
                      && model.getNotes().get(k).getOctave() == firstline.get(j).getOctave()) {
                musicState += "  |  ";
              }
            }
          }
        }
        if (!(model.getNotes().contains(firstline.get(j)))) {
          musicState += "     ";
        }
      }
      musicState += "\n";
    }
    ///required new line at the end
    return musicState += "\n";
  }

  @Override
  public void moveRight() {

    // DOES NOTHING BECAUSE THIS VIEW DOES NOT FIND THAT COMMAND TO BE USEFUL

  }

  @Override
  public void moveLeft() {

    // DOES NOTHING BECAUSE THIS VIEW DOES NOT FIND THAT COMMAND TO BE USEFUL
  }
}
