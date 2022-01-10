package io.github.coalangsoft.dragdropfx;

import javafx.event.EventHandler;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.HitInfo;

public class TextDragListenerContext {
	
	public TextDragListenerContext(TextInputControl text){
		this.textInput = text;
	}
	
	private boolean inClick;
	private TextInputControl textInput;
	private IndexRange currentSelection;
	
	private EventHandler<MouseEvent> makeOnButtonDownListener(){
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.PRIMARY){
					HitInfo i = DnDTextInput.getHitInfo((TextInputControl) event.getSource(), event);
					IndexRange r = textInput.getSelection();
					
					if(DnDTextInput.isInRange(i.getInsertionIndex(), r)){
						currentSelection = r;
					}
					
					inClick = true;
				}
			}
		};
	}
	
	private EventHandler<MouseEvent> makeOnButtonUpListener(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.PRIMARY && inClick){
					inClick = false;
					currentSelection = null;
				}
			}
		};
	}
	
	private EventHandler<MouseEvent> makeOnDragDetectedListener(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(currentSelection != null){
					Dragboard db = textInput.startDragAndDrop(TransferMode.ANY);
					ClipboardContent c = new ClipboardContent();
					c.putString(textInput.getText(currentSelection.getStart(), currentSelection.getEnd()));
					db.setContent(c);
					textInput.selectRange(currentSelection.getStart(), currentSelection.getEnd());
				}
			}
		};
	}
	
	public void apply(){
		textInput.addEventFilter(MouseEvent.MOUSE_PRESSED, makeOnButtonDownListener());
		textInput.addEventFilter(MouseEvent.MOUSE_RELEASED, makeOnButtonUpListener());
		textInput.setOnDragDetected(makeOnDragDetectedListener());
	}
	
}
