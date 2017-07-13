package io.github.coalangsoft.dragdropfx;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
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

	static void gfx(final Node n) {
		n.setOnDragDetected(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent e) {
				Dragboard db = n.startDragAndDrop(TransferMode.ANY);
				db.setContent(image(n.snapshot(null, null)));
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
				db.setContent(image(mediaview.snapshot(null, null)));
				
			}
		});
	}
	
	static ClipboardContent image(Image i) {
		ClipboardContent c = new ClipboardContent();
		c.putImage(i);
		try {
			File f = File.createTempFile("snapshot", ".png");
			ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png", f);
			c.putFiles(Arrays.asList(f));
			c.putUrl(f.toURI().toURL().toExternalForm());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

}
