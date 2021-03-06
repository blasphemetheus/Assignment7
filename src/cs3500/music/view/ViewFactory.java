package cs3500.music.view;

import javax.swing.text.CompositeView;

import cs3500.music.controller.KeyboardListener;
import cs3500.music.controller.MouseInputListener;
import cs3500.music.model.ModelOperations;

/**
 * A factory of views, constructs an appropriate view based on the String input.
 */
public final class ViewFactory {

  /**
   * Creates an appropriate view based on the input String (acceptable inputs are.
   * "visual" "midi" and "console").
   *
   * @param typeOfView the String input
   * @return A ViewOperations implementation of the appropriate type
   */
  public static ViewOperations create(String typeOfView, ModelOperations model) {
    switch (typeOfView) {
      case "visual":
        return new VisualView(model);
      case "midi":
        return new MidiView(model);
      case "console":
        return new TextualView(model);
      case "composite":
        return new CombinedView(model);
      default:
        throw new IllegalArgumentException("Incorrect String, cannot make that type of view: "
                + typeOfView);
    }
  }
}
