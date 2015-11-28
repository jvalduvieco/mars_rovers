(ns mars-rovers.core
  (:require [mars-rovers.commands.execute :as e :refer [execute-rover-command]]))

(defn- encode-rover-data [{:keys [x y heading]}]
  [x y heading])

(defn- decode-rover-data [[[x y heading] [commands]]]
  {:rover-state {:x x :y y :heading heading} :commands commands})

(defn- decode-map-data [[max-x max-y]]
  {:max-x max-x :max-y max-y})

(defn- do-mission [map-limits {:keys [rover-state commands]}]
  (->
    (reduce
      (partial e/execute-rover-command map-limits)
      rover-state commands)
    (encode-rover-data)))

(defn- raw-input-data->rover-state-and-commands [rover-input-data]
  (map decode-rover-data (partition 2 rover-input-data)))

(defn new-mission [raw-mission-input]
  (let [[raw-map-limits & rover-raw-input-data] raw-mission-input]
    (map
      (partial do-mission (decode-map-data raw-map-limits))
      (raw-input-data->rover-state-and-commands rover-raw-input-data))))