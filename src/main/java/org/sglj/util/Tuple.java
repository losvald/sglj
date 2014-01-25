/*
 * Tuple.java
 * 
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * 
 * This file is part of SGLJ.
 * 
 * SGLJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SGLJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.sglj.util;

/**
 * Class that provides a generic tuple with template arguments.<br>
 * Currently implemented tuples are the 3-tuple and the 4-tuple.
 * 
 * @author Leo Osvald
 * @version 0.01
 */
public class Tuple {
	Tuple() {
	}
	
	/**
	 * Ordered triple.
	 * 
	 * @author Leo Osvald
	 * @version 1.0
	 * @param <T1>
	 * @param <T2>
	 * @param <T3>
	 */
	public static class Tuple3<T1, T2, T3> 
	implements Comparable< Tuple3<T1, T2, T3> > {
		public T1 first;
		public T2 second;
		public T3 third;
		
		public Tuple3() {
		}
		
		public Tuple3(T1 first, T2 second, T3 third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}
		
		@SuppressWarnings("unchecked")
		public int compareTo(Tuple3<T1, T2, T3> o) {
			if(!(first instanceof Comparable<?>)) return 0; 
			Comparable<? super T1> compFirst = (Comparable) first; 
			int cmp = compFirst.compareTo(o.first);
			if(cmp != 0) return cmp;
			
			if(!(second instanceof Comparable<?>)) return 0; 
			Comparable<? super T2> compSecond = (Comparable) second; 
			cmp = compSecond.compareTo(o.second);
			if(cmp != 0) return cmp;
			
			if(!(third instanceof Comparable<?>)) return 0; 
			Comparable<? super T3> compThird = (Comparable) o.third; 
			return compThird.compareTo(o.third);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof Tuple3<?, ?, ?>)) return false;
			return first.equals(((Tuple3<?, ?, ?>)obj).first)
			&& second.equals(((Tuple3<?, ?, ?>)obj).second)
			&& third.equals(((Tuple3<?, ?, ?>)obj).third);
		}
		
		@Override
		public int hashCode() {
			return first.hashCode() ^ (second.hashCode()<<10)
			^ (third.hashCode()<<20);
		}
	}
	
	/**
	 * Ordered 4-tuple.
	 * 
	 * @author Leo Osvald
	 * @version 1.0
	 * @param <T1>
	 * @param <T2>
	 * @param <T3>
	 * @param <T4>
	 */
	public static class Tuple4<T1, T2, T3, T4> 
	implements Comparable< Tuple4<T1, T2, T3, T4> > {
		public T1 first;
		public T2 second;
		public T3 third;
		public T4 fourth;
		
		public Tuple4() {
		}
		
		public Tuple4(T1 first, T2 second, T3 third, T4 fourth) {
			this.first = first;
			this.second = second;
			this.third = third;
			this.fourth = fourth;
		}
		
		@SuppressWarnings("unchecked")
		public int compareTo(Tuple4<T1, T2, T3, T4> o) {
			if(!(first instanceof Comparable<?>)) return 0; 
			Comparable<? super T1> compFirst = (Comparable) first; 
			int cmp = compFirst.compareTo(o.first);
			if(cmp != 0) return cmp;
			
			if(!(second instanceof Comparable<?>)) return 0; 
			Comparable<? super T2> compSecond = (Comparable) second; 
			cmp = compSecond.compareTo(o.second);
			if(cmp != 0) return cmp;
			
			if(!(third instanceof Comparable<?>)) return 0; 
			Comparable<? super T3> compThird = (Comparable) o.third; 
			cmp = compThird.compareTo(o.third);
			if(cmp != 0) return cmp;
			
			if(!(fourth instanceof Comparable<?>)) return 0; 
			Comparable<? super T4> compFourth = (Comparable) o.fourth; 
			return compFourth.compareTo(o.fourth);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof Tuple4<?, ?, ?, ?>)) return false;
			return first.equals(((Tuple4<?, ?, ?, ?>)obj).first)
			&& second.equals(((Tuple4<?, ?, ?, ?>)obj).second)
			&& third.equals(((Tuple4<?, ?, ?, ?>)obj).third)
			&& fourth.equals(((Tuple4<?, ?, ?, ?>)obj).fourth);
		}
		
		@Override
		public int hashCode() {
			return first.hashCode() ^ (second.hashCode()<<16)
			^ third.hashCode() ^ (fourth.hashCode()<<16);
		}
	}
	
	//TODO
	class AbstractTuple {
		Object[] values;
		
		AbstractTuple(Object... values) {
			// TODO Auto-generated constructor stub
			this.values = values;
		}
		
	}

}



