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
  (first (filter #(>= % wanted) free-sectors)))

(defn track-sector-seq
  "Given a set of sectors free and a file, return a vector containing
  the sector sequence and the set of remaining sectors free."
  [interleave sectors-free current-sector file-size total-sectors]
  (let [next (mod (+ current-sector interleave) total-sectors)
        next-sector (find-next-sector sectors-free next)]))
