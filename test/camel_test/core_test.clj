(ns camel-test.core-test
  (:require [clojure.test :refer :all]
            [camel-test.core :refer :all]))

(deftest a-test
  (testing "No unit tests for camel route. Need an integration test."
    (is (= 0 1))))
