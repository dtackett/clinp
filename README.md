clinp
=====

Input handling library in ClojureScript.

Default key down handlers repeat the down event based on the keyboard repeat timing. This is often insufficeint and undesired for games or interactive applications.

This library tracks what keys are held and will invoke the registered listeners when the pulse is called.


#Project Usage

##Dependency
Add dependency to project.clj. Add the following line to the :dependencies section:

```
[clinp "0.1.0-SNAPSHOT"] to the project.clj
```

##Require
Require the clinp.core namespace where the library will be used.

```
(:require
  [clinp.core :as clinp])
```

##Initialize
Initialize the library for use:

```
(clinp/setup!)
```

This sets up the key handler listeners to make everything work properly. Currently this is bound to the entire document body.

##Setup

This will setup 'my-key-down-handler-function' to be invoked when the Z key is pressed.

```
(clinp/listen! :Z :down
                   (my-key-down-handler-function))
```

This will invoke the 'my-held-handler-function' when the pulse loop is invoked while the Z key is held down.

```
(clinp/listen! :Z :pulse
                   (my-held-handler-function))
```

This will setup the 'my-key-up-handler-function; to be invoked when the Z key is released.

```
(clinp/listen! :Z :up
                   (my-key-up-handler-function))
```

##Loop
In the main loop:

```
(clinp/pulse!)
```

This will trigger the 'pulse' event for keys that are currently held.

#Building
Build the library with test:

```
lein cljsbuild once dev
````

Build the library locally:

```
lein install
```