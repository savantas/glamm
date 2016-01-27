# Introduction #

Google already has a great [introduction](http://code.google.com/webtoolkit/doc/latest/DevGuideCompilingAndDebugging.html#launching_in_dev_mode) for developing apps in development mode, but there are a few details one needs to manage when developing for GLAMM.

# GLAMM in GWT Development Mode #

Here's how to get GLAMM up and running in development mode:

  1. Open up the SSH tunnel to a MicrobesOnline server.
  1. Make sure the topmost glamm directory is selected in [Eclipse](http://www.eclipse.org)'s Package Explorer.
  1. Click on "Debug As..." in the Eclipse toolbar - it's the icon that looks like a bug.
  1. Copy the URL from the "Development Mode" tab at the bottom of the screen into Firefox's URL bar - hit enter.
  1. Install the developer plugin, if necessary (this wil only happen the first time Firefox is started.)
  1. Wait... and I do mean wait... development mode is _slow_.
  1. Eventually, you can interact with the web application in the browser, set breakpoints in the debugger, examine stack traces, view exceptions: everything you'd expect to be able to do with an ordinary Java application.  Both client and server code can be altered on the fly, although GWT may occasionally prompt you to restart the program if modifications to the client are too extensive.

# Stopping a development mode instance #

Click the red square icon in the development mode tab toolbar at the bottom of the screen.

# Committing code to SVN #

After you've made some changes and are reasonably satisfied they work, it makes sense to put them into the repository.  Simply right-click on the topmost directory containing all your changes in the Package Explorer, and select Team->Commit... and follow the directions in the dialog box.  GLAMM's project workflow is fairly ad-hoc - it's a research project, after all!