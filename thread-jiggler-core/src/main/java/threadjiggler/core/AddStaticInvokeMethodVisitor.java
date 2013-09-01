package threadjiggler.core;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Add a invocation of a static method after selected instructions.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
 class AddStaticInvokeMethodVisitor extends MethodVisitor implements Opcodes {

	private final String owner;
	private final String name;
	private final String desc;
	private final boolean debug;

	public AddStaticInvokeMethodVisitor(int api, MethodVisitor methodVisitor, String owner, String name, String desc, boolean debug) {
		super(api, methodVisitor);
		this.owner = owner;
		this.name = name;
		this.desc = desc;
		this.debug = debug;
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		if (debug) {
			System.out.printf("FieldInsn %s,%s,%s,%s\n", opcode, owner, name, desc);
		}

		super.visitFieldInsn(opcode, owner, name, desc);
		super.visitMethodInsn(INVOKESTATIC, this.owner, this.name, this.desc);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		if (debug) {
			System.out.printf("MethodInsn %s,%s,%s,%s\n", opcode, owner, name, desc);
		}
		super.visitMethodInsn(opcode, owner, name, desc);
		super.visitMethodInsn(INVOKESTATIC, this.owner, this.name, this.desc);
	}
}
