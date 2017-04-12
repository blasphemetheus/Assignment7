package cs3500.music.view;

import java.util.List;
import java.util.Map;

import cs3500.music.model.Instrument;
import cs3500.music.model.ModelOperations;
import cs3500.music.model.Note;
import cs3500.music.model.Octave;
import cs3500.music.model.Pitch;

/**
 * The implementation of ModelOperations that is stored in the view. Only allows certain operations.
 */
public class ViewModel implements ModelOperations {

  @Override
  public void setTempo(int microsecondsPerBeat) {

  }

  @Override
  public int getTempo() {
    return 0;
  }

  @Override
  public void addNote(Note note) throws IllegalArgumentException {

  }

  @Override
  public void edit(Note x, Note y) throws IllegalArgumentException {

  }

  @Override
  public void removeNote(Note note) throws IllegalArgumentException {

  }

  @Override
  public void removeAllOf(Pitch pitch, Octave octave) {

  }

  @Override
  public void startEditor() {

  }

  @Override
  public void save() {

  }

  @Override
  public void retrieve() throws IllegalStateException {

  }

  @Override
  public void mergeWith(ModelOperations thatTrack) {

  }

  @Override
  public void combineConsecutively(ModelOperations thatTrack) {

  }

  @Override
  public List<Note> getAllPlayingAtBeat(int beat) {
    return null;
  }

  @Override
  public List<Note> getAllStartingAtBeat(int beat) {
    return null;
  }

  @Override
  public void overwriteWith(ModelOperations thatTrack) {

  }

  @Override
  public List<Note> getNotes() {
    return null;
  }

  @Override
  public void changeInstrument(Instrument instrument) {

  }

  @Override
  public int numBeats() {
    return 0;
  }

  @Override
  public Note getLowestNote() throws IllegalArgumentException {
    return null;
  }

  @Override
  public Note getHighestNote() throws IllegalArgumentException {
    return null;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public Instrument getInstrument() {
    return null;
  }
}
