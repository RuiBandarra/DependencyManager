package dependencymanagement.maven.settings.cryptography.plexus;

import org.apache.maven.settings.crypto.DefaultSettingsDecrypter;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;

import java.lang.reflect.Field;

/**
 *
 * Initializes by reflection the DefaultSettingsDecrypter's field securityDispatcher, since we won't be using components.xml to inject it.
 * @since 1.0.0
 *
 */
public class MavenSettingsDecrypter extends DefaultSettingsDecrypter {
        public void setSecurityDispatcher( SecDispatcher securityDispatcher ){
            try{
                Field field = DefaultSettingsDecrypter.class.getDeclaredField( "securityDispatcher" );
                field.setAccessible( true );
                field.set( this, securityDispatcher );
            }
            catch( Exception e ){
                throw new IllegalStateException( e );
            }
        }
}
