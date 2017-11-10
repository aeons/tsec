package tsec

import java.util.Base64.Encoder

import io.circe.Printer

package object jwt {
  val JWTPrinter = Printer(preserveOrder = true, dropNullValues = true, "")

  // https://tools.ietf.org/html/rfc7515#page-6
  val jwtB64URLEncoder = new Encoder(true, null, -1, false)
}
