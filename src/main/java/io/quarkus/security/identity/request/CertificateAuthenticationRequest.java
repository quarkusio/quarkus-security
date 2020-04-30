package io.quarkus.security.identity.request;

import java.security.cert.X509Certificate;

import io.quarkus.security.credential.CertificateCredential;

/**
 * A {@link AuthenticationRequest} to authenticate from a {@link CertificateCredential}, such as when authenticating clients through TLS
 */
public class CertificateAuthenticationRequest implements AuthenticationRequest {

    private final CertificateCredential certificate;

    public CertificateAuthenticationRequest(CertificateCredential certificate) {
        this.certificate = certificate;
    }

    public CertificateCredential getCertificate() {
        return certificate;
    }
}
