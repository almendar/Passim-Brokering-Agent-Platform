package pl.edu.pw.ii.passim.util

import java.net.{URLEncoder, URI}

// Copyright by: Piotr Kołaczkowski
// Created: 06.06.11 20:00

/** A helper factory for creating URIs */
object URIFactory {

  def createUri(base: String, query: Iterable[(String, Any)]): URI = {
    val pairs = query.map(x => x._1 + "=" + URLEncoder.encode(x._2.toString, "UTF-8"))
    val uriStr = base + (if (!pairs.isEmpty) "?" + pairs.reduceLeft(_ + "&" + _) else "")
    new URI(uriStr)
  }
}