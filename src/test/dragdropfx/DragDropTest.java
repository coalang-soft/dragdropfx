package test.dragdropfx;

import io.github.coalangsoft.dragdropfx.DragDropFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DragDropTest extends Application{

	@Override
	public void start(Stage s) throws Exception {
		final Label l = new Label("This is Text! :)");
		final TextField tf = new TextField();
		
		VBox layout = new VBox();
		layout.getChildren().add(l);
		layout.getChildren().add(tf);
		layout.getChildren().add(new ImageView("https://www.google.de/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"));
		
		new DragDropFX().handle(layout);
		
		s.setScene(new Scene(layout));
		
		s.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
