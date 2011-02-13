package org.sglj.math;

import org.sglj.math.UnaryPredicate.BooleanUnaryPredicate;
import org.sglj.math.UnaryPredicate.ByteUnaryPredicate;
import org.sglj.math.UnaryPredicate.CharUnaryPredicate;
import org.sglj.math.UnaryPredicate.DoubleUnaryPredicate;
import org.sglj.math.UnaryPredicate.FloatUnaryPredicate;
import org.sglj.math.UnaryPredicate.IntUnaryPredicate;
import org.sglj.math.UnaryPredicate.LongUnaryPredicate;
import org.sglj.math.UnaryPredicate.ShortUnaryPredicate;
import org.sglj.math.UnaryPredicate.TypeUnaryPredicate;

/**
 * Utility class for {@link UnaryPredicate}.
 * 
 * @author Leo Osvald
 *
 */
public final class UnaryPredicates {

	// disable instantiation
	private UnaryPredicates() { }

	//	tmp_file="/tmp/tmp"; xsel > "$tmp_file"; types="Boolean Byte Char Short Long Float Double Type"; prim="boolean byte char short long float double T"; ucase_prim="BOOLEAN BYTE CHAR SHORT LONG FLOAT DOUBLE TYPE"; types_arr=($types); prim_arr=($prim); ucase_prim_arr=($ucase_prim); echo "" > "$tmp_file"2; for ((i=0;i<7;++i)); do t=${types_arr[i]}; p=${prim_arr[i]}; prim_file=/tmp/$p; cat "$tmp_file" | sed 's/int\( \|s\)/'$p'\1/g' | sed 's/Int/'$t'/g' | sed 's/INT/'${ucase_prim_arr[i]}'/g' | sed 's/TypeUnaryPredicate/&<T>/g' >> "$tmp_file"2; echo -e "\n\n" >> "$tmp_file"2; done; cat "$tmp_file"2 | xsel -b

	// BEGIN HAND-CODED

