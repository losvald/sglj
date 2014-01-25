/*
 * BinaryPredicates.java
 *
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 *
 * This file is part of YOUR PROGRAM NAME.
 * 
 * YOUR PROGRAM NAME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * YOUR PROGRAM NAME is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with YOUR PROGRAM NAME. If not, see <http://www.gnu.org/licenses/>.
 */

package org.sglj.math;

import org.sglj.math.BinaryPredicate.BooleanBinaryPredicate;
import org.sglj.math.BinaryPredicate.ByteBinaryPredicate;
import org.sglj.math.BinaryPredicate.CharBinaryPredicate;
import org.sglj.math.BinaryPredicate.DoubleBinaryPredicate;
import org.sglj.math.BinaryPredicate.FloatBinaryPredicate;
import org.sglj.math.BinaryPredicate.IntBinaryPredicate;
import org.sglj.math.BinaryPredicate.LongBinaryPredicate;
import org.sglj.math.BinaryPredicate.ShortBinaryPredicate;
import org.sglj.math.BinaryPredicate.TypeBinaryPredicate;

/**
 * Utility class for {@link BinaryPredicate}.
 * 
 * @author Leo Osvald
 *
 */
public final class BinaryPredicates {

	// disable instantiation
	private BinaryPredicates() { }

	//	tmp_file="/tmp/tmp"; xsel > "$tmp_file"; types="Boolean Byte Char Short Long Float Double Type"; prim="boolean byte char short long float double T"; ucase_prim="BOOLEAN BYTE CHAR SHORT LONG FLOAT DOUBLE TYPE"; types_arr=($types); prim_arr=($prim); ucase_prim_arr=($ucase_prim); echo "" > "$tmp_file"2; for ((i=0;i<7;++i)); do t=${types_arr[i]}; p=${prim_arr[i]}; prim_file=/tmp/$p; cat "$tmp_file" | sed 's/int\( \|s\)/'$p'\1/g' | sed 's/Int/'$t'/g' | sed 's/INT/'${ucase_prim_arr[i]}'/g' | sed 's/TypeBinaryPredicate/&<T>/g' >> "$tmp_file"2; echo -e "\n\n" >> "$tmp_file"2; done; cat "$tmp_file"2 | xsel -b

	// BEGIN HAND-CODED

	public static final class TypeBinaryPredicates {
		// disable instantiation
		private TypeBinaryPredicates() { }

		/**
		 * Returns the negated predicate.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static <T> TypeBinaryPredicate<T> not(
				final TypeBinaryPredicate<T> binaryPredicate) {
			return new TypeBinaryPredicate<T>() {
				@Override
				public boolean holds(T a, T b) {
					return binaryPredicate.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static <T> TypeBinaryPredicate<T> and(
				final TypeBinaryPredicate<T> binaryPredicate1,
				final TypeBinaryPredicate<T> binaryPredicate2) {
			return new TypeBinaryPredicate<T>() {
				@Override
				public boolean holds(T a, T b) {
					return binaryPredicate1.holds(a, b)
					&& binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static <T> TypeBinaryPredicate<T> or(
				final TypeBinaryPredicate<T> binaryPredicate1,
				final TypeBinaryPredicate<T> binaryPredicate2) {
			return new TypeBinaryPredicate<T>() {
				@Override
				public boolean holds(T a, T b) {
					return binaryPredicate1.holds(a, b)
					|| binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static <T> TypeBinaryPredicate<T> xor(
				final TypeBinaryPredicate<T> binaryPredicate1,
				final TypeBinaryPredicate<T> binaryPredicate2) {
			return new TypeBinaryPredicate<T>() {
				@Override
				public boolean holds(T a, T b) {
					return binaryPredicate1.holds(a, b)
					^ binaryPredicate2.holds(a, b);
				}
			};
		}

	}

	// BEGIN XSEL

	public static final class IntBinaryPredicates {
		// disable instantiation
		public IntBinaryPredicates() { }

		/**
		 * A binary predicate representing the equal relation (==) 
		 * between two ints.
		 * @see IntBinaryPredicate
		 */
		public static final IntBinaryPredicate EQUAL_INT
		= new IntBinaryPredicate() {
			@Override
			public boolean holds(int a, int b) {
				return a == b;
			}
		};

