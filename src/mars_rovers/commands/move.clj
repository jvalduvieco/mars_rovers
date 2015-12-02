(ns mars-rovers.commands.move
  (:require [clojure.core.match :refer [match]]))


(defn- move-rover [{:keys [x y heading], :as rover-state}]
  (merge rover-state
         (match [heading]
                [\N] {:y (inc y)}
                [\E] {:x (inc x)}
                [\S] {:y (dec y)}
                [\W] {:x (dec x)})))

(defn move-rover-with-limits [{:keys [max-x max-y]} rover-state]
  (let [new-rover-state (move-rover rover-state)
        {:keys [x y]} new-rover-state]
    (if (and (>= max-x x 0) (>= max-y y 0)) new-rover-state rover-state)))
