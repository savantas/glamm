# Building GLAMM #

## Versioning ##

GLAMM is versioned using a less-than-rigorous implementation of [semantic versioning](http://semver.org/), since there isn't a public API beyond that which the client uses to communicate with the server.  That said, before you attempt to build and deploy GLAMM to a server, make sure to update the version number in `war/Glamm.html`.

## Build ##

Build GLAMM like you would any GWT application:

  1. Select "GWT Compile Project..." from the "Google Services and Development Tools" menu - typically contained in the dropdown behind the blue "g" icon in the Eclipse toolbar.
  1. If it hasn't already been specified, select `Glamm` as the entry point class - you can do this by clicking the "Add..." button in the "GWT Compile" dialog box.
  1. Now, press "Compile" and watch the console for errors.  Compilation takes about a minute on a 2011 MacBook Pro.

# Deploying GLAMM #

Currently, GLAMM runs in a [Tomcat 6](http://tomcat.apache.org/) instance on two virtual machines:

  * `glamm.lbl.gov` - The production server.
  * `glamm0.jbei.lbl.gov` - The test server.

It is **strongly encouraged** that you test your changes thoroughly on the test server before deploying on production.  We do not have a formal QA process and, to the developer's continuing embarrassment, no formal testing system in place.

To deploy glamm, you must have root access on these machines (that is, you must have permission to `sudo`.)  Ensure this is the case before doing the following:

  1. Copy the contents of `glamm/war` to a temporary directory on the target server, typically with a command like this (assuming you have an account on these machines and `/users/yourusername/tmp` exists:)
```
 scp -r * yourusername@glamm0.jbei.lbl.gov:/users/yourusername/tmp
```
  1. Ensure the files are all readable by executing `chmod -R a+r *` in the temporary directory.
  1. Change directory to `/var/lib/tomcat6/webapps/release/`
  1. Create a directory for this glamm release that mirrors the version number (look at the other release directories for examples.)
  1. Copy the contents of the temporary directory into this directory (i.e. `sudo cp -r ~/tmp/* .` .)
  1. Stop Tomcat by typing `sudo /etc/init.d/tomcat6 stop`
  1. Delete the old `current` link and replace it with a link to the new directory - `sudo ln -s newdirectoryname current`
  1. Restart Tomcat by typic `sudo /etc/init.d/tomcat6 start`

The new version of GLAMM should spring to life (albeit slowly the first time) on the target server!

Please note that access to the test server is restricted to LBNL IP space, so if you're working remotely (e.g. on UCB campus) you'll need to VPN in to access GLAMM.