package threadjiggler.core;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * Add an invocation of a static method between certain instructions.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
 class AddStaticInvokeClassVisitor extends ClassVisitor {
	private final String owner;
	private final String name;
	private final String desc;
	private final boolean debug;

	public AddStaticInvokeClassVisitor(int api, ClassVisitor classVisitor, String owner, String name, String desc, boolean debug) {
		super(api, classVisitor);
		this.owner = owner;
		this.name = name;
		this.desc = desc;
		this.debug = debug;
	}


	@Override
	public MethodVisitor visitMethod(int opcode, String s, String s2, String s3, String[] strings) {

		return new AddStaticInvokeMethodVisitor(super.api, super.visitMethod(opcode, s, s2, s3, strings), owner, name, desc, debug);
	}

}
