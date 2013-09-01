package threadjiggler.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class JigglingClassLoader extends ClassLoader {

	private boolean debug = Boolean.getBoolean("jiggler.debug");
	private String pattern = System.getProperty("jiggler.name", null);

	public JigglingClassLoader(ClassLoader parent) {
		super(parent);
	}

	public JigglingClassLoader(ClassLoader parent, String pattern) {
		super(parent);
		this.pattern = pattern;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (needsModifying(name)) {
			return findClass(name);
		} else {
			return super.loadClass(name);
		}
	}

	private byte[] modifyClass(InputStream name) throws IOException {
		final ClassReader cr = new ClassReader(name);
		final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cr.accept(new AddStaticInvokeClassVisitor(Opcodes.ASM4, cw, "java/lang/Thread", "yield", "()V", debug), 0);

		return cw.toByteArray();
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (needsModifying(name)) {
			System.out.println("jiggling " + name);
			try {
				InputStream classData = getResourceAsStream(name.replace('.', '/') + ".class");
				if (classData == null) {
					throw new ClassNotFoundException("class " + name + " not found");
				}
				byte[] bytes = modifyClass(classData);
				return defineClass(name, bytes);
			} catch (IOException io) {
				throw new ClassNotFoundException("failed to load " + name, io);
			}
		} else {
			return super.findClass(name);
		}
	}

	private boolean needsModifying(String name) {
		return name.matches(pattern);
	}

	private Class<?> defineClass(String name, byte[] bytes) {
		return defineClass(name, bytes, 0, bytes.length);
	}
}
