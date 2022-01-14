# DragDropFX - Add standard drag and drop actions to any JavaFX application.
Drag and Drop is a realy useful feature. But in JavaFX there is no built-in way to add the standard drag and drop actions to things like
TextFields, ImageViews and Labels. This is what DragDropFX was made for.

## Clone this project
Run the following commands from a terminal:
```
git clone https://www.github.com/coalang-soft/dragdropfx
cd ./dragdropfx
git submodule init
git submodule update
cd ..
```

## Add Drag and Drop
To add the default drag and drop actions (drag text into a text field, drag images etc.), you can do something like this:
```
//Create your GUI
Node gui = ...

//Add drag and drop
new DragDropFX().handle(gui);
```