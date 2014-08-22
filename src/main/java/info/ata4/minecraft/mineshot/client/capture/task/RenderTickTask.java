/*
 ** 2014 August 20
 **
 ** The author disclaims copyright to this source code. In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.capture.task;

import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public interface RenderTickTask {
    
    /**
     * Called on every frame to update the task.
     * 
     * @param evt current render tick event
     * @return true if the task is done and can be disposed or false if it should
     *         continue to be updated.
     * @throws Exception 
     */
    boolean onRenderTick(RenderTickEvent evt) throws Exception;
}
