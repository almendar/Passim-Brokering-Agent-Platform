package pl.edu.pw.ii.passim.util

import collection.generic.CanBuildFrom


/** Provides multi-map behaviour to Map[K, Set[V]] objects, by adding
 *  addBinding and removeBinding methods.
 *  @author Piotr Kołaczkowski */
object MultiMapUtil {

  class MultiMap[K, V, M <: Map[K, Set[V]]](val map: M, emptySet: Set[V]) {
    def addBinding(k: K, v: V): M = {
      val prev = map.getOrElse(k, emptySet)
      val r = prev + v
      (map + (k -> r)).asInstanceOf[M]
    }

    def addBinding(p: (K, V)): M = addBinding(p._1, p._2)

    def removeBinding(k: K, v: V): M = {
      val r = (map.getOrElse(k, emptySet) - v)
      (if (r.isEmpty) map - k else map + (k -> r)).asInstanceOf[M]
    }

    def removeBinding(p: (K, V)): M = removeBinding(p._1, p._2)

    def addBindings(p: Iterable[(K, V)]): M =
      p.foldLeft(this)((m, p) => new MultiMap(m addBinding p, emptySet)).map

    def removeBindings(p: Iterable[(K, V)]): M =
      p.foldLeft(this)((m, p) => new MultiMap(m removeBinding p, emptySet)).map

    def bindings: Iterable[(K, V)] = 
      for ((k, s) <- map; v <- s) yield (k, v)
  }

  // Warning! Here live dragons.
  implicit def mapToMultiMap[K, V, S[V] <: Set[V], M[K, S] <: Map[K, S]]
      (map: M[K, S[V]])(implicit bf: CanBuildFrom[Nothing, V, S[V]]) =
    new MultiMap[K, V, M[K, S[V]]](map, bf.apply.result)
}