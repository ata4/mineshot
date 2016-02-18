/*
** 2016 February 17
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
public class FrustumTransformer extends InvokeStaticRemapTransformer {
    
    public FrustumTransformer() {
        remap(
            "net/minecraft/client/renderer/culling/Frustum/<init>",
            "net/minecraft/client/renderer/culling/ClippingHelperImpl/getInstance",
            "info/ata4/minecraft/mineshot/client/wrapper/ToggleableClippingHelper/getInstanceWrapper",
            "()Lnet/minecraft/client/renderer/culling/ClippingHelper;"
        );
        
        remap(
            "bic/<init>",
            "bib/a",
            "info/ata4/minecraft/mineshot/client/wrapper/ToggleableClippingHelper/getInstanceWrapper",
            "()Lbid;"
        );
    }
}
