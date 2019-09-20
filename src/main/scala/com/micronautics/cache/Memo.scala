package com.micronautics.cache

import collection.mutable

trait Memoize {
  /** Transform given `Function1` instance into another `Function1` instance backed by a `WeakHashMap` for memoization of parameter/result pairs.
    * @param f Must be `Function1` (single argument) */
  def memoize[Key, Value](f: Key => Value): (Key) => Value = {
    /* Each memoized Function has its own cache */
    val cache = mutable.WeakHashMap.empty[Key, Value]
    key: Key => {
      cache.getOrElseUpdate(key, f(key))
    }
  }

  /** Transform given `Function2` instance into another `Function2` instance backed by a `WeakHashMap` for memoization of parameter/result pairs.
    * @param f Must be `Function2` */
  def memoize[Key1, Key2, Value](f: (Key1, Key2) => Value): (Key1, Key2) => Value = {
    import Memoizer.recordSeparator

    /* Each memoized Function has its own cache */
    val cache = mutable.WeakHashMap.empty[String, Value]
    (key1: Key1, key2: Key2) => {
      val combinedKey: String = s"$key1$recordSeparator$key2"
      cache.getOrElseUpdate(combinedKey, f(key1, key2))
    }
  }

  /** Transform given `Function3` instance into another `Function2` instance backed by a `WeakHashMap` for memoization of parameter/result pairs.
    * @param f Must be `Function3` */
  def memoize[Key1, Key2, Key3, Value](f: (Key1, Key2, Key3) => Value): (Key1, Key2, Key3) => Value = {
    import Memoizer.recordSeparator

    /* Each memoized Function has its own cache */
    val cache = mutable.WeakHashMap.empty[String, Value]
    (key1: Key1, key2: Key2, key3: Key3) => {
      val combinedKey: String = s"$key1$recordSeparator$key2$recordSeparator$key3"
      cache.getOrElseUpdate(combinedKey, f(key1, key2, key3))
    }
  }

  /** Transform given `Function3` instance into another `Function2` instance backed by a `WeakHashMap` for memoization of parameter/result pairs.
    * @param f Must be `Function3` */
  def memoize[Key1, Key2, Key3, Key4, Value](f: (Key1, Key2, Key3, Key4) => Value): (Key1, Key2, Key3, Key4) => Value = {
    import Memoizer.recordSeparator

    /* Each memoized Function has its own cache */
    val cache = mutable.WeakHashMap.empty[String, Value]
    (key1: Key1, key2: Key2, key3: Key3, key4: Key4) => {
      val combinedKey: String = s"$key1$recordSeparator$key2$recordSeparator$key3$recordSeparator$key4"
      cache.getOrElseUpdate(combinedKey, f(key1, key2, key3, key4))
    }
  }

  /** Transform given `Function3` instance into another `Function2` instance backed by a `WeakHashMap` for memoization of parameter/result pairs.
    * @param f Must be `Function3` */
  def memoize[Key1, Key2, Key3, Key4, Key5, Value](f: (Key1, Key2, Key3, Key4, Key5) => Value): (Key1, Key2, Key3, Key4, Key5) => Value = {
    import Memoizer.recordSeparator

    /* Each memoized Function has its own cache */
    val cache = mutable.WeakHashMap.empty[String, Value]
    (key1: Key1, key2: Key2, key3: Key3, key4: Key4, key5: Key5) => {
      val combinedKey: String = s"$key1$recordSeparator$key2$recordSeparator$key3$recordSeparator$key4$recordSeparator$key5"
      cache.getOrElseUpdate(combinedKey, f(key1, key2, key3, key4, key5))
    }
  }
}

/** Memoize a lazily evaluated block of code */
class Memoizer0[Value](block: => Value) {
  private[this] val values = mutable.Map.empty[String, Value]

  def apply: Value = {
    val key = block.toString
    if (values.keySet.contains(key)) {
      values(key)
    } else {
      val value = block
      values += key -> value
      value
    }
  }

  /** Discard the memoized key/value pair */
  def clear(): Unit = values.clear()
}

