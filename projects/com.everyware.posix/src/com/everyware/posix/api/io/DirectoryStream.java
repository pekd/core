package com.everyware.posix.api.io;

import com.everyware.posix.api.Errno;
import com.everyware.posix.api.PosixException;
import com.everyware.posix.api.PosixPointer;

public abstract class DirectoryStream extends Stream {
	@Override
	public int read(byte[] buf, int offset, int length) throws PosixException {
		throw new PosixException(Errno.EISDIR);
	}

	@Override
	public int write(byte[] buf, int offset, int length) throws PosixException {
		throw new PosixException(Errno.EISDIR);
	}

	@Override
	public int pread(byte[] buf, int offset, int length, long fileOffset) throws PosixException {
		throw new PosixException(Errno.EISDIR);
	}

	@Override
	public int pwrite(byte[] buf, int offset, int length, long fileOffset) throws PosixException {
		throw new PosixException(Errno.EISDIR);
	}

	@Override
	public void ftruncate(long size) throws PosixException {
		throw new PosixException(Errno.EINVAL);
	}

	public abstract long getdents(PosixPointer ptr, long count, int size);
}
