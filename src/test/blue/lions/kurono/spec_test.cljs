; Copyright 2024 HASEBA Junya
; 
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
; 
;     http://www.apache.org/licenses/LICENSE-2.0
; 
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.

(ns blue.lions.kurono.spec-test
  (:require [cljs.spec.alpha :as s]
            [cljs.test :refer [are deftest is testing]]
            [blue.lions.kurono.spec :as spec]))

(deftest common_non-blank-string-test
  (testing "Succeeds in verification when the target value is valid as ':common/non-blank-string'."
    (is (s/valid? :common/non-blank-string "string")))

  (testing "Fails in verification when the target value is invalid as ':common/non-blank-string'."
    (are [target] (not (s/valid? :common/non-blank-string target))
      ""
      ["not-string"]
      nil)))

(deftest config-value-test
  (testing "Succeeds in verification when the target value is valid as '::config-value'."
    (are [target] (s/valid? ::spec/config-value target)
      "value"
      ""
      ["value"]
      [""]
      ["value" ""]
      []))

  (testing "Fails in verification when the target value is invalid as '::config-value'."
    (are [target] (not (s/valid? ::spec/config-value target))
      #{"not-string"}
      [#{"not-string"}]
      [nil]
      ["value" #{"not-string"}]
      nil)))

(deftest config-test
  (testing "Succeeds in verification when the target value is valid as '::config'."
    (are [target] (s/valid? ::spec/config target)
      {:key "value"}
      {:key ["value1" "value2"]}
      {:key1 "value1" :key2 ["value2"]}
      {}))

  (testing "Fails in verification when the target value is invalid as '::config'."
    (are [target] (not (s/valid? ::spec/config target))
      {:key #{"not-string"}}
      {:key nil}
      {"not-key" "value"}
      "not-map"
      nil)))

(deftest edn-test
  (testing "Succeeds in verification when the target value is valid as '::edn'."
    (are [target] (s/valid? ::spec/edn target)
      {:key "value"}
      {}))

  (testing "Fails in verification when the target value is invalid as '::edn'."
    (are [target] (not (s/valid? ::spec/edn target))
      "not-map"
      nil)))

(deftest file-name-test
  (testing "Succeeds in verification when the target value is valid as '::file-name'."
    (are [target] (s/valid? ::spec/file-name target)
      "file-name.md"
      "日本語"))

  (testing "Fails in verification when the target value is invalid as '::file-name'."
    (are [target] (not (s/valid? ::spec/file-name target))
      "invalid\\file-name"
      "invalid/file-name"
      "invalid:file-name"
      "invalid*file-name"
      "invalid?file-name"
      "invalid\"file-name"
      "invalid>file-name"
      "invalid<file-name"
      "invalid|file-name"
      ""
      ["not-string"]
      nil)))

(deftest file-names-test
  (testing "Succeeds in verification when the target value is valid as '::file-names'."
    (are [target] (s/valid? ::spec/file-names target)
      ["file.md"]
      ["file1.md" "file2.md"]
      []))

  (testing "Fails in verification when the target value is invalid as '::file-names'."
    (are [target] (not (s/valid? ::spec/file-names target))
      ["invalid/file-name"]
      [nil]
      ["file.md" "invalid/file-name"]
      "not-vector"
      nil)))

(deftest catalog_appendices-test
  (testing "Succeeds in verification when the target value is valid as ':catalog/appendices'."
    (are [target] (s/valid? :catalog/appendices target)
      ["appendices.md"]
      ["appendices1.md" "appendices2.md"]
      []))

  (testing "Fails in verification when the target value is invalid as ':catalog/appendices'."
    (are [target] (not (s/valid? :catalog/appendices target))
      [""]
      ["invalid/file-name"]
      [nil]
      ["appendices.md" ""]
      "not-vector"
      nil)))

(deftest catalog_back-matters-test
  (testing "Succeeds in verification when the target value is valid as ':catalog/back-matters'."
    (are [target] (s/valid? :catalog/back-matters target)
      ["back-matters.md"]
      ["back-matters1.md" "back-matters2.md"]
      []))

  (testing "Fails in verification when the target value is invalid as ':catalog/back-matters'."
    (are [target] (not (s/valid? :catalog/back-matters target))
      [""]
      ["invalid/file-name"]
      [nil]
      ["back-matters.md" ""]
      "not-vector"
      nil)))

(deftest catalog_chapters-test
  (testing "Succeeds in verification when the target value is valid as ':catalog/chapters'."
    (are [target] (s/valid? :catalog/chapters target)
      ["chapters.md"]
      ["chapters1.md" "chapters2.md"]
      []))

  (testing "Fails in verification when the target value is invalid as ':catalog/chapters'."
    (are [target] (not (s/valid? :catalog/chapters target))
      [""]
      ["invalid/file-name"]
      [nil]
      ["chapters.md" ""]
      "not-vector"
      nil)))

(deftest catalog_front-matters-test
  (testing "Succeeds in verification when the target value is valid as ':catalog/front-matters'."
    (are [target] (s/valid? :catalog/front-matters target)
      ["front-matters.md"]
      ["front-matters1.md" "front-matters2.md"]
      []))

  (testing "Fails in verification when the target value is invalid as ':catalog/front-matters'."
    (are [target] (not (s/valid? :catalog/front-matters target))
      [""]
      ["invalid/file-name"]
      [nil]
      ["front-matters.md" ""]
      "not-vector"
      nil)))

(deftest catalog-test
  (testing "Succeeds in verification when the target value is valid as '::catalog'."
    (are [target] (s/valid? ::spec/catalog target)
      {:front-matters ["front-matters.md"]
       :chapters ["chapters.md"]
       :appendices ["appendices.md"]
       :back-matters ["back-matters.md"]}
      {:front-matters [] :chapters [] :appendices [] :back-matters []}
      {:chapters ["chapters.md"]}
      {:chapters ["chapters.md"] :extra-key "extra-value"}))

  (testing "Fails in verification when the target value is invalid as '::catalog'."
    (are [target] (not (s/valid? ::spec/catalog target))
      {:chapters "not-vector"}
      {:chapters nil}
      {:front-matters ["front-matters.md"]
       :chapters ["chapters.md"]
       :appendices ["appendices.md"]
       :back-matters "not-vector"}
      {:extra-key "extra-value"}
      {}
      "not-map"
      nil)))

(deftest file-path-test
  (testing "Succeeds in verification when the target value is valid as '::file-path'."
    (are [target] (s/valid? ::spec/file-path target)
      "valid/file-path"
      "C:\\valid\\file-path"
      "日本語"))

  (testing "Fails in verification when the target value is invalid as '::file-path'."
    (are [target] (not (s/valid? ::spec/file-path target))
      "invalid*file-path"
      "invalid?file-path"
      "invalid\"file-path"
      "invalid>file-path"
      "invalid<file-path"
      "invalid|file-path"
      ""
      ["not-string"]
      nil)))

(deftest markdown-test
  (testing "Succeeds in verification when the target value is valid as '::markdown'."
    (is (s/valid? ::spec/markdown "# Markdown")))

  (testing "Fails in verification when the target value is invalid as '::markdown'."
    (are [target] (not (s/valid? ::spec/markdown target))
      ""
      ["not-string"]
      nil)))

(deftest filename-and-markdown-test
  (testing "Succeeds in verification when the target value is valid as '::filename-and-markdown'."
    (is (s/valid? ::spec/filename-and-markdown ["markdown.md" "# Markdown"])))

  (testing "Fails in verification when the target value is invalid as '::filename-and-markdown'."
    (are [target] (not (s/valid? ::spec/filename-and-markdown target))
      ["" "# Markdown"]
      ["markdown.md" ""]
      ["markdown.md"]
      ["markdown.md" "# Markdown" "invalid-value"]
      []
      "not-vector"
      nil)))

(deftest list-of-filename-and-markdown-test
  (testing "Succeeds in verification when the target value is valid as '::list-of-filename-and-markdown'."
    (are [target] (s/valid? ::spec/list-of-filename-and-markdown target)
      [["markdown.md" "# Markdown"]]
      [["markdown1.md" "# Markdown1"] ["markdown2.md" "# Markdown2"]]
      []))

  (testing "Fails in verification when the target value is invalid as '::list-of-filename-and-markdown'."
    (are [target] (not (s/valid? ::spec/list-of-filename-and-markdown target))
      [["" "# Markdown"]]
      [["markdown.md" ""]]
      [nil]
      [["markdown.md" "# Markdown"] ["" "# Markdown"]]
      "not-vector"
      nil)))
