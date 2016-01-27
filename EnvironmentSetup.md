# Environment Setup #

## Join the GLAMM project! ##

Talk to one of the owners of the [GLAMM project on Google Code](http://code.google.com/p/glamm) and ask to be added as a committer.  It's quick and painless.

## Install Eclipse ##

The [Eclipse IDE](http://www.eclipse.org), with its associated plugins and code generation tools, is ideal for GLAMM development.  Install the latest version of the Eclipse IDE for Java Developers (at the time of this writing, it is 3.7.1 Indigo) from the [downloads page](http://www.eclipse.org/downloads/) on [eclipse.org](http://www.eclipse.org).

When you start Eclipse for the first time, you will be prompted for a default workspace location.  The workspace is where Eclipse stores your project files and settings.

## Install GWT ##

Next, you'll want to install the [Google Web Toolkit](http://code.google.com/webtoolkit/).  This is best done via Eclipse, as you'll get the SDK and Eclipse plugin simultaneously.  Google provides directions for the various eclipse releases on its [download page](http://code.google.com/eclipse/docs/download.html).  Naturally, these directions are out of date.  Only install the "Google Plugin for Eclipse" and "SDKs/Google Web Toolkit SDK 2.4.0."  For GLAMM development, everything else is superfluous.  After the plugin installs, Eclipse will restart.

## Import the GLAMM Project ##

Now that GWT is installed, select File->Import...

In the Import dialog box, select Google->Google Project Hosting.  Google will ask you for your email address and password.  The dialog box should now show projects on which you are a committer.  Choose glamm [svn](svn.md).

On a fresh install, you will be prompted to install [Subclipse](http://subclipse.tigris.org/) - a [Subversion](http://subversion.apache.org/) plugin for Eclipse.  Install the plugin, restarting Eclipse in the process.

Repeat the above steps, this time without installing Subclipse.

When prompted to select the folder to be checked out from SVN, choose "trunk."

At this point, if you click the "Finish" button, glamm will be installed into your default workspace in the directory `glamm`.  This will take some time - why not grab a coffee?

## Install Firefox ##

**BUT** Read this first!

At the time of this writing, the Google Development Plugin for Firefox on MacOS X is incompatible with the latest version of FireFox (9.0.1.)  Instead, you have to install Firefox 8.0.1.  Of course, Mozilla discourages this and doesn't make it particularly easy to find on their site, so I got it [here](http://mac.oldapps.com/firefox.php?old_firefox=859).

## SSH tunnel ##

In order to use the MicrobesOnline servers on which the GLAMM database resides, one first needs an account on said servers.  After that, it's simply a matter of setting up an ssh tunnel to that server.  I put the following in an executable shell script:

```
ssh -L 3307:mol_server_name_goes_here:3306 mol_server_name_goes_here

```

You're all set!  Check out the GLAMM development wiki to see how to develop in hosted mode.