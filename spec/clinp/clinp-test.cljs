(ns clinp.test
  (:require [clinp.core :as clinp]
            [speclj.core])
  (:require-macros [speclj.core :refer [describe it should should-not should= should-be-nil should-not-be-nil]]))

(describe "Key code lookups"
          (it "Getting known keycode should return an integer value"
              (should-not-be-nil (clinp/get-key-code :A)))
          (it "Getting unknown keycode should return nil"
              (should-be-nil (clinp/get-key-code :unknowablekey))))

(describe "Key handler execs"
          (it "Exec on a non register key handler"
              (should-be-nil
               (clinp/exec-handler! {65 {:down #(-> "test")}} :down 66)))
          (it "Exec on a non resitered phase"
              (should-be-nil
               (clinp/exec-handler! {65 {:down #(-> "test")}} :up 65)))
          (it "Exec on a registered key handler and phase"
              (should=
               "test"
               (clinp/exec-handler! {65 {:up #(-> "test")}} :up 65))))

(describe "Key down checking"
          (it "Check that a key not held down doesn't register as held"
              (should-not (clinp/keydown? :a)))
          (it "Check that a key held down does register as held"
              (with-redefs
                [clinp/downkeys (atom #{65})]
                (should (clinp/keydown? :A)))))

(describe "System setup/teardown"
          (it "Check setup")
          (it "Check teardown"))

(describe "Listening registration functions"
          (it "Basic listener registration"
              (should-not (empty? (clinp/listen! :A :down #()))))
          (it "Overwrite an existing listener"
              (with-redefs
                [clinp/listeners (atom {(clinp/get-key-code :A) {:down #(-> "old")}})]
                (should=
                 "new"
                 (clinp/exec-handler!
                  (clinp/listen! :A :down #(-> "new"))
                  :down
                  65)))))

(describe "Unlisten"
          (it "Basic unlisten"
              (with-redefs
                [clinp/listeners (atom {(clinp/get-key-code :A) {:down #(-> "value")}})]
                (should-be-nil
                 (clinp/exec-handler!
                  (clinp/unlisten! :A :down)
                  :down
                  65))))
          (it "Unlisten to a key/phase combo that isn't registered"
              (should-not-be-nil (clinp/unlisten! :A :down))))
