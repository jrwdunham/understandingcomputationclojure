===============================================================================
  uc-clojure
================================================================================

uc-clojure is my interpretation of Tom Stuart's "Understanding Computation"
code, translated to Clojure.


Usage
================================================================================

In your REPL-integrated editor of choice, run any of the expressions in
small_step_fiddle.clj.

Alternatively, open up a REPL and require ``uc-clojure.small-step``::

    $ clj
    user=> (require '[uc-clojure.small-step :refer :all])

Get a vector of the small steps of a machine as it performs a reduction on its
expression or statement. Each step is a 2-item vector consisting of the string
representation of an expression/statement and the current environment as a map::

    user=> (run machine-1)
    [["1 * 2 + 3 * 4" {}] ["2 + 3 * 4" {}] ["2 + 12" {}] ["14" {}]]

Pretty-print the derivation/reduction of a machine::

    user=> (run-print machine-1)
    1 * 2 + 3 * 4, {}
    2 + 3 * 4, {}
    2 + 12, {}
    14, {}

Get the return value (last step) of a machine::

    user=> (get-ret machine-1)
    ["14" {}]
    
License
================================================================================

Copyright © 2019 Joel Dunham

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
