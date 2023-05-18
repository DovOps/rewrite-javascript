/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.javascript.tree;

import org.junit.jupiter.api.Test;

@SuppressWarnings({"UnnecessaryContinueJS", "JSUnusedLocalSymbols"})
class ContinueTest extends ParserTest {

    @Test
    void continueStatement() {
        rewriteRun(
          javaScript(
            """
              for (let i = 0; i < 10; i++) {
                  if (i % 2)
                      continue ;
              }
              """
          )
        );
    }

    @Test
    void labeled() {
        rewriteRun(
          javaScript(
            """
              function test ( ) {
                  outer : for ( var i = 0 ; i < 3 ; i++ ) {
                      for ( var j = 0 ; j < 3 ; j++ ) {
                          if ( j === i ) {
                              continue outer ;
                          }
                      }
                  }
              }
              """
          )
        );
    }
}
