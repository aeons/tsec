---
layout: docs
number: 1
title: "Symmetric Ciphers"
---

# Symmetric Ciphers

### Important note:
For 256-bit key sizes, you will have to install the
Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy
    
You can get it [here](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
    
You can refer to [this](https://stackoverflow.com/questions/41580489/how-to-install-unlimited-strength-jurisdiction-policy-files) for installation help 
    
Alternatively, if you are using a package manager like aptitude and have the java8 repositories on your machine,
you can install oracle-java8-unlimited-jce-policy
   
For debian-like distros:
Follow the instructions [here](http://tipsonubuntu.com/2016/07/31/install-oracle-java-8-9-ubuntu-16-04-linux-mint-18)
then use: `sudo apt-get install oracle-java8-unlimited-jce-policy`

## Usage

These are the imports you will need for basic usage:

```tut:silent
 import tsec.common._
 import tsec.cipher.symmetric._
 import tsec.cipher.symmetric.imports._
```

First, we'll use the default Encryptors, which resolve to AES/CTR/NoPadding for Non authenticated encryption,
and AES/GCM/NoPadding for Authenticated encryption. All ciphers have their Random number generator re-seeded
after a reasonable amount of uses.

```tut
  val toEncrypt = "hi hello welcome to tsec".utf8Bytes
  val onlyEncrypt: Either[CipherError, String] = for {
    instance  <- DefaultEncryptor.instance //Instances are unsafe! Some JVMs may not have particular instances
    key       <- DefaultEncryptor.keyGen.generateKey() //Generate our key
    encrypted <- instance.encrypt(PlainText(toEncrypt), key) //Encrypt our message
    decrypted <- instance.decrypt(encrypted, key)
  } yield decrypted.content.toUtf8String // "hi hello welcome to tsec!"

  /** You can also turn it into a singlular array with the IV concatenated at the end */
  val onlyEncrypt2: Either[CipherError, String] = for {
    instance  <- DefaultEncryptor.instance //Instances are unsafe! Some JVMs may not have particular instances
    key       <- DefaultEncryptor.keyGen.generateKey() //Generate our key
    encrypted <- instance.encrypt(PlainText(toEncrypt), key) //Encrypt our message
    array = encrypted.toSingleArray
    from      <- DefaultEncryptor.fromSingleArray(array)
    decrypted <- instance.decrypt(from, key)
  } yield decrypted.content.toUtf8String // "hi hello welcome to tsec!"

  /** An authenticated encryption and decryption */
  val aad = AAD("myAdditionalAuthenticationData".utf8Bytes)
  val encryptAAD: Either[CipherError, String] = for {
    instance  <- DefaultAuthEncryptor.instance //Instances are unsafe! Some JVMs may not have particular instances
    key       <- DefaultEncryptor.keyGen.generateKey() //Generate our key
    encrypted <- instance.encryptAAD(PlainText(toEncrypt), key, aad) //Encrypt our message, with our auth data
    decrypted <- instance.decryptAAD(encrypted, key, aad) //Decrypt our message: We need to pass it the same AAD
  } yield decrypted.content.toUtf8String // "hi hello welcome to tsec!"
```

For more advanced usage, i.e you know which cipher you want specifically, you must import padding.
Note: AEAD modes are separate from general cipher modes, for correctness. Thus they are a separate import

```tut
  import tsec.cipher.common.padding._
  import tsec.cipher.symmetric.imports.aead._
  val advancedUsage: Either[CipherError, String] = for {
    instance  <- JCAAEADImpure[AES128, GCM, NoPadding]
    key       <- AES128.generateKey()
    encrypted <- instance.encryptAAD(PlainText(toEncrypt), key, aad) //Encrypt our message, with our auth data
    decrypted <- instance.decryptAAD(encrypted, key, aad) //Decrypt our message: We need to pass it the same AAD
  } yield decrypted.content.toUtf8String

  /** For interpretation into any F[_]: Sync,
    * use JCASymmPure or AEADPure
    */
  import cats.effect.{IO, Sync}

  val advancedUsage2: IO[String] = for {
    instance  <- JCAAEAD[IO, AES128, GCM, NoPadding]()
    key       <- AES128.generateLift[IO]
    encrypted <- instance.encryptAAD(PlainText(toEncrypt), key, aad) //Encrypt our message, with our auth data
    decrypted <- instance.decryptAAD(encrypted, key, aad) //Decrypt our message: We need to pass it the same AAD
  } yield decrypted.content.toUtf8String
```