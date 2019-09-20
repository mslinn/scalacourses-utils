package com.micronautics

import java.io.{BufferedReader, File, FileReader}

package object utils {
  def readLines(fileName: String): List[String] = readLines(new File(fileName))

  def readLines(file: File): List[String] =
    using(new BufferedReader(new FileReader(file)))
       { reader => unfold(())(_ => Option(reader.readLine).map(_ -> ((): Unit))) }

  def read(file: File): String = readLines(file).mkString

  def read(fileName: String): String = read(new File(fileName))

  def using[A <: AutoCloseable, B]
           (resource: A)
           (f: A => B): B =
    try f(resource) finally resource.close()

  /** Scala 2.13 defines `Iterator.unfold`. This is essentially the same method, provided for all Scala versions. */
  def unfold[A, S](start: S)
                  (op: S => Option[(A, S)]): List[A] =
    Iterator.
      iterate(op(start))(_.flatMap{ case (_, s) => op(s) })
        .map(_.map(_._1))
        .takeWhile(_.isDefined)
        .flatten
        .toList
}
