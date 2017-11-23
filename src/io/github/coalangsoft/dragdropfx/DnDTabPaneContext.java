package io.github.coalangsoft.dragdropfx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

public class DnDTabPaneContext {
	
	private TabPane sourceOfDragging;
	
	public void prepare(TabPane pane){
		pane.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				if(event.getGestureSource() instanceof TabPane){
					sourceOfDragging = (TabPane) event.getGestureSource();
					Dragboard db = event.getDragboard();
					if(db.hasString()){
						try{
							int i = Integer.parseInt(db.getString());
						}catch(Exception e){}
					}
				}
				event.acceptTransferModes(TransferMode.ANY);
			}
		});
		pane.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if(sourceOfDragging != null && sourceOfDragging != pane){
					try{
						Tab t = sourceOfDragging.getTabs().get(Integer.parseInt(event.getDragboard().getString()));
						sourceOfDragging.getTabs().remove(t);
						pane.getTabs().add(t);
					}catch(Exception e){
						e.printStackTrace();
					}
					event.consume();
				}
				sourceOfDragging = null;
			}
		});
		for(int i = 0; i < pane.getTabs().size(); i++){
			handleTab(pane.getTabs().get(i));
		}
	}

	public static void handleTab(Tab added){
		Node gfx = added.getGraphic();
		HBox box = new HBox();
		box.setMouseTransparent(false);
		box.setOnDragEntered(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				added.getTabPane().getSelectionModel().select(added);
			}
		});
		box.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Dragboard db = added.getTabPane().startDragAndDrop(TransferMode.ANY);
				ClipboardContent cc = new ClipboardContent();
				cc.putString(added.getTabPane().getSelectionModel().getSelectedIndex() + "");
				db.setContent(cc);
			}
		});
		if(gfx != null){
			box.getChildren().add(gfx);
		}
		
		Label text = new Label(added.getText());
		added.setText("");
		added.textProperty().addListener(new ChangeListener<String>() {
			
			private boolean changedText;
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(changedText){
					changedText = false;
					return;
				}
				text.setText(newValue);
				changedText = true;
				added.setText("");
			}
			
		});
		box.getChildren().add(text);
		
		added.setGraphic(box);
		
		added.graphicProperty().addListener(new ChangeListener<Node>() {

			private boolean changedGraphic;
			
			@Override
			public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
				if(changedGraphic){
					changedGraphic = false;
					return;
				}
				if(box.getChildren().size() == 2){
					box.getChildren().remove(0);
				}
				if(newValue != null){
					box.getChildren().add(0, newValue);
				}
				changedGraphic = true;
				added.setGraphic(box);
			}
		});
	}
	
}
