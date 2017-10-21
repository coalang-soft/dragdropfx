package test.dragdropfx;

import io.github.coalangsoft.dragdropfx.DnDPrepare;
import io.github.coalangsoft.dragdropfx.DragDropFX;
import io.github.coalangsoft.dragdropfx.TextDragListenerContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DragDropTest extends Application{

	@Override
	public void start(Stage s) throws Exception {
		TextField a = new TextField();
		
		s.setScene(new Scene(a));
		
		new DragDropFX().handle(a);
		
		s.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
