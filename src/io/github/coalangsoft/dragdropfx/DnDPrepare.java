package io.github.coalangsoft.dragdropfx;

import javafx.event.EventHandler;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;

public class DnDPrepare {

	static void labeled(final Labeled labeled) {
		labeled.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				Dragboard db = labeled.startDragAndDrop(TransferMode.ANY);
				
				ClipboardContent c = new ClipboardContent();
				c.putString(labeled.getText());
				
				db.setContent(c);
				db.setDragView(labeled.snapshot(null, null));
			}
			
		});
	}

	static void textinput(final TextInputControl textinput) {
		textinput.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent d) {
				if(d.getDragboard().hasString()){
					d.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
			}
			
		});
		textinput.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent d) {
				Dragboard db = d.getDragboard();
				if(db.hasString()){
					if(textinput.getSelectedText().isEmpty()){
						DnDTextInput.onCaretPosition(textinput, db);
					}else{
						DnDTextInput.onSelectedPosition(textinput, db);
					}
				}
			}
			
		});
	}

	static void imageview(final ImageView imageview) {
		imageview.setOnDragDetected(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent e) {
				Dragboard db = imageview.startDragAndDrop(TransferMode.ANY);
				
				ClipboardContent c = new ClipboardContent();
				c.putImage(imageview.snapshot(null, null));
				
				db.setContent(c);
			}
			
		});
	}
	
	static void mediaview(final MediaView mediaview){
		mediaview.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				MediaPlayer p = mediaview.getMediaPlayer();
				Status s = p.getStatus();
				if(s == Status.PLAYING){
					p.pause();
				}
				
				Dragboard db = mediaview.startDragAndDrop(TransferMode.ANY);
				
				ClipboardContent c = new ClipboardContent();
				c.putImage(mediaview.snapshot(null, null));
				c.putUrl(p.getMedia().getSource());
				c.putString(p.getMedia().getSource());
				
				db.setContent(c);
				
			}
		});
	}

}