	public static final class TypeBinaryPredicates {
		// disable instantiation
		private TypeBinaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static <T> TypeUnaryPredicate<T> not(
				final TypeUnaryPredicate<T> unaryPredicate) {
			return new TypeUnaryPredicate<T>() {
				@Override
				public boolean holds(T a) {
					return unaryPredicate.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static <T> TypeUnaryPredicate<T> and(
				final TypeUnaryPredicate<T> unaryPredicate1,
				final TypeUnaryPredicate<T> unaryPredicate2) {
			return new TypeUnaryPredicate<T>() {
				@Override
				public boolean holds(T a) {
					return unaryPredicate1.holds(a)
					&& unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static <T> TypeUnaryPredicate<T> or(
				final TypeUnaryPredicate<T> unaryPredicate1,
				final TypeUnaryPredicate<T> unaryPredicate2) {
			return new TypeUnaryPredicate<T>() {
				@Override
				public boolean holds(T a) {
					return unaryPredicate1.holds(a)
					|| unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static <T> TypeUnaryPredicate<T> xor(
				final TypeUnaryPredicate<T> unaryPredicate1,
				final TypeUnaryPredicate<T> unaryPredicate2) {
			return new TypeUnaryPredicate<T>() {
				@Override
				public boolean holds(T a) {
					return unaryPredicate1.holds(a)
					^ unaryPredicate2.holds(a);
				}
			};
		}
	}

	// BEGIN XSEL

	public static final class IntUnaryPredicates {
		// disable instantiation
		private IntUnaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static IntUnaryPredicate not(
				final IntUnaryPredicate unaryPredicate) {
			return new IntUnaryPredicate() {
				@Override
				public boolean holds(int a) {
					return unaryPredicate.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static IntUnaryPredicate and(
				final IntUnaryPredicate unaryPredicate1,
				final IntUnaryPredicate unaryPredicate2) {
			return new IntUnaryPredicate() {
				@Override
				public boolean holds(int a) {
					return unaryPredicate1.holds(a)
					&& unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static IntUnaryPredicate or(
				final IntUnaryPredicate unaryPredicate1,
				final IntUnaryPredicate unaryPredicate2) {
			return new IntUnaryPredicate() {
				@Override
				public boolean holds(int a) {
					return unaryPredicate1.holds(a)
					|| unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static IntUnaryPredicate xor(
				final IntUnaryPredicate unaryPredicate1,
				final IntUnaryPredicate unaryPredicate2) {
			return new IntUnaryPredicate() {
				@Override
				public boolean holds(int a) {
					return unaryPredicate1.holds(a)
					^ unaryPredicate2.holds(a);
				}
			};
		}
	}

	// END XSEL

	// END HAND-CODED


	public static final class BooleanUnaryPredicates {
		// disable instantiation
		private BooleanUnaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static BooleanUnaryPredicate not(
				final BooleanUnaryPredicate unaryPredicate) {
			return new BooleanUnaryPredicate() {
				@Override
				public boolean holds(boolean a) {
					return unaryPredicate.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static BooleanUnaryPredicate and(
				final BooleanUnaryPredicate unaryPredicate1,
				final BooleanUnaryPredicate unaryPredicate2) {
			return new BooleanUnaryPredicate() {
				@Override
				public boolean holds(boolean a) {
					return unaryPredicate1.holds(a)
					&& unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static BooleanUnaryPredicate or(
				final BooleanUnaryPredicate unaryPredicate1,
				final BooleanUnaryPredicate unaryPredicate2) {
			return new BooleanUnaryPredicate() {
				@Override
				public boolean holds(boolean a) {
					return unaryPredicate1.holds(a)
					|| unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static BooleanUnaryPredicate xor(
				final BooleanUnaryPredicate unaryPredicate1,
				final BooleanUnaryPredicate unaryPredicate2) {
			return new BooleanUnaryPredicate() {
				@Override
				public boolean holds(boolean a) {
					return unaryPredicate1.holds(a)
					^ unaryPredicate2.holds(a);
				}
			};
		}
	}


	public static final class ByteUnaryPredicates {
		// disable instantiation
		private ByteUnaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static ByteUnaryPredicate not(
				final ByteUnaryPredicate unaryPredicate) {
			return new ByteUnaryPredicate() {
				@Override
				public boolean holds(byte a) {
					return unaryPredicate.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static ByteUnaryPredicate and(
				final ByteUnaryPredicate unaryPredicate1,
				final ByteUnaryPredicate unaryPredicate2) {
			return new ByteUnaryPredicate() {
				@Override
				public boolean holds(byte a) {
					return unaryPredicate1.holds(a)
					&& unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static ByteUnaryPredicate or(
				final ByteUnaryPredicate unaryPredicate1,
				final ByteUnaryPredicate unaryPredicate2) {
			return new ByteUnaryPredicate() {
				@Override
				public boolean holds(byte a) {
					return unaryPredicate1.holds(a)
					|| unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static ByteUnaryPredicate xor(
				final ByteUnaryPredicate unaryPredicate1,
				final ByteUnaryPredicate unaryPredicate2) {
			return new ByteUnaryPredicate() {
				@Override
				public boolean holds(byte a) {
					return unaryPredicate1.holds(a)
					^ unaryPredicate2.holds(a);
				}
			};
		}
	}


	public static final class CharUnaryPredicates {
		// disable instantiation
		private CharUnaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static CharUnaryPredicate not(
				final CharUnaryPredicate unaryPredicate) {
			return new CharUnaryPredicate() {
				@Override
				public boolean holds(char a) {
					return unaryPredicate.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static CharUnaryPredicate and(
				final CharUnaryPredicate unaryPredicate1,
				final CharUnaryPredicate unaryPredicate2) {
			return new CharUnaryPredicate() {
				@Override
				public boolean holds(char a) {
					return unaryPredicate1.holds(a)
					&& unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static CharUnaryPredicate or(
				final CharUnaryPredicate unaryPredicate1,
				final CharUnaryPredicate unaryPredicate2) {
			return new CharUnaryPredicate() {
				@Override
				public boolean holds(char a) {
					return unaryPredicate1.holds(a)
					|| unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static CharUnaryPredicate xor(
				final CharUnaryPredicate unaryPredicate1,
				final CharUnaryPredicate unaryPredicate2) {
			return new CharUnaryPredicate() {
				@Override
				public boolean holds(char a) {
					return unaryPredicate1.holds(a)
					^ unaryPredicate2.holds(a);
				}
			};
		}
	}


	public static final class ShortUnaryPredicates {
		// disable instantiation
		private ShortUnaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static ShortUnaryPredicate not(
				final ShortUnaryPredicate unaryPredicate) {
			return new ShortUnaryPredicate() {
				@Override
				public boolean holds(short a) {
					return unaryPredicate.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static ShortUnaryPredicate and(
				final ShortUnaryPredicate unaryPredicate1,
				final ShortUnaryPredicate unaryPredicate2) {
			return new ShortUnaryPredicate() {
				@Override
				public boolean holds(short a) {
					return unaryPredicate1.holds(a)
					&& unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static ShortUnaryPredicate or(
				final ShortUnaryPredicate unaryPredicate1,
				final ShortUnaryPredicate unaryPredicate2) {
			return new ShortUnaryPredicate() {
				@Override
				public boolean holds(short a) {
					return unaryPredicate1.holds(a)
					|| unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static ShortUnaryPredicate xor(
				final ShortUnaryPredicate unaryPredicate1,
				final ShortUnaryPredicate unaryPredicate2) {
			return new ShortUnaryPredicate() {
				@Override
				public boolean holds(short a) {
					return unaryPredicate1.holds(a)
					^ unaryPredicate2.holds(a);
				}
			};
		}
	}


	public static final class LongUnaryPredicates {
		// disable instantiation
		private LongUnaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static LongUnaryPredicate not(
				final LongUnaryPredicate unaryPredicate) {
			return new LongUnaryPredicate() {
				@Override
				public boolean holds(long a) {
					return unaryPredicate.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static LongUnaryPredicate and(
				final LongUnaryPredicate unaryPredicate1,
				final LongUnaryPredicate unaryPredicate2) {
			return new LongUnaryPredicate() {
				@Override
				public boolean holds(long a) {
					return unaryPredicate1.holds(a)
					&& unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static LongUnaryPredicate or(
				final LongUnaryPredicate unaryPredicate1,
				final LongUnaryPredicate unaryPredicate2) {
			return new LongUnaryPredicate() {
				@Override
				public boolean holds(long a) {
					return unaryPredicate1.holds(a)
					|| unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static LongUnaryPredicate xor(
				final LongUnaryPredicate unaryPredicate1,
				final LongUnaryPredicate unaryPredicate2) {
			return new LongUnaryPredicate() {
				@Override
				public boolean holds(long a) {
					return unaryPredicate1.holds(a)
					^ unaryPredicate2.holds(a);
				}
			};
		}
	}


	public static final class FloatUnaryPredicates {
		// disable instantiation
		private FloatUnaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static FloatUnaryPredicate not(
				final FloatUnaryPredicate unaryPredicate) {
			return new FloatUnaryPredicate() {
				@Override
				public boolean holds(float a) {
					return unaryPredicate.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static FloatUnaryPredicate and(
				final FloatUnaryPredicate unaryPredicate1,
				final FloatUnaryPredicate unaryPredicate2) {
			return new FloatUnaryPredicate() {
				@Override
				public boolean holds(float a) {
					return unaryPredicate1.holds(a)
					&& unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static FloatUnaryPredicate or(
				final FloatUnaryPredicate unaryPredicate1,
				final FloatUnaryPredicate unaryPredicate2) {
			return new FloatUnaryPredicate() {
				@Override
				public boolean holds(float a) {
					return unaryPredicate1.holds(a)
					|| unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static FloatUnaryPredicate xor(
				final FloatUnaryPredicate unaryPredicate1,
				final FloatUnaryPredicate unaryPredicate2) {
			return new FloatUnaryPredicate() {
				@Override
				public boolean holds(float a) {
					return unaryPredicate1.holds(a)
					^ unaryPredicate2.holds(a);
				}
			};
		}
	}

	
	public static final class DoubleUnaryPredicates {
		// disable instantiation
		private DoubleUnaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static DoubleUnaryPredicate not(
				final DoubleUnaryPredicate unaryPredicate) {
			return new DoubleUnaryPredicate() {
				@Override
				public boolean holds(double a) {
					return unaryPredicate.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static DoubleUnaryPredicate and(
				final DoubleUnaryPredicate unaryPredicate1,
				final DoubleUnaryPredicate unaryPredicate2) {
			return new DoubleUnaryPredicate() {
				@Override
				public boolean holds(double a) {
					return unaryPredicate1.holds(a)
					&& unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static DoubleUnaryPredicate or(
				final DoubleUnaryPredicate unaryPredicate1,
				final DoubleUnaryPredicate unaryPredicate2) {
			return new DoubleUnaryPredicate() {
				@Override
				public boolean holds(double a) {
					return unaryPredicate1.holds(a)
					|| unaryPredicate2.holds(a);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param unaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static DoubleUnaryPredicate xor(
				final DoubleUnaryPredicate unaryPredicate1,
				final DoubleUnaryPredicate unaryPredicate2) {
			return new DoubleUnaryPredicate() {
				@Override
				public boolean holds(double a) {
					return unaryPredicate1.holds(a)
					^ unaryPredicate2.holds(a);
				}
			};
		}
	}
}