package test.dragdropfx;
import java.util.List;

import io.github.coalangsoft.dragdropfx.DnDPrepare;
import io.github.coalangsoft.dragdropfx.DragDropFX;
import io.github.coalangsoft.lib.data.Func;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TabDrag extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		TabPane tp = new TabPane();
		
		Tab t1 = new Tab("First");
		t1.setGraphic(new ProgressBar());
		t1.setContent(new TextArea("Stuff"));
		Tab t2 = new Tab("Second");
		t2.setContent(new Button("Hey!"));
		
		tp.getTabs().addAll(t1,t2);
		
		DragDropFX ddfx = new DragDropFX();
		ddfx.addFunction(TabPane.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				DnDPrepare.tabPane((TabPane) p);
				return null;
			}
		});
		ddfx.handle(tp);
		
		t1.setText("Change!");
//		t1.setGraphic(null);
		
		primaryStage.setScene(new Scene(tp));
		primaryStage.show();
		
		Stage second = new Stage();
		TabPane tp2 = new TabPane();
		tp2.getTabs().add(new Tab("Hey!"));
		ddfx.handle(tp2);
		second.setScene(new Scene(tp2));
		second.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
