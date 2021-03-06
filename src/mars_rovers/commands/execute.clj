(ns mars-rovers.commands.execute
  (:require [mars-rovers.commands.move :as m]
            [mars-rovers.commands.rotate :as r]))

(defmulti execute-rover-command (fn [map rover-state command] command))

(defmethod execute-rover-command \M [map rover-state _]
  (m/move-rover-with-limits map rover-state ))

(defmethod execute-rover-command \L [map rover-state command]
  (r/rotate-rover map rover-state command))

(defmethod execute-rover-command \R [map rover-state command]
  (r/rotate-rover map rover-state command))

(defmethod execute-rover-command :default [map rover-state command]
  (throw (IllegalArgumentException. "Invalid command.")))