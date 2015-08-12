Key Chain Management
-------------------

This project show us how to create an install a certificate to sign.

How to create an install a certificate with android

* Prerequisites

- Create a pfx.

```sh
$ keytool -genkeypair -alias MyCertificate -keystore MyKeyStore.pfx -storepass thepassword -validity 10000 -keyalg RSA -keysize 2048 -storetype pkcs12

```
