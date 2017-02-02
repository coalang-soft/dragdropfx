#DragDropFX - Add standard drag and drop actions to any JavaFX application.
Drag and Drop is a realy useful feature. But in JavaFX there is no built-in way to add the standard drag and drop actions to things like
TextFields, ImageViews and Labels. This is what DragDropFX was made for.

#Add Drag and Drop
To add the default drag and drop actions (drag text into a text field, drag images etc.), you can do something like this:
```
//Create your GUI
Node gui = ...

//Add drag and drop
new DragDropFX().handle(gui);
```
