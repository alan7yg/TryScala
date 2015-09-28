package com.kaihaosw

class Rational(val numer: Int, val denom: Int) extends Ordered[Rational] {
  private val g = gcd(numer, denom)

  def this(numer: Int) = this(numer, 1)

  def + (that: Rational): Rational =
    new Rational(
      numer * that.denom + denom * that.numer,
      denom * that.denom
    )

  def + (n: Int): Rational = new Rational(numer + denom * n, denom)

  def - (that: Rational): Rational =
    new Rational(
      numer * that.denom - denom * that.numer,
      denom * that.denom
    )

  def - (n: Int): Rational = new Rational(numer - denom * n, denom)

  def * (that: Rational): Rational =
    new Rational(
      numer * that.numer,
      denom * that.denom
    )

  def * (n: Int): Rational = new Rational(numer * n, denom)

  def / (that: Rational): Rational = this.*(new Rational(that.denom, that.numer))
  def / (n: Int): Rational = this.*(new Rational(1, n))

  def compare(that: Rational): Int = (that.denom * numer) - (denom * that.numer)
  def compare(n: Int): Int = this.compare(new Rational(n))

  override def toString = {
    if (numer * denom < 0)
      "-" + numer.abs + "/" + denom.abs
    else
      numer + "/" + denom
  }

  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd (b, a % b)
}

object Rational {
  implicit def intToRational(n: Int): Rational = new Rational(n)
}
