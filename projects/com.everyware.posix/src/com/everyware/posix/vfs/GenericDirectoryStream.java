package com.everyware.posix.vfs;

import java.util.Iterator;

import com.everyware.posix.api.Dirent;
import com.everyware.posix.api.PosixException;

public class GenericDirectoryStream extends VFSDirectoryStream {
	public GenericDirectoryStream(VFSDirectory dir, Iterator<Dirent> iterator) {
		super(dir, iterator);
	}

	@Override
	public int close() throws PosixException {
		return 0;
	}
}
