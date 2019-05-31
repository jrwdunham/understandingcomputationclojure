(ns uc-clojure.small-step-fiddle
  (:require [uc-clojure.small-step :refer :all]
            [clojure.pprint :as pp]))

(comment

  (run machine-1)

  (get-ret machine-1)

  (run machine-2)

  (get-ret machine-2)

  (run machine-3)

  (get-ret machine-3)

  (run machine-4)

  (get-ret machine-4)

  (run machine-5)

  (get-ret machine-5)

  (run machine-6)

  (get-ret machine-6)

  (run machine-7)

  (get-ret machine-7)

  (run machine-8)

  (get-ret machine-8)

  (run machine-bad)

  (get-ret machine-bad)

  (run-all machines)

  (get-ret-all machines)

)
