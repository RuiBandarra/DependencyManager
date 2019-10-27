package dependencymanagement.maven.settings.cryptography.plexus;

import org.sonatype.plexus.components.cipher.PlexusCipher;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;

import java.lang.reflect.Field;

/**
 *
 * Initializes by reflection the DefaultSecDispatcher's cipher field, since we won't be using components.xml to inject it.
 * @since 1.0.0
 *
 */
public class MavenSecDispatcher extends DefaultSecDispatcher {
    public void setCipher(PlexusCipher cipher){
        try{
            Field field=DefaultSecDispatcher.class.getDeclaredField("_cipher");
            field.setAccessible(true);
            field.set(this,cipher);
        }catch(Exception e){
            throw new IllegalStateException(e);
        }
    }
}