/** Memoize a Function1[Key, Value] */
class Memoizer[Key, Value](f: Key => Value) {
  private[this] val values = mutable.Map.empty[Key, Value]

  def apply(key: Key): Value =
    if (values.keySet.contains(key)) {
      values(key)
    } else {
      val value = f(key)
      values += key -> value
      value
    }

  /** Discard the memoized key/value pair */
  def clear(): Unit = values.clear()
}

/** Memoize a Function2[Key1, Key2, Value] */
class Memoizer2[Key1, Key2, Value](f: (Key1, Key2) => Value) {
  import Memoizer.recordSeparator

  private[this] val values = mutable.Map.empty[String, Value]

  def apply(key1: Key1, key2: Key2): Value = {
    val combinedKey: String = s"$key1$recordSeparator$key2"
    if (values.keySet.contains(combinedKey)) {
      values(combinedKey)
    } else {
      val result = f(key1, key2)
      values += combinedKey -> result
      result
    }
  }

  /** Discard the memoized key/value pair */
  def clear(): Unit = values.clear()
}

/** Memoize a Function3[Key1, Key2, Key3, Value] */
class Memoizer3[Key1, Key2, Key3, Value](f: (Key1, Key2, Key3) => Value) {
  import Memoizer.recordSeparator

  private[this] val values = mutable.Map.empty[String, Value]

  def apply(key1: Key1, key2: Key2, key3: Key3): Value = {
    val combinedKey: String = s"$key1$recordSeparator$key2$recordSeparator$key3"
    if (values.keySet.contains(combinedKey)) {
      values(combinedKey)
    } else {
      val value = f(key1, key2, key3)
      values += combinedKey -> value
      value
    }
  }

  /** Discard the memoized key/value pair */
  def clear(): Unit = values.clear()
}

/** Memoize a Function4[Key1, Key2, Key3, Key4, Value] */
class Memoizer4[Key1, Key2, Key3, Key4, Value](f: (Key1, Key2, Key3, Key4) => Value) {
  import Memoizer.recordSeparator

  private[this] val values = mutable.Map.empty[String, Value]

  def apply(key1: Key1, key2: Key2, key3: Key3, key4: Key4): Value = {
    val combinedKey: String = s"$key1$recordSeparator$key2$recordSeparator$key3$recordSeparator$key4"
    if (values.keySet.contains(combinedKey)) {
      values(combinedKey)
    } else {
      val value = f(key1, key2, key3, key4)
      values += combinedKey -> value
      value
    }
  }

  /** Discard the memoized key/value pair */
  def clear(): Unit = values.clear()
}

/** Memoize a Function4[Key1, Key2, Key3, Key4, Key5, Value] */
class Memoizer5[Key1, Key2, Key3, Key4, Key5, Value](f: (Key1, Key2, Key3, Key4, Key5) => Value) {
  import Memoizer.recordSeparator

  private[this] val values = mutable.Map.empty[String, Value]

  def apply(key1: Key1, key2: Key2, key3: Key3, key4: Key4, key5: Key5): Value = {
    val combinedKey: String = s"$key1$recordSeparator$key2$recordSeparator$key3$recordSeparator$key4$recordSeparator$key5"
    if (values.keySet.contains(combinedKey)) {
      values(combinedKey)
    } else {
      val value = f(key1, key2, key3, key4, key5)
      values += combinedKey -> value
      value
    }
  }

  /** Discard the memoized key/value pair */
  def clear(): Unit = values.clear()
}

object Memoizer {
  val recordSeparator: Char = 0x241E.toChar

  def apply[Value](block: => Value) = new Memoizer0(block)

  def apply[Key, Value](function1: Key => Value) = new Memoizer(function1)

  def apply[Key1, Key2, Value](function2: (Key1, Key2) => Value) = new Memoizer2(function2)

  def apply[Key1, Key2, Key3, Value](function3: (Key1, Key2, Key3) => Value) = new Memoizer3(function3)

  def apply[Key1, Key2, Key3, Key4, Value](function4: (Key1, Key2, Key3, Key4) => Value) = new Memoizer4(function4)
}
