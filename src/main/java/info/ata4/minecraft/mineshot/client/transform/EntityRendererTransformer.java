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
public class EntityRendererTransformer implements IClassTransformer {
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        if (!transformedName.equals("net.minecraft.client.renderer.EntityRenderer")) {
            return data;
        }
        
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                return new InstructionAdapter(api, methodVisitor) {

                    @Override
                    public void invokestatic(String owner, String name, String desc, boolean itf) {
                        // redirect Project.gluPerspective
                        if (owner.equals("org/lwjgl/util/glu/Project") && name.equals("gluPerspective")) {
                            owner = "info/ata4/minecraft/mineshot/client/Project";
                        }
                        
                        // redirect GlStateManager.ortho
                        if (owner.equals("net/minecraft/client/renderer/GlStateManager") && name.equals("ortho")) {
                            name = "glOrtho";
                            owner = "info/ata4/minecraft/mineshot/client/Project";
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
