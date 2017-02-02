package io.github.coalangsoft.dragdropfx;

import javafx.scene.control.IndexRange;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.Dragboard;

public class DnDTextInput {

	static void onCaretPosition(TextInputControl textinput,
			Dragboard dragboard) {
		String t1 = textinput.getText().substring(0, textinput.getCaretPosition());
		String t2 = textinput.getText().substring(textinput.getCaretPosition(), textinput.getText().length());
		textinput.setText(t1 + dragboard.getString() + t2);
	}

	static void onSelectedPosition(TextInputControl textinput,
			Dragboard dragboard) {
		IndexRange selection = textinput.getSelection();
		String t1 = textinput.getText().substring(0, selection.getStart());
		String t2 = textinput.getText().substring(selection.getEnd(), textinput.getText().length());
		textinput.setText(t1 + dragboard.getString() + t2);	
	}

}
