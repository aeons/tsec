package tsec.jws

import java.util.Base64

import io.circe.Error
import tsec.jwt.jwtB64URLEncoder

trait JWSSerializer[A] {
  def serializeToUtf8(body: A): Array[Byte]
  def fromUtf8Bytes(array: Array[Byte]): Either[Error, A]
  def toB64URL(elem: A): String                     = jwtB64URLEncoder.encodeToString(serializeToUtf8(elem))
  def fromB64URL(encoded: String): Either[Error, A] = fromUtf8Bytes(Base64.getUrlDecoder.decode(encoded))
}