		/**
		 * A binary predicate representing the not equal relation (!=) 
		 * between two ints.
		 * @see IntBinaryPredicate
		 */
		public static final IntBinaryPredicate NOT_EQUAL_INT
		= new IntBinaryPredicate() {
			@Override
			public boolean holds(int a, int b) {
				return a != b;
			}
		};

		/**
		 * A binary predicate representing the less than relation (<) 
		 * between two ints.
		 * @see IntBinaryPredicate
		 */
		public static final IntBinaryPredicate LESS_INT
		= new IntBinaryPredicate() {
			@Override
			public boolean holds(int a, int b) {
				return a < b;
			}
		};

		/**
		 * A binary predicate representing the greater than relation (>) 
		 * between two ints.
		 * @see IntBinaryPredicate
		 */
		public static final IntBinaryPredicate GREATER_INT
		= new IntBinaryPredicate() {
			@Override
			public boolean holds(int a, int b) {
				return a > b;
			}
		};

		/**
		 * A binary predicate representing the less than or equal to relation (<=) 
		 * between two ints.
		 * @see IntBinaryPredicate
		 */
		public static final IntBinaryPredicate LESS_OR_EQUAL_INT
		= new IntBinaryPredicate() {
			@Override
			public boolean holds(int a, int b) {
				return a <= b;
			}
		};

		/**
		 * A binary predicate representing the greater or equal to relation (>=) 
		 * between two ints.
		 * @see IntBinaryPredicate
		 */
		public static final IntBinaryPredicate GREATER_OR_EQUAL_INT
		= new IntBinaryPredicate() {
			@Override
			public boolean holds(int a, int b) {
				return a >= b;
			}
		};

		/**
		 * Returns the negated predicate.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static IntBinaryPredicate not(
				final IntBinaryPredicate binaryPredicate) {
			return new IntBinaryPredicate() {
				@Override
				public boolean holds(int a, int b) {
					return binaryPredicate.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static IntBinaryPredicate and(
				final IntBinaryPredicate binaryPredicate1,
				final IntBinaryPredicate binaryPredicate2) {
			return new IntBinaryPredicate() {
				@Override
				public boolean holds(int a, int b) {
					return binaryPredicate1.holds(a, b)
					&& binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static IntBinaryPredicate or(
				final IntBinaryPredicate binaryPredicate1,
				final IntBinaryPredicate binaryPredicate2) {
			return new IntBinaryPredicate() {
				@Override
				public boolean holds(int a, int b) {
					return binaryPredicate1.holds(a, b)
					|| binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static IntBinaryPredicate xor(
				final IntBinaryPredicate binaryPredicate1,
				final IntBinaryPredicate binaryPredicate2) {
			return new IntBinaryPredicate() {
				@Override
				public boolean holds(int a, int b) {
					return binaryPredicate1.holds(a, b)
					^ binaryPredicate2.holds(a, b);
				}
			};
		}
	}

	// END XSEL

	// END HAND-CODED

	public static final class BooleanBinaryPredicates {
		// disable instantiation
		public BooleanBinaryPredicates() { }

		/**
		 * A binary predicate representing the equal relation (==) 
		 * between two booleans.
		 * @see BooleanBinaryPredicate
		 */
		public static final BooleanBinaryPredicate EQUAL_BOOLEAN
		= new BooleanBinaryPredicate() {
			@Override
			public boolean holds(boolean a, boolean b) {
				return a == b;
			}
		};

		/**
		 * A binary predicate representing the not equal relation (!=) 
		 * between two booleans.
		 * @see BooleanBinaryPredicate
		 */
		public static final BooleanBinaryPredicate NOT_EQUAL_BOOLEAN
		= new BooleanBinaryPredicate() {
			@Override
			public boolean holds(boolean a, boolean b) {
				return a != b;
			}
		};

