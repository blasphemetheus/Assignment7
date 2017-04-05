package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

import cs3500.music.util.MouseRunnable;

/**
 * The Listener for Mouse Input. Only pays attention to a mouse-click event,
 * and runs the stored MouseRunnable if any exists in the eventMap.
 */
public class MouseInputListener implements MouseListener {
  private Map<Integer, MouseRunnable> mouseEventMap;

  MouseInputListener(Map<Integer, MouseRunnable> map) {
    this.mouseEventMap = map;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
            // The ID will always be e.mouse_clicked
    if (this.mouseEventMap.containsKey(e.MOUSE_CLICKED) && e.getID() == e.MOUSE_CLICKED) {
      mouseEventMap.get(e.MOUSE_CLICKED).run(e);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    //DOES NOTHING CURRENTLY
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    //CURRENTLY DOES NOTHING
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    //DOES NOTHING
  }

  @Override
  public void mouseExited(MouseEvent e) {
    //DOES NOTHING
  }

}
