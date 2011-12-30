(ns virtual-adventure.core)

(def world {:start "It's an empty room, nothing interesting here.",
            :room-north "This room is full of light and a celestial music.",
            :room-south "This room is dark, cold and scary...",
            :room-east "You are outside, there are high mountains far away.",
            :room-west "Another room with an old wood table in the center."})

(def paths {:start [["door" :north :room-north],
                    ["door" :south :room-south],
                    ["door" :east :room-east],
                    ["door" :west :room-west]],
            :room-north [["door" :south :start]],
            :room-south [["door" :north :start]],
            :room-east [["door" :west :start]],
            :room-west [["door" :east :start]]})

(def where (ref :start))

(defn describe-location
  "Describe the location."
  [location]
  (world location))

(defn describe-path
  "Describe a path as specified in the paths data structure."
  [path-spec]
  (str "There is a " (first path-spec) " going " (name (second path-spec)) " from here."))

(defn describe-paths
  "Describe what paths there is from this location."
  [location]
  (loop [path-specs (paths location), paths-description ""]
    (if (empty? path-specs)
      paths-description
      (recur (pop path-specs) (str paths-description (describe-path (peek path-specs)))))))

(defn look
  "Explore the current location where the character is."
  []
  (str (describe-location @where) " " (describe-paths @where)))

(defn valid-directions
  "Get valid directions from the supplied location."
  [location]
  (reduce conj [] (map (fn [path] (second path)) (paths location))))

(defn can-go?
  "Returns true if there is a path from the supplied location in the given direction."
  [location direction]
  (loop [valid (valid-directions location)]
    (cond (empty? valid) false
          (= (peek valid) direction) true
          :default (recur (pop valid)))))

(defn next-location
  "Returns the new location following a path from a start location."
  [location direction]
  (loop [valid-paths (paths location)]
    (let [path (peek valid-paths)]
      (if (= (second path) direction)
      (peek path)
      (recur (pop valid-paths))))))

(defn go
  "Go to the desired location (if accessible from where the character is now)."
  [direction]
  (if (not (can-go? @where direction))
    (str "You can't go there from here!")
    (do
      (dosync (ref-set where (next-location @where direction)))
      (look))))
