package io.quarkus.security;

import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.smallrye.common.annotation.Experimental;

/**
 * A CDI event that facilitates programmatic security setup.
 * The event can be observed with a synchronous observer method like in the example below:
 *
 * <pre>
 * {@code
 * import io.quarkus.security.identity.IdentityProvider;
 * import io.quarkus.security.identity.SecurityIdentityAugmentor;
 * import io.quarkus.security.identity.request.TokenAuthenticationRequest;
 * import jakarta.enterprise.event.Observes;
 *
 * public class SecurityConfiguration {
 *
 *     void observe(@Observes Security security) {
 *         SecurityIdentityAugmentor augmentor = createCustomIdentityAugmentor();
 *         IdentityProvider<TokenAuthenticationRequest> identityProvider = createCustomIdentityProvider();
 *         security.identityAugmentor(augmentor).identityProvider(identityProvider);
 *     }
 *
 * }
 * }
 * </pre>
 */
@Experimental("This API is currently experimental and might get changed")
public interface Security {

    /**
     * Registers given {@link IdentityProvider}.
     *
     * @param identityProvider {@link IdentityProvider}
     * @return Security
     */
    Security identityProvider(IdentityProvider<?> identityProvider);

    /**
     * Registers given {@link SecurityIdentityAugmentor}.
     *
     * @param securityIdentityAugmentor {@link SecurityIdentityAugmentor}
     * @return Security
     */
    Security identityAugmentor(SecurityIdentityAugmentor securityIdentityAugmentor);

}
