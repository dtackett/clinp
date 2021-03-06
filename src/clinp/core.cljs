(ns clinp.core
  (:require [clojure.browser.event :as event]
            [goog.events.KeyCodes]))

; Thoughts: Is this just going to end up in an event handler soup?
; Would this be better modeled by having the pulse generate events that go
; to some queue and allow something else to consume from that queue? That
; queue would have to be rebuilt every pulse? That might not be too bad if
; you assume that from pulse to pulse the down keys don't change that much.

; should follow the format of a map of key-code to a map of phases to a callback
(def listeners (atom {}))

(def downkeys (atom #{}))

(defn- get-key-code
  "Find the keyCode integer value for a keyword"
  [key-name]
  (aget js/goog.events.KeyCodes (name key-name)))

(defn- exec-handler!
  "Execute the function (if found) for the given bind key and phase"
  [listeners phase key-code]
  (let [handler (get (get listeners key-code) phase)]
    (if (not (nil? handler))
      (handler))))

; TODO Allow for an event listener target? Currently hard set to be the document body
(defn setup!
  "Perform initial setup for input handler"
  []
  (do
    (reset! downkeys #{})
    ; Setup keydown handler
    (event/listen
      (.-body js/document)
      "keydown"
      (fn [event]
        (do
          (if (not (contains? @downkeys (.-keyCode event)))
            (exec-handler! @listeners :down (.-keyCode event)))
          (swap! downkeys conj (.-keyCode event)))))
    ; Setup keyup handler
    (event/listen
      (.-body js/document)
      "keyup"
      (fn [event]
          (if (contains? @downkeys (.-keyCode event))
            (exec-handler! @listeners :up (.-keyCode event)))
        (swap! downkeys disj (.-keyCode event))))))

(defn teardown!
  "Perform any cleanup needed for input handler"
  []
  ; remove the google handler
  ; todo remove all current handlers
  (reset! downkeys #{}))

(defn keydown?
  "Test if the given key is currently held down."
  [test-key]
  (contains? @downkeys (get-key-code test-key)))

(defn listen!
  "Setup an callback for a given key and phase."
  [bind-key phase f]
  ; TODO How to validate phase is in a known set?
  ; TODO How to validate bind-key is known?
  (let [key-code (get-key-code bind-key)]
    (swap!
     listeners
     assoc
     key-code
     (assoc
       (get @listeners key-code {})
       phase
       f))))

(defn unlisten!
  "Remove a callback for a given key and phase."
  [bind-key phase]
  (let [key-code (get-key-code bind-key)]
    (swap!
     listeners
     assoc
     key-code
     (dissoc
       (get @listeners key-code {})
       phase))))

(defn pulse!
  ([]
   (pulse! @listeners @downkeys))
  ([listeners downkeys]
   (dorun (map (partial exec-handler! listeners :pulse) downkeys))))