		/**
		 * A binary predicate representing the less than relation (<) 
		 * between two booleans.
		 * @see BooleanBinaryPredicate
		 */
		public static final BooleanBinaryPredicate LESS_BOOLEAN
		= new BooleanBinaryPredicate() {
			@Override
			public boolean holds(boolean a, boolean b) {
				return !a && b;
			}
		};

		/**
		 * A binary predicate representing the greater than relation (>) 
		 * between two booleans.
		 * @see BooleanBinaryPredicate
		 */
		public static final BooleanBinaryPredicate GREATER_BOOLEAN
		= new BooleanBinaryPredicate() {
			@Override
			public boolean holds(boolean a, boolean b) {
				return a && !b;
			}
		};

		/**
		 * A binary predicate representing the less than or equal to relation (<=) 
		 * between two booleans.
		 * @see BooleanBinaryPredicate
		 */
		public static final BooleanBinaryPredicate LESS_OR_EQUAL_BOOLEAN
		= new BooleanBinaryPredicate() {
			@Override
			public boolean holds(boolean a, boolean b) {
				return !(a && !b);
			}
		};

		/**
		 * A binary predicate representing the greater or equal to relation (>=) 
		 * between two booleans.
		 * @see BooleanBinaryPredicate
		 */
		public static final BooleanBinaryPredicate GREATER_OR_EQUAL_BOOLEAN
		= new BooleanBinaryPredicate() {
			@Override
			public boolean holds(boolean a, boolean b) {
				return !(!a && b);
			}
		};

		/**
		 * Returns the negated predicate.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static BooleanBinaryPredicate not(
				final BooleanBinaryPredicate binaryPredicate) {
			return new BooleanBinaryPredicate() {
				@Override
				public boolean holds(boolean a, boolean b) {
					return binaryPredicate.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static BooleanBinaryPredicate and(
				final BooleanBinaryPredicate binaryPredicate1,
				final BooleanBinaryPredicate binaryPredicate2) {
			return new BooleanBinaryPredicate() {
				@Override
				public boolean holds(boolean a, boolean b) {
					return binaryPredicate1.holds(a, b)
					&& binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static BooleanBinaryPredicate or(
				final BooleanBinaryPredicate binaryPredicate1,
				final BooleanBinaryPredicate binaryPredicate2) {
			return new BooleanBinaryPredicate() {
				@Override
				public boolean holds(boolean a, boolean b) {
					return binaryPredicate1.holds(a, b)
					|| binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static BooleanBinaryPredicate xor(
				final BooleanBinaryPredicate binaryPredicate1,
				final BooleanBinaryPredicate binaryPredicate2) {
			return new BooleanBinaryPredicate() {
				@Override
				public boolean holds(boolean a, boolean b) {
					return binaryPredicate1.holds(a, b)
					^ binaryPredicate2.holds(a, b);
				}
			};
		}
	}


	public static final class ByteBinaryPredicates {
		// disable instantiation
		public ByteBinaryPredicates() { }

		/**
		 * A binary predicate representing the equal relation (==) 
		 * between two bytes.
		 * @see ByteBinaryPredicate
		 */
		public static final ByteBinaryPredicate EQUAL_BYTE
		= new ByteBinaryPredicate() {
			@Override
			public boolean holds(byte a, byte b) {
				return a == b;
			}
		};

		/**
		 * A binary predicate representing the not equal relation (!=) 
		 * between two bytes.
		 * @see ByteBinaryPredicate
		 */
		public static final ByteBinaryPredicate NOT_EQUAL_BYTE
		= new ByteBinaryPredicate() {
			@Override
			public boolean holds(byte a, byte b) {
				return a != b;
			}
		};

		/**
		 * A binary predicate representing the less than relation (<) 
		 * between two bytes.
		 * @see ByteBinaryPredicate
		 */
		public static final ByteBinaryPredicate LESS_BYTE
		= new ByteBinaryPredicate() {
			@Override
			public boolean holds(byte a, byte b) {
				return a < b;
			}
		};

