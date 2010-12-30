(ns anansi.forms
  (:use [hiccup core page-helpers]
        [anansi html-helpers text]
        [nl.zeekat utils]
        [compojure.core :only [wrap!]])
  (:require [hiccup.form-helpers :as form]))

(def *errors* {})
(def *params* {})

(defmacro with-params [params & body]
  `(let [p# ~params]
     (binding [*params* p#]
      ~@body)))

(defn- -insert-params
  ([f name]
     (f name (name *params*)))
  ([f name value]
     (f name value)))

(defn- insert-params
  [f]
  (fn [& args]
    (apply -insert-params f args)))

(def text-field
     (with-class form/text-field "input-text"))
(def password-field
     (with-class form/password-field "input-password"))

(def submit-button
     (with-class form/submit-button "input-submit"))

(def file-upload
     (with-class form/file-upload "input-file"))

(alias-from form form-to)

(defn text-area
  [name value]
  (add-class (form/text-area name (string->html value)) "text-area"))
(defn html-area
  [name value]
  (add-class (form/text-area name (string->html value)) "html-area"))

(def hidden-field (with-class form/hidden-field "hidden-field"))

(defn check-box
  [name value]
  (form/check-box name (= value (*params* name)) value))

(defn make-drop-down
     [options]
     (fn
       dd
       ([name value]
          (form/drop-down name (map #(if (keyword? %)
                                      [(text %) %]
                                      %)
                                    options) value))
       ([name]
          (dd name (name *params*)))))

(wrap! text-field insert-params)
(wrap! password-field insert-params)
(wrap! text-area insert-params)
(wrap! html-area insert-params)
(wrap! hidden-field insert-params)
(wrap! check-box insert-params)

(defn multipart-form-to
  [[method action] & body]
  (assoc-in (form-to [method action] body) [1 :enctype] "multipart/form-data"))

(defn form-for 
  [type & body]
  (multipart-form-to [:post (format "/%s/save/%s" type (or (:id *params*) ""))]
                     body))

(defn disabled [f]
  (fn [key & rest]
    (let [[tag attrs & rest] (apply f key rest)]
      (if (map? attrs)
        (into [tag (assoc attrs :disabled true)] rest)
        (into [tag {:disabled true} attrs] rest)))))

(defn enabled-if [test f]
  (if test
    f
    (disabled f)))

(defn required
  [f]
  (fn [key value] [:div.required (f key value)]))

(defn single-button-form
  [href txt & body]
  (add-class (form-to [:post href]
                      (submit-button txt)
                      body) "single-button-form"))

(defn labeled
  ([f key value label]
     (if-let [err (*errors* key)]
       [:div {:class (str "error " (name key))} [:label (text label) [:strong.error-msg (apply str err)] (f key value)]]
       [:div {:class (name key)} [:label (text label) (f key value)]]))
  ([f key value]
     (labeled f key value key))
  ([f key]
     (labeled f key (*params* key))))

(defn input-date-field 
  "Creates a form element to input a date"
  ([name value]
     (let [value (if (integer? value) value
                     (current-time))]
       (input-date-field name value 1900 2100)))
  ([name value fromYear toYear]
     (let [name (.replaceAll (str name) ":" "")]
       (html
        [:div.date-input
         (str "<script>
         function changed_date(name)
         {
           function $(id) { return document.getElementById(id); }
           var date = new Date(0);
           date.setUTCFullYear(parseInt($('year_" name "').value));
           date.setUTCMonth(parseInt($('month_" name "').value) - 1);
           date.setUTCDate(parseInt($('day_" name "').value));
           $('" name "').value = date.getTime();
         }
         $(document).ready(function(){changed_date('" name "')});
       </script>")
        [:input {:type 'hidden :name name :id name :value value}]
        [:select {:id (str "day_" name) :onchange (str "changed_date('" name "');")}
         (reduce #(str % (html [:option {:value %2 :selected (if (= %2 (timestamp->day value)) "true" nil)} %2 ])) "" (range 1 32))]
         " "
        [:select {:id (str "month_" name) :onchange (str "changed_date('" name "');")}
         (reduce #(str % (html [:option {:value %2 :selected (if (= %2 (timestamp->month value)) "true" nil)} (text (nth months %2))])) "" (range 1 13))] " "
        [:select {:id (str "year_" name) :onchange (str "changed_date('" name "');")}
         (reduce #(str % (html [:option {:value (+ fromYear %2)
                                         :selected (if (= (+ fromYear %2) (timestamp->year value)) "true" nil)
                                         } (+ fromYear %2)])) "" (range (- (inc toYear) fromYear)))] " "]))))