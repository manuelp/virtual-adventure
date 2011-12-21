(ns virtual-adventure.core)

(def world {:start "It's an empty room, nothing interesting here.",
            :north "This room is full of light and a celestial music.",
            :south "This room is dark, cold and scary...",
            :east "You are outside, there are high mountains far away.",
            :west "Another room with an old wood table in the center."})

(def where (ref :start))

(defn explore
  "Explore the current location where the character is."
  []
  (@where world))

(defn move
  "Move the character from the current location to the specified one."
  [location]
  (dosync (ref-set where location)))
