package threadjiggler.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;

/**
 * Loads classes but interleaves {@link Thread#yield()} into the bytecode so that the code will produce artificial
 * threading issues.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class JigglingClassLoader extends ClassLoader {

	/** Print debugging output. */
	private boolean debug = Boolean.getBoolean("jiggler.debug");
	/** Which classes should be jiggled. */
	private String pattern = System.getProperty("jiggler.pattern", null);

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

	private byte[] modifyClass(InputStream in) throws IOException {
		final ClassReader cr = new ClassReader(in);
		final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cr.accept(new AddStaticInvokeClassVisitor(Opcodes.ASM4, cw, "java/lang/Thread", "yield", "()V", debug), 0);
		return cw.toByteArray();
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		InputStream classData = getResourceAsStream(name.replace('.', '/') + ".class");
		if (classData == null) {
			throw new ClassNotFoundException("class " + name + " not found");
		}
		try {
			if (needsModifying(name)) {
				System.out.println("jiggling " + name);
				return defineClass(name, modifyClass(classData));
			} else {
				return defineClass(name, unmodifiedClass(classData));
			}
		} catch (IOException io) {
			throw new ClassNotFoundException("failed to load " + name, io);
		}
	}

	private byte[] unmodifiedClass(InputStream is) throws IOException {
		byte[] b = new byte[is.available()];
		int len = 0;
		while (true) {
			int n = is.read(b, len, b.length - len);
			if (n == -1) {
				if (len < b.length) {
					byte[] c = new byte[len];
					System.arraycopy(b, 0, c, 0, len);
					b = c;
				}
				return b;
			}
			len += n;
			if (len == b.length) {
				int last = is.read();
				if (last < 0) {
					return b;
				}
				byte[] c = new byte[b.length + 1000];
				System.arraycopy(b, 0, c, 0, len);
				c[len++] = (byte) last;
				b = c;
			}
		}
	}

	private boolean needsModifying(String name) {
		return name.matches(pattern);
	}

	private Class<?> defineClass(String name, byte[] bytes) {
		return defineClass(name, bytes, 0, bytes.length);
	}
}
