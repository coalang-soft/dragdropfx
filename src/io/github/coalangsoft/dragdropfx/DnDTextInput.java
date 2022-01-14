package io.github.coalangsoft.dragdropfx;

import javafx.scene.control.IndexRange;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.HitInfo;

public class DnDTextInput {

	static void onCaretPosition(TextInputControl textinput,
			Dragboard dragboard) {
		int caret = textinput.getCaretPosition();
		String insert = convertToString(dragboard);
		
		String t1 = textinput.getText().substring(0, textinput.getCaretPosition());
		String t2 = textinput.getText().substring(textinput.getCaretPosition(), textinput.getText().length());
		textinput.setText(t1 + insert + t2);
		textinput.positionCaret(caret + insert.length());
	}

	static void onSelectedPosition(TextInputControl textinput,
			Dragboard dragboard) {
		IndexRange selection = textinput.getSelection();
		String t1 = textinput.getText().substring(0, selection.getStart());
		String t2 = textinput.getText().substring(selection.getEnd(), textinput.getText().length());
		textinput.setText(t1 + convertToString(dragboard) + t2);
	}
	
	static HitInfo getHitInfo(TextInputControl textinput, MouseEvent d){
		return getHitInfo(textinput,d.getX(),d.getY());
	}
	
	static HitInfo getHitInfo(TextInputControl textinput, DragEvent d){
		return getHitInfo(textinput,d.getX(),d.getY());
	}
	
	static HitInfo getHitInfo(TextInputControl textinput, double x, double y){
		if(textinput instanceof TextField){
        	// position caret at drag coordinates 
			TextFieldSkin skin = (TextFieldSkin) textinput.getSkin();
			return skin.getIndex(x,y);
        }if(textinput instanceof TextArea){
        	TextAreaSkin skin = (TextAreaSkin) textinput.getSkin();
        	return skin.getIndex(x,y);
        }
        return null;
	}
	
	static boolean isInRange(int caret, IndexRange range){
		return caret > range.getStart() && caret < range.getEnd();
	}

	/**
	 * Returns true if {@link #convertToString(Dragboard)} can convert a given dragboard into a string.
	 * This is primarily meant to figure out if a drag event should be accepted or not.
	 * @param dragboard a dragboard
	 * @return if the dragboard can be converted into a string
	 */
	public static boolean accepts(Dragboard dragboard) {
		return dragboard.hasString() || dragboard.hasFiles() || dragboard.hasUrl();
	}

	/**
	 * Converts dragboard contents into a string
	 * @param dragboard a dragboard
	 * @return a string representation of the dragboard contents; or null if no contents to work with were found
	 * @see #accepts(Dragboard)
	 */
	public static String convertToString(Dragboard dragboard) {
		if(dragboard.hasString()) {
			return dragboard.getString();
		} else if(dragboard.hasFiles()) {
			return dragboard.getFiles().get(0).getAbsolutePath();
		} else if(dragboard.hasUrl()) {
			return dragboard.getUrl();
		} else {
			return null;
		}
	}
}