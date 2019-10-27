package dependencymanagement.maven.settings;

import dependencymanagement.maven.settings.cryptography.plexus.MavenSecDispatcher;
import dependencymanagement.maven.settings.cryptography.plexus.MavenSettingsDecrypter;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.*;
import org.apache.maven.settings.crypto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * Reads and holds Maven's Local Settings. Singleton.
 * @since 1.0.0
 *
 */
@Singleton
public class MavenSettingsReader {
    private final Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    private Settings mavenSettings;

    private static MavenSettingsReader instance=null;

    public Settings getMavenSettings() {
        return mavenSettings;
    }

    public static MavenSettingsReader getInstance() {
        if(instance==null){
            instance=new MavenSettingsReader();
        }
        return instance;
    }

    public String getMavenHomeGlobal(){
        final String MAVEN_HOME="M2_HOME";
        //Try M2_HOME
        String mavenHomeGlobal=System.getenv().get(MAVEN_HOME);

        //Try system call to mvn
        if(mavenHomeGlobal==null || mavenHomeGlobal.equals("") || !new File(mavenHomeGlobal).isDirectory()){
            mavenHomeGlobal=executeSystemCall("mvn help:evaluate -Dexpression=maven.home -q -DforceStdout");
            if(mavenHomeGlobal==null || mavenHomeGlobal.equals("") || !new File(mavenHomeGlobal).isDirectory()){
                logger.error("Couldn't find Global Maven Home. Set "+MAVEN_HOME+" as system environment variable or make mvn available to Minder by putting it in the System Path.");
                mavenHomeGlobal="";
            }
        }

        return mavenHomeGlobal;
    }

    public String getMavenHomeUser(){
        final String USER_HOME="user.home";
        //Try system variable method
        String mavenHomeUser=System.getProperty(USER_HOME)+File.separator+".m2";

        if(mavenHomeUser==null || mavenHomeUser.equals("") || !new File(mavenHomeUser).isDirectory()){
            //Try system call to mvn
            String mavenUserRepository=executeSystemCall("mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout");
            mavenHomeUser=mavenUserRepository+".."+File.separator;
            if(mavenHomeUser==null || mavenHomeUser.equals("") || !new File(mavenHomeUser).isDirectory()){
                logger.error("Couldn't find Maven User Home. Set "+USER_HOME+"as a system environment variable or make mvn available to Minder by putting it in the System Path.");
                mavenHomeUser="";
            }
        }
        return mavenHomeUser;
    }

    private MavenSettingsReader(){
        Settings mavenSettings=null;
        try {
            SettingsBuildingRequest settingsRequest = new DefaultSettingsBuildingRequest();

            //Global settings
            String mavenHomeGlobal= getMavenHomeGlobal();
            if(!mavenHomeGlobal.equals("")){
                String globalSettingsLocation=mavenHomeGlobal+File.separator+"conf"+File.separator+"settings.xml";
                File globalSettingsFile=new File(globalSettingsLocation);
                if (globalSettingsFile.exists()){
                    settingsRequest.setGlobalSettingsFile(globalSettingsFile);
                }else{
                    logger.error("Couldn't find Global Maven Settings File: "+globalSettingsLocation);
                }
            }

            //User settings
            String mavenHomeUser= getMavenHomeUser();
            if(!mavenHomeUser.equals("")){
                //User Settings
                String userSettingsLocation=mavenHomeUser+File.separator+"settings.xml";
                File userSettingsFile=new File(userSettingsLocation);
                if (userSettingsFile.exists()){
                    settingsRequest.setUserSettingsFile(userSettingsFile);
                }else{
                    logger.error("Couldn't find User Maven Settings File: "+userSettingsLocation);
                }
            }

            SettingsBuilder settingsBuilder = new DefaultSettingsBuilderFactory().newInstance();
            SettingsBuildingResult settingsBuildingResult = settingsBuilder.build(settingsRequest);
            mavenSettings = settingsBuildingResult.getEffectiveSettings();
            this.mavenSettings=mavenSettings;

            if(mavenSettings.getServers().size()>0){
                try {
                    DefaultSettingsDecryptionRequest decryptionRequest= new DefaultSettingsDecryptionRequest( mavenSettings );
                    MavenSecDispatcher secDispatcher = new MavenSecDispatcher();
                    secDispatcher.setCipher(new DefaultPlexusCipher());
                    secDispatcher.setConfigurationFile(mavenHomeUser+File.separator+"settings-security.xml");
                    MavenSettingsDecrypter settingsDecrypter=new MavenSettingsDecrypter();
                    settingsDecrypter.setSecurityDispatcher(secDispatcher);
                    SettingsDecryptionResult decryptionResult = settingsDecrypter.decrypt(decryptionRequest);
                    mavenSettings.setServers(decryptionResult.getServers());
                    this.mavenSettings=mavenSettings;
                } catch (PlexusCipherException e) {
                    logger.error("Error decrypting the server passwords. "+e.getMessage());
                }
            }

            String localRepository=mavenHomeUser+File.separator+"repository";
            if(new File(localRepository).isDirectory()){
                mavenSettings.setLocalRepository(localRepository);
            }
        }catch(SettingsBuildingException settingsBuildingException) {
            logger.error("Error Building Maven Settings: "+settingsBuildingException.getMessage());
        }
    }

    private String executeSystemCall(String systemCall){
        Runtime r = Runtime.getRuntime();
        Process p;
        String systemCallResult="";
        try {
            p = r.exec(systemCall);
            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            systemCallResult = b.readLine();
            b.close();
        } catch (IOException|InterruptedException exception) {
            logger.error("Exception executing command: "+ systemCall + "\n"+ exception.getMessage());
        }
        return systemCallResult;
    }
}
