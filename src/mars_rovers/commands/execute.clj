(ns mars-rovers.commands.execute
  (:require [mars-rovers.commands.move :as m :refer [move-rover-with-limits]]
            [mars-rovers.commands.rotate :as r :refer [rotate-rover]]))

(defmulti execute-rover-command (fn [map rover-state command] command))

(defmethod execute-rover-command \M [map rover-state _]
  (m/move-rover-with-limits map rover-state ))

(defmethod execute-rover-command \L [map rover-state command]
  (r/rotate-rover map rover-state command))

(defmethod execute-rover-command \R [map rover-state command]
  (r/rotate-rover map rover-state command))

(defmethod execute-rover-command \Z [map rover-state command]
  (throw (IllegalArgumentException. "Invalid command.")))