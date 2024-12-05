import * as J from "../../../dist/src/java/tree";
import {JavaType} from "../../../dist/src/java";
import * as JS from "../../../dist/src/javascript";
import {connect, disconnect, rewriteRun, typeScript} from '../testHarness';

describe('arithmetic operator mapping', () => {
    beforeAll(() => connect());
    afterAll(() => disconnect());

    test('plus', () => {
        rewriteRun(
          //language=typescript
          typeScript(
            '1 + 2',
            cu => {
                const binary = <J.Binary>(<JS.ExpressionStatement>cu.statements[0]).expression;
                expect((<JavaType.Primitive>binary.type).kind).toBe(JavaType.PrimitiveKind.Double);
            }
          )
        );
    });
    test('concat', () => {
        rewriteRun(
          //language=typescript
          typeScript(
            '"1" + 2',
            cu => {
                const binary = <J.Binary>(<JS.ExpressionStatement>cu.statements[0]).expression;
                expect((<JavaType.Primitive>binary.type).kind).toBe(JavaType.PrimitiveKind.String);
            }
          )
        );
    });

    test('minus', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 - 2')
        );
    });

    test('multiply', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 * 2')
        );
    });

    test('divide', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 / 2')
        );
    });

    test('modulo', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 % 2')
        );
    });

    test('left shift', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 << 2')
        );
    });

    test('right shift', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 >> 2')
        );
    });

    test('unsigned right shift', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 >>> 2')
        );
    });
});

describe('bitwise operator mapping', () => {
    beforeAll(() => connect());
    afterAll(() => disconnect());

    test('and', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 & 2')
        );
    });
    test('or', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 | 2')
        );
    });
    test('xor', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 ^ 2')
        );
    });
});

describe('relational operator mapping', () => {
    beforeAll(() => connect());
    afterAll(() => disconnect());

    test('less than', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 < 2')
        );
    });
    test('less than or equal', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 <= 2')
        );
    });
    test('greater than', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 > 2')
        );
    });
    test('greater than or equal', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 >= 2')
        );
    });
    test('equal', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 == 2')
        );
    });
    test('not equal', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 != 2')
        );
    });

    test('strict equal', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 === 2')
        );
    });
    test('strict not equal', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 !== 2')
        );
    });
})

describe('boolean operator mapping', () => {
    beforeAll(() => connect());
    afterAll(() => disconnect());

    test('and', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 && 2')
        );
    });
    test('or', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 || 2')
        );
    });
    test('in', () => {
        rewriteRun(
          //language=typescript
          typeScript('1 in 2')
        );
    });
});
describe('comma operator mapping', () => {
    beforeAll(() => connect());
    afterAll(() => disconnect());

    test('comma operator', () => {
        rewriteRun(
          //language=typescript
          typeScript(`
              let x = 1;

              x = (x++, x);

              console.log(x);
              // Expected output: 2

              x = (2, 3);

              console.log(x);
              // Expected output: 3
          `),
            //language=typescript
          typeScript(`
              const a = Array.from({length: 10}, () =>
                  Array.from({length: 10}, Math.random),
              ); // A 10×10 array of random numbers

              for (let i = 0, j = 9; i <= 9; i++, j--) {
                  console.log(\`a[\${i}][\${j}] = \${a[i][j]}\`);
              }
          `),
            //language=typescript
          typeScript(`
              let a, b, c;

              a = b = 3, c = 4; // Returns 4
              console.log(a); // 3 (left-most)

              let x, y, z;

              x = (y = 5, z = 6); // Returns 6
              console.log(x); // 6 (right-most)
          `),
        );
    });
});
