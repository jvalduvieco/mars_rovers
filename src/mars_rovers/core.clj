(ns mars-rovers.core
  (:require [clojure.core.match :refer [match]]))

(defn- move-rover [{:keys [x y heading], :as rover-state} action]
  (merge rover-state
         (match [heading action]
                [\N \M] {:y (inc y)}
                [\E \M] {:x (inc x)}
                [\S \M] {:y (dec y)}
                [\W \M] {:x (dec x)})))

(defn- rotate-rover [_ {:keys [heading], :as rover-state} action]
  (merge rover-state
         (match [heading action]
                [\N \R] {:heading \E}
                [\E \R] {:heading \S}
                [\S \R] {:heading \W}
                [\W \R] {:heading \N}
                [\N \L] {:heading \W}
                [\W \L] {:heading \S}
                [\S \L] {:heading \E}
                [\E \L] {:heading \N})))

(defn- move-rover-with-limits [[top-x top-y :as limits] rover-state action]
  (let [new-rover-state (move-rover rover-state action)
        {:keys [x y]} new-rover-state]
    (if (and (>= top-x x 0) (>= top-y y 0)) new-rover-state rover-state)))

(defn- encode-rover-data [{:keys [x y heading]}]
  [x y heading])

(defn- decode-rover-data [[[x y heading] [commands]]]
  {:rover-state {:x x :y y :heading heading} :commands commands})

(def command-handlers {\M move-rover-with-limits
                       \R rotate-rover
                       \L rotate-rover })

(defn- execute [command-handlers map-limits rover-state command]
  (let [handler (get command-handlers command)]
    (handler map-limits rover-state command)))

(defn- do-mission [map-limits {:keys [rover-state commands]}]
  (encode-rover-data
    (reduce
      (partial execute command-handlers map-limits)
      rover-state commands)))

(defn- raw-input-data->rover-state-and-commands [rover-input-data]
  (map decode-rover-data (partition 2 rover-input-data)))

(defn new-mission [raw-mission-input]
  (let [[map-limits & rover-raw-input-data] raw-mission-input]
    (map
      (partial do-mission map-limits)
      (raw-input-data->rover-state-and-commands rover-raw-input-data))))