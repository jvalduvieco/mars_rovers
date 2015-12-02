(ns mars-rovers.schemas
  (:require [schema.core :as s]))

(def Map
  {:max-x s/Int :max-y s/Int})

(def CardinalPoints
  (s/enum \N \E \S \W))

(def RoverState
  {:x s/Int :y s/Int :heading CardinalPoints})

(def RoverMission
  {:rover-state RoverState :commands s/Str})

