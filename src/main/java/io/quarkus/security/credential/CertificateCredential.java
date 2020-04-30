package io.quarkus.security.credential;

import java.security.cert.X509Certificate;

/**
 * A {@link X509Certificate} based credential 
 */
public class CertificateCredential implements Credential {

    private final X509Certificate certificate;

    public CertificateCredential(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }
}
