/*
   Copyright 2018 David Laurell

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.daverix.topologicalsorter;

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;

import java.util.List;

import javax.annotation.Nullable;

import static com.google.common.truth.Truth.assertAbout;

public final class PlacementSubject<T> extends Subject<PlacementSubject<T>, List<T>> {

    private PlacementSubject(FailureMetadata metadata, @Nullable List<T> actual) {
        super(metadata, actual);
    }

    public static <T> PlacementSubject assertThat(List<T> items) {
        Factory<PlacementSubject<T>,List<T>> factory = PlacementSubject::new;
        return assertAbout(factory).that(items);
    }

    public <TFirst extends T> Placement hasItem(TFirst first) {
        return new Placement<>(first);
    }

    public final class Placement<TFirst extends T> {
        private final TFirst first;

        private Placement(TFirst first) {
            this.first = first;
        }

        public <TSecond extends T> void placedBefore(TSecond second) {
            if(!isBefore(actual(), first, second)) {
                fail(String.format("has item %s placed before item %s", first, second));
            }
        }

        public <TSecond extends T> void placedAfter(TSecond second) {
            if(!isAfter(actual(), first, second)) {
                fail(String.format("has item %s placed after item %s", first, second));
            }
        }
    }

    private static <T> boolean isBefore(List<T> items, T first, T second) {
        int firstIndex = items.indexOf(first);
        int secondIndex = items.indexOf(second);

        if(firstIndex == -1)
            throw new IllegalStateException("first not found in items");

        if(secondIndex == -1)
            throw new IllegalStateException("second not found in items");

        return firstIndex < secondIndex;
    }

    private static <T> boolean isAfter(List<T> items, T first, T second) {
        int firstIndex = items.indexOf(first);
        int secondIndex = items.indexOf(second);

        if(firstIndex == -1)
            throw new IllegalStateException("first not found in items");

        if(secondIndex == -1)
            throw new IllegalStateException("second not found in items");

        return firstIndex > secondIndex;
    }
}
