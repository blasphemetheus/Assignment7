package cs3500.music.util;

import java.awt.event.MouseEvent;

/**
 * An interface that extends Runnable that also requires a run method that takes a mouseEvent.
 * Otherwise the same as Runnable.
 */
public interface MouseRunnable extends Runnable {
  void run(MouseEvent e);
}
