Simple Generic Library for Java
===============================

This library includes a series of **utility classes** for
* extensible data structures (multimaps, AVL trees, disjoint sets, timeout queues)
* discrete math (polymorphic arithmetic, some group theory abstractions like rings and fields)
* convenient primitive to/from reference type conversion
* String(Builder)

and several **simple but universal APIs** for:
* service-based Remote Procedure Call (platform-*independent*, unlike Java's RMI)
* messaging pipeline (MVC based; console views, filters, loggers, front-end interface)
* tasks management & synchronization (MVC based; managers, executors, front-end interface)
* string searching (incremental search with listeners, suffix trees)
* some GUI: 
  * setup dialogs (Swing-based)
  * progress bars (GUI extension to the task API)

The APIs are grouped into packages that can be imported as standalone JARs, to minimize dependencies.
