package cs3500.music.view;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.util.List;


import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JPanel;




import cs3500.music.controller.ButtonListener;
import cs3500.music.controller.KeyboardListener;
import cs3500.music.controller.MouseInputListener;
import cs3500.music.model.ModelOperations;
import cs3500.music.model.Note;

import cs3500.music.model.MusicModel;

/**
 * A skeleton Frame (i.e., a window) in Swing
 */
public class VisualView extends javax.swing.JFrame implements ViewOperations {
  private final VisualJPanel displayPanel; // You may want to refine this to a subtype of JPanel
  // Fossils from the MVC Class Example
  private JLabel display;
  private JButton echoButton;
  private JButton exitButton;
  private JTextField input;
  JScrollPane scroller;
  private Graphics g;
  private Graphics2D g2;
  public int WIDTH = 1000;
  public int HEIGHT = 600;
  private boolean scroll;

  ModelOperations viewModel;

  public static final int X_FRAME = 10;
  public static final int Y_FRAME = 10;
  public static final int X_SCALE = 10;
  public static final int Y_SCALE = 10;

  /**
   * Default public constructor, creates new VisualView.
   */
  public VisualView(ModelOperations model) {
    super("Music Editor");
    // String first = model.getNotes().get(0).toString();
    this.viewModel = model;
    this.scroll = false;

    JPanel container = new JPanel();
    JScrollPane jsp = new JScrollPane(container);
    container.setPreferredSize(new Dimension(WIDTH, HEIGHT/2));
    container.setLayout(null);

    // NEW STUFF :
    //    displayPanel.setFocusable(true);
    //    displayPanel.requestFocusInWindow();


    // FOSSIL:
//        setSize(1000, 1000);
    //    setLocation(200, 200);
    //    this.setResizable(false);
    //		this.setMinimumSize(new Dimension(300,300));
    displayPanel = new VisualJPanel(model);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    this.getContentPane().add(container);
    this.getContentPane().add(displayPanel);


    ///Set the size of our Display
    //displayPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setBackground(Color.BLACK);
    //Set location of our Display
    //displayPanel.setLocation(200, 200);
    setFocusable(true);
    setResizable(false);
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    //setMaximumSize(new Dimension(WIDTH, HEIGHT));


    this.pack();
    setVisible(true);
  }


  public void initialize(){
    this.setVisible(true);
  }

  @Override
  public Dimension getPreferredSize(){
    return new Dimension(100, 100);
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
  String getStringRepresentation(){return "";};


  @Override
  public void render() {

  }

  @Override
  public void addKeyListener(KeyboardListener kbd) {
    displayPanel.addKeyListener(kbd);
  }

  @Override
  public void removeKeyListener(KeyboardListener kbd) {

  }

  @Override
  public void moveRight() {
    displayPanel.moveRight();
  }

  @Override
  public void moveLeft() {
    displayPanel.moveLeft();
  }

  @Override
  public void addMouseListener(MouseInputListener mil) {

  }

  @Override
  public void removeMouseListener(MouseInputListener mil) {

  }

  @Override
  public void togglePlayback() {
    // does nothing
  }

  @Override
  public void jumpToBeginning() {

  }

  @Override
  public void jumpToEnd() {

  }

  @Override
  public void addNoteAtBeat(Note note) {

  }

  @Override
  public void addNoteAtBeat() {

  }

  @Override
  public void updateRedLine() {
    boolean scroller = false;

    if (displayPanel.getMaximumSize().width > this.getWidth()) {
      scroller = true;
    }

    if (scroller & scroll) {
      this.scrollRight();
    }

    this.displayPanel.updateRedLine();

    this.repaint();
  }

  private void scrollRight() {
    System.out.println("Scroll right");
  }

  /**
   * Sets the view to switch whether to render the scroll.
   */
  public void toggleScroll() {
    scroll = !scroll;
  }

  @Override
  public List<Note> getNotesAtCurrentBeat() {
    return viewModel.getAllStartingAtBeat(this.displayPanel.bar);
  }

  @Override
  public void setModel(ModelOperations model) {

  }

  @Override
  public void playAllStartingAtBeat(int beat) {

  }

  @Override
  public void update() {
    // TODO ADD Methods to reclaculate the endNote

    this.repaint();
  }
}