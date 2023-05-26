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

@SuppressWarnings("JSUnusedLocalSymbols")
class LiteralTest extends ParserTest {

    @Test
    void stringLiteral() {
        rewriteRun(
          javaScript(
            """
              let hello = 'World' ;
              """
          )
        );
    }

    @Test
    void numericLiteral() {
        rewriteRun(
          javaScript(
            """
              let n = 0 ;
              """
          )
        );
    }

    @Test
    void intentionallyBadUnicodeCharacter() {
        rewriteRun(
          javaScript(
            """
              let s1 = "\\\\u{U1}"
              let s2 = "\\\\u1234"
              let s3 = "\\\\u{00AUF}"
              """
          )
        );
    }

    @Test
    void unmatchedSurrogatePair() {
        rewriteRun(
          javaScript(
            """
              let c1 : Character = '\uD800'
              let c2 : Character = '\uDfFf'
              """
          )
        );
    }

    @Test
    void unmatchedSurrogatePairInString() {
        rewriteRun(
          javaScript(
            """
              let s1 : String = "\uD800"
              let s2 : String = "\uDfFf"
              """
          )
        );
    }
}
