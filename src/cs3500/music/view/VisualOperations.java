package cs3500.music.view;

import java.util.List;

import cs3500.music.model.Note;

/**
 * Public interface for the Visual View, requires the ability to update the red line.
 */
public interface VisualOperations extends ViewOperations {
  void updateRedLine();

  void update();

  void toggleScroll();

  List<Note> getNotesAtCurrentBeat();
}
