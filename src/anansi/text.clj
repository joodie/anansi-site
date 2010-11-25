(ns anansi.text
  (:use anansi.config))
(import org.joda.time.DateTime
        org.joda.time.format.DateTimeFormat
        org.joda.time.DateTimeZone)

(def #^{:doc "The current output language as used by (text) etc"}
     *language* :nl)

(def *timezone*
     #^{:doc "The current output timezone, as used by the timestamp->... functions"}
     (DateTimeZone/forID "Europe/Amsterdam"))

(def months [:none :january :february :march :april :may :june :july :august :september :october :november :december])

(def short-months [:none :short-january :short-february :short-march :short-april :short-may :short-june :short-july :short-august :short-september :short-october :short-november :short-december])

(defn text 
  ([key] (text *language* key))
  ([*language* key]
     (or (-> (:text *config*) *language* key)
         key)))

(defn date-time
  "coerse argument into a joda DateTime object"
  ([date]
     (DateTime. date *timezone*)))

(defn timestamp->text
  [stamp]
  (let [dt (date-time stamp)]
    (str (.getDayOfMonth dt) " " (text (months (.getMonthOfYear dt))) " "(.getYear dt) " " (format "%02d:%02d" (.getHourOfDay dt) (.getMinuteOfHour dt)))))

(defn timestamp->date
  [stamp]
  (let [dt (date-time stamp)]
    (str (.getDayOfMonth dt) " " (text (months (.getMonthOfYear dt))) " "(.getYear dt))))

(defn timestamp->short-date
  [stamp]
  (let [dt (date-time stamp)]
    (str (.getDayOfMonth dt) " " (text (short-months (.getMonthOfYear dt))) " "(.getYear dt))))

(defn timestamp->year
  [stamp]
  (.getYear (date-time stamp)))

(defn timestamp->month
  [stamp]
  (.getMonthOfYear (date-time stamp)))

(defn timestamp->day
  [stamp]
  (.getDayOfMonth (date-time stamp)))

(defn date->timestamp
  [year month day]
  (.getMillis (DateTime. year month day 0 0 0 0)))

(defn timestamp->birthday
  [stamp]
  (let [dt (date-time stamp)]
    (str (.getDayOfMonth dt) " " (text (months (.getMonthOfYear dt))))))

(defn timestamp->short-birthday
  [stamp]
  (let [dt (date-time stamp)]
    (str (.getDayOfMonth dt) " " (text (short-months (.getMonthOfYear dt))))))

