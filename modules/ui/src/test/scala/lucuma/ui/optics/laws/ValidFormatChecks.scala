// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.ui.optics.laws

import cats.Eq
import cats.data.Validated
import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import cats.syntax.all._
import lucuma.core.optics.laws._
import lucuma.ui.optics.ValidFormat

final case class ValidFormatChecks[E, T, A](validate: ValidFormat[E, T, A]) {

  def normalizeLaw(t: T): IsEq[Validated[E, Validated[E, A]]] =
    validate.normalize(t).map(validate.getValidated) <-> validate
      .getValidated(t)
      .map(Validated.valid[E, A])

  def parseRoundTripLaw(t: T): IsEq[Validated[E, Validated[E, T]]] = {
    val vt = validate.normalize(t)
    vt.map(validate.getValidated).map(_.map(validate.reverseGet)) <-> vt.map(Validated.valid[E, T])
  }

  def formatRoundTripLaw(a: A): IsEq[Validated[E, A]] =
    validate.getValidated(validate.reverseGet(a)) <-> Validated.valid(a)

  // True if `t` is invalid, or if it is valid but not in normal form. The existence of such a value
  // in our test data will show that `normalize` and `parseRoundTrip` are actually testing something.
  def ensureValidationOrNormalizationCheck(t: T)(implicit ev: Eq[T]): Boolean =
    validate.getValidated(t).map(validate.reverseGet) match {
      case Invalid(_) => true
      case Valid(tʹ)  => t =!= tʹ
    }

}
