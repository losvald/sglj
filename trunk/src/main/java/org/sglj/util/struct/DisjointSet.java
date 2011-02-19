package org.sglj.util.struct;

import java.util.Set;

public interface DisjointSet<E> extends Set<E> {
	Partition<E> find(E element);
	boolean union(E element1, E element2);
}