		/**
		 * A binary predicate representing the greater than relation (>) 
		 * between two bytes.
		 * @see ByteBinaryPredicate
		 */
		public static final ByteBinaryPredicate GREATER_BYTE
		= new ByteBinaryPredicate() {
			@Override
			public boolean holds(byte a, byte b) {
				return a > b;
			}
		};

		/**
		 * A binary predicate representing the less than or equal to relation (<=) 
		 * between two bytes.
		 * @see ByteBinaryPredicate
		 */
		public static final ByteBinaryPredicate LESS_OR_EQUAL_BYTE
		= new ByteBinaryPredicate() {
			@Override
			public boolean holds(byte a, byte b) {
				return a <= b;
			}
		};

		/**
		 * A binary predicate representing the greater or equal to relation (>=) 
		 * between two bytes.
		 * @see ByteBinaryPredicate
		 */
		public static final ByteBinaryPredicate GREATER_OR_EQUAL_BYTE
		= new ByteBinaryPredicate() {
			@Override
			public boolean holds(byte a, byte b) {
				return a >= b;
			}
		};

		/**
		 * Returns the negated predicate.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static ByteBinaryPredicate not(
				final ByteBinaryPredicate binaryPredicate) {
			return new ByteBinaryPredicate() {
				@Override
				public boolean holds(byte a, byte b) {
					return binaryPredicate.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static ByteBinaryPredicate and(
				final ByteBinaryPredicate binaryPredicate1,
				final ByteBinaryPredicate binaryPredicate2) {
			return new ByteBinaryPredicate() {
				@Override
				public boolean holds(byte a, byte b) {
					return binaryPredicate1.holds(a, b)
					&& binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static ByteBinaryPredicate or(
				final ByteBinaryPredicate binaryPredicate1,
				final ByteBinaryPredicate binaryPredicate2) {
			return new ByteBinaryPredicate() {
				@Override
				public boolean holds(byte a, byte b) {
					return binaryPredicate1.holds(a, b)
					|| binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static ByteBinaryPredicate xor(
				final ByteBinaryPredicate binaryPredicate1,
				final ByteBinaryPredicate binaryPredicate2) {
			return new ByteBinaryPredicate() {
				@Override
				public boolean holds(byte a, byte b) {
					return binaryPredicate1.holds(a, b)
					^ binaryPredicate2.holds(a, b);
				}
			};
		}
	}


	public static final class CharBinaryPredicates {
		// disable instantiation
		public CharBinaryPredicates() { }

		/**
		 * A binary predicate representing the equal relation (==) 
		 * between two chars.
		 * @see CharBinaryPredicate
		 */
		public static final CharBinaryPredicate EQUAL_CHAR
		= new CharBinaryPredicate() {
			@Override
			public boolean holds(char a, char b) {
				return a == b;
			}
		};

		/**
		 * A binary predicate representing the not equal relation (!=) 
		 * between two chars.
		 * @see CharBinaryPredicate
		 */
		public static final CharBinaryPredicate NOT_EQUAL_CHAR
		= new CharBinaryPredicate() {
			@Override
			public boolean holds(char a, char b) {
				return a != b;
			}
		};

		/**
		 * A binary predicate representing the less than relation (<) 
		 * between two chars.
		 * @see CharBinaryPredicate
		 */
		public static final CharBinaryPredicate LESS_CHAR
		= new CharBinaryPredicate() {
			@Override
			public boolean holds(char a, char b) {
				return a < b;
			}
		};

		/**
		 * A binary predicate representing the greater than relation (>) 
		 * between two chars.
		 * @see CharBinaryPredicate
		 */
		public static final CharBinaryPredicate GREATER_CHAR
		= new CharBinaryPredicate() {
			@Override
			public boolean holds(char a, char b) {
				return a > b;
			}
		};

		/**
		 * A binary predicate representing the less than or equal to relation (<=) 
		 * between two chars.
		 * @see CharBinaryPredicate
		 */
		public static final CharBinaryPredicate LESS_OR_EQUAL_CHAR
		= new CharBinaryPredicate() {
			@Override
			public boolean holds(char a, char b) {
				return a <= b;
			}
		};

