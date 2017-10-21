package io.github.coalangsoft.dragdropfx;

import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import com.sun.javafx.scene.text.HitInfo;

import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;

public class DnDTextInput {

	static void onCaretPosition(TextInputControl textinput,
			Dragboard dragboard) {
		int caret = textinput.getCaretPosition();
		String insert = dragboard.getString();
		
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
		textinput.setText(t1 + dragboard.getString() + t2);
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

}