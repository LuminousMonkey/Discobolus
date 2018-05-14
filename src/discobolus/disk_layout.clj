(ns discobolus.disk-layout
  (:import [java.util TreeMap]))

(def disk-sectors (TreeMap. {17 21 24 19 30 18 40 17}))

(defn num-of-sectors
  "Given a track number, return the maximum number of sectors available
  on that track."
  [track]
  (val (.ceilingEntry disk-sectors track)))

(defn sector-offset
  "Given a track, give the total number of sectors that are before that
  track."
  [track]
  (reduce + (map num-of-sectors (range 1 track))))

(def bytes-in-sector 256)

(defn byte-offset
  "Given the number of sectors (or track and sector), return the offset
  in number of bytes."
  ([sectors] (* bytes-in-sector sectors))
  ([track sector] (* bytes-in-sector (+ sector (sector-offset track)))))

;; Sector allocation might be as follows:
;; next = (current-sector + interleave) mod total-sectors-for-track
;; next-sector = sector left => next

(defn find-next-sector
  "Given a sector number, return that sector, or the next one available."
  [free-sectors wanted]
  (let [result (first (filter #(>= % wanted) free-sectors))]
    (if-not result
      (first free-sectors)
      result)))

(defn sector-seq
  "Given the remaining sectors, and interleave and number of sectors
  needed. Return the sectors to use."
  [current-sector interleave sectors-free sectors-needed total-sectors]
  (let [next (mod (+ current-sector interleave) total-sectors)
        next-sector (find-next-sector sectors-free next)]))

(defn get-next-sector
  ""
  [starting-sector file-interleave sectors-free total-sectors]
  (loop [result [] current-sector starting-sector sectors-free sectors-free]
    (let [next (mod (+ current-sector file-interleave) total-sectors)
          next-sector (find-next-sector (disj sectors-free current-sector) next)]
      (if next-sector
        (recur (conj result current-sector) next-sector
               (disj sectors-free current-sector))
        (conj result current-sector)))))

(defn track-sector-seq
  "Given a set of sectors free and a file, return a vector containing
  the sector sequence and the set of remaining sectors free."
  [track sector interleave sectors-free file-size]
  (let [next (mod (+ current-sector interleave) total-sectors)
        next-sector (find-next-sector sectors-free next)]))
