// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.ui.enum

import cats.syntax.all._
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import lucuma.core.util.Enumerated

sealed abstract class ExecutionEnvironment(val suffix: Option[NonEmptyString])
    extends Product
    with Serializable
object ExecutionEnvironment {
  case object Development extends ExecutionEnvironment(NonEmptyString("DEV").some)
  case object Staging     extends ExecutionEnvironment(NonEmptyString("STG").some)
  case object Production  extends ExecutionEnvironment(none)

  /** @group Typeclass Instances */
  implicit val ExecutionEnvironmentEnumerated: Enumerated[ExecutionEnvironment] =
    Enumerated.of(Development, Staging, Production)
}
