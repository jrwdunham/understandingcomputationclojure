(ns uc-clojure.denotational-fiddle
  (:require [uc-clojure.denotational :as den]
            [uc-clojure.constructs :refer :all]))

(comment

  (den/to-clojure (number- 5))

  (den/evaluate (number- 5) {})

  (den/to-clojure (boolean- false))

  (den/evaluate (boolean- false) {})

  (den/to-clojure (variable- :x))

  (den/evaluate (variable- :x) {:x 5})

  (den/to-clojure (add- (number- 5) (number- 6)))

  (den/evaluate (add- (number- 5) (number- 6)) {})

  (den/to-clojure (multiply- (number- 5) (number- 6)))

  (den/evaluate (multiply- (number- 5) (number- 6)) {})

  (den/to-clojure (less-than- (number- 5) (number- 6)))

  (den/evaluate (less-than- (number- 5) (number- 6)) {})

  (den/to-clojure
   (less-than-
    (add-
     (variable- :x)
     (number- 1))
    (number- 3)))

  (den/evaluate
   (less-than-
    (add-
     (variable- :x)
     (number- 1))
    (number- 3))
   {:x 1})

  (den/to-clojure
   (assign-
    :x
    (add-
     (number- 2)
     (number- 3))))

  (den/evaluate
   (assign-
    :y
    (add-
     (variable- :x)
     (number- 3)))
   {:x 3})

  (den/to-clojure
   (if-
     (less-than-
      (variable- :x)
      (number- 5))
     (assign-
      :y
      (number- 2))
     (assign-
      :y
      (number- 222))))

  (den/evaluate
   (if-
     (less-than-
      (variable- :x)
      (number- 5))
     (assign-
      :y
      (number- 2))
     (assign-
      :y
      (number- 222)))
   {:x 35})

  (den/to-clojure
   (sequence-
    (assign-
     :x
     (number- 2))
    (assign-
     :y
     (add-
      (number- 5)
      (variable- :x)))))

  (den/evaluate
   (sequence-
    (assign-
     :x
     (number- 2))
    (assign-
     :y
     (add-
      (number- 5)
      (variable- :x))))
   {})

  (den/to-clojure
   (while-
       (less-than-
        (variable- :x)
        (number- 5))
     (assign-
      :x
      (multiply-
       (variable- :x)
       (number- 3)))))

  (den/evaluate
   (while-
       (less-than-
        (variable- :x)
        (number- 5))
     (assign-
      :x
      (multiply-
       (variable- :x)
       (number- 3))))
   {:x 1})

)
