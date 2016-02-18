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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.lang3.tuple.Pair;
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
public class InvokeStaticRemapTransformer implements IClassTransformer  {
    
    private final Set<String> remapClasses = new HashSet<String>();
    private final Set<String> remapLocations = new HashSet<String>();
    private final List<InvokeStaticRemap> remaps = new ArrayList<InvokeStaticRemap>();
    
    private class InvokeStaticRemap {
        String className;
        String methodName;
        String oldOwner;
        String oldName;
        String newOwner;
        String newName;
        String desc;
    }
    
    protected final void remap(String location, String oldCall, String newCall, String desc) {
        InvokeStaticRemap remap = new InvokeStaticRemap();
        int index;
        
        index = location.lastIndexOf('/');
        if (index == -1) {
            throw new IllegalArgumentException("Invalid location format: " + location);
        }
        
        remap.className = location.substring(0, index).replace('/', '.');
        remap.methodName = location.substring(index + 1);
        
        index = oldCall.lastIndexOf('/');
        if (index == -1) {
            throw new IllegalArgumentException("Invalid old call format: " + oldCall);
        }
        
        remap.oldOwner = oldCall.substring(0, index);
        remap.oldName = oldCall.substring(index + 1);
        
        index = newCall.lastIndexOf('/');
        if (index == -1) {
            throw new IllegalArgumentException("Invalid new call format: " + newCall);
        }
        
        remap.newOwner = newCall.substring(0, index);
        remap.newName = newCall.substring(index + 1);
        
        remap.desc = desc;
        
        remaps.add(remap);
        remapClasses.add(remap.className);
        remapLocations.add(location.replace('/', '.'));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        // filter untouched classes
        if (!remapClasses.contains(name)) {
            return data;
        }
        
        final String className = name;
        
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                final String methodName = name;
                
                // filter untouched methods
                if (!remapLocations.contains(className + ".*") && !remapLocations.contains(className + "." + methodName)) {
                    return methodVisitor;
                }

                return new InstructionAdapter(api, methodVisitor) {
                    @Override
                    public void invokestatic(String owner, String name, String desc, boolean itf) {
                        // remap static calls
                        for (InvokeStaticRemap remap : remaps) {
                            if (
                                !remap.className.equals(className) ||
                                !(remap.methodName.equals("*") || remap.methodName.equals(methodName)) ||
                                !remap.oldOwner.equals(owner) || 
                                !remap.oldName.equals(name) ||
                                !remap.desc.equals(desc)
                            ) {
                                continue;
                            }
                                                        
                            owner = remap.newOwner;
                            name = remap.newName;
                            
                            System.out.printf("Remapped %s/%s %s to %s/%s %s\n",
                                    remap.oldOwner, remap.oldName, desc,
                                    remap.newOwner, remap.newName, desc);
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
