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

(ns blue.lions.kurono.spec
  (:require [cljs.spec.alpha :as s]
            [clojure.string :as str]))

(s/def :common/non-blank-string
  (s/and string?
         #(not (str/blank? %))))

(defn- valid-string?
  [invalid-chars target]
  (not-any? invalid-chars (seq target)))

(s/def ::config-value
  (s/or :string string?
        :vector (s/coll-of string? :kind vector?)))

(s/def ::config
  (s/map-of keyword? ::config-value))

(s/def ::edn
  map?)

(def valid-file-name? (partial valid-string? #{"\\" "/" ":" "*" "?" "\"" ">" "<" "|"}))

(s/def ::file-name
  (s/and :common/non-blank-string
         valid-file-name?))

(s/def ::file-names
  (s/coll-of ::file-name :kind vector?))

(s/def :catalog/appendices
  (s/coll-of ::file-name :kind vector?))

(s/def :catalog/back-matters
  (s/coll-of ::file-name :kind vector?))

(s/def :catalog/chapters
  (s/coll-of ::file-name :kind vector?))

(s/def :catalog/front-matters
  (s/coll-of ::file-name :kind vector?))

(s/def ::catalog
  (s/and (s/keys :opt-un [:catalog/front-matters :catalog/chapters :catalog/appendices :catalog/back-matters])
         (fn [target]
           (some #(contains? target %)
                 [:front-matters :chapters :appendices :back-matters]))))

(def valid-file-path? (partial valid-string? #{"*" "?" "\"" ">" "<" "|"}))

(s/def ::file-path
  (s/and :common/non-blank-string
         valid-file-path?))

(s/def ::markdown
  :common/non-blank-string)

(s/def ::filename-and-markdown
  (s/tuple ::file-name ::markdown))

(s/def ::list-of-filename-and-markdown
  (s/coll-of ::filename-and-markdown :kind vector?))
