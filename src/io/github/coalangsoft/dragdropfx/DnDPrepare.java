package io.github.coalangsoft.dragdropfx;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import io.github.coalangsoft.visit.Visitor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.text.HitInfo;
import javafx.util.Callback;

public class DnDPrepare {

	private static final DnDTabPaneContext tabPaneContext = new DnDTabPaneContext();
	
	public static void labeled(final Labeled labeled) {
		labeled.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				Dragboard db = labeled.startDragAndDrop(TransferMode.ANY);
				
				ClipboardContent c = new ClipboardContent();
				c.putString(labeled.getText());
				
				db.setContent(c);
			}
			
		});
	}

	public static void textinput(final TextInputControl textinput) {
		textinput.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent d) {
				if(DnDTextInput.accepts(d.getDragboard())){
					d.acceptTransferModes(TransferMode.ANY);

//					if(textinput.getSelection().getLength() == 0){
//						//Move the input position (caret) to where the mouse pointer is
//						HitInfo info = DnDTextInput.getHitInfo(textinput, d);
//						if(info != null){
//							textinput.positionCaret(info.getInsertionIndex());
//						}
//					}
				}
				d.consume();
			}
			
		});
		textinput.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent d) {
				Dragboard db = d.getDragboard();

				//We do not need any if statements to figure out if the dragboard has contents - that is for "onDragOver".

				//Move the input position (caret) to where the mouse pointer is
				int insertion = DnDTextInput.getHitInfo(textinput, d).getInsertionIndex();
				if(!DnDTextInput.isInRange(insertion, textinput.getSelection())){
					textinput.positionCaret(insertion);
				}

				//Actually insert the content
				if(textinput.getSelectedText().isEmpty()){
					DnDTextInput.onCaretPosition(textinput, db);
				}else{
					DnDTextInput.onSelectedPosition(textinput, db);
				}
			}
			
		});
		new TextDragListenerContext(textinput).apply();
	}

	public static void gfx(final Node n) {
		n.setOnDragDetected(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent e) {
				Dragboard db = n.startDragAndDrop(TransferMode.ANY);
				db.setContent(image(n.snapshot(null, null)));
			}
			
		});
	}
	
	public static void mediaview(final MediaView mediaview){
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
	
	public static ClipboardContent image(Image i) {
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

	public static void tableColumns(final Visitor v, List<? extends TableColumn<?, ?>> addedSubList) {
		for(int i = 0; i < addedSubList.size(); i++){
			final TableColumn<?,?> col = addedSubList.get(i);
			
			col.setCellFactory(new DnDTableCellFactory(v, col.getCellFactory()));
			col.cellFactoryProperty().addListener(new ChangeListener<Callback>() {
				@Override
				public void changed(ObservableValue<? extends Callback> observable, Callback oldValue,
						Callback newValue) {
					if(!(newValue instanceof DnDTableCellFactory)){
						col.setCellFactory(new DnDTableCellFactory(v, col.getCellFactory()));
					}
				}
			});
		}
	}
	
	public static void tabPane(TabPane pane){
		tabPaneContext.prepare(pane);
	}
	
	public static void toolBar(ToolBar bar){
		bar.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if(event.getGestureSource() instanceof Button){
					event.acceptTransferModes(TransferMode.ANY);
				}
			}
		});
		bar.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Button n = (Button) event.getGestureSource();
				try{
					Pane p = (Pane) n.getParent();
					p.visibleProperty().addListener(new ChangeListener<Boolean>() {
						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								Boolean newValue) {
							System.out.println(p + " visible " + newValue);
						}
					});
					p.getChildren().remove(n);
					bar.getItems().add(n);
					
					EventHandler<ActionEvent> handler = n.getOnAction();
					n.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							p.requestFocus();
							if(p.isFocused()){
								n.requestFocus();
								handler.handle(event);
							}
						}
					});
				}catch(Exception e){}
			}
		});
	}

	/**
	 * Prepares a list of nodes for drag and drop action.
	 * A given Visitor (probably an instance of {@link DragDropFX}) is used to handle all the nodes in the list.
	 * The visitor is called for any node that is in the list, as well as for any node that is added later.
	 * @see #nodeList(Visitor, List) if do not want to react to newly added items
	 *
	 * @param visitor a visitor that can handle nodes
	 * @param nodeList a list of nodes
	 */
	public static void modifiableNodeList(final Visitor visitor, ObservableList<? extends Node> nodeList) {
		nodeList(visitor, nodeList);

		//Listen for new items
		nodeList.addListener(new ListChangeListener<Node>() {
			@Override
			public void onChanged(Change<? extends Node> change) {
				while(change.next()) {
					nodeList(visitor, change.getAddedSubList());
				}
			}
		});
	}

	/**
	 * Prepares a list of nodes for drag and drop action.
	 * A given Visitor (probably an instance of {@link DragDropFX}) is used to handle all the nodes in the list.
	 * The visitor is called for any node that is in the list, but not for any node that is added later.
	 * @see #modifiableNodeList(Visitor, ObservableList) if you want to react to newly added items as well
	 *
	 * @param visitor a visitor that can handle nodes
	 * @param nodeList a list of nodes
	 */
	public static void nodeList(final Visitor visitor, List<? extends Node> nodeList) {
		//Go through all the nodes and visit them
		for(int i = 0; i < nodeList.size(); i++) {
			visitor.handle(nodeList.get(i));
		}
	}

}
