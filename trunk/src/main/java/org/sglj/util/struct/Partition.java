package org.sglj.util.struct;

import java.util.Set;

public interface Partition<E> extends Set<E> {
	DisjointSet<E> getOwner();
}
