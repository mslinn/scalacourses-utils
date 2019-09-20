package com.micronautics.future

object FuturePerfect {
  import scala.concurrent._
  import scala.util.{Failure, Success, Try}

  /** @param block is lazily evaluated and is used to create the Future.
    * @param ex is the usual ExecutionContext for the Future to run
    * @return Tuple containing the Future and a Function1[String,CancellationException].
    *         The Function1 returns None if Future has not been canceled, otherwise it returns Some(CancellationException))
    *         that contains the String passed to the function when the future was canceled.
    * @author Eric Pederson (@sourcedelica) http://stackoverflow.com/questions/16020964/cancellation-with-future-and-promise-in-scala
    * @author Mike Slinn updated to Scala 2.13 */
  def interruptibleFuture[T](block: =>T)
                            (implicit ex: ExecutionContext): (Future[T], String => Option[CancellationException]) = {
    val p = Promise[T]()
    val future = p.future
    val atomicReference = new java.util.concurrent.atomic.AtomicReference[Thread](null)
    p completeWith Future {
      val thread = Thread.currentThread
      atomicReference.synchronized { atomicReference.set(thread) }
      try block finally {
        atomicReference.synchronized { atomicReference getAndSet null } ne thread
        ()
      }
    }

    /* This method can be called multiple times without any problem */
    val cancel = (msg: String) => {
      if (p.isCompleted) {
        None
      } else {
        atomicReference.synchronized {
          Option(atomicReference getAndSet null) foreach { _.interrupt() }
        }
        val ex = new CancellationException(msg)
        p.tryFailure(ex)
        Some(ex)
      }
    }
    (future, cancel)
  }

  /** Immediately process future to complete in a collection;
    * can be called recursively so all other Futures are processed as soon as they complete.
    * @return the first future to complete, with the remainder of the Futures as a sequence.
    * @param futures a scala.collection.Seq
    * @author Victor Klang (https://gist.github.com/4488970)
    * @author Mike Slinn Updated to Scala 2.13 */
  def select[A](futures: Seq[Future[A]])
               (implicit ec: ExecutionContext): Future[(Try[A], Seq[Future[A]])] = {
    import scala.annotation.tailrec
    import scala.concurrent.Promise

    @tailrec
    def stripe(promise: Promise[(Try[A],
               Seq[Future[A]])],
               head: Seq[Future[A]],
               thisFuture: Future[A],
               tail: Seq[Future[A]]): Future[(Try[A], Seq[Future[A]])] = {
      thisFuture onComplete { result => if (!promise.isCompleted) promise.trySuccess((result, head ++ tail)) }
      if (tail.isEmpty) promise.future
      else stripe(promise, head :+ thisFuture, tail.head, tail.tail)
    }

    if (futures.isEmpty) Future.failed(new IllegalArgumentException("List of futures is empty"))
    else stripe(Promise(), futures, futures.head, futures.tail)
  }

  /** Driver for select; apply a function over a sequence of Future as soon as each Future completes.
    * @param futures collection of Future to operate on
    * @param operation Function1 to apply to each Future value as soon as the Future completes
    * @param whenDone block of code to execute when all Futures have been processed
    * @author David Crosson
    * @author Mike Slinn updated to Scala 2.13 */
  def asapFutures[T](futures: Seq[Future[T]])
                    (operation: Try[T]=>Unit)
                    (whenDone: =>Unit={})
                    (implicit ec: ExecutionContext): Unit = {

    def jiffyFutures(futures: Seq[Future[T]])
                    (operation: Try[T]=>Unit)
                    (whenDone: =>Unit): Unit = {
      if (futures.nonEmpty) {
        select(futures) andThen {
          case Success((tryResult, remainingFutures)) =>
            operation(tryResult)
            jiffyFutures(remainingFutures)(operation)(whenDone)

          case Failure(throwable) =>
            println("Unexpected exception: " + throwable.getMessage)
        } andThen {
          case _ =>
            if (futures.size==1)
              whenDone
        }
      }
      ()
    }

    if (futures.isEmpty)
      whenDone
    else
      jiffyFutures(futures)(operation)(whenDone)
  }
}
