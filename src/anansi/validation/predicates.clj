(ns anansi.validation.predicates)

(defn blank? [s]
  (every? #(Character/isWhitespace %) s))

(defn plain-id?
  [val]
  (and val
       (re-matches #"^[0-9a-z\-]+$" val)))

(defn present?
  "True if x is not nil and not an empty string."
  [x]
  (not (blank? x)))

(defn max-size
  "Returns a function to check a maximum size of a collection."
  [n]
  #(<= (count %) n))
