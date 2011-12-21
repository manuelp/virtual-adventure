(ns virtual-adventure.core)

(def world {:start "It's an empty room, nothing interesting here.",
            :north "This room is full of light and a celestial music.",
            :south "This room is dark, cold and scary...",
            :east "You are outside, there are high mountains far away.",
            :west "Another room with an old wood table in the center."})

(def paths {:start [:north :south :east :west]
            :north [:start]
            :south [:start]
            :east [:start]
            :west [:start]})

(def where (ref :start))

(defn look
  "Explore the current location where the character is."
  []
  (str (@where world)
       (loop [reachable (paths @where), locations " Reachable locations:"]
         (if (empty? reachable)
           locations
           (recur (pop reachable) (str locations " " (name (peek reachable))))))))

(defn move
  "Move the character from the current location to the specified one."
  [location]
  )

(defn reachable
  "Returns true if the supplied location is reachable from here."
  [location]
  (not= (.indexOf (paths @where) location) -1))

(defn go
  "Go to the desired location (if accessible from where the character is now)."
  [location]
  (if (and (paths @where) (reachable location))
    (dosync (ref-set where location))
    (str "You can't go there from here!")))
