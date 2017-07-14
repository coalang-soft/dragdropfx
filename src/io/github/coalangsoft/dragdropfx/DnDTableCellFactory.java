package io.github.coalangsoft.dragdropfx;

import java.util.List;

import io.github.coalangsoft.visit.Visitor;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class DnDTableCellFactory<A> implements Callback<TableColumn<A,?>, TableCell<A,?>>{

	private Callback<TableColumn<A,?>, TableCell<A, ?>> f;
	private Visitor v;

	public DnDTableCellFactory(Visitor v, Callback<TableColumn<A,?>, TableCell<A,?>> cellFactory) {
		this.v = v;
		this.f = cellFactory;
	}

	@Override
	public TableCell<A,?> call(TableColumn<A,?> arg0) {
		TableCell<A,?> c = f.call(arg0);
		v.handle(c);
		
		ObservableList<Node> l = c.getChildrenUnmodifiable();
		for(int i = 0; i < l.size(); i++){
			v.handle(l.get(i));
		}
		l.addListener(new ListChangeListener<Node>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Node> arg0) {
				while(arg0.next()){
					if(arg0.wasAdded()){
						List<? extends Node> li = arg0.getAddedSubList();
						for(int i = 0; i < li.size(); i++){
							v.handle(li.get(i));
						}
					}
				}
			}
			
		});
		return c;
	}

}
