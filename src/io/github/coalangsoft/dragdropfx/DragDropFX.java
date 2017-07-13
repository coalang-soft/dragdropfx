package io.github.coalangsoft.dragdropfx;

import java.util.ArrayList;

import io.github.coalangsoft.lib.data.Func;
import io.github.coalangsoft.visit.Visitor;
import io.github.coalangsoft.visit.VisitorInfo;
import io.github.coalangsoft.visitfx.ParentChildrenVisitor;
import io.github.coalangsoft.visitfx.ScrollPaneContentVisitor;
import io.github.coalangsoft.visitfx.TabPaneContentVisitor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.Chart;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

@VisitorInfo(Node.class)
public class DragDropFX extends Visitor{
	
	private ArrayList<Class<?>> ignore;

	{
		addFunction(TabPane.class, new TabPaneContentVisitor(this));
		addFunction(Parent.class, new ParentChildrenVisitor(this));
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
		addFunction(ScrollPane.class, new ScrollPaneContentVisitor(this));
		
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

