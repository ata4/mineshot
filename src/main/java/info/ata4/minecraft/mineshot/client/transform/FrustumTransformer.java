/*
** 2016 Februar 17
**
** The author disclaims copyright to this source code. In place of
** a legal notice, here is a blessing:
**    May you do good and not evil.
**    May you find forgiveness for yourself and forgive others.
**    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.transform;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.InstructionAdapter;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class FrustumTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        // modify Frustum class only
        if (!transformedName.equals("net.minecraft.client.renderer.culling.Frustum")) {
            return data;
        }
        
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                
                // modify constructor only
                if (!name.equals("<init>")) {
                    return methodVisitor;
                }

                // redirect getInstance to custom ClippingHelper implementation
                return new InstructionAdapter(api, methodVisitor) {
                    @Override
                    public void invokestatic(String owner, String name, String desc, boolean itf) {                        
                        if (owner.equals("net/minecraft/client/renderer/culling/ClippingHelperImpl") && name.equals("getInstance")) {
                            name = "getInstanceWrapper";
                            owner = "info/ata4/minecraft/mineshot/client/wrapper/ToggleableClippingHelper";
                        }
                        
                        super.invokestatic(owner, name, desc, itf);
                    }
                };
            }
        };
        
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
    
}
