(ns mars-rovers.core
  (:require [clojure.core.match :refer [match]]))

; ---- rover command handlers
(defn- move-rover [{:keys [x y heading], :as rover-state} action]
  (merge rover-state
         (match [heading action]
                [\N \M] {:y (inc y)}
                [\E \M] {:x (inc x)}
                [\S \M] {:y (dec y)}
                [\W \M] {:x (dec x)})))

(defn- within-limits? [{:keys [max-x max-y]} {:keys [x y]}]
  (if (and (>= max-x x 0) (>= max-y y 0)) true false))

(defn- handle-move-rover-within-limits [map-limits rover-state action]
  (let [new-rover-state (move-rover rover-state action)]
    (if (within-limits? map-limits new-rover-state) new-rover-state rover-state)))

(defn- handle-rotate-rover [_ {:keys [heading], :as rover-state} action]
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

; ---- rover command handlers infrastructure
(def command-handlers {\M handle-move-rover-within-limits
                       \R handle-rotate-rover
                       \L handle-rotate-rover })

(defn- execute [command-handlers map-limits rover-state command]
  (let [handler (get command-handlers command)]
    (handler map-limits rover-state command)))

; ---- Translation functions
(defn- encode-rover-data [{:keys [x y heading]}]
  [x y heading])

(defn- decode-rover-data [[[x y heading] [commands]]]
  {:rover-state {:x x :y y :heading heading} :commands commands})

(defn- raw-input-data->rover-state-and-commands [rover-input-data]
  (map decode-rover-data (partition 2 rover-input-data)))

(defn- decode-map-data [[max-x max-y]]
  {:max-x max-x :max-y max-y})

; ---- Mission infrastructure
(defn- do-mission [map-limits {:keys [rover-state commands]}]
  (encode-rover-data
    (reduce
      (partial execute command-handlers map-limits)
      rover-state commands)))

(defn new-mission [raw-mission-input]
  (let [[raw-map-limits & rover-raw-input-data] raw-mission-input]
    (map
      (partial do-mission (decode-map-data raw-map-limits))
      (raw-input-data->rover-state-and-commands rover-raw-input-data))))