		/**
		 * A binary predicate representing the greater or equal to relation (>=) 
		 * between two chars.
		 * @see CharBinaryPredicate
		 */
		public static final CharBinaryPredicate GREATER_OR_EQUAL_CHAR
		= new CharBinaryPredicate() {
			@Override
			public boolean holds(char a, char b) {
				return a >= b;
			}
		};

		/**
		 * Returns the negated predicate.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static CharBinaryPredicate not(
				final CharBinaryPredicate binaryPredicate) {
			return new CharBinaryPredicate() {
				@Override
				public boolean holds(char a, char b) {
					return binaryPredicate.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static CharBinaryPredicate and(
				final CharBinaryPredicate binaryPredicate1,
				final CharBinaryPredicate binaryPredicate2) {
			return new CharBinaryPredicate() {
				@Override
				public boolean holds(char a, char b) {
					return binaryPredicate1.holds(a, b)
					&& binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static CharBinaryPredicate or(
				final CharBinaryPredicate binaryPredicate1,
				final CharBinaryPredicate binaryPredicate2) {
			return new CharBinaryPredicate() {
				@Override
				public boolean holds(char a, char b) {
					return binaryPredicate1.holds(a, b)
					|| binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static CharBinaryPredicate xor(
				final CharBinaryPredicate binaryPredicate1,
				final CharBinaryPredicate binaryPredicate2) {
			return new CharBinaryPredicate() {
				@Override
				public boolean holds(char a, char b) {
					return binaryPredicate1.holds(a, b)
					^ binaryPredicate2.holds(a, b);
				}
			};
		}
	}


	public static final class ShortBinaryPredicates {
		// disable instantiation
		public ShortBinaryPredicates() { }

		/**
		 * A binary predicate representing the equal relation (==) 
		 * between two shorts.
		 * @see ShortBinaryPredicate
		 */
		public static final ShortBinaryPredicate EQUAL_SHORT
		= new ShortBinaryPredicate() {
			@Override
			public boolean holds(short a, short b) {
				return a == b;
			}
		};

		/**
		 * A binary predicate representing the not equal relation (!=) 
		 * between two shorts.
		 * @see ShortBinaryPredicate
		 */
		public static final ShortBinaryPredicate NOT_EQUAL_SHORT
		= new ShortBinaryPredicate() {
			@Override
			public boolean holds(short a, short b) {
				return a != b;
			}
		};

		/**
		 * A binary predicate representing the less than relation (<) 
		 * between two shorts.
		 * @see ShortBinaryPredicate
		 */
		public static final ShortBinaryPredicate LESS_SHORT
		= new ShortBinaryPredicate() {
			@Override
			public boolean holds(short a, short b) {
				return a < b;
			}
		};

		/**
		 * A binary predicate representing the greater than relation (>) 
		 * between two shorts.
		 * @see ShortBinaryPredicate
		 */
		public static final ShortBinaryPredicate GREATER_SHORT
		= new ShortBinaryPredicate() {
			@Override
			public boolean holds(short a, short b) {
				return a > b;
			}
		};

		/**
		 * A binary predicate representing the less than or equal to relation (<=) 
		 * between two shorts.
		 * @see ShortBinaryPredicate
		 */
		public static final ShortBinaryPredicate LESS_OR_EQUAL_SHORT
		= new ShortBinaryPredicate() {
			@Override
			public boolean holds(short a, short b) {
				return a <= b;
			}
		};

		/**
		 * A binary predicate representing the greater or equal to relation (>=) 
		 * between two shorts.
		 * @see ShortBinaryPredicate
		 */
		public static final ShortBinaryPredicate GREATER_OR_EQUAL_SHORT
		= new ShortBinaryPredicate() {
			@Override
			public boolean holds(short a, short b) {
				return a >= b;
			}
		};

