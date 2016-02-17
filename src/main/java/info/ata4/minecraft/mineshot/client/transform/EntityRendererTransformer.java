/*
** 2016 February 15
**
** The author disclaims copyright to this source code. In place of
** a legal notice, here is a blessing:
**    May you do good and not evil.
**    May you find forgiveness for yourself and forgive others.
**    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.transform;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class EntityRendererTransformer extends InvokeStaticRemapTransformer {
    
    public EntityRendererTransformer() {
        remap(
            "net/minecraft/client/renderer/EntityRenderer/*",
            "org/lwjgl/util/glu/Project/gluPerspective",
            "info/ata4/minecraft/mineshot/client/wrapper/Projection/perspective",
            "(FFFF)V"
        );
        
        remap(
            "net/minecraft/client/renderer/EntityRenderer/*",
            "net/minecraft/client/renderer/GlStateManager/ortho",
            "info/ata4/minecraft/mineshot/client/wrapper/Projection/ortho",
            "(DDDDDD)V"
        );
        
        remap(
            "bfk/*",
            "org/lwjgl/util/glu/Project/gluPerspective",
            "info/ata4/minecraft/mineshot/client/wrapper/Projection/perspective",
            "(FFFF)V"
        );
        
        remap(
            "bfk/*",
            "bfl/a",
            "info/ata4/minecraft/mineshot/client/wrapper/Projection/ortho",
            "(DDDDDD)V"
        );
    }
}
