package cs3500.music;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import javax.naming.ldap.Control;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import cs3500.music.controller.ControllerOperations;
import cs3500.music.controller.MusicController;
import cs3500.music.mocks.MockReciever;
import cs3500.music.mocks.MockSequencer;
import cs3500.music.mocks.MockSynthesizer;
import cs3500.music.model.ModelOperations;
import cs3500.music.model.MusicModel;
import cs3500.music.view.MidiView;
import cs3500.music.view.ViewFactory;
import cs3500.music.view.ViewOperations;

import static org.junit.Assert.assertEquals;

/**
 * The test class for the handlers and mocks.
 */
public class EditorTest {


  @Test
  public void testEmptyMidi() {
    StringBuilder sb = new StringBuilder();
    MockReciever mockRec = new MockReciever(sb);
    MockSynthesizer mockSynth = new MockSynthesizer(sb);
    ModelOperations model = new MusicModel();
    ViewOperations view = MidiView.builder().setSynth(mockSynth).build();
    MusicController controller = new MusicController(model, view);
    controller.start();
    assertEquals("", mockSynth.getStringBuilder().toString());
  }


  @Test
  public void testMidiMaryHadLamb() throws IOException {
    StringBuilder sb = new StringBuilder();
    MockReciever mockRec = new MockReciever(sb);
    Synthesizer mockSynth = new MockSynthesizer(sb);

    ModelOperations model = MusicEditor.getFromFile("C:\\songs\\mary-little-lamb.txt");

    ViewOperations view = MidiView.builder().setSynth(mockSynth).setReceiver(mockRec).noDelay().build();
    MusicController controller = new MusicController(model, view);
    controller.start();
    System.out.println(sb.length());
    assertEquals("bunch of stuff", sb.toString());

  }


  @Test
  public void testKeyHandler() {

  }



  @Test
  public void controllerSuite() throws IOException {
    ModelOperations model = new MusicModel();

    Readable fileReader = null;

    model = MusicEditor.getFromFile("mary-little-lamb.txt");

    ViewOperations view = ViewFactory.create("combined", model);

    ControllerOperations controller = new MusicController(model, view);

    controller.start();
    assertEquals("message", view.toString());
  }
}
