# Apache Karaf samples

Forked from [jbonofre/karaf-samples](https://github.com/jbonofre/karaf-samples).

## My noob notes to other noobs

### Build bundles
```
mvn clean compile org.apache.felix:maven-bundle-plugin:bundle
```

### Deploy them
Copy the built JARs to your [Apache Karaf](http://karaf.apache.org/) installation by copying them to its ```./deploy``` sub-folder.

### Start Apache Karaf
By issuing the command
```
bin/karaf
```
in your Apache Karaf installation folder. You get a Karaf shell prompt:
```
karaf@root()> Library service has been added:
Starting client
20000 Lieux Sous La Mer(Jules Vernes)
Config updated:
```
- The message about the Verne book is the doing of your ```org.apache.karaf.samples.client.Activator``` instance being started.
- The message about Config updates is the doing of your ```org.apache.karaf.samples.osgi.config.managed.Activator.ConfigDisplayer```.

### Fool around a bit with some static configuration

- Still in the karaf prompt, list the bundle states by
```
karaf@root()> list
START LEVEL 100 , List Threshold: 50
ID │ State     │ Lvl │ Version        │ Name
───┼───────────┼─────┼────────────────┼─────────────────────────────────────────
21 │ Active    │  80 │ 4.2.0.M2       │ Apache Karaf :: OSGi Services :: Event
40 │ Active    │  80 │ 4.0.6.SNAPSHOT │ simple-api
41 │ Active    │  80 │ 4.0.6.SNAPSHOT │ osgi-service-provider-bundle
42 │ Active    │  80 │ 4.0.6.SNAPSHOT │ osgi-service-simple-client-bundle
43 │ Resolved  │  80 │ 4.0.6.SNAPSHOT │ osgi-config-simple-bundle
44 │ Active    │  80 │ 4.0.6.SNAPSHOT │ osgi-service-tracker-bundle
45 │ Active    │  80 │ 4.0.6.SNAPSHOT │ osgi-config-managed-service-bundle
46 │ Installed │  80 │ 4.0.6.SNAPSHOT │ blueprint-service-provider-bundle
```
Oops, one of our bundle is not started, as it's still in ```Resolved``` state. Let's try starting it by using the ```bundle:start``` command together with the bundle ID of ```osgi-config-simple-bundle```, which, in my case, is 43.
```
karaf@root()> bundle:start 43
Error executing command: Error executing command on bundles:
	Error starting bundle 43: Activator start error in bundle org.apache.karaf.samples.osgi-config-simple-bundle [43].
```
Checking the logs with command ```log:tail``` (still at the karaf prompt), we can see an NPE, and by checking its location (```org.apache.karaf.samples.osgi.config.Activator.start(Activator.java:22)```) we get the impression that it might be failing due to the missing relevant configuration entirely, plus the fact that the code does not check for this eventuality.

Let's create that configuration, from inside Karaf, still at the karaf prompt:
```
karaf@root()> config:edit org.apache.karaf.samples.osgi
karaf@root()> config:property-set hello world
karaf@root()> config:update
```
At this point, the file ```./etc/org.apache.karaf.samples.osgi.cfg``` should exist, with the content of the single property definition ```hello = world```. Let's try starting that bundle again,
```
karaf@root()> bundle:start 43
felix.fileinstall.filename=XXX/apache-karaf-4.2.0.M2/etc/org.apache.karaf.samples.osgi.cfg
hello=world
service.pid=org.apache.karaf.samples.osgi
```
That printout greeting the world is already the Activator of that bundle, you can also check it's state again, with ```list``` command.

### Fool around more with some dynamic configuration

Let's define some properties in some other configuration, ```org.apache.karaf.samples.config```.
```
karaf@root()> config:edit org.apache.karaf.samples.config
karaf@root()> config:property-set foo bar
karaf@root()> config:property-set barf o0
karaf@root()> config:update
Config updated:
barf=o0
felix.fileinstall.filename=XXX/apache-karaf-4.2.0.M2/etc/org.apache.karaf.samples.config.cfg
foo=bar
service.pid=org.apache.karaf.samples.config
```
The listing that happens after the update, is done by the ```org.apache.karaf.samples.osgi.config.managed.Activator.ConfigDisplayer``` dynamically being notified about configuration change. And now, of course, the file ```./etc/org.apache.karaf.samples.config.cfg```should exist.

### Fool around even more with blueprint

In the listing above, ```blueprint-service-provider-bundle``` wasn't even in ```Resolved``` state, it was in ```Installed``` state which is worse.

Let's check what's wrong with it, by

```
karaf@root()> bundle:diag 46
blueprint-service-provider-bundle (46)
--------------------------------------
Status: Installed
Unsatisfied Requirements:
[org.apache.karaf.samples.blueprint-service-provider-bundle [46](R 46.0)] osgi.wiring.package; (&(osgi.wiring.package=org.osgi.service.blueprint)(version>=1.0.0)(!(version>=2.0.0)))
```
using bundle ID 46, as that corresponds to the bundle in question in my case.

We can also check the manifest headers by issuing
```
karaf@root()> bundle:headers 46

blueprint-service-provider-bundle (46)
--------------------------------------
Bnd-LastModified = 1516950151185
Build-Jdk = 1.8.0_151
Built-By = fdosarac
Created-By = Apache Maven Bundle Plugin
Manifest-Version = 1.0
Tool = Bnd-3.5.0.201709291849

Bundle-Blueprint = OSGI-INF/blueprint/blueprint.xml
Bundle-ManifestVersion = 2
Bundle-Name = blueprint-service-provider-bundle
Bundle-SymbolicName = org.apache.karaf.samples.blueprint-service-provider-bundle
Bundle-Version = 4.0.6.SNAPSHOT

Export-Service = 
	org.apache.karaf.samples.api.Library
Require-Capability = 
	osgi.ee;filter:=(&(osgi.ee=JavaSE)(version=1.5))

Export-Package = 
	org.apache.karaf.samples.blueprint.provider;
		uses:=org.apache.karaf.samples.api;
		version=4.0.6
Import-Package = 
	org.apache.karaf.samples.api;version="[4.0,5)",
	org.osgi.service.blueprint;version="[1.0.0,2.0.0)"
```
which should mark the package ```org.osgi.service.blueprint```in red, as it's still unsatisfied.

Based on the [official tutorial](http://karaf.apache.org/manual/latest/#_deploy_a_sample_application), we should just install some more stuff:
```
feature:repo-add camel 2.20.0
feature:install deployer camel-blueprint aries-blueprint
```
which should help the bundle ```blueprint-service-provider-bundle``` get started.

