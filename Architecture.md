# Introduction #

This document outlines the relationship between GLAMM's architecture and its source code at the package level.

At its core, GLAMM is a fairly conventional [Google Web Toolkit](http://code.google.com/webtoolkit/) (GWT) application.  That is, it adheres to the [Model-View-Presenter](http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) design pattern on the client side, and communicates with a server via GWT's asynchronous [RPC](http://en.wikipedia.org/wiki/Remote_procedure_call) service.

# GWT RPC #

To get a feel for how GWT's RPC mechanism works, take a look at [this document](http://code.google.com/webtoolkit/doc/latest/DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls).  The most important thing to remember is that these calls are non-blocking.  That is, when a call to an RPC service is made, the request is sent to the server and the client side code continues to execute.  When a response is received, it is handled by callback methods specified by the client.

In GLAMM, the RPC interface is defined here:
```
gov.lbl.glamm.client.rpc.GlammService.java
gov.lbl.glamm.client.rpc.GlammServiceAsync.java
```

and implemented here:
```
gov.lbl.glamm.server.GlammServiceImpl.java
```

When extending GLAMM's RPC capabilities, make sure to include method definitions in all three places!

# Client #

## Module Information ##

GLAMM is written as a single GWT module, with the module XML definition located here:
```
gov.lbl.glamm.Glamm.gwt.xml
```

Its entry point class is
```
gov.lbl.glamm.client.Glamm
```

This class is invoked when the module is loaded and, after displaying a splash screen, hands control over to the application controller class.

## Application Controller ##

```
gov.lbl.glamm.client.AppController
```

The application controller class provides the glue that allows models, views, and presenters to communicate with each other.  It is a single instance object with no public constructor, and this instance is never accessed directly, except by the entry point class.  It serves the following purposes:

  * Creation of the main panel (the panel into which all other views will be drawn.)
  * Creation of the RPC service interface.
  * Creation of the event bus that allows presenters to communicate with each other.
  * Instantiation of all presenters and views.
  * Attachment of event handlers to the presenters.

You will notice the `start` method calls a number of `load` methods.  Most of these add views to the main panel, but all of them add handlers to the event bus whose callbacks implement presenter-specific behaviors.

## Events ##

```
gov.lbl.glamm.client.events
```

As mentioned previously, presenters communicate with each other via events dispatched to an EventBus object instantiated by the ApplicationController.

Event classes extend `com.google.gwt.event.shared.GwtEvent`, a generic class that takes an extension of the `com.google.gwt.event.shared.EventHandler` interface as its parameter.  GLAMM typically defines this interface inside the event class.  GLAMM's custom events typically provide model objects as payloads, accessible through the callbacks specified by the EventHandler.

## Presenters ##

```
gov.lbl.glamm.client.presenter
```

Presenters bind model data to views and communicate with each other via the event bus.  GLAMM presenters define an interface for their view (typically called `View`) which provides read-only access to the various GWT widgets contained in the view.  The event handlers for these widgets are bound by the presenters in a method called `bindView`.  Presenters also communicate directly with the server via the RPC interface passed in with the constructor.

With few exceptions, presenters should not directly instantiate or contain references to each other - communication should only occur via events dispatched by the event bus.

## Models ##

```
gov.lbl.glamm.client.model
```

In GLAMM, models are often little more than Data Transfer Objects (DTOs.)  They provide a means of describing domain-specific data and getting it between the client and the server.  For the most part, they consist of a set of fields, along with "getter" and "setter" methods for accessing those fields.  Interfaces provided in `gov.lbl.glamm.client.model.interfaces` are contracts, provided to ensure the availability of synonyms, xrefs, measurements, etc for models which are expected to have them (e.g. `Compound`, `Reaction`, `Gene`)

## Views ##

```
gov.lbl.glamm.client.view
```

Views are collections of widgets that define a user interface.  To GLAMM, Views are effectively "dumb" collections of user interface widgets.

GLAMM views typically follow Google's recommendation that they extend `com.google.gwt.user.client.ui.Composite`.  This class effectively hides the implementation details of the underlying widgets.  The one exception is in the case of popup windows, in which case the view extends `com.google.gwt.user.client.ui.PopupPanel` or one of its derived classes.

Views also implement the `View` interface defined in the presenter.  This interface should be used to expose widget interfaces within the view to the presenter for the purpose of setting state, adding event handlers, etc.  This logic should be handled by the presenter - the view should not contain any state management beyond layout and (CSS) style.

# Server #

## Configuration ##

```
gov.lbl.glamm.server
```

In order to serve different projects and connect them with the datasets they expect, the GLAMM server was designed to respond to requests from certain domains with data from different sources.  To accommodate this, GLAMM has a fairly involved server configuration and session management scheme.

The `ServerConfig` class is designed to make this sort of routing as painless as possible.  By providing a persistent way to store these data through the duration of a session, the configuration for a specific domain need only happen once.

Configuration data for the server lives in `war/config/server_config.xml`.  Here is an example of an abbreviated `server_config.xml` file:

```
config>
	<server 
		serverdomain="127.0.0.1" 
		hibernatecfg="hibernate-debug.cfg.xml"
		isolatehost="microbesonline.org" 
		loginurl="http://microbesonline.org/cgi-bin/login"
		metagenomehost="meta.microbesonline.org" 
		db="debug"/>
	<db 
		name="debug" 
		uri="jdbc:mysql://127.0.0.1:3307/genomics_test" 
		driver="org.gjt.mm.mysql.Driver" 
		user="test" 
		passwd="test"/>
</config>
```

In order to fully specify server behavior, one needs both the `server` element and one ore more `db` elements.  The attributes are defined as follows:

| Tag | Attribute | Value |
|:----|:----------|:------|
| `server` | `serverdomain` | The request domain name for which this server configuration applies. |
| `server` | `hibernatecfg` | The hibernate configuration file name. |
| `server` | `isolatehost` | The isolate genome host name. |
| `server` | `loginurl` | The MicrobesOnline login URL. |
| `server` | `metagenomehost` | The metagenome host name. |
| `server` | `db`      | The JDBC configuration name, specified by the `db` element's `name` attribute. |
| `db` | `name`    | Name identifying this JDBC configuration. |
| `db` | `uri`     | The JDBC URI. |
| `db` | `driver`  | The JDBC driver. |
| `db` | `user`    | The user name. |
| `db` | `passwd`  | The password. |

## Session Management ##

```
gov.lbl.glamm.server.GlammSession.java
```

`GlammSession.java` provides a mechanism for managing session-specific state on the server side.  This is especially important when dealing with user-uploaded data, as there is currently no way to save this information in the database, nor are there policies in place for dealing with _any_ persistent client state.  As such, `GlammSession` instances are often required by Data Access Objects.  Fortunately, the creation and retrieval of `GlammSession` objects is trivial: use the static method `GlammSession.getGlammSession(HttpServletRequest request)`.

## RPC Servlet ##

```
gov.lbl.glamm.server.GlammServiceImpl.java
```

As described above, `GlammServiceImpl` defines the server side of the GWT RPC interface.  Instead of handling the application logic directly in the servlet implementation, it retrieves session configuration information and routes the request to the appropriate service class.

## Service Classes ##

```
gov.lbl.glamm.server.actions
```

Rather than invoking RPC methods directly from the servlet, GLAMM defines a package containing a number of service classes, with each class containing a static method that serves to handle the call.  This way, the RPC servlet does not become unnecessarily cluttered with application logic.

Should GLAMM be integrated into a service-oriented architecture as part of some larger effort, the service classes provide an obvious place to implement such a transition.

## Data Access Objects ##

```
gov.lbl.glamm.server.dao
gov.lbl.glamm.server.dao.impl
```

GLAMM's service classes require data from a diverse set of data sources (e.g. [MicrobesOnline](http://microbesonline.org)).  To facilitate integration of new data sources, as well as re-use code when possible, GLAMM has a Data Access Object (DAO) layer, thus decoupling data access from the service layer.  DAO implementations are defined in `gov.lbl.glamm.server.dao.impl`  Typically, one DAO interface is defined for each model type, although each interface may have multiple implementations.  Implementations may also be composed, to draw from multiple data sources.  As with the service classes, this layer may also be parceled out in a service-oriented architecture, serving data from a variety of sources.

## HTTP Servlet ##

```
gov.lbl.glamm.server.GlammServlet.java
```

In addition to the RPC service implementation as described above, GLAMM includes a conventional HttpServlet for handing form data and file uploads, implemented in `GlammServlet`.  Requests are handled by `RequestHandler` objects, generated by the `RequestHandlerFactory` class.  The mapping between actions and objects is specified in `war/config/request_handlers.xml`.  If you decide to add support for additional non-RPC requests, it is not necessary to modify `GlammServlet` or `RequestHandlerFactory` - simply add a mapping to the configuration file and write the corresponding `RequestHandler` implementation.

# Database #

The GLAMM database is one of the highlights and glaring weaknesses of the project.  Some of the biggest gains in application flexibility will come from addressing its shortcomings.

## Design ##

The GLAMM database was designed with the goal of unifying any suitably annotated compound, reaction, and pathway databases.  Specifically, it was designed to accommodate KEGG, MetaCyc, and BiGG.  The approach the author (johntbates) chose, at least in the case of compounds, merged relevant data from all of the sources into single compound entries where possible.  A more practical and flexible approach would have been to import these entries separately and reconciling them via a cross-reference table, while maintaining the database-specific id information in the entries.

Similarly, the database tags most of its entries with a `guid` field, making it distinct from all other entries in the database, _regardless of table_.  In hindsight, this approach is obviously wrong. as it makes modification of the database, once imported, nearly impossible.

What should be clear, by this point, is that the database was designed and implemented prior to the GLAMM application itself.  A complete overhaul might be worth considering.

## Assembly ##

The GLAMM database is currently built by a perl program: `glamm/db/bin/genGlammDb.pl`.  It parses the flat-file representations of the KEGG, MetaCyc, and BiGG databases and outputs them in SQL tables, ready for import by the SQL scripts found in `db/sql`.

## JDBC ##

The GLAMM application does its best to abstract away the details of working with the GLAMM and MicrobesOnline databases by wrapping the JDBC interfaces to this data in Data Access Objects (DAOs.)  Connections to these databases are managed by the `GlammDbConnectionPool`, a wrapper for [c3p0](http://www.mchange.com/projects/c3p0/index.html).

## Hibernate ##

A better approach has been adopted for recent additions to the database in the form of the [Hibernate](http://www.hibernate.org/) Object-Relational Mapper (ORM.)  If you are unfamiliar with Hibernate, there may be a bit of a learning curve, but it has excellent documentation and a very active community.

The attempt to replace the previous "parse-with-perl, manually import, access with JDBC" methodology with Hibernate have been quite successful so far with new additions to the database.  You can find this code in `gov.lbl.glammdb`.  Using Hibernate meshes well with the DAO interface strategy.