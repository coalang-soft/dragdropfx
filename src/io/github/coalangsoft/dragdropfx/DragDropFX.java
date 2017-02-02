package io.github.coalangsoft.dragdropfx;

import io.github.coalangsoft.lib.data.Func;
import io.github.coalangsoft.visit.Visitor;
import io.github.coalangsoft.visit.VisitorInfo;
import io.github.coalangsoft.visitfx.ParentChildrenVisitor;
import io.github.coalangsoft.visitfx.TabPaneContentVisitor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

@VisitorInfo(Node.class)
public class DragDropFX extends Visitor{
	
	{
		addFunction(TabPane.class, new TabPaneContentVisitor(this));
		addFunction(Parent.class, new ParentChildrenVisitor(this));
		addFunction(TextInputControl.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				TextInputControl c = (TextInputControl) p;
				DnDPrepare.textinput(c);
				return null;
			}
		});
		addFunction(Labeled.class, new Func<Object, Void>() {
			@Override
			public Void call(Object p) {
				Labeled c = (Labeled) p;
				DnDPrepare.labeled(c);
				return null;
			}
		});
		addFunction(ImageView.class, new Func<Object, Void>(){
			@Override
			public Void call(Object p) {
				ImageView c = (ImageView) p;
				DnDPrepare.imageview(c);
				return null;
			}
		});
		addFunction(MediaView.class, new Func<Object, Void>(){
			@Override
			public Void call(Object p) {
				MediaView c = (MediaView) p;
				DnDPrepare.mediaview(c);
				return null;
			}
		});
	}
	
}
