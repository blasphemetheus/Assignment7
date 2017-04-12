package cs3500.music.mocks;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

/**
 * Created by blewf on 4/3/2017.
 */
public class MockSequencer implements Sequencer {
  private Sequence sequence;
  private MockReciever receiver;


  private MockSequencer() {
    // this is private only, no public allowed
  }

  /**
   * Constructs a MockSequencer with a MockReciever.
   *
   * @param rec the reciever (a mock)
   */
  public MockSequencer(MockReciever rec) {
    this.receiver = rec;
  }

  @Override
  public Receiver getReceiver() throws MidiUnavailableException {
    return this.receiver;
  }

  @Override
  public void setSequence(Sequence sequence) throws InvalidMidiDataException {
    this.sequence = sequence;

  }

  @Override
  public void setSequence(InputStream stream) throws IOException, InvalidMidiDataException {
    throw new IllegalArgumentException("This is a mock, not allowed");
  }

  @Override
  public Sequence getSequence() {
    return this.sequence;
  }

  @Override
  public void start() {
    Track track = this.sequence.getTracks()[0];

    for (int i = 0; i < track.size() - 1 ; i += 1) {
      MidiMessage message1 = track.get(i).getMessage();
      MidiMessage message2 = track.get(i + 1).getMessage();
      long messageTick1 = track.get(i).getTick();
      long messageTick2 = track.get(i + 1).getTick();

      //pass messages to reciever
      receiver.send(message1, messageTick1);
      receiver.send(message2, messageTick2);
      i += 1;
    }
  }

  @Override
  public void stop() {
    // Don't do anything
  }

  @Override
  public boolean isRunning() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void startRecording() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void stopRecording() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public boolean isRecording() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void recordEnable(Track track, int channel) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void recordDisable(Track track) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public float getTempoInBPM() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setTempoInBPM(float bpm) {
    //TODO, could actually use in testing, but shouldn't do anything
    throw new IllegalArgumentException("Shouldn't use ever");
  }

  @Override
  public float getTempoInMPQ() {
    //TODO, could actually use in testing, but shouldn't do anything
    throw new IllegalArgumentException("Shouldn't use ever");
  }

  @Override
  public void setTempoInMPQ(float mpq) {
    //TODO, could actually use in testing, but shouldn't do anything
    throw new IllegalArgumentException("Shouldn't use ever");
  }

  @Override
  public void setTempoFactor(float factor) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public float getTempoFactor() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public long getTickLength() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public long getTickPosition() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setTickPosition(long tick) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public long getMicrosecondLength() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public Info getDeviceInfo() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void open() throws MidiUnavailableException {
    // Dont do anything
  }

  @Override
  public void close() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public boolean isOpen() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public long getMicrosecondPosition() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public int getMaxReceivers() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public int getMaxTransmitters() {
    throw new IllegalArgumentException("Not functional in mock");
  }


  @Override
  public List<Receiver> getReceivers() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public Transmitter getTransmitter() throws MidiUnavailableException {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public List<Transmitter> getTransmitters() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setMicrosecondPosition(long microseconds) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setMasterSyncMode(SyncMode sync) {
    throw new IllegalArgumentException("Not functional in mock");

  }

  @Override
  public SyncMode getMasterSyncMode() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public SyncMode[] getMasterSyncModes() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setSlaveSyncMode(SyncMode sync) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public SyncMode getSlaveSyncMode() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public SyncMode[] getSlaveSyncModes() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setTrackMute(int track, boolean mute) {
    throw new IllegalArgumentException("Not functional in mock");

  }

  @Override
  public boolean getTrackMute(int track) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setTrackSolo(int track, boolean solo) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public boolean getTrackSolo(int track) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public boolean addMetaEventListener(MetaEventListener listener) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void removeMetaEventListener(MetaEventListener listener) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public int[] addControllerEventListener(ControllerEventListener listener, int[] controllers) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public int[] removeControllerEventListener(ControllerEventListener listener, int[] controllers) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setLoopStartPoint(long tick) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public long getLoopStartPoint() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setLoopEndPoint(long tick) {
    throw new IllegalArgumentException("Not functional in mock");

  }

  @Override
  public long getLoopEndPoint() {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public void setLoopCount(int count) {
    throw new IllegalArgumentException("Not functional in mock");
  }

  @Override
  public int getLoopCount() {
    throw new IllegalArgumentException("Not functional in mock");
  }
}