		/**
		 * Returns the negated predicate.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static ShortBinaryPredicate not(
				final ShortBinaryPredicate binaryPredicate) {
			return new ShortBinaryPredicate() {
				@Override
				public boolean holds(short a, short b) {
					return binaryPredicate.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static ShortBinaryPredicate and(
				final ShortBinaryPredicate binaryPredicate1,
				final ShortBinaryPredicate binaryPredicate2) {
			return new ShortBinaryPredicate() {
				@Override
				public boolean holds(short a, short b) {
					return binaryPredicate1.holds(a, b)
					&& binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static ShortBinaryPredicate or(
				final ShortBinaryPredicate binaryPredicate1,
				final ShortBinaryPredicate binaryPredicate2) {
			return new ShortBinaryPredicate() {
				@Override
				public boolean holds(short a, short b) {
					return binaryPredicate1.holds(a, b)
					|| binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static ShortBinaryPredicate xor(
				final ShortBinaryPredicate binaryPredicate1,
				final ShortBinaryPredicate binaryPredicate2) {
			return new ShortBinaryPredicate() {
				@Override
				public boolean holds(short a, short b) {
					return binaryPredicate1.holds(a, b)
					^ binaryPredicate2.holds(a, b);
				}
			};
		}
	}


	public static final class LongBinaryPredicates {
		// disable instantiation
		public LongBinaryPredicates() { }

		/**
		 * A binary predicate representing the equal relation (==) 
		 * between two longs.
		 * @see LongBinaryPredicate
		 */
		public static final LongBinaryPredicate EQUAL_LONG
		= new LongBinaryPredicate() {
			@Override
			public boolean holds(long a, long b) {
				return a == b;
			}
		};

		/**
		 * A binary predicate representing the not equal relation (!=) 
		 * between two longs.
		 * @see LongBinaryPredicate
		 */
		public static final LongBinaryPredicate NOT_EQUAL_LONG
		= new LongBinaryPredicate() {
			@Override
			public boolean holds(long a, long b) {
				return a != b;
			}
		};

		/**
		 * A binary predicate representing the less than relation (<) 
		 * between two longs.
		 * @see LongBinaryPredicate
		 */
		public static final LongBinaryPredicate LESS_LONG
		= new LongBinaryPredicate() {
			@Override
			public boolean holds(long a, long b) {
				return a < b;
			}
		};

		/**
		 * A binary predicate representing the greater than relation (>) 
		 * between two longs.
		 * @see LongBinaryPredicate
		 */
		public static final LongBinaryPredicate GREATER_LONG
		= new LongBinaryPredicate() {
			@Override
			public boolean holds(long a, long b) {
				return a > b;
			}
		};

		/**
		 * A binary predicate representing the less than or equal to relation (<=) 
		 * between two longs.
		 * @see LongBinaryPredicate
		 */
		public static final LongBinaryPredicate LESS_OR_EQUAL_LONG
		= new LongBinaryPredicate() {
			@Override
			public boolean holds(long a, long b) {
				return a <= b;
			}
		};

		/**
		 * A binary predicate representing the greater or equal to relation (>=) 
		 * between two longs.
		 * @see LongBinaryPredicate
		 */
		public static final LongBinaryPredicate GREATER_OR_EQUAL_LONG
		= new LongBinaryPredicate() {
			@Override
			public boolean holds(long a, long b) {
				return a >= b;
			}
		};

