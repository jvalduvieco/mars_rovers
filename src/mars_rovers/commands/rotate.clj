(ns mars-rovers.commands.rotate
  (:require [clojure.core.match :refer [match]]))

(defn rotate-rover [_ {:keys [heading], :as rover-state} action]
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
