// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.ui.optics

import cats.data.NonEmptyChain
import cats.data.Validated
import cats.data.ValidatedNec
import cats.syntax.all._
import lucuma.core.optics.Format
import monocle.Iso
import monocle.Prism

/**
 * Convenience version of `ValidFormat` when the error type is `NonEmptyChain[E]`.
 */
object ValidFormatNec extends ValidFormatNecInstances {

  /**
   * Build optic that's always valid and doesn't normalize or format
   */
  def id[E, A]: ValidFormatNec[E, A, A] = fromIso(Iso.id[A])

  /**
   * Build optic from getValidated and reverseGet functions.
   */
  def apply[E, T, A](
    getValidated: T => ValidatedNec[E, A],
    reverseGet:   A => T
  ): ValidFormatNec[E, T, A] =
    ValidFormat(getValidated, reverseGet)

  /**
   * Build optic from a Format
   */
  def fromFormat[E, T, A](
    format: Format[T, A],
    error:  E
  ): ValidFormatNec[E, T, A] =
    ValidFormat(
      format.getOption.andThen(o => Validated.fromOption(o, NonEmptyChain(error))),
      format.reverseGet
    )

  /**
   * Build optic from a Prism
   */
  def fromPrism[E, T, A](
    prism: Prism[T, A],
    error: E
  ): ValidFormatNec[E, T, A] =
    fromFormat(Format.fromPrism(prism), error)

  /**
   * Build optic from a Iso
   */
  def fromIso[E, T, A](iso: Iso[T, A]): ValidFormatNec[E, T, A] =
    ValidFormat(
      (iso.get _).andThen(_.valid),
      iso.reverseGet
    )

}
