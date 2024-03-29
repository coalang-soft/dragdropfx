package io.github.coalangsoft.dragdropfx;

import java.util.ArrayList;

import io.github.coalangsoft.lib.data.Func;
import io.github.coalangsoft.visit.Visitor;
import io.github.coalangsoft.visit.VisitorInfo;
import io.github.coalangsoft.visitfx.ParentChildrenVisitor;
import io.github.coalangsoft.visitfx.ScrollPaneContentVisitor;
import io.github.coalangsoft.visitfx.TabPaneContentVisitor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.stage.Window;
import javafx.util.Callback;

@VisitorInfo(Node.class)
public class DragDropFX extends Visitor{
	
	private ArrayList<Class<?>> ignore;

	{
		addFunction(Window.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				Window window = (Window) p;

				//Handle the current content of the window, as well as new content
				DragDropFX.this.handle(window.getScene());
				window.sceneProperty().addListener((v,o,n) -> {
					DragDropFX.this.handle(window.getScene());
				});

				return null;
			}
		});
		addFunction(Scene.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				Scene scene = (Scene) p;

				//Handle the current content of the scene, as well as new content
				DragDropFX.this.handle(scene.getRoot());
				scene.rootProperty().addListener((v,o,n) -> {
					DragDropFX.this.handle(n);
				});

				return null;
			}
		});
		addFunction(Accordion.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}

				Accordion accordion = (Accordion) p;
				DnDPrepare.modifiableNodeList(DragDropFX.this, accordion.getPanes());
				return null;
			}
		});
		addFunction(TabPane.class, new TabPaneContentVisitor(this));
		addFunction(Parent.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				Parent parent = (Parent) p;

				//Handle the current content of the parent, as well as new content
				DnDPrepare.modifiableNodeList(DragDropFX.this, parent.getChildrenUnmodifiable());
				return null;
			}
		});
		addFunction(TitledPane.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				TitledPane pane = (TitledPane) p;

				//Handle the current content of the titled pane, as well as new content
				DragDropFX.this.handle(pane.getContent());
				pane.contentProperty().addListener((v,o,n) -> {
					if(n != null) DragDropFX.this.handle(n);
				});

				return null;
			}
		});
		addFunction(TextInputControl.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				TextInputControl c = (TextInputControl) p;
				DnDPrepare.textinput(c);
				return null;
			}
		});
		addFunction(Labeled.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				Labeled c = (Labeled) p;
				DnDPrepare.labeled(c);
				return null;
			}
		});
		addFunction(ImageView.class, new Func<Object, Void>(){
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				ImageView c = (ImageView) p;
				DnDPrepare.gfx(c);
				return null;
			}
		});
		addFunction(Chart.class, new Func<Object, Void>(){
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				Chart c = (Chart) p;
				DnDPrepare.gfx(c);
				return null;
			}
		});
		addFunction(MediaView.class, new Func<Object, Void>(){
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				MediaView c = (MediaView) p;
				DnDPrepare.mediaview(c);
				return null;
			}
		});
		addFunction(TableView.class, new Func<Object, Void>(){
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				final TableView<?> t = (TableView<?>) p;
				DnDPrepare.tableColumns(DragDropFX.this, t.getColumns());
				t.getColumns().addListener(new ListChangeListener<TableColumn<?,?>>(){

					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends TableColumn<?, ?>> arg0) {
						while(arg0.next()){
							if(arg0.wasAdded()){
								DnDPrepare.tableColumns(DragDropFX.this, arg0.getAddedSubList());
							}
						}
					}
					
				});
				return null;
			}
		});
		addFunction(ScrollPane.class, new ScrollPaneContentVisitor(this));
		addFunction(TabPane.class, new Func<Object, Void>(){
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				TabPane c = (TabPane) p;
				DnDPrepare.tabPane(c);
				return null;
			}
		});
		addFunction(ToolBar.class, new Func<Object, Void>(){
			@Override
			public Void call(Object p) {
				if(isIgnored(p)){return null;}
				ToolBar c = (ToolBar) p;
				DnDPrepare.toolBar(c);
				return null;
			}
		});
		
		ignore = new ArrayList<Class<?>>();
	}
	
	public void ignoreType(Object o){
		if(o instanceof Class){
			ignore.add((Class<?>) o);
		}else{
			try {
				Class<?> coaReflect = Class.forName("io.github.coalangsoft.reflect.Clss");
				if(coaReflect.isInstance(o)){
					ignore.add(
						(Class<?>) coaReflect.getField("base").get(o)
					);
				}
			} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				throw new RuntimeException("Unexpected ignore type description: " + o);
			}
		}
	}
	
	private boolean isIgnored(Object p) {
		for(int i = 0; i < ignore.size(); i++){
			if(ignore.get(i).isInstance(p)){
				return true;
			}
		}
		return false;
	}
	
}

