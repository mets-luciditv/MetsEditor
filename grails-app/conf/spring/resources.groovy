import metseditor.UserPasswordEncoderListener
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextPersistenceFilter
import org.springframework.security.web.session.ConcurrentSessionFilter
import org.springframework.security.web.session.SessionManagementFilter
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener, ref('hibernateDatastore'))

    sessionRegistry(SessionRegistryImpl)
    concurrentSessionFilter(ConcurrentSessionFilter,ref('sessionRegistry'),"/login/auth")
    simpleRedirectInvalidSessionStrategy(SimpleRedirectInvalidSessionStrategy,"/login/auth")

    securityContextRepository(HttpSessionSecurityContextRepository)
    sessionManagementFilter(SessionManagementFilter, ref('securityContextRepository')) {
        invalidSessionStrategy=ref('simpleRedirectInvalidSessionStrategy')
    }
    securityContextPersistenceFilter(SecurityContextPersistenceFilter, ref('securityContextRepository')) {
        forceEagerSessionCreation = true
    }

}
