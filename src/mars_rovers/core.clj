(ns mars-rovers.core
  (:require [clojure.core.match :refer [match]]))

(defn- move-rover [[x y heading :as rover-state] action]
  (match [heading action]
         [\N \M] [x (inc y) heading]
         [\E \M] [(inc x) y heading]
         [\S \M] [x (dec y) heading]
         [\W \M] [(dec x) y heading]
         [\N \R] [x y \E]
         [\E \R] [x y \S]
         [\S \R] [x y \W]
         [\W \R] [x y \N]
         [\N \L] [x y \W]
         [\W \L] [x y \S]
         [\S \L] [x y \E]
         [\E \L] [x y \N]
         ))

(defn- move-rover-with-limits [[top-x top-y :as limits] rover-state action]
  (let [new-rover-state (move-rover rover-state action)
        [x y _] new-rover-state]
    (if (and (>= top-x x 0) (>= top-y y 0)) new-rover-state rover-state)))

(defn- do-mission [map-limits [rover-state [commands] :as rover-data]]
  (reduce
    (partial move-rover-with-limits map-limits)
    rover-state commands))

(defn- raw-input-data->rover-state-and-commands [rover-input-data]
  (partition 2 rover-input-data))

(defn new-mission [raw-mission-input]
  (let [[map-limits & rover-raw-input-data] raw-mission-input]
    (map
      (partial do-mission map-limits)
      (raw-input-data->rover-state-and-commands rover-raw-input-data))))