(ns uc-clojure.big-step-fiddle
  (:require [uc-clojure.big-step :refer :all]
            [uc-clojure.constructs :refer :all]))

(comment

  (evaluate (number- 23) {})

  (evaluate (variable- :x) {:x (number- 23)})

  (evaluate
   (less-than-
    (add-
     (variable- :x)
     (number- 2))
    (variable- :y))
   {:x (number- 2)
    :y (number- 5)})

  (evaluate
   (assign-
    :x
    (number- 3))
   {:y (number- 2)})

  (evaluate (do-nothing-) {})

  (evaluate
   (if-
       (less-than-
        (number- 33)
        (number- 4))
     (assign-
      :x
      (number- 7))
     (assign-
      :x
      (number- 777)))
   {})

  (evaluate
   (sequence-
    (assign- :x (number- 2))
    (assign- :y (number- 3)))
   {})

  (evaluate
   (sequence-
    (assign- :x
             (add-
              (number- 1)
              (number- 1)))
    (assign- :y
             (add-
              (variable- :x)
              (number- 3))))
   {})

  (evaluate
   (while-
       (less-than-
        (variable- :x)
        (number- 5))
     (assign-
      :x
      (multiply-
       (variable- :x)
       (number- 3))))
   {:x (number- 1)})

  (let [{:keys [a b c]} {:a 2 :b 3}]
    [a b c])

  (let [[a b c] "ab"]
    [a b c])


)

