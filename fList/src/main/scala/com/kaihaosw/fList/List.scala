package com.kaihaosw.fList

import scala.{List => _, Nil => _}
import scala.annotation.tailrec

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def apply[A](xs: A*): List[A] =
    if (xs.isEmpty) Nil
    else Cons(xs.head, apply(xs.tail: _*))

  def isEmpty[A](ls: List[A]): Boolean =
    ls match {
      case Nil => true
      case _ => false
    }

  @tailrec
  def foldLeft[A, B](ls: List[A], z: B)(f: (B, A) => B): B =
    ls match {
      case Nil => z
      case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
    }

  def reverse[A](ls: List[A]): List[A] = foldLeft(ls, Nil: List[A])((a, b) => Cons(b, a))

  def foldRight[A, B](ls: List[A], z: B)(f: (A, B) => B): B = foldLeft(reverse(ls), z)((a, b) => f(b, a))

  def map[A, B](ls: List[A])(f: A => B): List[B] = foldRight(ls, Nil: List[B])((a, b) => Cons(f(a), b))

  def filter[A](ls: List[A])(p: A => Boolean): List[A] = foldRight(ls, Nil: List[A])((a, b) => if (p(a)) Cons(a, b) else b)

  def filterNot[A](ls: List[A])(p: A => Boolean): List[A] = foldRight(ls, Nil: List[A])((a, b) => if (!(p(a))) Cons(a, b) else b)

  def head[A](ls: List[A]): A =
    ls match {
      case Nil => error("No head for List.")
      case Cons(x, xs) => x
    }

  def tail[A](ls: List[A]): List[A] =
    ls match {
      case Nil => error("No tail for List.")
      case Cons(x, xs) => xs
    }

  def init[A](ls: List[A]): List[A] =
    ls match {
      case Nil => error("No init for List.")
      case Cons(x, Nil) => Nil
      case Cons(x, Cons(y, Nil)) => List(x)
      case Cons(x, xs) => Cons(x, init(xs))
    }

  def headOption[A](ls: List[A]): Option[A] = foldRight(ls, None: Option[A])((a, b) => a match { case Nil => b; case _ => Some(a) })

  def setHead[A](ls: List[A], elem: A): List[A]= Cons(elem, tail(ls))

  def drop[A](ls: List[A], n: Int): List[A] =
    if (n == 0) ls
    else drop(tail(ls), n - 1)

  def dropWhile[A](ls: List[A])(f: A => Boolean): List[A] =
    ls match {
      case Nil => Nil
      case Cons(x, xs) => if (f(x)) dropWhile(xs)(f) else Cons(x, xs)
    }

  def append[A](ls1: List[A], ls2: List[A]): List[A] = foldRight(ls1, ls2)((a, b) => Cons(a, b))

  def length[A](ls: List[A]): Int = foldLeft(ls, 0)((a, _) => a + 1)

  def flatMap[A, B](ls: List[A])(f: A => List[B]): List[B] = foldRight(ls, Nil: List[B])((a, b) => append(f(a), b))

  def zip[A, B](ls1: List[A], ls2: List[B]): List[(A, B)] =
    (ls1, ls2) match {
      case (Nil, xs2) => Nil
      case (xs1, Nil) => Nil
      case (Cons(x1, xs1), Cons(x2, xs2)) => Cons((x1, x2), zip(xs1, xs2))
    }

  def zipWithIndex[A](ls: List[A]): List[(A, Int)] = foldRight(ls, Nil: List[(A, Int)])((a, b) => Cons((a, length(ls) - length(b) - 1), b))

  def take[A](ls: List[A], n: Int): List[A] = reverse(foldLeft(ls, Nil: List[A])((a, b) => if (length(a) == n) a else Cons(b, a)))

  def takeWhile[A](ls: List[A])(f: A => Boolean): List[A] =
    ls match {
      case Nil => Nil
      case Cons(x, xs) => if (f(x)) Cons(x, takeWhile(xs)(f)) else Nil
    }

  def takeRight[A](ls: List[A], n: Int) = drop(ls, length(ls) - n)
  def dropRight[A](ls: List[A], n: Int) = take(ls, length(ls) - n)

  def contains[A](ls: List[A], elem: A): Boolean = foldLeft(ls, false)((a, b) => if (b == elem) true else a)

  def containsSlice[A](ls: List[A], sub: List[A]): Boolean = {
    def loop(ls: List[A], sub: List[A], subOriginal: List[A]): Boolean =
      (ls, sub) match {
        case (_, Nil) => true
        case (Nil, _) => false
        case (Cons(x, xs), Cons(y, ys)) => if (x == y) loop(xs, ys, subOriginal) else loop(xs, subOriginal, subOriginal)
      }
    loop(ls, sub, sub)
  }

  def count[A](ls: List[A])(p: A => Boolean): Int = foldLeft(ls, 0)((a, b) => if (p(b)) a + 1 else a)

  def diff[A](ls: List[A], ls2: List[A]): List[A] = foldRight(ls, Nil: List[A])((a, b) => if (contains(ls2, a)) b else Cons(a, b))

  def distinct[A](ls: List[A]): List[A] = {
    def loop(ls: List[A], res: List[A]): List[A] =
      ls match {
        case Nil => reverse(res)
        case Cons(x, xs) => if (contains(res, x)) loop(xs, res) else loop(xs, Cons(x, res))
      }
    loop(ls, Nil)
  }

  def endsWith[A](ls: List[A], sub: List[A]): Boolean = {
    def loop(ls: List[A], sub: List[A], subOrigin: List[A]): Boolean =
      (ls, sub) match {
        case (Nil, Nil) => true
        case (_, Nil) => false
        case (Nil, _) => false
        case (Cons(x, xs), Cons(y, ys)) => if (x == y) loop(xs, ys, subOrigin) else loop(xs, subOrigin, subOrigin)
      }
    if (isEmpty(sub)) true else loop(ls, sub, sub)
  }

  def exists[A](ls: List[A])(p: A => Boolean): Boolean = foldLeft(ls, false)((a, b) => if (p(b)) true else a)

  def find[A](ls: List[A])(p: A => Boolean): Option[A] = foldLeft(ls, None: Option[A])((a, b) => if (p(b)) Some(b) else a)

  def forall[A](ls: List[A])(p: A => Boolean): Boolean = foldLeft(ls, true)((a, b) => if (p(b)) a else false)

  def foreach[A](ls: List[A])(f: A => Unit): Unit = map(reverse(ls))(f)

  // def grouped[A](ls: List[A], size: Int): List[List[A]]

  def indexOf[A](ls: List[A], elem: A): Int = {
    def loop[A](ls: List[A], elem: A, n: Int): Int =
      ls match {
        case Nil => -1
        case Cons(x, xs) => if (x == elem) n else loop(xs, elem, n + 1)
      }
    loop(ls, elem, 0)
  }

  def indexOf[A](ls: List[A], elem: A, from: Int): Int = {
    def loop(ls: List[A], from: Int, res: Int): Int =
      (ls, from) match {
        case (xs, 0) =>
          val res2 = indexOf(ls, elem)
          if (res2 == -1) -1 else res2 + res
        case (Nil, _) => -1
        case (Cons(x, xs), n) => loop(xs, from - 1, res + 1)
      }
    loop(ls, from, 0)
  }

  // def indexOfSlice(ls: List[A], ls2: List[A]): Int

  // def indexOfSlice(ls: List[A], ls2: List[A], from: Int): Int

  def indexWhere[A](ls: List[A])(p: A => Boolean): Int = {
    def loop[A](ls: List[A], n: Int)(p: A => Boolean): Int =
      ls match {
        case Nil => -1
        case Cons(x, xs) => if (p(x)) n else loop(xs, n + 1)(p)
      }
    loop(ls, 0)(p)
  }

  // def indexWhere(ls: List[A], from: Int)(p: A => Boolean): Int

  def last[A](ls: List[A]): A =
    reverse(ls) match {
      case Nil => error("No last for List.")
      case xs => head(xs)
    }

  def lastOption[A](ls: List[A]): Option[A] = foldLeft(ls, None: Option[A])((a, b) => if (isEmpty(ls)) a else Some(b))

  // def lastIndexOf[A](ls: List[A], elem: A): Int
  // def lastIndexOf[A](ls: List[A], elem: A, from: Int): Int
  // def lastIndexOfSlice[A](ls: List[A], ls2: List[A]): Int
  // def lastIndexOfSlice[A](ls: List[A], ls2: List[A], from: Int): Int
  // def lastIndexWhere(ls: List[A])(p: A => Boolean): Int
  // def lastIndexWhere(ls: List[A], from: Int)(p: A => Boolean): Int

  def lengthCompare[A](ls: List[A], n: Int): Int =
    (length(ls) - n) match {
      case 0 => 0
      case x if x > 0 => 1
      case _ => -1
    }

  def max(ls: List[Int]): Int =
    ls match {
      case Nil => error("Max for empty list.")
      case Cons(x, Nil) => x
      case Cons(x1, Cons(x2, xs)) => if (x1 > x2) max(Cons(x1, xs)) else max(Cons(x2, xs))
    }

  // def maxBy[A, B](ls: List[A])(p: A => B): A

  def min(ls: List[Int]): Int =
    ls match {
      case Nil => error("Min for empty list.")
      case Cons(x, Nil) => x
      case Cons(x1, Cons(x2, xs)) => if (x1 < x2) min(Cons(x1, xs)) else min(Cons(x2, xs))
    }

  // def minBy[A, B](ls: List[A])(p: A => B): A

  def nonEmpty[A](ls: List[A]): Boolean = !(isEmpty(ls))

  def padTo[A](ls: List[A], n: Int, elem: A): List[A] = {
    def loop(n: Int, res: List[A]): List[A] =
      if (n <= 0) res
      else loop(n - 1, Cons(elem, res))
    append(ls, loop(n - length(ls), List()))
  }

  def partition[A](ls: List[A])(p: A => Boolean): (List[A], List[A]) = (filter(ls)(p), filterNot(ls)(p))

  // def slice[A](ls: List[A], from: Int, until: Int): List[A]

  def span[A](ls: List[A])(p: A => Boolean): (List[A], List[A]) = {
    def loop(ls: List[A], res1: List[A], res2: List[A]): (List[A], List[A]) =
      ls match {
        case Nil => (reverse(res1), res2)
        case Cons(x, xs) => if (p(x)) loop(xs, Cons(x, res1), xs) else (reverse(res1), res2)
      }
    loop(ls, List(), ls)
  }

  def splitAt[A](ls: List[A], n: Int): (List[A], List[A]) = {
    def loop(ls: List[A], n: Int, res1: List[A], res2: List[A]): (List[A], List[A]) =
      (ls, n) match {
        case (xs, 0) => (reverse(res1), res2)
        case (Nil, _) => (reverse(res1), res2)
        case (Cons(x, xs), n) => loop(xs, n - 1, Cons(x, res1), xs)
      }
    loop(ls, n, List(), ls)
  }

  def size[A](ls: List[A]): Int = length(ls)

  def scanLeft[A, B](ls: List[A], z: B)(f: (B, A) => B): List[B] = reverse(foldLeft(ls, List(z))((a, b) => Cons(f(head(a), b), a)))

  def scanRight[A, B](ls: List[A], z: B)(f: (A, B) => B): List[B] = foldRight(ls, List(z))((a, b) => Cons(f(a, head(b)), b))

  def union[A](ls: List[A], ls2: List[A]): List[A] = append(ls, ls2)

  // def permutations[A](ls: List[A]): List[List[A]]

  def sum(ls: List[Int]): Int = foldLeft(ls, 0)(_ + _)

  def product(ls: List[Int]): Int = foldLeft(ls, 1)(_ * _)
}
