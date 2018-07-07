Mineshot
========

Mineshot is basically a revamped version of Notch's huge screenshot function that was available in Minecraft Beta 1.2 to 1.4, which allowed you to create extremely high resolution screenshots.

In Mineshot, this is archived either by combining multiple smaller screeenshot tiles to one large image or by off-screen rendering the scene directly to a file.

There's also a build-in orthographic camera, which allows you to create nice high-res isometric screenshots directly in-game (singleplayer only).

Some scenarios and usages for such images could be:

 - High quality wallpapers
 - Poster printing
 - Overview images
 - Hardware stress test
 - Mob and block images for the Minecraft Wiki
 - ... you name it

## Huge Screenshot

To capture a huge screenshot, simply press **F9**. The key can be configured in the key bindings menu.

During capture, the game may be unresponsive for a few seconds, depending on how big the screenshot is.

To change the capturing size, go to "Mod Options" in the main menu, select Mineshot in the mod list and click on "Config".
The absolute maximum is 65,535 x 65,535 pixels, which is also the maximum size of a Targa image file. However, you most likely want to choose something smaller, since files with these resolutions are extremely big (several gigabytes) and difficult to open, yet alone edit, with most image editors.
Also note that when tiled rendering is disabled, the maximum screenshot size heavily depends on the available graphics card memory. Setting it too high will crash OpenGL together with the JVM, so it's highly recommended to stick to 8K (7,680 x 4,320) and below when tiled rendering is disabled.

If you use lots of shaders, it's not recommended to enable tiled rendering, since it may cause seams between the individual tiles or other major glitches.

## Orthographic Camera

To enable or disable the orthographic camera press **F7**. There are multiple settings which are by default configured as follows. *Note:* Instead of control the command key is used on Mac.

 - Control + F7: Reset orthographic camera
 - Backslash: Select next preset
 - Control + Backslash: Select previous preset

The following controls can be switched to work in steps by holding down the control key at the same time:

 - Right Bracket: Zoom in
 - Left Bracket: Zoom out
 - Left: Rotate left
 - Right: Rotate right
 - Up: Rotate up
 - Down: Rotate down
 
A major part of the orthographic camera is its GUI which allows for very precise configuartion and has additional options available. The GUI is launched by pressing **F8**.
 
Settings can be configured through input fields available in two different modes, either by using sliders or by typing values into text boxes. The mode can be switched using the arrow buttons on top.

Each input field has two buttons to increase or decrease the current value in steps. The default step value can be altered by holding certain keys while clicking on a button. The current step value is displayed as a tooltip.

 - Default: *1*
 - Control: *0.1*
 - Shift: *10*
 - Control + Shift: *0.01*
 - Alt: *100*
 - Control + Alt: *0.001*
 
The GUI also includes a focus mode which is activated by clicking a button featuring a magnifying glass next to any input field. This action will make everything apart from the corresponding input field vanish, providing a clear view of the current game setting.

On the bottom of the GUI are two additional options for configuring the orthographic camera:

 - Follow View: Sets camera rotation to follow the view of the player
 - Clipping: Switch [clipping](https://en.wikipedia.org/wiki/Clipping_(computer_graphics)) distance

## Download

Compiled Jars can be found on the [releases page](https://github.com/ata4/mineshot/releases).
