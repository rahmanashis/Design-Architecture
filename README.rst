Design Architecture: Python Learning Repository
===============================================

.. image:: docs/architecture-flow.svg
   :alt: Animated learning path from SOLID principles to design patterns and Onion Architecture
   :align: center

A small, runnable Python study repository for learning how software design ideas
fit together. It starts with the SOLID principles, applies common design
patterns in focused examples, and finishes with a single-file Onion Architecture
demonstration.

The examples are intentionally simple. They are meant to be read, run, changed,
and compared rather than treated as production-ready frameworks.

.. contents:: Table of Contents
   :depth: 2
   :local:

Learning path
-------------

Follow this order if you are visiting the repository for the first time:

#. Read the SOLID notebook to learn the vocabulary of responsibility,
   extension, substitution, and abstraction.
#. Open the ``without_*`` examples in ``Design_Patterns`` to see the problem
   each pattern is addressing.
#. Read the matching pattern implementation and run it.
#. Finish with ``Onlion_Architecture/onion.py`` to see domain entities,
   repository abstractions, services, controllers, and dependency injection in
   one example.

.. note::

   The directory names ``AbstructFactory`` and ``Onlion_Architecture`` preserve
   the names currently used by the repository. They are spelling variants of
   ``AbstractFactory`` and ``Onion_Architecture``; do not rename them when
   following the paths below.

What is in this repository?
---------------------------

.. list-table::
   :header-rows: 1
   :widths: 24 56 20

   * - Area
     - What to learn
     - Start here
   * - SOLID
     - Five principles for reducing coupling and keeping responsibilities clear.
     - ``Solid/SOLID_principals.ipynb``
   * - Design Patterns
     - Small Python examples that contrast a direct approach with a reusable
       pattern.
     - ``Design_Patterns/AbstructFactory/``
   * - Onion Architecture
     - Dependency direction between domain, infrastructure, application
       services, and presentation controllers.
     - ``Onlion_Architecture/onion.py``

Repository map
--------------

::

   Design-Architecture/
   +-- README.rst                         # This guide
   +-- LICENSE
   +-- docs/
   |   +-- architecture-flow.svg          # Animated overview diagram
   +-- Design_Patterns/
   |   +-- AbstructFactory/
   |       +-- AbstructFactory.py         # Abstract Factory
   |       +-- Factory/
   |       |   +-- Factory_1.py            # Factory implementation
   |       |   +-- withoutFactory_1.py    # Direct approach
   |       +-- Iterator/
   |       +-- Memento/
   |       +-- Observer/
   |       +-- Prototype/
   |       +-- Singleton/
   |       +-- Strategy/
   |       +-- Template/
   +-- Solid/
   |   +-- SOLID_principals.ipynb         # Guided notebook
   +-- Onlion_Architecture/
       +-- onion.py                       # Complete layered example

Prerequisites
-------------

Required:

* Python 3.9 or newer. Python 3.9+ is recommended because the Onion example
  uses built-in generic annotations such as ``list[Course]``.
* A terminal and a text editor. VS Code is convenient but not required.
* Git, if you want to clone the repository or contribute changes.

Optional:

* Jupyter Notebook or JupyterLab for ``Solid/SOLID_principals.ipynb``.
* A Python extension and a Jupyter extension in VS Code for interactive cells.

There are currently no third-party runtime dependencies for the Python scripts.
The pattern examples use only the standard library, especially ``abc``.

Setup
-----

Windows PowerShell::

   git clone https://github.com/rahmanashis/Design-Architecture.git
   cd Design-Architecture
   py -3 -m venv .venv
   .venv\\Scripts\\Activate.ps1
   python --version

Windows Git Bash::

   git clone https://github.com/rahmanashis/Design-Architecture.git
   cd Design-Architecture
   python -m venv .venv
   source .venv/Scripts/activate
   python --version

macOS or Linux::

   git clone https://github.com/rahmanashis/Design-Architecture.git
   cd Design-Architecture
   python3 -m venv .venv
   source .venv/bin/activate
   python --version

No ``pip install`` step is needed for the scripts. To work with the notebook,
install Jupyter only when you need it::

   python -m pip install --upgrade pip
   python -m pip install jupyter

How to run the examples
-----------------------

Run the Onion Architecture demonstration::

   python Onlion_Architecture/onion.py

It uses an in-memory ``Database`` and prints the flow through controllers,
services, and repositories. No external database is required. The sample also
tries to add a duplicate student ID so that the service-layer validation is
visible in the output.

Run a pattern example directly::

   python Design_Patterns/AbstructFactory/Factory/Factory_1.py
   python Design_Patterns/AbstructFactory/Iterator/iterator.py
   python Design_Patterns/AbstructFactory/Memento/memento.py
   python Design_Patterns/AbstructFactory/Observer/Observer.py
   python Design_Patterns/AbstructFactory/Singleton/Singleton.py
   python Design_Patterns/AbstructFactory/Strategy/strategy.py
   python Design_Patterns/AbstructFactory/Template/template.py

Some examples request values with ``input()``. The Factory and Strategy examples
use sender, receiver, message, and a method such as ``email``, ``sms``, or
``push``. Follow the prompt shown in the terminal.

Run the Abstract Factory example::

   python Design_Patterns/AbstructFactory/AbstructFactory.py

Open the notebook with Jupyter::

   jupyter notebook Solid/SOLID_principals.ipynb

Or open ``Solid/SOLID_principals.ipynb`` in VS Code and run the cells from top
to bottom. The notebook contains Markdown explanations and executable Python
cells for SRP, OCP, and LSP, followed by a summary of ISP and DIP.