		/**
		 * Returns the negated predicate.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static LongBinaryPredicate not(
				final LongBinaryPredicate binaryPredicate) {
			return new LongBinaryPredicate() {
				@Override
				public boolean holds(long a, long b) {
					return binaryPredicate.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static LongBinaryPredicate and(
				final LongBinaryPredicate binaryPredicate1,
				final LongBinaryPredicate binaryPredicate2) {
			return new LongBinaryPredicate() {
				@Override
				public boolean holds(long a, long b) {
					return binaryPredicate1.holds(a, b)
					&& binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static LongBinaryPredicate or(
				final LongBinaryPredicate binaryPredicate1,
				final LongBinaryPredicate binaryPredicate2) {
			return new LongBinaryPredicate() {
				@Override
				public boolean holds(long a, long b) {
					return binaryPredicate1.holds(a, b)
					|| binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static LongBinaryPredicate xor(
				final LongBinaryPredicate binaryPredicate1,
				final LongBinaryPredicate binaryPredicate2) {
			return new LongBinaryPredicate() {
				@Override
				public boolean holds(long a, long b) {
					return binaryPredicate1.holds(a, b)
					^ binaryPredicate2.holds(a, b);
				}
			};
		}
	}


	public static final class FloatBinaryPredicates {
		// disable instantiation
		public FloatBinaryPredicates() { }

		/**
		 * A binary predicate representing the equal relation (==) 
		 * between two floats.
		 * @see FloatBinaryPredicate
		 */
		public static final FloatBinaryPredicate EQUAL_FLOAT
		= new FloatBinaryPredicate() {
			@Override
			public boolean holds(float a, float b) {
				return a == b;
			}
		};

		/**
		 * A binary predicate representing the not equal relation (!=) 
		 * between two floats.
		 * @see FloatBinaryPredicate
		 */
		public static final FloatBinaryPredicate NOT_EQUAL_FLOAT
		= new FloatBinaryPredicate() {
			@Override
			public boolean holds(float a, float b) {
				return a != b;
			}
		};

		/**
		 * A binary predicate representing the less than relation (<) 
		 * between two floats.
		 * @see FloatBinaryPredicate
		 */
		public static final FloatBinaryPredicate LESS_FLOAT
		= new FloatBinaryPredicate() {
			@Override
			public boolean holds(float a, float b) {
				return a < b;
			}
		};

		/**
		 * A binary predicate representing the greater than relation (>) 
		 * between two floats.
		 * @see FloatBinaryPredicate
		 */
		public static final FloatBinaryPredicate GREATER_FLOAT
		= new FloatBinaryPredicate() {
			@Override
			public boolean holds(float a, float b) {
				return a > b;
			}
		};

		/**
		 * A binary predicate representing the less than or equal to relation (<=) 
		 * between two floats.
		 * @see FloatBinaryPredicate
		 */
		public static final FloatBinaryPredicate LESS_OR_EQUAL_FLOAT
		= new FloatBinaryPredicate() {
			@Override
			public boolean holds(float a, float b) {
				return a <= b;
			}
		};

		/**
		 * A binary predicate representing the greater or equal to relation (>=) 
		 * between two floats.
		 * @see FloatBinaryPredicate
		 */
		public static final FloatBinaryPredicate GREATER_OR_EQUAL_FLOAT
		= new FloatBinaryPredicate() {
			@Override
			public boolean holds(float a, float b) {
				return a >= b;
			}
		};

		/**
		 * Returns the negated predicate.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static FloatBinaryPredicate not(
				final FloatBinaryPredicate binaryPredicate) {
			return new FloatBinaryPredicate() {
				@Override
				public boolean holds(float a, float b) {
					return binaryPredicate.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static FloatBinaryPredicate and(
				final FloatBinaryPredicate binaryPredicate1,
				final FloatBinaryPredicate binaryPredicate2) {
			return new FloatBinaryPredicate() {
				@Override
				public boolean holds(float a, float b) {
					return binaryPredicate1.holds(a, b)
					&& binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static FloatBinaryPredicate or(
				final FloatBinaryPredicate binaryPredicate1,
				final FloatBinaryPredicate binaryPredicate2) {
			return new FloatBinaryPredicate() {
				@Override
				public boolean holds(float a, float b) {
					return binaryPredicate1.holds(a, b)
					|| binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static FloatBinaryPredicate xor(
				final FloatBinaryPredicate binaryPredicate1,
				final FloatBinaryPredicate binaryPredicate2) {
			return new FloatBinaryPredicate() {
				@Override
				public boolean holds(float a, float b) {
					return binaryPredicate1.holds(a, b)
					^ binaryPredicate2.holds(a, b);
				}
			};
		}
	}


	public static final class DoubleBinaryPredicates {
		// disable instantiation
		public DoubleBinaryPredicates() { }

		/**
		 * A binary predicate representing the equal relation (==) 
		 * between two doubles.
		 * @see DoubleBinaryPredicate
		 */
		public static final DoubleBinaryPredicate EQUAL_DOUBLE
		= new DoubleBinaryPredicate() {
			@Override
			public boolean holds(double a, double b) {
				return a == b;
			}
		};

