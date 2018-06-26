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

## Usage

To capture a big screenshot, simply press **F9**. The key can be configured in the key bindings menu.

During capture, the game may be unresponsive for a few seconds, depending on how big the screenshot is.

To change the capturing size, go to "Mod Options" in the main menu, select Mineshot in the mod list and click on "Config".
The absolute maximum is 65,535 x 65,535 pixels, which is also the maximum size of a Targa image file. However, you most likely want to choose something smaller, since files with these resolutions are extremely big (several gigabytes) and difficult to open, yet alone edit, with most image editors.
Also note that when tiled rendering is disabled, the maximum screenshot size heavily depends on the available graphics card memory. Setting it too high will crash OpenGL together with the JVM, so it's highly recommended to stick to 8K (7,680 x 4,320) and below when tiled rendering is disabled.

If you use lots of shaders, it's not recommended to enable tiled rendering, since it may cause seams between the individual tiles or other major glitches.

To enable or disable the orthographic camera press **F7**. There are multiple settings which are configured by default as follows. There is a modifier key which is defined as *Control* on Windows and Linux and as *Command* on Mac.

 - Semicolon: Let the camera follow the view of the player
 - Apostrophe: Switch [clipping](https://en.wikipedia.org/wiki/Clipping_(computer_graphics)) distance
 - Left Bracket: Select next preset
 - Modifier + Left Bracket: Select previous preset
 
The camera view can be further configured using these keys. They can be switched to work in steps by holding the modifier key at the same time:

 - Right Bracket: Zoom in
 - Backslash: Zoom out
 - Left: Rotate left
 - Right: Rotate right
 - Up: Rotate up
 - Down: Rotate down

## Download

Compiled Jars can be found on the [releases page](https://github.com/ata4/mineshot/releases).
