package com.everyware.posix.api;

public interface PosixPointer {
	PosixPointer add(int off);

	byte getI8();

	short getI16();

	int getI32();

	long getI64();

	void setI8(byte val);

	void setI16(short val);

	void setI32(int val);

	void setI64(long val);

	default long getAddress() {
		throw new AssertionError("not implemented");
	}

	default long size() {
		throw new AssertionError("not implemented");
	}

	default boolean hasMemory(@SuppressWarnings("unused") int size) {
		return false;
	}

	default byte[] getMemory() {
		throw new AssertionError("not implemented");
	}

	default int getOffset() {
		throw new AssertionError("not implemented");
	}

	default String getName() {
		return "[posix-pointer]";
	}
}