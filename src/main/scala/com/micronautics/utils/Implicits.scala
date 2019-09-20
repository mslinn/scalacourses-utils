package com.micronautics.utils

import scala.util.{Failure, Success, Try}

object Implicits extends Trys with Throwables {
  implicit class RichTry[A](theTry: Try[A]) {
    def andThen(pf: PartialFunction[Try[A], Unit]): Try[A] = {
      if (pf.isDefinedAt(theTry)) pf(theTry)
      theTry
    }
  }

  implicit class RichThrowable(throwable: Throwable) {
    def format(asHtml: Boolean=false, showStackTrace: Boolean = false): String =
      new Throwables{}.format(throwable, asHtml, showStackTrace)
  }
}

trait Throwables {
  /** @param showStackTrace is overridden if the Exception has no cause and no message */
  def format(ex: Throwable, asHtml: Boolean=false, showStackTrace: Boolean = false): String = {
    val cause = ex.getCause
    val noCause = (null==cause) || cause.toString.trim.isEmpty

    val message = ex.getMessage
    val noMessage = (null==message) || message.trim.isEmpty

    (if (noCause) "" else s"$cause: ") + (if (noMessage) "" else message) +
      (if (asHtml) {
        if (showStackTrace || (noCause && noMessage))
          "\n<pre>" + ex.getStackTrace.mkString("\n  ", "\n  ", "\n") + "</pre>\n"
        else ""
      } else { // console output
        (if (!showStackTrace && (!noCause || !noMessage)) "" else "\n  ") +
        (if (showStackTrace || (noCause && noMessage)) ex.getStackTrace.mkString("\n  ") else "")
      })
  }
}

trait Trys {
  /** @return collection of all failures from the given collection of Try */
  def failures[A](tries: Seq[Try[A]]): Seq[Throwable] = tries.collect { case Failure(t) => t }

  /** @return collection of all successful values from the given collection of Try */
  def successes[A](tries: Seq[Try[A]]): Seq[A] = tries.collect { case Success(t) => t }

  /** @return a Try of a collection from a collection of Try. If many Throwables are encountered, only one Exception needs to be captured. */
  def sequence[A](tries: Seq[Try[A]]): Try[Seq[A]] = Try(tries.map(_.get))

  /** @return a Tuple2 containing a collection of all failures and all the successes */
  def sequenceWithFailures[A](tries: Seq[Try[A]]): (Seq[Throwable], Seq[A]) =
    (failures(tries), successes(tries))
}
