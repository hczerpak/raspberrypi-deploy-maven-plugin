# raspberrypi-deploy-maven-plugin
Maven plugin for deploying to Raspberry Pi available on local network

### Configuration

If you're using this plugin for deploying on raspberrypi in your local 
area network you don't really have to set anything. Some reasonable 
default settings already exist. 
By default the plugin looks up a computer with network name `raspberrypi`,
tries to use standard login/password combination of `pi/raspberry` and
uses user's home directory to deploy project files using pattern 
`${project.name}/target` which should look familiar to Maven users.

### Maven dependency snippet

```
<plugin>
    <groupId>com.hczerpak.maven.plugins</groupId>
    <artifactId>raspberrypi-deploy-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <phase>deploy</phase>
            <goals>
                <goal>deploy</goal>
            </goals>
        </execution>
        <execution>
            <phase>clean</phase>
            <goals>
                <goal>clean</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```