		/**
		 * A binary predicate representing the not equal relation (!=) 
		 * between two doubles.
		 * @see DoubleBinaryPredicate
		 */
		public static final DoubleBinaryPredicate NOT_EQUAL_DOUBLE
		= new DoubleBinaryPredicate() {
			@Override
			public boolean holds(double a, double b) {
				return a != b;
			}
		};

		/**
		 * A binary predicate representing the less than relation (<) 
		 * between two doubles.
		 * @see DoubleBinaryPredicate
		 */
		public static final DoubleBinaryPredicate LESS_DOUBLE
		= new DoubleBinaryPredicate() {
			@Override
			public boolean holds(double a, double b) {
				return a < b;
			}
		};

		/**
		 * A binary predicate representing the greater than relation (>) 
		 * between two doubles.
		 * @see DoubleBinaryPredicate
		 */
		public static final DoubleBinaryPredicate GREATER_DOUBLE
		= new DoubleBinaryPredicate() {
			@Override
			public boolean holds(double a, double b) {
				return a > b;
			}
		};

		/**
		 * A binary predicate representing the less than or equal to relation (<=) 
		 * between two doubles.
		 * @see DoubleBinaryPredicate
		 */
		public static final DoubleBinaryPredicate LESS_OR_EQUAL_DOUBLE
		= new DoubleBinaryPredicate() {
			@Override
			public boolean holds(double a, double b) {
				return a <= b;
			}
		};

		/**
		 * A binary predicate representing the greater or equal to relation (>=) 
		 * between two doubles.
		 * @see DoubleBinaryPredicate
		 */
		public static final DoubleBinaryPredicate GREATER_OR_EQUAL_DOUBLE
		= new DoubleBinaryPredicate() {
			@Override
			public boolean holds(double a, double b) {
				return a >= b;
			}
		};

		/**
		 * Returns the negated predicate.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the negated predicate
		 */
		public static DoubleBinaryPredicate not(
				final DoubleBinaryPredicate binaryPredicate) {
			return new DoubleBinaryPredicate() {
				@Override
				public boolean holds(double a, double b) {
					return binaryPredicate.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff both specified predicate hold.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "and" operation
		 */
		public static DoubleBinaryPredicate and(
				final DoubleBinaryPredicate binaryPredicate1,
				final DoubleBinaryPredicate binaryPredicate2) {
			return new DoubleBinaryPredicate() {
				@Override
				public boolean holds(double a, double b) {
					return binaryPredicate1.holds(a, b)
					&& binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff any of the specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "or" operation
		 */
		public static DoubleBinaryPredicate or(
				final DoubleBinaryPredicate binaryPredicate1,
				final DoubleBinaryPredicate binaryPredicate2) {
			return new DoubleBinaryPredicate() {
				@Override
				public boolean holds(double a, double b) {
					return binaryPredicate1.holds(a, b)
					|| binaryPredicate2.holds(a, b);
				}
			};
		}

		/**
		 * Returns the predicate which holds iff exactly one specified predicate holds.
		 * @param binaryPredicate predicate which negation should be returned
		 * @return the predicate obtained by applying the logical "xor" operation
		 */
		public static DoubleBinaryPredicate xor(
				final DoubleBinaryPredicate binaryPredicate1,
				final DoubleBinaryPredicate binaryPredicate2) {
			return new DoubleBinaryPredicate() {
				@Override
				public boolean holds(double a, double b) {
					return binaryPredicate1.holds(a, b)
					^ binaryPredicate2.holds(a, b);
				}
			};
		}
	}
}