Design patterns included
------------------------

Abstract Factory
~~~~~~~~~~~~~~~~

Path: ``Design_Patterns/AbstructFactory/AbstructFactory.py``

Creates related objects as a family. The example chooses an email, SMS, or push
factory. Each factory creates a sender and a formatter that belong together.

Factory
~~~~~~~

Path: ``Design_Patterns/AbstructFactory/Factory/``

Centralizes object creation so the client does not need to instantiate each
sender class directly. Compare ``Factory_1.py`` with ``withoutFactory_1.py``.

Iterator
~~~~~~~~

Path: ``Design_Patterns/AbstructFactory/Iterator/``

Encapsulates traversal so client code can iterate over a collection without
knowing how that collection stores its items. Compare the ``iterator.py`` and
``without_iterator.py`` versions.

Memento
~~~~~~~

Path: ``Design_Patterns/AbstructFactory/Memento/``

Demonstrates preserving and restoring an object's state. The comparison file
shows the cost of handling state history without a dedicated abstraction.

Observer
~~~~~~~~

Path: ``Design_Patterns/AbstructFactory/Observer/``

Shows one-to-many notification: observers are informed when a subject changes.
The paired files make the coupling difference visible.

Prototype
~~~~~~~~~

Path: ``Design_Patterns/AbstructFactory/Prototype/``

Introduces object creation by copying an existing object. The current example
is a focused starting point for studying cloning and independent state.

Singleton
~~~~~~~~~

Path: ``Design_Patterns/AbstructFactory/Singleton/``

Shows controlled access to a single shared instance. Read this example with
care: Singleton can introduce global state and should be used sparingly.

Strategy
~~~~~~~~

Path: ``Design_Patterns/AbstructFactory/Strategy/``

Encapsulates interchangeable behavior. Here, different message senders share a
common interface while the factory selects the concrete strategy.

Template Method
~~~~~~~~~~~~~~~

Path: ``Design_Patterns/AbstructFactory/Template/``

Defines a stable algorithm structure while allowing subclasses to customize a
specific step. Compare ``template.py`` with ``without_template.py``.

Onion Architecture walkthrough
------------------------------

The file ``Onlion_Architecture/onion.py`` is intentionally self-contained. Its
layers are represented by classes rather than separate packages:

* Domain: ``Student``, ``Course``, and ``Trainer`` entities plus repository and
  service interfaces.
* Infrastructure: in-memory ``Database`` and concrete repository classes.
* Application/service: ``StudentService``, ``CourseService``, and
  ``TrainerService`` contain business operations and validation.
* Presentation: controller classes receive requests and delegate to services.
* Composition root: the ``if __name__ == "__main__"`` block wires concrete
  dependencies together.

The important dependency direction is::

   Controller -> Service interface -> Repository interface
                                      ^
                                      |
                         concrete in-memory repository

The service depends on an abstraction, not directly on the database. This makes
it possible to replace the in-memory repository with a real database adapter
without rewriting the core service logic.

How to study each example
-------------------------

For each pattern, use this short loop:

#. Read the ``without_*`` file first and identify the repeated code or tight
   coupling.
#. Read the pattern version and locate the abstraction, context, subject, or
   factory.
#. Run both files and compare their output.
#. Change one concrete class or add one new behavior.
#. Observe whether the pattern lets you extend the example without modifying
   unrelated code.
#. Write down the trade-off: patterns add structure, but structure is useful
   only when it solves a real design problem.

What this repository is not
---------------------------

* It is not a production framework or a packaged Python library.
* It does not provide a web API, persistent database, or deployment setup.
* It does not include automated tests yet; the runnable scripts are learning
  demonstrations.
* The examples favor clarity over exhaustive validation, typing, error
  handling, and packaging conventions.

Common questions
----------------

Do I need to install a database?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
No. ``onion.py`` uses an in-memory database made from Python lists. It is a
teaching substitute for a real persistence adapter.

Do I need every design pattern before learning Onion Architecture?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
No. Start with the SOLID notebook, then read the Onion example. The patterns
are supporting lessons that help explain the abstractions used in the larger
example.

Why are there two files for many patterns?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
The ``without_*`` file shows a direct or tightly coupled approach. The other
file applies the pattern so you can compare the design trade-offs rather than
memorize a definition.

Why does the repository contain spelling variants in directory names?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
The existing paths are ``AbstructFactory`` and ``Onlion_Architecture``. They are
kept to avoid breaking links and commands. New documentation should use the
canonical terms in prose while preserving the actual paths in code examples.

Why does a script wait for input?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
The Abstract Factory and Strategy demonstrations are interactive. Enter the
values requested in the terminal. For repeatable experiments, replace the
``input()`` calls with variables in a local copy.

Can I run the notebook without VS Code?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Yes. Install Jupyter and run ``jupyter notebook Solid/SOLID_principals.ipynb``.
A browser will open the notebook interface.

Where should I start changing the code?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Start with one ``without_*`` file and its paired pattern file. Add one new
sender, observer, strategy, or repository implementation. Then run the example
again and inspect which classes needed modification.

Contribution checklist
----------------------

Before committing a change:

* Keep examples small and focused on one design idea.
* Preserve the paired ``without_*`` comparison where it exists.
* Use the standard library unless a dependency is essential to the lesson.
* Run every changed Python script.
* If changing the notebook, run its Python cells from top to bottom.
* Update this guide when paths, prerequisites, or the learning path change.
* Keep generated environments such as ``.venv`` out of commits.

License
-------

See ``LICENSE`` for the repository's license terms.
